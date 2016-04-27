package helper.classes;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.NavigationView;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.EventData;

/**
 * Created by Nikith_Shetty on 15/04/2016.
 */
public class Global extends Application {
    static Context context;
    private static List<EventData> recent = null;
    private static Set<EventData> item = null;
    public static final String ipAddr = "gentle-mesa-83442.herokuapp.com";
    public static final String GET_EVENTS_DATA = "http://" + ipAddr + "/events/getData";
    public static final String GET_PLACES_DATA = "http://" + ipAddr + "/events/getPlaces";
    public static final String GET_COLLEGES_DATA = "http://" + ipAddr + "/events/getColleges";
    public static final String baseUrl = "http://" + ipAddr + "";
    public static final String ACTION_DATA_RECEIVED = "dataReceived";

    //Navigation View to  set itemSelected
    static NavigationView navigationView;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();


        // Initialize Stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl(Global.baseUrl)
                .build();
        Stormpath.init(this, stormpathConfiguration);
    }

    public static Context getContext(){
        return context;
    }

    public static void setRecent(EventData recently_used){
        if(recent==null){
            recent = new ArrayList<EventData>();
            item = new HashSet<EventData>();
        }
        //check for redundant entries
        if(item.add(recently_used))
            recent.add(recently_used);
    }

    public static List<EventData> getRecent() {
        return recent;
    }

    public static void setNavigationView(NavigationView view){
        navigationView = view;
    }
    public static NavigationView getNavigationView(){
        return navigationView;
    }
}
