package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.views.overlay.Overlay;

import io.realm.RealmObject;

public class UserOverlayModel extends RealmObject
{
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private int overlayType;
    @SerializedName("data")
    private String overlay;

    // region constructors
    public UserOverlayModel()
    {

    }
    public UserOverlayModel(String name, int overlayType, String overlay)
    {
        this.name = name;
        this.overlayType = overlayType;
        this.overlay = String.valueOf(overlay);
    }
    public UserOverlayModel(String name,String description, int overlayType, String overlay)
    {
        this.name = name;
        this.description = description;
        this.overlayType = overlayType;
        this.overlay = String.valueOf(overlay);
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getOverlayType()
    {
        return overlayType;
    }

    public void setOverlayType(int overlayType)
    {
        this.overlayType = overlayType;
    }

    public String getOverlay()
    {
        return overlay;
    }

    public void setOverlay(String overlay)
    {
        this.overlay = overlay;
    }

    //endregion
}
