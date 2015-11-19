package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceClose
 * Description: 关闭再平衡请求
 */
public class RequestRebalanceClose extends RequestBase {

	/** 组合ID */
	private final String PARAM_SID = "sid";
	
	private String strSid;
	
	public RequestRebalanceClose(String strSid){
		this.strSid = strSid;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.REBALANCE_CLOSE;
		
		properties.put(PARAM_SID, strSid);
	}
	
}
