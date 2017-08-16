package com.lenderman.ncidauth.rest;

import java.security.Principal;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.lenderman.ncidauth.UsersToTopics;

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