package com.jason.hao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.jason.bean.Device;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //初始化BmobSDK
        Bmob.initialize(this, "c8ca6baff4ca7663b39cb5e3975a2adc");
        Save2Bmob();
        initView();
    }

    /**
     * 保存设备型号到云端Bmob数据库
     */
    private void Save2Bmob() {
        final Device device = new Device();
        device.setSdk(Build.VERSION.SDK_INT);
        device.setModel(android.os.Build.MODEL);
        device.setRelease(android.os.Build.VERSION.RELEASE);

        device.save(this, new SaveListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
