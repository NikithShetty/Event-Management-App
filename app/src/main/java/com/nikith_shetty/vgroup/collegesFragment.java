package com.nikith_shetty.vgroup;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class collegesFragment extends Fragment {

    NavigationView view;

    public collegesFragment() {
        // Required empty public constructor
    }

    public void setArguments(NavigationView view) {
        this.view = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_colleges, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.setCheckedItem(R.id.nav_colleges);
    }

}
