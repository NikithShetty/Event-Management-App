package com.nikith_shetty.vgroup;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

import adapters.RVAdapter_home;
import helper.classes.Global;
import helper.classes.HTTPhelper;
import models.EventData;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {

    NavigationView navigationView;
    View view;
    List<EventData> recentEvents;
    LinearLayoutManager layout;
    RecyclerView rv;
    RVAdapter_home rvAdapterHome;
    Reader reader;
    Gson gson = new Gson();
    Handler messageHandler;
    private ProgressDialog progressDialog;

    public homeFragment() {
        // Required empty public constructor
    }

    public void setArguments(NavigationView view) {
        this.navigationView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //check for recent views
        if(Global.getRecent()!=null){
            recentEvents = Global.getRecent();
            setUpRecyclerView();
        }else{

            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        reader = new InputStreamReader(HTTPhelper.get(Global.GET_EVENTS_DATA).body().byteStream());
                        recentEvents = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                        Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
        IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEventDataReceived, eventDataReceived);
    }

    private void setUpRecyclerView() {
        rv = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        rv.setHasFixedSize(true);
        rvAdapterHome = new RVAdapter_home(recentEvents);
        rvAdapterHome.setListener(new RVAdapter_home.Listener() {
            @Override
            public void onClick(EventData data) {
                Global.setRecent(data);
                Intent intent = new Intent(getActivity(),EventDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                getActivity().startActivity(intent);
            }
        });
        rv.setAdapter(rvAdapterHome);
        layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layout);
    }

    private BroadcastReceiver onEventDataReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog!=null&&progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpRecyclerView();
        }
    };

}
