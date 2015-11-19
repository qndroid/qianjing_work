package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseRebalanceHoldingCompare;


/**
 * Title: IHandleRebalanceHoldingCompare
 * Description: 再平衡持仓对比请求返回数据处理接口
 */
public interface IHandleRebalanceHoldingCompare {

	// 处理函数
	public void handleResponse(ResponseRebalanceHoldingCompare response);
	
}
