package com.example.user.utilization;

/**
 * Created by Utku on 18.3.2015.
 */
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
public class JsonUtilization extends Activity {

    public JsonUtilization() {
    }

    getUrl g1 = new getUrl();
    String url = g1.getUrl();

    public String readJSONObject(String url) {

        StringBuilder builder = new StringBuilder();
        HttpResponse httpr;
        HttpClient httpc = new DefaultHttpClient();
        HttpGet httpg = new HttpGet(url);

        try {

            httpr = httpc.execute(httpg);
            StatusLine statusLine = httpr.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = httpr.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String temp = "";
                while ((temp = reader.readLine()) != null) {
                    builder.append(temp);
                }
                inputStream.close();

            } else {
                Log.d("JSON", "Failed to download file");
            }

        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }

        return builder.toString();

    }


}
