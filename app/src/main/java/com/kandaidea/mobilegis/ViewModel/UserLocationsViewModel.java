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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

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


        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("GPX"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("Locations", file.getName(), requestFile);

        retrofitMethods.uploadFile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<Void>>()
                {

                    @Override
                    public void onNext(Response<Void> aVoid)
                    {
                        Log.d(TAG, "sendGpxFileString OnNext" + aVoid);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.d(TAG, "sendGpxFileString Error "+ e.getMessage());
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.d(TAG, "sendGpxFileString Complete");
                    }
                });
        //clearData();
        return true;
    }
}
