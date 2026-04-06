package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import dev.erfangh.mobilegis.View.UserLocations;

import io.realm.RealmObject;

public class UserLocationModel extends RealmObject
{
    @SerializedName("Time")
    private String time;
    @SerializedName("Lat")
    private double lat;
    @SerializedName("Lng")
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

    @Override
    public String toString()
    {
        return "UserLocationModel{" +
                "time='" + time + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
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
