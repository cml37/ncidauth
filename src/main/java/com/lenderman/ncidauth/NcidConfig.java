package com.lenderman.ncidauth;

public class NcidConfig
{
    public static NcidConfig instance = new NcidConfig();

    public String keystorePassword = null;
    public int webserverHostPort = 0;
    public String googleFcmApiKey = null;
    public String googleFcmUrl = null;

    public String mariaHostname = null;
    public String mariaDbName = null;
    public String mariaUsername = null;
    public String mariaPassword = null;

    static
    {
        instance.mariaDbName = System.getenv("MARIA_DB_NAME");
        instance.mariaHostname = System.getenv("MARIA_DB_HOSTNAME");
        instance.mariaUsername = System.getenv("MARIA_DB_USERNAME");
        instance.mariaPassword = System.getenv("MARIA_DB_PASSWORD");
        instance.googleFcmApiKey = System.getenv("GOOGLE_FCM_API_KEY");
        instance.googleFcmUrl = System.getenv("GOOGLE_FCM_URL");
        instance.keystorePassword = System.getenv("KEYSTORE_PASSWORD");
        try
        {
            instance.webserverHostPort = Integer
                    .parseInt(System.getenv("WEB_SERVER_HOST_PORT"));
        }
        catch (Exception ex)
        {
            // DO nothing
        }
    }
}