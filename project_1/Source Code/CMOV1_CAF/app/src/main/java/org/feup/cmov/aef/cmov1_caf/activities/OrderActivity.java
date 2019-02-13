package org.feup.cmov.aef.cmov1_caf.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Base64;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.feup.cmov.aef.cmov1_caf.Constants;
import org.feup.cmov.aef.cmov1_caf.R;
import org.feup.cmov.aef.cmov1_caf.entities.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.feup.cmov.aef.cmov1_caf.Utils;
import org.feup.cmov.aef.cmov1_caf.entities.Voucher;
import org.feup.cmov.aef.cmov1_caf.responses.PlaceOrderTaskResponse;

public class OrderActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ActionBar bar = getSupportActionBar();

        products.add(new Product(1, "Popcorn", "3.0", 0));
        products.add(new Product(2, "Coffee", "0.25", 0));
        products.add(new Product(3, "Soda", "2.0", 0));
        products.add(new Product(4, "Nachos", "1.0", 0));
        products.add(new Product(5, "Water", "1.0", 0));
        products.add(new Product(6, "Candy", "1.0", 0));

        Intent intent = getIntent();

        int orderId = intent.getIntExtra("orderId", -1);
        String[] acceptedVouchers = intent.getStringArrayExtra("acceptedVoucherTypes");
        float totalPrice = intent.getFloatExtra("totalPrice", -1);
        int[] productIds = intent.getIntArrayExtra("products");

        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Order ID: " + orderId);
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        Button back_btn = (Button) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(v.getContext(), MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);

            }
        });

        Integer entryValue;
        Map<Integer, Integer> products = new HashMap();

        for ( int i =0; i < productIds.length; i++)
        {
            entryValue = (Integer)products.get(productIds[i]);

            if (entryValue == null)
            {
                products.put(productIds[i], new Integer(1));
            }

            else
            {
                products.put(productIds[i], new Integer(entryValue.intValue()+1));
            }
        }

        TableLayout table = ((TableLayout)findViewById(R.id.product_list));

        for(int i = 0; i < products.size(); i++)
        {
            Integer myKey = (Integer)products.keySet().toArray()[i];
            Product myProduct = getProductWithId(myKey);
            Integer myValue = (Integer)products.get(myKey);

            TableRow tr = new TableRow(this);
            tr.setPadding(0,5,0,5);
            tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView name = new TextView(this);
            name.setText((i+ 1) + ". " + myProduct.getName());
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.roboto_light),
                    Typeface.NORMAL);
            name.setTypeface(myTypeface);
            name.setTextSize(18);
            name.setTextColor(this.getResources().getColor(R.color.grey));
            name.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            tr.addView(name);

            TextView preco = new TextView(this);

            if(myProduct.getPrice().equals("Free"))
                preco.setText(myValue + " x " + myProduct.getPrice());
            else
                preco.setText(myValue + " x " + myProduct.getPrice() + "€" );
            preco.setTypeface(myTypeface);
            preco.setGravity(Gravity.RIGHT);
            preco.setTextSize(18);
            preco.setTextColor(this.getResources().getColor(R.color.grey));
            preco.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            tr.addView(preco);

            table.addView(tr);
        }

        TextView voucher1 = ((TextView)findViewById(R.id.voucher1));
        TextView voucher2 = ((TextView)findViewById(R.id.voucher2));

        if(acceptedVouchers.length > 0)
        {
            if(acceptedVouchers[0].equals("FreeCoffee"))
                voucher1.setText("Free Coffee");
            else if (acceptedVouchers[0].equals("FreePopcorn"))
                voucher1.setText("Free Popcorn");
            else
                voucher1.setText("5% Discount");

            if(acceptedVouchers.length == 2)
            {
                if(acceptedVouchers[1].equals("FreeCoffee"))
                    voucher2.setText("Free Coffee");
                else if (acceptedVouchers[1].equals("FreePopcorn"))
                    voucher2.setText("Free Popcorn");
                else
                    voucher2.setText("5% Discount");

            }
        }

        TextView total_price = ((TextView)findViewById(R.id.total_price));
        if(Float.compare(totalPrice, 0) <= 0)
            total_price.setText("Free");
        else
            total_price.setText(totalPrice + "€");

        // Transmit back to Client
        /*PlaceOrderTaskResponse placeOrderTaskResponse = new PlaceOrderTaskResponse(orderId, acceptedVouchers, totalPrice);

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(placeOrderTaskResponse, 0);
        byte[] data = parcel.marshall();
        parcel.recycle();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter != null)
        {
            NdefRecord ndefRecord = Utils.createMimeRecord("application/nfc.feup.cmov.aef.caforderresponse", data);
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
        int primaryColor = getResources().getColor(R.color.colorAccent);
        int secondaryColor = getResources().getColor(R.color.colorPrimary);
        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? primaryColor : secondaryColor;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);*/
    }

    private Product getProductWithId(int id)
    {
        for(int j = 0; j < products.size(); j++)
        {
            if(products.get(j).id == id)
            {
                return products.get(j);
            }
        }
        return null;
    }

    /*public void onNdefPushComplete(NfcEvent event)
    {
        runOnUiThread(() ->
        {
            Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }*/

}
