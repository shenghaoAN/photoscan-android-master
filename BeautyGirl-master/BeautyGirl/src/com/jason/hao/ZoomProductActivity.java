package com.jason.hao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jason.animation.DepthPageTransformer;
import com.jason.bean.CartoonObject;
import com.jason.global.CommonData;
import com.jason.photoview.HackyViewPager;
import com.jason.photoview.PhotoView;
import com.jason.photoview.PhotoViewAttacher;
import com.jason.utils.ImageTools;
import com.jason.utils.ToastShow;
import com.jason.utils.UniversalImageLoadTool;
import com.jason.view.Loading;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jason
 */
public class ZoomProductActivity extends BaseActivity {

    private HackyViewPager zoom_viewpager;
    private ImagePagerAdapter adapter;
    private View relative_top;
    private Button btn_save;
    private Button btn_back;
    private View relative_bottom;
    private TextView txt_title;

    private ImageLoader imageLoader;
    private List<CartoonObject> cartoonObjects;
    private int pagerposition = 0; // 页面当前位置
    private int currentposition = 0;

    //文件保存
    private Loading loading;
    private String SavePath = Environment.getExternalStorageDirectory().getPath() + "/Cartoon";   //文件保存路径
    private String message;

    //animation
    private AlphaAnimation alpha_1;
    private AlphaAnimation alpha_2;
    private AnimationSet closeBottomLayoutAnimation;
    private AnimationSet openBottomLayoutAnimation;
    private AnimationSet closeTopLayoutAnimation;
    private AnimationSet openTopLayoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_product);
        loading = new Loading(ZoomProductActivity.this);
        imageLoader = ImageLoader.getInstance();
        Bundle bundle = getIntent().getExtras();
        ArrayList arrayList = bundle.getParcelableArrayList(CommonData.LIST);
        cartoonObjects = (List<CartoonObject>) arrayList.get(0);
        pagerposition = bundle.getInt(CommonData.POSITION);
        findViewById();
        initView();
        showProduct();
        initAnimation();
    }

    protected void findViewById() {
        relative_top = (View) findViewById(R.id.relative_top);
        relative_bottom = (View) findViewById(R.id.relative_bottom);
        zoom_viewpager = (HackyViewPager) findViewById(R.id.zoom_viewpager);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_save = (Button) findViewById(R.id.btn_save);
        txt_title = (TextView) findViewById(R.id.txt_title);
    }

    protected void initView() {
        txt_title.setText(cartoonObjects.get(pagerposition).getDesc()
                + "(" + (pagerposition + 1) + "/" + cartoonObjects.size() + ")");
        zoom_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                pagerposition = arg0;
                currentposition = arg0 + 1;
                txt_title.setText(cartoonObjects.get(pagerposition).getDesc()
                        + "(" + currentposition + "/" + cartoonObjects.size() + ")");

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ZoomProductActivity.this.finish();
            }
        });

        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                loading.Start("save_img");
                new Thread(saveFileRunnable).start();
            }
        });
    }

    /**
     * 显示图片
     */
    private void showProduct() {
        adapter = new ImagePagerAdapter();
        zoom_viewpager.setAdapter(adapter);
        zoom_viewpager.setCurrentItem(pagerposition);
        zoom_viewpager.setPageTransformer(true, new DepthPageTransformer());
    }

    /**
     * 保存图片
     */
    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                //保存图片到指定路径
                ImageTools.savePhotoToSDCard(ZoomProductActivity.this, imageLoader.loadImageSync(cartoonObjects.get(pagerposition).getImage_url()),
                        SavePath, ImageTools.getFileName(), true);
                message = String.format(getString(R.string.save_success), SavePath);
            } catch (Exception e) {
                message = getString(R.string.save_failure);
                e.printStackTrace();
            }
            handler.sendMessage(handler.obtainMessage(1));
        }

    };


    /**
     * 线程处理图片的加载
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 1) {
                loading.End();
                ToastShow.displayToast(ZoomProductActivity.this, message);
            }
        }

    };

    /**
     * 动画
     */
    private void initAnimation() {

        //淡入
        alpha_1 = new AlphaAnimation(0.0f, 1.0f);
        alpha_1.setDuration(500);

        //淡出
        alpha_2 = new AlphaAnimation(1.0f, 0.0f);
        alpha_2.setDuration(500);

        //底部动画效果

        //移出
        TranslateAnimation outBottomTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f);

        //移进
        TranslateAnimation inBottomTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0f);

        //关闭
        closeBottomLayoutAnimation = new AnimationSet(true);
        closeBottomLayoutAnimation.addAnimation(outBottomTranslateAnimation);
        closeBottomLayoutAnimation.addAnimation(alpha_2);
        closeBottomLayoutAnimation.setDuration(500);
        closeBottomLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
                relative_bottom.setVisibility(View.INVISIBLE);
                relative_bottom.clearAnimation();
            }
        });

        //打开
        openBottomLayoutAnimation = new AnimationSet(true);
        openBottomLayoutAnimation.addAnimation(inBottomTranslateAnimation);
        openBottomLayoutAnimation.addAnimation(alpha_1);
        openBottomLayoutAnimation.setDuration(500);
        openBottomLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
                relative_bottom.setVisibility(View.VISIBLE);
                relative_bottom.clearAnimation();
            }
        });

        //移出
        TranslateAnimation outTopTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);

        //移进
        TranslateAnimation inTopTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f);

        //关闭
        closeTopLayoutAnimation = new AnimationSet(true);
        closeTopLayoutAnimation.addAnimation(outTopTranslateAnimation);
        closeTopLayoutAnimation.addAnimation(alpha_2);
        closeTopLayoutAnimation.setDuration(500);
        closeTopLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
                relative_top.setVisibility(View.INVISIBLE);
                relative_top.clearAnimation();
            }
        });

        //打开
        openTopLayoutAnimation = new AnimationSet(true);
        openTopLayoutAnimation.addAnimation(inTopTranslateAnimation);
        openTopLayoutAnimation.addAnimation(alpha_1);
        openTopLayoutAnimation.setDuration(500);
        openTopLayoutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
                relative_top.setVisibility(View.VISIBLE);
                relative_top.clearAnimation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /**
     * ViewPager适配器
     */
    class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cartoonObjects.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            // 引入自定义布局作为Viewpager子视图
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.item_zoom_product, null);
            PhotoView imageView = (PhotoView) view
                    .findViewById(R.id.img_zoom_photo);
            final ProgressBar loading = (ProgressBar) view
                    .findViewById(R.id.pb_zoom_photo);
            imageLoader.displayImage(cartoonObjects.get(position).getImage_url(), imageView, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image),
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            loading.setProgress(0);
                            loading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            loading.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {

                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current,
                                                     int total) {
                            // TODO Auto-generated method stub
                            loading.setProgress(Math.round(100.0f * current / total));
                        }
                    });
            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    // TODO Auto-generated method stub
                    relative_top.clearAnimation();
                    relative_bottom.clearAnimation();

                    if (relative_top.getVisibility() == View.VISIBLE) {
                        relative_top.setAnimation(closeTopLayoutAnimation);
                        relative_bottom.setAnimation(closeBottomLayoutAnimation);
                        closeBottomLayoutAnimation.start();
                        closeTopLayoutAnimation.start();
                    } else {
                        relative_top.setAnimation(openTopLayoutAnimation);
                        relative_bottom.setAnimation(openBottomLayoutAnimation);
                        openBottomLayoutAnimation.start();
                        openTopLayoutAnimation.start();
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

}
