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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.OrderTransactionAdapter;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.TicketTransactionAdapter;
import org.feup.cmov.aef.cmov1_app.entitites.Order;
import org.feup.cmov.aef.cmov1_app.entitites.OrderTransaction;
import org.feup.cmov.aef.cmov1_app.entitites.Performance;
import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Purchase;
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.TicketTransaction;
import org.feup.cmov.aef.cmov1_app.responses.ConsultTransactionsResponse;
import org.feup.cmov.aef.cmov1_app.tasks.ConsultTransactionsTask;

import java.util.ArrayList;
import java.util.Arrays;

public class TransactionsActivity extends AppCompatActivity implements ConsultTransactionsTask.ICallback {

    private ListView tickets_list;
    private ListView orders_list;

    TicketTransactionAdapter ticketsAdapter;
    OrderTransactionAdapter ordersAdapter;

    public class TabListener implements TabLayout.BaseOnTabSelectedListener
    {
        ListView tickets_list = (ListView)findViewById(R.id.tickets_list);
        ListView orders_list = (ListView)findViewById(R.id.orders_list);

        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            switch(tab.getPosition())
            {
                case 0:
                    tickets_list.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    orders_list.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab)
        {
            switch(tab.getPosition())
            {
                case 0:
                    tickets_list.setVisibility(View.GONE);
                    break;
                case 1:
                    orders_list.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab)
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        new ConsultTransactionsTask(this).execute();

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Transactions");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        MyApplication application = (MyApplication)getApplicationContext();

        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.tab).setVisibility(View.GONE);
        findViewById(R.id.tickets_list).setVisibility(View.GONE);
        findViewById(R.id.orders_list).setVisibility(View.GONE);

        ArrayList<TicketTransaction> tickets = new ArrayList<>();

        ArrayList<OrderTransaction> orders = new ArrayList<>();

        ticketsAdapter = new TicketTransactionAdapter(this, R.layout.ticket_transaction_row, tickets);
        tickets_list = findViewById(R.id.tickets_list);
        tickets_list.setAdapter(ticketsAdapter);

        ordersAdapter = new OrderTransactionAdapter(this, R.layout.order_transaction_row, orders);
        orders_list = findViewById(R.id.orders_list);
        orders_list.setAdapter(ordersAdapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.addOnTabSelectedListener(new TabListener());
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

    @Override
    public void onPostConsultTransactionsTask(ConsultTransactionsResponse response)
    {
        Intent intent = new Intent(this, MainMenuActivity.class);

        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.tab).setVisibility(View.VISIBLE);
        findViewById(R.id.tickets_list).setVisibility(View.VISIBLE);

        switch(response.response)
        {
            case Success:
                ArrayList<TicketTransaction> tickets = new ArrayList<>();

                for (Purchase ticket : response.purchases) {
                    tickets.add(new TicketTransaction(ticket.id,
                            new Performance(ticket.performanceId, ticket.performanceName, ticket.performanceDate, ticket.paidValue + ""), ticket.ticketAmount));
                }

                ArrayList<OrderTransaction> orderTransactions = new ArrayList<>();

                for (Order order : response.orders) {
                    orderTransactions.add(new OrderTransaction(order.id, new ArrayList<Product>(Arrays.asList(order.ordersProducts)),
                            new ArrayList<String>(Arrays.asList(order.acceptedVoucherTypes)), order.paidValue + ""));
                }

                ticketsAdapter.addAll(tickets);
                ordersAdapter.addAll(orderTransactions);
                break;
            case UserNotFound:
                Toast.makeText(getApplicationContext(), "There was an error on the server and the user was not found.", Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case UnknownError:
                Toast.makeText(getApplicationContext(), "There was an unknown error on the server, please try again", Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }


    }
}
