package com.lenderman.ncidauth.rest;

import java.security.Principal;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.lenderman.ncidauth.UsersToTopics;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("V1/getTopic")
public class NcidGetTopicHandler
{
    /** Class logger */
    private Logger log = Logger.getLogger(NcidGetTopicHandler.class);

    @RolesAllowed("ALLOWED")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response handleGetTopicRequest(
            @Context SecurityContext securityContext)
    {
        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();

        log.debug("ncidGetTopic for user " + username);

        String topic = UsersToTopics.getUserForTopic(username);

        if (topic == null)
        {
            JSONObject error = new JSONObject();
            error.put("Error", "Unrecognized User");
            log.error("handleGetTopicRequest: error - unrecognized user");
            return Response.serverError().entity(error.toString()).build();
        }
        else
        {
            JSONObject object = new JSONObject();
            object.put("topic", topic);

            return Response.ok(object.toString(), MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}