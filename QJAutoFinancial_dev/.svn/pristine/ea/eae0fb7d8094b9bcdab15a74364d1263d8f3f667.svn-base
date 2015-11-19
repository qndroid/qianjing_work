package com.qianjing.finance.ui.activity.common;

import java.util.Hashtable;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.AssembleTradeListActivity;
import com.qianjing.finance.ui.activity.wallet.WalletActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.WriteLog;
import com.qjautofinancial.R;

public class InformDetailActivity extends BaseActivity implements OnClickListener {

	private LinearLayout ll_showtype1;// 类型为1是的布局显示
	private WebView wb_showtype2;// 类型为2的布局显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
		handleIntent();
	}

	@Override
	protected void onRestart()
	{
		middleTxt.requestFocus();
		super.onRestart();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
	    
        setContentView(R.layout.fina_inform_result);
        findViewById(R.id.bt_back).setOnClickListener(this);
        middleTxt = (TextView) findViewById(R.id.title_middle_text);
        
		ll_showtype1 = (LinearLayout) findViewById(R.id.ll_showtype1);
		wb_showtype2 = (WebView) findViewById(R.id.wb_showtype2);
		wb_showtype2.getSettings().setSupportZoom(true);
		wb_showtype2.getSettings().setBuiltInZoomControls(true);
		wb_showtype2.getSettings().setJavaScriptEnabled(true);
		wb_showtype2.setWebChromeClient(new WebChromeClient()
		{

			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				middleTxt.setText(title);
				super.onReceivedTitle(view, title);
			}
		});
		wb_showtype2.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				dismissLoading();
				middleTxt.requestFocus();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				showLoading();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});

	}

	private void handleIntent() {
	    
	    Intent intent = getIntent();
		String msg_type = intent.getStringExtra("msg_type");
		String contentid = intent.getStringExtra("contentid");
		String contenturl = intent.getStringExtra("contenturl");
		switch (Integer.parseInt(msg_type))
		{
		case 1:// 系统通知：msg_type= 1 ；contentId值为通知信息的id；
			ll_showtype1.setVisibility(View.VISIBLE);
			requestInform(contentid);
			break;
		case 2:// 活动通知：msg_type = 2 ；contentUrl值为活动的web链接地址；
			wb_showtype2.setVisibility(View.VISIBLE);
			wb_showtype2.loadUrl(contenturl);
			break;
		case 3:// 系统消息（账号交易消息等）：msg_type = 3 ；contentId值为投资组合的id；跳转到交易详情页
			Intent intent2 = new Intent(this, AssembleTradeListActivity.class);
			intent2.putExtra("sid", contentid);
			startActivity(intent2);
			finish();
			break;
		case 4:// 再平衡：msg_type = 4 ；contentId值为投资组合的id；Android在平衡没做 后续完善2015.2.2
			break;
		case 8:
			startActivity(new Intent(this, WalletActivity.class));
			finish();
			break;
		}
		// 更新已读标识
        String id = intent.getStringExtra("id");
        PrefManager.getInstance().putInt("inform_" + id, 0);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		}

	}

	public void requestInform(String id)
	{
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("id", id);
		AnsynHttpRequest.requestByPost(InformDetailActivity.this, UrlConst.PUSH_INFO, callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack()
	{

		@Override
		public void back(String data, int url)
		{
			Message msg = new Message();
			msg.what = 1;
			msg.obj = data;
			mHandler.sendMessage(msg);

		}
	};

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			dismissLoading();
			switch (msg.what)
			{
			case 1:
				String data = (String) msg.obj;
				handleJson(data);
				break;

			default:
				break;
			}
		};
	};
	private TextView middleTxt;

	protected void handleJson(String strJson)
	{
		if (strJson == null || "".equals(strJson))
		{
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}
		try
		{
			JSONObject object = new JSONObject(strJson);
			String emsg = object.optString("emsg");
			int ecode = object.optInt("ecode");
			if (ecode == 0)
			{
				JSONObject data = object.optJSONObject("data");
				JSONObject object2 = data.optJSONObject("sysmsgs");
				String info_title = object2.optString("title");
				String info_time = object2.optString("log_time");
				String info_content = object2.optString("content");
				info_time = DateFormatHelper.formatDate(info_time.concat("000"), DateFormat.DATE_4);
				setText_Type_1(info_title, info_time, info_content);
			}
			else
			{
				showHintDialog(emsg);
			}
		}
		catch (Exception e)
		{
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}

	/*
	 * msg_type为1是设置数据的显示方法
	 */
	private void setText_Type_1(String info_title, String info_time, String info_content)
	{
		System.out.println(info_title + info_time + info_content);
		TextView tv_inforeslt_title = (TextView) findViewById(R.id.tv_inforeslt_title);
		TextView tv_inforeslt_time = (TextView) findViewById(R.id.tv_inforeslt_time);
		TextView tv_inforeslt_content = (TextView) findViewById(R.id.tv_inforeslt_content);
		tv_inforeslt_title.setText(info_title);
		tv_inforeslt_time.setText(info_time);
		tv_inforeslt_content.setText(info_content);
	}
}