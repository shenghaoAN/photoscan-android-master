package com.jason.hao;

import android.os.Bundle;

import com.jason.swipeback.SwipeBackActivity;

/**
 * Created by shenghao on 2015/12/3.
 */
public class AdTipsActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        initAd();
        initTopbar();
        initView();
    }

    private void initView() {
        txt_title.setText(getString(R.string.ad_tips));
    }


}
