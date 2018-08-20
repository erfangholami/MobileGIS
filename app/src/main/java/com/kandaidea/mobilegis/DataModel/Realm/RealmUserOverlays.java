package com.kandaidea.mobilegis.DataModel.Realm;

import com.kandaidea.mobilegis.DataModel.Models.UserOverlayModel;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        //should save object in realm
        /*
        userOverlayRealm.beginTransaction();
        userOverlayRealm.insert(model);
        userOverlayRealm.commitTransaction();
        */
    }
}
