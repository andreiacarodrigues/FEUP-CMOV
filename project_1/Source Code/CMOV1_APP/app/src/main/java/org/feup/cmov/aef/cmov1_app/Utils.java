package org.feup.cmov.aef.cmov1_app;

import android.nfc.NdefRecord;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;

public class Utils
{
    public static byte[] getBytes(Object obj)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            return bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bos.close();
            }
            catch (IOException ex)
            {
                // ignore close exception
            }
        }
        return null;
    }

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

    public static byte[] buildMessage(byte[] data)
    {
        try
        {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.KEYNAME, null);

            PrivateKey privateKey = ((KeyStore.PrivateKeyEntry)entry).getPrivateKey();

            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initSign(privateKey);
            sg.update(data, 0, data.length);
            return sg.sign();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static RSAPublicKey getPublicKey()
    {
        try
        {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.KEYNAME, null);

            return (RSAPublicKey)((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public static boolean luhnAlgorithm(String value)
    {
        int sum = 0;
        int digit;
        boolean doubleValue = false;
        for(int i = value.length() - 1; i >= 0; i--, doubleValue = !doubleValue)
        {
            digit = Character.getNumericValue(value.charAt(i));
            digit = doubleValue ? digit * 2 : digit;
            digit = digit > 9 ? digit - 9 : digit;
            sum += digit;
        }
        return sum % 10 == 0;
    }

    public static boolean nifValidation(String value)
    {
        if(value.length() != 9)
        {
            return false;
        }
        int sum = 0;
        for(int i = 7; i >= 0; i--)
        {
            char digit = value.charAt(i);
            int digitValue = Character.getNumericValue(digit);
            digitValue *= (9 - i);
            sum += digitValue;
        }
        int remainder = sum % 11;
        int checkDigit;
        if(remainder == 0)
        {
            checkDigit = 0;
        }
        else if(remainder == 1)
        {
            checkDigit = 9;
        }
        else
        {
            checkDigit = 11 - remainder;
        }
        return checkDigit == Character.getNumericValue(value.charAt(8));
    }

    public static NdefRecord createMimeRecord(String mimeType, byte[] payload)
    {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("ISO-8859-1"));
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}