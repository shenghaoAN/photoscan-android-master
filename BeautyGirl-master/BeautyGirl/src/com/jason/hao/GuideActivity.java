package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jason.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面
 * <p/>
 * Created by shenghao on 2015/5/15.
 */

public class GuideActivity extends BaseActivity implements OnClickListener,
        OnPageChangeListener {

    private ViewPager vp;
    private GuideViewPagerAdapter vpAdapter;
    private List<View> views;

    // 引导图片资源
    private static final int[] pics = {R.drawable.default1, R.drawable.default2,
            R.drawable.default3, R.drawable.default4};

    // 底部图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViewById();
        initView();
        // 初始化底部小点
        initDots();

    }

    protected void findViewById() {
        vp = (ViewPager) findViewById(R.id.viewpager);
    }

    protected void initView() {

        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            if (i != pics.length - 1) {
                ImageView iv = new ImageView(this);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setLayoutParams(mParams);
                iv.setImageResource(pics[i]);
                views.add(iv);
            } else {
                // 如果是最后一张,添加button 跳转到主页面
                View view = LayoutInflater.from(this).inflate(
                        R.layout.item_image_btn, null);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(GuideActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                views.add(view);

            }
        }
        // 初始化Adapter
        vpAdapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     * 当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
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