package com.jason.object;

import java.io.Serializable;

/**
 *
 * 图片搜索实体类
 *
 * Created by shenghao on 2015/7/6.
 */
public class PhotoSearchObject implements Serializable {

    private String queryExt;
    private String objURL;
    private int width;
    private int height;
    private String fromPageTitleEnc;
    private String bdImgnewsDate;

    public String getQueryExt() {
        return queryExt;
    }

    public void setQueryExt(String queryExt) {
        this.queryExt = queryExt;
    }

    public String getObjURL() {
        return objURL;
    }

    public void setObjURL(String objURL) {
        this.objURL = objURL;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFromPageTitleEnc() {
        return fromPageTitleEnc;
    }

    public void setFromPageTitleEnc(String fromPageTitleEnc) {
        this.fromPageTitleEnc = fromPageTitleEnc;
    }

    public String getBdImgnewsDate() {
        return bdImgnewsDate;
    }

    public void setBdImgnewsDate(String bdImgnewsDate) {
        this.bdImgnewsDate = bdImgnewsDate;
    }
}
