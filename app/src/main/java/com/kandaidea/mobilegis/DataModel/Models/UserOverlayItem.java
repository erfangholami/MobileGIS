package com.kandaidea.mobilegis.DataModel.Models;

import com.kandaidea.mobilegis.DataModel.Constants;

import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

public class UserOverlayItem
{
    //region Vars
    private Polygon mPolygon;
    private Polyline mPolyline;
    private List<Marker> mMarker;
    private String name;
    private String description;
    private boolean simplify;
    private float transparency;
    private int type;
    private int showMode = Constants.COMPATE_MODE;
    //endregion

    //region Constructor

    public UserOverlayItem()
    {
    }

    public UserOverlayItem(Polygon mPolygon, String name, String description, boolean simplify, float transparency, int type)
    {
        this.mPolygon = mPolygon;
        this.name = name;
        this.description = description;
        this.simplify = simplify;
        this.transparency = transparency;
        this.type = type;
    }

    public UserOverlayItem(Polyline mPolyline, String name, String description, boolean simplify, float transparency, int type)
    {
        this.mPolyline = mPolyline;
        this.name = name;
        this.description = description;
        this.simplify = simplify;
        this.transparency = transparency;
        this.type = type;
    }

    public UserOverlayItem(List<Marker> mMarker, String name, String description, boolean simplify, int type)
    {
        this.mMarker = mMarker;
        this.name = name;
        this.description = description;
        this.simplify = simplify;
        this.type = type;
    }
    //endregion

    //region GetterSetter
    public Polygon getmPolygon()
    {
        return mPolygon;
    }

    public void setmPolygon(Polygon mPolygon)
    {
        this.mPolygon = mPolygon;
    }

    public Polyline getmPolyline()
    {
        return mPolyline;
    }

    public void setmPolyline(Polyline mPolyline)
    {
        this.mPolyline = mPolyline;
    }

    public List<Marker> getmMarker()
    {
        return mMarker;
    }

    public void setmMarker(List<Marker> mMarker)
    {
        this.mMarker = mMarker;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isSimplify()
    {
        return simplify;
    }

    public void setSimplify(boolean simplify)
    {
        this.simplify = simplify;
    }

    public float getTransparency()
    {
        return transparency;
    }

    public void setTransparency(float transparency)
    {
        this.transparency = transparency;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getShowMode()
    {
        return showMode;
    }

    public void setShowMode(int showMode)
    {
        this.showMode = showMode;
    }
    //endregion
}
