package com.qianjing.finance.ui.activity.account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

public class PassResetActivity extends BaseActivity
{

	private Button BurSure;
	private Button But_SignCode;
	private QApplication myApp;
	private String datas;
	public Map<String, String> map;
	private static int McFlag = 0;

	private EditText Mobile_num;
	private EditText Sign_code;
	private EditText New_pass;
	private TextView title_middle_text;
	public static final String LOCK = "lock";
	public static final String LOCK_KEY = "lock_key";
	private MyCount mc;
	private int Flag = 0;
	private int SignFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_pass_reset);

		smsReceiver = new SMSBroadcastReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		registerReceiver(smsReceiver, filter);

		setLoadingUncancelable();

		myApp = (QApplication) getApplication();
		Mobile_num = (EditText) findViewById(R.id.Mobile_num);
		Sign_code = (EditText) findViewById(R.id.Sign_code);
		New_pass = (EditText) findViewById(R.id.New_pass);

		But_SignCode = (Button) findViewById(R.id.But_SignCode);
		BurSure = (Button) findViewById(R.id.BurSure);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(View.VISIBLE);
		myApp.addActivity(PassResetActivity.this);
		butBack.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(PassResetActivity.this, LoginActivity.class);

				startActivity(intent);
				finish();

			}
		});

		title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("找回密码");
		CheckBox checkBox = (CheckBox) findViewById(R.id.check);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					New_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else
				{
					New_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		But_SignCode.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{
				if (SignFlag == 1)
				{
					return;
				}
				SignFlag = 1;
				String mobile = Mobile_num.getText().toString();

				if (isBlank(mobile))
				{

					showHintDialog("找回密码", "手机号不能为空");
					return;
				}

				if (mobile.length() != 11)
				{

					showHintDialog("找回密码", "手机号码长度应为11位");
					return;
				}

				McFlag = 1;
				mc = new MyCount(60000, 1000);
				mc.start();

				MobileSign();
			}

		});

		BurSure.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{

				Flag = 1;
				String mobile = Mobile_num.getText().toString();
				String sign_code = Sign_code.getText().toString();
				String new_pass = New_pass.getText().toString();

				if (isBlank(mobile))
				{

					showHintDialog("找回密码", "手机号不能为空");
					return;
				}

				if (mobile.length() != 11)
				{

					showHintDialog("找回密码", "手机号码长度应为11位");
					return;
				}
				if (!mobile.contains("*") && isIllegalMobile(mobile))
				{

					showHintDialog("找回密码", "手机号格式不正确");
					return;
				}

				if (isBlank(sign_code))
				{

					showHintDialog("找回密码", "验证码不能为空");
					return;
				}
				if (isBlank(new_pass))
				{

					showHintDialog("找回密码", "密码不能为空");
					return;
				}
				if (!isMacth(new_pass))
				{

					showHintDialog("6-18位字符；可输入大小写字母、数字；至少包含其中两种");
					return;
				}
				MobileHttpSureReq();
			}

		});
	}

	void MobileSign()
	{
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("mb", Mobile_num.getText().toString());
		String url = "user/sendvc.php";
		AnsynHttpRequest.requestByPost(PassResetActivity.this, url, callbackData, upload);

	}

	void MobileHttpSureReq()
	{
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("pwd", New_pass.getText().toString());
		upload.put("vc", Sign_code.getText().toString());
		upload.put("mb", Mobile_num.getText().toString());
		String url = "user/resetpwd.php";
		AnsynHttpRequest.requestByPost(PassResetActivity.this, url, callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack()
	{
		public void back(String data, int url)
		{
			datas = data;
			Message msg = new Message();
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			JsonData(datas);
		};
	};
	private SMSBroadcastReceiver smsReceiver;

	public void JsonData(String s)
	{
		analyticJson(s);
	}

	public void analyticJson(String result)
	{

		if (result == null || "".equals(result))
		{
			dismissLoading();
			showHintDialog("找回密码", "网络不给力！");
			return;
		}

		try
		{
			JSONObject jsonObject = new JSONObject(result);

			String Succ = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");
			if (Succ.equals("134"))
			{
				dismissLoading();
				showHintDialog("找回密码", "验证码错误");
				Mobile_num.setText("");
				return;
			}
			if (Succ.equals("150"))
			{
				dismissLoading();
				showHintDialog("找回密码", "找回密码次过多，暂时被限");
				Mobile_num.setText("");
				return;
			}
			if (Succ.equals("131"))
			{
				dismissLoading();
				showHintDialog("找回密码", Errormsg);
				Mobile_num.setText("");
				return;
			}
			if (Flag == 0 && Succ.equals("0"))
			{
				return;
			}

			if (Succ.equals("0"))
			{
				Flag = 0;
				dismissLoading();
				Intent intent = new Intent(PassResetActivity.this, LoginActivity.class);

				startActivity(intent);
				finish();

			}
			else
			{
				dismissLoading();
				showHintDialog("找回密码", Errormsg);
				WriteLog.recordLog(result + Errormsg);
				return;
			}
		}
		catch (JSONException e)
		{
			dismissLoading();
			showHintDialog("找回密码", "网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + result);
		}
	}

	public static boolean isBlank(String str)
	{
		return (str == null || "".equals(str.trim()) || "null".equals(str));
	}

	// 手机号是否非
	public static final boolean isIllegalMobile(String str)
	{
		String strPattern = "((\\(\\d{2,3}\\))|(\\d{3}\\-))?(1[34578]\\d{9})";
		Pattern pattern = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return !matcher.matches();
	}

	class MyCount extends CountDownTimer
	{

		public MyCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			Date date = new Date(millisUntilFinished);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			String str = sdf.format(date);
			System.out.println(str);
			But_SignCode.setText(millisUntilFinished / 1000 + "重发验证码");
		}

		@Override
		public void onFinish()
		{
			But_SignCode.setText("重发验证码");
			SignFlag = 0;
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
		if (McFlag == 1)
		{
			mc.cancel();
			SignFlag = 0;
			McFlag = 0;
		}
		unregisterReceiver(smsReceiver);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(PassResetActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		return super.onKeyDown(keyCode, event);

	}

	// 6-18位非纯数字判断
	public boolean isMacth(String value)
	{

		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
		return value.matches(regex);

	}

	private class SMSBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			LogUtils.syso("接收到短信。。");
			for (Object obj : objs)
			{
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				String phoneNumber = sms.getOriginatingAddress();
				String content = sms.getMessageBody();
				LogUtils.syso("正在解析短信。。");
				if (phoneNumber.endsWith("90023408722"))
				{
					LogUtils.syso("正在提取验证码。。");
					String verCode = StringHelper.getVerCode(content);
					Sign_code.setText(verCode);
				}
			}
		}
	}
}
