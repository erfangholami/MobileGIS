package com.kandaidea.mobilegis;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kandaidea.mobilegis.Adapers.MapSettingTileAdapter;
import com.kandaidea.mobilegis.Adapers.UserOverlayAdapter;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.View.ColorFilter;
import com.kandaidea.mobilegis.View.Draw;
import com.kandaidea.mobilegis.View.SearchActivity;
import com.kandaidea.mobilegis.View.SettingActivity;
import com.kandaidea.mobilegis.View.UserLocations;
import com.kandaidea.mobilegis.ViewModel.MapsActivityViewModel;
import com.kandaidea.mobilegis.databinding.ActivityMainBinding;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.BuildConfig;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private MapsActivityViewModel mapsViewModel;
    private MapView mMapView;
    public MyLocationNewOverlay myLocationNewOverlay;

    //Views
    private Toolbar mToolbar;
    private ImageButton navigationDrawer;
    private ImageButton mMapItem;
    private ImageButton mSearchItem;
    private NavigationView mNavigationView;
    private TextView drawInformation;
    private CardView pointDetailCardView;
    private CardView mapSetting;
    private RecyclerView mapSettingTileRecycler;
    private RecyclerView mapSettingPolygonRecycler;
    private RecyclerView mapSettingPolylineRecycler;
    private RecyclerView mapSettingMarkerRecycler;
    private DrawerLayout drawerlayout;


    //vars
    private int mapMode = Constants.NONE;
    private Polyline mapDrawPolyline = new Polyline();
    private Polygon mapDrawPolygon = new Polygon();
    private ArrayList<Marker> areaPolygonMarkers = new ArrayList<>();
    private ArrayList<Marker> areaPolylineMarkers = new ArrayList<>();
    private Draw polygonDraw;
    private Draw polylineDraw;
    private Marker customMarker;


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

        //region BindViews
        drawInformation = findViewById(R.id.draw_information);
        pointDetailCardView = ((ConstraintLayout)findViewById(R.id.detail_point_view_card)).findViewById(R.id.detail_point_card_view);
        mapSetting = ((ConstraintLayout)findViewById(R.id.map_settings_view_card)).findViewById(R.id.map_setting_card_view);
        mapSettingTileRecycler = mapSetting.findViewById(R.id.tile_recycler_view);
        mapSettingPolygonRecycler = mapSetting.findViewById(R.id.polygon_recycler_view);
        mapSettingPolylineRecycler = mapSetting.findViewById(R.id.polyline_recycler_view);
        mapSettingMarkerRecycler = mapSetting.findViewById(R.id.marker_recycler_view);
        mToolbar = findViewById(R.id.main_toolbar);
        navigationDrawer = mToolbar.findViewById(R.id.navigation_drawer);
        mNavigationView= findViewById(R.id.nav_view);
        drawerlayout = findViewById(R.id.drawer_layout);

        //endregion

        //set screen settings
        initialScreenSettings();

        mapCashSet();
        initialMapSettings();




        pointDetailCardView.findViewById(R.id.share_location_point).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shareLocation(customMarker.getPosition());
            }
        });


        setAdapters();

        //region Toolbar

        navigationDrawer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawerlayout.openDrawer(findViewById(R.id.nav_view));
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
                showMainTools(false);
                if(pointDetailCardView.getVisibility() == View.VISIBLE)
                {
                    customMarker.setVisible(false);
                    pointDetailCardView.setVisibility(View.GONE);
                    mMapView.invalidate();
                }
                mapSetting.setVisibility(View.VISIBLE);
                ((RecyclerView)mapSetting.findViewById(R.id.tile_recycler_view)).getAdapter().notifyDataSetChanged();
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


        mNavigationView.getMenu().getItem(Constants.EXIT_ITEM_NUMBER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                drawerlayout.closeDrawer(findViewById(R.id.nav_view));
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.exit_item)
                        .setMessage(R.string.dialog_exit)
                        .setCancelable(false)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                finish();
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
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
                drawerlayout.closeDrawer(findViewById(R.id.nav_view));
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Draw")
                        .setMessage(R.string.dialog_choise_draw_mode)
                        .setCancelable(false)
                        .setNeutralButton("cancel", null)
                        .setNegativeButton(R.string.polygon, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                mapMode = Constants.DRAW_POLYGON_MODE;
                            }
                        })
                        .setPositiveButton(R.string.polyline, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                mapMode = Constants.DRAW_POLYLINE_MODE;
                            }
                        })
                        .show();
                return false;
            }
        });
        mNavigationView.getMenu().getItem(Constants.SETTING_ITEM_NUMBER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                drawerlayout.closeDrawer(findViewById(R.id.nav_view));
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
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
        addTileSources();
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
                if(mapMode == Constants.NONE)
                {
                    if(pointDetailCardView.getVisibility() == View.VISIBLE)
                    {
                        pointDetailCardView.setVisibility(View.GONE);
                        customMarker.setVisible(false);
                        mMapView.invalidate();
                        showMainTools(true);
                    }
                    else if(mapSetting.getVisibility() == View.VISIBLE)
                    {
                        mapSetting.setVisibility(View.GONE);
                        showMainTools(true);
                    }
                }
                if(mapMode == Constants.DRAW_POLYGON_MODE)
                {
                    if(mapDrawPolygon.getPoints().size() > 0)
                    {
                        polygonDraw.drawForPolygon(p);
                    }
                }
                if(mapMode == Constants.DRAW_POLYLINE_MODE)
                {
                    if(mapDrawPolyline.getPoints().size() > 0)
                    {
                        polylineDraw.drawForPolyline(p);
                    }
                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean longPressHelper(GeoPoint p)
            {
                if(mapMode == Constants.NONE)
                {
                    if(mapSetting.getVisibility() == View.VISIBLE)
                    {
                        mapSetting.setVisibility(View.GONE);
                    }
                    showMainTools(false);
                    showPointPopup(p);
                }
                if(mapMode == Constants.DRAW_POLYGON_MODE)
                {
                    if(mapDrawPolygon.getPoints().size() == 0)
                    {
                        areaPolygonMarkers.clear();
                        mMapView.invalidate();
                        polygonDraw = new Draw(getApplicationContext(), mMapView, mapDrawPolygon, mapDrawPolyline, areaPolygonMarkers, areaPolylineMarkers, Constants.DRAW_POLYGON_MODE);
                        polygonDraw.drawForPolygon(p);
                    }
                }
                if(mapMode == Constants.DRAW_POLYLINE_MODE)
                {
                    if(mapDrawPolyline.getPoints().size() == 0)
                    {
                        areaPolylineMarkers.clear();
                        mMapView.invalidate();
                        polylineDraw = new Draw(getApplicationContext(), mMapView, mapDrawPolygon, mapDrawPolyline, areaPolygonMarkers, areaPolylineMarkers, Constants.DRAW_POLYLINE_MODE);
                        polylineDraw.drawForPolyline(p);
                    }
                }
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(this, eventsReceiver);
        mMapView.getOverlays().add(Constants.MAP_EVENT_RECEIVER_OVERLAY_NUMBER, OverlayEvents);
        Marker x = new Marker(mMapView);
        x.setIcon(null);
        x.setImage(null);
        x.setVisible(false);
        areaPolygonMarkers.add(x);
        areaPolylineMarkers.add(x);
        x.setIcon(getResources().getDrawable(R.mipmap.ic_custom_marker));
        x.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        x.setInfoWindow(null);
        customMarker = x;
        mapDrawPolygon.setOnClickListener(new Polygon.OnClickListener()
        {
            @Override
            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos)
            {
                mapsViewModel.saveUserOverlay(mapDrawPolygon);
                Toast.makeText(getApplicationContext(), "Polygon saved !", Toast.LENGTH_SHORT).show();
                mapMode = Constants.NONE;
               polygonDraw.deleteForPolygon();
                mMapView.invalidate();
                return false;
            }
        });
        mapDrawPolyline.setOnClickListener(new Polyline.OnClickListener()
        {
            @Override
            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos)
            {
                //mapsViewModel.saveUserOverlay(mapDrawPolyline);
                Toast.makeText(getApplicationContext(), "Polyline saved !", Toast.LENGTH_SHORT).show();
                mapMode = Constants.NONE;
                polylineDraw.deleteForPolyline();
                mMapView.invalidate();
                return false;
            }
        });
        mMapView.getOverlays().add(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mapDrawPolygon);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, areaPolygonMarkers);
        mMapView.getOverlays().add(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mapDrawPolyline);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, areaPolylineMarkers);
        mMapView.getOverlays().add(Constants.DRAW_CUSTOM_MARKER_OVERLAY_NUMBER, customMarker);
        areaPolylineMarkers.clear();
        areaPolygonMarkers.clear();

        mMapView.invalidate();
    }

    private void showPointPopup(GeoPoint p)
    {
        customMarker.setVisible(true);
        customMarker.setPosition(p);
        pointDetailCardView.setVisibility(View.VISIBLE);
        TextView location = pointDetailCardView.findViewById(R.id.location_details);
        location.setText(String.valueOf(p.getLatitude() + "," + String.valueOf(p.getLongitude())));
        mMapView.getController().animateTo(customMarker.getPosition());
        mMapView.invalidate();
    }

    private void shareLocation(GeoPoint chosenPoint)
    {
        String uri = "geo:" + chosenPoint.getLatitude() + ","
                + chosenPoint.getLongitude() + "?q=" + chosenPoint.getLatitude()
                + "," + chosenPoint.getLongitude();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via :"));
    }


    private void addTileSources()
    {
        for(int i = 0; i < Constants.TILE_OVERLAIES_NUMBER.length; i++)
        {
            final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
            final ITileSource tileSource = Constants.TILE_OVERLAIES[i];
            tileProvider.setTileSource(tileSource);
            final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
            tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
            tilesOverlay.setColorFilter(new ColorFilter().getColorFilter());
            tilesOverlay.setEnabled(false);
            mMapView.getOverlays().add(Constants.TILE_OVERLAIES_NUMBER[i], tilesOverlay);
        }
    }

    private void setAdapters()
    {
        mapSettingTileRecycler.setHasFixedSize(true);
        mapSettingTileRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingTileRecycler.setAdapter(new MapSettingTileAdapter(this, mMapView));
        mapSettingTileRecycler.getAdapter().notifyDataSetChanged();

        mapSettingPolygonRecycler.setHasFixedSize(true);
        mapSettingPolygonRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingPolygonRecycler.setAdapter(new UserOverlayAdapter(mapsViewModel.getUserPolygons(0)));
        mapSettingPolygonRecycler.getAdapter().notifyDataSetChanged();

        mapSettingPolylineRecycler.setHasFixedSize(true);
        mapSettingPolylineRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingPolylineRecycler.setAdapter(new UserOverlayAdapter(mapsViewModel.getUserPolygons(1)));
        mapSettingPolylineRecycler.getAdapter().notifyDataSetChanged();

        mapSettingMarkerRecycler.setHasFixedSize(true);
        mapSettingMarkerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingMarkerRecycler.setAdapter(new UserOverlayAdapter(mapsViewModel.getUserPolygons(2)));
        mapSettingMarkerRecycler.getAdapter().notifyDataSetChanged();
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


    //region lifeCycle
    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDetach();
    }
    //endregion

}
