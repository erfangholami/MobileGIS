package com.kandaidea.mobilegis.DataModel;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

import java.util.Calendar;

public class MovingDetails extends AsyncTask<Void, Void, Void>
{
    private static final String TAG = MovingDetails.class.getSimpleName();

    private IMyLocationProvider mProvider;
    private Calendar calander;
    private GeoPoint firstPoint;
    private double firstTime = 0;
    private double time = 0;
    private double distance = 0;
    boolean stop = true;

    private double AvSpeed;
    private double speed;
    private double AvAcc;
    private double acc;

    public MovingDetails(IMyLocationProvider mProvider)
    {
        this.mProvider = mProvider;
        calander = Calendar.getInstance();
        Location x = this.mProvider.getLastKnownLocation();
        if(x == null)
        {
            while (x == null)
            {
                x = mProvider.getLastKnownLocation();
            }
        }
        Log.d(TAG, "locationIsNotNull");
        firstPoint.setCoords(x.getLatitude(), x.getLongitude());
        firstTime = calander.get(Calendar.SECOND);
        stop = false;
    }
    @Override
    protected Void doInBackground(Void... voids)
    {
        new Runnable()
        {
            @Override
            public void run()
            {
                while (!stop)
                {
                    Location x = mProvider.getLastKnownLocation();
                    while (x.getLatitude() == firstPoint.getLatitude() && x.getLongitude() == firstPoint.getLongitude())
                    {
                        x = mProvider.getLastKnownLocation();
                    }
                    calander = Calendar.getInstance();
                    double secondTime = calander.get(Calendar.SECOND);

                    speed = calculateSpeed(firstPoint, x, firstTime, secondTime);
                    acc = calculateAcc(firstPoint, x, firstTime, secondTime);

                    time += secondTime - firstTime;
                    distance += new CalculateOverlay().distanceTwoPoint(firstPoint, new GeoPoint(x.getLatitude(), x.getLongitude()));

                    AvSpeed = calculateAvSpeed();
                    AvAcc = calculateAvAcc();
                    Log.d(TAG, "movingDetails is : " + speed + " " + AvSpeed + " " + acc + " " + AvAcc);
                    firstTime = secondTime;
                    firstPoint = new GeoPoint(x.getLatitude(), x.getLongitude());

                }
            }
        };
        return null;
    }

    private double calculateAvAcc()
    {
        return distance / Math.pow(time, 2);
    }

    private double calculateAcc(GeoPoint firstPoint, Location x, double firstTime, double secondTime)
    {
        GeoPoint second = new GeoPoint(x.getLatitude(), x.getLongitude());
        return (new CalculateOverlay().distanceTwoPoint(firstPoint, second)) / Math.pow((secondTime - firstTime), 2);
    }

    private double calculateAvSpeed()
    {
        return distance / time;
    }

    private double calculateSpeed(GeoPoint firstPoint, Location x, double firstTime, double secondTime)
    {
        GeoPoint second = new GeoPoint(x.getLatitude(), x.getLongitude());
        return (new CalculateOverlay().distanceTwoPoint(firstPoint, second)) / (secondTime - firstTime);
    }
}
