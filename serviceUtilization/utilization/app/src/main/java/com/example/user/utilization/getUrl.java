package com.example.user.utilization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
/**
 * Created by Utku on 18.3.2015.
 */
public class getUrl {

    public String getUrl()
    {
        String url = "";
        url = urlReturn(url);
        return url;
    }
    public static String urlReturn(String url){

        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpc = new DefaultHttpClient();
            HttpResponse httpr = httpc.execute(new HttpGet(url));
            inputStream = httpr.getEntity().getContent();

            if(inputStream != null)
                result = inputStream.toString();
            else
                result = "Url does not work";

        } catch (Exception e) {
            Log.d("Input Stream", e.getLocalizedMessage());
        }

        return result;
    }

}
