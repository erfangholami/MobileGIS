package com.kandaidea.mobilegis.DataModel;

import android.Manifest;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Overlay;

public class Constants
{
    public static final String BASE_URL = "http://192.168.0.25/AppServer/KService.svc/";
    public static final String[] permissions = {Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int[] permissionCodes = {100, 101, 102};

    public static final Long ANIMATE_SPEED = 500l;
    public static final Double ANIMATE_ZOOM_LEVEL = 19.0d;
    public static final double MIN_MAP_ZOOM_LEVEL = 3d;
    public static final Double MAX_MAP_ZOOM_LEVEL = 22d;
    public static final GeoPoint MIDDLE_IRAN = new GeoPoint(33.33, 50);
    public static final BoundingBox MAP_BOUND = new BoundingBox(85.05112877980659, 180.0d,-85.05112877980659,-180.0d);


    //map all overlay numbers
    public static final int[] TILE_OVERLAIES_NUMBER = new int[]{4, 3, 2, 1, 0};
    public static final XYTileSource x1 =  new XYTileSource("x1", 4, 12, 256, ".png?type=google",
            new String[]{"http://wms.chartbundle.com/tms/v1.0/enrl/"});
    public static final XYTileSource x2 = new XYTileSource("x2",
            3, 20, 256, ".png", new String[] {
            "http://tile.thunderforest.com/cycle/"});
    public static final XYTileSource x3 = new XYTileSource("x3",
            3, 20, 256, ".png", new String[] {
            "http://tile.thunderforest.com/outdoors/"});

    public static final XYTileSource x4 = new XYTileSource("x4",
            3, 20, 256, ".png", new String[] {
            "http://tile.thunderforest.com/landscape/"});
    public static final XYTileSource x5 = new XYTileSource("x5",
            3, 20, 256, ".png", new String[] {
            "http://c.tiles.wmflabs.org/hillshading/"});
    public static final XYTileSource[] TILE_OVERLAIES = new XYTileSource[]{x1, x2, x3, x4, x5};
    public static final int MY_LOCATION_OVERLAY_NUMBER = 5;
    public static final int SCALE_BAR_OVERLAY_NUMBER = 6;
    public static final int MAP_EVENT_RECEIVER_OVERLAY_NUMBER = 7;
    public static final int DRAW_POLYGON_OVERLAY_NUMBER = 8;
    public static final int DRAW_POLYGON_MARKER_OVERLAY_NUMBER = 9;
    public static final int DRAW_POLYLINE_OVERLAY_NUMBER = 10;
    public static final int DRAW_POLYLINE_MARKER_OVERLAY_NUMBER = 11;
    public static final int DRAW_CUSTOM_MARKER_OVERLAY_NUMBER = 12;
    public static final int DRAW_USER_POLYGON_OVERLAY_NUMBER = 13;
    public static final int DRAW_USER_POLYLINE_OVERLAY_NUMBER = 14;
    public static final int DRAW_USER_SEARCH_ITEM_OVERLAY_NUMBER = 15;


    //main activity start intent for result request code
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 1000;
    public static final int SETTING_ACTIVITY_REQUEST_CODE = 1001;


    //save data in storage folder names
    public static final String MAIN_FOLDER = "MobileGIS";
    public static final String SCREENSHOT_FOLDER = "Screenshots";
    public static final String USER_LOCATIONS_FOLDER = "UserLocations";


    //navigation item numbers
    public static final int EXIT_ITEM_NUMBER = 6;
    public static final int SETTING_ITEM_NUMBER = 5;
    public static final int USER_LOCATIONS_ITEM_NUMBER = 4;
    public static final int DRAW_ITEM_NUMBER = 2;


    //map modes
    public static final int NONE = 0;
    public static final int DRAW_POLYGON_MODE = 1;
    public static final int DRAW_POLYLINE_MODE = 2;
    public static final int SHOW_SEARCH_MODE = 3;
    public static final int SHOW_MAPSETTING_MODE = 4;
    public static final int SHOW_MARKER_MODE = 5;


    public static final float MAX_TRANSPARENCY = 1;

    //save user overlays
    public static final int POLYGON_TYPE = 0;
    public static final int POLYLINE_TYPE = 1;
    public static final int MARKER_TYPE = 2;

    //user overlay items in map setting show mode
    public static final int COMPACT_MODE = 0;
    public static final int EXTEND_MODE = 1;

    //SettingActivity returning value key
    public static final String MY_LOCATION_ENABLE_VALUE = "my_location_value";
    public static final String SCALE_BAR_ENABLE_VALUE = "scale_bar_value";
    public static final String FOLLOW_LOCATION_ENABLE_VALUE = "follow_location_value";
    public static final String SPEED_ENABLE_VALUE = "speed_value";

    //key for save overlay features in DB
    public static final String STROKE_COLOR_STRING = "stroke_color";
    public static final String STROKE_SIZE_STRING = "stroke_size";
    public static final String FILL_COLOR_STRING = "fill_color";

    //login response code
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAILED = 0;
    public static final int CONNECTION_ERROR = -1;


    //
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;


    public static final String SEARCH_COORDINATES_KEY = "coordinates";
    public static final String SEARCH_TYPE_KEY = "geometry_type";
    public static final String SEARCH_ID_KEY = "id";
    public static final String SEARCH_NAME_KEY = "name";

    public static final String RETROFIT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    public static final String TOKEN_KEY = "token";
}
