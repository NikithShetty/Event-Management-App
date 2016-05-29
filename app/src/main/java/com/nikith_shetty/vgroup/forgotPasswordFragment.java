package com.nikith_shetty.vgroup;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;


/**
 * A simple {@link Fragment} subclass.
 */
public class forgotPasswordFragment extends Fragment {


    View view;
    EditText email;
    ProgressBar progressBar;
    Button reset;
    private ResetPasswordFragmentListener mListener;
    
    public forgotPasswordFragment() {
        // Required empty public constructor
    }

    public static forgotPasswordFragment newInstance(){
        return new forgotPasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResetPasswordFragmentListener) {
            mListener = (ResetPasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ResetPasswordFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        email = (EditText)view.findViewById(R.id.forgot_input_username);
        progressBar = (ProgressBar)view.findViewById(R.id.forgot_resetpw_progress_bar);
        reset = (Button)view.findViewById(R.id.forgot_resetpw_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(8)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //do email validation
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {

                        email.getBackground().clearColorFilter();

                    }
                    else{
                        //set underline color
                        email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    protected void onSend() {
        if (TextUtils.isEmpty(email.getText().toString())) { //check for valid email
            Snackbar.make(reset, com.stormpath.sdk.ui.R.string.stormpath_all_fields_mandatory, Snackbar.LENGTH_SHORT).show();
            return;
        }

        Stormpath.resetPassword(email.getText().toString(), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                Snackbar.make(reset, getString(com.stormpath.sdk.ui.R.string.stormpath_resetpassword_result), Snackbar.LENGTH_LONG).show();
                mListener.onFragmentDone();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(reset, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });

        showProgress();
    }

    public void showProgress() {
        reset.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        reset.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ResetPasswordFragmentListener {
        void onFragmentDone();
    }



}
