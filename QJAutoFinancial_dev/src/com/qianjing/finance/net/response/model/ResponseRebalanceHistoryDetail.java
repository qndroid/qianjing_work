package com.qianjing.finance.net.response.model;

import com.qianjing.finance.model.rebalance.RebalanceHistoryDetail;


/**
 * Title: ResponseRebalanceHistoryDetail
 * Description: 再平衡交易详情请求返回报文结构
 */
public class ResponseRebalanceHistoryDetail extends ResponseBase {
	
	public RebalanceHistoryDetail historyDetail;
	
	public ResponseRebalanceHistoryDetail() {
		historyDetail = new RebalanceHistoryDetail();
	}
	
}
