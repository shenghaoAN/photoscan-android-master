package com.jason.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jason.Cfg;
import com.jason.Debug;
import com.jason.adapter.ItemAdapter;
import com.jason.adapter.LunBoAdapter;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.bean.ItemCategoryBean;
import com.jason.bean.SearchBean;
import com.jason.dbservice.ItemCartoonDetailBeanService;
import com.jason.dbservice.ItemCategoryBeanService;
import com.jason.dbservice.ManagerService;
import com.jason.dbservice.SearchBeanService;
import com.jason.global.CommonData;
import com.jason.hao.DetailActivity;
import com.jason.hao.R;
import com.jason.helper.HttpClientHelper;
import com.jason.helper.JSONHttpHelper;
import com.jason.utils.DensityUtils;
import com.jason.utils.ToastShow;
import com.jason.view.MyViewPager;
import com.jason.view.NoScrollListView;
import com.jason.view.VerticalScrollView;
import com.jason.view.ViewPagerFocusView;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by shenghao on 2015/6/16.
 */
public class HomeFragment extends BaseFragment {

    //view
    private SwipeRefreshLayout swipeRefreshLayout;   //下拉刷新
    private VerticalScrollView scrollView;
    private FrameLayout frame_ad;
    private MyViewPager viewpager_ad;
    private ViewPagerFocusView focusView;
    private NoScrollListView listView;
    private ProgressBar progressBar;

    //搜索栏
    private AutoCompleteTextView edit_search;
    private ImageButton imgbtn_search;
    private ImageButton imgbtn_cancel;
    private ArrayAdapter<String> autoCompltetAdapter;

    //item数据
    private ItemAdapter itemAdapter;
    private List<ItemCategoryBean> itemCategoryBeans;

    //banner数据
    private LunBoAdapter lunboadapter;
    private List<ItemCartoonDetailBean> itemCartoonDetailBeans;

    private int ScreenWidth;   //屏幕宽度

    //标志是否刷新
    private boolean isRefresh = false;

    //判断是否轮播
    private boolean ifStartLunbo = false;

    private String title;  //post的参数

    //数据库处理
    private SearchBeanService searchBeanService;
    private ItemCategoryBeanService itemCategoryBeanService;
    private ItemCartoonDetailBeanService itemCartoonDetailBeanService;

