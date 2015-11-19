package com.qianjing.finance.net.response;

import android.app.Activity;
import android.widget.Toast;

import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.dialog.LoadingDialog;


public class ViewUtil {

	/** 当前活动界面 */
	private static Activity mCurrentActivity;
	/** 进度条 */
	private static LoadingDialog loadingDialog;
	/** 加载计数 */
	private static int LoadingCount;

	public static void setCurrentActivity(Activity mCurrentActivity) {
		ViewUtil.mCurrentActivity = mCurrentActivity;
	}
	
	public static void handleResponseError(int statusCode) {
		switch(statusCode) {
		case HttpConst.STATUS_ERROR_NETWORK:
			showToast("网络不给力");
			break;
		case HttpConst.STATUS_ERROR_COMMON:
			showToast("网络通用错误");
			break;
		case HttpConst.STATUS_ERROR_PARAM:
			showToast("网络参数错误");
			break;
		}
	}
 
	/**
	 * 显示提示语
	 */
	public static void showToast(String strHint) {
		if (mCurrentActivity!=null && !StringHelper.isBlank(strHint)) {
			Toast.makeText(mCurrentActivity, strHint, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 显示加载框
	 */
	public static void showLoading() {
		if (mCurrentActivity!=null && !mCurrentActivity.isFinishing()) {
			if (loadingDialog == null)
				loadingDialog = new LoadingDialog(mCurrentActivity);
			// 设置为不可取消
//			loadingDialog.setCancelable(false);
			loadingDialog.show();
			LoadingCount ++;
		}
	}

	/**
	 * 取消加载框
	 */
	public static void dismissLoading() {
		if (loadingDialog != null) {
			if (LoadingCount == 1)
				loadingDialog.dismiss();
			if (LoadingCount > 0)
				LoadingCount --;
			if (LoadingCount == 0)
				loadingDialog = null;
		}
	}

}
