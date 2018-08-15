package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API
{
    //API should decler here
    @GET("/login")
    Observable<LoginResponse> login(@Query("username")String username, @Query("pass")String password);

    @GET("/search")
    Observable<List<SearchResult>> getSearchResult(@Query("search") String searchString);

    @POST("/sendUserLocations")
    Call<ResponseBody> sendUserLocations(@Query("locationList")List<UserLocationModel> locations);
}
