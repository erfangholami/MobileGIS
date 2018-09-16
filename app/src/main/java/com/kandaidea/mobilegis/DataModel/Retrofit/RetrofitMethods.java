package com.kandaidea.mobilegis.DataModel.Retrofit;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.Address;
import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.SectorModel;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


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

    public Observable<List<SearchResult>> search(String searchText, String token)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<List<SearchResult>> call = api.getSearchResult(token, searchText);
        return call;
    }

    public Observable<SearchItem> searchById(int id, String token)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<SearchItem> call = api.getSearchItem(token, id);
        return call;
    }

    public Observable<String> sendUserLocations(String token, List<UserLocationModel> locations)
    {
        Gson gson = new Gson();
        String listString = gson.toJson(locations, new TypeToken<List<UserLocationModel>>() {}.getType()).toString();
        Log.d(TAG, "Request is : " + listString);
        return  api.sendUserLocations(Constants.RETROFIT_CONTENT_TYPE, token, listString);
    }
    public Observable<Response<Void>> uploadFile(String token, MultipartBody.Part body)
    {
        return api.uploadFile(Constants.RETROFIT_CONTENT_TYPE,token, body);
    }

    public Observable<List<SectorModel>> getSectors(String token)
    {
        return api.getSector(token);
    }
    public Observable<Address> getAddress(String token, double lat, double lng)
    {
        return api.getAddress(token, lat, lng);
    }
    public Observable<Void> sendFeedback(String token, String message)
    {
        return api.sendFeedback(token, message);
    }
}
