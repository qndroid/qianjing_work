
package com.qianjing.finance.ui.activity.common;

import java.util.Hashtable;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.handler.AliasHandler;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.Login;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.account.AboutActivity;
import com.qianjing.finance.ui.activity.account.PassResetActivity;
import com.qianjing.finance.util.PrefUtil;
import com.qjautofinancial.R;

public class LoginActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "login";

	/**
	 * UI
	 */
	private Button backButton;
	private Button ButRegi;
	private Button ButReg;
	private Button ButLogin;
	private Button ButPassReset;
	private Button ButConn;

	private TextView User_Num;
	private TextView User_Pass;
	private TextView title_middle_text;

	public Map<String, String> map;
	private String strContentUrl;

	/**
	 * 登陆后返回目标地址
	 */
	private int target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getBooleanExtra("locklogin_isOK", false)) {
			openActivity(MainActivity.class);
			finish();
			return;
		}
		target = getIntent().getIntExtra("LoginTarget", -1);

		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_login);
		setLoadingUncancelable();
		
		View titleBar = findViewById(R.id.title);
		titleBar.setBackgroundColor(0xffff3b3b);
		User_Num = (TextView) findViewById(R.id.User_Num);
		User_Pass = (TextView) findViewById(R.id.User_Pass);
		backButton = (Button) findViewById(R.id.bt_back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("登录");
		// 填充手机号。如果之前没有保存手机号，则设置空内容
		User_Num.setText(PrefUtil.getPhoneNumber(this));
		ButLogin = (Button) findViewById(R.id.btn_login);
		ButReg = (Button) findViewById(R.id.ButReg);
		ButRegi = (Button) findViewById(R.id.ButRegi);
		ButPassReset = (Button) findViewById(R.id.ButPassReset);
		ButConn = (Button) findViewById(R.id.ButConn);
		ButPassReset.setOnClickListener(this);
		ButRegi.setOnClickListener(this);
		ButReg.setOnClickListener(this);
		ButConn.setOnClickListener(this);
		ButLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bt_back:// 返回
			finish();
			break;
		case R.id.ButRegi:
			// Activity跳转
			openActivity(RegisterActivity.class);
			break;
		case R.id.ButReg:
			// Activity跳转
			openActivity(RegisterActivity.class);
			finish();
			break;
		case R.id.ButPassReset:
			openActivity(PassResetActivity.class);
			finish();
			break;
		case R.id.ButConn:
			// Activity跳转
			openActivity(AboutActivity.class);
			break;
		case R.id.btn_login:

			// if (StrUtil.isBlank(User_Num.getText().toString())) {
			// showHintDialog("登录", "帐号不能为空");
			// return;
			// }
			// if (StrUtil.isBlank(User_Pass.getText().toString())) {
			// showHintDialog("登录", "用户密码不能为空");
			// return;
			// }
			// if (User_Num.getText().toString().length() != 11) {
			// showHintDialog("登录", "手机号码长度应为11");
			// return;
			// }
			// if (User_Pass.getText().toString().length() < 6) {
			// showHintDialog("登录", "密码应为6-18");
			// return;
			// }

			showLoading();

			Hashtable<String, Object> upload = new Hashtable<String, Object>();
			upload.put("mb", User_Num.getText().toString());
			upload.put("pwd", User_Pass.getText().toString());
			RequestManager.request(this, UrlConst.LOGIN, upload, Login.class, new XCallBack() {
				@Override
				public void success(BaseModel model) {
					dismissLoading();

					Login login = (Login) model;
					if (login.stateCode == 0) {
						User user = login.user;

						mApplication.setIsLogin(false);
						mApplication.setLogined(true);

						// 另一账户登录则清除未读通知状态
						String currentNum = User_Num.getText().toString();
						String lastNum = PrefUtil.getPhoneNumber(LoginActivity.this);
						if (!TextUtils.equals(currentNum, lastNum)) {
							PrefUtil.removeAllInformFlag(LoginActivity.this);
						}

						// 如果是有效登录，需要记录有效登录状态
						PrefUtil.setEffectiveLogin(LoginActivity.this);
						// 有效登录后，记录手机号码
						PrefUtil.setPhoneNumber(LoginActivity.this, user.getMobile());

						UserManager.getInstance().setUser(user);

						// 注册推送别名
						new AliasHandler(LoginActivity.this, user.getMobile()).sendEmptyMessage(0);

						// 登录成功后，推送的活动通知直接跳转到Web页
						if (strContentUrl != null) {
							Intent i = new Intent(LoginActivity.this, AdsContentsActivity.class);
							i.putExtra("contentUrl", strContentUrl);
							startActivity(i);
						} else {
							// if (PrefUtil.isFirstToMain(mApplication)) {
							// mApplication.setIsLogin(true);
							// PrefUtil.saveKey(LoginActivity.this, "");
							//
							// Bundle bundle = new Bundle();
							// bundle.putBoolean("isFromSet", true);
							// openActivity(GestureLockActivity.class, bundle);
							// finish();
							// } else {
							switch (target) {
							case 0:
							case 1:
							case 2:
							case 3:
							case 4:
								finish();
								break;
							default:
								openActivity(MainActivity.class);
								finish();
								break;
							}
							// }
							finish();
						}

					} else {
						showHintDialog(login.strErrorMessage);
					}
				}

				@Override
				public void fail() {
					dismissLoading();
				}
			});
			break;
		}
	}

	/**
	 * 返回目标枚举
	 * 
	 * @author renzhiqiang
	 */
	public enum LoginTarget {

		FUND_DETAIL(0), QUICK_FUND_DETAIL(1), WALLET(2), ASSEMBLE(3), RECOMMAND_DETAIL(4);
		private int value;

		private LoginTarget(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
