package com.qianjing.finance.net.request.model;

import java.util.Hashtable;


/**
 * <p>Title: RequestBase</p>
 * <p>Description: 请求报文基类</p>
 * @author fangyan
 * @date 2015年3月9日
 */
public class RequestBase {
	
	/** 请求地址 */
	public String url;
	
	/** 参数 */
	public Hashtable<String, Object> properties = new Hashtable<String, Object>();
	
	public RequestBase () {}
	
	public RequestBase(String strUrl) {
		this.url = strUrl;
	}
	
}
