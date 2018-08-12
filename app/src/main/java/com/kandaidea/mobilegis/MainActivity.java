package com.kandaidea.mobilegis;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.kandaidea.mobilegis.ViewModel.MapsActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityMainBinding;

import org.osmdroid.bonuspack.BuildConfig;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private MapsActivityViewModel mapsViewModel;
    private MapView mMapView;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //set view by data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mapsViewModel = new MapsActivityViewModel();
        mapsViewModel.init(this);
        binding.setMapsViewModel(mapsViewModel);

        //set screen settings
        initialScreenSettings();

        mapCashSet();
        initialMapSettings();

        //region Toolbar
        mToolbar = findViewById(R.id.main_toolbar);
        ImageButton mMapItem = mToolbar.findViewById(R.id.map_item);
        mMapItem.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "mMapItemClicked");
                if(findViewById(R.id.detail_point_card_view).getVisibility() == View.VISIBLE)
                {
                    Log.d(TAG, "hiding detail point");
                    findViewById(R.id.detail_point_card_view).setVisibility(View.GONE);
                }
                showMainTools(false);

                View vieww = findViewById(R.id.map_settings_card_view);
                Animation animation = AnimationUtils.makeInAnimation(getApplicationContext(), false);
                vieww.setAnimation(animation);
                vieww.setVisibility(View.VISIBLE);
                vieww.startAnimation(animation);
            }
        });

        //endregion



    }


    private void showMainTools(boolean  show)
    {
        if(show)
        {
            findViewById(R.id.zoom_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.zoom_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.my_location_button).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.zoom_in_button).setVisibility(View.GONE);
            findViewById(R.id.zoom_out_button).setVisibility(View.GONE);
            findViewById(R.id.my_location_button).setVisibility(View.GONE);
        }
    }
    private void initialScreenSettings()
    {
        View decorView = getWindow().getDecorView();
        //hide navigation & fullscreen
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    private void mapCashSet()
    {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }
    private void initialMapSettings()
    {
        mMapView = findViewById(R.id.map_view_main);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setClickable(true);
        mMapView.setLongClickable(true);
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(getApplicationContext(), mMapView);
        mRotationGestureOverlay.setEnabled(true);
        mMapView.setMultiTouchControls(true);
        mMapView.getOverlays().add(mRotationGestureOverlay);
    }
}
