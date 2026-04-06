package dev.erfangh.mobilegis.DataModel;

import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayerManager
{
    private static final String TAG = LayerManager.class.getSimpleName();
    private Map<String, List<Integer>> items;
    private MapView mapView;

    public LayerManager()
    {
        items = new HashMap<>();
    }
    public LayerManager(Map<String, List<Integer>> items, MapView mapView)
    {
        this.items = items;
        this.mapView = mapView;
    }

    public boolean addItem(Overlay overlay, String name)
    {
        if(!items.containsKey(name))
        {
            boolean secssus = mapView.getOverlays().add(overlay);
            //mapView.invalidate();
            int value = mapView.getOverlays().size() - 1;
            List<Integer> valueList = new ArrayList<>();
            valueList.add(value);
            items.put(name, valueList);
            Log.d(TAG, "items: " + items.toString());
            return secssus;
        }
        else
        {
            return false;
        }
    }


    public boolean remmoveItem(String name)
    {
        Log.d(TAG, "onRemove" + name);
        if(items.containsKey(name))
        {
            List<Integer> value = items.get(name);
            for(Integer i: value)
            {
                Log.d(TAG, String.valueOf(i));
                Overlay overlay = new Overlay()
                {
                    @Override
                    public void draw(Canvas c, MapView osmv, boolean shadow)
                    {

                    }
                };
                overlay.setEnabled(false);
                try
                {
                    mapView.getOverlays().set(i, overlay);
                }
                catch(Exception e)
                {
                    Log.v(TAG, e.getMessage());
                }
            }
           // mapView.invalidate();
            items.remove(name);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Overlay getOverlay(String name)
    {
        if(items.containsKey(name))
        {
            return mapView.getOverlays().get(items.get(name).get(0));
        }
        return null;
    }
    public boolean addAll(List<? extends Overlay> overlays, String name)
    {
        if(!items.containsKey(name))
        {
            List<Integer> valueList = new ArrayList<>();
            for(Overlay overlay: overlays)
            {
                mapView.getOverlays().add(overlay);
                int value = mapView.getOverlays().indexOf(overlay);
                valueList.add(value);
            }
            //mapView.invalidate();
            items.put(name, valueList);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Map<String, List<Integer>> getItems()
    {
        return items;
    }

    public void setItems(Map<String, List<Integer>> items)
    {
        this.items = items;
    }
}
