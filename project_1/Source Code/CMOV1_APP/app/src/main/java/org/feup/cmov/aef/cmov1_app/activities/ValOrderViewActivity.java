package org.feup.cmov.aef.cmov1_app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;
import org.feup.cmov.aef.cmov1_app.models.CafeteriaOrderParcel;
import org.feup.cmov.aef.cmov1_app.responses.PlaceOrderTaskResponse;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class ValOrderViewActivity extends AppCompatActivity
{
    ImageView qrCodeView;
    CafeteriaOrderParcel cafeteriaOrderParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_val_order_view);

        qrCodeView = findViewById(R.id.qrCode);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Validate Order");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        MyApplication application = (MyApplication)getApplicationContext();
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        cafeteriaOrderParcel = new CafeteriaOrderParcel();
        cafeteriaOrderParcel.userId = MyApplication.getLoadSaveManager().getData().UserId;
        cafeteriaOrderParcel.setProductIds(application.getSelectedProducts().toArray(new Product[0]));
        cafeteriaOrderParcel.setVoucherIds(application.getSelectedVouchers().toArray(new Voucher[0]));
        String userId = MyApplication.getLoadSaveManager().getData().UserId;
        String[] voucherIds = cafeteriaOrderParcel.voucherIds;
        int[] productIds = cafeteriaOrderParcel.productIds;
        byte[] userIdByteArray = userId.getBytes(StandardCharsets.UTF_8);
        ArrayList<byte[]> vouchersByteArray = new ArrayList<>();
        int sizeOfBuffer = userIdByteArray.length;
        for(String voucherId : voucherIds)
        {
            byte[] voucherArray = voucherId.getBytes(StandardCharsets.UTF_8);
            vouchersByteArray.add(voucherArray);
            sizeOfBuffer += voucherArray.length;
        }
        sizeOfBuffer += productIds.length * Constants.SIZE_OF_INT;
        ByteBuffer byteBuffer = ByteBuffer.allocate(sizeOfBuffer);
        for(byte[] array : vouchersByteArray)
        {
            byteBuffer.put(array);
        }
        for(int value : productIds)
        {
            byteBuffer.putInt(value);
        }
        byteBuffer.put(userIdByteArray);
        byte[] dataBuffer = byteBuffer.array();
        byte[] signature = Utils.buildMessage(dataBuffer);
        byte[] encodedSignature = Base64.encode(signature, Base64.DEFAULT);
        cafeteriaOrderParcel.signature = new String(encodedSignature);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(cafeteriaOrderParcel, 0);
        byte[] data = parcel.marshall();
        parcel.recycle();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter != null)
        {
            NdefRecord ndefRecord = Utils.createMimeRecord("application/nfc.feup.cmov.aef.caforder", data);
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
            nfcAdapter.setNdefPushMessage(ndefMessage, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this::onNdefPushComplete, this);
        }

        byte[] encodedData = Base64.encode(data, Base64.DEFAULT);
        String dataString = new String(encodedData);
        BitMatrix bitMatrix;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(dataString, BarcodeFormat.QR_CODE, Constants.QR_CODE_DIMENSION, Constants.QR_CODE_DIMENSION);
        } catch (WriterException e)
        {
            e.printStackTrace();
            return;
        }
        int primaryColor = getResources().getColor(R.color.colorPrimary);
        int secondaryColor = getResources().getColor(R.color.white);
        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? primaryColor : secondaryColor;
            }
        }
      /*  Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        qrCodeView.setImageBitmap(bitmap);*/
    }

    public void onNdefPushComplete(NfcEvent event)
    {
        runOnUiThread(() ->
        {
            ((MyApplication)getApplicationContext()).clearOrder();
            List<String> vouchersSent = Arrays.asList(cafeteriaOrderParcel.voucherIds);
            Iterator<Voucher> iterator = MyApplication.getLoadSaveManager().getData().Vouchers.iterator();
            while(iterator.hasNext())
            {
                Voucher voucher = iterator.next();
                if(vouchersSent.contains(voucher.id))
                {
                    iterator.remove();
                }
            }
            MyApplication.getLoadSaveManager().Save(MyApplication.getLoadSaveManager().getData());
            Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_LONG).show();
            Intent mainActivity = new Intent(this, MainMenuActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
        });
    }
}
