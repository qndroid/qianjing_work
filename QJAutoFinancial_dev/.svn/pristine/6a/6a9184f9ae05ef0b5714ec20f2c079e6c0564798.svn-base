package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceDetail
 * Description: 再平衡详情请求
 */
public class RequestRebalanceDetail extends RequestBase {

	/** 组合ID */
	private final String PARAM_SID = "sid";
	
	private String strSid;
	
	public RequestRebalanceDetail(String strSid){
		this.strSid = strSid;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.REBALANCE_DETAIL;
		
		properties.put(PARAM_SID, strSid);
	}
	
}
