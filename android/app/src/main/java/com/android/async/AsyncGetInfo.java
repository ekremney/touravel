package com.android.async;

/**
 * Created by Ekrem on 5/6/15.
 */

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.interfaces.ProfileInterface;
import com.example.touravel.app.SettingsActivity;
import com.example.touravel.app.SplashScreen;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.touravel.app.SettingsActivity;
import com.example.touravel.app.SplashScreen;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class AsyncGetInfo extends AsyncTask<String, Void, Void> {

    protected JSONObject jsonObj = null;
    protected String url = null;
    protected String authKey = null;
    protected String email = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;

    private ProfileInterface listener;

    public AsyncGetInfo(ProfileInterface listener){
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
            authKey = params[1];
            email = params[2];

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient client = new DefaultHttpClient(httpParams);
            HttpGet request = new HttpGet(url);

            request.setHeader("auth-key", authKey);
            request.setHeader("email", email);
            //request.setEntity(new ByteArrayEntity(jsonObj.toString().getBytes("UTF8")));

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
        if(responseCode >=200 && responseCode < 300)
        {
            //Toast.makeText(SplashScreen.cnt, responseStr, Toast.LENGTH_LONG).show();
            listener.onTaskCompleted(responseStr);
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

}
