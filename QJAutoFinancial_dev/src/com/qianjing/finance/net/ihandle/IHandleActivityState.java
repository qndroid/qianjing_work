package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseActivityState;


/**
 * Title: IHandleLogin
 * Description: 运营活动信息请求返回数据处理接口
 */
public interface IHandleActivityState {

	// 处理函数
	public void handleResponse(ResponseActivityState response);
	
}
