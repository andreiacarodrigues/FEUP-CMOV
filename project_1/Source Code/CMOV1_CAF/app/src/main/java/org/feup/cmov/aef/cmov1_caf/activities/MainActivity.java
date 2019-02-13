package org.feup.cmov.aef.cmov1_caf.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.aef.cmov1_caf.R;
import org.feup.cmov.aef.cmov1_caf.models.CafeteriaOrderParcel;
import org.feup.cmov.aef.cmov1_caf.responses.PlaceOrderTaskResponse;
import org.feup.cmov.aef.cmov1_caf.tasks.PlaceOrderTask;

public class MainActivity extends AppCompatActivity implements PlaceOrderTask.ICallback
{

    int[] productIds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Order Validation");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

    }

    @Override
    public void onNewIntent(Intent intent)
    {
        setIntent(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
        {
            processOrderRequest((NdefMessage)(getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0]));
            setIntent(new Intent());
        }
    }

    public void processOrderRequest(NdefMessage message)
    {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        findViewById(R.id.content).setVisibility(View.GONE);

        byte[] data = message.getRecords()[0].getPayload();
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);
        parcel.readString();
        CafeteriaOrderParcel cafeteriaOrderParcel = new CafeteriaOrderParcel(parcel);
        parcel.recycle();
        productIds = cafeteriaOrderParcel.productIds;
        new PlaceOrderTask(this).execute(cafeteriaOrderParcel);
    }

    @Override
    public void onPostPlaceOrderTask(PlaceOrderTaskResponse result)
    {
        //TODO

        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.content).setVisibility(View.VISIBLE);

        switch(result.response)
        {
            case Success:
                String[] acceptedVoucherTypes = new String[result.acceptedVouchers.length];
                for(int i = 0; i < acceptedVoucherTypes.length; i++)
                {
                    acceptedVoucherTypes[i] = result.acceptedVouchers[i].type;
                }
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("orderId", result.orderId);
                intent.putExtra("acceptedVoucherTypes", acceptedVoucherTypes);
                intent.putExtra("totalPrice", result.totalPrice);
                intent.putExtra("products", productIds);
                startActivity(intent);
                break;
            case InvalidSignature:
                Toast.makeText(getApplicationContext(), "Invalid signature.", Toast.LENGTH_LONG).show();
                break;
            case MustChooseProducts:
                Toast.makeText(getApplicationContext(), "No products were sent in the order.", Toast.LENGTH_LONG).show();
                break;
            case CanOnlyUsedAtMost2Vouchers:
                Toast.makeText(getApplicationContext(), "Only 2 vouchers can be used per order.", Toast.LENGTH_LONG).show();
                break;
            case UserDoesNotOwnVoucher:
                Toast.makeText(getApplicationContext(), "User doesn't own voucher included in the order.", Toast.LENGTH_LONG).show();
                break;
            case AlreadyUsedVoucherSent:
                Toast.makeText(getApplicationContext(), "Voucher included was already used", Toast.LENGTH_LONG).show();
                break;
            case OnlyOne5DiscountCanBeUsedPerOrder:
                Toast.makeText(getApplicationContext(), "Only 1 5% discount voucher can be used per order.", Toast.LENGTH_LONG).show();
                break;
            case UserNotFound:
                Toast.makeText(getApplicationContext(), "User not found.", Toast.LENGTH_LONG).show();
                break;
            case VoucherNotFound:
                Toast.makeText(getApplicationContext(), "1 or more vouchers included were not found.", Toast.LENGTH_LONG).show();
                break;
            case ProductNotFound:
                Toast.makeText(getApplicationContext(), "1 or more products were not found in the cafeteria menu.", Toast.LENGTH_LONG).show();
                break;
            case UnknownError:
                Toast.makeText(getApplicationContext(), "An unknown error has occurred. Please try again later", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
