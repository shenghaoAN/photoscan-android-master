package com.jason;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author hao
 */

public class Cfg {

    public static String mode = "sandbox"; // sandbox or product
    public static final Integer appVersion = 3;
    public static String VersionName = "1.2.0";

    public static Integer appVersionInstalled = 0;
    public static boolean first = true;

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Context context;

    public static String BaseUrl = "http://image.baidu.com/";    //百度图片url
    public static String GagUrl = "http://infinigag-us.aws.af.cm/";  //9Gag搞笑图片url

    public static void setContext(Context ctx) {
        context = ctx;
        prefs = ctx.getSharedPreferences("Cfg", 0);
        editor = prefs.edit();
        get();
    }

    public static void get() {
        first = prefs.getBoolean("first", true);
    }

    public static void change(String key, Long value) {
        editor.putLong(key, value);
    }

    public static void change(String key, int value) {
        editor.putInt(key, value);
        if (key.equals("appVersionInstalled"))
            appVersionInstalled = value;
    }

    public static void change(String key, String value) {
        editor.putString(key, value);
    }

    public static void change(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public static void save() {
        editor.commit();
    }

    public static void save(String key, String value) {
        change(key, value);
        save();
    }

    public static void save(String key, int value) {
        change(key, value);
        save();
    }

    public static void save(String key, boolean value) {
        change(key, value);
        save();
    }

    public static void setAppVersionInstalled() {
        change("appVersionInstalled", Cfg.appVersion);
        save();
    }
}
