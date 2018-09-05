package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.kandaidea.mobilegis.DataModel.CalculateOverlay;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.Token;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayModel;
import com.kandaidea.mobilegis.DataModel.MovingDetails;
import com.kandaidea.mobilegis.DataModel.OverlayString;
import com.kandaidea.mobilegis.DataModel.Realm.RealmUserOverlays;
import com.kandaidea.mobilegis.DataModel.ScreenShot;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MapsActivityViewModel extends ViewModel
{
    public static final String TAG = MapsActivityViewModel.class.getSimpleName();
    private Activity mActivity;
    private MapView mMapView;
    public Realm userLocationRealm;
    public RealmUserOverlays realmUserOverlays;
    private MovingDetails moving;
    public String token;

    private boolean recordEnable = true;
    private boolean speedEnable = true;

    public void init(Activity mActivity)
    {
        this.mActivity = mActivity;
        Realm.init(this.mActivity.getApplicationContext());
        realmUserOverlays = new RealmUserOverlays();
        RealmConfiguration userLocationRealmConfig = new RealmConfiguration.Builder()
                .name("user_locations.realm")
                .schemaVersion(1)
                .build();
        userLocationRealm = Realm.getInstance(userLocationRealmConfig);
        mMapView = (this.mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.map_view_main);

        //make directory for screenshots
        File f = new File(Environment.getExternalStorageDirectory(), Constants.MAIN_FOLDER);
        if (!f.exists()) {
            f.mkdirs();
        }
        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + Constants.MAIN_FOLDER, Constants.SCREENSHOT_FOLDER);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        File f1l = new File(Environment.getExternalStorageDirectory() + "/" + Constants.MAIN_FOLDER, Constants.USER_LOCATIONS_FOLDER);
        if (!f1l.exists()) {
            f1l.mkdirs();
        }
        moving = new MovingDetails();
        token = new Token(mActivity.getApplicationContext()).readToken();
    }
    public void zoom(View v)
    {
        int zoomLevel = mMapView.getZoomLevel();
        if(v.getId() == R.id.zoom_out_button)
        {
            Log.d(TAG, "zoomOutClicked");
            zoomLevel = zoomLevel <= Constants.MIN_MAP_ZOOM_LEVEL ? zoomLevel : zoomLevel - 1;
        }
        else if(v.getId() == R.id.zoom_in_button)
        {
            Log.d(TAG, "zoomInClicked");
            zoomLevel = zoomLevel >= Constants.MAX_MAP_ZOOM_LEVEL ? zoomLevel : zoomLevel + 1;
        }
        mMapView.getController().setZoom(zoomLevel);
    }
    public void goMyLocation()
    {
        Log.d(TAG, "onLocationClicked" + mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER).getClass().getSimpleName());
        IGeoPoint location = ((MyLocationNewOverlay)(mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER))).getMyLocation();
        MyLocationNewOverlay x = ((MyLocationNewOverlay)(mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER)));
        if(location != null)
        {
            mMapView.getController().animateTo(location, Constants.ANIMATE_ZOOM_LEVEL, Constants.ANIMATE_SPEED);
        }
        ((MyLocationNewOverlay)(mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER))).getMyLocationProvider().startLocationProvider(new IMyLocationConsumer()
        {
            @Override
            public void onLocationChanged(Location location, IMyLocationProvider source)
            {
                if(location != null)
                {
                    if(speedEnable)
                    {
                        if(moving.start)
                        {
                            calculateAndUpdateSpeed(location);
                            mMapView.getController().animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()), Constants.ANIMATE_ZOOM_LEVEL, Constants.ANIMATE_SPEED);
                        }
                        else
                        {
                            moving.firstPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            moving.firstTime = System.currentTimeMillis();
                            moving.start = true;
                        }
                    }
                    if(recordEnable)
                    {
                        saveUserLocation(location);
                        ((MyLocationNewOverlay) (mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER))).onLocationChanged(location, source);
                    }
                }
            }
        });
    }
    public boolean takeScreenshot()
    {
        View v1 = mActivity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return new ScreenShot(bitmap).takeScreenshot();
    }

    public void saveUserLocation(Location location)
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        userLocationRealm.beginTransaction();
        Log.d(TAG, "addedLocation" + now.toString());
        userLocationRealm.insert(new UserLocationModel(now.toString(), location.getLatitude(), location.getLongitude()));
        userLocationRealm.commitTransaction();
    }

    public void saveUserOverlay(Polygon overlay)
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String x = new OverlayString().polygonToString(overlay);
        UserOverlayModel model = new UserOverlayModel(now.toString(),Constants.POLYGON_TYPE , x);
        mMapView.invalidate();
        realmUserOverlays.addOverlay(model);
    }
    public void saveUserOverlay(Polyline overlay)
    {
        //TODO save overlay in DB
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String x = new OverlayString().polylineToString(overlay);
        Log.d(TAG, x);
        UserOverlayModel model = new UserOverlayModel(now.toString(), Constants.POLYLINE_TYPE, x);
        realmUserOverlays.addOverlay(model);
    }
    public List<UserOverlayItem> getUserOverlays(int type)
    {
        return realmUserOverlays.getUserOverlay(type);
    }

    private void calculateAndUpdateSpeed(Location location)
    {
        long secondTime = System.currentTimeMillis();
        moving.speed = moving.calculateSpeed(moving.firstPoint, location, moving.firstTime, secondTime);
        moving.acc = moving.calculateAcc(moving.firstPoint, location, moving.firstTime, secondTime);
        Log.d(TAG, "movingDetails is speed : " + new CalculateOverlay().distanceTwoPoint(moving.firstPoint, new GeoPoint(location.getLatitude(), location.getLongitude())) + "," + String.valueOf(secondTime) +","+ String.valueOf(moving.firstTime));
        moving.time += secondTime - moving.firstTime;
        moving.distance += new CalculateOverlay().distanceTwoPoint(moving.firstPoint, new GeoPoint(location.getLatitude(), location.getLongitude()));

        moving.AvSpeed = moving.calculateAvSpeed();
        moving.AvAcc = moving.calculateAvAcc();
        Log.d(TAG, "movingDetails is : " + moving.speed + " " + moving.AvSpeed + " " + moving.acc + " " + moving.AvAcc);
        moving.firstTime = secondTime;
        moving.firstPoint.setCoords(location.getLatitude(), location.getLongitude());
        ((MainActivity)mActivity).updateSpeed(moving.speed);
    }

    //region GetterSetter
    public boolean isRecordEnable()
    {
        return recordEnable;
    }

    public void setRecordEnable(boolean recordEnable)
    {
        this.recordEnable = recordEnable;
    }

    public boolean isSpeedEnable()
    {
        return speedEnable;
    }

    public void setSpeedEnable(boolean speedEnable)
    {
        this.speedEnable = speedEnable;
    }
    //endregion
}
