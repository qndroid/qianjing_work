package com.qianjing.finance.ui.activity.card;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.model.common.CardBound;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;


/**
 * <p>Title: QuickBillActivity</p>
 * <p>Description: 快钱短信验证界面</p>
 * @author majinxing
 * @date 2015年3月25日
 */
public class QuickBillVerifyActivity extends BaseActivity implements OnClickListener{
	

	private Button bt_back;
	private Button bt_getcode;
	private EditText et_code;
	private Button bt_next;
	private TextView tv_phone_number;
	private TextView tv_not_receive;
	
	private CardBound bean;
	private String accoreqserial;
	private String otherserial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UMengStatics.onBindPage2View(this);
		
		setContentView(R.layout.activity_phone_validation);
		Intent intent=getIntent();
		bean =intent.getParcelableExtra("bean");
		accoreqserial =intent.getStringExtra("accoreqserial");
		otherserial =intent.getStringExtra("otherserial");
		
		initView();
	}
	
	private void initView() {
		bt_back=(Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		bt_getcode=(Button) findViewById(R.id.bt_getcode);
		bt_getcode.setOnClickListener(this);
		et_code=(EditText) findViewById(R.id.et_code);
		bt_next=(Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		if(StringHelper.isBlank(et_code.getText().toString()))bt_next.setEnabled(false);
		et_code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(StringHelper.isBlank(et_code.getText().toString())){
					bt_next.setEnabled(false);
				}
				else {
					bt_next.setEnabled(true);
				}
				
			}
		});
		tv_phone_number=(TextView) findViewById(R.id.tv_phone_number);
		if(null!=bean){
			tv_phone_number.setText(StringHelper.hideMobileNum(bean.mobile));
		}
		tv_not_receive=(TextView) findViewById(R.id.tv_not_receive);
		tv_not_receive.setOnClickListener(this);
		new MyCount(60000, 1000).start();
		bt_getcode.setEnabled(false);
	}
	
	
	private HttpCallBack callbackData = new HttpCallBack() {

		public void back(String data, int url) {
			Message msg = new Message();
			msg.obj = data;
			msg.what=1;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1://短信验证下一步返回数据处理
				JsonData((String) msg.obj);
				break;
			case 2:
				handleQuickRequest((String) msg.obj);
				break;
			
			}
			
		};
	};
	
	protected void handleQuickRequest(String strJson) {
		dismissLoading();
		if (strJson == null || "".equals(strJson)) {
			showHintDialog("网络不给力！");
			return;
		}
		try {
			JSONObject object=new JSONObject(strJson);
			int ecode =object.optInt("ecode");
			String emsg =object.optString("emsg");
			JSONObject data=object.optJSONObject("data");
			if(ecode==0){
				accoreqserial=data.optString("accoreqserial");
				otherserial=data.optString("otherserial");
			}else{
				showHintDialog(emsg);
			}
			
		} catch (Exception e) {
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}
	
	public void JsonData(String result){
		dismissLoading();
		
		if (result == null || "".equals(result) ) {
			showHintDialog("网络不给力！");
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			int ecode = jsonObject.optInt("ecode");
			String emsg = jsonObject.optString("emsg");
			
			// {"ecode":1002,"emsg":"未知异常"}
			if (ecode==0) {
			    
			    UMengStatics.onBindPage1Submit(this);
			    
				showHintDialog("恭喜您绑卡成功。", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(222);
						QuickBillVerifyActivity.this.finish();
					}
				});
			} 
			// 卡内余额不足.提示后返回绑卡页面
			else if (ecode == 1003) {
				showHintDialog(emsg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						return;
					}
				});
			}
			else {
				showHintDialog(emsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + result);
		}
	}
	
	//重新回去验证码
	private void confirmQuickRequest(CardBound bean) {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("card", bean.card);
		upload.put("identityno", bean.identityno);
		upload.put("realname", bean.realname);
		upload.put("bankserial", bean.bankserial);
		upload.put("mobile", bean.mobile);
		upload.put("bankname", bean.bankname);
		upload.put("brach", bean.brach);
		System.out.println(bean);
		
		AnsynHttpRequest.requestByPost(this,UrlConst.QUICKBILL_CARD_VERIFY, quickrequest, upload);

	}
	
	private HttpCallBack quickrequest=new HttpCallBack() {
		
		@Override
		public void back(String data, int status) {
			Message msg=Message.obtain();
			msg.what=2;
			msg.obj=data;
			mHandler.sendMessage(msg);
		}
	};
	
	/*
	 * 网络请求确认验证码
	 * @Param 个人信息  ， 验证码
	 * 
	 * 
	 * */
	
	 private void sureVerificationCode() {
	     
	     UMengStatics.onBindPage2Click(this);
	     
		String code=et_code.getText().toString();
		if (StringHelper.isBlank(code)) {
			showToast("请输入短信验证码");
			return;
		}
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("card", bean.card);
		upload.put("identityno", bean.identityno);
		upload.put("realname", bean.realname);
		upload.put("bankserial",bean.bankserial);
		upload.put("mobile", bean.mobile);
		upload.put("bankname", bean.bankname);
		upload.put("code", code);
		upload.put("accoreqserial", accoreqserial);
		upload.put("otherserial", otherserial);
		AnsynHttpRequest.requestByPost(QuickBillVerifyActivity.this,
				UrlConst.QUICKBILL_SMS_VERIFY, callbackData, upload);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		
		case R.id.bt_getcode://重新获取验证消息
			requestCode();
			break;
		case R.id.bt_next://下一步
			sureVerificationCode();
			break;
		case R.id.tv_not_receive://收不到验证码
//			showHintDialog("收不到验证码", textForgetPWD);
			showHintDialogKnow("收不到验证码", textForgetPWD);
			break;

		
		}
		
	}

	private void requestCode() {
		new MyCount(60*1000, 1000).start();
		bt_getcode.setEnabled(false);
		confirmQuickRequest(bean);
	}
	
	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onTick(long millisUntilFinished) {
//			Date date = new Date(millisUntilFinished);
//			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//			String str = sdf.format(date);
//			System.out.println(str);
			
			String strTemp = "重发验证码"+"("+millisUntilFinished / 1000 +")";
			System.out.println(strTemp);
			bt_getcode.setText(strTemp);
		}

		@Override
		public void onFinish() {
			bt_getcode.setText("重发验证码");
			bt_getcode.setEnabled(true);
		}
	}
	
	private String textForgetPWD="验证码发送至您的银行预留手机号\n\n"
			+ "1.请确认当前是否使用银行预留的手机号\n\n"
			+ "2.请检查短信是否被手机安全软件拦截\n\n"
			+ "3.若预留手机号码已停用，请联系银行客服咨询\n\n"
			+ "4.若获取更多帮助，请拨打客服电话400-893-6885\n";
	
	
	
}
