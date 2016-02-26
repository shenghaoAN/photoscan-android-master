package com.jason.bean;

import com.jason.database.DBField;
import com.jason.database.DBTable;
import com.jason.database.JsonField;

import java.io.Serializable;

/**
 * 分类详情表
 * <p/>
 * Created by shenghao on 2015/6/25.
 */

@DBTable(name = "ItemCartoonDetailBean")
public class ItemCartoonDetailBean implements Serializable {

    @JsonField(name = "id")
    @DBField(name = "id", type = DBField.Type.INTEGER, primaryKey = true)
    public Long id;

    @JsonField(name = "colum")
    @DBField(name = "colum", type = DBField.Type.TEXT, fineldName = "colum")
    public String colum;

    @JsonField(name = "tag")
    @DBField(name = "tag", type = DBField.Type.TEXT, fineldName = "tag")
    public String tag;

    @JsonField(name = "desc")
    @DBField(name = "desc", type = DBField.Type.TEXT, fineldName = "desc")
    public String desc;

    @JsonField(name = "date")
    @DBField(name = "date", type = DBField.Type.TEXT, fineldName = "date")
    public String date;

    @JsonField(name = "image_url")
    @DBField(name = "image_url", type = DBField.Type.TEXT, fineldName = "image_url")
    public String image_url;

    @JsonField(name = "image_width")
    @DBField(name = "image_width", type = DBField.Type.INTEGER, fineldName = "image_width")
    public Integer image_width;

    @JsonField(name = "image_height")
    @DBField(name = "image_height", type = DBField.Type.INTEGER, fineldName = "image_height")
    public Integer image_height;

    @JsonField(name = "share_url")
    @DBField(name = "share_url", type = DBField.Type.TEXT, fineldName = "share_url")
    public String share_url;

    public String ftags = "全部";

}
