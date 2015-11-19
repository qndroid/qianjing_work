package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseRebalanceHistoryDetail;


/**
 * Title: IHandleRebalanceHistoryDetail
 * Description: 再平衡交易详情请求返回数据处理接口
 */
public interface IHandleRebalanceHistoryDetail {

	// 处理函数
	public void handleResponse(ResponseRebalanceHistoryDetail response);
	
}
