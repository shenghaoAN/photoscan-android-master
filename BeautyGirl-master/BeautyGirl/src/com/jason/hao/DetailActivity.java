package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huewu.pla.lib.WaterFallListView;
import com.jason.Debug;
import com.jason.adapter.BeautyItemAdapter;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.dbservice.ItemCartoonDetailBeanService;
import com.jason.global.CommonData;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.swipeback.SwipeBackActivity;
import com.jason.utils.ToastShow;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 瀑布流形式显示图片
 * <p/>
 * Created by shenghao on 2015/6/16.
 */
public class DetailActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private ProgressBar progressBar;
    private TextView txt_error;
    private WaterFallListView listView;
    private BeautyItemAdapter beautyItemAdapter;

    private List<ItemCartoonDetailBean> itemCartoonDetailBeans;

    private String tag;  //小分类标签
    private String colum;  //大分类
    private int pn = 0;   //从哪一条数据开始
    private int rn = 30;  //每次取多少条
    private int total;

    private boolean isFresh = false;
    private boolean isLoadMore = false;

    private ItemCartoonDetailBeanService itemCartoonDetailBeanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        itemCartoonDetailBeans = new ArrayList<ItemCartoonDetailBean>();
        itemCartoonDetailBeanService = ItemCartoonDetailBeanService.instance(this);
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString(CommonData.TAG);
        colum = bundle.getString(CommonData.TITLE);
        initView();
        getGridData();
    }

    private void initView() {
        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(tag);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_error = (TextView) findViewById(R.id.txt_error);
        txt_error.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        listView = (WaterFallListView) findViewById(R.id.myListview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new xListViewListener());
        beautyItemAdapter = new BeautyItemAdapter(this, itemCartoonDetailBeans);

        listView.setAdapter(beautyItemAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });

        txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView != null && listView.getCount() > 0) {
                    listView.smoothScrollToPosition(0);   //滚动到顶部
                }
            }
        });
    }

    /**
     * 刷新监听器
     */
    class xListViewListener implements WaterFallListView.IXListViewListener {

        @Override
        public void onRefresh() {
            isFresh = true;
            pn = 0;
            getGridData();
        }

        @Override
        public void onLoadMore() {
            if (itemCartoonDetailBeans != null && itemCartoonDetailBeans.size() < total) {
                isLoadMore = true;
                pn = pn + rn;
                getGridData();
            } else {
                listView.stopLoadMore();
                ToastShow.displayToast(DetailActivity.this, getString(R.string.finish_data));
            }
        }
    }

    /**
     * 获取分类详细数据
     */
    private void getGridData() {
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("pn", pn);
        params.put("rn", rn);
        params.put("tag1", colum);
        params.put("tag2", tag);
        client.get("channel/listjson?ie=utf8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {
                            if (itemCartoonDetailBeans != null && itemCartoonDetailBeans.size() > 0) {
                                if (isFresh) {
                                    itemCartoonDetailBeans.clear();
                                    isFresh = false;
                                    listView.stopRefresh();
                                }
                            }

                            total = totalNum;
                            //没有找到需要搜索的分类
                            if (total == 0) {
                                listView.setVisibility(View.GONE);
                                txt_error.setText(getString(R.string.search_error));
                                txt_error.setVisibility(View.VISIBLE);
                                return;
                            }

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (datas.length() > 0) {
                                        Debug.Log("detail", "has data");
                                        UpdateDatas(datas);
                                    }
                                }
                            });
                            thread.start();

                        } catch (Exception e) {
                            if (isFresh) {
                                listView.stopRefresh();
                            }
                            e.printStackTrace();
                        }
                    }

                    public void Failure() {
                        progressBar.setVisibility(View.GONE);
                        ToastShow.displayToast(DetailActivity.this, getString(R.string.check_net));
                        if (isFresh) {
                            listView.stopRefresh();
                        }

                        if (itemCartoonDetailBeans.isEmpty()) {
                            //取出本地数据
                            List<ItemCartoonDetailBean> list = itemCartoonDetailBeanService.findListByColumTag(colum, tag);
                            if (list.size() == 0) {
                                listView.setVisibility(View.GONE);
                                txt_error.setVisibility(View.VISIBLE);
                                txt_error.setText(getString(R.string.net_error));
                                txt_error.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //打开网络连接
                                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    }
                                });
                                return;
                            }
                            //发送数据到handler中,刷新adapter
                            handler.sendMessage(handler.obtainMessage(1, list));
                        }
                    }

                });
    }

    /**
     * UpdateDatas
     *
     * @param datas
     */
    private void UpdateDatas(JSONArray datas) {

        if (itemCartoonDetailBeans != null && itemCartoonDetailBeans.size() > 0) {
            itemCartoonDetailBeanService.deleteByColumTag(colum, tag);
        }

        for (int i = 0; i < datas.length(); i++) {
            try {
                ItemCartoonDetailBean itemCartoonDetailBean = new ItemCartoonDetailBean();
                JSONObject d = (JSONObject) datas.get(i);
                itemCartoonDetailBean.desc = d.getString("desc");
                itemCartoonDetailBean.colum = d.getString("colum");
                itemCartoonDetailBean.tag = d.getString("tag");
                itemCartoonDetailBean.date = d.getString("date");
                itemCartoonDetailBean.image_url = d.getString("image_url");
                itemCartoonDetailBean.image_width = d.getInt("image_width");
                itemCartoonDetailBean.image_height = d.getInt("image_height");
                itemCartoonDetailBean.share_url = d.getString("share_url");
                itemCartoonDetailBeanService.save(itemCartoonDetailBean);
                itemCartoonDetailBeans.add(itemCartoonDetailBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(1, itemCartoonDetailBeans));

    }

    /**
     * handler处理数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Debug.Log("handler message", "success");
                    if (isLoadMore) {
                        isLoadMore = false;
                        listView.stopLoadMore();
                    }
                    if (progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }
                    List<ItemCartoonDetailBean> list = (List<ItemCartoonDetailBean>) msg.obj;
                    beautyItemAdapter.updateAdapter(list);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 随机数
     *
     * @return
     */
    private int getRandom() {
        Random random = new Random();
        return random.nextInt(100);
    }

}
