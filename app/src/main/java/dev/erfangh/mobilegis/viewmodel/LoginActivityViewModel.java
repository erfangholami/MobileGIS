package dev.erfangh.mobilegis.viewmodel;

import androidx.lifecycle.ViewModel;
import android.util.Log;

import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.DataModel.Models.LoginResponse;
import dev.erfangh.mobilegis.DataModel.Models.Token;
import dev.erfangh.mobilegis.DataModel.Retrofit.RetrofitMethods;
import dev.erfangh.mobilegis.View.LoginActivity;

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
    private Token token;
    public void init(LoginActivity mActivity)
    {
        this.mActivity = mActivity;
        token = new Token(this.mActivity.getApplicationContext());
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
                        Log.d(TAG, "responseIs" + loginResponse.getToken());
                        Log.d(TAG, "responseIs" +loginResponse.getRole());
                        if(loginResponse.getToken() == null )
                        {
                            //i = Constants.LOGIN_FAILED;
                            i = Constants.LOGIN_SUCCESS;
                        }
                        else
                        {
                            i = Constants.LOGIN_SUCCESS;
                            token.writeToken(loginResponse.getToken());
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TAG, "responseIs" +" error" + e.getMessage());
                        //mActivity.login(Constants.CONNECTION_ERROR);
                        mActivity.login(Constants.LOGIN_SUCCESS);
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
