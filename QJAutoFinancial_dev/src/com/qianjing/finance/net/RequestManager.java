
package com.qianjing.finance.net;

import java.util.Hashtable;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;

/**
 * @description 网络请求管理类
 * @author fangyan
 * @date 2015年8月12日
 */
public class RequestManager {

	/**
	 * @description 发送网络请求
	 * @author fangyan
	 * @param context 调用组件
	 * @param strUrl 请求网络地址
	 */
	public static synchronized <T> void request(final Context context, String strUrl) {
		request(context, strUrl, null, null, null);
	}

	/**
	 * @description 请求网络数据
	 * @author fangyan
	 * @param context 调用组件
	 * @param strUrl 请求网络地址
	 * @param tableProperty 请求参数
	 * @param classOfT 返回数据类型
	 * @param callback 回调接口实例
	 * @param isLoading 是否显示加载框
	 */
	public static synchronized <T> void request(final Context context, String strUrl, Hashtable<String, Object> tableProperty,
			final Class<T> classOfT, final XCallBack callback) {

		AnsynHttpRequest.requestByPost(context, strUrl, new HttpCallBack() {
			@Override
			public void back(final String strResponse, final int statusCode) {

				if (callback == null)
					return;

				new Handler(context.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {

						// 没有网络状态或Socket超时
						if (statusCode == HttpConst.STATUS_ERROR_NETWORK || statusCode == HttpConst.STATUS_EXCEPTION_IO) {

							ViewUtil.showToast("网络不给力");

							callback.fail();
							return;
						}

						if (statusCode != HttpConst.STATUS_OK) {
							callback.fail();
							return;
						}

						// 解析返回数据
						BaseModel model = null;
						try {
							model = (BaseModel) parseJson(context, strResponse, classOfT);
							// 如果状态码返回字母，Integer.valueOf()方法会抛出异常
							model.stateCode = Integer.valueOf(model.strStateCode);
							callback.success(model);
						} catch (NumberFormatException e) {

							if (model != null && StringHelper.isNotBlank(model.strErrorMessage)) {
								model.stateCode = ErrorConst.EC_SYSERROR;
								callback.success(model);
							} else
								callback.fail();
						} catch (JSONException e) {

							ViewUtil.showToast("数据解析异常");
							callback.fail();
						} catch (Exception e) { // GSON反序列化抛出异常

							ViewUtil.showToast("数据解析异常");
							callback.fail();
						}
					}
				});
			}
		}, tableProperty);
	}

	/**
	 * @description 将JSON反序列化为对象
	 * @author fangyan
	 * @param context 调用组件
	 * @param strJson 需要解析的JSON
	 * @param classOfT 封装数据的实例类型
	 * @return 封装数据的实例
	 * @throws JSONException
	 */
	private static synchronized <T> T parseJson(Context context, String strJson, Class<T> classOfT) throws JSONException {

		T target = null;
		Gson gson = new Gson();
		target = gson.fromJson(strJson, classOfT);
		return target;
	}

}
