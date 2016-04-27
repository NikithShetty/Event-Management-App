package helper.classes;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nikith_Shetty on 22/04/2016.
 */
public class HTTPhelper {
    static OkHttpClient client;
    static {
        client = new OkHttpClient();
    }

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response post(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        return response;
    }
}
