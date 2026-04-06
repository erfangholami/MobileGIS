package dev.erfangh.mobilegis.View;

import androidx.databinding.DataBindingUtil;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import dev.erfangh.mobilegis.DataModel.Retrofit.RetrofitMethods;
import dev.erfangh.mobilegis.R;
import dev.erfangh.mobilegis.viewmodel.FeedbackActivityViewModel;
import dev.erfangh.mobilegis.viewmodel.LoginActivityViewModel;
import dev.erfangh.mobilegis.databinding.ActivityFeedbackBinding;
import dev.erfangh.mobilegis.databinding.ActivityLoginBinding;

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
