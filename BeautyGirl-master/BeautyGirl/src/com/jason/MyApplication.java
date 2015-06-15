package com.jason;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
	
	private static Context context;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Debug.Log("app start", "");
		context = getApplicationContext();
		Cfg.setContext(context);
	}

	public static MyApplication getApplication() {
		return (MyApplication) context.getApplicationContext();
	}
	
}
