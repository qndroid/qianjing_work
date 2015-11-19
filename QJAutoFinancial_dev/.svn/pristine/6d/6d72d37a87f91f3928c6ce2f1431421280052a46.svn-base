
package com.qianjing.finance.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qianjing.finance.ui.Const;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

public class ViewUtil {

	/** 跳转到组合名称的标示名称 */
	public static final String ASSEMBLE_NAME_FLAG = "ASSEMBLE_NAME_FLAG";

	/** 以保存方式进入组合名称页进行保存 */
	public static final int SCHEMA_NAME_SAVE = 0;

	/** 以修改方式进入组合名称页进行修改 */
	public static final int ASSEMBLE_NAME_MODIFY = 1;

	/** 以保存购买方式进入组合名称页进行保存 */
	public static final int ASSEMBLE_NAME_SAVE = 2;

	/** 以虚拟试投方式进入组合名称页进行保存 */
	public static final int ASSEMBLE_VIRTUAL_NAME_SAVE = 3;

	/** 跳转到申购金额页的标示名称 */
	public static final String ASSEMBLE_BUY_FLAG = "ASSEMBLE_BUY_FLAG";

	/** 组合以初次投资方式进入申购金额页 */
	public static final int ASSEMBLE_BUY = 0;

	/** 组合以追加投资方式进入申购金额页 */
	public static final int ASSEMBLE_ADD_BUY = 1;

	/** 组合以复制虚拟组合的方式进入申购金额页 */
	public static final int ASSEMBLE_COPY_VIRTUAL_BUY = 2;

	/** 虚拟组合以初次投资方式进入申购金额页 */
	public static final int ASSEMBLE_VIRTUAL_BUY = 3;

	/** 虚拟组合以追加投资方式进入申购金额页 */
	public static final int ASSEMBLE_VIRTUAL_ADD_BUY = 4;

	/** 返回我的钱景交易明细的返回标示名称 */
	public static final String RESPONSE_TRADE = "RESPONSE_TRADE";

	/** 返回我的钱景交易明细的返回标示 */
	public static final int TRADE_FLAG = 1;

	/** 跳转到登陆页的标示名称 */
	public static final String LOGIN_FLAG = "LOGIN_FLAG";

	/** 从发现进入登录页 */
	public static final int LOGIN_FIND_SAVE = 0;

	/** 点击申购但是还没登录时进入登录页 */
	public static final int LOGIN_BUY = 1;

	/** 修改密码后进入登录页进行登录 */
	public static final int LOGIN_MODIFY_PASS = 2;

	/** 跳转时默认标识id */
	public static final int REQUEST_CODE = 10;

	/** 返回时默认标识id */
	public static final int RESULT_CODE = 11;

	/** 需要特殊处理的返回值 */
	public static final int SPECIAL_RESULT_CODE = 12;

	/** 返回推荐基金标签 */
	public static final int FIND_RESULT_CODE = 13;

	/** 返回全部基金标签 */
	public static final int MYASSETS_RESULT_CODE = 14;

	/** 申购完成返回值 */
	public static final int ASSEMBLE_BUY_RESULT_CODE = 15;

	/** 追加申购完成返回值 */
	public static final int ASSEMBLE_ADD_BUY_RESULT_CODE = 16;

	/** 赎回完成返回值 */
	public static final int ASSEMBLE_REDEEM_RESULT_CODE = 17;

	/** 更新组合的返回值 */
	public static final int ASSEMBLE_UPDATE_RESULT_CODE = 18;

	/** 删除组合的返回值 */
	public static final int ASSEMBLE_DELETE_RESULT_CODE = 19;

	/** 返回我的钱景标签 展示持仓明细 */
	public static final int SET_RESULT_CODE = 20;

	/** 跳转到绑卡提示页标示 */
	public static String BOUND_REQUEST = "BOUND_REQUEST";

	/** 从推荐基金页进入绑卡界面 */
	public static final int BOUND_REQUEST_RECOMMEND = 21;

	/** 从基金列表中的详情页进入绑卡界面 */
	public static final int BOUND_REQUEST_FUND = 22;

	/** 返回登录标签 */
	public static final int LOGIN_RESULT_CODE = 23;

	/** 用户信息修改返回码 */
	public static final int USER_INFO_MODIFY_CODE = 24;

	/** 定投申购完成返回值 */
	public static final int ASSEMBLE_AIP_BUY_RESULT_CODE = 25;

	/** 基本组合实例 */
	public static final String FLAG_EXTRA_BEAN_SCHEME_BASE = "AssembleBase";

	/** 组合详情实例 */
	public static final String FLAG_EXTRA_BEAN_SCHEME_DETAIL = "ShemeDetail";

	/** 跳转到网页的标示 */
	public static String TO_WEB_FLAG = "TO_WEB_FLAG";

	/** 从注册到网页 */
	public static final int TO_WEB_REGISTER = 1;

	/** 从绑卡到网页 */
	public static final int TO_WEB_BOUND = 2;

	/** 哪种类型 */
	public static final String WITCH_TYPE = "WITCH_TYPE";

	/** 标示主界面的哪个标签 */
	public static final String MAIN_TAB = "MAIN_TAB";

	/** 主界面 快速理财 */
	public static final int MAIN_TAB_QUICK = 0;

	/** 主界面 发现 */
	public static final int MAIN_TAB_FIND = 1;

	/** 主界面 我的资产 */
	public static final int MAIN_TAB_ASSETS = 2;

