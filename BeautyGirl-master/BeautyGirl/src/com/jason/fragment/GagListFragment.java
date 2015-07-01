package com.jason.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shenghao on 2015/7/1.
 */
public class GagListFragment extends BaseFragment {

    /**
     * 实例化fragment
     */
    public static GagListFragment newInstance() {
        GagListFragment fragment = new GagListFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(CommonData.TITLE, itemCategoryBean.title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("HHHHHH");
        return textView;
    }


}
