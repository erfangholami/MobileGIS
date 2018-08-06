package com.kandaidea.mobilegis;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import org.osmdroid.bonuspack.BuildConfig;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity
{
    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region ScreenSettings
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
        //endregion

        mapCashSet();
        mMapView = findViewById(R.id.map_view_main);
        mMapView.setTileSource(TileSourceFactory.USGS_SAT);
        mMapView.setBuiltInZoomControls(false);
    }
    private void mapCashSet()
    {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }
}
