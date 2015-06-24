package com.jason.hao;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.swipeback.SwipeBackActivity;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(getString(R.string.setting_favroite));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
