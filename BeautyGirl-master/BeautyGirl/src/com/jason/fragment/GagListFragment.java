package com.jason.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jason.Cfg;
import com.jason.Debug;
import com.jason.adapter.ItemGagAdapter;
import com.jason.global.CommonData;
import com.jason.hao.R;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.object.GagObject;
import com.jason.view.LoadingFooter;
import com.jason.view.PageListView;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/7/1.
 */
public class GagListFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PageListView listView;
    private ItemGagAdapter adapter;

    private List<GagObject> gagObjects;

    //标志是否刷新
    private boolean isRefresh = false;

    private String title = "";

    private String page = "0";

    /**
     * 实例化fragment
     */
    public static GagListFragment newInstance(String str) {
        GagListFragment fragment = new GagListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonData.TITLE, str);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 取出参数
     */
    private void parseArgument() {
        Bundle bundle = getArguments();
        title = bundle.getString(CommonData.TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gagObjects = new ArrayList<GagObject>();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GagListFragment"); // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GagListFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gag_list, container, false);
        parseArgument();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        listView = (PageListView) view.findViewById(R.id.listview);
        adapter = new ItemGagAdapter(getActivity(), gagObjects);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new onRefreshListener());
        listView.setLoadNextListener(new onNextListener());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getGagData();
        return view;
    }

    /**
     * 下拉刷新监听器
     */
    class onRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            isRefresh = true;
            swipeRefreshLayout.setRefreshing(true);
            page = "0";
            getGagData();
        }
    }

    /**
     * 加载更多监听器
     */
    class onNextListener implements PageListView.OnLoadNextListener {

        @Override
        public void onLoadNext() {
            isRefresh = false;
            getGagData();
        }
    }

    /**
     * 联网获取数据
     */
    private void getGagData() {
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        client.post(Cfg.GagUrl + title + "/" + page, params, new JSONHttpHelper.JSONHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, byte[] responseBody, Throwable throwable) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Debug.Log("server response:", response);
                try {

                    if (gagObjects != null && gagObjects.size() > 0) {
                        if (isRefresh) {
                            isRefresh = false;
                            gagObjects.clear();
                        } else {
                            listView.setState(LoadingFooter.State.Idle, 3000);
                        }
                    }

                    JSONObject jsonObject = new JSONObject(response.trim());
                    JSONArray data = (JSONArray) jsonObject.get("data");
                    page = jsonObject.getJSONObject("paging").getString("next");
                    UpdateItem(data);
                } catch (JSONException e) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        listView.setState(LoadingFooter.State.Idle, 3000);
                    }
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @param data
     */
    private void UpdateItem(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject d = data.getJSONObject(i);
                GagObject gagObject = new GagObject();
                gagObject.setId(d.getString("id"));
                gagObject.setCaption(d.getString("caption"));
                gagObject.setNormal(d.getJSONObject("images").getString("normal"));
                gagObject.setLink(d.getString("link"));
                gagObject.setCount(d.getJSONObject("votes").getString("count"));
                gagObjects.add(gagObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(0, gagObjects));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<GagObject> list = (List<GagObject>) msg.obj;
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.updateAdapter(list);
                    break;
                default:
                    break;
            }
        }
    };

}
