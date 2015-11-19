package com.qianjing.finance.net.test;

import java.util.Hashtable;

import android.content.Context;

import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.util.Network;


public class TestHttpRequest {

    
    /**
     * 访问网络初始化函数 支持Post请求方式
     * @param context
     * @param callBack 回调执行函数 支持线程
     * @param url 每个执行url
     * @param map 参数
     */
    public static void requestByPost(Context context, String url, HttpCallBack callback, Hashtable<String, Object> map) {

        // 判断网络是否可用
        if (!Network.checkNetWork(context)) { 
        	if (callback != null)
        		callback.back(null, HttpConst.STATUS_ERROR_NETWORK);
            return;
        }
        // 启动自定义线程，开始执行异步任务
        ThreadPoolUtil.execute(new XRunnable(url, map, context, callback));
    }

    
}

