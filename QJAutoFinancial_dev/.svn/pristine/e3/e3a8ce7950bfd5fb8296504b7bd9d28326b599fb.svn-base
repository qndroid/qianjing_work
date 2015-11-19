package com.qianjing.finance.net.helper;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.ihandle.IHandleAssembleList;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.ihandle.IHandleError;
import com.qianjing.finance.net.ihandle.IHandleRebalanceDetail;
import com.qianjing.finance.net.ihandle.IHandleRebalanceHistoryDetail;
import com.qianjing.finance.net.ihandle.IHandleRebalanceHoldingCompare;
import com.qianjing.finance.net.request.RequestManager;
import com.qianjing.finance.net.request.model.RequestAssembleList;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.request.model.RequestRebalanceClose;
import com.qianjing.finance.net.request.model.RequestRebalanceDetail;
import com.qianjing.finance.net.request.model.RequestRebalanceHistoryDetail;
import com.qianjing.finance.net.request.model.RequestRebalanceHoldingCompare;
import com.qianjing.finance.net.request.model.RequestRebalanceReply;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseAssembleList;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseRebalanceDetail;
import com.qianjing.finance.net.response.model.ResponseRebalanceHistoryDetail;
import com.qianjing.finance.net.response.model.ResponseRebalanceHoldingCompare;

/**
 * <p>Title: RequestRebalanceHelper</p>
 * <p>Description: 再平衡网络数据请求</p>
 * @author fangyan
 * @date 2015年6月23日
 */
public class RequestRebalanceHelper {
	

