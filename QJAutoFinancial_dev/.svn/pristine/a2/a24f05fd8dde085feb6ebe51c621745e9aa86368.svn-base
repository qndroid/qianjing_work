package com.qianjing.finance.net.response;

import org.json.JSONException;
import org.json.JSONObject;

import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.util.WriteLog;


/**
 * <p>Title: ResponseManager</p>
 * <p>Description: 默认返回数据解析封装处理</p>
 * @author fangyan
 * @date 2015年3月9日
 */
public class ResponseManager {

	private static ResponseManager instance;
	
	private ResponseManager() {}
	
	public static ResponseManager getInstance() {
		if (instance == null) {
			instance = new ResponseManager();
		}
		return instance;
	}
	
	/** 
	 * 简单请求返回 解析
	 * @param response 网络返回数据
	 * @return 默认请求返回报文
	 */
	public final ResponseBase parse(String response) {
		ResponseBase responseBase = null;
		JSONObject jsonObject;
		try {
			responseBase = new ResponseBase();
			jsonObject = new JSONObject(response);
			responseBase.ecode = jsonObject.getInt("ecode");
			responseBase.strError = jsonObject.getString("emsg");
			responseBase.jsonObject = jsonObject.optJSONObject("data");
		} 
		catch (JSONException e) {
			e.printStackTrace();
        	WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + response);
		}
		return responseBase;
	}
	
}
