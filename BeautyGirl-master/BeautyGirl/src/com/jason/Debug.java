package com.jason;

import android.util.Log;

import java.util.Date;

/**
 * 
 * @author hao
 *
 */

public class Debug {
    public static Date lastTime;
    public static void Log(String str, String str2){
        if(!Cfg.mode.equals("sandbox")) return;
        if(lastTime == null){
            lastTime = new Date();
            Log.d("atd-debug-start", String.valueOf(lastTime.getTime()));
        }
        Date newTime = new Date();
        Log.d("atd-" + str, str2 + ", " + String.valueOf(newTime.getTime() - lastTime.getTime()));
        lastTime = newTime;
    }
}
