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
    @SerializedName("Shape")
    private Shape shape;

    public SearchItem()
    {

    }

    public SearchItem(String coordinates, String type, int id, String name, Shape shape)
    {
        this.coordinates = coordinates;
        this.type = type;
        this.id = id;
        this.name = name;
        this.shape = shape;
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

    public Shape getShape()
    {
        return shape;
    }

    public void setShape(Shape shape)
    {
        this.shape = shape;
    }

    @Override
    public String toString()
    {
        return "SearchItem{" +
                "coordinates='" + coordinates + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", shape=" + shape +
                '}';
    }
}
