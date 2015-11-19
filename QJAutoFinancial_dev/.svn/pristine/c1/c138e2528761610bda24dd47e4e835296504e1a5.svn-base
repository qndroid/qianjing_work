package com.qianjing.finance.net.request;

import com.google.gson.Gson;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.util.TestUtil;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Hashtable;

public class RequestManager
{

	public static synchronized <T> void requestCommon(final Context context, String strUrl,
			Hashtable<String, Object> tableProperty, final Class<T> classOfT, final XCallBack callback)
	{

		ViewUtil.showLoading();

		AnsynHttpRequest.requestByPost(context, strUrl, new HttpCallBack()
		{
			@Override
			public void back(final String strResponse, final int statusCode)
			{

				new Handler(context.getMainLooper()).post(new Runnable()
				{
					@Override
					public void run()
					{

						ViewUtil.dismissLoading();

						// 判断网络状态
						if (statusCode == HttpConst.STATUS_ERROR_NETWORK)
						{

							ViewUtil.showToast("网络不给力");

							callback.fail();
							return;
						}

						String strTemp = strResponse;
						int status = statusCode;

						// 判断返回数据
						// if (statusCode != HttpConst.STATUS_OK) {
						// callback.fail();
						// return;
						// }

						// 解析返回数据
						try
						{
							Log.e("--------->", strResponse);
							BaseModel model = (BaseModel) parseJson(context, strResponse, classOfT);
							callback.success(model);
						}
						catch (JSONException e)
						{

							callback.fail();
						}
						catch (Exception e)
						{

							callback.fail();
						}
					}
				});
			}
		}, tableProperty);

	}

	private static synchronized <T> T parseJson(Context context, String strJson, Class<T> classOfT)
			throws JSONException
	{

//		strJson = TestUtil.getJson(context);

		T target = null;
		Gson gson = new Gson();
		target = gson.fromJson(strJson, classOfT);
		return target;
	}

	public static void requestCommon(final Context context, RequestBase requestBase, final IHandleBase iHandle)
	{

		if (context instanceof Activity)
			ViewUtil.showLoading();

		AnsynHttpRequest.requestByPost(context, requestBase.url, new HttpCallBack()
		{
			@Override
			public void back(final String strResponse, final int statusCode)
			{

				new Handler(context.getMainLooper()).post(new Runnable()
				{
					@Override
					public void run()
					{

						if (context instanceof Activity && strResponse != null)
							ViewUtil.dismissLoading();

						if (strResponse != null)
						{
							ResponseBase responseBase = ResponseManager.getInstance().parse(strResponse);
							iHandle.handleResponse(responseBase, HttpConst.STATUS_OK);
						}
						else iHandle.handleResponse(null, statusCode);
					}
				});
			}
		}, requestBase.properties);
	}

}
