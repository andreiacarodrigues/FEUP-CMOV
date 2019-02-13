package org.feup.cmov.aef.cmov1_app.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.entitites.User;
import org.feup.cmov.aef.cmov1_app.responses.UserResponse;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveManager;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterUserTask extends AsyncTask<User, Integer, RegisterUserTask.RegisterUserTaskResult>
{
    private ICallback callback;

    public interface ICallback
    {
        void onPostRegisterUserTask(RegisterUserTaskResult result);
    }

    public enum RegisterUserTaskResult
    {
        Success,
        UserWithTheSameUsernameAlreadyExists,
        UnknownError
    }

    public RegisterUserTask(ICallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected RegisterUserTaskResult doInBackground(User... users)
    {
        Gson gson = new Gson();
        String json = gson.toJson(users[0]);

        URL url;
        HttpURLConnection urlConnection = null;
        try
        {
            url = new URL(Constants.URL_PREFIX + "/api/User/Register");
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
                UserResponse userResponse = gson.fromJson(response, UserResponse.class);
                data.UserId = userResponse.id;
                data.Username = userResponse.password;
                data.Password = userResponse.password;
                data.NIF = userResponse.nif;
                data.CreditCardType = userResponse.creditCardType;
                data.CreditCardNumber = userResponse.creditCardNumber;
                data.CreditCardExpiration = userResponse.creditCardExpiration;
                manager.Save(data);
                return RegisterUserTaskResult.Success;
            }
            if (responseCode == 400)
            {
                String response = Utils.readStream(urlConnection.getErrorStream());
                if(response.equals("\"User with the same Username already exists\""))
                {
                    return RegisterUserTaskResult.UserWithTheSameUsernameAlreadyExists;
                }
            }
            return RegisterUserTaskResult.UnknownError;
        }
        catch (Exception e)
        {
            return RegisterUserTaskResult.UnknownError;
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
    protected void onPostExecute(RegisterUserTaskResult result)
    {
        callback.onPostRegisterUserTask(result);
    }
}