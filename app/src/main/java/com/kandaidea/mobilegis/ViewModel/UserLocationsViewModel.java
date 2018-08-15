package com.kandaidea.mobilegis.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kandaidea.mobilegis.DataModel.Constants;
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
           locationModels.add(a);
           Log.d(TAG, "locations" + a.getTime());
        }
        return locationModels;
    }
    public void sendToServer()
    {
        Log.d(TAG, "sendingToServer");
        retrofitMethods.sendUserLocations(getLocations());
        //clearData();
    }
    public void clearData()
    {
        realm = Realm.getInstance(userLocationRealmConfig);
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        Log.d(TAG, "allUserLocationsDeleted");
    }
    public void exportToSD()
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy_MM_dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + Constants.MAIN_FOLDER + "/"+ Constants.USER_LOCATIONS_FOLDER + "/" + now.toString()+ ".txt";

        File file = new File(mPath);
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for(UserLocationModel m: getLocations())
            {
                pw.println(m.getTime() + " , " + m.getLat() + " , " + m.getLng());
            }
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
