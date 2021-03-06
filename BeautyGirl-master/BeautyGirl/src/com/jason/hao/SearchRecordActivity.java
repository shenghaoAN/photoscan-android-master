package com.jason.hao;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.adapter.SearchRecordAdapter;
import com.jason.bean.SearchBean;
import com.jason.dbservice.ManagerService;
import com.jason.dbservice.SearchBeanService;
import com.jason.pinnedheaderlistview.PinnedHeaderListView;
import com.jason.swipeback.SwipeBackActivity;
import com.jason.utils.DensityUtils;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordActivity extends SwipeBackActivity {

    private TextView txt_no_data;
    private PinnedHeaderListView listView;
    private SearchRecordAdapter adapter;
    private SearchBeanService searchBeanService;

    private List<String> groupList;
    private List<List<SearchBean>> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_record);
        searchBeanService = ManagerService.instance(this).getSearchBeanService();
        groupList = new ArrayList<String>();
        childList = new ArrayList<List<SearchBean>>();
        getList();  // 获取数据

        initAd();
        initTopbar();
        initView();
    }

    private void initView() {

        txt_title.setText(getString(R.string.setting_search));

        txt_no_data = (TextView) findViewById(R.id.txt_no_data);
        listView = (PinnedHeaderListView) findViewById(R.id.listview);
        //添加headview
        TextView textView = new TextView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                DensityUtils.getWidth(this), DensityUtils.dip2px(this, 40));
        textView.setText(getString(R.string.long_click_to_delete_item));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(params);
        listView.addHeaderView(textView);
        adapter = new SearchRecordAdapter(this, searchBeanService, groupList, childList);
        listView.setAdapter(adapter);

        if (adapter.getCount() == 0) {
            listView.setVisibility(View.GONE);
            txt_no_data.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 用于adapter显示的数据源
     */
    private void getList() {
        groupList = searchBeanService.findGroupColumList();
        for (int i = 0; i < groupList.size(); i++) {
            List<SearchBean> searchBeans = searchBeanService.findListByColum(groupList.get(i));
            childList.add(searchBeans);
        }
    }
}
