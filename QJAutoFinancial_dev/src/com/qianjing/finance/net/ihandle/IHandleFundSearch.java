package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseFundSearch;


/**
 * Title: IHandleFundSearch
 * Description: 基金全量搜索请求返回数据处理接口
 */
public interface IHandleFundSearch {

	// 处理函数
	public void handleResponse(ResponseFundSearch response);
	
}
