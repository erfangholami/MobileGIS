package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;

public class LoginActivityViewModel extends ViewModel
{
    private static final String TAG = LoginActivityViewModel.class.getSimpleName();
    private Activity mActivity;

    private TextInputEditText usernameField;
    private TextInputEditText passwordField;
    private ProgressBar progressBar;
    public void init(Activity mActivity)
    {
        this.mActivity = mActivity;
        usernameField = (mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.login_username_edit_text);
        passwordField = (mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.login_password_edit_text);
        progressBar = (mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.login_progress_bar);
    }
    public void login()
    {
        //call webMethod for login
        progressBar.setVisibility(View.VISIBLE);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Log.d(TAG, "Login information is : " + username + " " + password);
        boolean response = new RetrofitMethods().login(username , password);

        if(response)
        {
            //valid to
            Toast.makeText(mActivity.getApplicationContext(), R.string.login_msg, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "LoginSuccessful");
            Intent intent = new Intent(mActivity.getApplicationContext(), MainActivity.class );
            mActivity.startActivity(intent);
        }
        else
        {
            //invalid username or password
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "LoginFailed");
        }
    }
}
