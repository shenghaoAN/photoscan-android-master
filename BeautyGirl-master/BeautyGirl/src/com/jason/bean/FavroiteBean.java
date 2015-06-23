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

    @JsonField(name="description")
    @DBField(name = "description", type = DBField.Type.TEXT, fineldName = "description")
    public String description;

    @Override
    public String toString() {
        return "FavroiteBean{" +
                "id=" + id +
                ", image_url='" + image_url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
