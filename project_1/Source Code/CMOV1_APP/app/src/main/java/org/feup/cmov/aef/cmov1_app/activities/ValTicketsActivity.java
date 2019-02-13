package org.feup.cmov.aef.cmov1_app.activities;

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
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.requests.ValidateTicketRequest;
import org.feup.cmov.aef.cmov1_app.responses.ValidateTicketResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValTicketsActivity extends AppCompatActivity {

    ValidateTicketRequest validateTicketRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_val_tickets);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Validate Tickets");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }
        String userId = MyApplication.getLoadSaveManager().getData().UserId;
        MyApplication application = (MyApplication)getApplicationContext();
        ArrayList<Ticket> tickets = application.getSelectedTickets();
        int numberOfTickets = tickets.size();
        ArrayList<String> ticketIdsArrayList = new ArrayList<>();
        ArrayList<Integer> ticketSeatNumbersArrayList = new ArrayList<>();
        for(Ticket ticket : tickets)
        {
            ticketIdsArrayList.add(ticket.id);
            ticketSeatNumbersArrayList.add(ticket.placeInRoom);
        }
        String[] ticketIdsArray = ticketIdsArrayList.toArray(new String[0]);
        int[] ticketSeatNumbersArray = new int[ticketIdsArrayList.size()];
        for(int i = 0; i < ticketIdsArrayList.size(); i++)
        {
            ticketSeatNumbersArray[i] = ticketSeatNumbersArrayList.get(i);
        }
        String date = "";
        String name = "";
        for(Ticket ticket : tickets)
        {
            if(date.equals(""))
            {
                date = ticket.performanceDate;
                name = ticket.performanceName;
                continue;
            }
            if(!date.equals(ticket.performanceDate))
            {
                Intent intent = new Intent(this, ValTicketsViewActivity.class);
                Toast.makeText(getApplicationContext(), "You can't choose 2 different performances.", Toast.LENGTH_LONG).show();
                startActivity(intent);
                return;
            }
        }
        validateTicketRequest = new ValidateTicketRequest(userId, numberOfTickets, ticketIdsArray, ticketSeatNumbersArray, name, date);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(validateTicketRequest, 0);
        byte[] data = parcel.marshall();
        parcel.recycle();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter != null)
        {
            NdefRecord ndefRecord = Utils.createMimeRecord("application/nfc.feup.cmov.aef.valticket", data);
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
            nfcAdapter.setNdefPushMessage(ndefMessage, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this::onNdefPushComplete, this);
        }

        /*byte[] encodedData = Base64.encode(data, Base64.DEFAULT);
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
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);*/
        //TODO <ImageView>.setImageBitmap(bitmap);
    }

    /*@Override
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
            processValTicketResponse((NdefMessage)(getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0]));
        }
    }

    public void processValTicketResponse(NdefMessage message)
    {
        byte[] data = message.getRecords()[0].getPayload();
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, data.length);
        ValidateTicketResponse validateTicketResponse = new ValidateTicketResponse(parcel);
        parcel.recycle();
        ArrayList<Ticket> tickets = MyApplication.getLoadSaveManager().getData().Tickets;
        for(String ticketId : validateTicketResponse.validatedTicketIds)
        {
            for(Ticket ticket : tickets)
            {
                if(ticket.id.equals(ticketId))
                {
                    ticket.Used = true;
                }
            }
        }
        runOnUiThread(() ->
        {
            Toast.makeText(getApplicationContext(), "Tickets validated", Toast.LENGTH_LONG).show();
            Intent mainActivity = new Intent(this, MainMenuActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
        });
    }*/

    public void onNdefPushComplete(NfcEvent event)
    {
        runOnUiThread(() ->
        {
            List<String> validatedIds = Arrays.asList(validateTicketRequest.ticketIds);
            for(Ticket ticket : MyApplication.getLoadSaveManager().getData().Tickets)
            {
                if(validatedIds.contains(ticket.id))
                {
                    ticket.Used = true;
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
