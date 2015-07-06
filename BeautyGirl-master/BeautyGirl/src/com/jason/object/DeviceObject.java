package com.jason.object;

import cn.bmob.v3.BmobObject;

/**
 * 用于Bmob后台服务器保存设备信息
 * <p/>
 * Created by shenghao on 2015/6/30.
 */
public class DeviceObject extends BmobObject {

    private Integer id;
    private Integer sdk;   //sdk版本号
    private String model;  //手机型号
    private String release;  //android系统版本号

    public Integer getSdk() {
        return sdk;
    }

    public void setSdk(Integer sdk) {
        this.sdk = sdk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
