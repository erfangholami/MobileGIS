package com.kandaidea.mobilegis;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.kandaidea.mobilegis.DataModel.Constants;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraHttpSender;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfiguration;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilderFactory;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application
{
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        //Map map = new HashMap<String, String>();
        //map.put("Content-Type", "application/x-www-form-urlencoded");
        String phoneName = null;
        int androidVersion ;
        phoneName = Build.PRODUCT;
        androidVersion = Build.VERSION.SDK_INT;

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.KEY_VALUE_LIST);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri(Constants.BASE_URL + "ReportIssue")
                //.setHttpHeaders(map)
                .setHttpMethod(HttpSender.Method.POST)
                .setEnabled(true);
        //builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class).setResText(R.string.acra_toast_text).setLength(Toast.LENGTH_LONG).setEnabled(true);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setPositiveButtonText("Yes").setNegativeButtonText("No").setTitle("Something wrong!").setText("Do you want to report error to server?").setEnabled(true).setResTheme(R.style.report_dialog_style);

        ACRA.init(this, builder);
        ACRA.getErrorReporter().putCustomData("phone_name", phoneName);
        ACRA.getErrorReporter().putCustomData("android_version", String.valueOf(androidVersion));

    }
}
