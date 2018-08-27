package com.kandaidea.mobilegis.DataModel.Models;

public class Userdata
{
    private String Username;
    private String Password;
    private String EmailAddress;
    private String Salt;

    public Userdata(String username, String password, String emailAddress, String salt)
    {
        Username = username;
        Password = password;
        EmailAddress = emailAddress;
        Salt = salt;
    }

    public String getUsername()
    {
        return Username;
    }

    public void setUsername(String username)
    {
        Username = username;
    }

    public String getPassword()
    {
        return Password;
    }

    public void setPassword(String password)
    {
        Password = password;
    }

    public String getEmailAddress()
    {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        EmailAddress = emailAddress;
    }

    public String getSalt()
    {
        return Salt;
    }

    public void setSalt(String salt)
    {
        Salt = salt;
    }
}
