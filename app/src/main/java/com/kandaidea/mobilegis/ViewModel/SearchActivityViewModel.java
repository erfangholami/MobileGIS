package com.kandaidea.mobilegis.ViewModel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.SearchItem;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.View.SearchActivity;

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
    private SearchActivity mActivity;
    public RetrofitMethods retrofitMethods = new RetrofitMethods();

    public void init(SearchActivity mAcxtivity)
    {
        this.mActivity = mAcxtivity;
    }

    @SuppressLint("CheckResult")
    public void getSearchResult(String search)
    {

        retrofitMethods.search(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<SearchResult>>()
                {
                    List<SearchResult> results = new ArrayList<>();
                    @Override
                    public void onNext(List<SearchResult> searchResults)
                    {
                        mActivity.updateAdapterDataSet(searchResults, Constants.SUCCESS);
                        for (SearchResult res: searchResults)
                        {
                            Log.d(TAG, "res is : " + res.getName() + " " + res.getId());
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mActivity.updateAdapterDataSet(results, Constants.ERROR);
                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }
    public void getSearchItem(int id)
    {
        retrofitMethods.searchById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<SearchItem>()
            {
                @Override
                public void onNext(SearchItem searchItem)
                {
                    Log.d(TAG, "final search is : " + searchItem.toString());
                    mActivity.finishActivity(searchItem);
                }

                @Override
                public void onError(Throwable e)
                {
                    //TODO handle error
                }

                @Override
                public void onComplete()
                {

                }
            });
    }
}
