package com.jason.dbservice;

import android.content.Context;

import com.jason.bean.SearchBean;

/**
 * Created by shenghao on 2015/6/23.
 */
public class SearchBeanService extends BasisService {

    private static SearchBeanService service;

    public SearchBeanService(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.clazz = SearchBean.class;
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static SearchBeanService instance(Context context) {
        if (service == null) {
            service = new SearchBeanService(context);
        }
        return service;
    }

}
