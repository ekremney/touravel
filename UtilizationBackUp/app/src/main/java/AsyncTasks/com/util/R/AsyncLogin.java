package AsyncTasks.com.util.R;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class AsyncLogin extends AsyncTask<String, Void, Void> {

    protected String url = null;
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

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);


            HttpClient client = new DefaultHttpClient(httpParams);
            HttpGet request = new HttpGet(url);

            request.setHeader("email", params[1]);
            request.setHeader("password", params[2]);

            HttpResponse response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());

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
        Toast.makeText(MainActivity.cnt, responseStr, Toast.LENGTH_LONG).show();
        Log.i("POST-Response", responseStr);
        MainActivity.setKey(responseStr);
    }

}
