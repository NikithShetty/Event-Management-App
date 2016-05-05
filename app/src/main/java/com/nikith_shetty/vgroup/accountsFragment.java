package com.nikith_shetty.vgroup;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikith_shetty.vgroup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class accountsFragment extends Fragment {

    Context context;
    appTitle appTitle;

    public accountsFragment() {
        // Required empty public constructor
    }

    public static accountsFragment newInstance(){
        return new accountsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitle.onSetTitle("My Dashboard");
    }

    public interface accountFragmentListener{
    }
}
