package com.kandaidea.mobilegis.DataModel.Retrofit;


import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API
{
    //API should decler here
    @POST("Login")
    @FormUrlEncoded
    Observable<LoginResponse> login
    (
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("GetPrimaryResult")
    @FormUrlEncoded
    Observable<List<SearchResult>> getSearchResult
    (
            @Field("searchTerm") String searchString
    );

    @POST("GetFinalResult")
    @FormUrlEncoded
    Observable<SearchItem> getSearchItem
    (
            @Field("id") int id
    );


    @POST("SendUserLocations")
    //@FormUrlEncoded
    Observable<String> sendUserLocations
    (
            @Header("Content-Type") String type,
            @Header("Token") String token,
            @Body String jsonObject
    );
}
