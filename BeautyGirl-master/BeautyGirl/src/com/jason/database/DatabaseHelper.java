package com.jason.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jason.bean.FavroiteBean;
import com.jason.bean.SearchBean;
import com.jason.utils.DBUtils;

/**
 * Created by shenghao on 2015/6/23.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;   //数据库版本
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBUtils.getDropSqlByClass(FavroiteBean.class));
        db.execSQL(DBUtils.getDropSqlByClass(SearchBean.class));
        onCreate(db);
    }

    /**
     * Close the database connections
     */
    @Override
    public void close() {
        super.close();
    }

}
