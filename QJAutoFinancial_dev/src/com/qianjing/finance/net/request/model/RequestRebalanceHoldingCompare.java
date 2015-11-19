package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceHoldingCompare
 * Description: 再平衡持仓对比请求
 */
public class RequestRebalanceHoldingCompare extends RequestBase {

	/** 组合ID */
	private final String PARAM_RSID = "rsid";
	
	private String strRsid;
	
	public RequestRebalanceHoldingCompare(String strRsid){
		this.strRsid = strRsid;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.REBALANCE_HOLDING_COMPARE;
		
		properties.put(PARAM_RSID, strRsid);
	}
	
}
