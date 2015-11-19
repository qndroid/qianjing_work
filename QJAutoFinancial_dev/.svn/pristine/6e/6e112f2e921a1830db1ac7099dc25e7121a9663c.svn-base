package com.qianjing.finance.net.connection;

import java.util.Hashtable;

import android.content.Context;

import com.qianjing.finance.model.common.UserDevice;
import com.qianjing.finance.util.Util;


public class XRunnable implements Runnable {
	
    private String mStrPostUrl;
    private Hashtable<String, Object> mTableEntity;
    private Context mContext;
    private HttpCallBack mCallback;

    public XRunnable(String strPostUrl, Hashtable<String, Object> map, Context context, HttpCallBack callBack) {
        this.mStrPostUrl = strPostUrl;
        this.mTableEntity = map;
        this.mContext = context;
        this.mCallback = callBack;
    }

	private Hashtable<String,String> getPropertyMap() {

		UserDevice.initInfo(mContext);
		
		Hashtable<String,String> tableProperty = new Hashtable<String,String>();
		tableProperty.put("Software", "auto_financial-" + UserDevice.SYSTEM_VERSION_NAME);
		tableProperty.put("Platform", "android-" + UserDevice.SDK_RELEASE);
		tableProperty.put("Channel", "android_" + Util.getChannelCode(mContext));
		
    	return tableProperty;
    }
    
    @Override
    public void run() {
    	
    	mStrPostUrl = UrlConst.ROOT_URL + mStrPostUrl;

    	new XHttpConnection(mContext).doPost(mStrPostUrl, mTableEntity, getPropertyMap(), mCallback);
    }

}
