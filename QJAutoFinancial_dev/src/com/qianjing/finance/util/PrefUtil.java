
package com.qianjing.finance.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.qianjing.finance.database.PrefManager;

/**
 * @description 配置文件工具类
 * @author fangyan
 * @date 2015年1月15日
 */
public class PrefUtil {

	private static final String PREF_FILE_STORE = "store";
	private static final String PREF_FILE_DEFULT = "defult";
	private static final String PREF_FILE_INFORM = "QJfinance.pre";
	private static final String LOCK_KEY = "qianjing";

	private static final String KEY_BOOL_FIRST_ENTER = "isFirstEnter";
	private static final String KEY_BOOL_EFFECTIVE_LOGIN = "effective_login";
	private static final String KEY_STR_PHONE_NUMBER = "phone_number";
	private static final String KEY_STR_STORE = "store";
	private static final String KEY_STR_UCOOKIE = "uc";
	private static final String KEY_STR_TCOOKIE = "tc";

	public static void setPhoneNumber(Context context, String strPN) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(KEY_STR_PHONE_NUMBER, strPN);
		editor.commit();
	}

	public static String getPhoneNumber(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		return preferences.getString(KEY_STR_PHONE_NUMBER, null);
	}

	public static void setFirstEnter(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(KEY_BOOL_FIRST_ENTER, false);
		editor.commit();
	}

	public static boolean getFirstEnter(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		return preferences.getBoolean(KEY_BOOL_FIRST_ENTER, true);
	}

	public static void setEffectiveLogin(Context context) {

		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(KEY_BOOL_EFFECTIVE_LOGIN, true);
		editor.commit();
	}

	public static boolean getEffectiveLogin(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_DEFULT, Context.MODE_PRIVATE);
		return preferences.getBoolean(KEY_BOOL_EFFECTIVE_LOGIN, false);
	}

	public static void saveObject(Context context, String strObject) {
		if (strObject == null || "".equals(strObject))
			return;
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(KEY_STR_STORE, strObject);
		edit.commit();
	}

	public static String getObject(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		return sp.getString(KEY_STR_STORE, null);
	}

	public static boolean hasCookies(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		if (sp.getString(KEY_STR_UCOOKIE, null) != null && sp.getString(KEY_STR_TCOOKIE, null) != null)
			return true;
		return false;
	}

	public static boolean setTCookie(Context context, String strTCookie) {
		if (strTCookie == null || TextUtils.equals("", strTCookie))
			return false;
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(KEY_STR_TCOOKIE, strTCookie);
		edit.commit();
		return true;
	}

	public static String getTCookie(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		return sp.getString(KEY_STR_TCOOKIE, null);
	}

	public static boolean setUCookie(Context context, String strUCookie) {
		if (strUCookie == null || TextUtils.equals("", strUCookie))
			return false;
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(KEY_STR_UCOOKIE, strUCookie);
		edit.commit();
		return true;
	}

	public static String getUCookie(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_STORE, Context.MODE_PRIVATE);
		return sp.getString(KEY_STR_UCOOKIE, null);
	}

	public static void saveKey(Context context, String strNM) {

		String key = PrefUtil.getPhoneNumber(context);
		if (key == null)
			return;
		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, strNM);
		editor.commit();
	}

	public static String getKey(Context context) {
		String key = PrefUtil.getPhoneNumber(context);
		if (key == null)
			return null;
		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		return preferences.getString(key, null);
	}

	public static void setMain(Context context, Boolean b) {

		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("ismain", b);
		editor.commit();
	}

	public static boolean isMain(Context context) {

		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		return preferences.getBoolean("ismain", false);
	}

	public static void setGuide(Context context, Boolean b) {

		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("isguide", b);
		editor.commit();
	}

	/** 
	* @description 移除未读通知相关键值 
	* @author fangyan 
	* @param context
	*/
	public static void removeAllInformFlag(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE_INFORM, Context.MODE_PRIVATE);
		Map<String, ?> map = sp.getAll();
		if (map.isEmpty())
			return;
		Editor editor = sp.edit();
		for (String strKey : map.keySet()) {
			if (strKey.contains("inform_"))
				editor.remove(strKey);
		}
		editor.remove(PrefManager.KEY_INIT_INFORM);
		editor.remove(PrefManager.KEY_UNREAD_INFORM);
		editor.commit();
	}

	public static boolean isGuide(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		return preferences.getBoolean("isguide", true);
	}

	public static void setIsFirstToMain(Context context, Boolean b) {
		String key = PrefUtil.getPhoneNumber(context);
		if (key == null) {
			return;
		}
		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key + LOCK_KEY, b);
		editor.commit();
	}

	public static boolean isFirstToMain(Context context) {
		String key = PrefUtil.getPhoneNumber(context);
		if (key == null) {
			return false;
		}
		SharedPreferences preferences = context.getSharedPreferences("lock_key", Context.MODE_PRIVATE);
		return preferences.getBoolean(key + LOCK_KEY, true);
	}

}
