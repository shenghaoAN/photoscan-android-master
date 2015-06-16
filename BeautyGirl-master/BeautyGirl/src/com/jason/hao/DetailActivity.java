package com.jason.hao;

import android.app.Activity;
import android.os.Bundle;

import com.huewu.pla.lib.WaterFallListView;
import com.jason.global.CommonData;

/**
 * Created by shenghao on 2015/6/16.
 */
public class DetailActivity extends Activity {

    private String grid_tag;

    private WaterFallListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        grid_tag = bundle.getString(CommonData.TAG);
        initView();
    }

    private void initView(){
        listView = (WaterFallListView) findViewById(R.id.myListview);
    }
}
