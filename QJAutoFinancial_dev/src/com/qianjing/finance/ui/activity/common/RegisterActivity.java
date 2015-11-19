package com.qianjing.finance.ui.activity.common;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.account.AgreementActivity;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.dialog.ShowDialog;
import com.qjautofinancial.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * @description 注册页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	/**
	 * UI
	 */
	private Button registerButton;
	private Button singCodeButton;
	private Button backButton;
	private CheckBox checkAgreeBox;
	private CheckBox showPasswdBox;
	private EditText mobileNumberView;
	private EditText signCodeView;
	private EditText passwordView;
	private EditText inviteView;
	private TextView titleView;
	private TextView argumentView;

	/**
	 * data
	 */
	private int flag = 0;
	private MyCount mc;
	private static int McFlag = 0;
	private int signFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		smsReceiver = new SMSBroadcastReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		registerReceiver(smsReceiver, filter);

	}

	private void initView() {

		setContentView(R.layout.activity_register_layout);

		setLoadingUncancelable();

		titleView = (TextView) findViewById(R.id.title_middle_text);
		backButton = (Button) findViewById(R.id.bt_back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		checkAgreeBox = (CheckBox) findViewById(R.id.check_agree);
		mobileNumberView = (EditText) findViewById(R.id.Mobile_num);
		signCodeView = (EditText) findViewById(R.id.Sign_code);
		passwordView = (EditText) findViewById(R.id.Set_pass);
		inviteView = (EditText) findViewById(R.id.invite_view);
		registerButton = (Button) findViewById(R.id.But_RegSure);
		registerButton.setOnClickListener(this);
		singCodeButton = (Button) findViewById(R.id.But_SignCode);
		singCodeButton.setOnClickListener(this);
		argumentView = (TextView) findViewById(R.id.TFree);
		argumentView.setOnClickListener(this);
		showPasswdBox = (CheckBox) findViewById(R.id.check);
		showPasswdBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					passwordView
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					passwordView.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});

		builder = new ShowDialog.Builder(this);
		titleView.setText("注册");
		if (Const.isShowInvite) {
			inviteView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 请求验证码
	 */
	private void requestCode() {
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("mb", mobileNumberView.getText().toString());
		AnsynHttpRequest.requestByPost(this, UrlConst.REGISTER_SMS,
				callbackData, upload);
	}

	private void requestRegister() {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("pwd", passwordView.getText().toString());
		upload.put("vc", signCodeView.getText().toString());
		upload.put("mb", mobileNumberView.getText().toString());
		upload.put("invitecode", inviteView.getText().toString());
		AnsynHttpRequest.requestByPost(this, UrlConst.REGISTER, callbackData,
				upload);
	}

	private HttpCallBack callbackData = new HttpCallBack() {
		public void back(String data, int status) {
			mHandler.sendMessage(mHandler.obtainMessage(1, data));
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1 :
					handleRegister((String) msg.obj);
					break;
			}
		};
	};
	private SMSBroadcastReceiver smsReceiver;

	private void handleRegister(String result) {

		dismissLoading();

		if (result == null || "".equals(result)) {
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(result);
			String ecode = jsonObject.getString("ecode");
			String emsg = jsonObject.getString("emsg");
			if (ecode.equals("130")) {
				showHintDialog("此号码已注册");
				mobileNumberView.setText("");
				return;
			}
			if (flag == 0 && "0".equals(ecode)) {
				mc = new MyCount(60000, 1000);
				mc.start();
				signFlag = 1;
				return;
			}
			if ("0".equals(ecode)) {
				startActivity(new Intent(this, LoginActivity.class));
				finish();

			} else {
				showHintDialog(emsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("网络不给力！");
		}

	}

	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			Date date = new Date(millisUntilFinished);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			String str = sdf.format(date);
			singCodeButton.setText(millisUntilFinished / 1000 + "重发验证码");
		}

		@Override
		public void onFinish() {
			singCodeButton.setText("重发验证码");
			signFlag = 0;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if (McFlag == 1) {
			if (mc != null) {
				mc.cancel();
			}
			signFlag = 0;
			McFlag = 0;
		}
		if (smsReceiver != null) {
			unregisterReceiver(smsReceiver);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_back :// 返回
				finish();
				break;
			case R.id.But_SignCode :// 获取验证码
				signCodeMethod();
				break;
			case R.id.But_RegSure :// 注册
				regSureMethod();
				break;
			case R.id.TFree :
				startActivity(new Intent(this, AgreementActivity.class));
				break;
		}

	}

	/**
	 * 请求注册接口
	 */
	private void regSureMethod() {
		flag = 1;
		String mobile = mobileNumberView.getText().toString();
		String sign_code = signCodeView.getText().toString();
		String set_pass = passwordView.getText().toString();

		if (StringHelper.isBlank(mobile)) {
			showHintDialog("手机号不能为空");
			return;
		}
		if (mobile.length() != 11) {
			showHintDialog("手机号码长度应为11位");
			return;
		}
		if (StringHelper.isBlank(sign_code)) {
			showHintDialog("验证码不能为空");
			return;
		}
		if (StringHelper.isBlank(set_pass)) {
			showHintDialog("密码不能为空");
			return;
		}
		if (!Util.isMacth(set_pass)) {
			showHintDialog("6-18位字符；可输入大小写字母、数字；至少包含其中两种");
			return;
		}
		if (!checkAgreeBox.isChecked()) {
			showHintDialog("请阅读使用条款和稳私政策");
			return;
		}

		requestRegister();
	}

	/**
	 * 获取验证码
	 */
	private void signCodeMethod() {
		flag = 0;
		if (signFlag == 1) {
			return;
		}
		String mobile = mobileNumberView.getText().toString();
		if (StringHelper.isBlank(mobile)) {
			showHintDialog("手机号不能为空");
			return;
		}
		if (mobile.length() != 11) {
			showHintDialog("手机号码长度应为11位");
			return;
		}
		if (!checkAgreeBox.isChecked()) {
			showHintDialog("请阅读使用条款和稳私政策");
			return;
		}
		McFlag = 1;
		requestCode();
	}

	private class SMSBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				String phoneNumber = sms.getOriginatingAddress();
				String content = sms.getMessageBody();
				if (phoneNumber.endsWith("90236408722")) {
					String verCode = StringHelper.getVerCode(content);
					signCodeView.setText(verCode);
				}
			}
		}
	}
}
