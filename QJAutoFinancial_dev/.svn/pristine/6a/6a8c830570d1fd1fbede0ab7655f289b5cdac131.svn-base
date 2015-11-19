package com.qianjing.finance.net.helper;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.ihandle.IHandleError;
import com.qianjing.finance.net.ihandle.IHandleFundSearch;
import com.qianjing.finance.net.ihandle.IHandleFundSearchLatestUpdate;
import com.qianjing.finance.net.request.RequestManager;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseFundSearch;
import com.qianjing.finance.net.response.model.ResponseFundSearchLatestUpdate;


/**
 * <p>Title: RequestFundHelper</p>
 * <p>Description: 基金网络数据请求</p>
 * @author fangyan
 * @date 2015年6月23日
 */
public class RequestFundHelper {

	/**
	 * <p>Title: requestFundSearch</p>
	 * <p>Description: 基金全量搜索请求</p>
	 * @param currentActivity 		当前界面
	 * @param IHandleFundSearch		返回数据接口
	 */
	public static void requestFundSearch(final Context context, final IHandleFundSearch iHandle) {
		requestFundSearch(context, iHandle);
	}

	/**
	 * <p>Title: requestFundSearch</p>
	 * <p>Description: 基金全量搜索请求</p>
	 * @param currentActivity 		当前界面
	 * @param IHandleFundSearch		返回数据接口
	 */
	public static void requestFundSearch(final Context context, final IHandleFundSearch iHandle, final IHandleError iHandleError) {
		
		RequestBase requestBase = new RequestBase(UrlConst.FUND_SEARCH);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {
				
				if (responseBase == null) {
					if (iHandleError != null)
						iHandleError.handleError(state);
					return;
				}
				
				ResponseFundSearch responseFundSearch = new ResponseFundSearch();
				try {
					responseFundSearch.listFund = ParseUtil.parseFundSearch(responseBase);
					if (responseFundSearch.listFund != null)
						iHandle.handleResponse(responseFundSearch);
					else {
						if (context instanceof Activity)
							ViewUtil.showToast(responseBase.strError);
					}
				} 
				catch (JSONException e) {
					
					if (iHandleError != null)
						iHandleError.handleError(HttpConst.STATUS_EXCEPTION_JSON_PARSE);
					
					if (context instanceof Activity && iHandleError!=null)
						ViewUtil.showToast("json parse error");
					
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>Title: reqeustFundSearchLatestUpdateTime</p>
	 * <p>Description: 基金搜索数据最新更新时间请求</p>
	 * @param currentActivity		当前界面
	 * @param iHandle				返回数据接口
	 */
	public static void reqeustFundSearchLatestUpdate(final Context context, final IHandleFundSearchLatestUpdate iHandle) {
		reqeustFundSearchLatestUpdate(context, iHandle);
	}
	
	/**
	 * <p>Title: reqeustFundSearchLatestUpdateTime</p>
	 * <p>Description: 基金搜索数据最新更新时间请求</p>
	 * @param currentActivity		当前界面
	 * @param iHandle				返回数据接口
	 */
	public static void reqeustFundSearchLatestUpdate(final Context context, final IHandleFundSearchLatestUpdate iHandle, final IHandleError iHandleError) {
		
		RequestBase requestBase = new RequestBase(UrlConst.FUND_LATESAT_UPDATE);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) {
					if (iHandleError != null)
						iHandleError.handleError(state);
					return;
				}
				
				ResponseFundSearchLatestUpdate responseFundSearchLatestUpdate = new ResponseFundSearchLatestUpdate();

				try {
					responseFundSearchLatestUpdate.latestTime = ParseUtil.parseFundSearchLatestUpdate(responseBase);
					if (responseFundSearchLatestUpdate.latestTime != -1)
						iHandle.handleResponse(responseFundSearchLatestUpdate);
					else {
						if (context instanceof Activity)
							ViewUtil.showToast(responseBase.strError);
					}
				} catch (JSONException e) {
					
					if (iHandleError != null)
						iHandleError.handleError(HttpConst.STATUS_EXCEPTION_JSON_PARSE);
					
					if (context instanceof Activity)
						ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
		
	}
	
	
}