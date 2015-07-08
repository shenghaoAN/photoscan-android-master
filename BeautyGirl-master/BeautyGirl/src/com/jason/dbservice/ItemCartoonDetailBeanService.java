package com.jason.dbservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.bean.ItemCartoonDetailBean;
import com.jason.database.DatabaseHelper;
import com.jason.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * item详细
 * <p/>
 * Created by shenghao on 2015/6/25.
 */
public class ItemCartoonDetailBeanService extends BasisService {

    private static ItemCartoonDetailBeanService service;

    public ItemCartoonDetailBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = ItemCartoonDetailBean.class;
    }

    public ItemCartoonDetailBeanService(Context context, DatabaseHelper sqlHelper, SQLiteDatabase database) {
        super(context, sqlHelper, database, ItemCartoonDetailBean.class);
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized ItemCartoonDetailBeanService instance(Context context) {
        if (service == null) {
            service = new ItemCartoonDetailBeanService(context);
        }
        return service;
    }

    /**
     * 根据大分类和小分类名称删除数据
     *
     * @param colum 大分类 如(明星)
     * @param tag   小分类 如(杨幂)
     */
    public void deleteByColumTag(String colum, String tag) {
        database.execSQL(" DELETE FROM " + DBUtils.getTableName(clazz) + " " +
                "WHERE colum = " + "'" + colum + "' AND " +
                "" + "TAG = " + " '" + tag + "'");
    }

    /**
     * 根据分类名称获取本地数据
     *
     * @param colum 大分类 如(明星)
     * @param tag   小分类 如(杨幂)
     * @return
     */
    public List<ItemCartoonDetailBean> findListByColumTag(String colum, String tag) {
        List<ItemCartoonDetailBean> list = new ArrayList<ItemCartoonDetailBean>();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " " +
                "WHERE colum = " + "'" + colum + "' AND " +
                "" + "TAG = " + " '" + tag + "'", null);
        while (cursor.moveToNext()) {
            try {
                ItemCartoonDetailBean basisEnter = new ItemCartoonDetailBean();
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
