package com.lenderman.ncidauth;

public class UserAuth
{
    public static boolean validateUser(String user, String password)
    {
        String storedPassword = DbUtils.getPasswordForUsername(user);
        return (password != null) && password.equals(storedPassword);
    }
}