package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceReply
 * Description: 再平衡用户反应（提交/拒绝）请求
 */
public class RequestRebalanceReply extends RequestBase {
	
	public static final int TYPE_SUBMIT = 1;
	public static final int TYPE_REFUSE = 2;

	/** 组合ID */
	private final String PARAM_SID = "sid";
	/** 登录密码 */
	private final String PARAM_PASS = "passwd";
	
	private String strSid;
	private String strPass;
	
	public RequestRebalanceReply(String strSid, String strPass, int replyType){
		this.strSid = strSid;
		this.strPass = strPass;
		
		create(replyType);
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(int replyType){
		if (replyType == TYPE_SUBMIT)
			url = UrlConst.REBALANCE_SUBMIT;
		else
			url = UrlConst.REBALANCE_REFUSE;
		
		properties.put(PARAM_SID, strSid);
		properties.put(PARAM_PASS, strPass);
	}
	
}
