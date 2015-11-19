package com.qianjing.finance.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.GuideActivity;
import com.qianjing.finance.ui.activity.wallet.WalletActivity;
import com.qianjing.finance.util.JumpActivityDelay;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.helper.StringHelper;


/**
 * <p>Title: PushService</p>
 * <p>Description: 推送服务。接收通知后处理相关跳转。</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年3月16日
 */
public class PushService extends Service {

	private static final String TAG = "PushService";
	
	private QApplication mApplication;

	

	private JumpActivityDelay jumpActivityDelay;

	private JumpActivityDelay jumpActivityDelay2;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mApplication = (QApplication) getApplication();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null) {
	        Bundle bundle = intent.getExtras();
//	        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			
			handleNotifyExtra(strExtra);
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	/*
	 * 推送通知跳转逻辑
	 * 
          -- 活动通知
                    -- 未绑定 -> 跳转到引导页，然后到web页
                    -- 已绑定
                              --  未登录
                                        --  lock     -> 解锁后跳转到主界面，然后到web页
                                        --  login    -> 登录后跳转到主界面，然后到web页
                              --  已登录
                                        -> 直接启动web页

          --  绑卡消息（不需要考虑是否已绑定）
                    -- 未登录
                              -- lock      -> 解锁后跳转到主界面，然后到银行卡列表页
                              -- login     -> 登录后跳转到主界面，然后到银行卡列表页
                    -- 已登录 -> 直接跳转到银行卡列表页
	 */
	
	private void handleNotifyExtra(String strExtra) {
		
		/* {
			 "contentUrl":"http://www.qianjing.com/other/read/20150317.html",
			 "messageType":"2",
			 "contentId":"3211"
		   } */
	
		// 如果messageType是2，则没有contentId
		// 1-系统通知 -- 消息详情页
		// 2-活动通知 -- 浏览器页
		// 3-系统消息 -- 交易详情页
		// 4-再平衡     -- 组合详情页
		// 5-绑卡消息 -- 银行卡列表页
		// 8-红包返现消息

		try {
			LogUtils.syso("推送:"+strExtra);
			JSONObject object = new JSONObject(strExtra);
			
			String messageType = object.optString("messageType");
			String strContentUrl = object.optString("contentUrl");
			String contentId = object.optString("contentId");
			
			Intent intent = new Intent();
			intent.putExtra(Const.KEY_NOTIFICATION_CONTENT_URL, strContentUrl);
			jumpActivityDelay = new JumpActivityDelay(mApplication, intent);
			
			//假如message字段没有加
			if(messageType==null || "".equals(messageType)){
				return ;
			}
			
			int massageType = Integer.valueOf(messageType);
			
			// 系统通知或活动通知
			if (massageType==Const.NOTIFICATION_EVENT) {
				
				
				// 已绑定(用户安装应用后已登录过)
				if (PrefUtil.getEffectiveLogin(this)) {
					
					// 已登录：直接跳转到Web页
					if (mApplication.isLogined()) {
						jumpActivityDelay.getJumpActivity().jumpToADS();
					}
					// 未登录
					else {
						//已设置手势密码：解锁后跳转到主界面，再启动web页
						if (!StringHelper.isBlank(PrefUtil.getKey(mApplication))) {
							System.out.println("已设置手势密码");
							// 传递ContentUrl出现问题  ???
							jumpActivityDelay.jumpViaLock(Const.ACTION_LAUNCH_ADS, true,1000);
						}
						// 没有设置手势密码：启动登录页，再启动web页
						else {
							jumpActivityDelay.jumpViaLogin(Const.ACTION_LAUNCH_ADS);
						}
					}
				}
				// 未绑定(用户安装应用后未登录过)：启动引导页，再启动web页
				else {
					
					//假如已经应用程序没有运行过引导，直接跳转到广告详情页
					if(PrefUtil.getFirstEnter(mApplication)){
						jumpActivityDelay.jumpViaGuide(Const.ACTION_LAUNCH_ADS);
					}else{
						//假如应用程序运行过引导界面
						jumpActivityDelay.getJumpActivity().jumpToADS();
					}
				}
				return;
			}
			
			// 系统消息、再平衡消息或绑卡消息
//			if (massageType==Const.MASSAGE_SYSTEM || massageType==Const.MASSAGE_REBALANCE || massageType==Const.MASSAGE_BOUND_CARD) {
//				
//			}
			
			if(massageType==Const.MASSAGE_BOUND_CARD){
				
				// 已绑定(用户安装应用后已登录过)
				if (PrefUtil.getEffectiveLogin(this)) {
					
					// 已登录：直接跳转到银行卡绑定页
					if (mApplication.isLogined()) {
						jumpActivityDelay.getJumpActivity().jumpToBindCARD();
					}
					// 未登录
					else {
						// 已设置手势密码：解锁后跳转到主界面，再启动银行卡绑定页
						if (!StringHelper.isBlank(PrefUtil.getKey(mApplication))) {
							System.out.println("已设置手势密码");
							// 传递ContentUrl出现问题  ???
							jumpActivityDelay.jumpViaLock(Const.ACTION_LAUNCH_BIND_CARD, true, 1000);
						}
						// 没有设置手势密码：启动登录页，再启动银行卡绑定页
						else {
							jumpActivityDelay.jumpViaLogin(Const.ACTION_LAUNCH_BIND_CARD);
						}
					}
				}
			}
			
			Intent intent2 = new Intent();
			intent2.putExtra("contentid", contentId);
			intent2.putExtra("msg_type", messageType);
			jumpActivityDelay2 = new JumpActivityDelay(mApplication, intent2);
			
			if(massageType==Const.NOTIFICATION_SYSTEM){
				
				if(contentId!=null){
					LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.qjautofinancial.DISMISSMESSAGE"));
				}
				
				// 已绑定(用户安装应用后已登录过)
				if (PrefUtil.getEffectiveLogin(this)) {
					
					// 已登录：直接跳转到Web页
					if (mApplication.isLogined()) {
						jumpActivityDelay2.getJumpActivity().jumpToWHAT(Const.ACTION_LAUNCH_MESSAGE);
					}
					// 未登录
					else {
						//已设置手势密码：解锁后跳转到主界面，再启动web页
						if (!StringHelper.isBlank(PrefUtil.getKey(mApplication))) {
							System.out.println("已设置手势密码");
							// 传递ContentUrl出现问题  ???
							jumpActivityDelay2.jumpViaLock(Const.ACTION_LAUNCH_MESSAGE, true,1000);
						}
						// 没有设置手势密码：启动登录页，再启动web页
						else {
							jumpActivityDelay2.jumpViaLogin(Const.ACTION_LAUNCH_MESSAGE);
						}
					}
				}
				// 未绑定(用户安装应用后未登录过)：启动引导页，再启动web页
				else {
					
					//假如已经应用程序没有运行过引导，直接跳转到广告详情页
					if(PrefUtil.getFirstEnter(mApplication)){
						jumpActivityDelay2.jumpViaGuide(Const.ACTION_LAUNCH_MESSAGE);
					}else{
						//假如应用程序运行过引导界面
						jumpActivityDelay2.getJumpActivity().jumpToWHAT(Const.ACTION_LAUNCH_MESSAGE);
					}
				}
				return;
			}
			
			/**
			 * 红包消息
			 * 
			 * */
			Intent intent3 = new Intent();
//			intent3.putExtra("msg_type", messageType);
			JumpActivityDelay jumpActivityDelay3 = new JumpActivityDelay(mApplication, intent3);
			
			if(massageType==Const.MASSAGE_RED_PAPER){
				
				// 已绑定(用户安装应用后已登录过)
				if (PrefUtil.getEffectiveLogin(this)) {
					
					// 已登录：直接跳转到Web页
					if (mApplication.isLogined()) {
						jumpActivityDelay3.getJumpActivity().jumpToWHAT(Const.ACTION_LAUNCH_WHAT);
					}
					// 未登录
					else {
						//已设置手势密码：解锁后跳转到主界面，再启动web页
						if (!StringHelper.isBlank(PrefUtil.getKey(mApplication))) {
//							System.out.println("已设置手势密码");
							// 传递ContentUrl出现问题  ???
							jumpActivityDelay3.jumpViaLock(Const.ACTION_LAUNCH_WHAT, true,1000);
						}
						// 没有设置手势密码：启动登录页，再启动web页
						else {
							jumpActivityDelay3.jumpViaLogin(Const.ACTION_LAUNCH_WHAT);
						}
					}
				}
				// 未绑定(用户安装应用后未登录过)：启动引导页，再启动web页
				else {
					
					//假如已经应用程序没有运行过引导，直接跳转到广告详情页
					if(PrefUtil.getFirstEnter(mApplication)){
						jumpActivityDelay3.jumpViaGuide(Const.ACTION_LAUNCH_WHAT);
					}else{
						//假如应用程序运行过引导界面
						jumpActivityDelay3.getJumpActivity().jumpToWHAT(Const.ACTION_LAUNCH_WHAT);
					}
				}
				return;
			}
		} 
		catch (JSONException e) {
			// 不处理
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i(TAG, "onDestroy()");
	}
}
