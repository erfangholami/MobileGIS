package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class SearchItem
{
    //TODO set var and functions for searched item
    @SerializedName("GetFinalResultResult")
    private SearchitemItem item;

    public SearchItem(SearchitemItem item)
    {
        this.item = item;
    }

    public SearchitemItem getItem()
    {
        return item;
    }

    public void setItem(SearchitemItem item)
    {
        this.item = item;
    }
}
