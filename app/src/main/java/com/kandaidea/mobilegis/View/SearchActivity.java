package com.kandaidea.mobilegis.View;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.kandaidea.mobilegis.Adapers.OnItemClickListener;
import com.kandaidea.mobilegis.Adapers.SearchAdapter;
import com.kandaidea.mobilegis.DataModel.Models.SearchResult;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.SearchActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivitySearchBinding;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class SearchActivity extends AppCompatActivity
{
    public static final String TAG = SearchActivity.class.getSimpleName();
    private SearchActivityViewModel viewModel;
    private SearchView mSearchView;
    private ImageButton mBackArrow;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchActivityViewModel();
        binding.setSearchViewModel(viewModel);

        mBackArrow = findViewById(R.id.back_arrow_search_bar);
        mSearchView = findViewById(R.id.search_field_Search_bar);
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //should initial bundle for return nothing
                Bundle bundle = new Bundle();
                finishActivity(bundle);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SearchAdapter(this, viewModel.getSearchResult(), new OnItemClickListener()
        {
            @Override
            public void onItemClick(SearchResult item)
            {
                Bundle bundle = new Bundle();
                finishActivity(bundle);
            }
        }));
        mRecyclerView.getAdapter().notifyDataSetChanged();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                Log.d(TAG, "searchClicked");
                viewModel.retrofitMethods.search(s);
                mRecyclerView.setAdapter(new SearchAdapter(getApplicationContext(), viewModel.getSearchResult(), new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(SearchResult item)
                    {
                        Bundle bundle = new Bundle();
                        finishActivity(bundle);
                    }
                }));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {

                return false;
            }
        });
    }

    //finish activity to return to main activity
    public void finishActivity(Bundle bundle)
    {
        Intent intent = new Intent();
        intent.putExtra("result", bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
