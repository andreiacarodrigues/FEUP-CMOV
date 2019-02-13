package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.Product;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    public Context context;
    public ArrayList<Product> products;

    public ProductAdapter(Context context, int resourceId, ArrayList<Product> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.products = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.product_check_row, parent, false);
        }
        Product p = this.products.get(position);
        ((CheckBox)row.findViewById(R.id.voucher_name)).setText(p.getName());
        if(p.getPrice().equals("Free"))
            ((TextView)row.findViewById(R.id.product_price)).setText("Free");
        else
            ((TextView)row.findViewById(R.id.product_price)).setText(p.getPrice() + "€");

        if(p.getQuantity() > 0)
            ((CheckBox)row.findViewById(R.id.voucher_name)).setChecked(true);

        return (row);
    }
}
