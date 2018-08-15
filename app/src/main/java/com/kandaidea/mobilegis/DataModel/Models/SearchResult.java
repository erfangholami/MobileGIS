package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class SearchResult
{
    @SerializedName("name")
    private String name;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;

    public SearchResult(String name, String city, String province)
    {
        this.name = name;
        this.city = city;
        this.province = province;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }
}
