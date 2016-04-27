package com.nikith_shetty.vgroup;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import helper.classes.Global;
import adapters.RVAdapter_events;
import helper.classes.HTTPhelper;
import models.EventData;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class eventFragment extends Fragment{
    static int FILTER;
    final static int PLACES_FILTER = 1;
    final static int NO_FILTER = 0;
    final static int COLLEGES_FILTER = 2;
    NavigationView nav_view;
    View view;
    LinearLayoutManager layout;
    RecyclerView rv;
    RVAdapter_events rvAdapterEvents;
    List<EventData> eventDataList;
    Reader reader;
    Gson gson = new Gson();
    private ProgressDialog progressDialog;
    private String data;

    public eventFragment() {
        FILTER = NO_FILTER;
    }

    public void setArguments(NavigationView view) {
        nav_view = view;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event,container,false);
        eventDataList = new ArrayList<EventData>();

        if(FILTER == NO_FILTER){
            fetchEventDataFromServer();
        }else if(FILTER == PLACES_FILTER){
            fetchEventDataWithPlacesFilter();
        }else if (FILTER == COLLEGES_FILTER){
            fetchEventDataWithCollegesFilter();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nav_view.setCheckedItem(R.id.nav_events);
        IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEventDataReceived, eventDataReceived);
        ((MainActivity)getActivity()).setTitle("Events");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onEventDataReceived);
    }

    private void setUpView() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rvAdapterEvents = new RVAdapter_events(eventDataList);
        rvAdapterEvents.setListener(new RVAdapter_events.Listener() {
            @Override
            public void onClick(EventData data) {
                Global.setRecent(data);
                Intent intent = new Intent(getActivity(),EventDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                getActivity().startActivity(intent);
            }
        });
        rv.setAdapter(rvAdapterEvents);
        layout = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layout);
    }

    private BroadcastReceiver onEventDataReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog!=null&progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpView();
        }
    };

    private void fetchEventDataFromServer() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    reader = new InputStreamReader(HTTPhelper.get(Global.GET_EVENTS_DATA).body().byteStream());
                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void fetchEventDataWithPlacesFilter(){
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("places", data.toString())
                            .build();
                    Request request = new Request.Builder()
                            .url(Global.GET_PLACES_DATA)
                            .post(requestBody)
                            .build();
                    reader = new InputStreamReader(HTTPhelper.post(request).body().byteStream());
                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void fetchEventDataWithCollegesFilter(){
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("colleges", data.toString())
                            .build();
                    Request request = new Request.Builder()
                            .url(Global.GET_COLLEGES_DATA)
                            .post(requestBody)
                            .build();
                    reader = new InputStreamReader(HTTPhelper.post(request).body().byteStream());                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setFilter(int filter, String data){
        FILTER = filter;
        this.data = data;
    }
}
