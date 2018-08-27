package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("ErrorUsername")
    private String ErrorUsername;
    @SerializedName("ErrorPassword")
    private String ErrorPassword;
    @SerializedName("ErrorInternalServer")
    private String ErrorInternalServer;
    @SerializedName("UserData")
    private Userdata userData;

    public LoginResponse(String errorUsername, String errorPassword, String errorInternalServer, Userdata userData)
    {
        ErrorUsername = errorUsername;
        ErrorPassword = errorPassword;
        ErrorInternalServer = errorInternalServer;
        this.userData = userData;
    }

    public String getErrorUsername()
    {
        return ErrorUsername;
    }

    public void setErrorUsername(String errorUsername)
    {
        ErrorUsername = errorUsername;
    }

    public String getErrorPassword()
    {
        return ErrorPassword;
    }

    public void setErrorPassword(String errorPassword)
    {
        ErrorPassword = errorPassword;
    }

    public String getErrorInternalServer()
    {
        return ErrorInternalServer;
    }

    public void setErrorInternalServer(String errorInternalServer)
    {
        ErrorInternalServer = errorInternalServer;
    }

    public Userdata getUserData()
    {
        return userData;
    }

    public void setUserData(Userdata userData)
    {
        this.userData = userData;
    }
}
