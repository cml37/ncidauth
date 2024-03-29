package com.lenderman.ncidauth.filter;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import com.lenderman.ncidauth.UserAuth;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

/**
 * This filter verify the access permissions for a user based on username and
 * password provided in request
 */
@Provider
public class AuthenticationFilter
        implements jakarta.ws.rs.container.ContainerRequestFilter
{
    private Logger log = Logger.getLogger(AuthenticationFilter.class);

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response.ResponseBuilder ACCESS_DENIED = Response
            .status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource");
    private static final Response.ResponseBuilder ACCESS_FORBIDDEN = Response
            .status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!");

    @Override
    public void filter(ContainerRequestContext requestContext)
    {

        Method method = resourceInfo.getResourceMethod();
        // Access allowed for all
        if (!method.isAnnotationPresent(PermitAll.class))
        {
            // Access denied for all
            if (method.isAnnotationPresent(DenyAll.class))
            {
                log.error("filter - access is forbidden");
                requestContext.abortWith(ACCESS_FORBIDDEN.build());
                return;
            }

            // Get request headers
            final MultivaluedMap<String, String> headers = requestContext
                    .getHeaders();

            // Fetch authorization header
            final List<String> authorization = headers
                    .get(AUTHORIZATION_PROPERTY);

            // If no authorization information present; block access
            if ((authorization == null) || authorization.isEmpty())
            {
                log.error(
                        "filter - access is denied due to no authorization provided");
                requestContext.abortWith(ACCESS_DENIED.build());
                return;
            }

            // Get encoded username and password
            final String encodedUserPassword = authorization.get(0)
                    .replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            // Decode username and password
            String usernameAndPassword = new String(
                    Base64.getDecoder().decode(encodedUserPassword.getBytes()));

            // Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(
                    usernameAndPassword, ":");

            if (tokenizer.countTokens() < 2)
            {
                log.error("filter - either username or password is missing");
                requestContext.abortWith(ACCESS_DENIED.build());
                return;
            }

            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            final SecurityContext currentSecurityContext = requestContext
                    .getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext()
            {
                @Override
                public Principal getUserPrincipal()
                {

                    return new Principal()
                    {

                        @Override
                        public String getName()
                        {
                            return username;
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role)
                {
                    return true;
                }

                @Override
                public boolean isSecure()
                {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme()
                {
                    return "Bearer";
                }
            });

            // Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class))
            {
                // Is user valid?
                if (!UserAuth.validateUser(username, password))
                {
                    log.error(
                            "filter - user access is denied due to unknown username or invalid password");
                    requestContext.abortWith(ACCESS_DENIED.build());
                    return;
                }
            }
        }
    }
}
