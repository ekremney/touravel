package AsyncTasks.com.util.R;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class AsyncChangeEmail extends AsyncTask<String, Void, Void> {

    protected JSONObject jsonObj = null;
    protected String url = null;
    protected String authKey = null;
    protected String responseStr = null;
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
                        "\"email\":\"" + params[2] + "\"," +
                        "\"email_again\":\"" + params[3] + "\"" +
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
        Toast.makeText(MainActivity.cnt, jsonObj.toString(), Toast.LENGTH_LONG).show();
        Log.i("POST", authKey + " | " + jsonObj.toString());
        Toast.makeText(MainActivity.cnt, responseStr, Toast.LENGTH_LONG).show();
        Log.i("POST-Response", responseStr);
    }

}
