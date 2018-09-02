package com.kandaidea.mobilegis.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.GPX;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.View.UserLocations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class UserLocationsViewModel extends ViewModel
{
    private static final String TAG = UserLocationsViewModel.class.getSimpleName();
    private RetrofitMethods retrofitMethods = new RetrofitMethods();
    private Realm realm;
    private RealmConfiguration userLocationRealmConfig = new RealmConfiguration.Builder()
            .name("user_locations.realm")
            .schemaVersion(1)
            .build();
    public void init()
    {

    }
    public List<UserLocationModel> getLocations()
    {
        realm = Realm.getInstance(userLocationRealmConfig);
        List<UserLocationModel> locationModels = new ArrayList<>();
        realm.beginTransaction();
        RealmQuery<UserLocationModel> query = realm.where(UserLocationModel.class);
        realm.commitTransaction();
        for(UserLocationModel a : query.findAll())
        {
            locationModels.add(realm.copyFromRealm(a));
        }
        return locationModels;
    }
    public boolean sendToServer()
    {
        Log.d(TAG, "sendingToServer");
        retrofitMethods.sendUserLocations(getLocations());
        //clearData();
        return true;

    }
    public void clearData()
    {
        realm = Realm.getInstance(userLocationRealmConfig);
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        Log.d(TAG, "allUserLocationsDeleted");
    }
    public boolean exportToSD()
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy_MM_dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + Constants.MAIN_FOLDER + "/"+ Constants.USER_LOCATIONS_FOLDER + "/" + now.toString()+ ".gpx";

        File file = new File(mPath);
        new GPX().writePath(file, "gpsFile", getLocations());
        clearData();
        return true;
    }
}
