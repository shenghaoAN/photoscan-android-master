package com.jason;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Debug.Log("app start", "");
        context = getApplicationContext();
        Cfg.setContext(context);
        initImageLoader(getApplicationContext());
    }

    public static MyApplication getApplication() {
        return (MyApplication) context.getApplicationContext();
    }

    /**
     * @param context initImageLoader
     */
    public static void initImageLoader(Context context) {
//      http://site.com/image.png // from Web
//      file:///mnt/sdcard/image.png // from SD card
//      file:///mnt/sdcard/video.mp4 // from SD card (video thumbnail)
//      content://media/external/images/media/13 // from content provider
//      content://media/external/video/media/13 // from content provider (video thumbnail)
//      assets://image.png // from assets
//      drawable:// + R.drawable.img // from drawables (non-9patch images)

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件路径
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 2) //设置当前线程的优先级
                        .threadPoolSize(3)   //线程池内加载的数量
                        .denyCacheImageMultipleSizesInMemory()
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())  //文件命名
                        .diskCacheSize(200 * 1024 * 1024)   //文件緩存大小
                        .memoryCacheSize(50 * 1024 * 1024)   //内存缓存大小
                        .tasksProcessingOrder(QueueProcessingType.LIFO)   //default
                        .writeDebugLogs() // 打印debug log
                        .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
