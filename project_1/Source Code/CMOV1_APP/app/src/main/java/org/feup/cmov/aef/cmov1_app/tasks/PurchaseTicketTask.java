package org.feup.cmov.aef.cmov1_app.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveManager;
import org.feup.cmov.aef.cmov1_app.models.PurchaseTicketParameter;
import org.feup.cmov.aef.cmov1_app.requests.PurchaseTicketRequest;
import org.feup.cmov.aef.cmov1_app.responses.PurchaseTicketResponse;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PurchaseTicketTask extends AsyncTask<PurchaseTicketParameter, Integer, PurchaseTicketTask.PurchaseTicketTaskResult>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostPurchaseTicketTask(PurchaseTicketTaskResult result);
    }

    public enum PurchaseTicketTaskResult
    {
        Success,
        InvalidSignature,
        UserNotFound,
        PerformanceNotFound,
        UnknownError
    }

    public PurchaseTicketTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected PurchaseTicketTaskResult doInBackground(PurchaseTicketParameter... parameters)
    {
        PurchaseTicketParameter parameter = parameters[0];
        String userId = MyApplication.getLoadSaveManager().getData().UserId;
        byte[] userIdByteArray = userId.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(2 * Constants.SIZE_OF_INT + userIdByteArray.length);
        byteBuffer.putInt(parameter.PerformanceId);
        byteBuffer.putInt(parameter.NumberOfTickets);
        byteBuffer.put(userIdByteArray);
        byte[] dataBuffer = byteBuffer.array();

        byte[] signature = Utils.buildMessage(dataBuffer);
        byte[] encoded = Base64.encode(signature, Base64.DEFAULT);
        String signatureString = new String(encoded, StandardCharsets.UTF_8);
        PurchaseTicketRequest purchaseTicketRequest = new PurchaseTicketRequest(parameter.PerformanceId, parameter.NumberOfTickets, userId, signatureString);

        Gson gson = new Gson();
        String json = gson.toJson(purchaseTicketRequest);

        URL url;
        HttpURLConnection urlConnection = null;
        try
        {
            url = new URL(Constants.URL_PREFIX + "/api/Purchase/MakePurchase");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches (false);
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(json);
            outputStream.flush();
            outputStream.close();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200)
            {
                LoadSaveManager manager = MyApplication.getLoadSaveManager();
                LoadSaveData data = manager.getData();
                String response = Utils.readStream(urlConnection.getInputStream());
                PurchaseTicketResponse userResponse = gson.fromJson(response, PurchaseTicketResponse.class);
                for(Ticket ticket : userResponse.tickets)
                {
                    ticket.performanceId = parameter.PerformanceId;
                }
                data.Tickets.addAll(Arrays.asList(userResponse.tickets));
                data.Vouchers.addAll(Arrays.asList(userResponse.vouchers));
                manager.Save(data);
                return PurchaseTicketTaskResult.Success;
            }
            else if(responseCode == 403)
            {
                String response = Utils.readStream(urlConnection.getErrorStream());
                if(response.equals("\"Invalid Signature\""))
                {
                    return PurchaseTicketTaskResult.InvalidSignature;
                }
            }
            else if(responseCode == 404)
            {
                String response = Utils.readStream(urlConnection.getErrorStream());
                if(response.equals("\"User\""))
                {
                    return PurchaseTicketTaskResult.UserNotFound;
                }
                else if(response.equals("\"Performance\""))
                {
                    return PurchaseTicketTaskResult.PerformanceNotFound;
                }
            }
            return PurchaseTicketTaskResult.UnknownError;

        }
        catch (Exception e)
        {
            return PurchaseTicketTaskResult.UnknownError;
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
    protected void onPostExecute(PurchaseTicketTaskResult result)
    {
        callback.onPostPurchaseTicketTask(result);
    }
}
