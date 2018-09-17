package com.kandaidea.mobilegis.DataModel;



import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverlayString
{
     static final String TAG = OverlayString.class.getSimpleName();

    public Gson gson;
    public Type type;

    Polygon polygon;
    Polyline polyline;
    boolean isPolgon;


    public OverlayString()
    {
        //TODO should create constructors and methods to convert to String and revers
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        gson = builder.create();
        type = new TypeToken<Polygon>(){}.getType();

    }
    public String polygonToString(Polygon polygon)
    {
        KmlFeature f = new KmlPlacemark(polygon, new KmlDocument());
        f.setExtendedData(Constants.STROKE_COLOR_STRING, String.valueOf(polygon.getStrokeColor()));
        f.setExtendedData(Constants.STROKE_SIZE_STRING, String.valueOf(polygon.getStrokeWidth()));
        f.setExtendedData(Constants.FILL_COLOR_STRING, String.valueOf(polygon.getFillColor()));
        String json = f.asGeoJSON(true).toString();
        return json;
    }
    public String polylineToString(Polyline polyline)
    {
        HashMap<String, String> mExtendedData =new HashMap<String,String>();
        mExtendedData.put(Constants.STROKE_COLOR_STRING, String.valueOf(polyline.getColor()));
        mExtendedData.put(Constants.STROKE_SIZE_STRING, String.valueOf(polyline.getWidth()));
        //add property to this hash map :+)
        //KmlPlacemark f = new KmlPlacemark((Polyline) polyline, new KmlDocument());
        JsonObject json = new JsonObject();
        KmlGeometry mGeometry = new KmlLineString();
        mGeometry.mCoordinates = polyline.getPoints();
        json.addProperty("type", "Feature");
        if (mGeometry != null)
            json.add("geometry", mGeometry.asGeoJSON());
        else
            json.add("geometry", JsonNull.INSTANCE);
        json.add("properties", geoJSONProperties(mExtendedData));
        return  json.toString();
    }

    public Polygon stringToPolygon(String jsonString)
    {
        Polygon polygon = new Polygon();
        JsonObject xv = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonObject x = xv.getAsJsonObject("geometry");
        KmlPolygon m = new KmlPolygon(x);
        JsonObject properiea = xv.getAsJsonObject("properties");
        String fillColor = properiea.get(Constants.FILL_COLOR_STRING).getAsString();
        String strokeColor = properiea.get(Constants.STROKE_COLOR_STRING).getAsString();
        String strokeSize = properiea.get(Constants.STROKE_SIZE_STRING).getAsString();
        polygon.setFillColor(Integer.valueOf(fillColor));
        polygon.setStrokeColor(Integer.valueOf(strokeColor));
        polygon.setStrokeWidth(Float.valueOf(strokeSize));
        polygon.setPoints(m.mCoordinates);
        return polygon;
    }
    public Polyline stringToPolyline(String jsonString)
    {
        Polyline polyline = new Polyline();
        JsonObject x = new JsonParser().parse(jsonString).getAsJsonObject().getAsJsonObject("geometry");
        KmlLineString m = new KmlLineString(x);
        JsonObject properiea = new JsonParser().parse(jsonString).getAsJsonObject().getAsJsonObject("properties");
        String strokeColor = properiea.get(Constants.STROKE_COLOR_STRING).getAsString();
        String strokeSize = properiea.get(Constants.STROKE_SIZE_STRING).getAsString();
        polyline.setColor(Integer.valueOf(strokeColor));
        polyline.setWidth(Float.valueOf(strokeSize));
        polyline.setPoints(m.mCoordinates);

        return polyline;
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

    protected JsonObject geoJSONProperties(HashMap<String, String> mExtendedData){
        try {
            JsonObject json = new JsonObject();
            if (mExtendedData != null){
                for (Map.Entry<String, String> entry : mExtendedData.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    json.addProperty(name, value);
                }
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
