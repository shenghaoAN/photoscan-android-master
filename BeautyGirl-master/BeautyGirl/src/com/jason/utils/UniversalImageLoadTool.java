package com.jason.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class UniversalImageLoadTool {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static boolean checkImageLoader() {
        return imageLoader.isInited();
    }

    public static void disPlay(String uri, ImageAware imageAware, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageOnFail(default_pic)
                .showImageForEmptyUri(default_pic)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY) //设置图片的缩放方式
                .displayer(new FadeInBitmapDisplayer(100))  //图片加载后渐入的动画时间
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader.displayImage(uri, imageAware, options);
    }

    /**
     * 获取option
     * @param default_pic 加载图片前显示的默认图片
     * @return
     */
    public static DisplayImageOptions getImageOption(int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageOnFail(default_pic)
                .showImageForEmptyUri(default_pic)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY) //设置图片的缩放方式
                .displayer(new FadeInBitmapDisplayer(100))  //图片加载后渐入的动画时间
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
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
