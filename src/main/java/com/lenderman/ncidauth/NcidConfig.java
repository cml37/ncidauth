package com.lenderman.ncidauth;

public class NcidConfig
{
    public static NcidConfig instance = new NcidConfig();

    public String sqlLiteDb;
    public String keystorePassword;
    public int webserverHostPort;
    public String googleFcmApiKey;
    public String googleFcmUrl;

    static
    {
        instance.sqlLiteDb = System.getenv("SQL_LITE_DB");
        instance.googleFcmApiKey = System.getenv("GOOGLE_FCM_API_KEY");
        instance.googleFcmUrl = System.getenv("GOOGLE_FCM_URL");
        instance.keystorePassword = System.getenv("KEYSTORE_PASSWORD");
        instance.webserverHostPort = Integer
                .parseInt(System.getenv("WEB_SERVER_HOST_PORT"));
    }
}