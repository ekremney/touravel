package com.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.interfaces.OnTaskCompleted;
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
                ArrayList<String> usernames = new ArrayList<String>();
                ArrayList<String> routes = new ArrayList<String>();
                ArrayList<String> locationss = new ArrayList<String>();
                for (int i = 0; i < reader.length(); i++) {
                    fetched = reader.getJSONObject("" + i);
                    usernames.add(fetched.getString("username"));
                    routes.add(fetched.getString("route"));
                    locationss.add(fetched.getString("stops"));
                }

                StorylineActivity.usernames = new String[usernames.size()];
                for (int i = 0; i < usernames.size(); i++)
                    StorylineActivity.usernames[i] = usernames.get(i);

                StorylineActivity.data = new String[routes.size() + locationss.size()];
                int i;
                for (i = 0; i < routes.size(); i++) {
                    StorylineActivity.data[i] = routes.get(i);
                    StorylineActivity.types[i] = "1";
                }
                for (; i < routes.size() + locationss.size(); i++) {
                    StorylineActivity.data[i] = locationss.get(i - routes.size());
                    StorylineActivity.types[i] = "0";
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
