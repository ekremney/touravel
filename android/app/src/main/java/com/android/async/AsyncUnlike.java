package com.android.async;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.touravel.app.SettingsActivity;
import com.example.touravel.app.SplashScreen;
import com.example.touravel.app.TimelineActivity;

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

/**
 * Created by gokg on 6.5.2015.
 */
public class AsyncUnlike extends AsyncTask<String, Void, Void> {

    protected JSONObject jsonObj = null;
    protected String url = null;
    protected String authKey = null;
    protected String responseStr = null;
    protected int responseCode = 0;
    protected int TIMEOUT_MILLISEC = 10000;

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

            jsonObj = new JSONObject
                    (
                            "{" +
                                    "\"post_id\":\"" + params[2] +
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
            JSONObject reader = null;
            try {
                reader = new JSONObject(responseStr);
                //TimelineActivity.likes[position] = reader.getString("like_amount");
            } catch (JSONException e) {
                e.printStackTrace();
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

}