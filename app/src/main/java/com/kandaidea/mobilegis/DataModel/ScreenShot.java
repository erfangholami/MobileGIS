package com.kandaidea.mobilegis.DataModel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot
{
    private static final String TAG = ScreenShot.class.getSimpleName();
    private Bitmap mBitmap;
    public ScreenShot(Bitmap mBitmap)
    {
        this.mBitmap = mBitmap;
    }
    public boolean takeScreenshot()
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try
        {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + Constants.MAIN_FOLDER + "/"+ Constants.SCREENSHOT_FOLDER + "/" + now.toString() + ".jpg";
            Log.d(TAG, mPath);
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "screenshotSavedSuccessfully");
            return true;
        }
        catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Log.d(TAG, "screenshotFailed");
            return false;
        }
    }
    public boolean savePhoto()
    {
        String now = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try
        {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + Constants.MAIN_FOLDER + "/"+ Constants.PHOTO_FOLDER + "/" + now + ".jpg";
            Log.d(TAG, mPath);
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "photoSavedSuccessfully");
            return true;
        }
        catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Log.d(TAG, "photoSavingFailed");
            return false;
        }
    }
}
