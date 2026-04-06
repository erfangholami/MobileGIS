package dev.erfangh.mobilegis.DataModel.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import dev.erfangh.mobilegis.DataModel.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance
{
    private static Retrofit mRetrofit;
    public static Retrofit getRetrofitInstance()
    {
        if (mRetrofit == null)
        {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
