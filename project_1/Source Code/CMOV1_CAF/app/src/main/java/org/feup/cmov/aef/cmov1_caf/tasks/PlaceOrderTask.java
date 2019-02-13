package org.feup.cmov.aef.cmov1_caf.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_caf.Constants;
import org.feup.cmov.aef.cmov1_caf.Utils;
import org.feup.cmov.aef.cmov1_caf.models.CafeteriaOrderParcel;
import org.feup.cmov.aef.cmov1_caf.responses.PlaceOrderTaskResponse;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaceOrderTask extends AsyncTask<CafeteriaOrderParcel, Integer, PlaceOrderTaskResponse>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostPlaceOrderTask(PlaceOrderTaskResponse result);
    }

    public PlaceOrderTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected PlaceOrderTaskResponse doInBackground(CafeteriaOrderParcel... parameters)
    {
        CafeteriaOrderParcel parameter = parameters[0];

        Gson gson = new Gson();
        String json = gson.toJson(parameter);

        URL url;
        HttpURLConnection urlConnection = null;
        PlaceOrderTaskResponse placeOrderTaskResponse = new PlaceOrderTaskResponse();
        try
        {
            url = new URL(Constants.URL_PREFIX + "/api/Order/PlaceOrder");
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
                    placeOrderTaskResponse = gson.fromJson(response, PlaceOrderTaskResponse.class);
                    placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.Success;
                    return placeOrderTaskResponse;
                }
                case 400: {
                    String response = Utils.readStream(urlConnection.getErrorStream());
                    switch (response) {
                        case "\"Must choose products\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.MustChooseProducts;
                            return placeOrderTaskResponse;
                        case "\"Can only used at most 2 vouchers\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.CanOnlyUsedAtMost2Vouchers;
                            return placeOrderTaskResponse;
                        case "\"User does not own voucher\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.UserDoesNotOwnVoucher;
                            return placeOrderTaskResponse;
                        case "\"Already used voucher sent\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.AlreadyUsedVoucherSent;
                            return placeOrderTaskResponse;
                        case "\"Only one 5Discount can be used per order\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.OnlyOne5DiscountCanBeUsedPerOrder;
                            return placeOrderTaskResponse;
                    }
                }
                case 403:
                {
                    String response = Utils.readStream(urlConnection.getErrorStream());
                    switch(response)
                    {
                        case "\"Invalid Signature\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.InvalidSignature;
                            return placeOrderTaskResponse;
                    }
                }
                case 404: {
                    String response = Utils.readStream(urlConnection.getErrorStream());
                    switch (response) {
                        case "\"User\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.UserNotFound;
                            return placeOrderTaskResponse;
                        case "\"Voucher\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.VoucherNotFound;
                            return placeOrderTaskResponse;
                        case "\"Product\"":
                            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.ProductNotFound;
                            return placeOrderTaskResponse;
                    }
                }
            }
            String response = Utils.readStream(urlConnection.getErrorStream());
            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.UnknownError;
            return placeOrderTaskResponse;
        }
        catch (Exception e)
        {
            placeOrderTaskResponse.response = PlaceOrderTaskResponse.ResponseValue.UnknownError;
            return placeOrderTaskResponse;
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
    protected void onPostExecute(PlaceOrderTaskResponse result)
    {
        callback.onPostPlaceOrderTask(result);
    }
}

