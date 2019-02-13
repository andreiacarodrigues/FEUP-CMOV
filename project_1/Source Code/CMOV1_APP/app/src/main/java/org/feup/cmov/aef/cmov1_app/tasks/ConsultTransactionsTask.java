package org.feup.cmov.aef.cmov1_app.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveManager;
import org.feup.cmov.aef.cmov1_app.responses.ConsultTransactionsResponse;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ConsultTransactionsTask extends AsyncTask<Void, Integer, ConsultTransactionsResponse>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostConsultTransactionsTask(ConsultTransactionsResponse response);
    }

    public ConsultTransactionsTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected ConsultTransactionsResponse doInBackground(Void... notUsed)
    {
        String userId = MyApplication.getLoadSaveManager().getData().UserId;

        Gson gson = new Gson();

        URL url;
        HttpURLConnection urlConnection = null;
        ConsultTransactionsResponse consultTransactionsResponse = new ConsultTransactionsResponse();
        try
        {
            String query = String.format(Locale.ENGLISH ,"?userId=%s", userId);
            url = new URL(Constants.URL_PREFIX + "/api/User/GetTransactions" + query);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200)
            {
                LoadSaveManager manager = MyApplication.getLoadSaveManager();
                LoadSaveData data = manager.getData();
                String response = Utils.readStream(urlConnection.getInputStream());
                consultTransactionsResponse = gson.fromJson(response, ConsultTransactionsResponse.class);
                consultTransactionsResponse.response = ConsultTransactionsResponse.ResponseValue.Success;
                data.Tickets = new ArrayList<>(Arrays.asList(consultTransactionsResponse.tickets));
                data.Vouchers = new ArrayList<>(Arrays.asList(consultTransactionsResponse.vouchers));
                manager.Save(data);
                return consultTransactionsResponse;
            }
            else if(responseCode == 404)
            {
                String response = Utils.readStream(urlConnection.getErrorStream());
                if(response.equals("\"User\""))
                {
                    consultTransactionsResponse.response = ConsultTransactionsResponse.ResponseValue.UserNotFound;
                    return consultTransactionsResponse;
                }
            }
            consultTransactionsResponse.response = ConsultTransactionsResponse.ResponseValue.UnknownError;
            return consultTransactionsResponse;

        }
        catch (Exception e)
        {
            consultTransactionsResponse = new ConsultTransactionsResponse();
            consultTransactionsResponse.response = ConsultTransactionsResponse.ResponseValue.UnknownError;
            return consultTransactionsResponse;
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
    protected void onPostExecute(ConsultTransactionsResponse response)
    {
        callback.onPostConsultTransactionsTask(response);
    }
}
