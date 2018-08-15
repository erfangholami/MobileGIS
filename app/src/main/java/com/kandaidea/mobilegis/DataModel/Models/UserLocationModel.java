package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;
import com.kandaidea.mobilegis.View.UserLocations;

import io.realm.RealmObject;

public class UserLocationModel extends RealmObject
{
    @SerializedName("time")
    private String time;
    @SerializedName("latitude")
    private double lat;
    @SerializedName("longitude")
    private double lng;

    public UserLocationModel()
    {

    }
    public UserLocationModel(String time, double lat, double lng)
    {
        this.time = time;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }
}
