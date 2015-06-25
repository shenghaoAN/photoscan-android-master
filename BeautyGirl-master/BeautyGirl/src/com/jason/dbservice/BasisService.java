package com.jason.dbservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.database.DatabaseHelper;
import com.jason.utils.DBUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BasisService<T> {

    protected SQLiteDatabase database;
    protected DatabaseHelper databaseHelper;
    protected Class<T> clazz;
    protected Context context;

    public BasisService(Context context, DatabaseHelper databaseHelper,
                        SQLiteDatabase database, Class clazz) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.database = database;
        this.clazz = clazz;
    }

    public BasisService(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.database = this.databaseHelper.getWritableDatabase();
    }

    // Ìí¼Ó
    public void save(T enter) {
        ContentValues values = new ContentValues();
        DBUtils.setContentValuesByObject(enter, values);
        values.remove("ID");
        database.insertOrThrow(DBUtils.getTableName(enter.getClass()), null,
                values);
        Cursor cursor = database.rawQuery("SELECT LAST_INSERT_ROWID() ", null);
        if (cursor.moveToFirst()) {
            this.setId(enter, cursor.getLong(0));
        }

    }

    // ¸üÐÂ
    public void update(T enter) {
        ContentValues values = new ContentValues();
        DBUtils.setContentValuesByObject(enter, values);
        values.remove("ID");
        database.update(DBUtils.getTableName(enter.getClass()), values,
                " ID = " + this.getId(enter), null);

    }

    // É¾³ý
    public void delete(long id) {
        database.execSQL(" DELETE FROM " + DBUtils.getTableName(clazz)
                + " WHERE ID = " + id);
    }

    // É¾³ý
    public void deleteAllList() {
        database.execSQL(" DELETE FROM " + DBUtils.getTableName(clazz));
    }

    public T findById(String id) {
        return findById(id, this.clazz);
    }

    public T findById(int id) {
        return findById(String.valueOf(id), this.clazz);
    }

    public T findById(String id, Class clazz) {
        T basisEnter = null;
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName
                + " WHERE id = '" + id + "'", null);
        if (cursor.moveToNext()) {
            try {
                basisEnter = (T) clazz.newInstance();
                DBUtils.setObjectPropertyByCursor(basisEnter, cursor);
            } catch (InstantiationException e) {
                e.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        cursor.close();
        return basisEnter;
    }

    public List<T> findAllList() {
        List list = new ArrayList();
        String tableName = DBUtils.getTableName(clazz);
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            try {
                T basisEnter = clazz.newInstance();
                DBUtils.setObjectPropertyByCursor(basisEnter, cursor);
                list.add(basisEnter);
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        cursor.close();
        return list;
    }

    private Long getId(T t) {
        try {
            Field field = t.getClass().getField("id");
            return (Long) field.get(t);
        } catch (NoSuchFieldException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        return null;
    }

    private void setId(T t, Object id) {
        try {
            Field field = t.getClass().getField("id");
            field.set(t, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (NoSuchFieldException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
    }

}
