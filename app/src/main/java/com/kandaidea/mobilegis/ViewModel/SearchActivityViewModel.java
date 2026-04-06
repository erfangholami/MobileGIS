package com.kandaidea.mobilegis.viewmodel;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModel;
import android.net.Uri;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.View.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchActivityViewModel extends ViewModel
{
    private static final String TAG = SearchActivityViewModel.class.getSimpleName();
    private WeakReference<SearchActivity> mActivity;
    public RetrofitMethods retrofitMethods = new RetrofitMethods();

    public void init(SearchActivity mAcxtivity)
    {
        this.mActivity = new WeakReference<>(mAcxtivity);
    }

    @SuppressLint("CheckResult")
    public void getSearchResult(String search, String token)
    {

        retrofitMethods.search(search, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<SearchResult>>()
                {
                    List<SearchResult> results = new ArrayList<>();
                    @Override
                    public void onNext(List<SearchResult> searchResults)
                    {
                        SearchActivity activity = mActivity.get();
                        if (activity != null) activity.updateAdapterDataSet(searchResults, Constants.SUCCESS);
                        for (SearchResult res: searchResults)
                        {
                            Log.d(TAG, "res is : " + res.getName() + " " + res.getId());
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TAG, "ErrorOnGettingSearchResult " + e.getMessage());
                        SearchActivity activity = mActivity.get();
                        if (activity != null) activity.updateAdapterDataSet(results, Constants.ERROR);
                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }
    public void getSearchItem(int id,String token)
    {
        retrofitMethods.searchById(id, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<SearchItem>()
            {
                @Override
                public void onNext(SearchItem searchItem)
                {
                    Log.d(TAG, "FinalSearch is : " + searchItem.toString());
                    Log.d(TAG, "FinalSearch is : " + Uri.decode(searchItem.getShape().toString()));

                    SearchActivity activity = mActivity.get();
                    if (activity != null) activity.finishActivity(searchItem);
                }

                @Override
                public void onError(Throwable e)
                {
                    //TODO handle error
                    Log.d(TAG, "FinalSearch error " + e.getMessage());
                }

                @Override
                public void onComplete()
                {
                    Log.d(TAG, "FinalSearch complete ");
                }
            });
    }
}
