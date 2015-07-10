package com.jason.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jason.Debug;
import com.jason.bean.FavroiteBean;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.bean.ItemCategoryBean;
import com.jason.bean.SearchBean;
import com.jason.dbservice.ItemCategoryBeanService;
import com.jason.dbservice.ManagerService;
import com.jason.utils.DBUtils;

/**
 * Created by shenghao on 2015/6/23.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;   //数据库版本
    private static final String DATABASE_NAME = "photo.db";   //数据库名称
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBUtils.getCreateSqlByClass(FavroiteBean.class));
        db.execSQL(DBUtils.getCreateSqlByClass(SearchBean.class));
        db.execSQL(DBUtils.getCreateSqlByClass(ItemCategoryBean.class));
        db.execSQL(DBUtils.getCreateSqlByClass(ItemCartoonDetailBean.class));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(DBUtils.getDropSqlByClass(FavroiteBean.class));
//        db.execSQL(DBUtils.getDropSqlByClass(SearchBean.class));
//        db.execSQL(DBUtils.getDropSqlByClass(ItemCategoryBean.class));
//        db.execSQL(DBUtils.getCreateSqlByClass(ItemCartoonDetailBean.class));
//        onCreate(db);

        //数据库升级处理
        if (oldVersion == 1 && newVersion == 2) {

            Debug.Log("upgrade db", "create new table");
            db.execSQL(DBUtils.getCreateSqlByClass(FavroiteBean.class,"_tmp_FavroiteBean"));
            db.execSQL(DBUtils.getCreateSqlByClass(SearchBean.class,"_tmp_SearchBean"));
            db.execSQL(DBUtils.getCreateSqlByClass(ItemCategoryBean.class,"_tmp_ItemCategoryBean"));
            db.execSQL(DBUtils.getCreateSqlByClass(ItemCartoonDetailBean.class,"_tmp_ItemCartoonDetailBean"));

            Debug.Log("upgrade db", "move data from old tables");
            db.execSQL("Insert into _tmp_FavroiteBean Select * from FavroiteBean");
            db.execSQL("Insert into _tmp_SearchBean(id,column,text,date) Select id,column,text,date from SearchBean");
            db.execSQL("Insert into _tmp_ItemCategoryBean Select * from ItemCategoryBean");
            db.execSQL("Insert into _tmp_ItemCartoonDetailBean Select * from ItemCartoonDetailBean");

            Debug.Log("upgrade db", "delete old tables");
            db.execSQL("DROP TABLE IF EXISTS FavroiteBean");
            db.execSQL("DROP TABLE IF EXISTS SearchBean");
            db.execSQL("DROP TABLE IF EXISTS ItemCategoryBean");
            db.execSQL("DROP TABLE IF EXISTS ItemCartoonDetailBean");

            Debug.Log("upgrade db", "rename tables");
            db.execSQL("ALTER TABLE _tmp_FavroiteBean RENAME TO FavroiteBean");
            db.execSQL("ALTER TABLE _tmp_SearchBean RENAME TO SearchBean");
            db.execSQL("ALTER TABLE _tmp_ItemCategoryBean RENAME TO ItemCategoryBean");
            db.execSQL("ALTER TABLE _tmp_ItemCartoonDetailBean RENAME TO ItemCartoonDetailBean");

            Debug.Log("upgrade db", "completed");
        }
    }

    /**
     * Close the database connections
     */
    @Override
    public void close() {
        super.close();
    }

}
