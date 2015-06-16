package com.jason.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 
 * @author shenghao
 *
 */
public class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕 DisplayMetrics
     */
    public static DisplayMetrics getdisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度像素
     *
     * @return width
     */
    public static int getWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int Width = dm.widthPixels;
        return Width;
    }

    /**
     * 获取屏幕高度像素
     *
     * @return height
     */
    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftWindow(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}