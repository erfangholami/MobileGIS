package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("Role")
    private int role;
    @SerializedName("Token")
    private String token;

    public LoginResponse()
    {

    }
    public LoginResponse(int role, String token)
    {
        this.role = role;
        this.token = token;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
