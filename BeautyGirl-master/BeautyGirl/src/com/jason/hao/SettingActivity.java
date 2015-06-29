package com.jason.hao;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.swipeback.SwipeBackActivity;
import com.umeng.fb.FeedbackAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SettingActivity extends SwipeBackActivity {

    private View topbar;
    private ImageView img_back;
    private TextView txt_title;

    private LinearLayout Layout_View;

    private List<SettingClass> list;

    private static final String TAG_FAVROITE = "favroite";
    private static final String TAG_SEARCH = "search";
    private static final String TAG_FEEDBACK = "feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        setDatas();
        updateUI();
    }

    private void initView() {

        topbar = (View) findViewById(R.id.topbar);
        img_back = (ImageView) topbar.findViewById(R.id.img_back);
        txt_title = (TextView) topbar.findViewById(R.id.txt_title);
        txt_title.setText(getString(R.string.setting_manager));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Layout_View = (LinearLayout) findViewById(R.id.Layout_View);
    }

    private void setDatas() {
        list = new ArrayList<SettingClass>();
        list.add(setSettingClass(R.string.setting_favroite, TAG_FAVROITE));
        list.add(setSettingClass(R.string.setting_search, TAG_SEARCH));
        list.add(setSettingClass(R.string.setting_feedback, TAG_FEEDBACK));
    }

    private SettingClass setSettingClass(Integer id, String tag) {
        SettingClass settingClass = new SettingClass();
        settingClass.Text = getString(id);
        settingClass.Tag = tag;
        return settingClass;
    }

    private void updateUI() {
        Layout_View.removeAllViews();
        if (!list.isEmpty()) {
            for (SettingClass settingClass : list) {
                updateRow(settingClass);
            }
        }
    }

    private void updateRow(SettingClass settingClass) {
        View view = LayoutInflater.from(this).inflate(
                R.layout.setting_row, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(settingClass.Text);
        view.setTag(settingClass);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingClass settingClass1 = (SettingClass) v.getTag();
                setRowClick(settingClass1);
            }
        });
        Layout_View.addView(view);
    }

    private void setRowClick(SettingClass settingClass) {
        Intent intent = null;
        if (settingClass.Tag.equals(TAG_FAVROITE)) {
            intent = new Intent(SettingActivity.this, FavroiteActivity.class);
            startActivity(intent);
        } else if (settingClass.Tag.equals(TAG_SEARCH)) {
            intent = new Intent(SettingActivity.this, SearchRecordActivity.class);
            startActivity(intent);
        } else if (settingClass.Tag.equals(TAG_FEEDBACK)) {
            FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
            agent.startFeedbackActivity();
        }
    }

    class SettingClass {
        private String Text = "";
        private String Tag = "";
    }
}
