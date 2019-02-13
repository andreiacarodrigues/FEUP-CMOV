package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.Product;

import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends ArrayAdapter<Product> {
    public Context context;
    public ArrayList<Product> products;

    public OrderAdapter(Context context, int resourceId, ArrayList<Product> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.products = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.product_row, parent, false);
        }

        Product p = this.products.get(position);
        ((TextView)row.findViewById(R.id.voucher_name)).setText(p.getName());
        if(p.getPrice().equals("Free"))
            ((TextView)row.findViewById(R.id.product_price)).setText("Free");
        else
            ((TextView)row.findViewById(R.id.product_price)).setText(p.getPrice() + "€");

        TextView quantity = ((TextView)row.findViewById(R.id.product_quantity_));

        quantity.setText(String.format(Locale.ENGLISH, "%d", p.getQuantity()));
        Button add_btn = (Button)row.findViewById(R.id.add_btn);
        Button remove_btn = (Button)row.findViewById(R.id.remove_btn);

        remove_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(p.getQuantity() > 1)
                {
                    p.setQuantity(p.getQuantity() - 1);
                    quantity.setText(String.format(Locale.ENGLISH, "%d", p.getQuantity()));
                }
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                p.setQuantity(p.getQuantity() + 1);
                quantity.setText(String.format(Locale.ENGLISH, "%d", p.getQuantity()));
            }
        });

        return (row);
    }
}
