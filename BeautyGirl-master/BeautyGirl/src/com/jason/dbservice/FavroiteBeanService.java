package com.jason.dbservice;

import android.content.Context;
import android.database.Cursor;

import com.jason.bean.FavroiteBean;
import com.jason.bean.SearchBean;
import com.jason.utils.CharacterParser;
import com.jason.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏记录
 * <p/>
 * Created by shenghao on 2015/6/23.
 */
public class FavroiteBeanService extends BasisService {

    private static FavroiteBeanService service;

    public FavroiteBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = FavroiteBean.class;
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static FavroiteBeanService instance(Context context) {
        if (service == null) {
            service = new FavroiteBeanService(context);
        }
        return service;
    }

    /**
     * 获取分组list
     *
     * @return list
     */
    public List<String> findGroupTagList() {
        List<String> list = new ArrayList<String>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT distinct tag FROM " + tableName + " order by tagpingyin asc", null);
        while (cursor.moveToNext()) {
            try {
                list.add(cursor.getString(cursor.getColumnIndex("tag")));
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
     * @param tag 分类名称
     * @return
     */
    public List<FavroiteBean> findListByTag(String tag) {
        List<FavroiteBean> list = new ArrayList<FavroiteBean>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " where tag = '" + tag + "' order by id asc", null);
        while (cursor.moveToNext()) {
            try {
                FavroiteBean basisEnter = new FavroiteBean();
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
