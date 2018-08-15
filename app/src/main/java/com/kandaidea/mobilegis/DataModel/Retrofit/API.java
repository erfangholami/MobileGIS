package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API
{
    //API should decler here
    @GET("/login")
    Observable<LoginResponse> login(@Query("username")String username, @Query("pass")String password);

    @GET("/search")
    Observable<List<SearchResult>> getSearchResult(@Query("search") String searchString);
}
