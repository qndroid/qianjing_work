package com.qianjing.finance.ui.activity.account;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qjautofinancial.R;

public class UserActivity extends BaseActivity {

	private static Context mContext;
	private static QApplication myApp;
	private String datas;
	private EditText reset_infor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_user);
		
		myApp = (QApplication) getApplication();
		myApp.addActivity(UserActivity.this);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(butBack.VISIBLE);
		TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		reset_infor = (EditText) findViewById(R.id.Reset_infor);
		title_middle_text.setText("用户反馈");
		Button But_send = (Button) findViewById(R.id.But_send);
		Button But_reset = (Button) findViewById(R.id.But_reset);

		butBack.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// Intent intent = new Intent(Fina_Login_Activity.this,
				// Fina_Mobile_Number_Reg_Activity.class);

				// startActivity(intent);
				finish();

			}
		});

		But_send.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				String sreset_infor = reset_infor.getText().toString();

				if (isBlank(sreset_infor)) {

					showHintDialog("反馈信息不能为空!");
					return;
				}
				UserInfor();

			}
		});

		But_reset.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				reset_infor.setText("");

			}
		});

	}

	void UserInfor() {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		String version = null;
		// String downloadUrl;
		upload.put("fd", reset_infor.getText());
		// upload.put("nmonth", myApp.GetSamallDate().toString());
		// upload.put("rate", "0");
		// upload.put("age", "0");
		// upload.put("money", "0");
		// upload.put("month", "0");

		// upload.put("retire", "0");
		// upload.put("init", myApp.GetSamllMoney().toString());
		// upload.put("year", "0");
		// upload.put("type", "6");
		// upload.put("nm", "0");

		Boolean isNeedUpdate;
		String url = "user/feedback.php";
		AnsynHttpRequest.requestByPost(UserActivity.this, url, callbackData,
				upload);

	}

	private HttpCallBack callbackData = new HttpCallBack() {
		public void back(String data, int url) {
			
				try {
					// Index index = new Index();
					// index = index.convertHttpHead(context,data);
					// // App.index = index;
					// Log.i(tag, index.toString());
					datas = data;
					Message msg = new Message();
					msg.what = 1;
					// msg.obj = index;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}
	};

	/**
	 * 处理UI线程中数
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JsonData(datas);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Toast.makeText(Fina_User.this, "测试数据 数据编号+datas,
				// Toast.LENGTH_SHORT).show();
				break;
			
			}
		};
	};

	public void JsonData(String s) throws JSONException {

		analyticJson(s);

	}

	public void analyticJson(String result) throws JSONException {
		dismissLoading();
		if (null==result||"".equals(result)) {
			showHintDialog("网络不给力！");
			return;
		}
		JSONObject jsonObject = new JSONObject(result); // 返回的数据形式是一个Object类型，所以可以直接转换成一个Object

		String Succ = jsonObject.getString("ecode");
		String Errormsg = jsonObject.getString("emsg");

		if (Succ.equals("0")) {
			showHintDialog("反馈成功!");

		} else {
			showHintDialog( Errormsg);
			return;
		}

	}

	public static boolean isBlank(String str) {
		return (str == null || "".equals(str.trim()) || "null".equals(str));
	}

	// 手机号是否非
	public static final boolean isIllegalMobile(String str) {
		String strPattern = "((\\(\\d{2,3}\\))|(\\d{3}\\-))?(1[3458]\\d{9})";
		Pattern pattern = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return !matcher.matches();
	}

	

}