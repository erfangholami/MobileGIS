package com.kandaidea.mobilegis.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kandaidea.mobilegis.Adapers.UserLocationAdapter;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.UserLocationsViewModel;
import com.kandaidea.mobilegis.databinding.ActivityUserLocationsBinding;

public class UserLocations extends AppCompatActivity
{
    private static final String TAG = UserLocations.class.getSimpleName();
    private UserLocationsViewModel viewModel;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityUserLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_locations);
        viewModel = new UserLocationsViewModel();
        viewModel.init();
        binding.setLocationViewModel(viewModel);

        mRecyclerView = findViewById(R.id.user_location_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new UserLocationAdapter(viewModel.getLocations()));

    }
}
