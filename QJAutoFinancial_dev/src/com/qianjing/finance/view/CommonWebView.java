package com.qianjing.finance.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.qianjing.finance.model.common.UserDevice;
import com.qianjing.finance.util.PrefUtil;
import com.qjautofinancial.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class CommonWebView extends WebView {
    
    Context context;
    private ProgressBar mProgressBar;
    CommonWebViewListener mCommonWebViewListener;

    public CommonWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }
    
    
    
    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }



    public CommonWebView(Context context) {
        super(context);
        initView(context);
    }



    @SuppressLint("SetJavaScriptEnabled")
    private void initView(Context context) {
        getSettings().setJavaScriptEnabled(true);
    }
    
    @SuppressWarnings("deprecation")
    public CommonWebView addProgressBar(Context actContext){
        mProgressBar = new ProgressBar(actContext, null, android.R.attr.progressBarStyleHorizontal);
//        Drawable drawable = actContext.getResources().getDrawable(R.drawable.progress_bar);
//        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar,new LayoutParams(LayoutParams.MATCH_PARENT,8,0,0));
        return this;
    }
    
    public CommonWebView addUrl(String url){
        loadUrl(url);
        return this;
    }
    
    
    public CommonWebView addNativeSupprot(){
        addJavascriptInterface(this, "qianjing");
        return this;
    }
    
    public CommonWebView build(){
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        return this;
    }
    
    public CommonWebView addCommonWebViewListener(CommonWebViewListener mCommonWebViewListener){
        this.mCommonWebViewListener = mCommonWebViewListener;
        return this;
    }
    
    public CommonWebView addCookieUrl(String url,Context appContext) {
        
        CookieSyncManager.createInstance(appContext);
        CookieManager cookieManager = CookieManager.getInstance();
        
        String strBCookie = "bcookie=" + UserDevice.getIMEI(appContext);
        String strTCookie = "tcookie=" + PrefUtil.getTCookie(appContext)+ "; path=/; domain=.qianjing.com";
        String strUCookie = "ucookie=" + PrefUtil.getUCookie(appContext)+ "; path=/; domain=.qianjing.com";
        
        cookieManager.setCookie(url, strBCookie);
        cookieManager.setCookie(url, strTCookie);
        cookieManager.setCookie(url, strUCookie);
        CookieSyncManager.getInstance().sync();
        
        loadUrl(url);
        
        return this;
    }
    
    public class WebChromeClient extends android.webkit.WebChromeClient {
        
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            
            if(mProgressBar==null){
                return ;
            }
            
            if(newProgress==100){
                mProgressBar.setVisibility(View.GONE);
            }else{
                if(mProgressBar.getVisibility()==View.GONE){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
        
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(mCommonWebViewListener!=null){
                mCommonWebViewListener.toSetTitle(title);
            }
            super.onReceivedTitle(view, title);
        }
    }
    
    
    public class WebViewClient extends android .webkit.WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    
    @JavascriptInterface
    public void startNativeFunction(String jsonStr) {
//      Toast.makeText(this, "调用了这个函数", 1).show();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String method = jsonObject.optString("method");
            if("toRegister".equals(method)){
                if(mCommonWebViewListener!=null){
                    mCommonWebViewListener.toRegister();
                }
            }else if("toLogin".equals(method)){
                if(mCommonWebViewListener!=null){
                    mCommonWebViewListener.toLogin();
                }
            }else if("toBindCard".equals(method)){
                if(mCommonWebViewListener!=null){
                    mCommonWebViewListener.toBindCard();
                }
            }else if("toInvest".equals(method)){
                if(mCommonWebViewListener!=null){
                    mCommonWebViewListener.toInvest();
                }
            }else if("toShare".equals(method)){
                JSONObject data = jsonObject.optJSONObject("data");
                String shareContent = data.optString("eventShareContent");
                String shareImageUrl = data.optString("eventShareImageUrl");
                String shareTitle = data.optString("eventShareTitle");
                String shareWebUrl = data.optString("eventShareWebUrl");
                if(mCommonWebViewListener!=null){
                    mCommonWebViewListener.toShare(shareContent,shareImageUrl,shareTitle,shareWebUrl);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public interface CommonWebViewListener{
        public void toRegister();
        public void toLogin();
        public void toBindCard();
        public void toInvest();
        public void toShare(String shareContent,String shareImageUrl,String shareTitle,String shareWebUrl);
        public void toSetTitle(String title);
    }
    
}
