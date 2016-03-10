package com.jason.hao;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jason.global.CommonData;
import com.jason.photoview.PhotoView;
import com.jason.photoview.PhotoViewAttacher;
import com.jason.swipeback.SwipeBackActivity;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 显示webview点击的图片
 * <p/>
 * Created by shenghao on 2015/5/22.
 */
public class ImageActivity extends SwipeBackActivity {

    private PhotoView imageView;
    private ImageLoader imageLoader;
    private String img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById();
        initView();
    }

    protected void findViewById() {
        imageView = new PhotoView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);

        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }

    protected void initView() {
        imageLoader = ImageLoader.getInstance();
        img_url = getIntent().getExtras().getString(CommonData.PHOTO);
        if (!img_url.isEmpty()) {
            imageLoader.displayImage(img_url, imageView, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        } else {
            imageLoader.displayImage("drawable://" + R.drawable.huoying12, imageView, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        }
    }
}
