package org.feup.cmov.aef.cmov1_tic.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.cmov.aef.cmov1_tic.Constants;
import org.feup.cmov.aef.cmov1_tic.R;
import org.feup.cmov.aef.cmov1_tic.TicketAdapter;
import org.feup.cmov.aef.cmov1_tic.Utils;
import org.feup.cmov.aef.cmov1_tic.entities.Ticket;
import org.feup.cmov.aef.cmov1_tic.requests.ValidateTicketRequest;
import org.feup.cmov.aef.cmov1_tic.responses.ValidateTicketResponse;
import org.feup.cmov.aef.cmov1_tic.tasks.ValidateTicketTask;

import java.util.ArrayList;
import java.util.Arrays;

public class TicketActivity extends AppCompatActivity
{

    TicketAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ActionBar bar = getSupportActionBar();

        ValidateTicketRequest request = getIntent().getParcelableExtra("request");
        ValidateTicketResponse response = getIntent().getParcelableExtra("response");

        ArrayList<Ticket> items = new ArrayList<>();
        ArrayList<String> responseIds = new ArrayList<>(Arrays.asList(response.ticketIds));

        for(int i = 0; i < request.numberOfTickets; i++)
        {
            items.add(new Ticket(request.ticketIds[i], request.performanceName, request.performanceDate, request.ticketSeatNumbers[i], responseIds.contains(request.ticketIds[i])));
        }


        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText(items.get(0).getPerformanceName());
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        // Aqui ele já sabe que sao todos do mesmo evento
        //  ((TextView)findViewById(R.id.event_name)).setText(items.get(0).getPerformanceName());
        String date = items.get(0).getPerformanceDate().replace("T", " ");
        date = date.substring(0, date.length() - 3);
        ((TextView)findViewById(R.id.event_date)).setText(date);

        itemsAdapter = new TicketAdapter(this, R.layout.ticket_row, items);
        ListView listView = (ListView)findViewById(R.id.tickets_list);
        listView.setAdapter(itemsAdapter);

        Button back_btn = (Button) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(v.getContext(), MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);

            }
        });

/*        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(response, 0);
        byte[] data = parcel.marshall();
        parcel.recycle();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter != null)
        {
            NdefRecord ndefRecord = Utils.createMimeRecord("application/nfc.feup.cmov.aef.valticketresponse", data);
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

/*    public void onNdefPushComplete(NfcEvent event)
    {
        runOnUiThread(() ->
        {
            Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_LONG).show();
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
        });
    }*/
}
