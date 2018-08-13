package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.MainActivity;
import com.kandaidea.mobilegis.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapsActivityViewModel extends ViewModel
{
    public static final String TAG = MapsActivityViewModel.class.getSimpleName();
    private Activity mActivity;
    private MapView mMapView;

    public void init(Activity mActivity)
    {
        this.mActivity = mActivity;
        mMapView = (this.mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.map_view_main);

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
}