    /**
     * 实例化fragment
     * 接收activity的参数
     *
     * @param itemCategoryBean
     * @return
     */
    public static HomeFragment newInstance(ItemCategoryBean itemCategoryBean) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonData.TITLE, itemCategoryBean.title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBeanService = ManagerService.instance(getActivity()).getSearchBeanService();
        itemCategoryBeanService = ManagerService.instance(getActivity()).getItemCategoryBeanService();
        itemCartoonDetailBeanService = ManagerService.instance(getActivity()).getItemCartoonDetailBeanService();
        itemCartoonDetailBeans = new ArrayList<ItemCartoonDetailBean>();
        itemCategoryBeans = new ArrayList<ItemCategoryBean>();
        ScreenWidth = DensityUtils.getWidth(getActivity());
    }

    /**
     * 取出参数
     */
    private void parseArgument() {
        Bundle bundle = getArguments();
        title = bundle.getString(CommonData.TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initAD();
        initView(view);
        parseArgument();
        getBanner();
        getItem();
        setClickListener();

        return view;
    }

    /**
     * 设置插屏广告
     */
    private void initAD() {
        if (Cfg.showHomeAdNum % Cfg.adNum == 0) {
            // 加载插播资源
            SpotManager.getInstance(getActivity()).loadSpotAds();
            SpotManager.getInstance(getActivity()).setSpotOrientation(
                    SpotManager.ORIENTATION_LANDSCAPE);
            SpotManager.getInstance(getActivity()).showSpotAds(getActivity());
        }
        Cfg.showHomeAdNum++;
    }

    /**
     * initView
     *
     * @param view
     */
    private void initView(View view) {
        // 实例化广告条
        AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
        // 获取要嵌入广告条的布局
        LinearLayout adLayout = (LinearLayout) view.findViewById(R.id.adLayout);
        // 将广告条加入到布局中
        adLayout.addView(adView);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        scrollView = (VerticalScrollView) view.findViewById(R.id.scrollview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        frame_ad = (FrameLayout) view.findViewById(R.id.frame_ad);
        viewpager_ad = (MyViewPager) view.findViewById(R.id.viewpager_ad);
        focusView = (ViewPagerFocusView) view.findViewById(R.id.viewpger_focusview);
        listView = (NoScrollListView) view.findViewById(R.id.listview);
        itemAdapter = new ItemAdapter(getActivity(), itemCategoryBeans);
        listView.setAdapter(itemAdapter);
        //广告显示大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenWidth, ScreenWidth);//图片比例
        frame_ad.setLayoutParams(lp);
        frame_ad.setVisibility(View.GONE);

        //搜索
        edit_search = (AutoCompleteTextView) view.findViewById(R.id.edit_search);
        imgbtn_search = (ImageButton) view.findViewById(R.id.imgbtn_search);
        imgbtn_cancel = (ImageButton) view.findViewById(R.id.imgbtn_cancel);
        if (edit_search.getText().toString().isEmpty()) {
            imgbtn_cancel.setVisibility(View.GONE);
        }
        if (searchBeanService.findTexts() != null) {
            autoCompltetAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, android.R.id.text1, searchBeanService.findTexts());
            edit_search.setAdapter(autoCompltetAdapter);
        }

        swipeRefreshLayout.setOnRefreshListener(new onRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 事件监听器
     */
    private void setClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemCategoryBean itemCategoryBean = (ItemCategoryBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.TAG, itemCategoryBean.tag);
                bundle.putString(CommonData.FTAGS, itemCategoryBean.ftags);
                bundle.putString(CommonData.TITLE, itemCategoryBean.title);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DensityUtils.hideSoftWindow(getActivity(), edit_search);
                return false;
            }
        });

        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchText();
            }
        });

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchText();
                }
                return false;
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edit_search.getText().toString();
                if (str.isEmpty()) {
                    imgbtn_cancel.setVisibility(View.GONE);
                } else {
                    imgbtn_cancel.setVisibility(View.VISIBLE);
                }
            }
        });

        imgbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search.setText("");
                imgbtn_cancel.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 搜索
     */
    private void SearchText() {
        if (edit_search.getText().toString().isEmpty()) {
            if (isAdded()) {
                ToastShow.displayToast(getActivity(), getActivity().getResources().getString(R.string.search));
            }
            return;
        }

        //保存搜索记录到数据库
        SearchBean searchBean = new SearchBean();
        searchBean.text = edit_search.getText().toString();
        searchBean.column = title;
        searchBean.date = new Date();
        searchBeanService.save(searchBean);

        //保存到云端Bmob服务器
        searchBean.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
//                ToastShow.displayToast(getActivity(),"成功");
            }

            @Override
            public void onFailure(int i, String s) {
//                ToastShow.displayToast(getActivity(),"失败");
            }
        });

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CommonData.TAG, edit_search.getText().toString());
        bundle.putString(CommonData.TITLE, title);
        intent.putExtras(bundle);
        startActivity(intent);
        DensityUtils.hideSoftWindow(getActivity(), edit_search);

