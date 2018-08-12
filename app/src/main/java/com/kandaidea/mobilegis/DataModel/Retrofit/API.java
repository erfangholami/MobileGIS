package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface API
{
    //API should decler here
    @GET("/login")
    Observable<LoginResponse> login();
}
