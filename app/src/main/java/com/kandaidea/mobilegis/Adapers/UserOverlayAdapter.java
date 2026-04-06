package com.kandaidea.mobilegis.Adapers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.LayerManager;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.viewmodel.MapsActivityViewModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.ArrayList;
import java.util.List;

public class UserOverlayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = UserOverlayItem.class.getSimpleName();

    private LayerManager manager;
    private MapsActivityViewModel viewModel;
    private MapView mMapView;
    private List<UserOverlayItem> items ;

    List<Polygon> polygons = new ArrayList<>();
    List<Polyline> polylines = new ArrayList<>();

    public UserOverlayAdapter(MapsActivityViewModel model, MapView mMapView, List<UserOverlayItem> items)
    {
        this.manager = model.mLayerManager;
        viewModel = model;
        this.mMapView = mMapView;
        this.items = items;
        addData();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        Log.v(TAG, "viewType is : " + viewType);
        if(viewType == Constants.COMPACT_MODE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layer_item, parent, false);
            return new CompatViewHolder(view);
        }
        else if(viewType == Constants.EXTEND_MODE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polygon_layer_details, parent, false);
            return new ExtendViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
    {
        //TODO set component attribute
        if(holder.getItemViewType() == Constants.COMPACT_MODE)
        {
            CompatViewHolder mHolder = ((CompatViewHolder)holder);
            if(items.get(0).getType() == Constants.POLYGON_TYPE)
            {
                mHolder.turnOnOff.setChecked(polygons.get(position).isEnabled());
            }
            else if(items.get(0).getType() == Constants.POLYLINE_TYPE)
            {
                mHolder.turnOnOff.setChecked(polylines.get(position).isEnabled());
            }
            else if(items.get(0).getType() == Constants.MARKER_TYPE)
            {

            }
            mHolder.layerName.setText(items.get(position).getName());
            mHolder.extend.setOnClickListener((View view) ->
            {
                items.get(position).setShowMode(Constants.EXTEND_MODE);
                notifyItemChanged(position, items.get(position));
            });
            mHolder.remove.setOnClickListener((View view) ->
            {
                Log.v(TAG, "remove overlay" + position);
                if(items.get(position).getType() == Constants.POLYGON_TYPE)
                {
                    manager.remmoveItem(Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                    Log.d(TAG, "overlayName is : " + items.get(position).getName());
                    viewModel.deleteUserOverlay(items.get(position).getName());
                    items.remove(position);
                    polygons.remove(position);
                    manager.addAll(polygons, Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.POLYLINE_TYPE)
                {
                    manager.remmoveItem(Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                    viewModel.deleteUserOverlay(items.get(position).getName());
                    items.remove(position);
                    polylines.remove(position);
                    manager.addAll(polylines, Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.MARKER_TYPE)
                {
                    //TODO handle change on/off switch with map
                }
                notifyDataSetChanged();
                mMapView.invalidate();
            });
            mHolder.turnOnOff.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) ->
            {
                Log.v(TAG, "switch is : " + position + " " + b);
                if(items.get(position).getType() == Constants.POLYGON_TYPE)
                {
                    //mMapView.getOverlays().removeAll(polygons);
                    manager.remmoveItem(Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                    Polygon x = polygons.get(position);
                    x.setEnabled(b);
                    polygons.set(position, x);


                    Canvas canvas = new Canvas();
                    final Bitmap patternBMP ;
                    GeoPoint point = x.getPoints().get(0);
                    Paint fillPaint = new Paint();
                    fillPaint.setColor(Color.RED);
                    canvas.drawText("drawCanvas", 200, 700, fillPaint);
                    Paint stkPaint = new Paint();
                    stkPaint.setStyle(Paint.Style.STROKE);
                    stkPaint.setStrokeWidth(8);
                    stkPaint.setColor(Color.BLACK);
                    canvas.drawText("drawCanvas", 200, 700, stkPaint);




                    polygons.get(position).draw(canvas, mMapView ,false);

                    //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                    manager.addAll(polygons, Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.POLYLINE_TYPE)
                {
                    //mMapView.getOverlays().removeAll(polylines);
                    manager.remmoveItem(Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                    Polyline x = polylines.get(position);
                    x.setEnabled(b);
                    polylines.set(position, x);
                    //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                    manager.addAll(polylines, Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.MARKER_TYPE)
                {
                    //TODO handle change on/off switch with map
                }
                mMapView.invalidate();
            });
        }
        else if(holder.getItemViewType() == Constants.EXTEND_MODE)
        {
            final ExtendViewHolder mHolder = (ExtendViewHolder)holder;
            if(items.get(0).getType() == Constants.POLYGON_TYPE)
            {
                mHolder.turnOnOff.setChecked(polygons.get(position).isEnabled());
                enableTools(mHolder, mHolder.turnOnOff.isChecked());
            }
            else if(items.get(0).getType() == Constants.POLYLINE_TYPE)
            {

            }
            else if(items.get(0).getType() == Constants.MARKER_TYPE)
            {

            }
            mHolder.layerName.setText(items.get(position).getName());
            mHolder.extend.setOnClickListener((View view) ->
            {
                items.get(position).setShowMode(Constants.COMPACT_MODE);
                notifyItemChanged(position);
            });
            mHolder.turnOnOff.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) ->
            {
                Log.v(TAG, "switch is : " + position + " " + b);
                if(items.get(position).getType() == Constants.POLYGON_TYPE)
                {
                    enableTools(mHolder, mHolder.turnOnOff.isChecked());
                    //mMapView.getOverlays().removeAll(polygons);
                    manager.remmoveItem(Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                    Polygon x = polygons.get(position);
                    x.setEnabled(b);
                    polygons.set(position, x);
                    //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                    manager.addAll(polygons, Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.POLYLINE_TYPE)
                {
                    enableTools(mHolder, mHolder.turnOnOff.isChecked());
                    //mMapView.getOverlays().removeAll(polylines);
                    manager.remmoveItem(Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                    Polyline x = polylines.get(position);
                    x.setEnabled(b);
                    polylines.set(position, x);
                    //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                    manager.addAll(polylines, Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                    mMapView.invalidate();
                }
                else if(items.get(position).getType() == Constants.MARKER_TYPE)
                {
                    //TODO handle change on/off switch with map
                }
                mMapView.invalidate();
            });
        }

        //set change listeners

    }

    private void enableTools(ExtendViewHolder holder, boolean b)
    {
        holder.fillColor.setEnabled(b);
        holder.transparency.setEnabled(b);
        holder.simplify.setEnabled(b);
        holder.stroke.setEnabled(b);
        holder.strokeColor.setEnabled(b);
        holder.editPoints.setEnabled(b);
        holder.fillColor.setEnabled(b);
    }
    public void updateDataSet(List<UserOverlayItem> items)
    {
        this.items = items;
        addData();
        notifyDataSetChanged();
    }
    void addData()
    {
        if(items.size() != 0)
        {
            if(items.get(0).getType() == Constants.POLYGON_TYPE)
            {
                //mMapView.getOverlays().removeAll(polygons);
                manager.remmoveItem(Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                polygons.clear();
                for(UserOverlayItem item: items)
                {
                    polygons.add(item.getmPolygon());
                }
                //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                manager.addAll(polygons, Constants.DRAW_USER_POLYGON_OVERLAY_STRING);
                mMapView.invalidate();
            }
            else if(items.get(0).getType() == Constants.POLYLINE_TYPE)
            {
                mMapView.getOverlays().removeAll(polylines);
                polylines.clear();
                for(UserOverlayItem item: items)
                {
                    polylines.add(item.getmPolyline());
                }
                //mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                manager.addAll(polylines, Constants.DRAW_USER_POLYLINE_OVERLAY_STRING);
                mMapView.invalidate();
            }
            else if (items.get(0).getType() == Constants.MARKER_TYPE)
            {
                //TODO add markers to map
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        super.getItemViewType(position);
        return items.get(position).getShowMode();
    }

    class CompatViewHolder extends RecyclerView.ViewHolder
    {
        TextView layerName;
        Switch turnOnOff;
        ImageButton extend;
        ImageButton remove;
        public CompatViewHolder(View itemView)
        {
            super(itemView);
            layerName = itemView.findViewById(R.id.layer_name);
            turnOnOff = itemView.findViewById(R.id.layer_status);
            remove = itemView.findViewById(R.id.remove);
            extend = itemView.findViewById(R.id.show_more);
        }
    }
    class ExtendViewHolder extends RecyclerView.ViewHolder
    {
        TextView layerName;
        TextView transparencyText;
        TextView strokeText;
        TextView description;
        Switch turnOnOff;
        Switch simplify;
        SeekBar transparency;
        SeekBar stroke;
        Button strokeColor;
        Button fillColor;
        Button editPoints;
        ImageButton extend;
        public ExtendViewHolder(View itemView)
        {
            super(itemView);
            layerName = itemView.findViewById(R.id.layer_name);
            transparencyText = itemView.findViewById(R.id.transparency_text);
            strokeText = itemView.findViewById(R.id.stroke_text);
            description = itemView.findViewById(R.id.description);
            turnOnOff = itemView.findViewById(R.id.layer_status);
            simplify = itemView.findViewById(R.id.simplify);
            transparency = itemView.findViewById(R.id.transparency);
            stroke = itemView.findViewById(R.id.stroke);
            strokeColor = itemView.findViewById(R.id.stroke_color);
            fillColor = itemView.findViewById(R.id.fill_color);
            editPoints = itemView.findViewById(R.id.edit_point);
            extend = itemView.findViewById(R.id.show_less);
        }
    }
}
