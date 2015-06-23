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
import com.jason.adapter.BeautyItemAdapter;
import com.jason.bean.CartoonObject;
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

    private ImageView img_back;
    private TextView txt_title;

    private ProgressBar progressBar;
    private TextView txt_error;
    private WaterFallListView listView;
    private BeautyItemAdapter beautyItemAdapter;

    private List<CartoonObject> cartoonObjects;

    private String grid_tag;  //小分类标签
    private String title;  //大分类
    private int pn = 0;   //从哪一条数据开始
    private int rn = 30;  //每次取多少条
    private int total;

    private boolean isFresh = false;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cartoonObjects = new ArrayList<CartoonObject>();
        Bundle bundle = getIntent().getExtras();
        grid_tag = bundle.getString(CommonData.TAG);
        title = bundle.getString(CommonData.TITLE);
        initView();
        getGridData();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(grid_tag);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_error = (TextView) findViewById(R.id.txt_error);
        txt_error.setVisibility(View.GONE);
        listView = (WaterFallListView) findViewById(R.id.myListview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new xListViewListener());
        beautyItemAdapter = new BeautyItemAdapter(this, cartoonObjects);

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
            if (cartoonObjects != null && cartoonObjects.size() < total) {
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
        params.put("tag1", title);
        params.put("tag2", grid_tag);
        client.get("channel/listjson?ie=utf8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {

                            if (cartoonObjects != null && cartoonObjects.size() > 0) {
                                if (isFresh) {
                                    cartoonObjects.clear();
                                    isFresh = false;
                                    listView.stopRefresh();
                                }
                                if (isLoadMore) {
                                    isLoadMore = false;
                                    listView.stopLoadMore();
                                }
                            }

                            progressBar.setVisibility(View.GONE);
                            total = totalNum;
                            if (total == 0) {
                                txt_error.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
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
                            e.printStackTrace();
                        }
                    }

                    public void Failure() {
                        ToastShow.displayToast(DetailActivity.this, getString(R.string.check_net));
                    }

                });
    }

    private void UpdateDatas(JSONArray datas) {
        for (int i = 0; i < datas.length(); i++) {
            try {
                CartoonObject cartoonObject = new CartoonObject();
                JSONObject d = (JSONObject) datas.get(i);
                cartoonObject.setDesc(d.getString("desc"));
                cartoonObject.setColum(d.getString("colum"));
                cartoonObject.setDate(d.getString("date"));
                cartoonObject.setImage_url(d.getString("image_url"));
                cartoonObject.setImage_width(d.getInt("image_width"));
                cartoonObject.setImage_height(d.getInt("image_height"));
                cartoonObject.setTag(d.getString("tag"));
                cartoonObject.setShare_url(d.getString("share_url"));
                cartoonObjects.add(cartoonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(1, cartoonObjects));

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Debug.Log("handler message", "success");
                    List<CartoonObject> list = (List<CartoonObject>) msg.obj;
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
