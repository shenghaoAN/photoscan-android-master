package com.jason;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jason.global.CommonData;
import com.jason.hao.PhotoSearchActivity;
import com.jason.hao.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;


public class MyApplication extends Application {

    private static Context context;

    private PushAgent mPushAgent;

    public static final String CALLBACK_RECEIVER_ACTION = "callback_receiver_action";

    public static IUmengRegisterCallback mRegisterCallback;

    public static IUmengUnregisterCallback mUnregisterCallback;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Debug.Log("app start", "");
        context = getApplicationContext();
        Cfg.setContext(context);
        initImageLoader(getApplicationContext());
        MobclickAgent.openActivityDurationTrack(false);
        UmengPush();
//        getDeviceInfo(context);

    }

    /**
     * getApplication
     *
     * @return
     */
    public static MyApplication getApplication() {
        return (MyApplication) context.getApplicationContext();
    }

    /**
     * 友盟推送处理
     */
    private void UmengPush() {
        mPushAgent = PushAgent.getInstance(this);
        if (Cfg.mode.equals("sandbox")) {
            mPushAgent.setDebugMode(true);  //测试时才设置调试模式
        }

        /**
         * 该Handler是在IntentService中被调用，故
         * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
         * 	      或者可以直接启动Service
         * */
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        System.out.println("推送的msg=" + msg.toString());
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, PhotoSearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.WORD, msg.custom);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                startActivity(intent);
                Debug.Log("自定义行为", msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        mRegisterCallback = new IUmengRegisterCallback() {

            @Override
            public void onRegistered(String registrationId) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(CALLBACK_RECEIVER_ACTION);
                sendBroadcast(intent);
            }

        };
        mPushAgent.setRegisterCallback(mRegisterCallback);

        mUnregisterCallback = new IUmengUnregisterCallback() {

            @Override
            public void onUnregistered(String registrationId) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(CALLBACK_RECEIVER_ACTION);
                sendBroadcast(intent);
            }
        };
        mPushAgent.setUnregisterCallback(mUnregisterCallback);
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
            Debug.Log("device", json.toString());
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
