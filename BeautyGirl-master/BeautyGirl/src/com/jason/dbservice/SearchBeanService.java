package com.jason.dbservice;

import android.content.Context;
import android.database.Cursor;

import com.jason.bean.SearchBean;

/**
 * Created by shenghao on 2015/6/23.
 */
public class SearchBeanService extends BasisService {

    private static SearchBeanService service;

    public SearchBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = SearchBean.class;
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static SearchBeanService instance(Context context) {
        if (service == null) {
            service = new SearchBeanService(context);
        }
        return service;
    }

    public String[] findTexts() {
        Cursor cursor = database.rawQuery("SELECT distinct text FROM SearchBean order by id desc", null);
        if (cursor.getCount() > 0) {
            String[] strs = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                strs[i] = cursor.getString(cursor.getColumnIndex("text"));
                i++;
            }
            cursor.close();
            return strs;
        } else {
            return null;
        }
    }

}
