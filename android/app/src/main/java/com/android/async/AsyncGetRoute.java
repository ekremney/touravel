package com.android.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.interfaces.OnTaskCompleted;
import com.example.touravel.app.BackgroundService;
import com.example.touravel.app.LoginActivity;
import com.example.touravel.app.SplashScreen;
import com.example.touravel.app.StorylineActivity;
import com.example.touravel.app.TimelineActivity;
import com.example.touravel.app.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AsyncGetRoute extends AsyncTask<String, Void, Void> implements OnTaskCompleted{

    protected String url = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;
    private OnTaskCompleted listener;

    public AsyncGetRoute(OnTaskCompleted listener){
        this.listener=listener;
    }

    @Override
    protected void onPreExecute()
    {
    }

    @Override
    protected Void doInBackground(String... params)
    {
        try
        {
            url = params[0];

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);


            HttpClient client = new DefaultHttpClient(httpParams);
            HttpGet request = new HttpGet(url);

            request.setHeader("auth-key", params[1]);
            request.setHeader("day", params[2]);

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            responseCode = response.getStatusLine().getStatusCode();

            Log.i("GET", "auth-key -> " + params[1]);
            Log.i("GET", "day -> " + params[2]);

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... param)
    {
    }

    @Override
    protected void onPostExecute(Void param)
    {
        if(responseCode >=200 && responseCode < 300) {
            try {
                JSONObject reader, fetched;
                reader = new JSONObject(responseStr);
                ArrayList<String> routes = new ArrayList<String>();
                ArrayList<String> stops = new ArrayList<String>();
                for (int i = 0; i < reader.length(); i++) {
                    fetched = reader.getJSONObject("" + i);
                    routes.add(fetched.getString("route"));
                    String loc = fetched.getString("stops");
                    //Toast.makeText(SplashScreen.cnt, loc, Toast.LENGTH_LONG).show();
                    if(!loc.equals(""))
                        stops.add(fetched.getString("stops"));
                }

                int locNo = 0;
                for (int k = 0; k < stops.size(); k++) {
                    String locs = stops.get(k) + "!";
                    while(!locs.equals("!")) {
                        locNo++;
                        locs = locs.substring(locs.indexOf('+') + 1);
                    }
                }

                int size = routes.size() + locNo;

                StorylineActivity.usernames = new String[size];
                for (int i = 0; i < size; i++)
                    StorylineActivity.usernames[i] = BackgroundService.username;

                StorylineActivity.data = new String[size];
                StorylineActivity.types = new String[size];
                int i;
                for (i = 0; i < routes.size(); i++) {
                    StorylineActivity.data[size - i - 1] = routes.get(i);
                    StorylineActivity.types[size - i - 1] = "1";
                }

                for (int k = 0; k < stops.size(); k++) {
                    String locs = stops.get(k) + "!";
                    while(!locs.equals("!")) {
                        StorylineActivity.data[size - i - 1] = locs.substring(0, locs.indexOf('+') + 1);
                        StorylineActivity.types[size - i - 1] = "0";
                        i++;
                        locs = locs.substring(locs.indexOf('+') + 1);
                    }
                }

                listener.onTaskCompleted();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SplashScreen.cnt, "A wild error occurred!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            String message;
            try {
                JSONObject reader = new JSONObject(responseStr);
                message = reader.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                message = "An error occurred!";
            }

            Toast.makeText(SplashScreen.cnt, message , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        LoginActivity.showProgress(false);
    }

    @Override
    public void onTaskCompleted(List<User> users) {

    }

    @Override
    public void onTaskCompleted() {

    }
}
