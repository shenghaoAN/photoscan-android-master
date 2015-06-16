package com.jason.hao;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jason.fragment.HomeFragment;
import com.jason.fragment.MenuFragment;
import com.jason.view.DragLayout;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author jason
 */
public class MainActivity extends FragmentActivity {

    private DragLayout dl;
    private ImageView iv_icon;
    private ImageView iv_bottom;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDragLayout();
        findById();
        initFragment();
        initView();
    }

    private void findById() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_bottom.setImageResource(R.drawable.kenan);
        iv_icon.setImageResource(R.drawable.kenan);
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        menuFragment = new MenuFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linear_fragment, homeFragment);
        fragmentTransaction.add(R.id.menu_fragment, menuFragment);
        fragmentTransaction.commit();

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
