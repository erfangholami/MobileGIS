package com.kandaidea.mobilegis.DataModel;

import android.graphics.PointF;
import android.icu.util.IslamicCalendar;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toRadians;

public class CalculateOverlay
{
    private static final double EARTH_RADIUS = 3958.75;
    public double distanceTwoPoint(GeoPoint p1, GeoPoint p2)
    {
        /*double lat1 = p1.getLatitude();
        double lat2 = p2.getLatitude();
        double lng1 = p1.getLongitude();
        double lng2 = p2.getLongitude();
        // in miles, change to 6371 for kilometer output
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = EARTH_RADIUS * c;// output distance, in MILES
        return dist * 1609.34; // output distance, in METER*/
        return p1.distanceToAsDouble(p2);
    }
    public double distancePointFromPolyline(GeoPoint p, Polyline polyline)
    {
        if(polyline.getPoints().size() == 0)
        {
            return -1;
        }
        else if(polyline.getPoints().size() < 2)
        {
            return distanceTwoPoint(p, polyline.getPoints().get(0));
        }
        else
        {
            GeoPoint[] nearestPoints = findTwoNearestPoint(p, polyline.getPoints());
            double dist1 = distanceTwoPoint(p, nearestPoints[0]);
            double dist2 = distanceTwoPoint(p, nearestPoints[1]);
            double dist3 = distancePointFromLine(p, nearestPoints[0], nearestPoints[1]);
            return Math.min(dist1, Math.min(dist2, dist3));
        }
    }
    public double distancePointFromPolygon(GeoPoint p, Polygon polygon)
    {
        if(polygon.getPoints().size() == 0)
        {
            return -1;
        }
        else if(polygon.getPoints().size() < 2)
        {
            return distanceTwoPoint(p, polygon.getPoints().get(0));
        }
        else
        {
            GeoPoint[] nearestPoints = findTwoNearestPoint(p, polygon.getPoints());
            double dist1 = distanceTwoPoint(p, nearestPoints[0]);
            double dist2 = distanceTwoPoint(p, nearestPoints[1]);
            double dist3 = distancePointFromLine(p, nearestPoints[0], nearestPoints[1]);
            return Math.min(dist1, Math.min(dist2, dist3));
        }
    }
    public GeoPoint[] findTwoNearestPoint(GeoPoint p, List<GeoPoint> points)
    {
        GeoPoint p1 = points.get(0);
        GeoPoint p2 = points.get(1);
        double dist1 = distanceTwoPoint(p, p1);
        double dist2 = distanceTwoPoint(p, p2);
        for(int i = 2; i < points.size(); i++)
        {
            double dist = distanceTwoPoint(p, points.get(i));
            if((dist > dist1 && dist < dist2) || (dist < dist1 && dist < dist2 && dist1 < dist2))
            {
                dist2 = dist;
                p2 = points.get(i);
            }
            else if((dist < dist1 && dist > dist2) || (dist < dist1 && dist < dist2 && dist1 > dist2))
            {
                dist1 = dist;
                p1 = points.get(i);
            }
        }
        return new GeoPoint[]{p1 ,p2};
    }
    public double distancePointFromLine(GeoPoint p1, GeoPoint start, GeoPoint end)
    {
        final double s0lat = toRadians(p1.getLatitude());
        final double s0lng = toRadians(p1.getLongitude());
        final double s1lat = toRadians(start.getLatitude());
        final double s1lng = toRadians(start.getLongitude());
        final double s2lat = toRadians(end.getLatitude());
        final double s2lng = toRadians(end.getLongitude());

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0)
        {
            return distanceTwoPoint(p1, start);
        }
        if (u >= 1)
        {
            return distanceTwoPoint(p1, end);
        }
        GeoPoint sa = new GeoPoint(p1.getLatitude() - start.getLatitude(), p1.getLongitude() - start.getLongitude());
        GeoPoint sb = new GeoPoint(u * (end.getLatitude() - start.getLatitude()), u * (end.getLongitude() - start.getLongitude()));
        return distanceTwoPoint(sb, sa);
    }
    public double polygonArea(Polygon polygon)
    {
        double sum = 0;
        List<PointF> vertices = new ArrayList<>();
        for(GeoPoint p: polygon.getPoints())
        {
            vertices.add(convertLatLongToUTM39N(p));
        }
        if(vertices.size() < 3)
            return 0;
        for (int i = 0; i < vertices.size() ; i++)
        {
            if (i == 0)
            {
                sum += vertices.get(i).x * (vertices.get(i + 1).y - vertices.get(vertices.size() - 1).y);
            }
            else if (i == vertices.size() - 1)
            {
                sum += vertices.get(i).x * (vertices.get(0).y - vertices.get(i - 1).y);
            }
            else
            {
                sum += vertices.get(i).x * (vertices.get(i + 1).y - vertices.get(i - 1).y);
            }
        }
        double area = 0.5 * Math.abs(sum);
        return area;

    }
    public double polygonLength(Polygon polygon)
    {
        if(polygon.getPoints().size() < 2)
            return 0;
        return distanceTwoPoint(polygon.getPoints().get(0), polygon.getPoints().get(polygon.getPoints().size() - 1)) + calculateLength(polygon.getPoints());
    }
    public double polylineLength(Polyline polyline)
    {
        if(polyline.getPoints().size() < 2)
            return 0;
        return calculateLength(polyline.getPoints());
    }
    public PointF convertLatLongToUTM39N(GeoPoint pt)
    {

        double Easting;
        double Northing;
        int Zone;
        char Letter;
        double Lat = pt.getLatitude();
        double Lon = pt.getLongitude();

        Zone = (int) Math.floor(Lon/6+31);
        if (Lat<-72)
            Letter='C';
        else if (Lat<-64)
            Letter='D';
        else if (Lat<-56)
            Letter='E';
        else if (Lat<-48)
            Letter='F';
        else if (Lat<-40)
            Letter='G';
        else if (Lat<-32)
            Letter='H';
        else if (Lat<-24)
            Letter='J';
        else if (Lat<-16)
            Letter='K';
        else if (Lat<-8)
            Letter='L';
        else if (Lat<0)
            Letter='M';
        else if (Lat<8)
            Letter='N';
        else if (Lat<16)
            Letter='P';
        else if (Lat<24)
            Letter='Q';
        else if (Lat<32)
            Letter='R';
        else if (Lat<40)
            Letter='S';
        else if (Lat<48)
            Letter='T';
        else if (Lat<56)
            Letter='U';
        else if (Lat<64)
            Letter='V';
        else if (Lat<72)
            Letter='W';
        else
            Letter='X';
        Easting=0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(Lat*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2)/3)+500000;
        Easting=Math.round(Easting*100)*0.01;
        Northing = (Math.atan(Math.tan(Lat*Math.PI/180)/Math.cos((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))-Lat*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(Lat*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))/(1-Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))+0.9996*6399593.625*(Lat*Math.PI/180-0.005054622556*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+4.258201531e-05*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))/3);
        if (Letter<'M')
            Northing = Northing + 10000000;
        Northing = Math.round(Northing*100)*0.01;


        PointF x = new PointF();
        x.set((float) Easting, (float) Northing);

        return x;
    }
    public double calculateLength(List<GeoPoint> points)
    {
        double sum = 0;
        for(int i = 1; i < points.size(); i++)
        {
            sum += distanceTwoPoint(points.get(i), points.get(i - 1));
        }
        return sum;
    }
}
