package com.lenderman.ncidauth;

public class UsersToTopics
{
    public static String getUserForTopic(String user)
    {
        return DbUtils.getTopicForUsername(user);
    }
}