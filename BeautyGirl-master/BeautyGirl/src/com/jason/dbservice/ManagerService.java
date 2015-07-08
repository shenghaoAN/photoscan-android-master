package com.jason.dbservice;

import android.content.Context;

/**
 * Created by shenghao on 2015/7/8.
 */
public class ManagerService {

    private Context context;
    private static ManagerService instance;

    private ManagerService(Context context) {
        this.context = context;
    }

    public static synchronized ManagerService instance(Context context) {
        if (instance == null) {
            instance = new ManagerService(context);
        }
        return instance;
    }

    public FavroiteBeanService getFavroiteBeanService() {
        return FavroiteBeanService.instance(this.context);
    }

    public ItemCartoonDetailBeanService getItemCartoonDetailBeanService() {
        return ItemCartoonDetailBeanService.instance(this.context);
    }

    public ItemCategoryBeanService getItemCategoryBeanService() {
        return ItemCategoryBeanService.instance(this.context);
    }

    public SearchBeanService getSearchBeanService() {
        return SearchBeanService.instance(this.context);
    }

}
