package com.jason.bean;

import com.jason.database.DBField;
import com.jason.database.DBTable;
import com.jason.database.JsonField;

/**
 * Created by shenghao on 2015/6/23.
 */

@DBTable(name = "FavroiteBean")
public class FavroiteBean {

    public FavroiteBean(){

    }

    @JsonField(name="id")
    @DBField(name = "id", type = DBField.Type.INTEGER, primaryKey = true)
    public Long id;

    @JsonField(name="image_url")
    @DBField(name = "image_url", type = DBField.Type.TEXT, fineldName = "image_url")
    public String image_url;

    @JsonField(name="colum")
    @DBField(name = "colum", type = DBField.Type.TEXT, fineldName = "colum")
    public String colum;

    @JsonField(name="tag")
    @DBField(name = "tag", type = DBField.Type.TEXT, fineldName = "tag")
    public String tag;

    @JsonField(name="share_url")
    @DBField(name = "share_url", type = DBField.Type.TEXT, fineldName = "share_url")
    public String share_url;


    @JsonField(name="description")
    @DBField(name = "description", type = DBField.Type.TEXT, fineldName = "description")
    public String description;

    @Override
    public String toString() {
        return "FavroiteBean{" +
                "id=" + id +
                ", image_url='" + image_url + '\'' +
                ", colum='" + colum + '\'' +
                ", tag='" + tag + '\'' +
                ", share_url='" + share_url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
