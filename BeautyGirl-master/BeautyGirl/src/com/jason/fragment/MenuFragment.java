package com.jason.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jason.Debug;
import com.jason.adapter.MenuAdapter;
import com.jason.bean.ItemCategoryBean;
import com.jason.hao.MainActivity;
import com.jason.hao.R;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.helper.MenuHelper;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-1 on 2015/6/16.
 */
public class MenuFragment extends BaseFragment {

    private MainActivity mActivity;

    private ListView listView;
    private MenuAdapter menuAdapter;
    private List<ItemCategoryBean> itemObjects;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemObjects = new ArrayList<ItemCategoryBean>();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("onPageStart MenuFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("onPageEnd MenuFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        menuAdapter = new MenuAdapter(getActivity(), itemObjects);
        getItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemCategoryBean itemObject = (ItemCategoryBean) parent.getItemAtPosition(position);
                if (itemObject.tag.equals("9GAG")) {
                    mActivity.showGagFragment();
                } else {
                    mActivity.setCategory(itemObject);
                }
            }
        });
        return view;
    }

    /**
     * 获取item
     */
    private void getItem() {
        Debug.Log("Item", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        client.get("detail/info?fr=channel&ie=utf-8&oe=utf-8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String responseBody, Throwable throwable) {
                        try {
                            UpdateItem(MenuHelper.getMenuLocal(getActivity()));  //网络连接失败时，取本地数据
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String responseBody) {
                        Debug.Log("server response:", responseBody);
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.trim());
                            JSONObject data = (JSONObject) jsonObject.get("data");
                            JSONArray thumbs = data.getJSONArray("thumbs");
                            UpdateItem(thumbs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void UpdateItem(JSONArray thumbs) {

        ItemCategoryBean itemCategoryBean = new ItemCategoryBean();
        itemCategoryBean.icon = "";
        itemCategoryBean.title = "9GAG";
        itemCategoryBean.tag = "9GAG";
        itemObjects.add(itemCategoryBean);

        for (int i = 0; i < thumbs.length(); i++) {
            try {
                ItemCategoryBean itemObject = new ItemCategoryBean();
                JSONObject d = (JSONObject) thumbs.get(i);
                itemObject.icon = d.getString("thumb_url");
                itemObject.title = d.getString("column");
                itemObject.tag = convertTagName(d.getString("tag"));
                itemObjects.add(itemObject);
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
                    listView.setAdapter(menuAdapter);
                    listView.setItemChecked(0, true);
                    menuAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 去除符号
     */
    private String convertTagName(String str) {

        str = str.replace("\"", "");
        str = str.replace("[", "");
        str = str.replace("]", "");

        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
