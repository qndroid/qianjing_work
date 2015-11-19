package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceSubmit
 * Description: 再平衡提交请求
 */
public class RequestRebalanceSubmit extends RequestBase {

	/** 组合ID */
	private final String PARAM_SID = "sid";
	/** 登录密码 */
	private final String PARAM_PASS = "passwd";
	
	private String strSid;
	private String strPass;
	
	public RequestRebalanceSubmit(String strSid, String strPass){
		this.strSid = strSid;
		this.strPass = strPass;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.REBALANCE_SUBMIT;
		
		properties.put(PARAM_SID, strSid);
		properties.put(PARAM_PASS, strPass);
	}
	
}
