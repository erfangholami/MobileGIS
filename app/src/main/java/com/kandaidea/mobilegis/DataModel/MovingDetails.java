package com.kandaidea.mobilegis.DataModel;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

import java.util.Calendar;

public class MovingDetails
{
    private static final String TAG = MovingDetails.class.getSimpleName();

    public GeoPoint firstPoint;
    public long firstTime = 0;
    public long time = 0;
    public double distance = 0;
    public boolean start = false;

    public double AvSpeed;
    public double speed;
    public double AvAcc;
    public double acc;

    public MovingDetails()
    {
        Log.d(TAG, "locationIsNotNull");
        start = false;
    }

    public double calculateAvAcc()
    {
        return distance / Math.pow(time / 1000, 2);
    }

    public double calculateAcc(GeoPoint firstPoint, Location x, long firstTime, long secondTime)
    {
        GeoPoint second = new GeoPoint(x.getLatitude(), x.getLongitude());
        if((secondTime - firstTime) / 1000 == 0)
            return 0;
        return (new CalculateOverlay().distanceTwoPoint(firstPoint, second)) / Math.pow((secondTime - firstTime) / 1000, 2);
    }

    public double calculateAvSpeed()
    {
        return distance / (time / 1000) ;
    }

    public double calculateSpeed(GeoPoint firstPoint, Location x, long firstTime, long secondTime)
    {
        GeoPoint second = new GeoPoint(x.getLatitude(), x.getLongitude());
        if((secondTime - firstTime) / 1000 == 0)
            return 0;
        return (new CalculateOverlay().distanceTwoPoint(firstPoint, second)) / ((secondTime - firstTime) / 1000);
    }
}
