
package com.qianjing.finance.ui.activity.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.widget.CustomToast;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qianjing.finance.widget.dialog.LoadingDialog;
import com.qianjing.finance.widget.dialog.LoadingHintDialog;
import com.qianjing.finance.widget.dialog.ShowDialog;
import com.qianjing.finance.widget.dialog.TwoButtonDialog;
import com.qjautofinancial.R;
import com.umeng.analytics.MobclickAgent;

/**
 * <p>Title: BaseActivity</p>
 * <p>Description: Activity基类</p>
 * @author fangyan
 * @date 2015年1月14日
 */
public class BaseActivity extends FragmentActivity {

	public static final String TAG = "BaseActivity";

	// Application实例
	public QApplication mApplication;

	// 加载框
	protected LoadingDialog loadingDialog;
	// 提示对话框
	protected ShowDialog.Builder builder;
	// 有文字提示的加载框
	protected LoadingHintDialog loadingHintDialog;
	// 双按钮对话框
	private TwoButtonDialog.Builder twoButtonDialog;
	// 输入对话框
	private InputDialog.Builder inputDialog;

	// 是否需要点击返回键提示用户是否退出应用
	private boolean isNeedExitApp = false;
	// 退出应用时间标志位
	private long mTimeStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

		initUmeng();
	}

	@Override
	public void onStart() {
		super.onStart();
		CustomConstants.IS_RUNNING_FOREGROUND = true;
	}

	@Override
	public void onStop() {
		super.onStop();

		ViewUtil.setCurrentActivity(null);

		CustomConstants.IS_RUNNING_FOREGROUND = false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		ViewUtil.setCurrentActivity(this);

		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 保存统计数据
		MobclickAgent.onKillProcess(BaseActivity.this);
	}

	@Override
	public void finish() {
		Util.hideInputView(this);
		super.finish();
	}

	protected void init() {
		mApplication = (QApplication) getApplication();
		QApplication.currentActivity = this;

		ViewUtil.setCurrentActivity(this);

		loadingDialog = new LoadingDialog(this);
		loadingHintDialog = new LoadingHintDialog(this);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		// 保存统计数据
		MobclickAgent.onKillProcess(BaseActivity.this);
	}

	protected void initUmeng() {
		// 调试模式下数据实时发送，不受发送策略控制
		// MobclickAgent.setDebugMode(true);

		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);

		MobclickAgent.updateOnlineConfig(this);
	}

	/**
	 * 显示加载框
	 */
	public void showLoading() {
		if (loadingDialog != null) {
			loadingDialog.show();
		}
	}

	/**
	 * 取消加载框
	 */
	public void dismissLoading() {
		if (loadingDialog != null)
			loadingDialog.dismiss();
	}

	/**
	 * 显示有文字提示的加载框
	 */
	protected void showHintLoading(String msg) {
		if (loadingHintDialog != null) {
			loadingHintDialog.show();
			loadingHintDialog.setHint(msg);
		}
	}

	/**
	 * 取消有文字提示的加载框
	 */
	public void dismissHintLoading() {
		if (loadingHintDialog != null)
			loadingHintDialog.dismiss();
	}

	/**
	 * 设置加载框为不可取消
	 */
	protected void setLoadingUncancelable() {
		loadingDialog.setCancelable(false);
	}

	/**
	 * 设置有文字提示的加载框为不可取消
	 */
	protected void setHintLoadingUncancelable() {
		loadingHintDialog.setCancelable(false);
	}

	/**
	 * 显示默认Title的提示框
	 * 
	 * @param strMsg
	 */
	public void showHintDialog(String strMsg) {
		showHintDialog(null, strMsg, null);
	}

	/**
	 * 显示默认Title的提示框
	 * 
	 * @param strMsg
	 * @param onClick
	 */
	protected void showHintDialog(String strMsg, DialogInterface.OnClickListener onClick) {
		showHintDialog(null, strMsg, onClick);
	}

	public void showToast(String strMsg) {
		Toast.makeText(this, strMsg, 5 * 1000).show();
	}

	/**
	 * 显示自定义Title的提示框
	 * 
	 * @param titles
	 * @param message
	 */
	protected void showHintDialog(String strTitle, String strMsg) {
		showHintDialog(strTitle, strMsg, null);
	}

	protected void showHintDialogKnow(String strTitle, String strMsg) {
		showHintDialogKnow(strTitle, strMsg, null);
	}

	protected void showHintDialogKnow(String strTitle, String strMsg, DialogInterface.OnClickListener onClick) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				builder = new ShowDialog.Builder(this);
				builder.setMessage(strMsg);
				builder.setTitle(strTitle == null ? "提示" : strTitle);
				if (onClick != null)
					builder.setPositiveButton("确定", onClick);
				else
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				builder.create().show();
			}
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
		if (strMsg != null && !TextUtils.equals(strMsg, "")) {
			builder = new ShowDialog.Builder(this);
			builder.setMessage(strMsg);
			builder.setTitle(strTitle == null ? "提示" : strTitle);
			if (onClick != null)
				builder.setPositiveButton("知道了", onClick);
			else
				builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			builder.createKnow().show();
		}
	}

	protected void showHintDialog(String strTitle, String strMsg, DialogInterface.OnClickListener onClick) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				builder = new ShowDialog.Builder(this);
				builder.setMessage(strMsg);
				builder.setTitle(strTitle == null ? "提示" : strTitle);
				if (onClick != null)
					builder.setPositiveButton("确定", onClick);
				else
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				builder.create().show();
			}
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
	}

	protected void showHintDialog(String strTitle, String strMsg, DialogInterface.OnClickListener onClick, String btnTitle) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				builder = new ShowDialog.Builder(this);
				builder.setMessage(strMsg);
				builder.setTitle(strTitle == null ? "提示" : strTitle);
				if (onClick != null)
					builder.setPositiveButton(btnTitle, onClick);
				else
					builder.setPositiveButton(btnTitle, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				builder.create().show();
			}
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
	}

	/**
	 * 双按钮对话框只添加确定点击
	 */
	protected void showTwoButtonDialog(String strMsg, DialogInterface.OnClickListener onClickPositive) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				twoButtonDialog = new TwoButtonDialog.Builder(this);
				twoButtonDialog.setTitle("提示");
				twoButtonDialog.setMessage(strMsg);
				twoButtonDialog.setPositiveButton("确定", onClickPositive);
				twoButtonDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				twoButtonDialog.create().show();
			}
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
	}

	/**
	 * 双按钮对话框添加确定和取消点击
	 */
	public void showTwoButtonDialog(String strMsg, DialogInterface.OnClickListener onClickPositive, DialogInterface.OnClickListener onClickNegative) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				twoButtonDialog = new TwoButtonDialog.Builder(this);
				twoButtonDialog.setTitle("提示");
				twoButtonDialog.setMessage(strMsg);
				twoButtonDialog.setPositiveButton("确定", onClickPositive);
				twoButtonDialog.setNagetiveButton("取消", onClickNegative);
				twoButtonDialog.create().show();
			}
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
	}

	/**
	 * 双按钮对话框添加确定和取消点击
	 */
	protected void showTwoButtonDialog(String title, String strMsg, String rightName, String leftName,
			DialogInterface.OnClickListener onClickPositive, DialogInterface.OnClickListener onClickNegative) {

		try {
			if (strMsg != null && !TextUtils.equals(strMsg, "")) {
				twoButtonDialog = new TwoButtonDialog.Builder(this);
				twoButtonDialog.setTitle(title);
				twoButtonDialog.setMessage(strMsg);
				twoButtonDialog.setPositiveButton(rightName, onClickPositive);
				twoButtonDialog.setNagetiveButton(leftName, onClickNegative);
				twoButtonDialog.create().show();
			}
		} catch (Exception e) {
		}
	}

	protected void showInputDialog(String title, DialogInterface.OnClickListener positiveClick) {
		try {
			inputDialog = new InputDialog.Builder(this, null);
			inputDialog.setTitle(title);
			inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			inputDialog.setPositiveButton("确定", positiveClick);
			inputDialog.create().show();
		} catch (Exception e) {
			WriteLog.recordLog(TAG + "\r\n" + e.getMessage());
		}
	}

	/**
	 * 打开Activity，并跳转 不带参数
	 * 
	 * @param clazz
	 */
	public void openActivity(Class clazz) {
		openActivity(clazz, null);
	}

	/**
	 * 打开Activity，并跳转 带参数
	 * 
	 * @param clazz
	 * @param bundle
	 */
	public void openActivity(Class clazz, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	// 设置Title bar 标题栏
	protected final void setTitleWithId(int titleTextId) {
		TextView tvTitle = (TextView) findViewById(R.id.title_middle_text);
		if (tvTitle != null) {
			tvTitle.setText(getResources().getString(titleTextId));
		}
	}

	// 设置Title bar 标题栏
	protected final void setTitleWithString(String str) {
		TextView tvTitle = (TextView) findViewById(R.id.title_middle_text);
		if (tvTitle != null) {
			tvTitle.setText(str);
		}
	}

	// 设置默认返回按键
	protected final void setTitleBack() {
		setTitleImgLeft(R.drawable.title_left, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 设置Title bar 左边图片
	protected final void setTitleImgLeft(int resId, OnClickListener onClickListener) {
		ImageView imageLeft = (ImageView) findViewById(R.id.title_left_image);
		if (imageLeft != null) {
			imageLeft.setImageResource(resId);
			imageLeft.setVisibility(View.VISIBLE);
			imageLeft.setOnClickListener(onClickListener);
		}
	}

	// 设置Title bar 左边图片
	protected final void setTitleImgLeft(OnClickListener onClickListener) {
		ImageView imageLeft = (ImageView) findViewById(R.id.title_left_image);
		if (imageLeft != null) {
			imageLeft.setImageResource(R.drawable.title_left);
			imageLeft.setVisibility(View.VISIBLE);
			imageLeft.setOnClickListener(onClickListener);
		}
	}

	// 设置TitleBar右边图片
	protected final void setTitleImgRight(int resId, OnClickListener onClickListener) {
		ImageView imageRight = (ImageView) findViewById(R.id.title_right_image);
		if (imageRight != null) {
			imageRight.setImageResource(resId);
			imageRight.setVisibility(View.VISIBLE);
			imageRight.setOnClickListener(onClickListener);
		}
	}

	// 隐藏TitleBar右边图片
	protected final void hideTitleImgRight() {
		ImageView imageRight = (ImageView) findViewById(R.id.title_right_image);
		if (imageRight != null) {
			imageRight.setVisibility(View.INVISIBLE);
		}
	}

	protected void setNeedExitApp(boolean isNeedExitApp) {
		this.isNeedExitApp = isNeedExitApp;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!isNeedExitApp)
			return super.onKeyDown(keyCode, event);

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (loadingDialog.isShowing())
				return false;

			if (System.currentTimeMillis() - mTimeStart > 1500) {
				CustomToast.showToast(this, R.string.exit_app, 2000);
				mTimeStart = System.currentTimeMillis();
				return false;
			} else {
				// 执行退出操作
				exitApp();
				return false;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void exitApp() {

		// 保存统计数据
		MobclickAgent.onKillProcess(BaseActivity.this);
		int nRet = QApplication.activity_list.size();
		for (int i = 0; i < nRet; i++) {
			QApplication.activity_list.get(i).finish();
		}
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME); // 很重要
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 可无
		startActivity(startMain);
		System.exit(0);
	}

	/**
	 * requestCode表示类型 QUICK_PUECHASE快速申购流程
	 * virtual_purchase虚拟申购流程
	 * 
	 * resultCode表示操作 SHUDDOWN_ACTIVITY关闭
	 * 
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * 快速申购流程
		 * */
		if (resultCode == CustomConstants.SHUDDOWN_ACTIVITY && requestCode == CustomConstants.QUICK_PUECHASE) {
			this.finish();
		}

		/**
		 * 虚拟申购流程
		 * */
		if (resultCode == CustomConstants.SHUDDOWN_ACTIVITY && requestCode == CustomConstants.VIRTUAL_PUTCHASE) {
			this.finish();
		}
	}
}
