package com.qianjing.finance.net.helper;

import org.json.JSONException;

import android.app.Activity;

import com.qianjing.finance.net.connection.HttpConst;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.ihandle.IHandleAssembleAssets;
import com.qianjing.finance.net.ihandle.IHandleAssembleDetail;
import com.qianjing.finance.net.ihandle.IHandleAssembleList;
import com.qianjing.finance.net.ihandle.IHandleAssembleMinLimit;
import com.qianjing.finance.net.ihandle.IHandleAssembleUpdate;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.request.RequestManager;
import com.qianjing.finance.net.request.model.RequestAssembleDetail;
import com.qianjing.finance.net.request.model.RequestAssembleList;
import com.qianjing.finance.net.request.model.RequestAssembleMinLimit;
import com.qianjing.finance.net.request.model.RequestAssembleUpdate;
import com.qianjing.finance.net.request.model.RequestBase;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.ViewUtil;
import com.qianjing.finance.net.response.model.ResponseAssembleAssets;
import com.qianjing.finance.net.response.model.ResponseAssembleDetail;
import com.qianjing.finance.net.response.model.ResponseAssembleList;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;


/**
 * <p>Title: RequestAssembleHelper</p>
 * <p>Description: 组合网络数据请求</p>
 * @author fangyan
 * @date 2015年6月23日
 */
