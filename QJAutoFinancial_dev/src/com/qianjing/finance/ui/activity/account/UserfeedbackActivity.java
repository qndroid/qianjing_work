package com.qianjing.finance.ui.activity.account;

import java.util.Hashtable;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

public class UserfeedbackActivity extends BaseActivity implements OnClickListener{
	
	private TextView title_middle_text;
	private Button ButBack;
	private EditText tv_vcontent;
	private Button bt_sendmsg;
	private Button bt_resetmsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_userfeedback);
		initView();
		setListener();
	}

	private void setListener() {
		ButBack.setOnClickListener(this);
		bt_sendmsg.setOnClickListener(this);
		bt_resetmsg.setOnClickListener(this);
	}

	private void initView() {
		title_middle_text=(TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("用户反馈");
		ButBack=(Button) findViewById(R.id.bt_back);
		ButBack.setVisibility(View.VISIBLE);
		tv_vcontent=(EditText) findViewById(R.id.tv_vcontent);
		bt_sendmsg=(Button) findViewById(R.id.bt_sendmsg);
		bt_resetmsg=(Button) findViewById(R.id.bt_resetmsg);
	}
	private void submitInfo() {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("fb", tv_vcontent.getText().toString());
		AnsynHttpRequest.requestByPost(UserfeedbackActivity.this, UrlConst.USER_FEEDBACK,
				callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack() {
		@Override
		public void back(String data, int url) {
			Message msg = new Message();
			msg.obj = data;
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handleJsonData((String) msg.obj);
		}

	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back://返回
			finish();
			break;
		case R.id.bt_sendmsg://发送
			if(StringHelper.isBlank(tv_vcontent.getText().toString())){
				showHintDialog("反馈内容不能为空。");
			}else{
				submitInfo();
			}
			break;
		case R.id.bt_resetmsg://重置
			tv_vcontent.setText("");
			break;

		}
		
	}

	protected void handleJsonData(String strJson) {
		dismissLoading();
		System.out.println("strJson:"+strJson);
		  if (strJson==null || "".equals(strJson)) {
	            dismissLoading();
	            showHintDialog("网络不给力！");
	            return;
	        }
		try {
			JSONObject object=new JSONObject(strJson);
			int ecode=object.optInt("ecode");
			String emsg=object.optString("emsg");
			if(ecode==0){
				showHintDialog("感谢您的建议或意见，我们将尽快处理您的反馈。", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
			}else{
				showHintDialog(emsg);
			}
			
		} catch (Exception e) {
        	showHintDialog( "网络不给力！");
        	WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}
}
