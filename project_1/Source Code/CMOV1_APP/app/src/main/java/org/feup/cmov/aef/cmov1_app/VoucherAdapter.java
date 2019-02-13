package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

import java.util.ArrayList;

public class VoucherAdapter extends ArrayAdapter<Voucher> {
    public Context context;
    public int resource;
    public String type;
    public ArrayList<Voucher> vouchers;

    public VoucherAdapter(Context context, int resourceId, ArrayList<Voucher> objects, String type) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.vouchers = objects;
        this.resource = resourceId;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }
        Voucher v = this.getItem(position);

        CheckBox checkBox = row.findViewById(R.id.voucher_check);
        if(checkBox != null)
        {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(v.selected);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                v.selected = isChecked;
            });
        }


        Drawable drawable = null;
        if(v.getType().equals("FreePopcorn"))
            drawable = parent.getResources().getDrawable(R.drawable.popcorn);
        else if(v.getType().equals("FreeCoffee"))
            drawable = parent.getResources().getDrawable(R.drawable.coffee);
        else
            drawable = parent.getResources().getDrawable(R.drawable.discount);

        ImageView symbol = (ImageView)row.findViewById(R.id.voucher_image);
        symbol.setImageDrawable(drawable);
        if(v.getType().equals("FreePopcorn"))
            ((TextView)row.findViewById(R.id.voucher_name)).setText("Free Popcorn");
        else if(v.getType().equals("FreeCoffee"))
            ((TextView)row.findViewById(R.id.voucher_name)).setText("Free Coffee");
        else
            ((TextView)row.findViewById(R.id.voucher_name)).setText("5% discount");


        return (row);
    }

}
