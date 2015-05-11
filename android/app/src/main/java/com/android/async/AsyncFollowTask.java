package com.android.async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.interfaces.OnTaskCompleted;
import com.example.touravel.app.SplashScreen;
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

public class AsyncFollowTask extends AsyncTask<String, Void, Void> {

    protected int TIMEOUT_MILLISEC = 10000;
    protected JSONObject jsonObj = null;
    protected String url = null;
    protected String authKey = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    private OnTaskCompleted listener;

    public AsyncFollowTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(String... params) {

        url = params[0];
        authKey = params[1];

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet(url);

        request.setHeader("auth-key", authKey);
        //request.setEntity(new ByteArrayEntity(jsonObj.toString().getBytes("UTF8")));

        HttpResponse response = null;
        try {
            response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            responseStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("GET", "auth-key -> " + params[1]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
       /*
        Toast.makeText(SplashScreen.cnt, jsonObj.toString(), Toast.LENGTH_LONG).show();
        Log.i("POST", authKey + " | " + jsonObj.toString());
        Toast.makeText(SplashScreen.cnt, responseStr, Toast.LENGTH_LONG).show();
        Log.i("POST-Response", responseStr);
        */
        Toast.makeText(SplashScreen.cnt, "r.code: "+responseCode, Toast.LENGTH_LONG).show();

        if (responseCode >= 200 && responseCode < 300) {
            JSONObject reader, data;
            List<User> users = new ArrayList<User>();
            try {
                reader = new JSONObject(responseStr);
                Toast.makeText(SplashScreen.cnt, responseStr, Toast.LENGTH_LONG).show();
                for (int i = 0; i < reader.length(); i++) {
                    data = reader.getJSONObject("" + i);
                    users.add(new User("@" + data.getString("username"), data.getString("name"), data.getString("location"), data.getString("about_me"), data.getString("avatar_thumb")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SplashScreen.cnt, "A wild error occurred!", Toast.LENGTH_LONG).show();
            }

            listener.onTaskCompleted(users);
        } else {
            String message;
            try {
                JSONObject reader = new JSONObject(responseStr);
                message = reader.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                message = "An error occurred!";
            }

        }
    }
}
