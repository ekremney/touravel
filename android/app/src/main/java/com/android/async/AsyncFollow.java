package com.android.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.interfaces.FollowInterface;
import com.example.touravel.app.LoginActivity;
import com.example.touravel.app.MainActivity;
import com.example.touravel.app.SplashScreen;

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


public class AsyncFollow extends AsyncTask<String, Void, Void> {

    protected String url = null;
    protected String responseStr = null;
    protected String username = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;
    private FollowInterface listener;

    public AsyncFollow(FollowInterface listener){
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
            username = params[2];

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);


            HttpClient client = new DefaultHttpClient(httpParams);
            HttpGet request = new HttpGet(url);

            request.setHeader("auth-key", params[1]);
            request.setHeader("username", params[2]);

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            responseCode = response.getStatusLine().getStatusCode();

            Log.i("GET", "auth-key -> " + params[1]);
            Log.i("GET", "username -> " + params[2]);

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

        if (responseCode >= 200 && responseCode < 300) {
            Toast.makeText(SplashScreen.cnt, "You followed " + username , Toast.LENGTH_LONG).show();
        }
        else {
            String message;
            try {
                JSONObject reader = new JSONObject(responseStr);
                message = reader.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                message = "An error occurred!";
            }

            Toast.makeText(SplashScreen.cnt, message , Toast.LENGTH_LONG).show();
            listener.onFollowCompleted();
        }


    }

    @Override
    protected void onCancelled() {
        LoginActivity.showProgress(false);
    }

}
