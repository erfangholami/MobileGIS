package com.kandaidea.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.views.overlay.Overlay;

import io.realm.RealmObject;

public class UserOverlayModel
{
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private int overlayType;
    @SerializedName("data")
    private Overlay overlay;

    // region constructors
    public UserOverlayModel()
    {

    }
    public UserOverlayModel(String name, int overlayType, Overlay overlay)
    {
        this.name = name;
        this.overlayType = overlayType;
        this.overlay = overlay;
    }
    //endregion

    //region getter/setter
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOverlayType()
    {
        return overlayType;
    }

    public void setOverlayType(int overlayType)
    {
        this.overlayType = overlayType;
    }

    public Overlay getOverlay()
    {
        return overlay;
    }

    public void setOverlay(Overlay overlay)
    {
        this.overlay = overlay;
    }
    //endregion
}
