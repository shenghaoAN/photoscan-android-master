package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.adapter.SearchRecordAdapter;
import com.jason.bean.SearchBean;
import com.jason.dbservice.SearchBeanService;
import com.jason.global.CommonData;
import com.jason.swipeback.SwipeBackActivity;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private ListView listView;
    private SearchRecordAdapter adapter;
    private SearchBeanService searchBeanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_record);
        searchBeanService = SearchBeanService.instance(this);
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

        listView = (ListView) findViewById(R.id.listview);
        adapter = new SearchRecordAdapter(this, searchBeanService.findDistinctList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchBean searchBean = (SearchBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchRecordActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.TAG, searchBean.text);
                bundle.putString(CommonData.TITLE, searchBean.column);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
