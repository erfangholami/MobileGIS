package com.kandaidea.mobilegis;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kandaidea.mobilegis.Adapers.MapSettingTileAdapter;
import com.kandaidea.mobilegis.Adapers.UserOverlayAdapter;
import com.kandaidea.mobilegis.DataModel.CalculateOverlay;
import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.DataModel.MovingDetails;
import com.kandaidea.mobilegis.DataModel.ScreenShot;
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
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;


public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private MapsActivityViewModel mapsViewModel;
    private MapView mMapView;
    public MyLocationNewOverlay myLocationNewOverlay;
    private ScaleBarOverlay mScaleBarOverlay;

    //Views
    private Toolbar mToolbar;
    private ImageButton navigationDrawer;
    private ImageButton mMapItem;
    private ImageButton mSearchItem;
    private ImageView mScreenShot;
    private NavigationView mNavigationView;
    private TextView drawInformation;
    private CardView pointDetailCardView;
    private CardView mapSetting;
    private RecyclerView mapSettingTileRecycler;
    private RecyclerView mapSettingPolygonRecycler;
    private RecyclerView mapSettingPolylineRecycler;
    private RecyclerView mapSettingMarkerRecycler;
    private DrawerLayout drawerlayout;
    private ImageButton myLocation;
    private ImageButton zoomIn;
    private ImageButton zoomOut;
    private TextView speed;

    //menu items
    private Menu drawerMenu;
    private MenuItem directionItem;
    private MenuItem sectorItem;
    private MenuItem drawItem;
    private MenuItem gotoItem;
    private MenuItem takePhotoItem;
    private MenuItem userLocationsItem;
    private MenuItem kmlItem;
    private MenuItem settingsItem;
    private MenuItem exitItem;




    //vars
    private int mapMode = Constants.NONE;
    private Polyline mapDrawPolyline = new Polyline();
    private Polygon mapDrawPolygon = new Polygon();
    private ArrayList<Marker> areaPolygonMarkers = new ArrayList<>();
    private ArrayList<Marker> areaPolylineMarkers = new ArrayList<>();
    private Draw polygonDraw;
    private Draw polylineDraw;
    private Marker customMarker;
    private List<Overlay> searchOverlays = new ArrayList<>();
    private String mCurrentPhotoPath;

    private Polygon.OnClickListener mapDrawPolygonListener;
    private Polyline.OnClickListener mapDrawPolylineListener;

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
        mScreenShot = mToolbar.findViewById(R.id.screenshot_item);
        mNavigationView= findViewById(R.id.nav_view);
        drawerlayout = findViewById(R.id.drawer_layout);
        speed = findViewById(R.id.speed);

        //main tool bar items
        mMapItem = mToolbar.findViewById(R.id.map_item);
        mSearchItem = mToolbar.findViewById(R.id.search_item);

        //initial drawerMenu items
        drawerMenu = mNavigationView.getMenu();
        directionItem = drawerMenu.getItem(Constants.DIRECTION_ITEM_NUMBER);
        sectorItem = drawerMenu.getItem(Constants.HISTOGRAM_ITEM_NUMBER);
        drawItem = drawerMenu.getItem(Constants.DRAW_ITEM_NUMBER);
        gotoItem = drawerMenu.getItem(Constants.GOTO_ITEM_NUMBER);
        takePhotoItem = drawerMenu.getItem(Constants.TAKE_PHOTO_ITEM_NUMBER);
        userLocationsItem = drawerMenu.getItem(Constants.USER_LOCATIONS_ITEM_NUMBER);
        kmlItem = drawerMenu.getItem(Constants.OPEN_KML_ITEM_NUMBER);
        settingsItem = drawerMenu.getItem(Constants.SETTING_ITEM_NUMBER);
        exitItem = drawerMenu.getItem(Constants.EXIT_ITEM_NUMBER);


        //mainTools
        zoomIn = findViewById(R.id.zoom_in_button);
        zoomOut = findViewById(R.id.zoom_out_button);
        myLocation = findViewById(R.id.my_location_button);

        //endregion

        //set screen settings
        initialScreenSettings();

        mapCashSet();
        initialMapSettings();




        pointDetailCardView.findViewById(R.id.share_location_point).setOnClickListener((View view) ->
            shareLocation(customMarker.getPosition()));
        pointDetailCardView.findViewById(R.id.make_direction_point).setOnClickListener((View view) ->
        {
            mapsViewModel.getRoad(((MyLocationNewOverlay) mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER)).getMyLocation(),
                    customMarker.getPosition());
            pointDetailCardView.setVisibility(View.GONE);
            customMarker.setVisible(false);
            mMapView.invalidate();
            showMainTools(true);
        });


        setAdapters();

        //region Toolbar Items

        navigationDrawer.setOnClickListener((View view) ->
        {
            drawerlayout.openDrawer(findViewById(R.id.nav_view));
        });
        mScreenShot.setOnClickListener((View view) ->
        {
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            if(new ScreenShot(bitmap).takeScreenshot())
            {
                Toast.makeText(getApplicationContext(), R.string.screenshot_ok_msg, Toast.LENGTH_SHORT).show();
            }
        });
        mMapItem.setOnClickListener((View view) ->
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
            ((UserOverlayAdapter)mapSettingPolygonRecycler.getAdapter()).updateDataSet(mapsViewModel.getUserOverlays(Constants.POLYGON_TYPE));
            ((UserOverlayAdapter)mapSettingPolylineRecycler.getAdapter()).updateDataSet(mapsViewModel.getUserOverlays(Constants.POLYLINE_TYPE));
            ((UserOverlayAdapter)mapSettingMarkerRecycler.getAdapter()).updateDataSet(mapsViewModel.getUserOverlays(Constants.MARKER_TYPE));
        });
        mSearchItem.setOnClickListener((View view) ->
        {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(Constants.TOKEN_KEY, mapsViewModel.token);
                startActivityForResult(intent, Constants.SEARCH_ACTIVITY_REQUEST_CODE);
        });

        //endregion

        //region Drawer Items
        directionItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            return false;
        });
        sectorItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            mapsViewModel.getSectors();
            return false;
        });
        drawItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            drawerlayout.closeDrawer(findViewById(R.id.nav_view));
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Draw")
                    .setMessage(R.string.dialog_choise_draw_mode)
                    .setCancelable(false)
                    .setNeutralButton("cancel", null)
                    .setNegativeButton(R.string.polygon, (DialogInterface dialogInterface, int i) ->
                    {
                        mapMode = Constants.DRAW_POLYGON_MODE;
                        mapDrawPolygon.setPoints(new ArrayList<>());
                    })
                    .setPositiveButton(R.string.polyline, (DialogInterface dialogInterface, int i) ->
                    {
                        mapMode = Constants.DRAW_POLYLINE_MODE;
                        mapDrawPolyline.setPoints(new ArrayList<>());
                    })
                    .show();
            return false;
        });
        gotoItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            drawerlayout.closeDrawer(findViewById(R.id.nav_view));
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.goto_layout);
            dialog.setCancelable(false);
            final TextInputEditText lat = dialog.findViewById(R.id.get_latitude);
            final TextInputEditText lng = dialog.findViewById(R.id.get_longitude);
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener((View view) ->
            {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.goto_location).setOnClickListener((View view) ->
            {
                if(lat.getText().length() != 0 && lng.getText().length() != 0 )
                {
                    GeoPoint p = new GeoPoint(Double.valueOf(lat.getText().toString()), Double.valueOf(lng.getText().toString()));
                    showPointPopup(p);
                    mMapView.getController().animateTo(p, Constants.ANIMATE_ZOOM_LEVEL, Constants.ANIMATE_SPEED);
                    showMainTools(false);
                    dialog.dismiss();
                }

            });
            dialog.show();
            return false;
        });
        takePhotoItem.setOnMenuItemClickListener((MenuItem item) ->
        {
            Intent takePhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhoto.resolveActivity(getPackageManager()) != null)
            {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = null;
                try
                {
                    image = File.createTempFile(imageFileName, ".jpg", storageDir);
                    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(takePhoto, Constants.IMAGE_CAPTURE_REQUEST_CODE);
            }
            return false;
        });
        userLocationsItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            Intent intent = new Intent(getApplicationContext(), UserLocations.class);
            intent.putExtra(Constants.TOKEN_KEY, mapsViewModel.token);
            startActivity(intent);
            return false;
        });
        kmlItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            new GetKml().execute();
            return false;
        });
        settingsItem.setOnMenuItemClickListener((MenuItem menuItem) ->
        {
            drawerlayout.closeDrawer(findViewById(R.id.nav_view));
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            intent.putExtra(Constants.MY_LOCATION_ENABLE_VALUE, mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER).isEnabled());
            intent.putExtra(Constants.SCALE_BAR_ENABLE_VALUE, mMapView.getOverlays().get(Constants.SCALE_BAR_OVERLAY_NUMBER).isEnabled());
            intent.putExtra(Constants.FOLLOW_LOCATION_ENABLE_VALUE, mapsViewModel.isRecordEnable());
            intent.putExtra(Constants.SPEED_ENABLE_VALUE, mapsViewModel.isSpeedEnable());
            startActivityForResult(intent, Constants.SETTING_ACTIVITY_REQUEST_CODE);
            return false;
        });
        exitItem.setOnMenuItemClickListener((MenuItem item) ->
        {
            drawerlayout.closeDrawer(findViewById(R.id.nav_view));
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.exit_item)
                    .setMessage(R.string.dialog_exit)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no, (DialogInterface dialogInterface, int i) -> {})
                    .setPositiveButton(R.string.yes, (DialogInterface dialogInterface, int i) ->
                    {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .show();
            return false;

        });

        //endregion
    }

    private void initialScreenSettings()
    {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR ;
        decorView.setSystemUiVisibility(uiOptions);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void mapCashSet()
    {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }


    //find mapView, add default setting, add tile sources, add myLocation and scaleBar & initial mapClickListener
    private void initialMapSettings()
    {
        mMapView = findViewById(R.id.map_view_main);

        //region default settings and tileSources
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

        addTileSources();

        //endregion

        //region MyLocation & ScaleBar
        GpsMyLocationProvider x = new GpsMyLocationProvider(getBaseContext());
        x.addLocationSource(LocationManager.GPS_PROVIDER);
        myLocationNewOverlay = new MyLocationNewOverlay(x, mMapView);
        myLocationNewOverlay.enableMyLocation();
        Bitmap myLocationLogo = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_my_location_point)).getBitmap();
        myLocationNewOverlay.setPersonHotspot(myLocationLogo.getWidth() / 2, myLocationLogo.getHeight() / 2);
        myLocationNewOverlay.setDirectionArrow(myLocationLogo, myLocationLogo);
        myLocationNewOverlay.setDrawAccuracyEnabled(true);
        mMapView.getOverlayManager().add(Constants.MY_LOCATION_OVERLAY_NUMBER, myLocationNewOverlay);
        mMapView.invalidate();

        //add scale bar
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(mMapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(300, dm.heightPixels - 150);
        mMapView.getOverlays().add(Constants.SCALE_BAR_OVERLAY_NUMBER, mScaleBarOverlay);
        mMapView.invalidate();

        //endregion

        initialMapClickListener();
    }

    //behave when user interact with mapView
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
                        polygonDraw = new Draw(getApplicationContext(), mMapView, mapDrawPolygon, mapDrawPolyline, areaPolygonMarkers, areaPolylineMarkers, Constants.DRAW_POLYGON_MODE, mapDrawPolygonListener, mapDrawPolylineListener, drawInformation);
                        polygonDraw.drawForPolygon(p);
                    }
                }
                if(mapMode == Constants.DRAW_POLYLINE_MODE)
                {
                    if(mapDrawPolyline.getPoints().size() == 0)
                    {
                        areaPolylineMarkers.clear();
                        mMapView.invalidate();
                        polylineDraw = new Draw(getApplicationContext(), mMapView, mapDrawPolygon, mapDrawPolyline, areaPolygonMarkers, areaPolylineMarkers, Constants.DRAW_POLYLINE_MODE, mapDrawPolygonListener, mapDrawPolylineListener, drawInformation);
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
        mapDrawPolygonListener = new Polygon.OnClickListener()
        {
            @Override
            public boolean onClick(final Polygon polygon, MapView mapView, GeoPoint eventPos)
            {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.save_overlay_layout);
                dialog.setCancelable(false);
                ((SeekBar)dialog.findViewById(R.id.stroke_size_seek)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                    {
                        polygon.setStrokeWidth(seekBar.getProgress() / 10);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {

                    }
                });
                Log.d(TAG, "polygon width is : " + polygon.getStrokeWidth());
                dialog.findViewById(R.id.selected_color_fill).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        new ChromaDialog.Builder()
                                .initialColor(Color.GREEN)
                                .colorMode(ColorMode.ARGB) // There's also ARGB and HSV
                                .onColorSelected(new ColorSelectListener()
                                {
                                    @Override
                                    public void onColorSelected(int i)
                                    {
                                        polygon.setFillColor(i);
                                        dialog.findViewById(R.id.selected_color_fill).setBackgroundColor(i);
                                    }
                                })
                                .create()
                                .show(getSupportFragmentManager(), "ChromaDialog");
                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.clear_overlay).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mapDrawPolygon.getPoints().clear();
                        Toast.makeText(getApplicationContext(), "Polygon cleared ", Toast.LENGTH_SHORT).show();
                        mapMode = Constants.NONE;
                        drawInformation.setVisibility(View.GONE);
                        polygonDraw.deleteForPolygon();
                        mMapView.invalidate();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.save_overlay).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        mapsViewModel.saveUserOverlay(polygon, ((TextInputEditText)dialog.findViewById(R.id.enter_description)).getText().toString());
                        mapDrawPolygon.getPoints().clear();
                        Toast.makeText(getApplicationContext(), "Polygon saved !", Toast.LENGTH_SHORT).show();
                        mapMode = Constants.NONE;
                        drawInformation.setVisibility(View.GONE);
                        polygonDraw.deleteForPolygon();
                        mMapView.invalidate();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        };
        mapDrawPolygon.setOnClickListener(mapDrawPolygonListener);
        mapDrawPolylineListener = new Polyline.OnClickListener()
        {
            @Override
            public boolean onClick(final Polyline polyline, MapView mapView, GeoPoint eventPos)
            {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.save_overlay_layout);
                dialog.setCancelable(false);
                ((SeekBar)dialog.findViewById(R.id.stroke_size_seek)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                    {
                        polyline.setWidth(seekBar.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {

                    }
                });
                dialog.findViewById(R.id.selected_color_fill).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        new ChromaDialog.Builder()
                                .initialColor(Color.GREEN)
                                .colorMode(ColorMode.ARGB) // There's also ARGB and HSV
                                .onColorSelected(new ColorSelectListener()
                                {
                                    @Override
                                    public void onColorSelected(int i)
                                    {
                                        polyline.setColor(i);
                                        dialog.findViewById(R.id.selected_color_fill).setBackgroundColor(i);
                                    }
                                })
                                .create()
                                .show(getSupportFragmentManager(), "ChromaDialog");
                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.clear_overlay).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(getApplicationContext(), "Polyline cleared !", Toast.LENGTH_SHORT).show();
                        mapMode = Constants.NONE;
                        drawInformation.setVisibility(View.GONE);
                        polylineDraw.deleteForPolyline();
                        mMapView.invalidate();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.save_overlay).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mapsViewModel.saveUserOverlay(polyline, ((TextInputEditText)dialog.findViewById(R.id.enter_description)).getText().toString());
                        Toast.makeText(getApplicationContext(), "Polyline saved !", Toast.LENGTH_SHORT).show();
                        mapMode = Constants.NONE;
                        drawInformation.setVisibility(View.GONE);
                        polylineDraw.deleteForPolyline();
                        mMapView.invalidate();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        };
        mapDrawPolyline.setOnClickListener(mapDrawPolylineListener);
        mMapView.getOverlays().add(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mapDrawPolygon);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, areaPolygonMarkers);
        mMapView.getOverlays().add(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mapDrawPolyline);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, areaPolylineMarkers);
        mMapView.getOverlays().add(Constants.DRAW_CUSTOM_MARKER_OVERLAY_NUMBER, customMarker);
        areaPolylineMarkers.clear();
        areaPolygonMarkers.clear();

        mMapView.invalidate();

        //add userOverlays to map
        List<Polygon> items = new ArrayList<>();
        List<Polyline> itemss = new ArrayList<>();
        Polygon xx = new Polygon();
        Polyline xxx = new Polyline();
        items.add(xx);
        itemss.add(xxx);
        searchOverlays.add(xx);
        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, items);
        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, itemss);
        mMapView.getOverlays().addAll(Constants.DRAW_USER_SEARCH_ITEM_OVERLAY_NUMBER, searchOverlays);
        mMapView.getOverlays().add(Constants.ROAD_ITEM_OVERLAY_NUMBER, customMarker);
        mMapView.getOverlays().addAll(Constants.SECTOR_ITEM_OVERLAY_NUMBER, items);

        FolderOverlay kmlOverlay = new FolderOverlay();
        mMapView.getOverlays().add(Constants.KML_OVERLAY_NUMBER, kmlOverlay);
        //TODO add marker list draw to the map
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
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_location_msg)));
    }

    //copy location to clipboard
    private void copyLocation(GeoPoint point)
    {
        String text = "p=" + point.getLatitude() + "," + point.getLongitude();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("locationPoint", text);
        clipboard.setPrimaryClip(clip);
        Log.v(TAG, getString(R.string.copy_clipboard_msg));
        Toast.makeText(this, R.string.copy_clipboard_msg, Toast.LENGTH_SHORT).show();
    }


    //region add default tile sources to mapOverlays
    private void addTileSources()
    {
        for(int i = Constants.TILE_OVERLAIES_NUMBER.length - 1; i >= 0 ; i--)
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
    //endregion

    //region setMainPageRecyclerAdapter
    private void setAdapters()
    {
        mapSettingTileRecycler.setHasFixedSize(true);
        mapSettingTileRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingTileRecycler.setAdapter(new MapSettingTileAdapter(this, mMapView));
        mapSettingTileRecycler.getAdapter().notifyDataSetChanged();

        mapSettingPolygonRecycler.setHasFixedSize(true);
        mapSettingPolygonRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingPolygonRecycler.setAdapter(new UserOverlayAdapter(mMapView, mapsViewModel.getUserOverlays(Constants.POLYGON_TYPE)));
        mapSettingPolygonRecycler.getAdapter().notifyDataSetChanged();

        mapSettingPolylineRecycler.setHasFixedSize(true);
        mapSettingPolylineRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingPolylineRecycler.setAdapter(new UserOverlayAdapter(mMapView, mapsViewModel.getUserOverlays(Constants.POLYLINE_TYPE)));
        mapSettingPolylineRecycler.getAdapter().notifyDataSetChanged();

        mapSettingMarkerRecycler.setHasFixedSize(true);
        mapSettingMarkerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mapSettingMarkerRecycler.setAdapter(new UserOverlayAdapter(mMapView, mapsViewModel.getUserOverlays(Constants.MARKER_TYPE)));
        mapSettingMarkerRecycler.getAdapter().notifyDataSetChanged();
    }
    //endregion

    // region Show or Hide three main tools
    private void showMainTools(boolean  show)
    {
        if(show)
        {
            zoomIn.setVisibility(View.VISIBLE);
            zoomOut.setVisibility(View.VISIBLE);
            myLocation.setVisibility(View.VISIBLE);
        }
        else
        {
            zoomIn.setVisibility(View.GONE);
            zoomOut.setVisibility(View.GONE);
            myLocation.setVisibility(View.GONE);
        }
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //set data to map view getting Bundle
        switch (requestCode)
        {
            case Constants.SEARCH_ACTIVITY_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK)
                {
                    //TODO (GET BUNDLE) get bundle and convert it to SearchitemItem type
                    Bundle bundle = data.getExtras();
                    String cord = bundle.getString(Constants.SEARCH_COORDINATES_KEY);
                    String type = bundle.getString(Constants.SEARCH_TYPE_KEY);
                    String name = bundle.getString(Constants.SEARCH_NAME_KEY);
                    int id = bundle.getInt(Constants.SEARCH_ID_KEY);
                    searchOverlays.clear();
                    //add overlay to array
                    Marker x = new Marker(mMapView);
                    x.setPosition(new GeoPoint(15d, 15d));
                    searchOverlays.add(x);
                    mapMode = Constants.SHOW_SEARCH_MODE;
                    mMapView.getOverlays().remove(Constants.DRAW_USER_SEARCH_ITEM_OVERLAY_NUMBER);
                    mMapView.getOverlays().addAll(Constants.DRAW_USER_SEARCH_ITEM_OVERLAY_NUMBER, searchOverlays);
                    mMapView.getController().animateTo(x.getPosition(), Constants.ANIMATE_ZOOM_LEVEL, Constants.ANIMATE_SPEED);
                    mMapView.invalidate();

                }
                break;
            }
            case Constants.SETTING_ACTIVITY_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK)
                {
                    Bundle bundle = data.getExtras();
                    mMapView.getOverlays().get(Constants.MY_LOCATION_OVERLAY_NUMBER).setEnabled(bundle.getBoolean(Constants.MY_LOCATION_ENABLE_VALUE));
                    mMapView.getOverlays().get(Constants.SCALE_BAR_OVERLAY_NUMBER).setEnabled(bundle.getBoolean(Constants.SCALE_BAR_ENABLE_VALUE));
                    mapsViewModel.setRecordEnable(bundle.getBoolean(Constants.FOLLOW_LOCATION_ENABLE_VALUE));
                    mapsViewModel.setSpeedEnable(bundle.getBoolean(Constants.SPEED_ENABLE_VALUE));
                    if(bundle.getBoolean(Constants.SPEED_ENABLE_VALUE))
                    {
                        speed.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        speed.setVisibility(View.GONE);
                    }
                    mMapView.invalidate();
                }
                break;
            }
            case Constants.IMAGE_CAPTURE_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK && data != null)
                {
                    Bitmap mImageBitmap = null;
                    try
                    {
                        mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    new ScreenShot(mImageBitmap).savePhoto();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if(mapMode == Constants.SHOW_SEARCH_MODE)
        {
            mMapView.getOverlays().get(Constants.DRAW_USER_SEARCH_ITEM_OVERLAY_NUMBER).setEnabled(false);
            mMapView.invalidate();
            mapMode = Constants.NONE;
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void updateSpeed(double sp)
    {
        sp *= 3.6;
        speed.setText(String.format("%.1f", sp ));
        if(sp > 50)
        {
            speed.setTextColor(Color.RED);
            speed.setTypeface(null, Typeface.BOLD);
        }
        else
        {
            speed.setTextColor(Color.BLACK);
            speed.setTypeface(null, Typeface.NORMAL);
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
        mMapView.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDetach();
    }
    //endregion

    class GetKml extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseKMLUrl("http://mapsengine.google.com/map/kml?forcekml=1&mid=z6IJfj90QEd4.kUUY9FoHFRdE");
            FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mMapView, null, null, kmlDocument);
            mMapView.getOverlays().remove(Constants.KML_OVERLAY_NUMBER);
            mMapView.getOverlays().add(Constants.KML_OVERLAY_NUMBER, kmlOverlay);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            mMapView.invalidate();
            super.onPostExecute(aVoid);
        }
    }
}
