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

import org.feup.cmov.aef.cmov1_app.entitites.Ticket;

import java.util.ArrayList;

public class TicketValidationAdapter extends ArrayAdapter<Ticket> {
    public Context context;
    public ArrayList<Ticket> tickets;

    public TicketValidationAdapter(Context context, int resourceId, ArrayList<Ticket> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.tickets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.ticket_check_row, parent, false);
        }
        Ticket t = this.tickets.get(position);

        CheckBox cb = row.findViewById(R.id.ticket_check);
        if(cb != null)
        {
            cb.setOnCheckedChangeListener(null);
            cb.setChecked(t.selected);
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                t.selected = isChecked;
            });
        }
        int id = parent.getResources().getIdentifier("org.feup.cmov.aef.cmov1_app:drawable/image_" + t.performanceId, null, null);
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
        ((TextView)row.findViewById(R.id.event_name)).setText(t.getPerformanceName());
        String date = t.getPerformanceDate().replace("T", " ");
        date = date.substring(0, date.length() - 3);
        ((TextView)row.findViewById(R.id.event_date)).setText(date);
        ((TextView)row.findViewById(R.id.seat_nr)).setText("Seat nr: " + t.getPlaceInRoom());

        return (row);
    }
}
