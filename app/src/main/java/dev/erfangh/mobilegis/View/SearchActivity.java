package dev.erfangh.mobilegis.View;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import dev.erfangh.mobilegis.Adapers.OnItemClickListener;
import dev.erfangh.mobilegis.Adapers.SearchAdapter;
import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.DataModel.Models.SearchItem;
import dev.erfangh.mobilegis.DataModel.Models.SearchResult;
import dev.erfangh.mobilegis.DataModel.Retrofit.RetrofitMethods;
import dev.erfangh.mobilegis.R;
import dev.erfangh.mobilegis.viewmodel.SearchActivityViewModel;
import dev.erfangh.mobilegis.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class SearchActivity extends AppCompatActivity
{
    public static final String TAG = SearchActivity.class.getSimpleName();
    private String token;

    private SearchActivityViewModel viewModel;

    private SearchView mSearchView;
    private ImageButton mBackArrow;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchActivityViewModel();
        viewModel.init(this);
        binding.setSearchViewModel(viewModel);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString(Constants.TOKEN_KEY);

        mBackArrow = findViewById(R.id.back_arrow_search_bar);
        mSearchView = findViewById(R.id.search_field_Search_bar);
        mProgressbar = findViewById(R.id.search_progressbar);
        mSearchView.setIconified(false);

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
        mRecyclerView.setAdapter(new SearchAdapter(this, new ArrayList<SearchResult>(), new OnItemClickListener()
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
                viewModel.getSearchResult(s, token);
                mRecyclerView.setAdapter(new SearchAdapter(getApplicationContext(), new ArrayList<SearchResult>(), new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(SearchResult item)
                    {
                        mProgressbar.setVisibility(View.VISIBLE);
                        viewModel.getSearchItem(item.getId(), token);

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

    public void updateAdapterDataSet(List<SearchResult> items, int status)
    {
        ((SearchAdapter)mRecyclerView.getAdapter()).updateDataSet(items);
    }
    public void finishActivity(SearchItem item)
    {
        mProgressbar.setVisibility(View.GONE);
        String x =  Uri.decode(item.getShape().toString());

        Intent intent = new Intent();
        intent.putExtra(Constants.SEARCH_COORDINATES_KEY, x);
        intent.putExtra(Constants.SEARCH_TYPE_KEY, item.getType());
        intent.putExtra(Constants.SEARCH_NAME_KEY, item.getName());
        intent.putExtra(Constants.SEARCH_ID_KEY, item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
