package com.nikith_shetty.vgroup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import helper.classes.Global;
import helper.classes.HTTPhelper;
import models.EventData;

/**
 * Created by Nikith_Shetty on 02/05/2016.
 */
public class SplashActivity extends AppCompatActivity {

    List<EventData> eventDataList;
    ImageView imageView;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_splash);

        eventDataList = Global.loadEventDataList(getApplicationContext());
        if(eventDataList == null)
            downloadEventData();
        else
            gotoHomePage();
    }

    private void downloadEventData() {
        imageView = (ImageView)findViewById(R.id.splash_imageView);
        if (imageView != null) {
        }
        eventDataList = new ArrayList<EventData>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(HTTPhelper.get(Global.GET_EVENTS_DATA).body().byteStream());
                    eventDataList = gson.fromJson(reader, new TypeToken<List<EventData>>(){}.getType());
                    Global.saveEventDataList(getBaseContext(), eventDataList);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            gotoHomePage();
                        }
                    });
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void gotoHomePage() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
