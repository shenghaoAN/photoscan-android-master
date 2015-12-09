package com.jason.bean;

import com.jason.database.DBField;
import com.jason.database.DBTable;
import com.jason.database.JsonField;

import java.io.Serializable;

/**
 * 分类表
 * <p/>
 * Created by shenghao on 2015/6/25.
 */
@DBTable(name = "ItemCategoryBean")
public class ItemCategoryBean implements Serializable {

    public ItemCategoryBean() {

    }

    @JsonField(name = "id")
    @DBField(name = "id", type = DBField.Type.INTEGER, primaryKey = true)
    public Long id;

    @JsonField(name = "icon")
    @DBField(name = "icon", type = DBField.Type.TEXT, fineldName = "icon")
    public String icon;

    @JsonField(name = "title")
    @DBField(name = "title", type = DBField.Type.TEXT, fineldName = "title")
    public String title;

    @JsonField(name = "tag")
    @DBField(name = "tag", type = DBField.Type.TEXT, fineldName = "tag")
    public String tag;

    public String ftags = "";

}
