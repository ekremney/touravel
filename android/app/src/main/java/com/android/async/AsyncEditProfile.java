package com.android.async;

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


public class AsyncEditProfile extends AsyncTask<String, Void, Void> {

    protected JSONObject jsonObj = null;
    protected String url = null;
    protected String authKey = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;
    protected String name = null;
    protected String location = null;
    protected String about_me = null;

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
            name = params[2];
            location = params[3];
            about_me = params[4];

            jsonObj = new JSONObject
            (
                "{" +
                        "\"name\":\"" + params[2] + "\"," +
                        "\"location\":\"" + params[3] + "\"," +
                        "\"about_me\":\"" + params[4] + "\"" +
                "}"
            );

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost request = new HttpPost(url);

            request.setHeader("auth-key", authKey);
            request.setHeader("Content-Type","application/json");
            request.setEntity(new ByteArrayEntity(jsonObj.toString().getBytes("UTF8")));

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            responseCode = response.getStatusLine().getStatusCode();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
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
            Toast.makeText(SplashScreen.cnt, "Profile information has been changed" , Toast.LENGTH_LONG).show();
            SplashScreen.user.setName(name);
            SplashScreen.user.setLocation(location);
            SplashScreen.user.setAbout_me(about_me);

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
