package com.jason.hao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jason.view.DragLayout;
import com.jason.view.Loading;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends Activity {

    private DragLayout dl;
    private ImageView iv_icon;
    private ImageView iv_bottom;

    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = new Loading(this);
        initDragLayout();
        findById();
        initView();
    }

    private void findById() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_bottom.setImageResource(R.drawable.huoying);
        iv_icon.setImageResource(R.drawable.huoying);
    }

    private void initView() {
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.open();
            }
        });
    }

    /**
     * 侧滑菜单
     */
    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(iv_icon, 1 - percent);
            }
        });
    }

    /**
     * 动画
     */
    private void shake() {
        iv_icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }


}
