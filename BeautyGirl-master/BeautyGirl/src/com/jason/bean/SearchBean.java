package com.jason.bean;

import com.jason.database.DBField;
import com.jason.database.DBTable;
import com.jason.database.JsonField;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索纪录表
 * <p/>
 * Created by shenghao on 2015/6/23.
 */

@DBTable(name = "SearchBean")
public class SearchBean implements Serializable {

    public SearchBean() {

    }

    @JsonField(name = "id")
    @DBField(name = "id", type = DBField.Type.INTEGER, primaryKey = true)
    public Long id;

    @JsonField(name = "text")
    @DBField(name = "text", type = DBField.Type.TEXT, fineldName = "text")
    public String text;

    @JsonField(name = "column")
    @DBField(name = "column", type = DBField.Type.TEXT, fineldName = "column")
    public String column;

    @JsonField(name = "date")
    @DBField(name = "date", type = DBField.Type.DATE, fineldName = "date")
    public Date date;

    @Override
    public String toString() {
        return "SearchBean{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", column='" + column + '\'' +
                ", date=" + date +
                '}';
    }
}
