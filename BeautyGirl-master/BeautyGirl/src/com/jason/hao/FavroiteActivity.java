package com.jason.hao;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.adapter.FavroiteAdapter;
import com.jason.bean.FavroiteBean;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.dbservice.FavroiteBeanService;
import com.jason.dbservice.ManagerService;
import com.jason.pinnedheaderlistview.PinnedHeaderListView;
import com.jason.swipeback.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class FavroiteActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private TextView txt_no_data;
    private PinnedHeaderListView listView;
    private FavroiteAdapter adapter;
    private FavroiteBeanService favroiteBeanService;
    private List<ItemCartoonDetailBean> itemCartoonDetailBeans;

    private List<String> groupList;
    private List<List<FavroiteBean>> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favroite);
        favroiteBeanService = ManagerService.instance(this).getFavroiteBeanService();
        itemCartoonDetailBeans = new ArrayList<ItemCartoonDetailBean>();
        groupList = new ArrayList<String>();
        childList = new ArrayList<List<FavroiteBean>>();
        getList(); //获取数据源
        initView();
    }

    private void initView() {

        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(getString(R.string.setting_favroite));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_no_data = (TextView) findViewById(R.id.txt_no_data);
        listView = (PinnedHeaderListView) findViewById(R.id.listview);
        adapter = new FavroiteAdapter(this, groupList, childList, favroiteBeanService);
        listView.setAdapter(adapter);

        if(adapter.getCount() == 0){
            listView.setVisibility(View.GONE);
            txt_no_data.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 用于adapter显示的数据源
     */
    private void getList() {
        groupList = favroiteBeanService.findGroupTagList();
        for (int i = 0; i < groupList.size(); i++) {
            List<FavroiteBean> list = favroiteBeanService.findListByTag(groupList.get(i));
            childList.add(list);
        }
    }

}
