package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class Shape
{
    @SerializedName("geometry")
    private String geometry;
    @SerializedName("properties")
    private Propertise propertise;
    @SerializedName("type")
    private String type;

    public Shape(String geometry, Propertise propertise, String type)
    {
        this.geometry = geometry;
        this.propertise = propertise;
        this.type = type;
    }

    public String getGeometry()
    {
        return geometry;
    }

    public void setGeometry(String geometry)
    {
        this.geometry = geometry;
    }

    public Propertise getPropertise()
    {
        return propertise;
    }

    public void setPropertise(Propertise propertise)
    {
        this.propertise = propertise;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        JsonObject x = new JsonParser().parse(geometry).getAsJsonObject();
        String xx = x.toString();

        return '{' +
                "\"geometry\":" + xx  +
                ",\"properties\":" + propertise.toString() +
                ",\"type\":" + type  +
                '}';
    }
}
