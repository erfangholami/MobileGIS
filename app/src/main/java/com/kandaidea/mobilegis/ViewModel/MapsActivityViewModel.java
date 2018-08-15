package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.ScreenShot;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;

public class MapsActivityViewModel extends ViewModel
{
    public static final String TAG = MapsActivityViewModel.class.getSimpleName();
    private Activity mActivity;
    private MapView mMapView;

    public void init(Activity mActivity)
    {
        this.mActivity = mActivity;
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
        IGeoPoint location = ((MyLocationNewOverlay)(mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER))).getMyLocation();
        if(location != null)
        {
            mMapView.getController().animateTo(location, Constants.ANIMATE_ZOOM_LEVEL, Constants.ANIMATE_SPEED);
        }
    }
    public boolean takeScreenshot()
    {
        View v1 = mActivity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return new ScreenShot(bitmap).takeScreenshot();
    }
}
