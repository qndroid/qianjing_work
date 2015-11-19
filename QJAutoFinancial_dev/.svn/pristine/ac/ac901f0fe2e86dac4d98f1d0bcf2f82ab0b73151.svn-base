package com.qianjing.finance.ui.activity.account;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：AgreementActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年7月8日 上午10:03:18
 * @文件描述：用户协议页面
 * @修改历史：2015年7月8日创建初始版本
 **********************************************************/
public class AgreementActivity extends BaseActivity
{
	private TextView titleView;
	private Button backButton;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_agreement_activity);
		mWebView = (WebView) findViewById(R.id.WebViewsIntr);
		titleView = (TextView) findViewById(R.id.title_middle_text);
		titleView.setText(getString(R.string.user_aggrement));

		backButton = (Button) findViewById(R.id.bt_back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		mWebView.loadUrl("file:///android_asset/register_agreement.html");
	}
}
