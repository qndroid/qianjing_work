
package com.qianjing.finance.ui.activity.common;

import android.os.Bundle;

import java.util.LinkedList;

import com.qianjing.finance.view.CommonWebView;
import com.qianjing.finance.view.CommonWebView.CommonWebViewListener;
import com.qjautofinancial.R;

public class P2PWebActivity extends BaseActivity implements CommonWebViewListener {

    private CommonWebView webView;
    private LinkedList<String> linkedList = new LinkedList<String>();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_p2p);
        setTitleBack();
        String url = getIntent().getStringExtra("url");
        webView = (CommonWebView) findViewById(R.id.webview);
        webView.addCookieUrl(url, getApplicationContext()).addNativeSupprot()
                .addProgressBar(this).addCommonWebViewListener(this).build();
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            linkedList.removeLast();
            setTitleWithString(linkedList.getLast());
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void toRegister() {

    }

    @Override
    public void toLogin() {

    }

    @Override
    public void toBindCard() {

    }

    @Override
    public void toInvest() {
        
    }

    @Override
    public void toShare(String shareContent, String shareImageUrl, String shareTitle,
            String shareWebUrl) {
        
    }
    
    @Override
    public void toSetTitle(String title) {
        linkedList.addLast(title);
        setTitleWithString(title);
    }
    
    @Override
    protected void onDestroy() {
        linkedList.clear();
        super.onDestroy();
    }

}
