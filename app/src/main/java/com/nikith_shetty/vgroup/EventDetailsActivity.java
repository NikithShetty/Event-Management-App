package com.nikith_shetty.vgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.EventData;

public class EventDetailsActivity extends AppCompatActivity {

    EventData data;
    @Bind(R.id.details_event_name)
    TextView eventNameTxt;
    @Bind(R.id.details_college)
    TextView collegeTxt;
    @Bind(R.id.details_fee)
    TextView feeTxt;
    @Bind(R.id.details_details)
    TextView detailsTxt;
    @Bind(R.id.details_venue)
    TextView venueTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        data = new Gson().fromJson(extras.getString("data"), EventData.class);
        getSupportActionBar().setTitle(data.getEventName());
        eventNameTxt.setText(data.getEventName());
        collegeTxt.setText(data.getCollege());
        feeTxt.setText(data.getFee());
        detailsTxt.setText(data.getDetails());
        String venue = data.getVenue().getStreetAddr() + ", " + data.getVenue().getArea() + ", " + data.getVenue().getCity() + ", " + data.getVenue().getState() + "\n" + data.getVenue().getPincode();
        venueTxt.setText(venue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
