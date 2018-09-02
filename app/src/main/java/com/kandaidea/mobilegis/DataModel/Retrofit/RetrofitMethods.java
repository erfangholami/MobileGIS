package com.kandaidea.mobilegis.DataModel.Retrofit;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;


import java.util.List;

import io.reactivex.Observable;


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

    public Observable<List<SearchResult>> search(String searchText)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<List<SearchResult>> call = api.getSearchResult(searchText);
        return call;
    }

    public Observable<SearchItem> searchById(int id)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<SearchItem> call = api.getSearchItem(id);
        return call;
    }

    public Observable<String> sendUserLocations(List<UserLocationModel> locations)
    {
        Gson gson = new Gson();
        String listString = gson.toJson(locations, new TypeToken<List<UserLocationModel>>() {}.getType()).toString();
        return  api.sendUserLocations(Constants.RETROFIT_CONTENT_TYPE,"dsdv", Uri.encode(listString));
    }
}
