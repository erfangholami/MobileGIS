package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse<T>
{
    @SerializedName("valid")
    private boolean valid;
    LoginResponse(boolean valid)
    {
        this.valid = valid;
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }
}
