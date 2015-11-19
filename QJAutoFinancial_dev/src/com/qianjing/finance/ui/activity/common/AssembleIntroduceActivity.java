package com.qianjing.finance.ui.activity.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.qjautofinancial.R;


/**
 * <p>Title: ShemeIntruductionActivity</p>
 * <p>Description: 组合介绍界面</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年5月13日
 */
public class AssembleIntroduceActivity extends BaseActivity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_assemble_introduce);
		setTitleBack();
		
		WebView mWebView = (WebView) findViewById(R.id.WebViewsIntr);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://www.qianjing.com/other/mob/brief.html");
		mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitleWithString(title);
            }
        });
	}

}
