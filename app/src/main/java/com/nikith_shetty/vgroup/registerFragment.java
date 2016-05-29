package com.nikith_shetty.vgroup;


import android.annotation.TargetApi;
import android.app.Activity;
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
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.StormpathError;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class registerFragment extends Fragment {

    View view;
    private RegisterFragmentListener registerFragmentListener;
//    @Bind(R.id.register_input_firstname)
    EditText firstName;
//    @Bind(R.id.register_input_surname)
    EditText surname;
//    @Bind(R.id.register_input_email)
    EditText email;
//    @Bind(R.id.register_input_password)
    EditText password;
//    @Bind(R.id.register_register_button)
    Button registerButton;
//    @Bind(R.id.register_register_progress_bar)
    ProgressBar progressBar;



    public registerFragment() {
        // Required empty public constructor
    }

    public static registerFragment newInstance(){
        return new registerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentListener) {
            registerFragmentListener = (RegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment
//        ButterKnife.bind((Activity) view.getContext());
        firstName = (EditText)view.findViewById(R.id.register_input_firstname);
        surname = (EditText)view.findViewById(R.id.register_input_surname);
        password = (EditText)view.findViewById(R.id.register_input_password);
        progressBar = (ProgressBar)view.findViewById(R.id.register_register_progress_bar);
        email = (EditText)view.findViewById(R.id.register_input_email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(8)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //do email validation
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {

                        email.getBackground().clearColorFilter();

                    } else {
                        //set underline color
                        email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }
                }


            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerButton = (Button)view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClicked();
            }
        });

            return view;
    }

    private void onRegisterClicked() {
        if (TextUtils.isEmpty(firstName.getText().toString())
                || TextUtils.isEmpty(surname.getText().toString())
                || TextUtils.isEmpty(email.getText().toString())
                || TextUtils.isEmpty(password.getText().toString())) {
            Snackbar.make(registerButton, com.stormpath.sdk.ui.R.string.stormpath_all_fields_mandatory, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                Snackbar.make(registerButton, com.stormpath.sdk.ui.R.string.stormpath_requires_valid_email, Snackbar.LENGTH_SHORT).show();
            }
        }

        Stormpath.register(new RegisterParams(firstName.getText().toString(), surname.getText().toString(),
                email.getText().toString(), password.getText().toString()), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                registerFragmentListener.onFragmentDone();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(registerButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });

        showProgress();
    }

    public interface RegisterFragmentListener {
        void onFragmentDone();
    }

    public void showProgress() {
        registerButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        registerButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registerFragmentListener = null;
    }
}
