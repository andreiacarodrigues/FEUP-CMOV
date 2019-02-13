package org.feup.cmov.aef.cmov1_tic.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.cmov.aef.cmov1_tic.Constants;
import org.feup.cmov.aef.cmov1_tic.R;
import org.feup.cmov.aef.cmov1_tic.Utils;
import org.feup.cmov.aef.cmov1_tic.requests.ValidateTicketRequest;
import org.feup.cmov.aef.cmov1_tic.responses.ValidateTicketResponse;
import org.feup.cmov.aef.cmov1_tic.tasks.ValidateTicketTask;

public class MainActivity extends AppCompatActivity implements ValidateTicketTask.ICallback
{

    ValidateTicketRequest validateTicketRequest;

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
            tv.setText("Ticket Validation");
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
            processValTicketRequest((NdefMessage)(getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0]));
            setIntent(new Intent());
        }
    }

    public void processValTicketRequest(NdefMessage message)
    {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        findViewById(R.id.content).setVisibility(View.GONE);

        byte[] data = message.getRecords()[0].getPayload();
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);
        parcel.readString();
        validateTicketRequest = new ValidateTicketRequest(parcel);
        //this.validateTicketRequest = new ValidateTicketRequest(validateTicketRequest.userId, validateTicketRequest.numberOfTickets, validateTicketRequest.ticketIds, validateTicketRequest.ticketSeatNumbers, validateTicketRequest.performanceName, validateTicketRequest.performanceDate);
        parcel.recycle();
        new ValidateTicketTask(this).execute(validateTicketRequest);
    }

    @Override
    public void onPostValidateTicketTask(ValidateTicketResponse result)
    {
        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.content).setVisibility(View.VISIBLE);

        switch(result.response)
        {
            case Success:
                Intent intent = new Intent(this, TicketActivity.class);
                intent.putExtra("request", validateTicketRequest);
                intent.putExtra("response", result);
                startActivity(intent);
                break;
            case UserNotFound:
                Toast.makeText(getApplicationContext(), "User was not found.", Toast.LENGTH_LONG).show();
                break;
            case CanOnlyValidateUpTo4TicketsAtOnce:
                Toast.makeText(getApplicationContext(), "Only 4 tickets can be validated at the same time.", Toast.LENGTH_LONG).show();
                break;
            case CouldNotFindTicket:
                Toast.makeText(getApplicationContext(), "Could not to find ticket.", Toast.LENGTH_LONG).show();
                break;
            case CouldNotFindPurchase:
                Toast.makeText(getApplicationContext(), "Could not find purchase.", Toast.LENGTH_LONG).show();
                break;
            case TicketsAreNotAllForTheSamePerformance:
                Toast.makeText(getApplicationContext(), "The tickets to be validated have to be for the same performance.", Toast.LENGTH_LONG).show();
                break;
            case NotTicketOwner:
                Toast.makeText(getApplicationContext(), "User is not the ticket owner.", Toast.LENGTH_LONG).show();
                break;
            case UnknownError:
                Toast.makeText(getApplicationContext(), "Unknown error has occured. Please try again later.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
