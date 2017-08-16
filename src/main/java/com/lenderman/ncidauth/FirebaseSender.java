/**
 */

package com.lenderman.ncidauth;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class FirebaseSender
{
    /** Class logger */
    private static Logger log = Logger.getLogger(FirebaseSender.class);

    public static boolean Send(String message, String topic)
    {
        try
        {
            JSONObject jData = new JSONObject();
            JSONObject jMessage = new JSONObject();
            jMessage.put("message", message);
            jData.put("data", jMessage);
            jData.put("to", "/topics/" + topic);

            // Create connection to send FCM Message request.
            URL url = new URL(NcidConfig.instance.googleFcmUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization",
                    "key=" + NcidConfig.instance.googleFcmApiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send FCM message content.
            log.debug(
                    "Firebase Send - Message:" + message + " Topic: " + topic);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jData.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            inputStream.close();
            conn.disconnect();

            log.debug("Firebase Send - Response: " + resp);
        }
        catch (Exception e)
        {
            log.error("Firebase Send - Error sending request: ", e);
            return false;
        }
        return true;
    }
}