package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class Address
{
    @SerializedName("Country")
    private String country;
    @SerializedName("Province")
    private String province;
    @SerializedName("City")
    private String city;
    @SerializedName("Address")
    private String address;

    public Address(String country, String province, String city, String address)
    {
        this.country = country;
        this.province = province;
        this.city = city;
        this.address = address;
    }

    //region GetterSetter
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    //endregion
}
