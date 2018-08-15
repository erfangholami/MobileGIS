package com.kandaidea.mobilegis.Adapers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandaidea.mobilegis.DataModel.Models.UserLocationModel;
import com.kandaidea.mobilegis.R;

import java.util.ArrayList;
import java.util.List;

public class UserLocationAdapter extends RecyclerView.Adapter<UserLocationAdapter.ViewHolder>
{
    private List<UserLocationModel> locations = new ArrayList<>();

    public UserLocationAdapter(List<UserLocationModel> locations)
    {
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_location_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(locations.size() != 0)
        {
            holder.time.setText(locations.get(position).getTime());
            holder.location.setText(locations.get(position).getLat() + "," + locations.get(position).getLng());
        }
    }

    @Override
    public int getItemCount()
    {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView time, location;
        public ViewHolder(View itemView)
        {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
        }
    }
}
