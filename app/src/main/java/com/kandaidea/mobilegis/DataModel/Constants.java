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
    public static final String BASE_URL = "http://google.com";
    public static final String[] permissions = {Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int[] permissionCodes = {100, 101, 102};

    public static final Long ANIMATE_SPEED = 500l;
    public static final Double ANIMATE_ZOOM_LEVEL = 19.0d;
    public static final long MIN_TIME_LOCATION_MANAGER = 4000;
    public static final float MIN_DISTANCE_LOCATION_MANAGER = 100f;
    public static final double MIN_MAP_ZOOM_LEVEL = 3d;
    public static final Double MAX_MAP_ZOOM_LEVEL = 22d;
    public static final GeoPoint MIDDLE_IRAN = new GeoPoint(33.33, 50);
    public static final BoundingBox MAP_BOUND = new BoundingBox(85.05112877980659, 180.0d,-85.05112877980659,-180.0d);

    public static final int MY_LOCATION_OVERLAY_NUMBER = 5;
    public static final int MAP_EVENT_RECEIVER_OVERLAY_NUMBER = 6;
    public static final int DRAW_POLYGON_OVERLAY_NUMBER = 7;
    public static final int DRAW_POLYGON_MARKER_OVERLAY_NUMBER = 8;
    public static final int DRAW_POLYLINE_OVERLAY_NUMBER = 9;
    public static final int DRAW_POLYLINE_MARKER_OVERLAY_NUMBER = 10;
    public static final int DRAW_CUSTOM_MARKER_OVERLAY_NUMBER = 11;
    public static final int[] TILE_OVERLAIES_NUMBER = new int[]{0, 1, 2, 3, 4};
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

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 1000;


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

    //save user overlays
    public static int POLYGON_TYPE = 0;
    public static int POLYLINE_TYPE = 1;
}
