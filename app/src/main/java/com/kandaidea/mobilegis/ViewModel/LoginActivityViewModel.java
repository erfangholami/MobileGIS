package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.Models.Userdata;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivityViewModel extends ViewModel
{
    private static final String TAG = LoginActivityViewModel.class.getSimpleName();

    public void init()
    {

    }
    public int login(String username, String password)
    {
        String x = new String() ;
        new RetrofitMethods().login(username , password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .blockingFirst().getUserData().getUsername();
        if(x != null)
            return 1;
        else
            return 0;
    }
}