	/** 主界面 设置 */
	public static final int MAIN_TAB_SET = 3;

	/** 进入申购界面 是否有银行卡 */
	public static final String TO_BUY_CARD = "TO_BUY_CARD";

	/** 进入申购界面 有银行卡 */
	public static final int TO_BUY_HAS_CARD = 0;

	/** 进入申购界面 无银行卡 */
	public static final int TO_BUY_NO_CARD = 1;

	/** 进入组合详情页 */
	public static final String FLAG_ASSEMBLE_DETAIL = "FLAG_ASSEMBLE_DETAIL";

	/** 进入组合详情页 从我的资产进入 */
	public static final int FROM_ASSETS = 0;

	/** 进入组合详情页 从发现进入 */
	public static final int FROM_FIND = 1;

	/** 进入组合目标 */
	public static final String TO_SCHEMA_GOAL = "TO_SCHEMA_GOAL";

	/** 进入组合目标 从修改组合进入 */
	public static final int TO_SCHEMA_GOAL_MODIFY = 1;

	/**
	 * 配置基金颜色等级
	 */
	public static final int[] ARRAY_FUND_COLOR = new int[] { R.color.color_5ba7e1, R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7,
			R.color.color_a6d0e6, R.color.color_b19ee0, R.color.color_5a79b7 };

	/**
	 * 设置基金列表全高
	 */
	public static void setListViewFullHeight(ListView lv, BaseAdapter adapter) {
		int h = 0;
		final int cnt = adapter.getCount();
		for (int i = 0; i < cnt; i++) {
			View item = adapter.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = h + (lv.getDividerHeight() * (cnt - 1));
		lv.setLayoutParams(lp);
	}

	public static int[] stateStr = { R.string.submit, R.string.purchasing, R.string.redeeming, R.string.success, R.string.fail, R.string.cancel_order };

	public static String getStateStr(ArrayList<Integer> fdstates) {

		boolean success = false;
		boolean fail = false;

		for (Integer integer : fdstates) {
			if (integer == 3) {
				success = true;
			} else if (integer == 4) {
				fail = true;
			}
		}

		if (success && fail) {
			return "部分";
		}
		return "";
	}

	public static String getCurrentState(boolean flag, Context context, int state, int operate, ArrayList<Integer> fdstates) {

		String str = "";

		switch (state) {
		case 0:
		case 1:
		case 2:
			if (flag) {
				if (!"".equals(getStateStr(fdstates))) {
					str += "部分";
				}
			}
			break;
		case 3:
		case 4:
			if (!"".equals(getStateStr(fdstates))) {
				str += "部分";
			}
			if (operate == 1) {
				return str + "申购" + context.getString(stateStr[state]);
			} else {
				return str + "赎回" + context.getString(stateStr[state]);
			}
		}
		return str + context.getString(stateStr[state]);
	}

	public static String getFundState(Context context, String state, String operate) {

		int stateI = Integer.parseInt(state.trim());

		switch (stateI) {
		case 0:
		case 1:
		case 2:
			break;
		case 3:
		case 4:
			if ("1".equals(operate)) {
				return "申购" + context.getString(stateStr[stateI]);
			} else {
				return "赎回" + context.getString(stateStr[stateI]);
			}
		}
		return context.getString(stateStr[stateI]);
	}

	public static float getEnsureValue(ArrayList<Float> fdsum, ArrayList<Integer> fdstate) {

		float sum = 0;

		for (int i = 0; i < fdsum.size(); i++) {
			if (fdstate.get(i) != 4) {
				sum += fdsum.get(i);
			}
		}
		return sum;
	}

	/**
	 * 获取银行卡icon
	 * 
	 * @param bankName
	 * @return
	 */
	public static int getBankImageByName(Context context, String bankName) {
		String[] mBankNameArray = context.getResources().getStringArray(R.array.bank_name);
		for (int i = 0; i < mBankNameArray.length; i++) {
			if (mBankNameArray[i].equals(bankName)) {
				return i;
			}
		}
		return mBankNameArray.length;
	}

	/** 
	* @description 获取银行卡尾号 
	* @author fangyan 
	* @param strNum
	* @return
	*/
	public static String getCardTail(String strNum) {
		String strTail = "";
		if (!StringHelper.isBlank(strNum) && strNum.length() > 4) {
			strTail = strNum.substring(strNum.length() - 4, strNum.length());
		}
		return strTail;
	}

	/**
	 * @description 根据组合类型获取组合名称
	 * @author fangyan
	 * @param context
	 * @param type
	 * @return
	 */
	public static String getPortfolioName(Context context, int type) {

		String strPort = "组合";
		if (type == Const.ASSEMBLE_INVEST)
			return context.getString(R.string.title_assemble_invest) + strPort;
		else if (type == Const.ASSEMBLE_PENSION)
			return context.getString(R.string.title_assemble_pension) + strPort;
		else if (type == Const.ASSEMBLE_HOUSE)
			return context.getString(R.string.title_assemble_house) + strPort;
		else if (type == Const.ASSEMBLE_CHILDREN)
			return context.getString(R.string.title_assemble_children) + strPort;
		else if (type == Const.ASSEMBLE_MARRY)
			return context.getString(R.string.title_assemble_marry) + strPort;
		else if (type == Const.ASSEMBLE_DREAM)
			return context.getString(R.string.title_assemble_dream) + strPort;
		return strPort;
	}

}
