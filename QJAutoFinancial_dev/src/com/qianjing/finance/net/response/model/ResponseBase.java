package com.qianjing.finance.net.response.model;

import org.json.JSONObject;


/**
 * <p>Title: ResponseBase</p>
 * <p>Description: 返回报文结构</p>
 * @author fangyan
 * @date 2015年3月9日
 */
public class ResponseBase {
	
	/** 错误码 */
	public int ecode;
	
	/** 错误信息 */
	public String strError;
	
	/** 数据 */
	public JSONObject jsonObject;
	
}
