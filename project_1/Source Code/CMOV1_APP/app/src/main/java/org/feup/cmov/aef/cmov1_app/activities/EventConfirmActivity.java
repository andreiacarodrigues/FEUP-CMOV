package org.feup.cmov.aef.cmov1_app.activities;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import org.feup.cmov.aef.cmov1_app.Constants;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.models.PurchaseTicketParameter;
import org.feup.cmov.aef.cmov1_app.tasks.PurchaseTicketTask;

import java.util.ArrayList;

public class EventConfirmActivity extends AppCompatActivity implements PurchaseTicketTask.ICallback
{

    private ProgressBar spinner;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_confirm);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Confirm Purchase");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        spinner = (ProgressBar)findViewById(R.id.progress_bar);
        content = (LinearLayout) findViewById(R.id.content);

        Intent intent = getIntent();
        ArrayList<String> e_info = intent.getStringArrayListExtra(Constants.EVENT_INFO); // ID - Nome - Data - Preço - Tickets

        // Event Info
        TextView e_name = (TextView) findViewById(R.id.event_name);
        TextView e_date = (TextView) findViewById(R.id.event_date);
        TextView e_price = (TextView) findViewById(R.id.event_price);
        TextView e_tickets = (TextView) findViewById(R.id.event_tickets);

        e_name.setText(e_info.get(1));
        e_date.setText(e_info.get(2));
        e_price.setText(e_info.get(3));
        e_tickets.setText(e_info.get(4));

        // User Info
        TextView nif_info = (TextView) findViewById(R.id.nif_info);
        TextView card_type_info = (TextView) findViewById(R.id.card_type_info);
        TextView card_number_info = (TextView) findViewById(R.id.card_number_info);
        TextView card_validity_info = (TextView) findViewById(R.id.card_validity_info);

        LoadSaveData data = MyApplication.getLoadSaveManager().getData();
        nif_info.setText(data.NIF);
        card_type_info.setText(data.CreditCardType);
        card_number_info.setText(data.CreditCardNumber);
        card_validity_info.setText(data.CreditCardExpiration);

        Button confirm_btn = (Button) findViewById(R.id.confirm_button);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View textEntryView = inflater.inflate(R.layout.modal_login_confirm, null);
                builder.setView(textEntryView)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // é para ficar vazio aqui - a função que trata disto está em baixo
                                // porque isto dá dismiss do modal se for feito aqui
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                final Dialog dialog = builder.create();
                dialog.show();
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText password = textEntryView.findViewById(R.id.confirm_password);
                        String passwordString = password.getText().toString();
                        LoadSaveData data = MyApplication.getLoadSaveManager().getData();
                        if(passwordString.isEmpty())
                        {
                            password.setError("Please fill the password before submiting");
                        }
                        else if(!passwordString.equals(data.Password))
                        {
                            password.setError("Password incorrect!");
                        }
                        else
                        {
                            findViewById(R.id.loading).setVisibility(View.VISIBLE);
                            int n_tickets = Integer.parseInt(e_info.get(4));
                            int performanceId = Integer.parseInt(e_info.get(0));

                            PurchaseTicketParameter purchaseTicketParameter = new PurchaseTicketParameter(performanceId, n_tickets);
                            new PurchaseTicketTask(EventConfirmActivity.this).execute(purchaseTicketParameter);

                            dialog.dismiss();

                        }
                    }
                });
            }
        });

        spinner.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostPurchaseTicketTask(PurchaseTicketTask.PurchaseTicketTaskResult result)
    {
        findViewById(R.id.loading).setVisibility(View.GONE);
        Intent mainActivity = new Intent(this, MainMenuActivity.class);

        switch(result)
        {
            case Success:
                Toast.makeText(this,"Ticket bought successfully", Toast.LENGTH_LONG).show();
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                break;
            case UserNotFound:
                Toast.makeText(this,"There was an error in the server and the user was not found.", Toast.LENGTH_LONG).show();
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                break;
            case PerformanceNotFound:
                Toast.makeText(this,"There was an error in the server and the performance was not found.", Toast.LENGTH_LONG).show();
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                break;
            case UnknownError:
                Toast.makeText(this,"There was an unknown error in the server. Please try again later.", Toast.LENGTH_LONG).show();
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                break;
        }
    }
}
