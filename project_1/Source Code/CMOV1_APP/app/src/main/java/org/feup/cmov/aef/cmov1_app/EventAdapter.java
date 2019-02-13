package org.feup.cmov.aef.cmov1_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.cmov.aef.cmov1_app.entitites.Performance;
import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Performance> {
    public Context context;
    public ArrayList<Performance> events;

    public EventAdapter(Context context, int resourceId, ArrayList<Performance> objects) {
        super(context, resourceId, objects); // the Activity, row layout, and array of values
        this.context = context;
        this.events = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.event_row, parent, false);
        }
        Performance e = this.events.get(position);
        ((TextView)row.findViewById(R.id.event_name)).setText(e.name);
        String date = e.date.replace("T", " ");
        date = date.substring(0, date.length() - 3);

        ((TextView)row.findViewById(R.id.event_date)).setText(date);

        int id = parent.getResources().getIdentifier("org.feup.cmov.aef.cmov1_app:drawable/image_" + e.id, null, null);
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

        return (row);
    }
}
