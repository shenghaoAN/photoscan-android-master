package com.jason.hao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jason.Debug;
import com.jason.helper.BaiduLocationHelper;
import com.jason.object.DeviceObject;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/**
 * 欢迎页面 判断是否第一次启动应用
 * <p/>
 * Created by shenghao on 2015/5/22.
 */

public class WelcomeActivity extends BaseActivity {

    private SplashView splashView;
    private View splash;
    private RelativeLayout splashLayout;

    private boolean is_first_run;

    private LocationClient locationClient;
    private BaiduLocationHelper baiduLocationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //有米广告
        AdManager.getInstance(WelcomeActivity.this).init("5b834e4202318ff6", "36de673ce82c34e7", true);

        //初始化BmobSDK
        Bmob.initialize(this, "c8ca6baff4ca7663b39cb5e3975a2adc");
        locationClient = new LocationClient(this);
        baiduLocationHelper = new BaiduLocationHelper(this, locationClient);

        Save2Bmob();   //保存数据到bmob后台服务器
        initView();   //初始化view
        initLocation();   //开始定位
    }

    protected void initView() {

        // 嵌入有米广告
        // 第二个参数传入目标activity，或者传入null，改为setIntent传入跳转的intent
        splashView = new SplashView(this, null);
        // 设置是否显示倒数
        splashView.setShowReciprocal(true);
        // 隐藏关闭按钮
        splashView.hideCloseBtn(true);

        SharedPreferences preferences = getSharedPreferences("is_first_run",
                Context.MODE_PRIVATE);
        is_first_run = preferences.getBoolean("is_first_run", true);
        if (is_first_run) {
            preferences.edit().putBoolean("is_first_run", false).commit();
            Intent intent = new Intent(this, GuideActivity.class);
            splashView.setIntent(intent);
            splashView.setIsJumpTargetWhenFail(true);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            splashView.setIntent(intent);
            splashView.setIsJumpTargetWhenFail(true);
        }

        splash = splashView.getSplashView();
        setContentView(R.layout.activity_welcome);
        splashLayout = ((RelativeLayout) findViewById(R.id.splashview));
        splashLayout.setVisibility(View.GONE);
        splashLayout.addView(splash);

        SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
                new SpotDialogListener() {

                    @Override
                    public void onShowSuccess() {
                        splashLayout.setVisibility(View.VISIBLE);
                        splashLayout.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.pic_enter_anim_alpha));
                        Debug.Log("youmisdk", "展示成功");
                    }

                    @Override
                    public void onShowFailed() {
                        Debug.Log("youmisdk", "展示失败");
                    }

                    @Override
                    public void onSpotClosed() {
                        Debug.Log("youmisdk", "展示关闭");
                    }

                    @Override
                    public void onSpotClick(boolean isWebPath) {
                        Debug.Log("youmisdk", "插屏点击");
                    }
                });
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

    /**
     * 初始化定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");  //返回的定位结果是百度经纬度，默认值gcj02
        int span = 900;   //span < 1000 则为 app主动请求定位,span >= 1000 则表示定时定位
        option.setScanSpan(span);  //设置发起定位请求的间隔时间
        option.setIsNeedAddress(true);  //设置是否需要转换成地址
        option.setProdName("BeautyGirl");  //设置prod字段
        locationClient.setLocOption(option);
        locationClient.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port
        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduLocationHelper.setUnRegisterLocationListener(locationClient);
    }

    @Override
    public void onBackPressed() {
        // 取消后退键
    }
}
