package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;
import com.kandaidea.mobilegis.DataModel.Constants;

public class Propertise
{
    @SerializedName(Constants.FILL_COLOR_STRING)
    private int fillColor;
    @SerializedName(Constants.STROKE_COLOR_STRING)
    private int strokeColor;
    @SerializedName(Constants.STROKE_SIZE_STRING)
    private float strokeSize;


    public Propertise(int fillColor, int strokeColor, float strokeSize)
    {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeSize = strokeSize;
    }

    public int getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(int fillColor)
    {
        this.fillColor = fillColor;
    }

    public int getStrokeColor()
    {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor)
    {
        this.strokeColor = strokeColor;
    }

    public float getStrokeSize()
    {
        return strokeSize;
    }

    public void setStrokeSize(float strokeSize)
    {
        this.strokeSize = strokeSize;
    }

    @Override
    public String toString()
    {
        return '{' +
                "\"fill_color\":" + fillColor +
                ",\"stroke_color\":" + strokeColor +
                ",\"stroke_size\":" + strokeSize +
                '}';
    }
}
