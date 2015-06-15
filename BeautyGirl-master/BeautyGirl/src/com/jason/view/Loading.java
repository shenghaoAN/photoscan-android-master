package com.jason.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

import com.jason.helper.ResourceHelper;


/**
 * 
 * @author hao
 * 
 */
public class Loading {
	public ProgressDialog pd;
	public Activity context;

	public Loading(Activity c) {
		context = c;
	}

	public void End() {
		if (pd != null)
			pd.dismiss();
	}

	public void Start(String msg, OnDismissListener DismissListener) {
		pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		pd.setCancelable(true);
		pd.setIndeterminate(true);
		pd.setMessage(ResourceHelper.getResourceString(msg, context));
		pd.setOnDismissListener(DismissListener);
        pd.setCanceledOnTouchOutside(false);
		pd.show();
	}

	public void setMessage(String msg) {
		pd.setMessage(msg);
	}

	public void Start() {
		Start("loading", new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

			}
		});
	}
}
