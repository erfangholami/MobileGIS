package com.kandaidea.mobilegis.View;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kandaidea.mobilegis.Adapers.UserLocationAdapter;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.viewmodel.UserLocationsViewModel;
import com.kandaidea.mobilegis.databinding.ActivityUserLocationsBinding;

public class UserLocations extends AppCompatActivity
{
    private static final String TAG = UserLocations.class.getSimpleName();
    private String token;

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

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString(Constants.TOKEN_KEY);

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
                if(viewModel.sendToServer(token))
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
