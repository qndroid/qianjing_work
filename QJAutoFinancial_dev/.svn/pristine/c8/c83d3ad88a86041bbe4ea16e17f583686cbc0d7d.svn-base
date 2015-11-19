package com.qianjing.finance.net.helper;

import org.json.JSONException;

import android.app.Activity;

import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.ihandle.IHandleError;
import com.qianjing.finance.net.ihandle.IHandleLogin;
import com.qianjing.finance.net.request.RequestManager;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.request.model.RequestLogin;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseLogin;


/**
 * <p>Title: RequestCommonHelper</p>
 * <p>Description: 一般性网络数据请求</p>
 * @author fangyan
 * @date 2015年6月23日
 */
public class RequestCommonHelper {
	
		
	/**
	 * <p>Title: requestLogin</p>
	 * <p>Description: 登录请求</p>
	 * @param currentActivity	当前界面
	 * @param strMobile			用户名
	 * @param strPass			用户密码
	 * @param iHandleLogin		返回数据接口
	 */
	public static void requestLogin(Activity currentActivity, String strMobile, String strPass, final IHandleLogin iHandle) {
		requestLogin(currentActivity, strMobile, strPass, iHandle, null);
	}

	/**
	 * <p>Title: requestLogin</p>
	 * <p>Description: 登录请求</p>
	 * @param currentActivity	当前界面
	 * @param strMobile			用户名
	 * @param strPass			用户密码
	 * @param iHandle			返回数据接口
	 * @param iHandleError		返回异常数据接口
	 */
	public static void requestLogin(Activity currentActivity, String strMobile, String strPass, final IHandleLogin iHandle, final IHandleError iHandleError) {
		
		RequestBase requestLogin = new RequestLogin(strMobile, strPass);
		
		RequestManager.requestCommon(currentActivity, requestLogin, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) return;
				
				ResponseLogin responseLogin = new ResponseLogin();
				try {
					responseLogin.user = ParseUtil.parseUser(responseBase);
					if (responseLogin.user != null)
						iHandle.handleResponse(responseLogin);
					else {
						if (iHandleError != null)
							iHandleError.handleError(HttpConst.STATUS_ERROR_COMMON);
						else
							ViewUtil.showToast(responseBase.strError);
					}
				} catch (JSONException e) {
					ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}
		
		
	
	
	
	
	
}