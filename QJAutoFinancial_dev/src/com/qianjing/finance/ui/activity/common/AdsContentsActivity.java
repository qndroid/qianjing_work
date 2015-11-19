package com.qianjing.finance.ui.activity.common;

import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.UserDevice;
import com.qianjing.finance.net.connection.StreamUtil;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.util.Network;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.wheelview.SelectPicPopupWindow;
import com.qjautofinancial.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class AdsContentsActivity extends BaseActivity {
	
	private IWXAPI api;
	private static final String APP_ID = "wxf18094a5a3280ead";
	
	// 分享选项
	// 自定义的弹出框类
	private SelectPicPopupWindow menuWindow;
	private boolean sharedFriend = true;
	private Button ButReg;
	public Map<String, String> map;

	private WebView mWebView;
	private RelativeLayout rl_noline;
	private String eventShareWebUrl;
	private String eventShareContent;
	private String eventShareTitle;
	private String eventShareImageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();

		Intent intent = getIntent();
		try {
			if (intent != null) {
				
				String strContentUrl = intent.getStringExtra(Const.KEY_NOTIFICATION_CONTENT_URL);
				
	        	if (!StringHelper.isBlank(strContentUrl)) {
					mWebView.loadUrl(strContentUrl);
				}
	        	
				
				String strUrl = intent.getStringExtra("url");
				if (strUrl != null) {
					JSONObject object = new JSONObject(strUrl);
					String url = object.optString("eventClickUrl");
					eventShareImageUrl = object.optString("eventShareImageUrl");
					eventShareWebUrl = object.optString("eventShareWebUrl");
					eventShareContent = object.optString("eventShareContent");
					eventShareTitle = object.optString("eventShareTitle");
					
					if (url.contains("?")) {
						url = url.trim() + "&" + getAddString();
					} else {
						url = url.trim() + "?" + getAddString();
					}
					
					setCookie(url);
					
					mWebView.loadUrl(url);

					if (!StringHelper.isBlank(eventShareWebUrl)
							&& !StringHelper.isBlank(UserManager.getInstance().getUser().getUid())
							&& !StringHelper.isBlank(eventShareTitle)
							&& !StringHelper.isBlank(eventShareContent)
							&& !StringHelper.isBlank(eventShareImageUrl)) {
						
						ButReg.setVisibility(View.VISIBLE);
						ButReg.setText("分享");
						ButReg.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 实例化SelectPicPopupWindow
								menuWindow = new SelectPicPopupWindow(AdsContentsActivity.this, itemsOnClick);
								// 显示窗口
								menuWindow.showAtLocation(AdsContentsActivity.this.findViewById(R.id.layout_main),
										Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
							}
						});
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void initShareData() {
        
        if (!StringHelper.isBlank(eventShareWebUrl)
                && !StringHelper.isBlank(UserManager.getInstance().getUser().getUid())
                && !StringHelper.isBlank(eventShareTitle)
                && !StringHelper.isBlank(eventShareContent)
                && !StringHelper.isBlank(eventShareImageUrl)) {
         // 实例化SelectPicPopupWindow
            menuWindow = new SelectPicPopupWindow(AdsContentsActivity.this, itemsOnClick);
            // 显示窗口
            menuWindow.showAtLocation(AdsContentsActivity.this.findViewById(R.id.layout_main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        }
    }
	
	
	@JavascriptInterface
    public void startNativeFunction(String jsonStr) {
//        Toast.makeText(this, "调用了这个函数", 1).show();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String method = jsonObject.optString("method");
            if("toRegister".equals(method)){
                toRegister();
            }else if("toLogin".equals(method)){
                toLogin();
            }else if("toBindCard".equals(method)){
                toBindCard();
            }else if("toInvest".equals(method)){
                toInvest();
            }else if("toShare".equals(method)){
                JSONObject data = jsonObject.optJSONObject("data");
                String shareContent = data.optString("eventShareContent");
                String shareImageUrl = data.optString("eventShareImageUrl");
                String shareTitle = data.optString("eventShareTitle");
                String shareWebUrl = data.optString("eventShareWebUrl");
                toShare(shareContent,shareImageUrl,shareTitle,shareWebUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public  void toLogin(){
        openActivity(LoginActivity.class);
    };
    
    public  void toRegister(){
        openActivity(RegisterActivity.class);
    };
    
    public  void toBindCard(){
        openActivity(QuickBillActivity.class);
    };
    
    public  void toInvest(){
        openActivity(MainActivity.class);
    };
    
    public  void toShare(String... share){
        eventShareContent = share[0];
        eventShareImageUrl = share[1];
        eventShareTitle = share[2];
        eventShareWebUrl = share[3];
        initShareData();
    };
	
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		
		setContentView(R.layout.activity_web_with_share);
		
		mWebView = (WebView) findViewById(R.id.WebViewsIntr);
		rl_noline = (RelativeLayout) findViewById(R.id.rl_noline_page);
		middleText = (TextView) findViewById(R.id.title_middle_text);
		
		if (!Network.checkNetWork(this)) {
			showToast("请检查当前网络连接");
		}else{
			mWebView.setVisibility(View.VISIBLE);
			rl_noline.setVisibility(View.GONE);
		}
		

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);

		// 将该app注册到微信
		ButReg = (Button) findViewById(R.id.btnTitleRight);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(View.VISIBLE);
		butBack.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});
//		class JsObject {  
//		    @JavascriptInterface  
//		    public String toString() { return "injectedObject"; }  
//		 }
		// mWebView.removeJavascriptInterface(name);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
//		mWebView.addJavascriptInterface(new JsObject(), "injectedObject"); 
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
			@Override
			public void onReceivedTitle(WebView view, String title) {
//			    middleText.setText(title);
			    super.onReceivedTitle(view, title);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
		mWebView.addJavascriptInterface(this, "qianjing");
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			// 判断微信版本是否支持。第三方接入微信要求微信是最新版本
			if (!api.isWXAppInstalled())
				showToast("您的微信不支持第三方分享，请将微信升级至最新版本.");
			
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
    private TextView middleText;
	
	class GetImageTask extends AsyncTask<String, Integer, byte[]> {

		@Override
		protected byte[] doInBackground(String... params) {
			byte[] bytes = StreamUtil.getData(params[0]);
			return bytes;
		}

		protected void onPostExecute(byte[] bytes) {// 后台任务执行完之后被调用，在ui线程执行
			if (bytes != null) {
				// 将应用注册到微信
			    api.registerApp(APP_ID);    	
				WXWebpageObject webpage = new WXWebpageObject();
				if (eventShareWebUrl.contains("?")) {
					webpage.webpageUrl = eventShareWebUrl + "&" + getAddString();
				} else {
					webpage.webpageUrl = eventShareWebUrl + "?" + getAddString();
				}
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = eventShareTitle;
				msg.description = eventShareContent;
				// 微信文档注明图片不能大于32k，但实测不能超过6k
				if (bytes.length > 6*1000) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					// 6k-12k缩放2倍；12k以上缩放4倍；32k以上缩放8倍
					if (bytes.length > 12*1000)
						options.inSampleSize = 4;
					else if (bytes.length > 32*1000)
						options.inSampleSize = 8;
					else
						options.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
					msg.thumbData = Util.bmpToByteArray(bitmap, true);
				}
				else msg.thumbData = bytes;
				
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.message = msg;
				if (sharedFriend) {
					req.scene = SendMessageToWX.Req.WXSceneSession;
				} else {
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
				}
				api.sendReq(req);
			} 
			else { }
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

		String strBCookie = "bcookie=" + UserDevice.getIMEI(this);
		String strTCookie = "tcookie=" + PrefUtil.getTCookie(this)+ "; path=/; domain=.qianjing.com";
		String strUCookie = "ucookie=" + PrefUtil.getUCookie(this)+ "; path=/; domain=.qianjing.com";

		cookieManager.setCookie(url, strBCookie);
		cookieManager.setCookie(url, strTCookie);
		cookieManager.setCookie(url, strUCookie);
		CookieSyncManager.getInstance().sync();
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
			e.printStackTrace();
		}
		String appversion = "";
		if (packInfo != null) {
			appversion = packInfo.versionName;// 版本号
		}

		String uniq_id = Util.getMac();// 设备唯一标识
		String security_key = "wkgnxsjirkdfjsla;wpwld";
		// 用户id-时间戳-平台-版本号-key
		String addAll = uid + "-" + time_stamp + "-" + platform + "-"
				+ appversion + "-" + security_key;
		String md5addAll = Util.stringToMD5(addAll);
		// uid=2342342&time_stamp=2387294628&platform=ios&uniq_id=idfa&appversion=1.1.1&hash_key=98fhsef9e8fsehff
		String finaladd = "uid=" + uid + "&time_stamp=" + time_stamp + "&platform=android&uniq_id="
				+ uniq_id + "&appversion=" + appversion + "&hash_key=" + md5addAll;
		return finaladd;
	}
	
	@Override
	protected void onDestroy() {
		((LinearLayout)findViewById(R.id.layout_main)).removeView(mWebView);
		mWebView.destroy();
		super.onDestroy();
	}
	
}
