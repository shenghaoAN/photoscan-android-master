package com.jason.bean;

import java.io.Serializable;

/**
 * Created by shenghao on 2015/6/16.
 */
public class CartoonObject implements Serializable {

    private String id;
    private String desc;
    private String tag;
    private String date;
    private String image_url;
    private String colum;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColum() {
        return colum;
    }

    public void setColum(String colum) {
        this.colum = colum;
    }
}
