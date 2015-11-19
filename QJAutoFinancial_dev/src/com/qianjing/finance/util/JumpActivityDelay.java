package com.qianjing.finance.util;

import android.content.Intent;

import com.qianjing.finance.net.connection.ThreadPoolUtil;
import com.qianjing.finance.ui.QApplication;

/**
 * <p>Title:工具类</p>
 * <p>Description: 负责处理推送跳转,需要handleReceiver</p>
 * <p>Company: www.qianjing.com</p> 
 * @author liuchen
 * @date 2015年4月7日
 */


public class JumpActivityDelay {


	private QApplication mApplication;
	private JumpActivityTools jat;
	private boolean lock_main_content;
	private boolean login_content;
	private boolean guide_content;
	
	public JumpActivityDelay(QApplication mApplication,Intent intent) {
		this.mApplication = mApplication;
		jat = new JumpActivityTools(mApplication, intent);
	}

	
	/**
	 * 跳转到目标之前先跳转到锁屏界面然后跳转到主界面
	 * 默认解锁后会跳转到主界面
	 * param1:目标界面
	 * param2:是否延时
	 * param3:延时时间
	 * 
	 * */
	public void jumpViaLock(final String target,final boolean isDelayed,final int time){
		
		jat.jumpToLOCK();
		lock_main_content = true;
		
		ThreadPoolUtil.execute(new Runnable(){
			@Override
			public void run() {
				while(lock_main_content){
					
					if(PrefUtil.isMain(mApplication) && mApplication.isLogined()){
						
						if(isDelayed){
							try {
								Thread.sleep(time);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						jat.jumpToWHAT(target);
						lock_main_content = false;
						
					}
				}
			}
		});
	}
	
	//跳转到目标之前先跳转到登陆界面
	public void jumpViaLogin(final String target){
		
		jat.jumpToLOGIN();
		login_content = true;
		ThreadPoolUtil.execute(new Runnable(){
			@Override
			public void run() {
				while(login_content){
					if(mApplication.isLogined()){
						jat.jumpToWHAT(target);
						login_content = false;
					}
				}
			}
		});
	}
	
	//跳转到目标之前先跳转到引导界面
	public void jumpViaGuide(final String target){
		
		jat.jumpToGUIDE();
		guide_content = true;
		ThreadPoolUtil.execute(new Runnable(){
			@Override
			public void run() {
				while(guide_content){
					if(!PrefUtil.isGuide(mApplication)){
						jat.jumpToWHAT(target);
						PrefUtil.setGuide(mApplication, true);
						guide_content = false;
					}
				}
			}
		});
	}
	
	public JumpActivityTools getJumpActivity(){
		return jat;
	}
	
}
