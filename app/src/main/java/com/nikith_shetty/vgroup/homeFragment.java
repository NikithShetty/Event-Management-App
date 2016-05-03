package com.nikith_shetty.vgroup;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import java.util.List;
import adapters.RVAdapter_home;
import helper.classes.Global;
import models.EventData;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {

    View view;
    List<EventData> recentEvents;
    StaggeredGridLayoutManager layout;
    RecyclerView rv;
    RVAdapter_home rvAdapterHome;
    private ProgressDialog progressDialog;
    Context context;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance(){
        return new homeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
            recentEvents = Global.loadEventDataList(getActivity());
            if(progressDialog!=null&&progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpRecyclerView();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog = null;
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
        layout = new StaggeredGridLayoutManager(2, 1);
        rv.setLayoutManager(layout);
    }

}
