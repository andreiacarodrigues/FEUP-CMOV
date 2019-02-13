package org.feup.cmov.aef.cmov1_tic;

import android.nfc.NdefRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Utils
{
    public static String readStream(InputStream in)
    {
        BufferedReader reader = null;
        String line;
        StringBuilder response = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null)
            {
                response.append(line);
            }
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    response = new StringBuilder(e.getMessage());
                }
            }
        }
        return response.toString();
    }

    public static NdefRecord createMimeRecord(String mimeType, byte[] payload)
    {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("ISO-8859-1"));
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }
}
