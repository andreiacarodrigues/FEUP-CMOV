package org.feup.cmov.aef.cmov1_app.files;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class LoadSaveManager
{
    private Context context;
    private LoadSaveData data;

    public LoadSaveManager(Context context)
    {
        this.context = context;
        this.data = new LoadSaveData();
    }

    public LoadSaveData getData()
    {
        return data;
    }

    public boolean Load()
    {
        String json = "";
        String filename = "Data";

        FileInputStream inputStream;
        try
        {
            inputStream = context.openFileInput(filename);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int read;
            while((read = inputStream.read(buffer)) > 0)
            {
                byte[] cleanBuffer = Arrays.copyOf(buffer, read);
                stringBuilder.append(new String(cleanBuffer));
            }
            inputStream.close();
            json = stringBuilder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(!json.equals(""))
        {
            Gson gson = new Gson();
            data = gson.fromJson(json, LoadSaveData.class);
            return true;
        }
        else
        {
            data = new LoadSaveData();
            return false;
        }
    }

    public void Save(LoadSaveData data)
    {
        Gson gson = new Gson();
        String json = gson.toJson(data);

        String filename = "Data";

        FileOutputStream outputStream;
        try
        {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
            this.data = data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}