package com.jason;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

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
        MobclickAgent.openActivityDurationTrack(false);
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
                        .memoryCacheSize(2 * 1024 * 1024)   //内存缓存大小
                        .tasksProcessingOrder(QueueProcessingType.LIFO)   //default
                        .writeDebugLogs() // 打印debug log
                        .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取DeviceInfo
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
