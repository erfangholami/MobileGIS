package com.kandaidea.mobilegis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;
import com.kandaidea.mobilegis.View.Draw;
import com.kandaidea.mobilegis.View.SearchActivity;
import com.kandaidea.mobilegis.View.UserLocations;
import com.kandaidea.mobilegis.ViewModel.MapsActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityMainBinding;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.BuildConfig;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private MapsActivityViewModel mapsViewModel;
    private MapView mMapView;
    private Toolbar mToolbar;
    public MyLocationNewOverlay myLocationNewOverlay;

    //Views
    private ImageButton navigationDrawer;
    private ImageButton mMapItem;
    private ImageButton mSearchItem;
    private NavigationView mNavigationView;


    //vars
    private int mapMode = Constants.NONE;
    private Polyline mapDrawPolyline = new Polyline();
    private Polygon mapDrawPolygon = new Polygon();
    private ArrayList<Marker> areaPolygonMarkers = new ArrayList<>();

    private ArrayList<Marker> areaPolylineMarkers = new ArrayList<>();
    private Draw polygonDraw;
    private Draw polylineDraw;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        navigationDrawer = mToolbar.findViewById(R.id.navigation_drawer);
        navigationDrawer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DrawerLayout dl = findViewById(R.id.drawer_layout);
                dl.openDrawer(findViewById(R.id.nav_view));
            }
        });
        mMapItem = mToolbar.findViewById(R.id.map_item);
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
        mSearchItem = mToolbar.findViewById(R.id.search_item);
        mSearchItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, Constants.SEARCH_ACTIVITY_REQUEST_CODE);
            }
        });

        mNavigationView= findViewById(R.id.nav_view);
        mNavigationView.getMenu().getItem(Constants.EXIT_ITEM_NUMBER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });
        mNavigationView.getMenu().getItem(Constants.USER_LOCATIONS_ITEM_NUMBER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                Intent intent = new Intent(getApplicationContext(), UserLocations.class);
                startActivity(intent);
                return false;
            }
        });
        mNavigationView.getMenu().getItem(Constants.DRAW_ITEM_NUMBER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                mapMode = Constants.DRAW_POLYGON_MODE;
                return false;
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
        int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR ;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initialMapSettings()
    {
        mMapView = findViewById(R.id.map_view_main);
        mMapView.setScrollableAreaLimitDouble(Constants.MAP_BOUND);
        final IMapController mapController = mMapView.getController();
        mapController.setZoom(Constants.MIN_MAP_ZOOM_LEVEL);
        mMapView.setMaxZoomLevel(Constants.MAX_MAP_ZOOM_LEVEL);
        mMapView.setMinZoomLevel(Constants.MIN_MAP_ZOOM_LEVEL);
        IGeoPoint centerPoint = Constants.MIDDLE_IRAN;
        mapController.setCenter(centerPoint);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setMultiTouchControls(true);
        mMapView.setClickable(true);
        mMapView.setLongClickable(true);


        GpsMyLocationProvider x = new GpsMyLocationProvider(this);
        myLocationNewOverlay = new MyLocationNewOverlay(x, mMapView);
        myLocationNewOverlay.enableMyLocation();
        Bitmap myLocationLogo = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_my_location_point)).getBitmap();
        myLocationNewOverlay.setPersonHotspot(myLocationLogo.getWidth() / 2, myLocationLogo.getHeight() / 2);
        myLocationNewOverlay.setDirectionArrow(myLocationLogo, myLocationLogo);
        myLocationNewOverlay.setDrawAccuracyEnabled(true);
        mMapView.getOverlays().add(Constants.MY_LOCATION_OVERLAY_NUMBER, myLocationNewOverlay);

        initialMapClickListener();

    }
    private void initialMapClickListener()
    {
        mMapView.setLongClickable(true);
        MapEventsReceiver eventsReceiver = new MapEventsReceiver()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p)
            {
                if(mapMode == Constants.DRAW_POLYGON_MODE)
                {
                    if(mapDrawPolygon.getPoints().size() > 0)
                    {
                        polygonDraw.drawForPolygon(p);
                    }
                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean longPressHelper(GeoPoint p)
            {
                if(mapMode == Constants.DRAW_POLYGON_MODE)
                {
                    if(mapDrawPolygon.getPoints().size() == 0)
                    {
                        polygonDraw = new Draw(getApplicationContext(), mMapView, mapDrawPolygon, mapDrawPolyline, areaPolygonMarkers, areaPolylineMarkers, Constants.DRAW_POLYGON_MODE);
                        polygonDraw.drawForPolygon(p);
                    }
                }
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(this, eventsReceiver);
        mMapView.getOverlays().add(Constants.MAP_EVENT_RECEIVER_OVERLAY_NUMBER, OverlayEvents);
        areaPolygonMarkers.add(new Marker(mMapView));
        areaPolylineMarkers.add(new Marker(mMapView));
        mMapView.getOverlays().add(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mapDrawPolygon);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, areaPolygonMarkers);
        mMapView.getOverlays().add(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mapDrawPolyline);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, areaPolylineMarkers);
        areaPolylineMarkers.clear();
        areaPolygonMarkers.clear();
        mMapView.getOverlays().remove(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER);
        mMapView.getOverlays().remove(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER);
        mMapView.invalidate();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //set data to map view getting Bundle
        if(requestCode == Constants.SEARCH_ACTIVITY_REQUEST_CODE)
        {

        }
    }
}
