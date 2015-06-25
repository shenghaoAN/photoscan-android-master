package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.adapter.FavroiteAdapter;
import com.jason.bean.FavroiteBean;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.dbservice.FavroiteBeanService;
import com.jason.global.CommonData;
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

    private ListView listView;
    private FavroiteAdapter adapter;
    private FavroiteBeanService favroiteBeanService;
    private List<ItemCartoonDetailBean> itemCartoonDetailBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favroite);
        favroiteBeanService = FavroiteBeanService.instance(this);
        itemCartoonDetailBeans = new ArrayList<ItemCartoonDetailBean>();
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

        listView = (ListView) findViewById(R.id.listview);
        adapter = new FavroiteAdapter(this, favroiteBeanService.findAllList(), favroiteBeanService);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDataForFavroiteBean();
                Intent intent = new Intent(FavroiteActivity.this, ZoomProductActivity.class);
                Bundle bundle = new Bundle();
                ArrayList arrayList = new ArrayList();
                arrayList.add(itemCartoonDetailBeans);
                bundle.putParcelableArrayList(CommonData.LIST, arrayList);
                bundle.putInt(CommonData.POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 将数据库获取的数据保存到list中
     */
    private void getDataForFavroiteBean() {
        List<FavroiteBean> list = favroiteBeanService.findAllList();
        if (itemCartoonDetailBeans != null && !itemCartoonDetailBeans.isEmpty())
            itemCartoonDetailBeans.clear();
        if (list != null && !list.isEmpty()) {
            for (FavroiteBean favroiteBean : list) {
                ItemCartoonDetailBean itemCartoonDetailBean = new ItemCartoonDetailBean();
                itemCartoonDetailBean.desc = favroiteBean.description;
                itemCartoonDetailBean.colum = favroiteBean.colum;
                itemCartoonDetailBean.tag = favroiteBean.tag;
                itemCartoonDetailBean.image_url = favroiteBean.image_url;
                itemCartoonDetailBean.share_url = favroiteBean.share_url;
                itemCartoonDetailBeans.add(itemCartoonDetailBean);
            }
        }
    }


}
