package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult
{
    @SerializedName("GetPrimaryResultResult")
    private List<SearchModel> resultList;

    public SearchResult(List<SearchModel> resultList)
    {
        this.resultList = resultList;
    }

    public List<SearchModel> getResultList()
    {
        return resultList;
    }

    public void setResultList(List<SearchModel> resultList)
    {
        this.resultList = resultList;
    }


}
