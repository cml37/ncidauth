package com.lenderman.ncidauth.rest;

import java.security.Principal;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.lenderman.ncidauth.FirebaseSender;
import com.lenderman.ncidauth.UsersToTopics;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("V1/sendMessage")
public class NcidSendFirebaseMessage
{
    /** Class logger */
    private Logger log = Logger.getLogger(NcidSendFirebaseMessage.class);

    @RolesAllowed("ALLOWED")
    @POST
    @Consumes("application/json")
    public Response handleSendMessageRequest(
            @Context SecurityContext securityContext, String json)
    {
        log.debug("ncidSendFirebaseMessage");

        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();

        String topic = UsersToTopics.getUserForTopic(username);

        if (topic == null)
        {
            JSONObject error = new JSONObject();
            error.put("Error", "Unrecognized User");
            log.error("handleSendMessageRequest: error - unrecognized user");
            return Response.serverError().entity(error.toString()).build();
        }

        else
        {
            JSONObject object = new JSONObject(json);

            if (!object.has("message"))
            {
                JSONObject error = new JSONObject();
                error.put("Error", "No value for message provided");
                log.error(
                        "handleSendMessageRequest: error - no value provided for message");
                return Response.serverError().entity(error.toString()).build();
            }
            else
            {
                if (FirebaseSender.Send(object.getString("message"), topic))
                {
                    return Response.ok().build();
                }
                else
                {
                    JSONObject error = new JSONObject();
                    error.put("Error", "Couldn't send firebase message");
                    log.error(
                            "handleSendMessageRequest: error - couldn't send firebase message");
                    return Response.serverError().entity(error.toString())
                            .build();
                }
            }
        }
    }
}