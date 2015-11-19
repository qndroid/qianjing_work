package com.qianjing.finance.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qianjing.finance.widget.dialog.LoadingDialog;
import com.qianjing.finance.widget.dialog.LoadingHintDialog;
import com.qianjing.finance.widget.dialog.ShowDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Title: BaseFragment
 * Description: Fragment界面基类
 */
public class BaseFragment extends Fragment
{

	// Application实例
	protected QApplication mApplication;
	// 关联的Activity
	public Activity mCurrentActivity;
	// 提示对话框
	protected ShowDialog.Builder builder;
	// 加载对话框
	protected LoadingDialog loadingDialog;
	// 有文字提示的加载对话框
	protected LoadingHintDialog loadingHintDialog;
	// 输入对话框
	private InputDialog.Builder inputDialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		init();

		initUmeng();
	}

	protected void init()
	{
		mCurrentActivity = getActivity();

		if (mCurrentActivity != null)
			mApplication = (QApplication) mCurrentActivity.getApplication();

		loadingDialog = new LoadingDialog(mCurrentActivity);
	}

	protected void initUmeng()
	{
		// 调试模式下数据实时发送，不受发送策略控制
		// MobclickAgent.setDebugMode(true);

		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);

		MobclickAgent.updateOnlineConfig(mCurrentActivity);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		MobclickAgent.onResume(mCurrentActivity);
	}

	@Override
	public void onPause()
	{
		super.onPause();

		MobclickAgent.onPause(mCurrentActivity);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// 保存统计数据
		MobclickAgent.onKillProcess(mCurrentActivity);
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();

		// 保存统计数据
		MobclickAgent.onKillProcess(mCurrentActivity);
	}

	/**
	 * 显示加载对话框
	 */
	public void showLoading()
	{
		if (loadingDialog != null && !loadingDialog.isShowing())
		{
			loadingDialog.show();
		}
	}

	/**
	 * 取消加载对话框
	 */
	public void dismissLoading()
	{
		if (loadingDialog != null)
			loadingDialog.dismiss();
	}

	/**
	 * 显示有文字提示的加载对话框
	 */
	protected void showHintLoading(String msg)
	{
		if (loadingHintDialog != null && !loadingHintDialog.isShowing())
		{
			loadingHintDialog.show();
			loadingHintDialog.setHint(msg);
		}
	}

	/**
	 * 取消有文字提示的加载对话框
	 */
	public void dismissHintLoading()
	{
		if (loadingHintDialog != null)
			loadingHintDialog.dismiss();
	}

	protected void setLoadingCancelable(boolean isCancelable)
	{
		loadingDialog.setCancelable(false);
	}

	/**
	 * 显示默认Title的提示对话框
	 * 
	 * @param strMsg
	 */
	protected void showHintDialog(String strMsg)
	{
		showHintDialog(null, strMsg, null);
	}

	/**
	 * 显示默认Title的提示对话框
	 * 
	 * @param strMsg
	 * @param onClick
	 */
	protected void showHintDialog(String strMsg, DialogInterface.OnClickListener onClick)
	{
		showHintDialog(null, strMsg, onClick);
	}

	protected void showHintToast(String strMsg)
	{
		Toast.makeText(mCurrentActivity, strMsg, 3 * 1000).show();
	}

	/**
	 * 显示自定义Title的提示对话框
	 * 
	 * @param titles
	 * @param message
	 */
	protected void showHintDialog(String strTitle, String strMsg)
	{
		showHintDialog(strTitle, strMsg, null);
	}

	protected void showHintDialog(String strTitle, String strMsg, DialogInterface.OnClickListener onClick)
	{

		builder = new ShowDialog.Builder(mCurrentActivity);
		builder.setMessage(strMsg);
		builder.setTitle(strTitle == null ? "提示" : strTitle);
		if (onClick != null)
			builder.setPositiveButton("确定", onClick);
		else builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	protected void showInputDialog(String title, DialogInterface.OnClickListener positiveClick)
	{
		inputDialog = new InputDialog.Builder(mCurrentActivity, null);
		inputDialog.setTitle(title);
		inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		inputDialog.setPositiveButton("确定", positiveClick);
		inputDialog.create().show();
	}

	/**
	 * 打开Activity，并跳转 不带参数
	 * 
	 * @param clazz
	 */
	protected void openActivity(Class clazz)
	{
		openActivity(clazz, null);
	}

	/**
	 * 打开Activity，并跳转 带参数
	 * 
	 * @param clazz
	 * @param bundle
	 */
	protected void openActivity(Class clazz, Bundle bundle)
	{
		Intent intent = new Intent(mCurrentActivity, clazz);
		if (bundle != null)
		{
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

}
