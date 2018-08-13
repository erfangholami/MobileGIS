package com.kandaidea.mobilegis.View;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.RequestPermissions;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.ViewModel.LoginActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{
    private LoginActivityViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginActivityViewModel();
        loginViewModel.init(this);
        binding.setLoginViewModel(loginViewModel);
        //return true/false
        new RequestPermissions(this).request();
    }
}
