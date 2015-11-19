
package com.qianjing.finance.ui.activity.common;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.UserDevice;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.wheelview.SelectPicPopupWindow;
import com.qjautofinancial.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @Description:web页面
 * @author majinxin
 * @date 2014-11-13 下午6:05:28
 */
public class WebActivity extends BaseActivity {

	private WebView webview; //webview
	private int type;
	private SelectPicPopupWindow menuWindow;
	private boolean sharedFriend = true;
	private IWXAPI api;
	private boolean IS_LOAD_SHARE_PAGE = false;
	private static final String APP_ID = "wxf18094a5a3280ead";

	public static String[] urllist = new String[] { "file:///android_asset/bound_agreement.html",//绑卡中的协议  webType=0
			"http://www.qianjing.com/other/pamphlet/",//设置-关于我们-公司简介，跳到URL  webType=1
			"http://www.qianjing.com/other/pamphlet/qa1.html",//活期宝    webType=2
			"http://www.qianjing.com/other/pamphlet/qa3.html",//取现    webType=3
			"http://www.qianjing.com/other/pamphlet/qa2.html",//充值    webType=4
			"http://i.qianjing.com/account/ac/coupon.php",//优惠券
			"http://i.qianjing.com/account/ac/invite_code.php",//邀请码
			"http://www.qianjing.com/other/pamphlet/qa4.html",//申购失败显示
			"http://i.qianjing.com/activ/active_test_android/test.php?from=Android"//交互页面
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initView() {

		setContentView(R.layout.activity_web);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		type = getIntent().getIntExtra("webType", -1);

		tvRightText = (TextView) findViewById(R.id.title_right_text);
		tvRightText.setOnClickListener(new ClosePage());

		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitleWithString(title);
				super.onReceivedTitle(view, title);
			}
		});

		switch (type) {
		case 0://绑卡页面的协议
			webview.loadUrl(urllist[0]);
			tvRightText.setText("关闭");
			break;
		case 1://设置-关于我们-公司简介
			webview.loadUrl(urllist[1]);
			tvRightText.setText("关闭");
			break;
		case 2://活期宝
			setCookie(urllist[2]);
			webview.loadUrl(urllist[2]);
			tvRightText.setText("关闭");
			break;
		case 3://取现
			setCookie(urllist[3]);
			webview.loadUrl(urllist[3]);
			tvRightText.setText("关闭");
			break;
		case 4://充值
			setCookie(urllist[4]);
			webview.loadUrl(urllist[4]);
			tvRightText.setText("关闭");
			break;
		case 5://我的优惠券
			setCookie(urllist[5]);
			webview.loadUrl(urllist[5]);
			tvRightText.setText("关闭");
			break;
		case 6://我的邀请码
			initInviteView();
			requestShareDate();
			setCookie(urllist[6]);
			webview.loadUrl(urllist[6]);
			tvRightText.setText("分享");
			break;
		case 7:
			setCookie(urllist[7]);
			webview.loadUrl(urllist[7]);
			tvRightText.setText("关闭");
			break;
		case 8:
			String url = getIntent().getStringExtra("url");
			setCookie(url);
			webview.loadUrl(url);
			tvRightText.setText("关闭");
			break;
		case 9://与Web页交互
			String strUrl = urllist[8];
			webview.loadUrl(strUrl);
			tvRightText.setText("关闭");
			break;
		}

		webview.addJavascriptInterface(this, "qianjing");

		setTitleImgLeft(R.drawable.title_left, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		});
	}

