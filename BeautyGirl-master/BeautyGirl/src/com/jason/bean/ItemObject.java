package com.jason.bean;

import java.io.Serializable;

/**
 * Created by shenghao on 2015/6/16.
 */
public class ItemObject implements Serializable {

    private String icon;
    private String title;
    private String tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
