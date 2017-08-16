package com.lenderman.ncidauth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import com.google.gson.Gson;

public class NcidConfig
{
    /** Class logger */
    private static Logger log = Logger.getLogger(NcidConfig.class);

    public static NcidConfig instance = new NcidConfig();

    public String sqlLiteDb;
    public String keystorePassword;
    public int webserverHostPort;
    public String googleFcmApiKey;
    public String googleFcmUrl;

    static
    {
        Gson gson = new Gson();

        try
        {
            String path = OsUtils.getCurrentPath();

            String configFilePath = path + "/config.json";
            log.debug(
                    "The path to the configuration file is: " + configFilePath);

            InputStream is = new FileInputStream(configFilePath);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null)
            {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            instance = gson.fromJson(sb.toString(), NcidConfig.class);

            buf.close();
        }
        catch (Exception ex)
        {
            log.error("Could not open and configure configuration file: ", ex);
        }
    }
}