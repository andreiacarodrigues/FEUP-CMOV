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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.ProductAdapter;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.VoucherAdapter;
import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

import java.util.ArrayList;
import java.util.Arrays;

public class VouchersViewActivity extends AppCompatActivity {

    VoucherAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_view);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Add Vouchers");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        MyApplication application = (MyApplication)getApplicationContext();

        ArrayList<Voucher> vouchers = MyApplication.getLoadSaveManager().getData().Vouchers;
        for(Voucher voucher : vouchers)
        {
            voucher.selected = false;
        }

        itemsAdapter = new VoucherAdapter(this, R.layout.voucher_check_row, vouchers, "checkbox");
        ListView listView = findViewById(R.id.vouchers_list);
        listView.setAdapter(itemsAdapter);

        Button v_button = findViewById(R.id.complete);

        v_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Voucher> selected =  new ArrayList<>();
                boolean hasFivePercent = false;

                CheckBox cb;
                for (Voucher voucher : vouchers){
                    if(voucher.selected){
                        if(voucher.getType().equals("5Cafeteria")) {
                            if (hasFivePercent) {
                                Toast.makeText(v.getContext(), "Only 1 one 5% discount voucher can be used.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            hasFivePercent = true;
                        }
                        selected.add(voucher);
                    }
                }

                if(selected.size() > 2)
                {
                    Toast.makeText(v.getContext(),"Can't accept more than 2 vouchers on the same order.", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<Product> selectedProducts = application.getSelectedProducts();
                int hasCoffee = 0;
                int hasPopcorn = 0;
                int foundCoffee = 0;
                int foundPopcorn = 0;

                for(int j = 0; j < selected.size(); j++)
                {
                    Voucher sel = selected.get(j);
                    if(sel.getType().equals("FreeCoffee"))
                        hasCoffee++;

                    if(sel.getType().equals("FreePopcorn"))
                        hasPopcorn++;
                }

                for(int k = 0; k < selectedProducts.size(); k++)
                {
                    if(selectedProducts.get(k).getName().equals("Coffee"))
                        foundCoffee += selectedProducts.get(k).quantity;

                    if(selectedProducts.get(k).getName().equals("Popcorn"))
                        foundPopcorn += selectedProducts.get(k).quantity;
                }

                if(hasCoffee > foundCoffee || hasPopcorn > foundPopcorn)
                {
                    Toast.makeText(v.getContext(),"A free product voucher needs a respective product in the order.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent orderActivity = new Intent(v.getContext(), ValOrderViewActivity.class);
                MyApplication.getLoadSaveManager().getData().Vouchers.removeAll(selected);
                application.setVouchers(selected);
                v.getContext().startActivity(orderActivity);
            }
        });
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.content).setVisibility(View.VISIBLE);
    }
}
