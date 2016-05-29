package com.nikith_shetty.vgroup;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
    View view;
    LinearLayoutManager layout;
    RecyclerView rv;
    RVAdapter_events rvAdapterEvents;
    Reader reader;
    Gson gson = new Gson();
    private ProgressDialog progressDialog;
    private String data;
    SwipeRefreshLayout swipeRefreshLayout;
    Bundle config;
    Context context;
    appTitleInterface appTitleInterface;

    static List<EventData> eventDataList;

    public eventFragment() {
        FILTER = NO_FILTER;
    }

    public static eventFragment newInstance(){
        eventFragment ef = new eventFragment();
        return ef;
    }

    public static eventFragment newInstance(int filter, String data){
        eventFragment ef = new eventFragment();
        Bundle config = new Bundle();
        config.putInt("filter", filter);
        config.putString("data", data);
        ef.setArguments(config);
        return ef;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        final Activity activity = getActivity();
        if (activity instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventDataList = new ArrayList<EventData>();
        config = getArguments();
        if(config!=null){
            data = config.getString("data");
            FILTER = config.getInt("filter");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event,container,false);

        setUpSwipRefershView();

        if(FILTER == NO_FILTER){
            fetchEventDataFromSharedPreference();
        }else if(FILTER == PLACES_FILTER){
            fetchEventDataFromServer("places");
        }else if (FILTER == COLLEGES_FILTER){
            this.fetchEventDataFromServer("colleges");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitleInterface.onSetTitle("Events");
        IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
        LocalBroadcastManager.getInstance(context).registerReceiver(onEventDataReceived, eventDataReceived);
        ((MainActivity)context).setTitle("Events");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(onEventDataReceived);
        progressDialog = null;
    }

    private void setUpSwipRefershView() {
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.events_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(FILTER == NO_FILTER){
                    fetchEventDataFromServer();
                }else if(FILTER == PLACES_FILTER){
                    fetchEventDataFromServer("places");
                }else if (FILTER == COLLEGES_FILTER){
                    fetchEventDataFromServer("colleges");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchEventDataFromSharedPreference() {
        progressDialog = ProgressDialog.show(context, "", "Loading...");
        eventDataList = Global.loadEventDataList(context);
        if(progressDialog!=null  &&  progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        view.invalidate();
        setUpView();
    }

    private void setUpView() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rvAdapterEvents = new RVAdapter_events(eventDataList);
        rvAdapterEvents.setListener(new RVAdapter_events.Listener() {
            @Override
            public void onClick(EventData data) {
                Global.setRecent(data);
                Intent intent = new Intent(context,EventDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                context.startActivity(intent);
            }
        });
        rv.setAdapter(rvAdapterEvents);
        layout = new LinearLayoutManager(context);
        rv.setLayoutManager(layout);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setChangeDuration(500);
        rv.setItemAnimator(itemAnimator);
    }

    private BroadcastReceiver onEventDataReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpView();
        }
    };

    private void fetchEventDataFromServer() {
        progressDialog = ProgressDialog.show(context, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    reader = new InputStreamReader(HTTPhelper.get(Global.GET_EVENTS_DATA).body().byteStream());
                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Global.saveEventDataList(context, eventDataList);
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void fetchEventDataFromServer(final String filter){
        progressDialog = ProgressDialog.show(context, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url;
                try{
                    if(filter.equals("places"))
                        url = Global.GET_PLACES_DATA;
                    else
                        url = Global.GET_COLLEGES_DATA;
                    RequestBody requestBody = new FormBody.Builder()
                            .add(filter, data.toString())
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    reader = new InputStreamReader(HTTPhelper.post(request).body().byteStream());
                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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

    public interface eventFragmentListener{
    }
}
