package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.TicketTransaction;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TicketTransactionAdapter extends ArrayAdapter<TicketTransaction> {
    public Context context;
    public ArrayList<TicketTransaction> tickets;

    public TicketTransactionAdapter(Context context, int resourceId, ArrayList<TicketTransaction> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.tickets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.ticket_transaction_row, parent, false);
        }
        TicketTransaction t = this.tickets.get(position);
        int id = parent.getResources().getIdentifier("org.feup.cmov.aef.cmov1_app:drawable/image_" + t.getPerformance().id, null, null);
        Drawable drawable;
        if(id <= 0)
        {
            drawable = parent.getResources().getDrawable(R.drawable.none);
        }
        else
        {
            drawable = parent.getResources().getDrawable(id);
        }
        ImageView symbol = (ImageView)row.findViewById(R.id.voucher_image);
        symbol.setImageDrawable(drawable);
        ((TextView)row.findViewById(R.id.event_name)).setText(t.getPerformance().name);
        String date = t.getPerformance().date.replace("T", " ");
        date = date.substring(0, date.length() - 3);
        ((TextView)row.findViewById(R.id.event_date)).setText(date);
        ((TextView)row.findViewById(R.id.ticket_quantity)).setText("Nr of Tickets: " + t.getQuantity());
        if(t.getPerformance().price.equals("Free"))
        {
            ((TextView)row.findViewById(R.id.order_price)).setText("Total Price: Free");
        }
        else
        {
            float price = Float.parseFloat(t.getPerformance().price);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            price = Float.valueOf(decimalFormat.format(price));
            ((TextView)row.findViewById(R.id.order_price)).setText("Total Price: " + price + "€");
        }

        return (row);
    }
}
