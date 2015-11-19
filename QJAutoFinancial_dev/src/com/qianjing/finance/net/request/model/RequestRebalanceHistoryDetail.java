package com.qianjing.finance.net.request.model;

import java.util.Hashtable;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestRebalanceHistoryDetail
 * Description: 再平衡交易记录详情请求
 */
public class RequestRebalanceHistoryDetail extends RequestBase {

	/** 再平衡交易记录ID */
	private final String PARAM_SOPID = "sopid";
	
	private String strSopid;
	
	public RequestRebalanceHistoryDetail(String strSopid){
		this.strSopid = strSopid;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.REBALANCE_HISTORY_DETAIL;
		
		properties.put(PARAM_SOPID, strSopid);
	}
	
}
