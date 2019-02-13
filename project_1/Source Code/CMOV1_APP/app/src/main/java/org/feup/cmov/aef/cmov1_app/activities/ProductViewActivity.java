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

import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.ProductAdapter;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.entitites.Product;

import java.util.ArrayList;

public class ProductViewActivity extends AppCompatActivity {

    ProductAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Add Products");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        MyApplication application = (MyApplication)getApplicationContext();

        ArrayList<Product> products =  application.getProducts();

        itemsAdapter = new ProductAdapter(this, R.layout.product_check_row, products);
        ListView listView = (ListView)findViewById(R.id.product_list);
        listView.setAdapter(itemsAdapter);

        Button p_button = (Button) findViewById(R.id.add_products);

        p_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb;
                for (int x = 0; x < listView.getChildCount();x++){
                    cb = (CheckBox)listView.getChildAt(x).findViewById(R.id.voucher_name);
                    if(cb.isChecked()){
                        if(products.get(x).getQuantity() < 1)
                            application.setProduct(x, 1);
                    }else{
                        if(products.get(x).getQuantity() > 0)
                            application.setProduct(x, 0);
                    }
                }

                Intent orderActivity = new Intent(v.getContext(), CafeteriaViewActivity.class);
                v.getContext().startActivity(orderActivity);
            }
        });
    }
}
