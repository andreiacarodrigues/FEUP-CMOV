package org.feup.cmov.aef.cmov1_app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import org.feup.cmov.aef.cmov1_app.Constants;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import org.feup.cmov.aef.cmov1_app.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EventViewActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private MaterialCardView content;
    private int e_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayShowHomeEnabled(true);
            Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_regular),
                    Typeface.BOLD);
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("Event Details");
            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            Typeface tf = Typeface.create(ResourcesCompat.getFont(this, R.font.niramit_bold), Typeface.BOLD);
            tv.setTypeface(tf);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(tv);
        }

        spinner = (ProgressBar)findViewById(R.id.progress_bar);
        content = (MaterialCardView)findViewById(R.id.content);

        Intent intent = getIntent();
        // ID - Nome - Data - Preço - Tickets
        ArrayList<String> e_info = intent.getStringArrayListExtra(Constants.EVENT_INFO);

        e_id = Integer.parseInt(e_info.get(0));
        ImageView e_image = (ImageView) findViewById(R.id.voucher_image);
        TextView e_name = (TextView) findViewById(R.id.event_title);
        TextView e_date = (TextView) findViewById(R.id.event_date);
        TextView e_price = (TextView) findViewById(R.id.event_price);
        EditText e_tickets = (EditText) findViewById(R.id.event_tickets_input);
        Button e_button = (Button) findViewById(R.id.buy_ticket_button);

        e_name.setText(e_info.get(1));
        e_date.setText(e_info.get(2));

        if(e_info.get(3).equals("Free"))
            e_price.setText("Free");
        else
        {

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            float price = Float.valueOf(decimalFormat.format(Float.parseFloat(e_info.get(3))));
            e_price.setText(price + "€");
        }


        int image_id = getResources().getIdentifier("org.feup.cmov.aef.cmov1_app:drawable/image_" + e_info.get(0), null, null);
        if(image_id <= 0)
        {
            Drawable drawable = getResources().getDrawable(R.drawable.none);
            e_image.setImageDrawable(drawable);
        }
        else
        {
            Drawable drawable = getResources().getDrawable(image_id);
            e_image.setImageDrawable(drawable);
        }

        e_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(e_tickets.getText().toString().isEmpty())
                {
                    e_tickets.setError("Insert the number of tickets to buy.");
                    return;
                }

                ArrayList<String> e_info = new ArrayList<String>();
                e_info.add(String.valueOf(e_id));
                e_info.add(e_name.getText().toString());
                e_info.add(e_date.getText().toString());
                e_info.add(e_price.getText().toString());
                e_info.add(e_tickets.getText().toString());

                Intent confirmationActivity = new Intent(v.getContext(), EventConfirmActivity.class);
                confirmationActivity.putStringArrayListExtra(Constants.EVENT_INFO, e_info);
                v.getContext().startActivity(confirmationActivity);
            }
        });

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
