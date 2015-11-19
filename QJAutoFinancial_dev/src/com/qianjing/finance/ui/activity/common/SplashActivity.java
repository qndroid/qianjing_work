package com.qianjing.finance.ui.activity.common;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.service.UpdateFundService;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

/**
 * <p>
 * Title: SplashActivity
 * </p>
 * <p>
 * Description: Logo启动界面
 * </p>
 * <p>
 * Company: www.qianjing.com
 * </p>
 * 
 * @author fangyan
 * @date 2015年1月31日
 */
public class SplashActivity extends BaseActivity
{
	public static final String TAG = "SplashActivity";

	private static final int FLAG_START_APP = 1;
	private static final int FLAG_FINISH = 2;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message massage)
		{
			super.handleMessage(massage);

			switch (massage.what)
			{
			case FLAG_START_APP:

				// 已设置手势密码
				if (!StringHelper.isBlank(PrefUtil.getKey(mApplication)))
				{
					// 开始启动锁屏扫描
					mApplication.isStartLockScan = true;
					mApplication.startLock();
					// 登录流程交给Application中的锁屏扫描
					// 1s后关闭自己
					mHandler.sendEmptyMessageDelayed(FLAG_FINISH, 1000);
					return;
				}

				// 已经过则跳转到选择登录页
				if (PrefUtil.getFirstEnter(SplashActivity.this))
				{
					openActivity(GuideActivity.class);
				}
				else
				{
					openActivity(MainActivity.class);
				}
				finish();
				break;
			case FLAG_FINISH:
				finish();
				break;
			case 0x03:
				handleInvite(massage.obj.toString());
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mApplication.setIsLogin(true);
		setContentView(R.layout.start_activity);

		startService(new Intent(this, UpdateFundService.class));
		if (!Util.checkSDKLevel(this))
		{
			Toast.makeText(this, R.string.version_low, Toast.LENGTH_LONG).show();
			mHandler.sendEmptyMessageDelayed(FLAG_FINISH, 5 * 1000);
		}
		else
		{
			mHandler.sendEmptyMessageDelayed(FLAG_START_APP, 5 * 1000);
		}
		requestConfig();
	}

	/**
	 * 发送邀请码是否显示
	 */
	private void requestConfig()
	{
		AnsynHttpRequest.requestByPost(this, UrlConst.COMMON, callbackData, null);
	}

	private HttpCallBack callbackData = new HttpCallBack()
	{
		@Override
		public void back(String data, int status)
		{
			if (data == null || data.equals(""))
			{
				return;
			}
			else
			{
				mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
			}
		}
	};

	private void handleInvite(String strJson)
	{
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			switch (ecode)
			{
			case 0:
				JSONObject data = object.optJSONObject("data");
				Const.isShowInvite = data.optInt("openInvite") == 1 ? true : false;
				break;
			default:
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		JPushInterface.onPause(this);
	}

}
