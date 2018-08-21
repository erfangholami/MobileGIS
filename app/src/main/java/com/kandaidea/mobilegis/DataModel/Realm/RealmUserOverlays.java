package com.kandaidea.mobilegis.DataModel.Realm;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayItem;
import com.kandaidea.mobilegis.DataModel.Models.UserOverlayModel;
import com.kandaidea.mobilegis.DataModel.OverlayString;

import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmUserOverlays
{
    private Realm userOverlayRealm;
    public RealmUserOverlays()
    {
        RealmConfiguration userOverlayConfig = new RealmConfiguration.Builder()
                .name("user_overlays.realm")
                .schemaVersion(1)
                .build();
        userOverlayRealm = Realm.getInstance(userOverlayConfig);
    }
    public void addOverlay(UserOverlayModel model)
    {
        userOverlayRealm.beginTransaction();
        userOverlayRealm.insert(model);
        userOverlayRealm.commitTransaction();
    }

    public List<UserOverlayItem> getUserOverlay(int type)
    {
        List<UserOverlayModel> realmList ;
        List<UserOverlayItem> returningList = new ArrayList<>();
        userOverlayRealm.beginTransaction();
        RealmResults<UserOverlayModel> results = userOverlayRealm.where(UserOverlayModel.class).equalTo("overlayType", type).findAll();
        realmList = Arrays.asList((UserOverlayModel[]) results.toArray());
        for(UserOverlayModel model: realmList)
        {
            UserOverlayItem newItem = new UserOverlayItem();
            newItem.setName(model.getName());
            newItem.setType(model.getOverlayType());
            newItem.setDescription(model.getDescription());
            newItem.setSimplify(false);
            switch (model.getOverlayType())
            {
                case Constants.POLYGON_TYPE:
                {
                    newItem.setmPolygon(new OverlayString().toPolygon(model.getOverlay()));
                    newItem.setTransparency(Constants.MAX_TRANSPARENCY);
                    break;
                }
                case Constants.POLYLINE_TYPE:
                {
                    newItem.setmPolyline(new OverlayString().toPolyline(model.getOverlay()));
                    newItem.setTransparency(Constants.MAX_TRANSPARENCY);
                    break;
                }
                case Constants.MARKER_TYPE:
                {
                    newItem.setmMarker(new OverlayString().toMarker(model.getOverlay()));
                    break;
                }
            }
            returningList.add(newItem);
        }
        return returningList;
    }
}
