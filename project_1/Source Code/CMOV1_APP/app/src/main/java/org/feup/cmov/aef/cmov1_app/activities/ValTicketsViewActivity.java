package org.feup.cmov.aef.cmov1_app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.TicketAdapter;
import org.feup.cmov.aef.cmov1_app.TicketValidationAdapter;
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;

import java.util.ArrayList;

public class ValTicketsViewActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_val_tickets_view);

        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Select Tickets to Validate");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        spinner = findViewById(R.id.progress_bar);
        content = findViewById(R.id.content);

        MyApplication application = (MyApplication)getApplicationContext();

        ArrayList<Ticket> items = new ArrayList<>();

        LoadSaveData data = MyApplication.getLoadSaveManager().getData();
        for(Ticket ticket : data.Tickets)
        {
            if(!ticket.Used)
            {
                ticket.selected = false;
                items.add(ticket);
            }
        }

        TicketValidationAdapter itemsAdapter = new TicketValidationAdapter(this, R.layout.ticket_check_row, items);
        ListView listView = findViewById(R.id.tickets_list);
        listView.setAdapter(itemsAdapter);

        spinner.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);

        Button t_button = (Button) findViewById(R.id.complete);

        t_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Ticket> selected =  new ArrayList<Ticket>();

                CheckBox cb;
                for (Ticket ticket : items){
                    if(ticket.selected){
                        selected.add(ticket);
                    }
                }

                if(selected.size() <= 0)
                {
                    Toast.makeText(v.getContext(),"1 ticket required to validate.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(selected.size() > 4)
                {
                    Toast.makeText(v.getContext(),"Can't validate more than 4 tickets at the same time.", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    int n = selected.get(0).performanceId;
                    for(int i = 0; i< selected.size(); i++)
                    {
                        if(n != selected.get(i).performanceId){
                            Toast.makeText(v.getContext(),"Tickets are for different performances!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }


                    application.setTickets(selected);

                    Intent valTickets = new Intent(v.getContext(), ValTicketsActivity.class);
                    v.getContext().startActivity(valTickets);

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_home) {
            Intent mainActivity = new Intent(this, MainMenuActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
