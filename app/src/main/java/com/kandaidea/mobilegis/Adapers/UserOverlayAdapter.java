package com.kandaidea.mobilegis.Adapers;

import android.app.Notification;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
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
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class UserOverlayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = UserOverlayItem.class.getSimpleName();

    private MapView mMapView;
    private List<UserOverlayItem> items ;

    List<Polygon> polygons = new ArrayList<>();
    List<Polyline> polylines = new ArrayList<>();

    private ViewGroup par;
    public UserOverlayAdapter(MapView mMapView, List<UserOverlayItem> items)
    {
        this.mMapView = mMapView;
        this.items = items;
        if(items.size() != 0)
        {
            if(items.get(0).getType() == Constants.POLYGON_TYPE)
            {
                polygons.clear();
                for(UserOverlayItem item: items)
                {
                    polygons.add(item.getmPolygon());
                }
                mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                mMapView.invalidate();
            }
            else if(items.get(0).getType() == Constants.POLYLINE_TYPE)
            {
                polylines.clear();
                for(UserOverlayItem item: items)
                {
                    polylines.add(item.getmPolyline());
                }
                mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                mMapView.invalidate();
            }
            else if (items.get(0).getType() == Constants.MARKER_TYPE)
            {
                //TODO add markers to map
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        par = parent;
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
            mHolder.extend.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view)
                {
                    items.get(position).setShowMode(Constants.EXTEND_MODE);
                    notifyItemChanged(position);
                }
            });
            mHolder.turnOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    Log.v(TAG, "switch is : " + position + " " + b);
                    if(items.get(position).getType() == Constants.POLYGON_TYPE)
                    {
                        mMapView.getOverlays().removeAll(polygons);
                        mMapView.invalidate();
                        Polygon x = polygons.get(position);
                        x.setEnabled(b);
                        polygons.set(position, x);
                        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                        mMapView.invalidate();
                    }
                    else if(items.get(position).getType() == Constants.POLYLINE_TYPE)
                    {
                        mMapView.getOverlays().removeAll(polylines);
                        mMapView.invalidate();
                        Polyline x = polylines.get(position);
                        x.setEnabled(b);
                        polylines.set(position, x);
                        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                        mMapView.invalidate();
                    }
                    else if(items.get(position).getType() == Constants.MARKER_TYPE)
                    {
                        //TODO handle change on/off switch with map
                    }
                    mMapView.invalidate();
                }
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
            mHolder.extend.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    items.get(position).setShowMode(Constants.COMPACT_MODE);
                    notifyItemChanged(position);
                }
            });
            mHolder.turnOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    Log.v(TAG, "switch is : " + position + " " + b);
                    if(items.get(position).getType() == Constants.POLYGON_TYPE)
                    {
                        enableTools(mHolder, mHolder.turnOnOff.isChecked());
                        mMapView.getOverlays().removeAll(polygons);
                        mMapView.invalidate();
                        Polygon x = polygons.get(position);
                        x.setEnabled(b);
                        polygons.set(position, x);
                        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYGON_OVERLAY_NUMBER, polygons);
                        mMapView.invalidate();
                    }
                    else if(items.get(position).getType() == Constants.POLYLINE_TYPE)
                    {
                        enableTools(mHolder, mHolder.turnOnOff.isChecked());
                        mMapView.getOverlays().removeAll(polylines);
                        mMapView.invalidate();
                        Polyline x = polylines.get(position);
                        x.setEnabled(b);
                        polylines.set(position, x);
                        mMapView.getOverlays().addAll(Constants.DRAW_USER_POLYLINE_OVERLAY_NUMBER, polylines);
                        mMapView.invalidate();
                    }
                    else if(items.get(position).getType() == Constants.MARKER_TYPE)
                    {
                        //TODO handle change on/off switch with map
                    }
                    mMapView.invalidate();
                }
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
        public CompatViewHolder(View itemView)
        {
            super(itemView);
            layerName = itemView.findViewById(R.id.layer_name);
            turnOnOff = itemView.findViewById(R.id.layer_status);
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
