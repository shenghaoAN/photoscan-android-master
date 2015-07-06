package com.jason.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jason.animation.ZoomOutPageTransformer;
import com.jason.hao.R;
import com.jason.view.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by shenghao on 2015/7/1.
 */
public class GagFragment extends BaseFragment {

    private ViewPager contentPager;
    private PagerAdapter adapter;
    private PagerSlidingTabStrip tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GagFragment"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GagFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gag, null);
        setPager(rootView);
        return rootView;
    }

    private void setPager(View rootView) {
        contentPager = (ViewPager) rootView.findViewById(R.id.content_pager);
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        contentPager.setAdapter(adapter);
        contentPager.setOffscreenPageLimit(2);
        contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setTextColorResource(R.color.light_gray_text);
        tabs.setDividerColorResource(R.color.common_list_divider);
        // tabs.setUnderlineColorResource(R.color.common_list_divider);
        tabs.setIndicatorColorResource(R.color.red);
        tabs.setSelectedTextColorResource(R.color.red);
        // tabs.setIndicatorHeight(5);
        tabs.setViewPager(contentPager);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private String Title[] = {"Hot", "Trending", "Fresh"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return GagListFragment.newInstance(Title[arg0].toLowerCase());
        }

        @Override
        public int getCount() {
            return Title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Title[position];
        }

    }
}
