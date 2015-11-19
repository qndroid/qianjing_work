package com.qianjing.finance.receiver;

import cn.jpush.android.api.JPushInterface;

import com.qianjing.finance.service.PushService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * <p>Title: PushReceiver</p>
 * <p>Description: 接收推送通知的接收器</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年3月11日
 */
public class PushReceiver extends BroadcastReceiver {
	
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		
        Bundle bundle = intent.getExtras();
		
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        	
            Log.d(TAG, "[PushReceiver] 接收到推送的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[PushReceiver] 接收到推送的通知的ID: " + notifactionId);
        } 
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[PushReceiver] 用户点击打开了通知");
            
        	Intent i = new Intent(context, PushService.class);
        	i.putExtras(bundle);
        	context.startService(i);
        }
	}
	
}
