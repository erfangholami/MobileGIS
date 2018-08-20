package com.kandaidea.mobilegis.View;

import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.SettingActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity
{
    private static final String TAG = SettingActivity.class.getSimpleName();
    private SettingActivityViewModel viewModel;

    //views
    private ImageButton backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        viewModel = new SettingActivityViewModel();
        binding.setViewModel(viewModel);



        //onClick
        backArrow = findViewById(R.id.back_arrow_search_bar);
        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}