	/**
	 * <p>Title: requestAssembleList</p>
	 * <p>Description: 再平衡组合列表请求</p>
	 * @param currentActivity				当前界面
	 * @param startNumber					数据开始位置
	 * @param pageNumber					列表页数
	 * @param iHandle						返回数据接口
	 */
	public static void requestAssembleList(Activity currentActivity, int startNumber, int pageNumber, final IHandleAssembleList iHandle) {

		RequestBase requestAssembleList = new RequestAssembleList(startNumber, pageNumber, RequestAssembleList.TYPE_REBALANCE);
		
		RequestManager.requestCommon(currentActivity, requestAssembleList, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) return;
				
				ResponseAssembleList responseAssembleList = new ResponseAssembleList();
				try {
					responseAssembleList.listAssemble = ParseUtil.parseAssembleList(responseBase);
					if (responseAssembleList.listAssemble != null)
						iHandle.handleResponse(responseAssembleList);
					else
						ViewUtil.showToast(responseBase.strError);
				} catch (JSONException e) {
					ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>Title: requestRebalanceClose</p>
	 * <p>Description: 关闭再平衡请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSid 						组合ID
	 * @param iHandle						返回数据接口
	 */
	public static void requestRebalanceClose(final Context context, String strSid, final IHandleBase iHandle) {
		
		RequestBase requestBase = new RequestRebalanceClose(strSid);
		RequestManager.requestCommon(context, requestBase, iHandle);
	}

	
	/**
	 * <p>Title: requestRebalanceHistoryDetail</p>
	 * <p>Description: 再平衡交易记录详情请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSopid 						交易记录ID
	 * @param IHandleRebalanceHistoryDetail	返回数据接口
	 */
	public static void requestRebalanceHistoryDetail(final Context context, String strSopid, final IHandleRebalanceHistoryDetail iHandle) {
		requestRebalanceHistoryDetail(context, strSopid, iHandle, null);
	}

	/**
	 * <p>Title: requestRebalanceHistoryDetail</p>
	 * <p>Description: 再平衡交易记录详情请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSopid 						交易记录ID
	 * @param IHandleRebalanceDetail		返回数据接口
	 */
	public static void requestRebalanceHistoryDetail(final Context context, String strSopid, final IHandleRebalanceHistoryDetail iHandle, final IHandleError iHandleError) {
		
		RequestBase requestBase = new RequestRebalanceHistoryDetail(strSopid);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) {
					if (iHandleError != null)
						iHandleError.handleError(state);
					return;
				}
				
				ResponseRebalanceHistoryDetail responseRebalanceHistoryDetail = new ResponseRebalanceHistoryDetail();
				responseRebalanceHistoryDetail.ecode = responseBase.ecode;
				responseRebalanceHistoryDetail.strError = responseBase.strError;
				try {
					responseRebalanceHistoryDetail.historyDetail = ParseUtil.parseRebalanceHistoryDetail(responseBase);
					if (responseRebalanceHistoryDetail.historyDetail != null)
						iHandle.handleResponse(responseRebalanceHistoryDetail);
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

	/**
	 * <p>Title: requestRebalanceHoldingCompare</p>
	 * <p>Description: 再平衡持仓对比请求</p>
	 * @param currentActivity 				当前界面
	 * @param strRsid 						组合ID
	 * @param IHandleRebalanceDetail		返回数据接口
	 */
	public static void requestRebalanceHoldingCompare(final Context context, String strRsid, final IHandleRebalanceHoldingCompare iHandle) {
		requestRebalanceHoldingCompare(context, strRsid, iHandle);
	}

	/**
	 * <p>Title: requestRebalanceHoldingCompare</p>
	 * <p>Description: 再平衡持仓对比请求</p>
	 * @param currentActivity 				当前界面
	 * @param strRsid 						组合ID
	 * @param IHandleRebalanceDetail		返回数据接口
	 * @param IHandleError					返回错误数据接口
	 */
	public static void requestRebalanceHoldingCompare(final Context context, String strRsid, final IHandleRebalanceHoldingCompare iHandle, final IHandleError iHandleError) {
		
		RequestBase requestBase = new RequestRebalanceHoldingCompare(strRsid);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) {
					if (iHandleError != null)
						iHandleError.handleError(state);
					return;
				}
				
				ResponseRebalanceHoldingCompare responseRebalanceHoldingCompare = new ResponseRebalanceHoldingCompare();
				responseRebalanceHoldingCompare.ecode = responseBase.ecode;
				responseRebalanceHoldingCompare.strError = responseBase.strError;
				try {
					responseRebalanceHoldingCompare.holdingCompare = ParseUtil.parseRebalanceHoldingCompare(responseBase);
					if (responseRebalanceHoldingCompare.holdingCompare != null)
						iHandle.handleResponse(responseRebalanceHoldingCompare);
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

	/**
	 * <p>Title: requestRebalanceDetail</p>
	 * <p>Description: 再平衡详情请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSid 						组合ID
	 * @param IHandleRebalanceDetail		返回数据接口
	 */
	public static void requestRebalanceDetail(final Context context, String strSid, final IHandleRebalanceDetail iHandle) {
		requestRebalanceDetail(context, strSid, iHandle, null);
	}

	/**
	 * <p>Title: requestRebalanceDetail</p>
	 * <p>Description: 再平衡详情请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSid 						组合ID
	 * @param IHandleRebalanceDetail		返回数据接口
	 * @param IHandleError					返回错误数据接口
	 */
	public static void requestRebalanceDetail(final Context context, String strSid, final IHandleRebalanceDetail iHandle, final IHandleError iHandleError) {
		
		RequestBase requestBase = new RequestRebalanceDetail(strSid);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) {
					if (iHandleError != null)
						iHandleError.handleError(state);
					return;
				}
				
				ResponseRebalanceDetail responseRebalanceDetail = new ResponseRebalanceDetail();
				responseRebalanceDetail.ecode = responseBase.ecode;
				responseRebalanceDetail.strError = responseBase.strError;
				try {
					responseRebalanceDetail.detail = ParseUtil.parseRebalanceDetail(responseBase);
					iHandle.handleResponse(responseRebalanceDetail);
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

	/**
	 * <p>Title: requestRebalanceRefuse</p>
	 * <p>Description: 拒绝再平衡请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSid 						组合ID
	 * @param strPass 						登录密码
	 * @param iHandle						返回数据接口
	 */
	public static void requestRebalanceRefuse(final Context context, String strSid, String strPass, final IHandleBase iHandle) {
		
		RequestBase requestBase = new RequestRebalanceReply(strSid, strPass, RequestRebalanceReply.TYPE_REFUSE);
		RequestManager.requestCommon(context, requestBase, iHandle);
	}
	
	/**
	 * <p>Title: requestRebalanceSubmit</p>
	 * <p>Description: 再平衡提交请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSid 						组合ID
	 * @param strPass 						登录密码
	 * @param IHandleRebalanceDetail		返回数据接口
	 */
	public static void requestRebalanceSubmit(final Context context, String strSid, String strPass, final IHandleRebalanceDetail iHandle) {
		
		RequestBase requestBase = new RequestRebalanceReply(strSid, strPass, RequestRebalanceReply.TYPE_SUBMIT);
		
		RequestManager.requestCommon(context, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) return;
				
				ResponseRebalanceDetail responseRebalanceDetail = new ResponseRebalanceDetail();
				try {
					responseRebalanceDetail = (ResponseRebalanceDetail) ParseUtil.parseRebalanceDetail2(responseBase);
					iHandle.handleResponse(responseRebalanceDetail);
				} catch (JSONException e) {
					if (context instanceof Activity)
						ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}
	
	
}