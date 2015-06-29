package com.jason.hao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Random;

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
        initView();
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
