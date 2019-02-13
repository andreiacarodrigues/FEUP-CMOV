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

import org.feup.cmov.aef.cmov1_app.EventAdapter;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.OrderAdapter;
import org.feup.cmov.aef.cmov1_app.ProductAdapter;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.entitites.Performance;
import org.feup.cmov.aef.cmov1_app.entitites.Product;

import java.util.ArrayList;

public class CafeteriaViewActivity extends AppCompatActivity {
    OrderAdapter itemsAdapter;
    private ProgressBar spinner;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafeteria_view);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Make Order");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        spinner = (ProgressBar)findViewById(R.id.progress_bar);
        content = (LinearLayout) findViewById(R.id.content);

        MyApplication application = (MyApplication)getApplicationContext();

        ArrayList<Product> products =  application.getProducts();
        ArrayList<Product> selected = new ArrayList<>();

        for (int x = 0; x < products.size(); x++){
            if(products.get(x).getQuantity() > 0)
                selected.add(products.get(x));
        }

        if(selected.size() == 0)
        {
            ((TextView)findViewById(R.id.product_label)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.quantity_label)).setVisibility(View.GONE);
        }
        itemsAdapter = new OrderAdapter(this, R.layout.product_row, selected);
        ListView listView = (ListView)findViewById(R.id.my_order_list);
        listView.setAdapter(itemsAdapter);

        Button p_button = (Button) findViewById(R.id.add_product);

        p_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productActivity = new Intent(v.getContext(), ProductViewActivity.class);
                v.getContext().startActivity(productActivity);
            }
        });

        Button n_button = (Button) findViewById(R.id.next_step);

        n_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(selected.size() == 0)
                {
                    Toast.makeText(v.getContext(),"Must choose at least 1 product to proceed.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent vouchersActivity = new Intent(v.getContext(), VouchersViewActivity.class);
                v.getContext().startActivity(vouchersActivity);
            }
        });

        spinner.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
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
