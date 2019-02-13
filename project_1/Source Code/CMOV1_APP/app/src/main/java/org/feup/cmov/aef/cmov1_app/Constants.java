package org.feup.cmov.aef.cmov1_app;

public abstract class Constants
{
    public static final int KEY_SIZE = 512;
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String KEY_ALGO = "RSA";
    public static final String SIGN_ALGO = "SHA256WithRSA";
    public static String KEYNAME = "aRandomKeyForCMOV";
    public static String URL_PREFIX = "https://cmov1api.azurewebsites.net";
    public static int QR_CODE_DIMENSION = 500;

    // Extras
    public static final String EVENT_INFO = "EVENT_INFO";
    public static final String ORDER_INFO = "ORDER_INFO";

    public static final int SIZE_OF_INT = 4;
}