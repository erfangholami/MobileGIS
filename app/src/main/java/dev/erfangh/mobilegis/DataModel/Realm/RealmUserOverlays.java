package dev.erfangh.mobilegis.DataModel.Realm;

import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.DataModel.Models.UserLocationModel;
import dev.erfangh.mobilegis.DataModel.Models.UserOverlayItem;
import dev.erfangh.mobilegis.DataModel.Models.UserOverlayModel;
import dev.erfangh.mobilegis.DataModel.OverlayString;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

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
    public boolean addOverlay(UserOverlayModel model)
    {
        boolean returning;
        userOverlayRealm.beginTransaction();
        RealmResults<UserOverlayModel> result = userOverlayRealm.where(UserOverlayModel.class).equalTo("name", model.getName()).findAll();
        if(result.isEmpty())
        {
            returning = true;
            userOverlayRealm.insert(model);
        }
        else
        {
            returning = false;
        }
        userOverlayRealm.commitTransaction();
        return returning;
    }

    public List<UserOverlayItem> getUserOverlay(int type)
    {
        List<UserOverlayModel> realmList ;
        List<UserOverlayItem> returningList = new ArrayList<>();
        userOverlayRealm.beginTransaction();
        RealmResults<UserOverlayModel> results = userOverlayRealm.where(UserOverlayModel.class).equalTo("overlayType", type).findAll();
        userOverlayRealm.commitTransaction();
        //realmList = Arrays.asList((UserOverlayModel[]) results.toArray());
        for(UserOverlayModel model: results)
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
                    Polygon x = new OverlayString().stringToPolygon(model.getOverlay());
                    x.setOnClickListener(null);
                    newItem.setmPolygon(x);
                    newItem.setTransparency(Constants.MAX_TRANSPARENCY);
                    break;
                }
                case Constants.POLYLINE_TYPE:
                {
                    newItem.setmPolyline(new OverlayString().stringToPolyline(model.getOverlay()));
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
    public void deleteOverlay(String name)
    {
        userOverlayRealm.beginTransaction();
        RealmResults<UserOverlayModel> result = userOverlayRealm.where(UserOverlayModel.class).equalTo("name", name).findAll();
        result.deleteAllFromRealm();
        userOverlayRealm.commitTransaction();
    }
}
