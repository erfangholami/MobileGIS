package com.kandaidea.mobilegis.DataModel.Retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance
{
    private static Retrofit mRetrofit;
    public static final String BASE_URL = "WWW.google.com";
    public static Retrofit getRetrofitInstance()
    {
        if (mRetrofit == null)
        {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
