package com.qianjing.finance.util;

import com.qianjing.finance.ui.Const;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;


/**
 * <p>Title:工具类</p>
 * <p>Description: 负责处理推送跳转,需要handleReceiver</p>
 * <p>Company: www.qianjing.com</p> 
 * @author liuchen
 * @date 2015年4月7日
 */

public class JumpActivityTools {

	private Intent intent;
	private Context context;
	
	public JumpActivityTools(Context context,Intent intent) {
		this.intent = intent;
		this.context = context;
	}
	
	public void jumpToADS(){
		intent.setAction(Const.ACTION_LAUNCH_ADS);
		context.sendBroadcast(intent);
	}
	
	public void jumpToLOCK(){
		intent.setAction(Const.ACTION_LAUNCH_LOCK);
		context.sendBroadcast(intent);
	}
	
	public void jumpToLOGIN(){
		intent.setAction(Const.ACTION_LAUNCH_LOGIN);
		context.sendBroadcast(intent);
	}
	
	public void jumpToGUIDE(){
		intent.setAction(Const.ACTION_LAUNCH_GUIDE);
		context.sendBroadcast(intent);
	}
	
	public void jumpToBindCARD(){
		intent.setAction(Const.ACTION_LAUNCH_BIND_CARD);
		context.sendBroadcast(intent);
	}
	
	public void jumpToWHAT(String what){
		intent.setAction(what);
		context.sendBroadcast(intent);
	}
	
	public void jumpToWHAT(Class<?> clazz){
		Intent intent = new Intent(context,clazz);
		context.startActivity(intent);
	}

	
}
