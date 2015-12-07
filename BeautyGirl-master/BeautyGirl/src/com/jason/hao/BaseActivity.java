package com.jason.hao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

/**
 * Created by shenghao on 2015/6/18.
 */
public class BaseActivity extends FragmentActivity {

    //topbar
    protected View topbar;
    protected ImageView img_back;
    protected TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果不调用此方法，将会导致按照"几天不活跃"条件来推送失效。
        //可以只在应用的主Activity中调用此方法，但是由于SDK的日志发送策略，不能保证一定可以统计到日活数据。
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    //onStop之前调用，用于保存信息
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //注释掉，让其不再保存Fragment的状态，达到其随着Activity一起被回收的效果！
//        super.onSaveInstanceState(outState);
    }

    /**
     * initTopbar
     */
    protected void initTopbar(){
        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(getString(R.string.setting_manager));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * initAd
     */
    protected void initAd(){
        // 实例化广告条
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        // 获取要嵌入广告条的布局
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
        // 将广告条加入到布局中
        adLayout.addView(adView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
