package com.splitfee.app.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by huteri on 5/24/17.
 */

public class ViewUtils {

    public static int dpToPx(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
