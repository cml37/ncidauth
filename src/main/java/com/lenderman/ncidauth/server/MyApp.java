package com.lenderman.ncidauth.server;

import org.glassfish.jersey.server.ResourceConfig;
import com.lenderman.ncidauth.filter.AuthenticationFilter;

class MyApp extends ResourceConfig
{
    public MyApp()
    {
        packages("com.lenderman.ncidauth.rest");

        // Register Auth Filter here
        register(AuthenticationFilter.class);

    }
}
