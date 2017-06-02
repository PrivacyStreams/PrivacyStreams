package com.github.privacystreams.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * A set of ui-related utility functions.
 */

public class UIUtils {

    private static Point sScreenSize;

    public static int getScreenHeight(Context context) {
        fetchScreenSize(context);
        return sScreenSize.y;
    }

    public static int getScreenWidth(Context context) {
        fetchScreenSize(context);
        Log.e("screen size", "" + sScreenSize.x);
        return sScreenSize.x;
    }

    private static void fetchScreenSize(Context context) {
        if (sScreenSize == null) {
            int i;
            int i2;
            Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            context.getResources().getConfiguration();
            defaultDisplay.getRotation();
            sScreenSize = new Point();
            if (Build.VERSION.SDK_INT >= 17) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics);
                DisplayMetrics displayMetrics2 = new DisplayMetrics();
                defaultDisplay.getRealMetrics(displayMetrics2);
                i = displayMetrics2.heightPixels - displayMetrics.heightPixels;
                sScreenSize.x = displayMetrics.widthPixels;
                sScreenSize.y = displayMetrics.heightPixels;
            } else {
                defaultDisplay.getSize(sScreenSize);
                i = 0;
            }
            if (sScreenSize.x <= sScreenSize.y) {
                i2 = 1;
            }
            i2 = sScreenSize.x;
            sScreenSize.x = sScreenSize.y + i;
            sScreenSize.y = i2 - i;
        }
    }
}
