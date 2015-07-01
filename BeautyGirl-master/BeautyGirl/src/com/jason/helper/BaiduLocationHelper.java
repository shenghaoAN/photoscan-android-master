package com.jason.helper;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jason.Debug;
import com.jason.utils.ToastShow;

/**
 * 百度定位服务
 * <p/>
 * Created by shenghao on 2015/7/1.
 */
public class BaiduLocationHelper {

    private Context context;
    private MyLocationListener myLocationListener;

    public BaiduLocationHelper(Context context, LocationClient locationClient) {
        this.context = context;
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
    }

    /**
     * 反注册
     */
    public void setUnRegisterLocationListener(LocationClient locationClient) {
        if (locationClient.isStarted()) {
            locationClient.unRegisterLocationListener(myLocationListener);
        }
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getCity());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getCity());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            Debug.Log("BaiduLocationApiDem", sb.toString());
            ToastShow.displayToast(context, sb.toString());
        }
    }

}
