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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.EventAdapter;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.entitites.Performance;
import org.feup.cmov.aef.cmov1_app.tasks.GetUpcomingPerformancesTask;

import java.util.ArrayList;

public class EventsViewActivity extends AppCompatActivity implements GetUpcomingPerformancesTask.ICallback
{
    EventAdapter itemsAdapter;

    private ProgressBar spinner;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_view);
        new GetUpcomingPerformancesTask(this).execute();

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Next Events");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        spinner = (ProgressBar)findViewById(R.id.progress_bar);
        content = (LinearLayout)findViewById(R.id.content);

        ArrayList<Performance> items = new ArrayList<>();
        itemsAdapter = new EventAdapter(this, R.layout.event_row, items);
        ListView listView = (ListView)findViewById(R.id.events_list);
        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Performance e = items.get(position);
                Intent eventActivity = new Intent(view.getContext(), EventViewActivity.class);

                ArrayList<String> e_info = new ArrayList<String>();
                e_info.add(String.valueOf(e.id));
                e_info.add(e.name);
                String date = e.date.replace("T", " ");
                date = date.substring(0, date.length() - 3);
                e_info.add(date);
                e_info.add(e.price);

                eventActivity.putStringArrayListExtra(Constants.EVENT_INFO, e_info);
                view.getContext().startActivity(eventActivity);
            }
        });

    }

    @Override
    public void onPostGetUpcomingPerfomancesTask(Performance[] performances)
    {
        if(performances == null)
        {
            Toast.makeText(getApplicationContext(), "There was an error getting the performances, please try later.", Toast.LENGTH_LONG).show();
            Intent mainActivity = new Intent(this, MainMenuActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
            return;
        }
        itemsAdapter.addAll(performances);
        spinner.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_home) {
            Intent mainActivity = new Intent(this, MainMenuActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
