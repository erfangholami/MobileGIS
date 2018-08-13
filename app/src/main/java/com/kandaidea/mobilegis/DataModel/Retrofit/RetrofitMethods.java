package com.kandaidea.mobilegis.DataModel.Retrofit;


import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RetrofitMethods
{

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
}
