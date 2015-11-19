package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseFundSearchLatestUpdate;


/**
 * Title: IHandleFundSearchLatestUpdate
 * Description: 基金搜索数据最新更新时间请求返回数据处理接口
 */
public interface IHandleFundSearchLatestUpdate {

	// 处理函数
	public void handleResponse(ResponseFundSearchLatestUpdate response);
	
}
