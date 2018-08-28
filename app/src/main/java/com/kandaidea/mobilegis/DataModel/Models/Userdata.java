package com.kandaidea.mobilegis.DataModel.Models;

import android.service.autofill.UserData;

import com.google.gson.annotations.SerializedName;

public class Userdata
{
    @SerializedName("Username")
    private String Username;
    @SerializedName("Password")
    private String Password;
    @SerializedName("EmailAddress")
    private String EmailAddress;
    @SerializedName("Salt")
    private String Salt;

    public Userdata()
    {

    }
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
