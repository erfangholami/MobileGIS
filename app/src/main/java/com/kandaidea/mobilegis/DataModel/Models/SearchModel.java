package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class SearchModel
{
    @SerializedName("Name")
    private String name;
    @SerializedName("ID")
    private int id;
    @SerializedName("GeometryType")
    private String type;


    public SearchModel(String name, int id, String type)
    {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
