package com.jason.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.jason.Debug;


/**
 * 
 * @author hao
 *
 */
public class ResourceHelper {
    public static String getResourceString(String name, Context context) {
        if(name == null) return "";
        int nameResourceID = context.getResources().getIdentifier(name, "string", context.getApplicationInfo().packageName);
        if (nameResourceID == 0) {
            Debug.Log("resource string", "failed");
            return name;
        } else {
            return context.getString(nameResourceID);
        }
    }
    public static int getResourceDrawable(String name, Context context) {
        int nameResourceID = context.getResources().getIdentifier(name, "drawable", context.getApplicationInfo().packageName);
        if (nameResourceID == 0) {
            Debug.Log("resource string","failed");
            return 0;
        } else {
            return nameResourceID;
        }
    }
    public static Drawable getDrawable(String name, Context context){
        int r = getResourceDrawable(name, context);
        if(r == 0) return null;
        return context.getResources().getDrawable(r);
    }
}
