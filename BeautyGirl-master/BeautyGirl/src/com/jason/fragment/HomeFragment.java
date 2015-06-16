package com.jason.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jason.Debug;
import com.jason.adapter.ItemAdapter;
import com.jason.adapter.LunBoAdapter;
import com.jason.bean.CartoonObject;
import com.jason.bean.ItemObject;
import com.jason.global.CommonData;
import com.jason.hao.DetailActivity;
import com.jason.hao.R;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.utils.DensityUtils;
import com.jason.view.MyViewPager;
import com.jason.view.NoScrollListView;
import com.jason.view.ViewPagerFocusView;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shenghao on 2015/6/16.
 */
public class HomeFragment extends Fragment {

    private FrameLayout frame_ad;
    private MyViewPager viewpager_ad;
    private ViewPagerFocusView focusView;
    private NoScrollListView listView;
    private ItemAdapter itemAdapter;
    private List<ItemObject> itemObjects;

    private LunBoAdapter lunboadapter;
    private List<CartoonObject> cartoonObjects;

    private int ScreenWidth;

    //判断是否轮播
    private boolean ifStartLunbo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartoonObjects = new ArrayList<CartoonObject>();
        itemObjects = new ArrayList<ItemObject>();
        ScreenWidth = DensityUtils.getWidth(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        frame_ad = (FrameLayout) view.findViewById(R.id.frame_ad);
        viewpager_ad = (MyViewPager) view.findViewById(R.id.viewpager_ad);
        focusView = (ViewPagerFocusView) view.findViewById(R.id.viewpger_focusview);
        listView = (NoScrollListView) view.findViewById(R.id.listview);
        itemAdapter = new ItemAdapter(getActivity(), itemObjects);
        //广告显示大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenWidth, 331 * ScreenWidth / 584);//图片比例
        frame_ad.setLayoutParams(lp);
        frame_ad.setVisibility(View.GONE);
        getBanner();
        getItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemObject itemObject = (ItemObject) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.TAG, itemObject.getTag());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseLunbo();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLunbo();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        pauseLunbo();
    }

    /**
     * 停止轮播
     */
    public void pauseLunbo() {
        if (viewpager_ad != null && ifStartLunbo) {
            viewpager_ad.stopTimer();
            ifStartLunbo = false;
        }
    }

    /**
     * 开始轮播
     */
    public void startLunbo() {
        if (viewpager_ad != null && !ifStartLunbo) {
            viewpager_ad.startTimer();
            ifStartLunbo = true;
        }
    }

    /**
     * 轮播广告显示
     */
    private void initLunBoView() {
        lunboadapter = new LunBoAdapter(getActivity(), cartoonObjects);
        viewpager_ad.setAdapter(lunboadapter);
        viewpager_ad.setOnPageChangeListener(new GuidePageChangeListener());

        // 总的点数
        focusView.setCount(cartoonObjects.size());
        int gbs = 1000 / cartoonObjects.size();
        int totalMax = gbs * cartoonObjects.size();
        viewpager_ad.setCurrentItem(totalMax); // 初始位置在靠近1000的整个整除的数字

        // 当前位置
        int currentIndex = viewpager_ad.getCurrentItem() % cartoonObjects.size();
        focusView.setCurrentIndex(currentIndex);
        startLunbo();
    }

    /**
     * 广告滑动监听
     */
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(final int arg0) {
            int currentIndex = arg0 % cartoonObjects.size();
            focusView.setCurrentIndex(currentIndex);
        }
    }

    /**
     * 获取item
     */
    private void getItem() {
        Debug.Log("Item", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("column", "动漫");
        client.get("detail/info?fr=channel&ie=utf-8&oe=utf-8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String responseBody, Throwable throwable) {
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
        for (int i = 0; i < thumbs.length(); i++) {
            try {
                ItemObject itemObject = new ItemObject();
                JSONObject d = (JSONObject) thumbs.get(i);
                itemObject.setIcon(d.getString("thumb_url"));
                itemObject.setTitle(d.getString("column"));
                itemObject.setTag(convertTagName(d.getString("tag")));
                itemObjects.add(itemObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(2));

    }

    /**
     * 获取广告
     */
    private void getBanner() {
        Debug.Log("banner", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("pn", getRandom());
        params.put("rn", 4);
        client.get("channel/listjson?tag1=动漫&tag2=全部&ie=utf8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {
                            Debug.Log("banner", "success");
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (datas.length() > 0) {
                                        Debug.Log("banner", "has data");
                                        UpdateAD(datas);

                                    } else {
                                        Debug.Log("banner", "hide without data");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                frame_ad.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            });
                            thread.start();

                        } catch (Exception e) {
                            Debug.Log("banner error", "failed");
                            e.printStackTrace();
                        }
                    }

                    public void Failure() {
                        Debug.Log("banner get", "failed");
                    }

                });
    }

    private void UpdateAD(JSONArray datas) {
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

    private int getRandom() {
        Random random = new Random();
        return random.nextInt(100);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Debug.Log("handler message 1", "success");
                    frame_ad.setVisibility(View.VISIBLE);
                    if (cartoonObjects != null && cartoonObjects.size() > 0) {
                        initLunBoView();
                    }
                    break;
                case 2:
                    Debug.Log("handler message 2", "success");
                    listView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
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

}
