package com.kandaidea.mobilegis.Adapers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.R;
import com.kandaidea.mobilegis.View.ColorFilter;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MapSettingTileAdapter extends RecyclerView.Adapter<MapSettingTileAdapter.ViewHolder>
{
    private static final String TAG = MapSettingTileAdapter.class.getSimpleName();
    private MapView mMapView;
    private Context mContext;

    private List<Overlay> lists = new ArrayList<>();
    private float[] seekBars = new float[5];

    public MapSettingTileAdapter(Context mContext,MapView mMapView)
    {
        this.mMapView = mMapView;
        this.mContext = mContext;
        for(int i = 0; i < 5; i++)
        {
            lists.add(mMapView.getOverlays().get(Constants.TILE_OVERLAIES_NUMBER[i]));
            seekBars[i] = 0;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_layer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        holder.aSwitch.setChecked(lists.get(position).isEnabled());
        if(lists.get(position).isEnabled())
        {
            holder.seekBar.setEnabled(true);
        }
        else
        {
            holder.seekBar.setEnabled(false);
        }
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                mMapView.getOverlays().get(position).setEnabled(b);
                holder.seekBar.setEnabled(b);
            }
        });
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                float x = ((float) seekBar.getProgress() / 100.0f);
                TilesOverlay tilesOverlay = ((TilesOverlay) lists.get(position));
                tilesOverlay.setColorFilter(new ColorFilter(x).getColorFilter());
                tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
                mMapView.getOverlays().set(Constants.TILE_OVERLAIES_NUMBER[position], tilesOverlay);
                mMapView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 5;
    }



    class ViewHolder extends RecyclerView.ViewHolder
    {
        public SeekBar seekBar;
        public Switch aSwitch;
        public ViewHolder(View itemView)
        {
            super(itemView);
            seekBar = itemView.findViewById(R.id.transparency_seek_bar);
            aSwitch = itemView.findViewById(R.id.on_off_switch);
        }
    }
}
