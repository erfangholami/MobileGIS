package com.kandaidea.mobilegis.DataModel.Models;

import android.graphics.Color;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Sector
{

    private static final String TAG = Sector.class.getSimpleName();
    public List<Polygon> makeSector(List<SectorModel> sectorModels)
    {
        List<Polygon> returnedList = new ArrayList<>();
        for(SectorModel model: sectorModels)
        {
            Polygon newPolygon = getSectorPolygon(model);
            returnedList.add(newPolygon);
            Log.d(TAG, "PolygonSize : " + newPolygon.getPoints().size() + newPolygon.toString() );

        }
        return returnedList;
    }
    private Polygon getSectorPolygon(SectorModel model)
    {
        GeoPoint base = new GeoPoint(model.getY(), model.getX());
        Polygon polygon = new Polygon();
        polygon.addPoint(base);
        int times = (int) (model.gethBW() * 5);
        for(int i = 0; i <= times;i++)
        {
            polygon.addPoint(getEdge(base, (int) model.getR()* 2 , (i * 0.2) + model.getAzimuth() - (model.gethBW() / 2)));
        }
        polygon.setFillColor(Color.RED);
        polygon.setStrokeWidth(5f);
        return polygon;
    }
    private GeoPoint getEdge(GeoPoint base, int rad, double azimuth )
    {
        double R = 6378.1 ;
        double brng = Math.toRadians(azimuth);
        double d = rad / 1000 ;

        double lat1 = Math.toRadians(base.getLatitude());
        double lon1 = Math.toRadians(base.getLongitude());
        double lat2 = Math.asin( (Math.sin(lat1)*Math.cos(d/R)) +
                (Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng)));

        double lon2 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1),
                Math.cos(d/R)-(Math.sin(lat1)*Math.sin(lat2)));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);
        GeoPoint point1 = new GeoPoint(lat2, lon2);
        return point1;
    }
}