public class RequestAssembleHelper {

	
	/**
	 * <p>Title: requestAssembleAssets</p>
	 * <p>Description: 组合总资产请求</p>
	 * @param currentActivity	当前界面
	 * @param iHandle			返回数据接口
	 */
	public static void requestAssembleAssets(Activity currentActivity, final IHandleAssembleAssets iHandle) {
		
		RequestBase requestBase = new RequestBase(UrlConst.CAL_ASSETS);
		
		RequestManager.requestCommon(currentActivity, requestBase, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {
				
				if (responseBase == null) return;
				
				ResponseAssembleAssets responseAssembleAssets = new ResponseAssembleAssets();
				try {
					responseAssembleAssets.assets = ParseUtil.parseAssembleAssets(responseBase);
					if (responseAssembleAssets.assets != null)
						iHandle.handleResponse(responseAssembleAssets);
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
	 * <p>Title: requestAssembleList</p>
	 * <p>Description: 持仓组合列表请求</p>
	 * @param currentActivity	当前界面
	 * @param startNumber		数据开始位置
	 * @param pageNumber		列表页数
	 * @param iHandle			返回数据接口
	 */
	public static void requestAssetsAssembleList(Activity currentActivity, int startNumber, int pageNumber, final IHandleAssembleList iHandle) {

		RequestBase requestAssembleList = new RequestAssembleList(startNumber, pageNumber, RequestAssembleList.TYPE_ASSETS);
		
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
     * <p>Title: requestNoAssetsAssembleList</p>
     * <p>Description: 未持仓组合列表请求</p>
     * @param currentActivity   当前界面
     * @param startNumber       数据开始位置
     * @param pageNumber        列表页数
     * @param iHandle           返回数据接口
     */
    public static void requestNoAssetsAssembleList(Activity currentActivity, int startNumber, int pageNumber, final IHandleAssembleList iHandle) {

        RequestBase requestAssembleList = new RequestAssembleList(startNumber, pageNumber, RequestAssembleList.TYPE_NO_ASSETS);
        
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
	 * <p>Title: requestAssembleDetail</p>
	 * <p>Description: 组合详情请求</p>
	 * @param currentActivity		当前界面
	 * @param strSid				组合ID
	 * @param iHandle				返回数据接口
	 */
	public static void requestAssembleDetail(Activity currentActivity, String strSid, final IHandleAssembleDetail iHandle) {

		RequestBase requestAssembleDetail = new RequestAssembleDetail(strSid);
		
		RequestManager.requestCommon(currentActivity, requestAssembleDetail, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int status) {

				if (responseBase == null) return;
				ViewUtil.showToast("解析");
				if (status != HttpConst.STATUS_OK) {
					ViewUtil.showToast("错误码-" + status);
					return;
				}
				
				ResponseAssembleDetail responseAssembleDetail = new ResponseAssembleDetail();
				try {
					responseAssembleDetail.detail = ParseUtil.parseAssembleDetail(responseBase);
					if (responseAssembleDetail.detail != null)
						iHandle.handleResponse(responseAssembleDetail);
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
	 * <p>Title: requestAssembleInvestUpdate</p>
	 * <p>Description: 更新理财增值组合请求</p>
	 * @param currentActivity	当前界面
	 * @param strName			组合名称
	 * @param strInitSum		申购额
	 * @param risk				风险值
	 * @param iHandle			返回数据接口
	 */
	public static void requestAssembleInvestUpdate(Activity currentActivity, String strSid, String strName, String strInitSum, int risk, final IHandleAssembleUpdate iHandle) {
		requestAssembleUpdate(currentActivity, strSid, Const.ASSEMBLE_INVEST, strName, strInitSum, risk, 0, 0, "", "", 0, iHandle);
	}

	/**
	 * <p>Title: requestAssembleUpdate</p>
	 * <p>Description: 更新组合请求</p>
	 * @param currentActivity	当前界面
	 * @param type				组合类型
	 * @param strName			组合名称
	 * @param strInitSum		申购额
	 * @param risk				风险值
	 * @param currentAge		年龄
	 * @param retireAge			退休年龄
	 * @param strMonthAmount	每月定投数额
	 * @param strGoalSum		目标数额
	 * @param years				投资年数
	 * @param iHandle			返回数据接口
	 */
	public static void requestAssembleUpdate(Activity currentActivity, String strSid, int type, String strName, String strInitSum, int risk, 
			int currentAge, int retireAge, String strMonthAmount, String strGoalSum, int years, final IHandleAssembleUpdate iHandle) {
		
		RequestBase request = new RequestAssembleUpdate(strSid, type, strName, strInitSum, 0, risk, currentAge, retireAge, strMonthAmount, strGoalSum, years);

		RequestManager.requestCommon(currentActivity, request, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) return;
				
				ResponseAssembleDetail responseAssembleDetail = new ResponseAssembleDetail();
				try {
					responseAssembleDetail.detail = ParseUtil.parseAssembleDetail(responseBase);
					if (responseAssembleDetail.detail != null)
						iHandle.handleResponse(responseAssembleDetail);
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
	 * <p>Title: requestAssembleInvestMinLimit</p>
	 * <p>Description: 理财增值组合最小申购限额请求</p>
	 * @param currentActivity	当前界面
	 * @param strInitSum		申购额
	 * @param risk				风险值
	 * @param iHandle			返回数据接口
	 */
	public static void requestAssembleInvestMinLimit(Activity currentActivity, String strInitSum, int risk, final IHandleAssembleMinLimit iHandle) {
		requestAssembleMinLimit(currentActivity, Const.ASSEMBLE_INVEST, strInitSum, risk, 0, 0, "", "", 0, iHandle);
	}
	
	/**
	 * <p>Title: requestAssembleMinLimit</p>
	 * <p>Description: 组合最小申购限额请求</p>
	 * @param currentActivity	当前界面
	 * @param type				组合类型
	 * @param strInitSum		申购额
	 * @param risk				风险值
	 * @param currentAge		年龄
	 * @param retireAge			退休年龄
	 * @param strMonthAmount	每月定投数额
	 * @param strGoalSum		目标数额
	 * @param years				投资年数
	 * @param iHandle			返回数据接口
	 */
	private static void requestAssembleMinLimit(Activity currentActivity, int type, String strInitSum, int risk, 
			int currentAge, int retireAge, String strMonthAmount, String strGoalSum, int years, final IHandleAssembleMinLimit iHandle) {
		
		RequestBase request = new RequestAssembleMinLimit(type, "", strInitSum, 0, risk, currentAge, retireAge, strMonthAmount, strGoalSum, years);
		
		RequestManager.requestCommon(currentActivity, request, new IHandleBase() {
			@Override
			public void handleResponse(ResponseBase responseBase, int state) {

				if (responseBase == null) return;
				
				ResponseAssembleDetail responseAssembleDetail = new ResponseAssembleDetail();
				try {
					responseAssembleDetail.detail = ParseUtil.parseAssembleDetail(responseBase);
					if (responseAssembleDetail.detail != null)
						iHandle.handleResponse(responseAssembleDetail);
					else
						ViewUtil.showToast(responseBase.strError);
				} catch (JSONException e) {
					ViewUtil.showToast("json parse error");
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
}