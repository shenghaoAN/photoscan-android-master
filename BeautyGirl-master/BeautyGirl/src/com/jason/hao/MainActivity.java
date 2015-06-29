package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.ItemCategoryBean;
import com.jason.fragment.HomeFragment;
import com.jason.fragment.MenuFragment;
import com.jason.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @author jason
 */
public class MainActivity extends BaseActivity {

    private DragLayout dl;
    private ImageView iv_icon;
    private TextView txt_title;
    private ImageView iv_set;
    private ImageView iv_bottom;

    private HomeFragment homeFragment;

    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);  //日志加密,true表示加密，false表示不加密
        UmengUpdateAgent.update(this);   //友盟更新
        setUpUmengFeedback();  //友盟反馈
        setUpUmengPush(); //友盟推送
        initDragLayout();
        findById();
        initFragment();
        initView();
    }

    /**
     * 友盟推送
     */
    private void setUpUmengPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
//        device_token = UmengRegistrar.getRegistrationId(this);
//        Debug.Log("device_token",device_token);
    }

    /**
     * 友盟反馈
     */
    private void setUpUmengFeedback() {
        FeedbackAgent fb = new FeedbackAgent(this);
        // check if the app developer has replied to the feedback or not.
        fb.sync();
        fb.openAudioFeedback();
    }

    private void findById() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_set = (ImageView) findViewById(R.id.iv_set);
        iv_bottom.setImageResource(R.drawable.kenan);
        iv_icon.setImageResource(R.drawable.kenan);
    }

    private void initFragment() {
        replaceFragment(R.id.menu_fragment, new MenuFragment());
        ItemCategoryBean itemObject = new ItemCategoryBean();
        itemObject.title = "明星";
        setCategory(itemObject);
    }

    private void initView() {
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.open();
            }
        });

        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
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

    /**
     * 点击菜单时切换fragment
     */
    public void setCategory(ItemCategoryBean itemCategoryBean) {
        dl.close();
        if (itemCategoryBean == null)
            return;
        txt_title.setText(itemCategoryBean.title);
        homeFragment = HomeFragment.newInstance(itemCategoryBean);
        replaceFragment(R.id.linear_fragment, homeFragment);
    }

    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

}
