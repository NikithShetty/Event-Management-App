package com.nikith_shetty.vgroup;


import android.app.Activity;
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
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import adapters.RVAdapter_places;
import helper.classes.Global;
import helper.classes.HTTPhelper;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class placesFragment extends Fragment {

    View view;
    ProgressDialog progressDialog;
    JSONArray jsonArray;
    RecyclerView rv;
    RVAdapter_places rvAdapter_places;
    LinearLayoutManager layout;
    Context context;
    appTitle appTitle;

    public placesFragment() {
        // Required empty public constructor
    }

    public static placesFragment newInstance(){
        return new placesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        final Activity activity = getActivity();
        if (activity instanceof appTitle) {
            appTitle = (appTitle) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_places, container, false);

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                     jsonArray = convertFromInputStreamToJsonobject(HTTPhelper.get(Global.GET_PLACES_DATA).body());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitle.onSetTitle("Places");
        IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEventDataReceivedPlaces, eventDataReceived);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onEventDataReceivedPlaces);
        progressDialog = null;
    }

    public JSONArray convertFromInputStreamToJsonobject(ResponseBody responseBody){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        JSONArray jsonArray = null;
        StringBuilder sb = new StringBuilder();
        String line;
        String json;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
            jsonArray = new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private BroadcastReceiver onEventDataReceivedPlaces = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpView();
        }
    };

    private void setUpView() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView_places);
        rv.setHasFixedSize(true);
        rvAdapter_places = new RVAdapter_places(jsonArray);
        rvAdapter_places.setListener(new RVAdapter_places.Listener() {
            @Override
            public void onClick(String data) {
                makeTransactionToEventsFragment(data);
            }
        });
        rv.setAdapter(rvAdapter_places);
        layout = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layout);
    }

    private void makeTransactionToEventsFragment(String data) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_area,eventFragment.newInstance(com.nikith_shetty.vgroup.eventFragment.PLACES_FILTER, data));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface placesFragmentListener{
    }
}
