package com.kandaidea.mobilegis.DataModel.Retrofit;


import android.annotation.SuppressLint;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RetrofitMethods
{
    private static final String TAG = RetrofitMethods.class.getSimpleName();
    public Observable<List<SearchResult>> searchResult = new Observable<List<SearchResult>>()
    {
        @Override
        protected void subscribeActual(Observer<? super List<SearchResult>> observer)
        {

        }
    };
    //api instance
    API api = RetrofitClientInstance.getRetrofitInstance().create(API.class);

    //decler methods and their response here
    @SuppressLint("CheckResult")
    public Observable<LoginResponse> login(String username, String password)
    {
        final LoginResponse[] responseLogin = new LoginResponse[1];
        Observable<LoginResponse> response = api.login(username, password);
        return response;
        /*response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginResponse>()
                {
                    @Override
                    public void onNext(LoginResponse loginResponse)
                    {
                        responseLogin[0] = loginResponse;
                        Log.d(TAG, "responseIs" + responseLogin[0].getUserData().getUsername());
                        Log.d(TAG, "responseIs" +responseLogin[0].getUserData().getEmailAddress());
                        Log.d(TAG, "responseIs" +responseLogin[0].getUserData().getPassword());
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TAG, "responseIs" +" error" + e.getMessage());
                        valid[0] = false;
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "responseIs" + " complete");
                        valid[0] = true;
                    }
                });*/
    }

    public Observable<List<SearchResult>> search(String searchText)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<List<SearchResult>> call = api.getSearchResult(searchText);
        searchResult = call;
        return call;
    }

    public void sendUserLocations(List<UserLocationModel> locations)
    {
        //should send to server
        api.sendUserLocations(locations);
    }
}
