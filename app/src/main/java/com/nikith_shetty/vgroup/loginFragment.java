package com.nikith_shetty.vgroup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;

import helper.classes.Global;

/**
 * Created by Nikith_Shetty on 28/04/2016.
 */
public class loginFragment extends Fragment {
    View view;
    EditText userName;
    EditText password;
    Button loginButton;
    Button registerButton;
    Button forgotPassword;
    ProgressDialog loginProgress;
    private LoginFragmentListener loginFragmentListener;

    public static loginFragment newInstance(){
        return new loginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        if (activity instanceof LoginFragmentListener) {
            loginFragmentListener = (LoginFragmentListener) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement LoginFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        userName = (EditText)view.findViewById(R.id.login_username);
        password = (EditText)view.findViewById(R.id.login_password);
        loginButton = (Button)view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });
        registerButton = (Button)view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        });
        forgotPassword = (Button) view.findViewById(R.id.login_forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPasswordClicked();
            }
        });
        return view;
    }

    private void onLoginSuccess() {
        if(loginProgress!=null && loginProgress.isShowing()){
            loginProgress.dismiss();
        }
        loginFragmentListener.onLoginSuccess();
        Log.e("Login Fragment : ", "Login Success ");
    }

    private void onLoginButtonClicked() {
        if (TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
            Snackbar.make(loginButton, com.stormpath.sdk.ui.R.string.stormpath_fill_in_credentials, Snackbar.LENGTH_SHORT).show();
            return;
        }
        loginProgress = ProgressDialog.show(getActivity(), "", "logging In...");
        Stormpath.login(userName.getText().toString(), password.getText().toString(), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
                    @Override
                    public void onSuccess(UserProfile userProfile) {
                        Global.setUserName(userProfile.getFullName());
                    }

                    @Override
                    public void onFailure(StormpathError error) {
                        if(Global.getUserName() != "")
                            Global.setUserName("");
                    }
                });
                onLoginSuccess();
            }

            @Override
            public void onFailure(StormpathError error) {
                if(loginProgress!=null && loginProgress.isShowing()){
                    loginProgress.dismiss();
                }
                Snackbar.make(loginButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void onRegisterButtonClicked() {
        loginFragmentListener.onRegisterClicked(userName.getText().toString().trim(), password.getText().toString().trim());

    }


    private void onForgotPasswordClicked() {
        loginFragmentListener.onForgotPasswordClicked(userName.getText().toString().trim());

    }

    public interface LoginFragmentListener {

        void onRegisterClicked(String username, String password);

        void onLoginSuccess();

        void onForgotPasswordClicked(String username);
    }
}
