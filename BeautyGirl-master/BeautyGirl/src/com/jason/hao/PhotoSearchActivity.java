package com.jason.hao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huewu.pla.lib.WaterFallListView;
import com.jason.Debug;
import com.jason.adapter.PhotoSearchAdapter;
import com.jason.global.CommonData;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.object.PhotoSearchObject;
import com.jason.swipeback.SwipeBackActivity;
import com.jason.utils.ToastShow;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据关键字搜索图片
 *
 * Created by shenghao on 2015/7/6.
 */
public class PhotoSearchActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private ProgressBar progressBar;
    private TextView txt_error;
    private WaterFallListView listView;
    private PhotoSearchAdapter adapter;

    private int pn = 0;   //从哪一条数据开始
    private int rn = 30;  //每次取多少条
    private int total = 0;

    private boolean isFresh = false;
    private boolean isLoadMore = false;

    private String word = "";   //搜索关键字
    private List<PhotoSearchObject> photoSearchObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        photoSearchObjects = new ArrayList<PhotoSearchObject>();
        Bundle bundle = getIntent().getExtras();
        word = bundle.getString(CommonData.WORD);
        initView();
        getGridData();
    }

    private void initView() {
        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(word);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_error = (TextView) findViewById(R.id.txt_error);
        txt_error.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        listView = (WaterFallListView) findViewById(R.id.myListview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new xListViewListener());
        adapter = new PhotoSearchAdapter(this, photoSearchObjects);

        listView.setAdapter(adapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoSearchActivity.this.finish();
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
            if (photoSearchObjects != null && photoSearchObjects.size() < total) {
                isLoadMore = true;
                pn = pn + rn;
                getGridData();
            } else {
                listView.stopLoadMore();
                ToastShow.displayToast(PhotoSearchActivity.this, getString(R.string.finish_data));
            }
        }
    }

    /**
     * 获取搜索图片的详细数据
     */
    private void getGridData() {
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("pn", pn);
        params.put("rn", rn);
        params.put("word", word);
        client.get("i?tn=baiduimagejson&width=&height=&face=0&istype=2&ie=utf-8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {
                            if (photoSearchObjects != null && photoSearchObjects.size() > 0) {
                                if (isFresh) {
                                    photoSearchObjects.clear();
                                    isFresh = false;
                                    listView.stopRefresh();
                                }
                            }

                            total = totalNum;
                            //没有找到需要搜索的分类
                            if (total == 0) {
                                progressBar.setVisibility(View.GONE);
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
                        ToastShow.displayToast(PhotoSearchActivity.this, getString(R.string.check_net));
                        if (isFresh) {
                            listView.stopRefresh();
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
        for (int i = 0; i < datas.length(); i++) {
            try {
                PhotoSearchObject photoSearchObject = new PhotoSearchObject();
                JSONObject d = (JSONObject) datas.get(i);
                photoSearchObject.setQueryExt(word);
                photoSearchObject.setObjURL(d.getString("objURL"));
                photoSearchObject.setFromPageTitleEnc(d.getString("fromPageTitleEnc"));
                photoSearchObject.setWidth(d.getInt("width"));
                photoSearchObject.setHeight(d.getInt("height"));
                photoSearchObject.setBdImgnewsDate(d.getString("bdImgnewsDate"));
                photoSearchObjects.add(photoSearchObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(1, photoSearchObjects));

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
                    List<PhotoSearchObject> list = (List<PhotoSearchObject>) msg.obj;
                    adapter.updateAdapter(list);
                    break;
                default:
                    break;
            }
        }
    };


}
