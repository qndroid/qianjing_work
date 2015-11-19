package com.qianjing.finance.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.receiver.ScreenObserver;
import com.qianjing.finance.ui.activity.common.GestureLockActivity;
import com.qianjing.finance.util.CustomCrashHandler;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.helper.StringHelper;

public class QApplication extends Application
{

	/**
	 * 当前手势解锁逻辑：
	 * 
	 * 前台锁屏:on
	 * 后台锁屏:off
	 * 前台——后台:cut
	 * 前台——后台——锁屏:cut 失效，off:
	 * 
	 * */
	private ScreenObserver mScreenObserver;
	// 是否需要登录
	private boolean isLogin = true;
	// 是否开始启动锁屏扫描；由登录界面启动
	public boolean isStartLockScan = false;
	// 是否已启动锁屏界面
	public boolean isStartLock = false;
	// 保存配置信息
	private SharedPreferences preferences;
	// 设置好的图形锁密码
	private String drawKey = "";
	private boolean isSetting = false;

	static QApplication app;

	public static ArrayList<Activity> activity_list = new ArrayList<Activity>();

	/** 当前界面 */
	public static Activity currentActivity;

	// 是否有效登录
	private boolean isLogined;

	private int timerNum = 500;

	private boolean currentAppState = true;

	private boolean flag = true;

	public boolean isLogined()
	{
		return isLogined;
	}

	public void setLogined(boolean isLogined)
	{
		this.isLogined = isLogined;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		/**
		 * catch application Exception
		 * */
//         CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
//         mCustomCrashHandler.setCustomCrashHanler(this);

		app = QApplication.this;
		isLogin = true;

		String str = PrefUtil.getKey(this);
		if (!(str == null || "".equals(str.trim())))
		{
			startLock();
		}

		initPush();
	}

	public void startLock()
	{

		preferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
		initLockListener();
		if (lockDog == null)
		{
			lockDog = new Thread(new LockWatchDog());
			lockDog.start();
		}
	}

	public ScreenObserver getScreenObserver()
	{
		return mScreenObserver;
	}

	public void initLockListener()
	{
		// 屏幕的监听类
		mScreenObserver = new ScreenObserver(this);
		mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener()
		{
			@Override
			public void onScreenOn()
			{
				// 已启动锁屏扫描，且前台状态，且由非前台状态切换至前台状态以来没有启动过锁屏界面
				if (isStartLockScan && isAppOnForeground() && !isStartLock)
				{
					startLockWatch();
				}
			}

			@Override
			public void onScreenOff()
			{
				// 程序是前台状态，且屏幕关闭
				if (isAppOnForeground())
					isStartLock = false;
			}
		});
	}

	private Handler handler = new Handler();
	private boolean PAUSELOCK = false;

	Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{

			if (initTime > 0)
			{
				initTime -= timerNum;
				// 前台切换到后台时，延时执行扫描
				handler.postDelayed(this, timerNum);
			}
			else
			{
				isStartLock = true;
				PAUSELOCK = false;
			}
		}
	};

	private Thread lockDog;

	private int initTime;

	private void pauseLockWatch()
	{
		// 初始时间
		PAUSELOCK = true;
		initTime = 60000;
		startLockTimer();
	}

	private void startLockWatch()
	{
		PAUSELOCK = false;
		stopLockTimer();
	}

	public class LockWatchDog implements Runnable
	{

		@Override
		public void run()
		{

			while (flag)
			{
				// 已启动锁屏扫描，且前台状态，且由非前台状态切换至前台状态以来没有启动过锁屏界面
				if (!PAUSELOCK)
				{
					if (isStartLockScan && isAppOnForeground() && !isStartLock && CustomConstants.IS_RUNNING_FOREGROUND)
					{
						startGestureLock();
					}
				}

				if (currentAppState == true && isAppOnForeground() == false)
				{
					// 程序由前台切换到后台
					currentAppState = isAppOnForeground();
					pauseLockWatch();
				}
				else if (currentAppState == false && isAppOnForeground() == true
						&& CustomConstants.IS_RUNNING_FOREGROUND)
				{
					// 程序由后台切换到前台
					currentAppState = isAppOnForeground();

				}
				else
				{
					currentAppState = isAppOnForeground();
				}

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void stopLockTimer()
	{
		if (runnable != null)
		{
			handler.removeCallbacks(runnable);
		}
	}

	public void startLockTimer()
	{
		if (runnable != null)
		{
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, 0);
		}
	}

	public boolean getIsLogin()
	{
		return this.isLogin;
	}

	public void setIsLogin(boolean isLogin)
	{
		this.isLogin = isLogin;
	}

	public static QApplication getInsten()
	{
		return app;
	}

	private void startGestureLock()
	{
		if (StringHelper.isBlank(PrefUtil.getKey(getApplicationContext())))
			return;
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		drawKey = preferences.getString("drawKey", "");
		isSetting = drawKey == null || "".equals(drawKey);
		intent.putExtra("isSetting", isSetting);
		intent.setClass(getApplicationContext(), GestureLockActivity.class);
		startActivity(intent);

		isStartLock = true;
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground()
	{

		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(
				Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo process : processes)
		{
			if (process.processName.equals(getApplicationContext().getPackageName()))
			{
				if (process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						|| process.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE)
				{
					return true;
				}
				else
				{
					isStartLock = false;
				}

			}
		}
		return false;
	}

	/**
	 * <p>
	 * Title: initPush
	 * </p>
	 * <p>
	 * Description: 初始化推送
	 * </p>
	 */
	private void initPush()
	{

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		// 设置别名, for test
		JPushInterface.setAlias(this, "fangyan", new TagAliasCallback()
		{
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2)
			{
			}
		});
	}

	// 增加activity到activity_list，容易内存溢出
	public void addActivity(Activity aty)
	{
		activity_list.add(aty);
	}

}
