package dev.erfangh.mobilegis.View;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ColorFilter
{
    private ColorMatrixColorFilter colorFilter;
    public ColorFilter()
    {
        final ColorMatrix negate =new ColorMatrix();
        negate.set(new float[]{
                0.0f,0,0,0,0,        //red
                0,0.0f,0,0,0,//green
                0,0,.0f,0,0,//blue
                0,0,0,.0f,0 //alpha
        });
        adjustHue(negate, 255);
        colorFilter = new ColorMatrixColorFilter(negate);
    }
    public ColorFilter(float trans)
    {
        final ColorMatrix negate =new ColorMatrix();
        negate.set(new float[]{
                trans,0,0,0,0,        //red
                0,trans,0,0,0,//green
                0,0,trans,0,0,//blue
                0,0,0,trans,0 //alpha
        });
        adjustHue(negate, 255);
        colorFilter = new ColorMatrixColorFilter(negate);
    }



    protected static float cleanValue(float p_val, float p_limit)
    {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }

    public static void adjustHue(ColorMatrix cm, float value)
    {
        value = cleanValue(value, 180f) / 180f * (float) Math.PI;
        if (value == 0)
        {
            return;
        }
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);
        float lumR = 0.213f;
        float lumG = 0.715f;
        float lumB = 0.072f;
        float[] mat = new float[]
                {
                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
                        0f, 0f, 0f, 1f, 0f
                };
        cm.postConcat(new ColorMatrix(mat));
    }

    public ColorMatrixColorFilter getColorFilter()
    {
        return colorFilter;
    }
}
