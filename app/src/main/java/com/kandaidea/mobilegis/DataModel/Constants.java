package com.kandaidea.mobilegis.DataModel;

import android.Manifest;

import org.osmdroid.api.IGeoPoint;
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

    public static final int MY_LOCATION_OVERLAY_NUMBER = 0;

}
