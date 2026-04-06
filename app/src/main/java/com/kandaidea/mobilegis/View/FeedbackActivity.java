package com.kandaidea.mobilegis.View;

import androidx.databinding.DataBindingUtil;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.viewmodel.FeedbackActivityViewModel;
import com.kandaidea.mobilegis.viewmodel.LoginActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityFeedbackBinding;
import com.kandaidea.mobilegis.databinding.ActivityLoginBinding;

public class FeedbackActivity extends AppCompatActivity
{
    private static final String TAG = FeedbackActivity.class.getSimpleName();

    private TextInputEditText inputMsg;
    private AppCompatButton send;
    private FeedbackActivityViewModel viewModel;
    private RetrofitMethods retrofitMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityFeedbackBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        viewModel = new FeedbackActivityViewModel();
        viewModel.init(getApplicationContext());
        binding.setFeedbackViewModel(viewModel);

        retrofitMethods = new RetrofitMethods();

        inputMsg = findViewById(R.id.feedback_msg);
        send = findViewById(R.id.submit_feedback);

        send.setOnClickListener((View view) ->
        {
            if(inputMsg.getText().toString().length() != 0)
            {
                viewModel.sendFeedback(inputMsg.getText().toString());
                inputMsg.setText(null);
                Toast.makeText(getApplicationContext(), R.string.feedback_msg_send, Toast.LENGTH_LONG).show();
            }
        });

    }
}
