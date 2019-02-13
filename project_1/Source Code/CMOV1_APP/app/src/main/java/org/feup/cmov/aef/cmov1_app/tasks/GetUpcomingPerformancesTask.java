package org.feup.cmov.aef.cmov1_app.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.entitites.Performance;
import org.feup.cmov.aef.cmov1_app.responses.UpcomingPerformancesResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetUpcomingPerformancesTask extends AsyncTask<Void, Void, Performance[]>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostGetUpcomingPerfomancesTask(Performance[] performances);
    }

    public GetUpcomingPerformancesTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected Performance[] doInBackground(Void... notUsed)
    {
        URL url;
        HttpURLConnection urlConnection = null;
        try
        {
            url = new URL(Constants.URL_PREFIX + "/api/Performance/GetUpcomingPerformances");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200)
            {
                Gson gson = new Gson();
                String string = Utils.readStream(urlConnection.getInputStream());
                UpcomingPerformancesResponse upcomingPerformancesResponse = new UpcomingPerformancesResponse();
                upcomingPerformancesResponse.performances = gson.fromJson(string, Performance[].class);
                return upcomingPerformancesResponse.performances;
            }
            else
            {
                return null;
            }

        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Performance[] performances)
    {
        callback.onPostGetUpcomingPerfomancesTask(performances);
    }
}
