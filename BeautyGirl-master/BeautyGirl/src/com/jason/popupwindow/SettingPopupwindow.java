package com.jason.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jason.hao.R;

/**
 * Created by shenghao on 2015/6/22.
 */
public class SettingPopupwindow extends BasePopupWindow implements View.OnClickListener {

    private Button btn_favroite;
    private Button btn_save;
    private Button btn_share;
    private Button btn_wrapper;

    private OnPopSettingClickListener listener;

    public SettingPopupwindow(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.popup_setting, null));
    }

    public SettingPopupwindow(Context context, int width, int height) {
        super(LayoutInflater.from(context).inflate(
                R.layout.popup_setting, null), width, height);
    }

    @Override
    public void init() {

    }

    @Override
    public void initViews() {
        btn_favroite = (Button) findViewById(R.id.btn_favroite);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_wrapper = (Button) findViewById(R.id.btn_wrapper);
    }

    @Override
    public void initEvents() {
        btn_favroite.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_wrapper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_favroite:
                if(listener != null)
                    listener.onFavroiteClick();
                break;
            case R.id.btn_save:
                if(listener != null)
                    listener.onSaveClick();
                break;
            case R.id.btn_share:
                if(listener != null)
                    listener.onShareClick();
                break;
            case R.id.btn_wrapper:
                if(listener != null)
                    listener.onWrapperClick();
                break;
            default:
                break;
        }
    }

    /**
     * 设置接口
     * @param listener
     */
    public void setOnPopSettingClickListener(OnPopSettingClickListener listener) {
        this.listener = listener;
    }

    /**
     * popupwindow 回调接口
     */
    public interface OnPopSettingClickListener {

        //收藏
        void onFavroiteClick();
        //保存到相册
        void onSaveClick();
        //分享
        void onShareClick();
        //设置壁纸
        void onWrapperClick();
    }

}
