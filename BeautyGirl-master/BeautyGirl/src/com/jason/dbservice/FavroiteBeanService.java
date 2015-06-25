package com.jason.dbservice;

import android.content.Context;

import com.jason.bean.FavroiteBean;

/**
 *
 * 收藏记录
 *
 * Created by shenghao on 2015/6/23.
 */
public class FavroiteBeanService extends BasisService {

    private static FavroiteBeanService service;

    public FavroiteBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = FavroiteBean.class;
    }

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static FavroiteBeanService instance(Context context) {
        if (service == null) {
            service = new FavroiteBeanService(context);
        }
        return service;
    }

}
