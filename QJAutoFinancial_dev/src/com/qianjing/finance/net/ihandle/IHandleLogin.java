package com.qianjing.finance.net.ihandle;

import com.qianjing.finance.net.response.model.ResponseLogin;


/**
 * Title: IHandleLogin
 * Description: 登录请求返回数据处理接口
 */
public interface IHandleLogin {

	// 处理函数
	public void handleResponse(ResponseLogin response);
	
}
