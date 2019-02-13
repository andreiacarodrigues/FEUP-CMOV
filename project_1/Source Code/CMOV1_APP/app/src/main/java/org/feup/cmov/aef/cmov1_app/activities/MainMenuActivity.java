package org.feup.cmov.aef.cmov1_app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;

import org.feup.cmov.aef.cmov1_app.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            bar.setTitle("");
        }

        MaterialCardView events = findViewById(R.id.events_menu);
        MaterialCardView tickets = findViewById(R.id.tickets_menu);
        MaterialCardView cafeteria = findViewById(R.id.cafeteria_menu);
        MaterialCardView vouchers = findViewById(R.id.vouchers_menu);
        MaterialCardView val_tickets = findViewById(R.id.val_tickets_menu);
        MaterialCardView transactions = findViewById(R.id.transactions);
        menuOnClickListener(events, EventsViewActivity.class);
        menuOnClickListener(tickets, TicketsViewActivity.class);
        menuOnClickListener(cafeteria, CafeteriaViewActivity.class);
        menuOnClickListener(vouchers, VouchersListActivity.class);
        menuOnClickListener(val_tickets, ValTicketsViewActivity.class);
        menuOnClickListener(transactions, TransactionsActivity.class);
    }

    public void menuOnClickListener(MaterialCardView button, Class activity){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(v.getContext(), activity);
                v.getContext().startActivity(newActivity);
            }
        });
    }
}
