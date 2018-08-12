package com.kandaidea.mobilegis.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

public class MapsActivityViewModel extends ViewModel
{
    private Activity mActivity;

    public void init(Activity mActivity)
    {
        this.mActivity = mActivity;
    }
}
