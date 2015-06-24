package com.jason.helper;

import android.content.Context;
import android.content.res.Resources;

import com.jason.hao.R;

import org.json.JSONArray;

import java.io.InputStream;

/**
 * Created by shenghao on 2015/6/24.
 */
public class MenuHelper {

    public MenuHelper() {

    }

    /**
     * 取出本地缓存数据
     * @param context
     * @return
     */
    public static JSONArray getMenuLocal(Context context) {
        String result;
        JSONArray menu;
        try {
            Resources res = context.getResources();
            InputStream inputStream = res.openRawResource(R.raw.menu);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
            menu = new JSONArray(result);
        } catch (Exception e) {
            menu = new JSONArray();
        }
        return menu;
    }

}
