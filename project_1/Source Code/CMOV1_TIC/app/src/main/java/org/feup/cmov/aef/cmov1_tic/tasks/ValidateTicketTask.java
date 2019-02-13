package org.feup.cmov.aef.cmov1_tic.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_tic.Constants;
import org.feup.cmov.aef.cmov1_tic.Utils;
import org.feup.cmov.aef.cmov1_tic.requests.ValidateTicketRequest;
import org.feup.cmov.aef.cmov1_tic.responses.ValidateTicketResponse;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValidateTicketTask extends AsyncTask<ValidateTicketRequest, Integer, ValidateTicketResponse>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostValidateTicketTask(ValidateTicketResponse result);
    }

    public ValidateTicketTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected ValidateTicketResponse doInBackground(ValidateTicketRequest... parameters)
    {
        ValidateTicketRequest parameter = parameters[0];

        Gson gson = new Gson();
        String json = gson.toJson(parameter);

        URL url;
        HttpURLConnection urlConnection = null;
        ValidateTicketResponse validateTicketResponse = new ValidateTicketResponse();
        try
        {
            url = new URL(Constants.URL_PREFIX + "/api/Ticket/ValidateTickets");
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
            switch (responseCode) {
                case 200: {
                    String response = Utils.readStream(urlConnection.getInputStream());
                    validateTicketResponse = gson.fromJson(response, ValidateTicketResponse.class);
                    validateTicketResponse.response = ValidateTicketResponse.ResponseValue.Success;
                    return validateTicketResponse;
                }
                case 404:
                    validateTicketResponse.response = ValidateTicketResponse.ResponseValue.UserNotFound;
                    return validateTicketResponse;
                case 400: {
                    String response = Utils.readStream(urlConnection.getErrorStream());
                    switch (response) {
                        case "\"Can only validate up to 4 tickets at once\"":
                            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.CanOnlyValidateUpTo4TicketsAtOnce;
                            return validateTicketResponse;
                        case "\"Couldn't find ticket\"":
                            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.CouldNotFindTicket;
                            return validateTicketResponse;
                        case "\"Couldn't find purchase\"":
                            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.CouldNotFindPurchase;
                            return validateTicketResponse;
                        case "\"Tickets are not all for the same performance\"":
                            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.TicketsAreNotAllForTheSamePerformance;
                            return validateTicketResponse;
                        case "\"Not ticket owner\"":
                            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.NotTicketOwner;
                            return validateTicketResponse;
                    }
                    break;
                }
            }
            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.UnknownError;
            return validateTicketResponse;

        }
        catch (Exception e)
        {
            validateTicketResponse.response = ValidateTicketResponse.ResponseValue.UnknownError;
            return validateTicketResponse;
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
    protected void onPostExecute(ValidateTicketResponse result)
    {
        callback.onPostValidateTicketTask(result);
    }
}

