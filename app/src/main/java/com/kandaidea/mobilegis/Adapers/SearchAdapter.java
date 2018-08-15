package com.kandaidea.mobilegis.Adapers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.View.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>
{
    public static final String TAG = SearchAdapter.class.getSimpleName();
    public Observable<List<SearchResult>> result;

    public void setResult(Observable<List<SearchResult>> result)
    {
        this.result = result;
    }

    private Context mContext;
    private OnItemClickListener listener;
    public List<SearchResult> ss = new ArrayList<>();
    public SearchAdapter(Context context, Observable<List<SearchResult>> result, OnItemClickListener listener)
    {
        this.result = result;
        this.mContext = context;
        this.listener = listener;
        if(result != null)
        {
            this.result.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<SearchResult>>()
                    {
                        @Override
                        public void onNext(List<SearchResult> r)
                        {
                            for (SearchResult s: r)
                            {
                                ss.add(s);
                                Log.d(TAG, "constructor" + s.getCity());
                            }
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
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(TAG, "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Log.v(TAG, "onBindViewHolder");
        holder.textView.setText(ss.get(position).getCity());
        holder.bind(ss.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return ss == null ? 0 : ss.size();
    }


    //holder class
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.city);
        }
        public void bind(final SearchResult item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
