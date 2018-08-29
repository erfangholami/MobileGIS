package com.kandaidea.mobilegis.DataModel.Retrofit;


import android.annotation.SuppressLint;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.Call;

public class RetrofitMethods
{
    private static final String TAG = RetrofitMethods.class.getSimpleName();

    //api instance
    API api = RetrofitClientInstance.getRetrofitInstance().create(API.class);

    //decler methods and their response here
    @SuppressLint("CheckResult")
    public Observable<LoginResponse> login(String username, String password)
    {
        Observable<LoginResponse> response = api.login(username, password);
        return response;
    }

    public Observable<SearchResult> search(String searchText)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<SearchResult> call = api.getSearchResult(searchText);
        return call;
    }

    public Observable<SearchItem> searchBuId(int id)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<SearchItem> call = api.getSearchItem(id);
        return call;
    }

    public void sendUserLocations(List<UserLocationModel> locations)
    {
        //should send to server
        api.sendUserLocations(locations);
    }
}
