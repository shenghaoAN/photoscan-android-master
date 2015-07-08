package com.jason.dbservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.bean.ItemCartoonDetailBean;
import com.jason.bean.ItemCategoryBean;
import com.jason.database.DatabaseHelper;
import com.jason.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页item分类
 * <p/>
 * Created by shenghao on 2015/6/25.
 */
public class ItemCategoryBeanService extends BasisService {

    private static ItemCategoryBeanService service;

    public ItemCategoryBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = ItemCategoryBean.class;
    }

    public ItemCategoryBeanService(Context context, DatabaseHelper sqlHelper, SQLiteDatabase database) {
        super(context, sqlHelper, database, ItemCategoryBean.class);
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized ItemCategoryBeanService instance(Context context) {
        if (service == null) {
            service = new ItemCategoryBeanService(context);
        }
        return service;
    }

    /**
     * 根据title删除数据
     *
     * @param title 大分类名称
     */
    public void deleteByTitle(String title) {
        database.execSQL(" DELETE FROM " + DBUtils.getTableName(clazz) + " " +
                "WHERE title = " + "'" + title + "'");
    }

    /**
     * 根据title获取数据
     *
     * @param title 大分类名称
     * @return
     */
    public List<ItemCategoryBean> findListByTitle(String title) {
        List<ItemCategoryBean> list = new ArrayList<ItemCategoryBean>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " " +
                "WHERE title = " + "'" + title + "'", null);
        while (cursor.moveToNext()) {
            try {
                ItemCategoryBean basisEnter = new ItemCategoryBean();
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
