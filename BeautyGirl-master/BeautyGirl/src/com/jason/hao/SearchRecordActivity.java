package com.jason.hao;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.adapter.SearchRecordAdapter;
import com.jason.bean.SearchBean;
import com.jason.dbservice.SearchBeanService;
import com.jason.pinnedheaderlistview.PinnedHeaderListView;
import com.jason.swipeback.SwipeBackActivity;
import com.jason.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private PinnedHeaderListView listView;
    private SearchRecordAdapter adapter;
    private SearchBeanService searchBeanService;

    private List<String> groupList;
    private List<List<SearchBean>> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_record);
        searchBeanService = SearchBeanService.instance(this);
        groupList = new ArrayList<String>();
        childList = new ArrayList<List<SearchBean>>();
        getList();  // 获取数据
        initView();
    }

    private void initView() {
        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(getString(R.string.setting_search));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
