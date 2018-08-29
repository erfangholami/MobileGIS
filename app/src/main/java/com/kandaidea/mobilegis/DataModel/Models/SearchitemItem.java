package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

import java.sql.Struct;

public class SearchitemItem
{
    @SerializedName("Coordinates")
    private String coordinates;
    @SerializedName("GeometryType")
    private String type;
    @SerializedName("ID")
    private int id;
    @SerializedName("Name")
    private String name;


    public SearchitemItem(String coordinates, String type, int id, String name)
    {
        this.coordinates = coordinates;
        this.type = type;
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Name:"+name + " Type:"+type + "ID:"+ String.valueOf(id)+ " cords:"+coordinates + "\n";
     }

    public String getCoordinates()
    {
        return coordinates;
    }

    public void setCodinates(String coordinates)
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
