package com.jason.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author shenghao
 *
 */
public class ToastShow {

    public static void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
