package com.qianjing.finance.net.helper;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.ihandle.IHandleActivityState;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.ihandle.IHandleLotteryStatus;
import com.qianjing.finance.net.ihandle.IHandleRedBagList;
import com.qianjing.finance.net.request.RequestManager;
import com.qianjing.finance.net.request.model.RequestAssembleRedBag;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.request.model.RequestFundRedBag;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseActivityState;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseLotteryStatus;
import com.qianjing.finance.net.response.model.ResponseRedBagList;

/**
 * <p>Title: RequestActivityHelper</p>
 * <p>Description: 运营活动网络数据请求</p>
 * @author fangyan
 * @date 2015年7月13日
 */
public class RequestActivityHelper
{

	/**
	 * <p>Title: requestAssembleRedBag</p>
	 * <p>Description: 组合申购使用红包请求</p>
	 * @param currentActivity 				当前界面
	 * @param strOpId						组合申购操作ID
	 * @param strId							红包ID
	 * @param IHandleBase					返回数据接口
	 */
	public static void requestAssembleRedBag(final Context context, String strOpId, String strId, IHandleBase iHandle)
	{

		RequestBase requestBase = new RequestAssembleRedBag(strOpId, strId);
		RequestManager.requestCommon(context, requestBase, iHandle);
	}

	/**
	 * <p>Title: requestFundRedBag</p>
	 * <p>Description: 基金申购使用红包请求</p>
	 * @param currentActivity 				当前界面
	 * @param strSoid						基金申购操作ID
	 * @param strId							红包ID
	 * @param IHandleBase					返回数据接口
	 */
	public static void requestFundRedBag(final Context context, String strOpId, String strId, IHandleBase iHandle)
	{

		RequestBase requestBase = new RequestFundRedBag(strOpId, strId);
		RequestManager.requestCommon(context, requestBase, iHandle);
	}

	/**
	 * <p>Title: requestRedBagList</p>
	 * <p>Description: 红包列表请求</p>
	 * @param currentActivity 				当前界面
	 * @param IHandleRedBagList				返回数据接口
	 */
	public static void requestRedBagList(final Context context, final IHandleRedBagList iHandle)
	{

		RequestBase requestBase = new RequestBase(UrlConst.ACTIVITY_REDBAG_LIST);

		RequestManager.requestCommon(context, requestBase, new IHandleBase()
		{
			@Override
			public void handleResponse(ResponseBase responseBase, int state)
			{
				if (responseBase == null) return;

				ResponseRedBagList responseRedBagList = new ResponseRedBagList();
				try
				{
					responseRedBagList.listRedBag = ParseUtil.parseRedBagList(responseBase);
					if (responseRedBagList.listRedBag != null)
						iHandle.handleResponse(responseRedBagList);
					else
					{
						if (context instanceof Activity)
							ViewUtil.showToast(responseBase.strError);
					}
				}
				catch (JSONException e)
				{
					if (context instanceof Activity)
						ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>Title: requestLotteryStatus</p>
	 * <p>Description: 获取抽奖资格请求</p>
	 * @param currentActivity 				当前界面
	 * @param IHandleActivityState			返回数据接口
	 */
	public static void requestLotteryStatus(final Context context, final IHandleLotteryStatus iHandle)
	{

		RequestBase requestBase = new RequestBase(UrlConst.ACTIVITY_LOTTEY_STATUS);

		RequestManager.requestCommon(context, requestBase, new IHandleBase()
		{
			@Override
			public void handleResponse(ResponseBase responseBase, int state)
			{
				if (responseBase == null) return;

				ResponseLotteryStatus responseLotteryStatus = new ResponseLotteryStatus();
				try
				{
					responseLotteryStatus.lottery = ParseUtil.parseLotteryStatus(responseBase);
					if (responseLotteryStatus.lottery != null)
						iHandle.handleResponse(responseLotteryStatus);
					else
					{
						if (context instanceof Activity)
							ViewUtil.showToast(responseBase.strError);
					}
				}
				catch (JSONException e)
				{
					if (context instanceof Activity)
						ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>Title: requestState</p>
	 * <p>Description: 获取活动状态请求</p>
	 * @param currentActivity 				当前界面
	 * @param IHandleActivityState			返回数据接口
	 */
	public static void requestState(final Context context, final IHandleActivityState iHandle)
	{
		RequestBase requestBase = new RequestBase(UrlConst.ACTIVITY_STATE);

		RequestManager.requestCommon(context, requestBase, new IHandleBase()
		{
			@Override
			public void handleResponse(ResponseBase responseBase, int state)
			{
				if (responseBase == null) return;

				ResponseActivityState responseActivityState = new ResponseActivityState();
				try
				{
					responseActivityState.listActivity = ParseUtil.parseActivityState(responseBase);
					if (responseActivityState.listActivity != null)
						iHandle.handleResponse(responseActivityState);
					else
					{
						if (context instanceof Activity)
							ViewUtil.showToast(responseBase.strError);
					}
				}
				catch (JSONException e)
				{
					if (context instanceof Activity)
						ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}
}