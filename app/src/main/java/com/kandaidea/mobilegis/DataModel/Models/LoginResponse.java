package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("LoginResult")
    private Userdata userData;

    public LoginResponse(Userdata userData)
    {
        this.userData = userData;
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
