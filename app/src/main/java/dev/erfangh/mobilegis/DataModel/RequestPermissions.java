package dev.erfangh.mobilegis.DataModel;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RequestPermissions
{
    private Activity mActivity;
    public RequestPermissions(Activity mActiviy)
    {
        this.mActivity = mActiviy;
    }
    public boolean request()
    {
        for(int i = 0 ; i < Constants.permissions.length; i++)
        {
            if(ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Constants.permissions[i]) != PackageManager.PERMISSION_GRANTED)
            {
                //not granted
                ActivityCompat.requestPermissions(mActivity, new String[]{Constants.permissions[i]}, Constants.permissionCodes[i]);
            }
        }
        for(int i = 0 ; i < Constants.permissions.length; i++)
        {
            if(ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Constants.permissions[i]) != PackageManager.PERMISSION_GRANTED)
            {
                //not granted
                return false;
            }
        }
        return true;
    }
}
