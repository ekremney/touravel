package com.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.interfaces.OnTaskCompleted;
import com.example.touravel.app.LoginActivity;
import com.example.touravel.app.SettingsActivity;
import com.example.touravel.app.SplashScreen;
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


public class AsyncGetTimeline extends AsyncTask<String, Void, Void> {

    protected String url = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;
    private OnTaskCompleted listener;

    public AsyncGetTimeline(OnTaskCompleted listener){
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
            //request.setHeader("limit", params[2]);

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            responseCode = response.getStatusLine().getStatusCode();


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
                ArrayList<String> users = new ArrayList<String>();
                ArrayList<String> avatars = new ArrayList<String>();
                ArrayList<String> types = new ArrayList<String>();
                ArrayList<String> data = new ArrayList<String>();
                ArrayList<String> likes = new ArrayList<String>();
                for (int i = 0; i < reader.length(); i++) {
                    fetched = reader.getJSONObject("" + i);
                    users.add(fetched.getString("name"));
                    avatars.add(fetched.getString("avatar_thumb"));
                    types.add(fetched.getString("post_type"));
                    data.add(fetched.getString("data"));
                    likes.add(fetched.getString("like_amount"));
                }

                TimelineActivity.users = new String[users.size()];
                for (int i = 0; i < users.size(); i++)
                    TimelineActivity.users[i] = users.get(i);

                TimelineActivity.avatars = new String[avatars.size()];
                for (int i = 0; i < avatars.size(); i++)
                    TimelineActivity.avatars[i] = avatars.get(i);

                TimelineActivity.types = new String[types.size()];
                for (int i = 0; i < types.size(); i++)
                    TimelineActivity.types[i] = types.get(i);

                TimelineActivity.data = new String[data.size()];
                for (int i = 0; i < data.size(); i++)
                    TimelineActivity.data[i] = data.get(i);

                TimelineActivity.likes = new String[likes.size()];
                for (int i = 0; i < likes.size(); i++)
                    TimelineActivity.likes[i] = likes.get(i);

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

}
