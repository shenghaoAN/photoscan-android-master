package com.jason.hao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.ItemObject;
import com.jason.fragment.HomeFragment;
import com.jason.fragment.MenuFragment;
import com.jason.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * @author jason
 */
public class MainActivity extends BaseActivity {

    private DragLayout dl;
    private ImageView iv_icon;
    private TextView txt_title;
    private ImageView iv_bottom;

    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);  //日志加密,true表示加密，false表示不加密
        initDragLayout();
        findById();
        initFragment();
        initView();
    }

    private void findById() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_bottom.setImageResource(R.drawable.kenan);
        iv_icon.setImageResource(R.drawable.kenan);
    }

    private void initFragment() {
        replaceFragment(R.id.menu_fragment, new MenuFragment());
        ItemObject itemObject = new ItemObject();
        itemObject.setTitle("明星");
        setCategory(itemObject);
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

    /**
     * 点击菜单时切换fragment
     */
    public void setCategory(ItemObject itemObject) {
        dl.close();
        if (itemObject == null)
            return;
        txt_title.setText(itemObject.getTitle());
        homeFragment = HomeFragment.newInstance(itemObject);
        replaceFragment(R.id.linear_fragment, homeFragment);
    }

    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

}
