package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.Cfg;
import com.jason.Debug;
import com.jason.bean.ItemCategoryBean;
import com.jason.fragment.GagFragment;
import com.jason.fragment.HomeFragment;
import com.jason.fragment.MenuFragment;
import com.jason.global.CommonData;
import com.jason.utils.UniversalImageLoadTool;
import com.jason.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;

import net.youmi.android.spot.SpotManager;

/**
 * @author jason
 */
public class MainActivity extends BaseActivity {

    private DragLayout dl;
    private ImageView iv_icon;
    private TextView txt_title;
    private ImageView iv_set;
    private ImageView iv_bottom;
    private TextView txt_version;

    private HomeFragment homeFragment;

    //    private String icon = "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-9/12247100_1728831534006388_3356717374968482423_n.jpg?oh=45114efe136b2d0b69dcfd9bc4543936&oe=56EE4359&__gda__=1461502418_daeb74d421faa792a7544b923621f441";
    private String icon = "";

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
        String device_token = UmengRegistrar.getRegistrationId(this);
        Debug.Log("device_token", device_token);
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
        txt_version = (TextView) findViewById(R.id.txt_version);
        txt_version.setText("Version:" + Cfg.VersionName);

        iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.PHOTO, icon);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initFragment() {
        replaceFragment(R.id.menu_fragment, new MenuFragment());
        ItemCategoryBean itemObject = new ItemCategoryBean();
        itemObject.title = "明星";
        setCategory(itemObject);
//        showGagFragment();  //显示Menu gag页面
    }

    /**
     * 显示GagFragment页面
     */
    public void showGagFragment() {
        dl.close();
        replaceFragment(R.id.linear_fragment, new GagFragment());
        txt_title.setText(getString(R.string.app_name));
    }

    /**
     * initView
     */
    private void initView() {

        if (!icon.isEmpty()) {
            ImageLoader.getInstance().displayImage(icon, iv_icon,
                    UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));

            ImageLoader.getInstance().displayImage(icon, iv_bottom,
                    UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.huoying12, iv_icon,
                    UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));

            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.huoying12, iv_bottom,
                    UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        }

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
//                shake();
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

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), this.getString(R.string.out_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    protected void onStop() {
//        // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
//        SpotManager.getInstance(this).onStop();
//        super.onStop();
//    }
//
    @Override
    protected void onDestroy() {
//        SpotManager.getInstance(this).onDestroy();
        SpotManager.getInstance(this).setSpotOrientation(
                SpotManager.ORIENTATION_LANDSCAPE);
        SpotManager.getInstance(this).showSpotAds(this);
        super.onDestroy();
    }

}
