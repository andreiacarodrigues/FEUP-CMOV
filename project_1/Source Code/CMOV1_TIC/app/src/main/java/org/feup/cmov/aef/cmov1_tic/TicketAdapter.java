package org.feup.cmov.aef.cmov1_tic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.feup.cmov.aef.cmov1_tic.entities.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends ArrayAdapter<Ticket> {
    public Context context;
    public ArrayList<Ticket> tickets;

    public TicketAdapter(Context context, int resourceId, ArrayList<Ticket> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.tickets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.ticket_row, parent, false);
        }
        Ticket e = this.tickets.get(position);
        ((TextView)row.findViewById(R.id.ticket_seat)).setText("Seat nr: " + e.getPlaceInRoom());
    /*    ((TextView)row.findViewById(R.id.event_name)).setText(e.getPerformanceName());
        String date = e.getPerformanceDate().replace("T", " ");
        date = date.substring(0, date.length() - 3);
        ((TextView)row.findViewById(R.id.event_date)).setText(date);*/

        if(e.getUsed())
        {
            ((TextView)row.findViewById(R.id.validated)).setTextColor(getContext().getResources().getColor(R.color.green));
            ((TextView)row.findViewById(R.id.validated)).setText("VALID");
        }
        else
        {
            ((TextView)row.findViewById(R.id.validated)).setTextColor(getContext().getResources().getColor(R.color.red));
            ((TextView)row.findViewById(R.id.validated)).setText("INVALID");
        }

        return (row);
    }
}
