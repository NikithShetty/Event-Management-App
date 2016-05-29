package com.nikith_shetty.vgroup;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class LoginActivity extends AppCompatActivity implements com.nikith_shetty.vgroup.loginFragment.LoginFragmentListener, com.nikith_shetty.vgroup.registerFragment.RegisterFragmentListener, com.nikith_shetty.vgroup.forgotPasswordFragment.ResetPasswordFragmentListener {

    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_container, loginFragment.newInstance(), null)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRegisterClicked(String username, String password) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_container, registerFragment.newInstance(), null)
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onForgotPasswordClicked(String username) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_container, forgotPasswordFragment.newInstance(), null)
                .commit();
    }

    public void onFragmentDone() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_container, loginFragment.newInstance(), null)
                .commit();
    }
}
