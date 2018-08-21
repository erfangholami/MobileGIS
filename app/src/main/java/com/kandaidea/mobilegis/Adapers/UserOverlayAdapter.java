package com.kandaidea.mobilegis.Adapers;

import android.app.Notification;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.R;

import java.util.ArrayList;
import java.util.List;

public class UserOverlayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = UserOverlayItem.class.getSimpleName();
    private List<UserOverlayItem> items ;
    public UserOverlayAdapter(List<UserOverlayItem> items)
    {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        if(viewType == Constants.COMPATE_MODE)
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
        if(getItemViewType(position) == Constants.COMPATE_MODE)
        {
            ((CompatViewHolder)holder).layerName.setText(items.get(position).getName());
            ((CompatViewHolder)holder).extend.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view)
                {
                    Log.d(TAG, "CompatExtendClicked" + items.get(position).getShowMode());
                    items.get(position).setShowMode(Constants.EXTEND_MODE);
                    notifyDataSetChanged();
                    //TODO change view after click more
                }
            });
        }
        if(getItemViewType(position) == Constants.EXTEND_MODE)
        {
            ((ExtendViewHolder)holder).layerName.setText(items.get(position).getName());
            ((ExtendViewHolder)holder).extend.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Log.d(TAG, "ExtendExtendClicked");
                    items.get(position).setShowMode(Constants.COMPATE_MODE);
                    notifyDataSetChanged();
                    //TODO change view after click less
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
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
