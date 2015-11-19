
package com.qianjing.finance.ui.activity.common;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.handler.AliasHandler;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.Login;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.ui.activity.account.PassWordManagerActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.LockView;
import com.qjautofinancial.R;

/**
 * liuchen
 */
public class GestureLockActivity extends BaseActivity implements OnClickListener {

	private LockView lv_lock;
	private TextView tv_setlockkey;
	private TextView tv_testkey;
	private TextView tv_show_two_white;
	private boolean isFromSet = false;
	private FrameLayout fl_foot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gesture_lock);

		LogUtils.i(GestureLockActivity.this, "running activity");
		initEvent();
		initView();
	}

	private void initView() {

		if (isFromSet) {
			if (PrefUtil.isFirstToMain(mApplication)) {
				// 如果用户是从登陆界面直接跳转到设置手势锁界面
				LogUtils.i(GestureLockActivity.this, "登陆后设置手势解锁");
				formLoginSetting();

			} else {
				// 用户是从设置手势锁密码界面跳转的
				LogUtils.i(GestureLockActivity.this, "设置界面设置手势解锁");
				alreadyLoginSetting();
			}
		} else {
			if (mApplication.isLogined()) {
				// 用户已经登陆后验证手势密码
				LogUtils.i(GestureLockActivity.this, "登陆后验证手势解锁");
				alreadyLoginVerify();
			} else {
				// 用户未登陆时验证手势密码
				LogUtils.i(GestureLockActivity.this, "未登陆时验证手势解锁");
				noLoginedVerify();
			}
		}
	}

	// 第一次进入应用设置手势锁
	private void formLoginSetting() {

		tv_setlockkey.setText("跳过设置");
		tv_testkey.setVisibility(View.GONE);
		tv_setlockkey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 跳过设置
				openActivity(MainActivity.class);
				finish();
			}
		});

		// 进行手势密码设置
		lv_lock.setIsNeedVerify(false);
		tv_show_two_white.setText(R.string.str_drawgesturetext);
	}

	// 未登陆进入手势锁验证界面
	private void noLoginedVerify() {
		tv_setlockkey.setOnClickListener(this);
		// 进行手势密码校验
		// 需要应用退出提示
		tv_show_two_white.setText("Hi," + StringHelper.hideMobileNum(PrefUtil.getPhoneNumber(this)));
		// setNeedExitApp(true);
		lv_lock.setIsNeedVerify(true);
	}

	// 已经登陆后进入手势锁设置界面
	private void alreadyLoginSetting() {
		fl_foot.setVisibility(View.GONE);
		// 进行手势密码设置
		lv_lock.setIsNeedVerify(false);
		tv_show_two_white.setText(R.string.str_drawgesturetext);
	}

	// 已经登陆后进入手势锁验证界面
	private void alreadyLoginVerify() {

		tv_setlockkey.setText("忘记密码");
		tv_testkey.setVisibility(View.GONE);
		tv_setlockkey.setOnClickListener(this);
		// 进行手势密码校验
		// 需要应用退出提示
		tv_show_two_white.setText("Hi," + StringHelper.hideMobileNum(PrefUtil.getPhoneNumber(this)));
		// setNeedExitApp(true);
		lv_lock.setIsNeedVerify(true);
	}

	private void initEvent() {
		mApplication.addActivity(this);

		isFromSet = getIntent().getBooleanExtra("isFromSet", false);

		lv_lock = (LockView) findViewById(R.id.lv_lock);
		tv_setlockkey = (TextView) findViewById(R.id.tv_setlockkey);
		tv_testkey = (TextView) findViewById(R.id.tv_testkey);
		tv_show_two_white = (TextView) findViewById(R.id.tv_show_two_white);
		fl_foot = (FrameLayout) findViewById(R.id.fl_foot);

		tv_setlockkey.setOnClickListener(this);
		tv_testkey.setOnClickListener(this);
		lv_lock.setLockViewListener(new LockView.LockViewListener() {
			@Override
			public void onIsNotMatchFirst() {
				Toast.makeText(GestureLockActivity.this, "与上一次绘制不一致，请重新绘制", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSetPassOK() {
				Toast.makeText(GestureLockActivity.this, "手势密码设置成功", Toast.LENGTH_SHORT).show();

				mApplication.isStartLock = true;
				mApplication.isStartLockScan = true;
				mApplication.startLock();
				openActivity(MainActivity.class);
				finish();
			}

			@Override
			public void onIsNeededInput() {
				Toast.makeText(GestureLockActivity.this, "再次绘制手势图案", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onInputIsTwoShort() {
				Toast.makeText(GestureLockActivity.this, "至少连接4个点，请重新输入", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onVerifyIsOK(boolean isOK, int count) {
				if (isOK) {
					if (mApplication.getIsLogin()) {
						sendLoginRequest();
					} else {
						finish();
					}

				} else {
					if (count < 1) {
						openActivity(LoginActivity.class);
						finish();
					} else {
						Toast.makeText(GestureLockActivity.this, "密码错误，还可以再次输入" + count + "次", Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onDrawingStart() {
			}

			@Override
			public void onDrawingFinished() {
			}

			@Override
			public void onDrawingFinishedKeyStr(String keyStr) {
			}

		});
	}

	private void sendLoginRequest() {

		RequestManager.request(this, UrlConst.LOGIN_GESTURE, null, Login.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {

				Login login = (Login) model;
				if (login.stateCode == 0) {
					User user = login.user;

					mApplication.setIsLogin(false);
					mApplication.setLogined(true);

					// 如果是有效登录，需要记录有效登录状态
					PrefUtil.setEffectiveLogin(GestureLockActivity.this);
					// 有效登录后，记录手机号码
					PrefUtil.setPhoneNumber(GestureLockActivity.this, user.getMobile());

					// 注册推送别名
					new AliasHandler(GestureLockActivity.this, user.getMobile()).sendEmptyMessage(0);

					UserManager.getInstance().setUser(user);

					openActivity(MainActivity.class);

					finish();
				}
			}

			@Override
			public void fail() {
				Toast.makeText(GestureLockActivity.this, "网络不给力", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_setlockkey:
			forgetPassword();
			break;
		case R.id.tv_testkey:
			mApplication.setIsLogin(false);
			openActivity(LoginActivity.class);
			finish();
			break;
		}
	}

	private void forgetPassword() {

		PrefUtil.saveKey(this, "");
		if (UserManager.getInstance().getUser() != null) {
			UserManager.getInstance().setUser(null);
		}
		if (!mApplication.isLogined()) {
			openActivity(MainActivity.class);
		}
		openActivity(PassWordManagerActivity.class);
		finish();
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess == null)
				return false;
			if (appProcess.processName == null)
				return false;
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (UserManager.getInstance().getUser() != null) {
				UserManager.getInstance().setUser(null);
			}
			openActivity(MainActivity.class);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
