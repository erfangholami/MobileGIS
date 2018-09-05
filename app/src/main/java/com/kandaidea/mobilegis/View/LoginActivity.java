package com.kandaidea.mobilegis.View;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.LoginResponse;
import com.kandaidea.mobilegis.DataModel.RequestPermissions;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.LoginActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityLoginBinding;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginActivityViewModel loginViewModel;

    //Views
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;
    private Button loginButton;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginActivityViewModel();
        loginViewModel.init(this);
        binding.setLoginViewModel(loginViewModel);


        //initial views
        usernameField = findViewById(R.id.login_username_edit_text);
        passwordField = findViewById(R.id.login_password_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progress_bar);

        //return true/false
        new RequestPermissions(this).request();

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if(username.length() == 0 || password.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), R.string.empty_username_password, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(username, password);
                }
            }
        });


    }
    public void login(int response)
    {
        if(response == Constants.LOGIN_SUCCESS)
        {
            Toast.makeText(getApplicationContext(), R.string.login_msg, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "LoginSuccessful");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(intent);
        }
        else if(response == Constants.LOGIN_FAILED)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.incorrect_login_msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, "LoginFailed");
        }
        else if(response == Constants.CONNECTION_ERROR)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            Log.d(TAG, "connectionError");
        }
    }
}
