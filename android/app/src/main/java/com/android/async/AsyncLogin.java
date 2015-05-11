package com.android.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.touravel.app.LoginActivity;
import com.example.touravel.app.MainActivity;
import com.example.touravel.app.ProfileActivity;
import com.example.touravel.app.RegisterActivity;
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


public class AsyncLogin extends AsyncTask<String, Void, Void> {

    protected String url = null;
    protected int responseCode = 0;
    protected String responseStr = null;
    protected String responseAuth = null;
    protected int TIMEOUT_MILLISEC = 10000;
    protected String tempUsr;

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

            request.setHeader("email", params[1]);
            tempUsr = params[1];
            request.setHeader("password", params[2]);

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            responseCode = response.getStatusLine().getStatusCode();

            Log.i("GET", "email -> " + params[1]);
            Log.i("GET", "password -> " + params[2]);

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
        //Toast.makeText(SplashScreen.cnt, ""+responseCode, Toast.LENGTH_LONG).show();
        Log.i("POST-Response", ""+responseCode);



        boolean state = (( responseCode >= 200 ) && ( responseCode < 300));


        Log.i("state", ""+state);
        if(state)
        {
            String auth = "";
            JSONObject reader;
            try {
                reader = new JSONObject(responseStr);
                auth = reader.getString("auth-key");

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SplashScreen.cnt, "A wild error occurred!" , Toast.LENGTH_LONG).show();
            }

            SplashScreen.setAuth(auth);

            Log.i("setUsernameEmail", ""+tempUsr);

            SplashScreen.setUsernameEmail(tempUsr);

            Intent intent = new Intent(LoginActivity.cnt, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.cnt.startActivity(intent);

            LoginActivity.showProgress(false);
            //Toast.makeText(SplashScreen.cnt, "Logged In" , Toast.LENGTH_LONG).show();

        }
        else
        {
            LoginActivity.showProgress(false);
            Toast.makeText(SplashScreen.cnt, "Error !" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        LoginActivity.showProgress(false);
    }

}
