package com.lenderman.ncidauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class DbUtils
{
    // private static Semaphore semaphore = new Semaphore(1);

    /** Class logger */
    private static Logger log = Logger.getLogger(DbUtils.class);

    public static String getPasswordForUsername(String username)
    {
        String password = null;
        String selectTableSQL = "select password from users where username='"
                + username + "'";
        try
        {
            // semaphore.acquire();
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectTableSQL);
            while (rs.next())
            {
                password = rs.getString("password");
            }
            disconnect(conn);
            // semaphore.release();
        }
        catch (Exception e)
        {
            log.error("Issue executing getPasswordForUsername query :", e);
        }
        return password;
    }

    public static String getTopicForUsername(String username)
    {
        String topic = null;
        String selectTableSQL = "select topic from users where username='"
                + username + "'";
        try
        {
            // semaphore.acquire();
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectTableSQL);
            while (rs.next())
            {
                topic = rs.getString("topic");
            }
            disconnect(conn);
            // semaphore.release();
        }
        catch (Exception e)
        {
            log.error("Issue executing getTopicForUsername query :", e);
        }
        return topic;
    }

    private static void disconnect(Connection conn)
    {
        try
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        catch (Exception ex)
        {
            log.error("StatementExecutor - issue closing connection:", ex);
        }
    }

    private static Connection connect()
    {
        Connection conn = null;
        try
        {
            // Class.forName("org.sqlite.JDBC");
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://" + NcidConfig.instance.mariaHostname
                    + ":3306/" + NcidConfig.instance.mariaDbName;
            conn = DriverManager.getConnection(url,
                    NcidConfig.instance.mariaUsername,
                    NcidConfig.instance.mariaPassword);
        }
        catch (Exception e)
        {
            log.error("Issue connecting to database:", e);
        }
        return conn;
    }
}
