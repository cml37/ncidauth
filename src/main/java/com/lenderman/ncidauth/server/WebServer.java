package com.lenderman.ncidauth.server;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyStore;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.lenderman.ncidauth.NcidConfig;

public class WebServer
{
    public static void StartWebServer() throws Exception
    {
        Server httpServer = createHttpServer();
        httpServer.start();
        System.out.println(String
                .format("\nJersey Application Server started with WADL available at "
                        + "%sapplication.wadl\n", getURI()));
    }

    private static Server createHttpServer() throws Exception
    {
        InputStream keystoreInput = WebServer.class.getResourceAsStream(
                "/com/lenderman/ncidauth/resources/jetty_keystore.jks");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] keyPassword = NcidConfig.instance.keystorePassword.toCharArray();
        keyStore.load(keystoreInput, keyPassword);
        keystoreInput.close();

        ResourceConfig appResourceConfig = new MyApp();
        SslContextFactory factory = new SslContextFactory(true);
        factory.setKeyStore(keyStore);
        factory.setKeyManagerPassword(NcidConfig.instance.keystorePassword);
        return JettyHttpContainerFactory.createServer(getURI(), factory,
                appResourceConfig);
    }

    private static URI getURI()
    {
        return UriBuilder.fromUri("https://" + getHostName() + "/")
                .port(NcidConfig.instance.webserverHostPort).build();
    }

    private static String getHostName()
    {
        String hostName = "localhost";
        try
        {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return hostName;
    }
}