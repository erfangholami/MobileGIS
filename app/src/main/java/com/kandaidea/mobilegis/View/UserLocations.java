package com.kandaidea.mobilegis.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kandaidea.mobilegis.Adapers.UserLocationAdapter;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.UserLocationsViewModel;
import com.kandaidea.mobilegis.databinding.ActivityUserLocationsBinding;

public class UserLocations extends AppCompatActivity
{
    private static final String TAG = UserLocations.class.getSimpleName();

    private UserLocationsViewModel viewModel;

    //views
    private RecyclerView mRecyclerView;
    private Button export;
    private Button clear;
    private Button sendServer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActivityUserLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_locations);
        viewModel = new UserLocationsViewModel();
        viewModel.init();
        binding.setLocationViewModel(viewModel);

        mRecyclerView = findViewById(R.id.user_location_recycler_view);
        export = findViewById(R.id.export);
        clear = findViewById(R.id.clear);
        sendServer = findViewById(R.id.send_server);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new UserLocationAdapter(viewModel.getLocations()));

        clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewModel.clearData();
                ((UserLocationAdapter)mRecyclerView.getAdapter()).updateDataSet(viewModel.getLocations());
                Toast.makeText(getApplicationContext(), R.string.clear_location_data_msg, Toast.LENGTH_SHORT).show();
            }
        });
        sendServer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(viewModel.sendToServer())
                {
                    ((UserLocationAdapter)mRecyclerView.getAdapter()).updateDataSet(viewModel.getLocations());
                    Toast.makeText(getApplicationContext(), R.string.clear_location_data_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        export.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(viewModel.exportToSD())
                {
                    ((UserLocationAdapter)mRecyclerView.getAdapter()).updateDataSet(viewModel.getLocations());
                    Toast.makeText(getApplicationContext(), R.string.clear_location_data_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
