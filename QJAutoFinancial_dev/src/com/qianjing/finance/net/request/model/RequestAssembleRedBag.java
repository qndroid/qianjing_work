package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestAssembleRedBag
 * Description: 组合申购使用红包请求
 */
public class RequestAssembleRedBag extends RequestBase {
	
	/** 组合申购操作ID字段 */
	private final String PARAM_OP_ID = "sopid";
	/** 红包ID字段 */
	private final String PARAM_REDBAG_ID = "id";
	/** 组合申购操作ID */
	private String strOpId;
	/** 红包ID */
	private String strId;
	
	public RequestAssembleRedBag(String strOpId, String strId){
		this.strOpId = strOpId;
		this.strId = strId;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.ACTIVITY_REDBAG_BUY_ASSEMBLE;
		
		properties.put(PARAM_OP_ID, strOpId);
		properties.put(PARAM_REDBAG_ID, strId);
	}
}
