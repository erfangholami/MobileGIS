package dev.erfangh.mobilegis.View;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.R;
import dev.erfangh.mobilegis.viewmodel.SettingActivityViewModel;
import dev.erfangh.mobilegis.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity
{
    private static final String TAG = SettingActivity.class.getSimpleName();
    private SettingActivityViewModel viewModel;

    //views
    private ImageButton backArrow;
    private Switch myLocationSwitch;
    private Switch scaleBarSwitch;
    private Switch followLocationSwitch;
    private Switch speedSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get input bundle
        Bundle bundle = getIntent().getExtras();

        //set view and viewModel
        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        viewModel = new SettingActivityViewModel();
        binding.setViewModel(viewModel);



        //assign container views
        backArrow = findViewById(R.id.back_arrow_search_bar);
        myLocationSwitch = findViewById(R.id.my_location_switch);
        scaleBarSwitch = findViewById(R.id.scale_bar_switch);
        followLocationSwitch = findViewById(R.id.follow_location_switch);
        speedSwitch = findViewById(R.id.speed_switch);


        myLocationSwitch.setChecked(bundle.getBoolean(Constants.MY_LOCATION_ENABLE_VALUE));
        scaleBarSwitch.setChecked(bundle.getBoolean(Constants.SCALE_BAR_ENABLE_VALUE));
        followLocationSwitch.setChecked(bundle.getBoolean(Constants.FOLLOW_LOCATION_ENABLE_VALUE));
        speedSwitch.setChecked(bundle.getBoolean(Constants.SPEED_ENABLE_VALUE));
        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finishActivity();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finishActivity();
    }
    private void finishActivity()
    {
        Intent intent = new Intent();
        intent.putExtra(Constants.MY_LOCATION_ENABLE_VALUE, myLocationSwitch.isChecked());
        intent.putExtra(Constants.SCALE_BAR_ENABLE_VALUE, scaleBarSwitch.isChecked());
        intent.putExtra(Constants.FOLLOW_LOCATION_ENABLE_VALUE, followLocationSwitch.isChecked());
        intent.putExtra(Constants.SPEED_ENABLE_VALUE, speedSwitch.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }
}
