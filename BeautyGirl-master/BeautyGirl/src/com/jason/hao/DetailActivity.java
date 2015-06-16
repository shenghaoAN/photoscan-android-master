package com.jason.hao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.huewu.pla.lib.WaterFallListView;
import com.jason.Debug;
import com.jason.adapter.BeautyItemAdapter;
import com.jason.bean.CartoonObject;
import com.jason.global.CommonData;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/16.
 */
public class DetailActivity extends Activity {

    private WaterFallListView listView;
    private BeautyItemAdapter beautyItemAdapter;

    private List<CartoonObject> cartoonObjects;

    private String grid_tag;  //分类标签
    private int pn = 0;   //从哪一条数据开始
    private int rn = 30;  //每次取多少条
    private boolean isFresh = false;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cartoonObjects = new ArrayList<CartoonObject>();
        Bundle bundle = getIntent().getExtras();
        grid_tag = bundle.getString(CommonData.TAG);
        initView();
        getGridData();
    }

    private void initView() {
        listView = (WaterFallListView) findViewById(R.id.myListview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        beautyItemAdapter = new BeautyItemAdapter(this, cartoonObjects);
        listView.setAdapter(beautyItemAdapter);
    }

    /**
     * 获取分类详细数据
     */
    private void getGridData() {
        Debug.Log("banner", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("pn", pn);
        params.put("rn", rn);
        params.put("tag2", grid_tag);
        client.get("channel/listjson?tag1=动漫&ie=utf8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {
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
                    }

                });
    }

    private void UpdateDatas(JSONArray datas) {
        for (int i = 0; i < datas.length(); i++) {
            try {
                CartoonObject cartoonObject = new CartoonObject();
                JSONObject d = (JSONObject) datas.get(i);
                cartoonObject.setId(d.getString("id"));
                cartoonObject.setDesc(d.getString("desc"));
                cartoonObject.setColum(d.getString("colum"));
                cartoonObject.setDate(d.getString("date"));
                cartoonObject.setImage_url(d.getString("image_url"));
                cartoonObject.setTag(d.getString("tag"));
                cartoonObjects.add(cartoonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(1));

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Debug.Log("handler message 1", "success");
                    beautyItemAdapter.updateAdapter(cartoonObjects);
                    break;
                default:
                    break;
            }
        }
    };


}
