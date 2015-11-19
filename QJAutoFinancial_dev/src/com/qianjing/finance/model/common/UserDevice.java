package com.qianjing.finance.model.common;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.qianjing.finance.util.helper.StringHelper;
import com.umeng.analytics.AnalyticsConfig;


public class UserDevice {

	/** 机型 */
	public static String MODEL = "";

	/** 厂商 */
	public static String BRAND = "";

	/** 运营商 */
	public static String OPERATOR = "";

	/** 当前网络类型 */
	public static String NETWORK = "";

	/** SDK代号 */
	public static String SDK_LEVEL = "";

	/** SDK版本号 */
	public static String SDK_RELEASE = "";

	/** 系统版本号 */
	public static int SYSTEM_VERSION = 0;

	/** 系统版本名字 */
	public static String SYSTEM_VERSION_NAME = "1.0";

	/** 手机IMEI */
	public static String IMEI = "";

	/** 手机IMSI */
	public static String IMSI = "";

	/** 是否已登录 */
	public static boolean isHasLogin = false;

	/** sesstion超时时间 */
	public static long sessionTimeout = 30000;

	/** 用户cookie */
	public static String uCookie = "";

	/** 时间cookie */
	public static String tCookie = "";

	/** 设备cookie */
	public static String bCookie;

	/** 银行活期利率 */
	public static double RATE = 0.35;

	/** 服务端最新版本号 */
	public static String curVersion;

	/** 当前app版本 */
	public static String VERSION_NAME = "";

	/** 当前渠道号 */
	public static String CHANNEL = "";

	/**
	 * 得到当前app版本号
	 * 
	 * @param mContext
	 * @return
	 */
	public static final String getVersionName(Context mContext) {
		if (StringHelper.isBlank(VERSION_NAME)) {
			try {
				VERSION_NAME = mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 1).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return VERSION_NAME;
	}

	public static final String getIMEI(Context context) {
		if (StringHelper.isBlank(IMEI)) {
			IMEI = getIMEINub(context);
		}
		return IMEI;
	}

	// 初始化
	public static final void initInfo(Context context) {
		// 机型
		MODEL = getModel();
		// 运营商
		OPERATOR = getIMSIType(context);
		// 厂商
		BRAND = getBrand();
		// 网络类型
		NETWORK = getNetwork(context);
		// SDK代号
		SDK_LEVEL = getSDKLevel();
		// SDK版本号
		SDK_RELEASE = getSDKRelease();
		// 系统版本
		SYSTEM_VERSION = getSystemVersionCode(context);
		SYSTEM_VERSION_NAME = getSystemVersionName(context);
		CHANNEL = AnalyticsConfig.getChannel(context);

		// IMEI = Common.getIMEI(context);
		IMEI = getIMEI(context);
		IMSI = getIMSI(context);
		VERSION_NAME = getVersionName(context);

		if (StringHelper.isBlank(IMEI)) {
			IMEI = "866365000567780";// Common.getRandomNumber(15);
		}
		if (StringHelper.isBlank(IMSI)) {
			IMSI = "23230866365000567780";// Common.getRandomNumber(15);
		}
	}

	// 清空session信息
	public static void clearSession() {
		clearLoginInfo();
	}

	// 清空登录信息
	public static void clearLoginInfo() {
		isHasLogin = false;
	}

	// 获取IMEI(手机串号)
	public static String getIMEINub(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

	// 获取IMSI(SIM卡串号)
	public static String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi;
	}

	// 判断手机是否获取了ROOT权限
	public static final boolean isRoot() {
		try {
			File file = new File("/system/app/Superuser.apk");
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	// 是否有SDCARD
	public static final boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// 获取SDK级别代号
	public static String getSDKLevel() {
		String sdkLevel = android.os.Build.VERSION.SDK;
		return sdkLevel;
	}

	// 获取SDK级别代号
	public static String getSDKRelease() {
		String sdkRelease = android.os.Build.VERSION.RELEASE;
		return sdkRelease;
	}

	// 获取SIM卡运行商
	public static String getIMSIType(Context context) {
		String providersName = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String IMSI = telephonyManager.getSubscriberId();
			if (IMSI != null) {
				// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
				if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
					providersName = "中国移动";
				} else if (IMSI.startsWith("46001")) {
					providersName = "中国联通";
				} else if (IMSI.startsWith("46003")) {
					providersName = "中国电信";
				} else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
					providersName = telephonyManager.getSimOperatorName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providersName;
	}

	/**
	 * 检测是否有网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 获取当前网络类型
	public static String getNetwork(Context context) {
		String network = "未连接任何网络";
		try {
			ConnectivityManager connectionManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取网络的状态信息，有下面三种方式
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable()) {
				network = networkInfo.getTypeName()
						+ (networkInfo.getExtraInfo() == null ? "" : " - "
								+ networkInfo.getExtraInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return network;
	}

	// 获取生产厂商
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	// 获取手机型号
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	// 获取系统版本号
	public static int getSystemVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getApplicationInfo().packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	// 获取系统版本号显示
	public static String getSystemVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getApplicationInfo().packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取从系统通讯录返回后选择的联系人号码
	 * 
	 * @param context
	 *            上下文
	 * @param requestCode
	 *            请求时编号
	 * @param data
	 *            返回的数据
	 * @return 号码
	 */
	public static String getNumberFromContactResult(Context context,
			int requestCode, Intent data) {
		String number = "";
		Cursor cursor = null;
		try {
			if (data != null) {
				cursor = context.getContentResolver().query(data.getData(),
						null, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					String columnName = Build.VERSION.SDK_INT < 7 ? "number"
							: "data1";
					number = cursor.getString(cursor
							.getColumnIndexOrThrow(columnName));
					// 去空格和特殊字符
					number = number.trim();
					number = number.contains(" ") ? number.replaceAll(" ", "")
							: number;
					number = number.contains("+86") ? number.replace("+86", "")
							: number;
					number = number.contains("-") ? number.replace("-", "")
							: number;
					number = number.contains("#") ? number.replace("#", "")
							: number;
					number = number.contains("*") ? number.replace("*", "")
							: number;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}

}
