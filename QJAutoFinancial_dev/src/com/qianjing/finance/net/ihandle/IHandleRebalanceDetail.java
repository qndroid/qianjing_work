package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseRebalanceDetail;


/**
 * Title: IHandleRebalanceDetail
 * Description: 再平衡详情请求返回数据处理接口
 */
public interface IHandleRebalanceDetail {

	// 处理函数
	public void handleResponse(ResponseRebalanceDetail response);
	
}
