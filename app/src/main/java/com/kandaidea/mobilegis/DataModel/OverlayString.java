package com.kandaidea.mobilegis.DataModel;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

public class OverlayString
{
    public Gson gson;
    public Type type;

     Polygon polygon;
     Polyline polyline;
     boolean isPolgon;
     static final String TAG = OverlayString.class.getSimpleName();

    public OverlayString()
    {
        //TODO should create constructors and methods to convert to String and revers
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        gson = builder.create();
        type = new TypeToken<Polygon>(){}.getType();
    }
    public String toString(Polygon overlay)
    {
        try
        {
            return gson.toJson(overlay, Polygon.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public Polygon toPolygon(String string)
    {
        return gson.fromJson(string, type);
    }

    public Polyline toPolyline(String string)
    {
        return gson.fromJson(string, type);
    }
    public List<Marker> toMarker(String string)
    {
        return gson.fromJson(string, type);
    }
}