//        Intent intent = new Intent(getActivity(), PhotoSearchActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(CommonData.WORD, edit_search.getText().toString());
//        intent.putExtras(bundle);
//        startActivity(intent);
//        DensityUtils.hideSoftWindow(getActivity(), edit_search);
    }

    /**
     * 下拉刷新监听器
     */
    class onRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            isRefresh = true;
            swipeRefreshLayout.setRefreshing(true);
            getItem();
            getBanner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("onPageEnd HomeFragment");
        pauseLunbo();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("onPageStart HomeFragment");
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
        lunboadapter = new LunBoAdapter(getActivity(), itemCartoonDetailBeans);
        viewpager_ad.setAdapter(lunboadapter);
        viewpager_ad.setOnPageChangeListener(new GuidePageChangeListener());

        if (itemCartoonDetailBeans != null && itemCartoonDetailBeans.size() > 0) {
            // 总的点数
            focusView.setCount(itemCartoonDetailBeans.size());
            int gbs = 1000 / itemCartoonDetailBeans.size();
            int totalMax = gbs * itemCartoonDetailBeans.size();
            viewpager_ad.setCurrentItem(totalMax); // 初始位置在靠近1000的整个整除的数字

            // 当前位置
            int currentIndex = viewpager_ad.getCurrentItem() % itemCartoonDetailBeans.size();
            focusView.setCurrentIndex(currentIndex);
            // 描述文字
            focusView.setTitle(itemCartoonDetailBeans.get(currentIndex).desc.trim());
//          focusView.getBackground().setAlpha(100);
            startLunbo();
        }
    }

    /**
     * 广告滑动监听
     */
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            enableDisableSwipeRefresh(arg0 == ViewPager.SCROLL_STATE_IDLE);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(final int arg0) {
            int currentIndex = arg0 % itemCartoonDetailBeans.size();
            focusView.setCurrentIndex(currentIndex);
            // 描述文字
            focusView.setTitle(itemCartoonDetailBeans.get(currentIndex).desc.trim());
        }
    }

    /**
     * viewpager左右滑动时不触发下拉刷新
     *
     * @param enable
     */
    protected void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }

    /**
     * 获取item
     */
    private void getItem() {
        Debug.Log("Item", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("column", title);
        client.get("detail/info?fr=channel&ie=utf-8&oe=utf-8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] responseBody, Throwable throwable) {
                        if (swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);

                        progressBar.setVisibility(View.GONE);
                        //取出本地数据
                        if (itemCategoryBeans.isEmpty()) {
                            itemCategoryBeans = itemCategoryBeanService.findListByTitle(title);
                            handler.sendMessage(handler.obtainMessage(2, itemCategoryBeans));
                        }
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        Debug.Log("server response:", response);
                        try {
                            progressBar.setVisibility(View.GONE);
                            if (itemCategoryBeans != null && itemCategoryBeans.size() > 0) {
                                if (isRefresh) {
                                    isRefresh = false;
                                    itemCategoryBeans.clear();
                                }
                            }

                            JSONObject jsonObject = new JSONObject(response.trim());
                            JSONObject data = (JSONObject) jsonObject.get("data");
                            JSONArray thumbs = data.getJSONArray("thumbs");
                            UpdateItem(thumbs);
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * UpdateItem
     *
     * @param thumbs
     */
    private void UpdateItem(JSONArray thumbs) {

        //将itemObjects数据保存到本地数据库ItemCategoryBean,没有互联网时就直接取本地数据
        itemCategoryBeanService.deleteByTitle(title);

        ItemCategoryBean itemCategoryBean1 = new ItemCategoryBean();
        itemCategoryBean1.icon = "https://scontent-hkg3-1.xx.fbcdn.net/hphotos-xpa1/v/t1.0-9/12243545_1731919540364254_5872207366297951127_n.jpg?oh=dd43d2b82c6ddcb07fc8d1d4d433ec2b&oe=56E2F432";
        itemCategoryBean1.title = title;
        itemCategoryBean1.tag = "全部";
        itemCategoryBeans.add(itemCategoryBean1);

        if (title.equals("明星")) {
            ItemCategoryBean itemCategoryBean2 = new ItemCategoryBean();
            itemCategoryBean2.icon = "http://cdn.duitang.com/uploads/item/201510/22/20151022223433_xAYeu.jpeg";
            itemCategoryBean2.title = title;
            itemCategoryBean2.tag = "全部";
            itemCategoryBean2.ftags = "男明星";
            itemCategoryBeans.add(itemCategoryBean2);

            ItemCategoryBean itemCategoryBean3 = new ItemCategoryBean();
            itemCategoryBean3.icon = "http://p3.qhimg.com/t01d4016bb09560f198.jpg";
            itemCategoryBean3.title = title;
            itemCategoryBean3.tag = "全部";
            itemCategoryBean3.ftags = "女明星";
            itemCategoryBeans.add(itemCategoryBean3);
        }

        for (int i = 0; i < thumbs.length(); i++) {
            try {
                ItemCategoryBean itemCategoryBean = new ItemCategoryBean();
                JSONObject d = (JSONObject) thumbs.get(i);
                itemCategoryBean.icon = d.getString("thumb_url");
                itemCategoryBean.title = d.getString("column");
                itemCategoryBean.tag = convertTagName(d.getString("tag"));
                itemCategoryBeanService.save(itemCategoryBean);
                itemCategoryBeans.add(itemCategoryBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.sendMessage(handler.obtainMessage(2, itemCategoryBeans));

    }

    /**
     * 获取广告
     */
    private void getBanner() {
        Debug.Log("banner", "get");
        HttpClientHelper client = new HttpClientHelper();
        RequestParams params = new RequestParams();
        params.put("pn", getRandom());
        params.put("rn", 10);
        params.put("tag1", title);
        client.get("channel/listjson?tag2=全部&ie=utf8", params,
                new JSONHttpHelper.JSONHttpResponseHandler() {

                    @Override
                    public void Success() {
                        // TODO Auto-generated method stub
                        try {
                            Debug.Log("banner", "success");
                            if (itemCartoonDetailBeans != null && !itemCartoonDetailBeans.isEmpty()) {
                                itemCartoonDetailBeans.clear();
                            }
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
                        if (isAdded()) {
                            ToastShow.displayToast(getActivity(), getActivity().getResources().getString(R.string.check_net));
                        }
                        Debug.Log("banner", "获取本地数据库");
                        //取出本地数据
                        if (itemCartoonDetailBeans.isEmpty()) {
                            itemCartoonDetailBeans = itemCartoonDetailBeanService.findListByColumTag(title, "全部");
                            handler.sendMessage(handler.obtainMessage(1));
                        }
                    }

                });
    }

    /**
     * UpdateAD
     *
     * @param datas
     */
    private void UpdateAD(JSONArray datas) {

        itemCartoonDetailBeanService.deleteByColumTag(title, "全部");

        for (int i = 0; i < datas.length(); i++) {
            try {
                ItemCartoonDetailBean itemCartoonDetailBean = new ItemCartoonDetailBean();
                JSONObject d = (JSONObject) datas.get(i);
                itemCartoonDetailBean.desc = d.getString("desc");
                itemCartoonDetailBean.colum = d.getString("colum");
                itemCartoonDetailBean.tag = d.getString("tag");
                itemCartoonDetailBean.ftags = convertTagName(d.getString("tags"));
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

        handler.sendMessage(handler.obtainMessage(1));

    }

    /**
     * 获取随机数
     *
     * @return
     */
    private int getRandom() {
        Random random = new Random();
        return random.nextInt(1000);
    }

    /**
     * handler处理事件
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Debug.Log("handler message 1", "success");
                    frame_ad.setVisibility(View.VISIBLE);
                    if (itemCartoonDetailBeans != null && itemCartoonDetailBeans.size() > 0) {
                        initLunBoView();
                    }
                    break;
                case 2:
                    Debug.Log("handler message 2", "success");
                    swipeRefreshLayout.setRefreshing(false);
                    List<ItemCategoryBean> list = (List<ItemCategoryBean>) msg.obj;
                    itemAdapter.updateAdapter(list);
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
