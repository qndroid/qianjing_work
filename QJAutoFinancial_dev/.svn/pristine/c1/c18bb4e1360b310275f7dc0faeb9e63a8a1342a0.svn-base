package com.qianjing.finance.ui.activity.account;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.widget.dialog.LoadingDialog;
import com.qianjing.finance.widget.dialog.ShowDialog;
import com.qjautofinancial.R;

public class PassActivity extends BaseActivity
{
	private String datas;
	private EditText Source_Pass;
	private EditText Pass_new;
	private EditText Pass_confirm;
	private static ShowDialog.Builder builder;
	private LoadingDialog dialog;
	private QApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_pass);
		myApp = (QApplication) getApplication();
		builder = new ShowDialog.Builder(PassActivity.this);
		dialog = new LoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(View.VISIBLE);
		TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		Source_Pass = (EditText) findViewById(R.id.Source_Pass);
		Pass_new = (EditText) findViewById(R.id.Pass_new);
		Pass_confirm = (EditText) findViewById(R.id.Pass_confirm);
		title_middle_text.setText("修改密码");
		myApp.addActivity(PassActivity.this);
		Button But_Sure = (Button) findViewById(R.id.But_Sure);
		Button But_reset = (Button) findViewById(R.id.But_reset);
		butBack.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{
				finish();

			}
		});
		But_Sure.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{
				String source_Pass = Source_Pass.getText().toString();
				String pass_new = Pass_new.getText().toString();
				String pass_confirm = Pass_confirm.getText().toString();
				if (isBlank(source_Pass))
				{

					showHintDialog("原密码不能为空");
					return;
				}
				if (isBlank(pass_new))
				{

					showHintDialog("新密码不能为空");
					return;
				}
				if (isBlank(pass_confirm))
				{

					showHintDialog("确认密码不能为空");
					return;
				}
				if (!isMacth(pass_new))
				{

					showHintDialog("6-18位字符；可输入大小写字母、数字；至少包含其中两种");
					return;
				}
				if (pass_new.equals(pass_confirm))
				{
					ModifyPassword();

				}
				else
				{
					showHintDialog("新的密码与确认密码不一致");
					return;
				}

			}
		});

		But_reset.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{
				Source_Pass.setText("");
				Pass_new.setText("");
				Pass_confirm.setText("");

			}
		});

	}

	void ModifyPassword()
	{

		dialog.show();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("newpwd", Pass_new.getText());
		upload.put("oldpwd", Source_Pass.getText());

		String url = "user/chgpwd.php";
		AnsynHttpRequest.requestByPost(PassActivity.this, url, callbackData, upload);

	}

	private HttpCallBack callbackData = new HttpCallBack()
	{
		public void back(String data, int url)
		{

			try
			{
				datas = data;
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
			catch (Exception e)
			{
				showHintDialog("网络不给力！");
			}

		}
	};

	/**
	 * 处理UI线程中数
	 */
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				try
				{
					JsonData(datas);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	};

	public void JsonData(String s) throws JSONException
	{

		analyticJson(s);

	}

	public void analyticJson(String result) throws JSONException
	{
		if (null == result || "".equals(result))
		{
			showHintDialog("网络不给力！");
			return;
		}

		JSONObject jsonObject = new JSONObject(result); // 返回的数据形式是一个Object类型，所以可以直接转换成一个Object

		String Succ = jsonObject.getString("ecode");

		if (Succ.equals("0"))
		{
			dialog.dismiss();
			// DialogShows("修改密码","修改成功!");
			Intent intent = new Intent(PassActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();

		}
		else
		{
			showHintDialog("修改密码", "6-18位字符；可输入大小写字母、数字；至少包含其中两种");
			dialog.dismiss();
			return;
		}

	}

	public static boolean isBlank(String str)
	{
		return (str == null || "".equals(str.trim()) || "null".equals(str));
	}

	// 手机号是否非
	public static final boolean isIllegalMobile(String str)
	{
		String strPattern = "((\\(\\d{2,3}\\))|(\\d{3}\\-))?(1[3458]\\d{9})";
		Pattern pattern = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return !matcher.matches();
	}

	public static void DialogShows(String Titles, String Message)
	{

		builder.setMessage(Message);
		builder.setTitle(Titles);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置你的操作事项
			}
		});

		builder.create().show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
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
}