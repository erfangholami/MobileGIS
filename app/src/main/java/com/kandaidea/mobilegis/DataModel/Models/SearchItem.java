package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class SearchItem
{
    @SerializedName("Coordinates")
    private String coordinates;
    @SerializedName("GeometryType")
    private String type;
    @SerializedName("ID")
    private int id;
    @SerializedName("Name")
    private String name;

    public SearchItem()
    {

    }
    public SearchItem(String coordinates, String type, int id, String name)
    {
        this.coordinates = coordinates;
        this.type = type;
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "SearchItem{" +
                "coordinates='" + coordinates + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
