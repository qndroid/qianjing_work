package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestFundRedBag
 * Description: 基金申购使用红包请求
 */
public class RequestFundRedBag extends RequestBase {
	
	/** 基金申购操作ID字段 */
	private final String PARAM_OP_ID = "opid";
	/** 红包ID字段 */
	private final String PARAM_REDBAG_ID = "id";
	/** 基金申购操作ID */
	private String strOpId;
	/** 红包ID */
	private String strId;
	
	public RequestFundRedBag(String strOpId, String strId){
		this.strOpId = strOpId;
		this.strId = strId;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.ACTIVITY_REDBAG_BUY_FUND;
		
		properties.put(PARAM_OP_ID, strOpId);
		properties.put(PARAM_REDBAG_ID, strId);
	}
}
