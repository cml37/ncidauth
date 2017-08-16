package com.lenderman.ncidauth;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.log4j.Logger;

public class OsUtils
{
    /** Class logger */
    private static Logger log = Logger.getLogger(NcidConfig.class);

    /**
     * Gets the directory which contains the running JAR file
     *
     * @param Class the class to assess
     * @return File representing the JAR directory
     */
    private static File getJarDir(Class<?> aclass)
    {
        URL url;
        String extURL;

        // get an url
        try
        {
            url = aclass.getProtectionDomain().getCodeSource().getLocation();
        }
        catch (SecurityException ex)
        {
            url = aclass.getResource(aclass.getSimpleName() + ".class");
        }

        // convert to external form
        extURL = url.toExternalForm();

        // prune for various cases
        if (extURL.endsWith(".jar"))
        {
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));
        }
        else
        { // from getResource
            String suffix = "/" + (aclass.getName()).replace(".", "/")
                    + ".class";
            extURL = extURL.replace(suffix, "");
            if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
            {
                extURL = extURL.substring(4, extURL.lastIndexOf("/"));
            }
        }

        // convert back to url
        try
        {
            url = new URL(extURL);
        }
        catch (MalformedURLException mux)
        {
            // leave url unchanged; probably does not happen
        }

        // convert url to File
        try
        {
            return new File(url.toURI());
        }
        catch (URISyntaxException ex)
        {
            return new File(url.getPath());
        }
    }

    /**
     * Gets the current path
     *
     * @return String the current path
     */
    public static String getCurrentPath()
    {
        String path = null;
        URL url = ClassLoader.getSystemClassLoader().getResource(".");

        if (url != null)
        {
            try
            {
                path = (URLDecoder.decode(url.getPath(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                log.warn("Couldn't decode current path: " + e);
            }
        }

        if ((path == null) && (getJarDir(NcidConfig.class) != null))
        {
            path = getJarDir(NcidConfig.class).getAbsolutePath();
        }

        if (path == null)
        {
            path = System.getenv("user.dir");
        }
        return path;
    }
}
