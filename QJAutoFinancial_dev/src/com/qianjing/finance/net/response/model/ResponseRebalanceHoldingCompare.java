package com.qianjing.finance.net.response.model;

import com.qianjing.finance.model.rebalance.RebalanceHoldingCompare;


/**
 * Title: ResponseRebalanceHoldingCompare
 * Description: 再平衡持仓对比请求返回报文结构
 */
public class ResponseRebalanceHoldingCompare extends ResponseBase {
	
	public RebalanceHoldingCompare holdingCompare;
	
	public ResponseRebalanceHoldingCompare() {
		holdingCompare = new RebalanceHoldingCompare();
	}
	
}
