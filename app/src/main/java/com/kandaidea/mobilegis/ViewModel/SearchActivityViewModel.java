package com.kandaidea.mobilegis.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.View.SearchActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class SearchActivityViewModel extends ViewModel
{
    private static final String TAG = SearchActivityViewModel.class.getSimpleName();
    public RetrofitMethods retrofitMethods = new RetrofitMethods();
    public Observable<List<SearchResult>> getSearchResult()
    {
        return retrofitMethods.searchResult;
    }
}
