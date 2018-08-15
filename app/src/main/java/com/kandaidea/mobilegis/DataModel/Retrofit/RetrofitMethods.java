package com.kandaidea.mobilegis.DataModel.Retrofit;


import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
    public boolean login(String username, String password)
    {
        final boolean[] valid = {false};
        Observable<LoginResponse> response = api.login(username, password);
        response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginResponse>()
                {
                    @Override
                    public void onNext(LoginResponse loginResponse)
                    {
                        valid[0] = loginResponse.isValid();
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {
                    }
                });
        //return valid[0];
        return true;
    }

    public Observable<List<SearchResult>> search(String searchText)
    {
        Log.d(TAG, "OnSearchMethod");
        Observable<List<SearchResult>> call = api.getSearchResult(searchText);
        searchResult = call;
        return call;
    }
}
