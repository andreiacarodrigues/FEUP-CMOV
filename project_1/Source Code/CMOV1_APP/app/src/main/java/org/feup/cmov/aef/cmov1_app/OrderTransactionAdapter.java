package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.OrderTransaction;

import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;

public class OrderTransactionAdapter extends ArrayAdapter<OrderTransaction> {
    public Context context;
    public ArrayList<OrderTransaction> orders;

    public OrderTransactionAdapter(Context context, int resourceId, ArrayList<OrderTransaction> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.orders = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.order_transaction_row, parent, false);
        }
        OrderTransaction o = this.orders.get(position);
        ((TextView)row.findViewById(R.id.event_name)).setText("#" + o.getId());
        //https://stackoverflow.com/questions/24078275/how-to-add-a-row-dynamically-in-a-tablelayout-in-android

        TableLayout tl = (TableLayout) row.findViewById(R.id.product_list);
        for(int i = 0; i < o.getProducts().size(); i++)
        {
            TableRow tr = new TableRow(row.getContext());
            tr.setPadding(0,5,0,5);
            tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView name = new TextView(row.getContext());
            name.setText((i+ 1) + ". " + o.getProducts().get(i).getName());
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(row.getContext(), R.font.roboto_light),
                    Typeface.NORMAL);
            name.setTypeface(myTypeface);
            name.setTextSize(14);
            name.setTextColor(row.getContext().getResources().getColor(R.color.grey));
            name.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            tr.addView(name);

            TextView preco = new TextView(row.getContext());
            if(o.getProducts().get(i).getPrice() != null && o.getProducts().get(i).getPrice().equals("Free"))
                preco.setText(o.getProducts().get(i).getQuantity() + " x " + o.getProducts().get(i).getPrice() );
            else
                preco.setText(o.getProducts().get(i).getQuantity() + " x " + o.getProducts().get(i).getPrice() + "€" );
            preco.setTypeface(myTypeface);
            preco.setGravity(Gravity.RIGHT);
            preco.setTextSize(14);
            preco.setTextColor(row.getContext().getResources().getColor(R.color.grey));
            preco.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            tr.addView(preco);

            tl.addView(tr);
        }

        TableRow vouchtr = new TableRow(row.getContext());
        vouchtr.setPadding(0,15,0,5);
        vouchtr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView vouchersLabel = new TextView(row.getContext());
        vouchersLabel.setText("Vouchers");
        Typeface tf = Typeface.create(ResourcesCompat.getFont(row.getContext(), R.font.niramit_bold),
                Typeface.NORMAL);
        vouchersLabel.setTypeface(tf);
        vouchersLabel.setTextSize(16);
        vouchersLabel.setTextColor(row.getContext().getResources().getColor(R.color.grey));
        vouchersLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        vouchtr.addView(vouchersLabel);

        tl.addView(vouchtr);

        for(int i = 0; i < o.Vouchers.size(); i++) {

            TableRow tr = new TableRow(row.getContext());
            tr.setPadding(0,5,0,5);
            tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView vouchers = new TextView(row.getContext());
            if(o.Vouchers.get(i).equals("FreePopcorn"))
                vouchers.setText((i + 1) + ". Free Popcorn" );
            else if(o.Vouchers.get(i).equals("FreeCoffee"))
                vouchers.setText((i + 1) + ". Free Coffee" );
            else
                vouchers.setText((i + 1) + ". 5% Discount" );
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(row.getContext(), R.font.roboto_light),
                    Typeface.NORMAL);
            vouchers.setTypeface(myTypeface);
            vouchers.setTextSize(14);
            vouchers.setTextColor(row.getContext().getResources().getColor(R.color.grey));
            vouchers.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            tr.addView(vouchers);

            tl.addView(tr);
        }
        if(o.getMoney().equals("Free"))
        {
            ((TextView)row.findViewById(R.id.order_price)).setText("Total Price: Free");
        }
        else
        {
            ((TextView)row.findViewById(R.id.order_price)).setText("Total Price: " + (o.getMoney()) + "€");
        }

        return (row);
    }
}
