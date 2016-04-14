package com.nikith_shetty.vgroup;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class eventFragment extends Fragment {

    NavigationView view;
    LinearLayoutManager layout;
    RecyclerView rv;
    RVAdapter rvAdapter;
    List<eventData> eventDataList;

    public eventFragment() {
        // Required empty public constructor
    }

    public void setArguments(NavigationView view) {
        this.view = view;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_event, container, false);
//        layout = new LinearLayoutManager(getActivity().getApplicationContext());
//        rv = (RecyclerView)view.findViewById(R.id.recyclerView);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(layout);
//        rvAdapter = new RVAdapter(eventDataList);
//        rv.setAdapter(rvAdapter);
//        // Inflate the layout for this fragment
//        return view;
        View view = inflater.inflate(R.layout.fragment_event,container,false);
        eventDataList = new ArrayList<eventData>();
        eventDataList.add(new eventData("Buggy Racing", "Dr.AIT", 50, "Dr.AIT campus", "", ""));
        eventDataList.add(new eventData("Buggy Racing", "Dr.AIT", 50, "Dr.AIT campus", "", ""));
        eventDataList.add(new eventData("Buggy Racing", "Dr.AIT", 50, "Dr.AIT campus", "", ""));
        eventDataList.add(new eventData("Buggy Racing", "Dr.AIT", 50, "Dr.AIT campus", "", ""));
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rvAdapter = new RVAdapter(eventDataList);
        rv.setAdapter(rvAdapter);
        layout = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layout);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        view.setCheckedItem(R.id.nav_events);

    }
}
