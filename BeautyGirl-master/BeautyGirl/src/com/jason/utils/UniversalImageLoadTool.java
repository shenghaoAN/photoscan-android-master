package com.jason.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.jason.hao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UniversalImageLoadTool {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static boolean checkImageLoader() {
        return imageLoader.isInited();
    }

    private static DisplayImageOptions DEFAULT_PIC_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.btn_upload_image)
            .showImageForEmptyUri(R.drawable.btn_upload_image)
            .showImageOnFail(R.drawable.btn_upload_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public static void disPlay(String uri, ImageAware imageAware, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageOnFail(default_pic)
                .showImageForEmptyUri(default_pic)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY) //设置图片的缩放方式
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader.displayImage(uri, imageAware, options);
    }


    /**
     * 获取option
     *
     * @param default_pic 加载图片前显示的默认图片
     * @return
     */
    public static DisplayImageOptions getImageOption(int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageOnFail(default_pic)
                .showImageForEmptyUri(default_pic)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY) //设置图片的缩放方式
                .displayer(new FadeInBitmapDisplayer(100))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

    public static void loadimgAnimate(String url, ImageView imgview) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(url, imgview, DEFAULT_PIC_OPTIONS,
                new AnimateFirstDisplayListener());
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**
     * 清理图片缓存
     */
    public static void clear() {
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
    }

    public static void resume() {
        imageLoader.resume();
    }

    /**
     * 暂停加载
     */
    public static void pause() {
        imageLoader.pause();
    }

    /**
     * 停止加载
     */
    public static void stop() {
        imageLoader.stop();
    }

    /**
     * 销毁加载
     */
    public static void destroy() {
        imageLoader.destroy();
    }
}
