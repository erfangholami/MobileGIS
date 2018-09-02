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

    private Context mContext;
    private OnItemClickListener listener;
    public List<SearchResult> items;

    public SearchAdapter(Context context, List<SearchResult> items, OnItemClickListener listener)
    {
        this.items = items;
        this.mContext = context;
        this.listener = listener;
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
        //TODO set vars
        Log.v(TAG, "onBindViewHolder");
        holder.textView.setText(items.get(position).getName());
        holder.id.setText(String.valueOf(items.get(position).getId()));
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return items == null ? 0 : items.size();
    }


    public void updateDataSet(List<SearchResult> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    //holder class
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public TextView id;
        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
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
