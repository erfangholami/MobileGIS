package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API
{
    //API should decler here
    @GET("/login")
    Observable<LoginResponse> login(@Query("username")String username, @Query("pass")String password);
}
