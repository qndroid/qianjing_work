package com.qianjing.finance.widget;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


/**
 * <p>Title: CustomToast</p>
 * <p>Description: 通用Toast，避免重复显示</p>
 * @author fangyan
 * @date 2015年3月3日
 */
public class CustomToast {

	private static Toast mToast;
	
	private static Handler mHandler = new Handler();
	
	private static Runnable mRunnable = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};
	
	public static void showToast(Context mContext, String text, int duration) {
	
		mHandler.removeCallbacks(mRunnable);
		
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		
		mHandler.postDelayed(mRunnable, duration);
		
		mToast.show();
	}
	
	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}
}
