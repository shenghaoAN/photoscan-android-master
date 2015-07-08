package com.jason.dbservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.bean.SearchBean;
import com.jason.database.DatabaseHelper;
import com.jason.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索记录
 * <p/>
 * Created by shenghao on 2015/6/23.
 */
public class SearchBeanService extends BasisService {

    private static SearchBeanService service;

    public SearchBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = SearchBean.class;
    }

    public SearchBeanService(Context context, DatabaseHelper sqlHelper, SQLiteDatabase database) {
        super(context, sqlHelper, database, SearchBean.class);
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized SearchBeanService instance(Context context) {
        if (service == null) {
            service = new SearchBeanService(context);
        }
        return service;
    }

    /**
     * 查找搜索关键字
     *
     * @return
     */
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

    /**
     * 查找不重复的搜索记录
     *
     * @return
     */
    public List<SearchBean> findDistinctList() {
        List<SearchBean> list = new ArrayList<SearchBean>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " group by text limit 50", null);
        while (cursor.moveToNext()) {
            try {
                SearchBean basisEnter = new SearchBean();
                DBUtils.setObjectPropertyByCursor(basisEnter, cursor);
                list.add(basisEnter);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 获取分组list
     *
     * @return list
     */
    public List<String> findGroupColumList() {
        List<String> list = new ArrayList<String>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT distinct column FROM " + tableName, null);
        while (cursor.moveToNext()) {
            try {
                list.add(cursor.getString(cursor.getColumnIndex("column")));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 根据分类名称获取分组数据list
     *
     * @param colum 分类名称
     * @return
     */
    public List<SearchBean> findListByColum(String colum) {
        List<SearchBean> list = new ArrayList<SearchBean>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " where column = '" + colum + "' group by text order by date asc", null);
        while (cursor.moveToNext()) {
            try {
                SearchBean basisEnter = new SearchBean();
                DBUtils.setObjectPropertyByCursor(basisEnter, cursor);
                list.add(basisEnter);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        cursor.close();
        return list;
    }

}