//	@JavascriptInterface
//	private  void startNativeFunction(String jsonStr) {
//		try {
//			JSONObject jsonObject = new JSONObject(jsonStr);
//			String method = jsonObject.optString("method");
//			if ("toRegister".equals(method)) {
//				toRegister();
//			} else if ("toLogin".equals(method)) {
//				toLogin();
//			} else if ("toBindCard".equals(method)) {
//				toBindCard();
//			} else if ("toInvest".equals(method)) {
//				toInvest();
//			} else if ("toShare".equals(method)) {
//				JSONObject data = jsonObject.optJSONObject("data");
//				String shareContent = data.optString("eventShareContent");
//				String shareImageUrl = data.optString("eventShareImageUrl");
//				String shareTitle = data.optString("eventShareTitle");
//				String shareWebUrl = data.optString("eventShareWebUrl");
//				toShare(shareContent, shareImageUrl, shareTitle, shareWebUrl);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

	@JavascriptInterface
	public void toLogin() {
		openActivity(LoginActivity.class);
	};

	public void toRegister() {
		openActivity(RegisterActivity.class);
	};

	public void toBindCard() {
		openActivity(QuickBillActivity.class);
	};

	public void toInvest() {
		openActivity(MainActivity.class);
	};

	public void toShare(String... share) {
		eventShareContent = share[0];
		eventShareImageUrl = share[1];
		eventShareTitle = share[2];
		eventShareWebUrl = share[3];
		initShareData();
	};

	private class ClosePage implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (IS_LOAD_SHARE_PAGE) {
				if (type == 6) {
					initShareData();
				} else {
					finish();
				}
			} else {
				finish();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			finish();
		}
	}

	/**
	 * <p>Title: setCookie</p>
	 * <p>Description: 设置Cookies到指定URL</p>
	 * @param url
	 */
	private void setCookie(String url) {

		CookieSyncManager.createInstance(getApplicationContext());
		CookieManager cookieManager = CookieManager.getInstance();
		//		cookieManager.removeSessionCookie();

		String strBCookie = "bcookie=" + UserDevice.getIMEI(this);
		/**
		 * 必须有path和domain
		 */
		String strTCookie = "tcookie=" + PrefUtil.getTCookie(this) + "; path=/; domain=.qianjing.com";
		String strUCookie = "ucookie=" + PrefUtil.getUCookie(this) + "; path=/; domain=.qianjing.com";

		cookieManager.setCookie(url, strBCookie);
		cookieManager.setCookie(url, strTCookie);
		cookieManager.setCookie(url, strUCookie);
		CookieSyncManager.getInstance().sync();
	}

	private void requestShareDate() {
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.INVITE_SHARE, new HttpCallBack() {

			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				handler.sendMessage(msg);
			}
		}, null);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonStr = (String) msg.obj;
			analysisShareData(jsonStr);
		};
	};

	private String eventShareContent;

	private String eventShareImageUrl;

	private String eventShareTitle;

	private String eventShareWebUrl;
	private TextView tvRightText;

	protected void analysisShareData(String jsonStr) {

		if (jsonStr == null || "".equals(jsonStr)) {
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try {

			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			if (ecode == 0) {

				JSONArray dataArray = jobj.optJSONArray("data");
				JSONObject data = dataArray.getJSONObject(0);

				eventShareContent = data.optString("eventShareContent");
				eventShareImageUrl = data.optString("eventShareImageUrl");
				eventShareTitle = data.optString("eventShareTitle");
				eventShareWebUrl = data.optString("eventShareWebUrl");

				leftTxt.setVisibility(View.VISIBLE);
				IS_LOAD_SHARE_PAGE = true;
				dismissLoading();

			} else {
				// ecode不为o，则隐藏分享按钮
				tvRightText.setText("关闭");
				IS_LOAD_SHARE_PAGE = false;
				leftTxt.setVisibility(View.GONE);
				dismissLoading();
			}
		} catch (Exception e) {
			showHintDialog(getString(R.string.net_data_error));
			dismissLoading();
		}
	}

	private void initShareData() {

		if (!StringHelper.isBlank(eventShareWebUrl) && !StringHelper.isBlank(UserManager.getInstance().getUser().getUid())
				&& !StringHelper.isBlank(eventShareTitle) && !StringHelper.isBlank(eventShareContent) && !StringHelper.isBlank(eventShareImageUrl)) {
			menuWindow = new SelectPicPopupWindow(WebActivity.this, itemsOnClick);
			// 显示窗口
			menuWindow.showAtLocation(WebActivity.this.findViewById(R.id.ll_activity_web), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		}
	}

	public void initInviteView() {

		leftTxt = (TextView) findViewById(R.id.title_left_text);
		leftTxt.setVisibility(View.GONE);
		leftTxt.setText("关闭");
		leftTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				sharedFriend = true;
				new GetImageTask().execute(eventShareImageUrl);
				break;
			case R.id.btn_pick_photo:
				sharedFriend = false;
				new GetImageTask().execute(eventShareImageUrl);
				break;
			default:
				break;
			}
		}
	};
	private TextView leftTxt;

	class GetImageTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			HttpClient hc = new DefaultHttpClient();
			HttpGet hg = new HttpGet(params[0]);// 获取csdn的logo
			final Bitmap bm;
			try {
				HttpResponse hr = hc.execute(hg);
				bm = BitmapFactory.decodeStream(hr.getEntity().getContent());
			} catch (Exception e) {
				return null;
			}
			return bm;
		}

		protected void onPostExecute(Bitmap result) {// 后台任务执行完之后被调用，在ui线程执行
			if (result != null) {
				WXWebpageObject webpage = new WXWebpageObject();
				if (eventShareWebUrl.contains("?")) {
					webpage.webpageUrl = eventShareWebUrl + "&" + getAddString();
				} else {
					webpage.webpageUrl = eventShareWebUrl + "?" + getAddString();
				}
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = eventShareTitle;
				msg.description = eventShareContent;
				// 图片不要超过30K
				msg.thumbData = Util.bmpToByteArray(result, true);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				// req.transaction = buildTransaction("webpage");
				req.message = msg;
				if (sharedFriend) {
					req.scene = SendMessageToWX.Req.WXSceneSession;
				} else {
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
				}
				api.sendReq(req);
			} else {
			}
		}
	}

	public String getAddString() {
		String uid = UserManager.getInstance().getUser().getUid();// :用户id
		String time_stamp = new Date().getTime() + "";// 时间戳
		String platform = "android";// 平台(ios/android)
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String appversion = "";
		if (packInfo != null) {
			appversion = packInfo.versionName;// 版本号
		}

		String uniq_id = Util.getMac();// 设备唯一标识
		String security_key = "wkgnxsjirkdfjsla;wpwld";
		// 用户id-时间戳-平台-版本号-key
		String addAll = uid + "-" + time_stamp + "-" + platform + "-" + appversion + "-" + security_key;
		String md5addAll = Util.stringToMD5(addAll);
		// uid=2342342&time_stamp=2387294628&platform=ios&uniq_id=idfa&appversion=1.1.1&hash_key=98fhsef9e8fsehff
		String finaladd = "uid=" + uid + "&time_stamp=" + time_stamp + "&platform=android&uniq_id=" + uniq_id + "&appversion=" + appversion
				+ "&hash_key=" + md5addAll;
		return finaladd;
	}

}