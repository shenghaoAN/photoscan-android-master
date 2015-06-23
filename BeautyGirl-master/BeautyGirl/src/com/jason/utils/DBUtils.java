package com.jason.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.jason.Debug;
import com.jason.database.DBField;
import com.jason.database.DBTable;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hao
 */

public class DBUtils {

    public static void main(String[] arge) {

    }

    private static Map<Class, List<Field>> clazzField = new HashMap();

    public static List<Field> getClassFieldByDBField(Class clazz) {
        List<Field> fieldList = new ArrayList();
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                DBField dbField = field.getAnnotation(DBField.class);
                if (dbField != null) {
                    fieldList.add(field);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return fieldList;
    }

    public static StringBuffer getCreateFieldSqlByDBField(DBField dbField) {
        StringBuffer sql = new StringBuffer();
        sql.append(dbField.name() + " ");

        if (dbField.primaryKey() == true) {
            return sql.append("INTEGER PRIMARY KEY AUTOINCREMENT");
        }

        if (dbField.type() == DBField.Type.VARCHAR) {
            sql.append("VARCAHR(" + dbField.length() + ")");
        } else if (dbField.type() == DBField.Type.TEXT) {
            sql.append("TEXT");
        } else if (dbField.type() == DBField.Type.INTEGER) {
            sql.append("INTEGER");
        } else if (dbField.type() == DBField.Type.DATE) {
            sql.append("DATETIME");
        } else {
            return new StringBuffer();
        }
        return sql;
    }

    public static String getDropSqlByClass(Class clazz) {
        String tableName = getTableName(clazz);
        StringBuffer sql = new StringBuffer();
        sql.append("DROP TABLE IF EXISTS " + tableName + ";");

        return sql.toString();
    }

    public static String getCreateSqlByClass(Class clazz) {
        return getCreateSqlByClass(clazz, "");
    }

    //with default table name
    public static String getCreateSqlByClass(Class clazz, String table_name) {
        String tableName = table_name;
        if (tableName.isEmpty()) {
            tableName = getTableName(clazz);
        }

        List<Field> fields = getDBField(clazz);
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS " + tableName + "(");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            DBField dbField = field.getAnnotation(DBField.class);
            sql.append(getCreateFieldSqlByDBField(dbField));
            if (i < fields.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(");");
        Debug.Log("db_table: " + tableName, sql.toString());
        return sql.toString();
    }

    public static String getTableName(Class clazz) {
        DBTable dBTable = (DBTable) clazz.getAnnotations()[0];
        String tableName = dBTable.name();
        return tableName;
    }

    public static List<Field> getDBField(Class clazz) {
        List<Field> fields = clazzField.get(clazz);
        if (fields == null) {
            fields = getClassFieldByDBField(clazz);
            if (fields != null) {
                clazzField.put(clazz, fields);
            }
        }
        return fields;
    }

    public static void setObjectPropertyByCursor(Object object, Cursor cursor) {
        if (object == null || cursor == null) {
            return;
        }
        List<Field> fields = getDBField(object.getClass());
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = fields.get(i);
                DBField dbField = field.getAnnotation(DBField.class);
                int num = cursor.getColumnIndex(dbField.name());
                if (num < 0) {
                    break;
                }
                if (dbField.type() == DBField.Type.INTEGER) {
                    Long value = new Long(cursor.getInt(num));
                    if (value == null) {
                        field.set(object, null);
                        break;
                    }
                    if (field.getType().equals(Integer.class)) {
                        field.set(object, value.intValue());
                    } else if (field.getType().equals(Long.class)) {
                        field.set(object, value);
                    }
                } else if (dbField.type() == DBField.Type.VARCHAR || dbField.type() == DBField.Type.TEXT) {
                    String value = cursor.getString(num);
                    if (field.getType().equals(String.class)) {
                        field.set(object, value);
                    }
                } else if (dbField.type() == DBField.Type.DATE) {
                    String value = cursor.getString(num);
                    if (value == null) {
                        field.set(object, null);
                    } else {
                        field.set(object, simpleDateFormat.parse(value));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void setContentValuesByObject(Object object, ContentValues values) {
        List<Field> fields = getDBField(object.getClass());
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = fields.get(i);
                DBField dbField = field.getAnnotation(DBField.class);
                String name = dbField.name();
                if (dbField.type() == DBField.Type.INTEGER) {
                    Object value = field.get(object);
                    if (field.getType().equals(Integer.class)) {
                        values.put(name, (Integer) value);
                    } else if (field.getType().equals(Long.class)) {
                        values.put(name, (Long) value);
                    }
                } else if (dbField.type() == DBField.Type.VARCHAR || dbField.type() == DBField.Type.TEXT) {
                    if (field.getType().equals(String.class)) {
                        values.put(name, (String) field.get(object));
                    }
                } else if (dbField.type() == DBField.Type.DATE) {
                    if (field.getType().equals(Date.class)) {
                        Date date = (Date) field.get(object);
                        if (date == null) {
                            values.put(name, (String) null);
                        } else {
                            values.put(name, simpleDateFormat.format(date));
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
