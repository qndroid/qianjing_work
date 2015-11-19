package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseBase;


/**
 * Title: IHandleBase
 * Description: 默认网络返回数据处理接口
 */
public interface IHandleBase {

	// 处理函数
	public void handleResponse(ResponseBase responseBase, int status);
	
}
