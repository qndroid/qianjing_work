package com.qianjing.finance.net.request.model;

import com.qianjing.finance.net.connection.UrlConst;


/**
 * Title: RequestLogin
 * Description: 登录请求
 */
public class RequestLogin extends RequestBase {
	
	/** 手机号字段 */
	private final String PARAM_MOBILE = "mb";
	/** 密码字段 */
	private final String PARAM_PASSWORD = "pwd";
	/** 手机号 */
	private String mb;
	/** 密码 */
	private String pwd;
	
	public RequestLogin(String mb,String pwd){
		this.mb = mb;
		this.pwd = pwd;
		
		create();
	}
	
	/**
	 * 初始化请求
	 * @return
	 */
	public void create(){
		url = UrlConst.LOGIN;
		
		properties.put(PARAM_MOBILE, mb);
		properties.put(PARAM_PASSWORD, pwd);
	}
}
