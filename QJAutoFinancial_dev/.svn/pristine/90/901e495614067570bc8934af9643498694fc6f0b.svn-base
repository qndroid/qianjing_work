package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestAssembleDetail
 * Description: 组合详情请求
 */
public class RequestAssembleDetail extends RequestBase {

	/** 组合ID */
	private final String PARAM_SID = "sid";
	
	private String strSid;
	
	public RequestAssembleDetail(String strSid){
		this.strSid = strSid;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.ASSEMBLE_DETAIL;
		
		properties.put(PARAM_SID, strSid);
	}
	
}
