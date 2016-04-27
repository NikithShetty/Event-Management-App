package com.nikith_shetty.vgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import adapters.RVAdapter_events;
import models.EventData;

public class PlacesDetailsActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager layoutManager;
    RVAdapter_events adapter;
    List<EventData> eventDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //setup recyclerView
//        rv = (RecyclerView)findViewById(R.id.places_recyclerView);
//        layoutManager = new LinearLayoutManager(this);
//        rv.setAdapter(adapter);
    }
}
