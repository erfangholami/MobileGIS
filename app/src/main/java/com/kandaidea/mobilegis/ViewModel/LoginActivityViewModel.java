package com.kandaidea.mobilegis.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.View.LoginActivity;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginActivityViewModel extends ViewModel
{
    private static final String TAG = LoginActivityViewModel.class.getSimpleName();

    private LoginActivity mActivity;
    public void init(LoginActivity mActivity)
    {
        this.mActivity = mActivity;
    }
    public void login(final String username, String password)
    {

        new RetrofitMethods().login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginResponse>()
                {
                    int i = -1;
                    @Override
                    public void onNext(LoginResponse loginResponse)
                    {
                        Log.d(TAG, "responseIs" + loginResponse.getUserData().getUsername());
                        Log.d(TAG, "responseIs" +loginResponse.getUserData().getEmailAddress());
                        Log.d(TAG, "responseIs" +loginResponse.getUserData().getPassword());
                        if((String)loginResponse.getUserData().getUsername() == null )
                        {
                            i = Constants.LOGIN_FAILED;
                        }
                        else
                        {
                            i = Constants.LOGIN_SUCCESS;
                        }
                        Log.d(TAG, "i is : " + loginResponse.getUserData().getUsername() + " " + username +" " + i);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TAG, "responseIs" +" error" + e.getMessage());
                        mActivity.login(Constants.CONNECTION_ERROR);
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "responseIs" + " complete");
                        mActivity.login(i);
                    }
                });
    }
}
