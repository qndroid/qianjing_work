
package com.qianjing.finance.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.qianjing.finance.ui.QApplication;

/**
 * @description 存取简单数据
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class PrefManager {
	private static SharedPreferences sp = null;
	private static PrefManager spManager = null;
	private static Editor editor = null;
	/**
	 * Preference文件名
	 */
	private static final String SHARE_PREFREENCE_NAME = "QJfinance.pre";

	/**
	 * 上次基金更新时间
	 */
	public static final String LAST_UPDATE_FUND = "last_update_fund";

	/**
	 * 是否显示过红包提示,显示过则不再显示
	 */
	public static final String HAVE_SHOW_REWARD = "have_show_reward";
	/**
	 * 是否有单笔满8888活动
	 */
	public static final String HAVE_MORE_THAN_ACTIVITY = "have_more_than_activity";
	/**
	 * 活动提示语
	 */
	public static final String ACTIVITY_LOTTERY_MESSAGE = "ACTIVITY_LOTTERY_MESSAGE";
	/**
	 * 抽奖页面的URL地址；如果没有抽奖资格则为空字符串
	 */
	public static final String KEY_LOTTERY_URL = "lottery_url";
	/**
	 * 是否初始化通知列表;true为已初始化
	 */
	public static final String KEY_INIT_INFORM = "init_inform";
	/**
	 * 是否有未读通知；true为有未读通知
	 */
	public static final String KEY_UNREAD_INFORM = "unread_inform";

	/** 消息已读 */
	public static final int STATE_READED = 0;
	/** 消息未读 */
	public static final int STATE_UNREAD = 1;

	private PrefManager() {
		sp = QApplication.getInsten().getSharedPreferences(SHARE_PREFREENCE_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public static PrefManager getInstance() {
		if (spManager == null || sp == null || editor == null) {
			spManager = new PrefManager();
		}
		return spManager;
	}

	public void putInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	public void putLong(String key, Long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key, int defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public void putFloat(String key, float value) {
		editor.putFloat(key, value);
		editor.commit();
	}

	public boolean isKeyExist(String key) {
		return sp.contains(key);
	}

	public float getFloat(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public void remove(String key) {
		editor.remove(key);
		editor.commit();
	}
}
