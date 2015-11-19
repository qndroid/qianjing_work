package com.qianjing.finance.receiver;

import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.card.CardManagerActivity;
import com.qianjing.finance.ui.activity.common.AdsContentsActivity;
import com.qianjing.finance.ui.activity.common.GestureLockActivity;
import com.qianjing.finance.ui.activity.common.GuideActivity;
import com.qianjing.finance.ui.activity.common.InformDetailActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.wallet.WalletActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


/**
 * <p>Title: HandleReceiver</p>
 * <p>Description: 处理接收到通知后的跳转</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年3月19日
 */
public class HandleReceiver extends BroadcastReceiver {

	public static final String TAG = "HandleReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {

    	if (intent.hasExtra(Const.KEY_NOTIFICATION_CONTENT_URL)) {
    		Log.i(TAG, "has Extra");
    	}else {
    		Log.i(TAG, "no Extra");
    	}
    	

		if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_ADS))
		{
	    	if (intent.hasExtra(Const.KEY_NOTIFICATION_CONTENT_URL)) {
				intent.setClass(context, AdsContentsActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
	    	}
		}
		else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_LOGIN))
		{
			intent.setClass(context, LoginActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_LOCK))
		{
			intent.setClass(context, GestureLockActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_GUIDE))
		{
			intent.setClass(context, GuideActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_BIND_CARD))
		{
			intent.setClass(context, CardManagerActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_MESSAGE))
		{
			intent.setClass(context, InformDetailActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}else if (TextUtils.equals(intent.getAction(), Const.ACTION_LAUNCH_WHAT))
		{
			intent.setClass(context, WalletActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}
}
