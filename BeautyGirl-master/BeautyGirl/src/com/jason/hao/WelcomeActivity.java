package com.jason.hao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.jason.object.DeviceObject;

import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/**
 * 欢迎页面 判断是否第一次启动应用
 * <p/>
 * Created by shenghao on 2015/5/22.
 */

public class WelcomeActivity extends BaseActivity {

    private ImageView img_view;

    private boolean is_first_run;
    private Handler handler;

    private int[] pics = {R.drawable.default1, R.drawable.default2,
            R.drawable.default3, R.drawable.default4, R.drawable.default5};

//    private LocationClient locationClient;
//    private BaiduLocationHelper baiduLocationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //初始化BmobSDK
        Bmob.initialize(this, "c8ca6baff4ca7663b39cb5e3975a2adc");
//        locationClient = new LocationClient(this);
//        baiduLocationHelper = new BaiduLocationHelper(this, locationClient);

        Save2Bmob();   //保存数据到bmob后台服务器
        initView();   //初始化view
//        initLocation();   //开始定位
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 保存设备型号到云端Bmob数据库
     */
    private void Save2Bmob() {
        final DeviceObject deviceObject = new DeviceObject();
        deviceObject.setSdk(Build.VERSION.SDK_INT);
        deviceObject.setModel(android.os.Build.MODEL);
        deviceObject.setRelease(android.os.Build.VERSION.RELEASE);

        deviceObject.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    protected void initView() {

        img_view = (ImageView) findViewById(R.id.img_view);
        img_view.setImageResource(pics[new Random().nextInt(5)]);

        handler = new Handler() {
            /**
             * 0：跳转到引导页面；1：跳转到首页。
             */
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                Intent intent;
                switch (msg.what) {
                    case 0:
                        intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };

        SharedPreferences preferences = getSharedPreferences("is_first_run",
                Context.MODE_PRIVATE);
        is_first_run = preferences.getBoolean("is_first_run", true);
        if (is_first_run) {
            preferences.edit().putBoolean("is_first_run", false).commit();
            handler.sendEmptyMessage(0);
        } else {
            handler.sendEmptyMessageDelayed(1, 1500);
        }
    }

    /**
     * 初始化定位
     */
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
//        option.setCoorType("gcj02");  //返回的定位结果是百度经纬度，默认值gcj02
//        int span = 900;   //span < 1000 则为 app主动请求定位,span >= 1000 则表示定时定位
//        option.setScanSpan(span);  //设置发起定位请求的间隔时间
//        option.setIsNeedAddress(true);  //设置是否需要转换成地址
//        option.setProdName("BeautyGirl");  //设置prod字段
//        locationClient.setLocOption(option);
//        locationClient.start();
//    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
//        locationClient.stop();
        super.onStop();
    }

}
