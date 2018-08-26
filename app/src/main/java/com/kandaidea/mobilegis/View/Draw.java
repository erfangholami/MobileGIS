package com.kandaidea.mobilegis.View;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Draw
{
    private static final String TAG = Draw.class.getSimpleName();
    private Context mContext;
    private MapView mMapView;
    private Polygon mPolygon;
    private Polyline mPolyline;
    private ArrayList<Marker> mPolygonMarker;
    private ArrayList<Marker> mPolylineMarker;
    private int mMode;
    private Polygon.OnClickListener polygonListener;
    private Polyline.OnClickListener polylineListener;

    public Draw(Context mContext,MapView mMapView, Polygon mPolygon, Polyline mPolyline, ArrayList<Marker> mPolygonMarker, ArrayList<Marker> mPolylineMarker, int mMode, Polygon.OnClickListener listener, Polyline.OnClickListener listener2)
    {
        this.mContext = mContext;
        this.mMapView = mMapView;
        this.mPolygon = mPolygon;
        this.mPolyline = mPolyline;
        this.mPolygonMarker = mPolygonMarker;
        this.mPolylineMarker = mPolylineMarker;
        this.mMode = mMode;
        this.polygonListener = listener;
        this.polylineListener = listener2;
    }

    public void drawForPolygon(GeoPoint p)
    {
        mMapView.getOverlays().remove(Constants.DRAW_POLYGON_OVERLAY_NUMBER);
        Marker newMarker = new Marker(mMapView);
        newMarker.setPosition(p);
        newMarker.setDraggable(true);
        newMarker.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_draw_edge));
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        newMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDrag(Marker marker)
            {
                Log.d(TAG, "onMarkerDrag");
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                Log.d(TAG, "onMarkerDragEnd");

                int index = mPolygonMarker.indexOf(marker);
                GeoPoint p = marker.getPosition();
                List<GeoPoint> x = mPolygon.getPoints();
                Log.d(TAG, "sizeOfPolygon is : " + x.size());
                x.set(index, p);
                Polygon poly = new Polygon();
                poly.setPoints(x);
                poly.setFillColor(mContext.getResources().getColor(R.color.polygon_fill_color));
                mPolygon = poly;
                mPolygon.setOnClickListener(polygonListener);
                mMapView.getOverlays().add(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mPolygon);
                mMapView.getOverlays().remove(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER);
                mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, mPolygonMarker);
                mMapView.invalidate();
            }

            @Override
            public void onMarkerDragStart(Marker marker)
            {
                Log.d(TAG, "onMarkerDragStart");
                mMapView.getOverlays().remove(mPolygon);
                mMapView.invalidate();
            }
        });
        mMapView.getOverlays().remove(mPolygonMarker);
        mPolygonMarker.add(newMarker);
        mPolygon.addPoint(p);
        mPolygon.setFillColor(mContext.getResources().getColor(R.color.polygon_fill_color));
        mMapView.getOverlays().add(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mPolygon);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, mPolygonMarker);
        mMapView.invalidate();
    }
    public void deleteForPolygon()
    {
        mPolygon.getPoints().clear();
        mMapView.getOverlays().set(Constants.DRAW_POLYGON_OVERLAY_NUMBER, mPolygon);
        mMapView.getOverlays().removeAll(mPolygonMarker);
        mPolygonMarker.clear();
        Marker x = new Marker(mMapView);
        x.setVisible(false);
        mPolygonMarker.add(x);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYGON_MARKER_OVERLAY_NUMBER, mPolygonMarker);
    }
    public void drawForPolyline(GeoPoint p)
    {
        mMapView.getOverlays().remove(Constants.DRAW_POLYLINE_OVERLAY_NUMBER);
        Marker newMarker = new Marker(mMapView);
        newMarker.setPosition(p);
        newMarker.setDraggable(true);
        newMarker.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_draw_edge));
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        newMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDrag(Marker marker)
            {
                Log.d(TAG, "onMarkerDrag");
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                Log.d(TAG, "onMarkerDragEnd");

                int index = mPolylineMarker.indexOf(marker);
                GeoPoint p = marker.getPosition();
                List<GeoPoint> x = mPolyline.getPoints();
                Log.d(TAG, "sizeOfPolygon is : " + x.size());
                x.set(index, p);
                Polyline poly = new Polyline();
                poly.setPoints(x);
                mPolyline = poly;
                mPolyline.setOnClickListener(polylineListener);
                mMapView.getOverlays().add(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mPolyline);
                mMapView.getOverlays().remove(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER);
                mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, mPolylineMarker);
                mMapView.invalidate();
            }

            @Override
            public void onMarkerDragStart(Marker marker)
            {
                Log.d(TAG, "onMarkerDragStart");
                mMapView.getOverlays().remove(Constants.DRAW_POLYLINE_OVERLAY_NUMBER);
                mMapView.invalidate();
            }
        });
        mMapView.getOverlays().remove(mPolylineMarker);
        mPolylineMarker.add(newMarker);
        mPolyline.addPoint(p);
        mMapView.getOverlays().add(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mPolyline);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, mPolylineMarker);
        mMapView.invalidate();
    }
    public void deleteForPolyline()
    {
        List<GeoPoint> xx =  mPolyline.getPoints();
        xx.clear();
        mPolyline.setPoints(xx);
        mMapView.getOverlays().set(Constants.DRAW_POLYLINE_OVERLAY_NUMBER, mPolyline);
        mMapView.invalidate();
        mMapView.getOverlays().removeAll(mPolylineMarker);
        mPolylineMarker.clear();
        Marker x = new Marker(mMapView);
        x.setVisible(false);
        mPolylineMarker.add(x);
        mMapView.getOverlays().addAll(Constants.DRAW_POLYLINE_MARKER_OVERLAY_NUMBER, mPolylineMarker);
    }

    // region getterSetter
    public MapView getmMapView()
    {
        return mMapView;
    }

    public void setmMapView(MapView mMapView)
    {
        this.mMapView = mMapView;
    }

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

    public ArrayList<Marker> getmPolygonMarker()
    {
        return mPolygonMarker;
    }

    public void setmPolygonMarker(ArrayList<Marker> mPolygonMarker)
    {
        this.mPolygonMarker = mPolygonMarker;
    }

    public ArrayList<Marker> getmPolylineMarker()
    {
        return mPolylineMarker;
    }

    public void setmPolylineMarker(ArrayList<Marker> mPolylineMarker)
    {
        this.mPolylineMarker = mPolylineMarker;
    }
    //endregion
}
