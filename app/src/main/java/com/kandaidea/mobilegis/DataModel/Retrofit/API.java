package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("/search")
    Observable<List<SearchResult>> getSearchResult(@Query("search") String searchString);

    @POST("/sendUserLocations")
    Call<ResponseBody> sendUserLocations(@Field("locationList[]")List<UserLocationModel> locations);
}
