
package com.qianjing.finance.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.assemble.AssembleAssets;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.AssembleFixed;
import com.qianjing.finance.model.assemble.AssembleReminder;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.common.InformList.InformListKey.Information;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qjautofinancial.R;

/**
 * <p>Title: Common</p>
 * <p>Description: 常用函数</p>
 * @author fangyan
 * @date 2015年4月4日
 */
public class CommonUtil {

	public static final String KEY_BANK_NAME = "bankName";
	public static final String KEY_BANK_CODE = "bankCode";
	public static final String KEY_BANK_ICON = "bankIcon";

	public static int getBankIconId(String strBankName) {
		ArrayList<HashMap<String, Object>> LIST_BANK = getListBank();
		for (HashMap<String, Object> mapBank : LIST_BANK) {
			if (TextUtils.equals(strBankName, (String) mapBank.get(CommonUtil.KEY_BANK_NAME)))
				return (Integer) mapBank.get(CommonUtil.KEY_BANK_ICON);
		}
		return 0;
	}

	public static ArrayList<HashMap<String, Object>> getListBank() {
		ArrayList<HashMap<String, Object>> listBank = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "工商银行");
		map.put(KEY_BANK_ICON, R.drawable.gongshang);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "农业银行");
		map.put(KEY_BANK_ICON, R.drawable.nonghang);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "中国银行");
		map.put(KEY_BANK_ICON, R.drawable.zhongguo);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "建设银行");
		map.put(KEY_BANK_ICON, R.drawable.jianhang);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "交通银行");
		map.put(KEY_BANK_ICON, R.drawable.jiaotong);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "招商银行");
		map.put(KEY_BANK_ICON, R.drawable.zhaoshang);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "光大银行");
		map.put(KEY_BANK_ICON, R.drawable.guangda);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "浦发银行");
		map.put(KEY_BANK_ICON, R.drawable.pufa);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "兴业银行");
		map.put(KEY_BANK_ICON, R.drawable.xingye);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "民生银行");
		map.put(KEY_BANK_ICON, R.drawable.minsheng);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "中信银行");
		map.put(KEY_BANK_ICON, R.drawable.zhongxin);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "广发银行");
		map.put(KEY_BANK_ICON, R.drawable.guangfa);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "平安银行");
		map.put(KEY_BANK_ICON, R.drawable.pingan);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "邮政储蓄");
		map.put(KEY_BANK_ICON, R.drawable.youzheng);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "华夏银行");
		map.put(KEY_BANK_ICON, R.drawable.huaxia);
		listBank.add(map);

		map = new HashMap<String, Object>();
		map.put(KEY_BANK_NAME, "上海银行");
		map.put(KEY_BANK_ICON, R.drawable.shanghai);
		listBank.add(map);

		return listBank;
	}

	public static ArrayList<HashMap<String, Object>> getListP2PBank() {
		ArrayList<HashMap<String, Object>> listBank = new ArrayList<HashMap<String, Object>>();
		// 工商
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "01020000");
		map.put(KEY_BANK_ICON, R.drawable.gongshang);
		listBank.add(map);
		// 农业
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "01030000");
		map.put(KEY_BANK_ICON, R.drawable.nonghang);
		listBank.add(map);
		// 中国
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "01040000");
		map.put(KEY_BANK_ICON, R.drawable.zhongguo);
		listBank.add(map);
		// 建设
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "01050000");
		map.put(KEY_BANK_ICON, R.drawable.jianhang);
		listBank.add(map);
		// 光大
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03030000");
		map.put(KEY_BANK_ICON, R.drawable.guangda);
		listBank.add(map);
		// 浦发
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03100000");
		map.put(KEY_BANK_ICON, R.drawable.pufa);
		listBank.add(map);
		// 兴业
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03090000");
		map.put(KEY_BANK_ICON, R.drawable.xingye);
		listBank.add(map);
		// 民生
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03050000");
		map.put(KEY_BANK_ICON, R.drawable.minsheng);
		listBank.add(map);
		// 中信
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03020000");
		map.put(KEY_BANK_ICON, R.drawable.zhongxin);
		listBank.add(map);
		// 广发
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03060000");
		map.put(KEY_BANK_ICON, R.drawable.guangfa);
		listBank.add(map);
		// 平安
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03070000");
		map.put(KEY_BANK_ICON, R.drawable.pingan);
		listBank.add(map);
		// 邮政
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "01000000");
		map.put(KEY_BANK_ICON, R.drawable.youzheng);
		listBank.add(map);
		// 华夏
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03040000");
		map.put(KEY_BANK_ICON, R.drawable.huaxia);
		listBank.add(map);
		// 招商
		map = new HashMap<String, Object>();
		map.put(KEY_BANK_CODE, "03080000");
		map.put(KEY_BANK_ICON, R.drawable.zhaoshang);
		listBank.add(map);
		return listBank;
	}

	/**
	 * <p>Title: parseAssembleDetail</p>
	 * <p>Description: 解析组合详情JSON</p>
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static AssembleDetail parseAssembleDetail(String strJson) throws JSONException {

		AssembleDetail detail = new AssembleDetail(new AssembleBase(), new AssembleConfig(), new AssembleAssets(), new AssembleReminder(),new AssembleFixed());

		int type = 0;

		JSONObject objData = new JSONObject(strJson);
		JSONObject objSchema = objData.optJSONObject("schema");
		if (objSchema != null) {
			/** AssembleBase */
			AssembleBase assemble = detail.getAssembleBase();
			if (objSchema.has("uid"))
				assemble.setUid(objSchema.getString("uid"));
			if (objSchema.has("sid"))
				assemble.setSid(objSchema.getString("sid"));
			if (objSchema.has("name"))
				assemble.setName(objSchema.getString("name"));
			// limit
			if (objSchema.has("limit")) {

				if ("[]".equals(objSchema.optString("limit"))) {

				} else {
					String strLimit = objSchema.getString("limit");
					// limit节点下可能还有其他属性，这里只需要一个init属性
					JSONObject objLimit = new JSONObject(strLimit);
					assemble.setMinLimit(objLimit.optInt("init"));
				}

			}
			// type
			type = Integer.valueOf(objSchema.getString("type"));
			assemble.setType(type);
			if (type == Const.ASSEMBLE_INVEST) {
				assemble.setInvestInitSum(objSchema.optString("financial_init"));
				assemble.setInvestRate(objSchema.optString("financial_rate"));
				assemble.setInvestRisk(objSchema.optString("financial_risk"));
			} else if (type == Const.ASSEMBLE_PENSION) {
				assemble.setPensionInitSum(objSchema.optString("pension_init"));
				assemble.setPensionCurrentAge(objSchema.optString("pension_age"));
				assemble.setPensionMonthAmount(objSchema.optString("pension_month"));
				assemble.setPensionRetireAge(objSchema.optString("pension_retire"));
			} else if (type == Const.ASSEMBLE_HOUSE) {
				assemble.setHouseInitSum(objSchema.optString("house_init"));
				assemble.setHouseGoalSum(objSchema.optString("house_money"));
				assemble.setHouseYears(objSchema.optString("house_year"));
			} else if (type == Const.ASSEMBLE_CHILDREN) {
				assemble.setChildGoalSum(objSchema.optString("education_money"));
				assemble.setChildYears(objSchema.optString("education_year"));
			} else if (type == Const.ASSEMBLE_MARRY) {
				assemble.setMarryInitSum(objSchema.optString("married_init"));
				assemble.setMarryGoalSum(objSchema.optString("married_money"));
				assemble.setMarryYears(objSchema.optString("married_year"));
			} else if (type == Const.ASSEMBLE_DREAM) {
				assemble.setDreamInitSum(objSchema.optString("small_init"));
				assemble.setDreamMonths(objSchema.optString("small_nmonth"));
			} else if (type == Const.ASSEMBLE_SPESIAL) {
				assemble.setSpecialAge(objSchema.optString("financial2_age"));
				assemble.setSpecialInitSum(objSchema.optString("financial2_init"));
				assemble.setSpecialPref(objSchema.optString("financial2_preference"));
				assemble.setSpecialRisk(objSchema.optString("financial2_risk"));
				assemble.setSpecialRiskLevel(objSchema.optInt("financial2_risklevel"));
			}
		}

		/** AssembleConfig */
		AssembleConfig config = detail.getAssembleConfig();
		if (objData.has("assembly")) {
			String strAssembly = objData.getString("assembly");
			JSONObject objAssemb = new JSONObject(strAssembly);
			config.setUid(objAssemb.getString("uid"));
			config.setSid(objAssemb.getString("sid"));
			config.setSname(objAssemb.getString("sname"));
			config.setAssembT(String.valueOf(objAssemb.getLong("assembT")));
			config.setHgRatio(objAssemb.getString("hgRatio"));
			config.setMonth(objAssemb.getString("month"));
			config.setRisk(String.valueOf(objAssemb.getInt("risk")));
			config.setInit(String.valueOf(objAssemb.getDouble("init")));
			if (objAssemb.has("nostock_ratio"))
				config.setRatioNonStock(objAssemb.getDouble("nostock_ratio"));
			if (objAssemb.has("nostock_text"))
				config.setStrNonStock(objAssemb.getString("nostock_text"));
			if (objAssemb.has("stock_ratio"))
				config.setRatioStock(objAssemb.getDouble("stock_ratio"));
			if (objAssemb.has("stock_text"))
				config.setStrStock(objAssemb.getString("stock_text"));
			if (objAssemb.has("card"))
				config.setCard(objAssemb.getString("card"));
			if (objAssemb.has("bank"))
				config.setBank(objAssemb.getString("bank"));
			if (objAssemb.has("earnings"))
				config.setEarnings(objAssemb.getString("earnings"));

			List<String> listFundCode = config.getFdcodes();
			List<String> listFundName = config.getAbbrevs();
			List<String> listFundShareType = config.getSharetypes();
			List<String> listFundRatio = config.getRatios();
			JSONArray arrFdcodes = objAssemb.getJSONArray("fdcodes");
			JSONArray arrAbbrevs = objAssemb.getJSONArray("abbrevs");
			JSONArray arrShareTypes = objAssemb.getJSONArray("sharetypes");
			JSONArray arrRatios = objAssemb.getJSONArray("ratios");
			for (int i = 0; i < arrAbbrevs.length(); i++) {
				listFundCode.add(arrFdcodes.getString(i));
				listFundName.add(arrAbbrevs.getString(i));
				listFundShareType.add(arrShareTypes.getString(i));
				listFundRatio.add(String.valueOf(arrRatios.getDouble(i)));
			}
			setAssemFundList(detail);
		}

		/** AssembleAssets */
		AssembleAssets assets = detail.getAssembleAssets();
		if (objData.has("assets")) {
			String strAssets = objData.getString("assets");
			JSONObject objAssets = new JSONObject(strAssets);
			assets.setUid(objAssets.getString("uid"));
			assets.setSid(objAssets.getString("sid"));
			assets.setAssets(objAssets.getString("assets"));
			assets.setProfitYesday(objAssets.getString("profitYesday"));
			assets.setModitm(objAssets.getString("moditm"));
			assets.setUnpaid(objAssets.getString("unpaid"));
			assets.setInvest(objAssets.getString("invest"));
			assets.setIncome(objAssets.getString("income"));
			assets.setSubscripting(objAssets.getString("subscripting"));
			assets.setRedemping(objAssets.getString("redemping"));
			assets.setProfit(objAssets.getString("profit"));
		}

		/** AssembleReminder */

		return detail;
	}

	/**
	 * <p>
	 * Title: parseCardList
	 * </p>
	 * <p>
	 * Description: 解析用户银行卡列表JSON
	 * </p>
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<Card> parseCardList(JSONObject objData) throws JSONException {

		ArrayList<Card> listCard = new ArrayList<Card>();
		JSONArray cards = objData.optJSONArray("cards");
		for (int i = 0; i < cards.length(); i++) {
			Card card = new Card();
			JSONObject cardStr = cards.optJSONObject(i);
			card.setBankName(cardStr.optString("bank"));
			card.setBoundTime(cardStr.optLong("boundT"));
			card.setUid(cardStr.optString("uid"));
			card.setNumber(cardStr.optString("card"));
			card.setProvince(cardStr.optString("province"));
			card.setCity(cardStr.optString("city"));
			card.setMobile(cardStr.optString("mobile"));
			card.setIsUnbound(cardStr.optInt("isUnBC"));
			card.setLimitDailyRecharge(Double.valueOf(cardStr.optString("daily_limit")));
			card.setLimitRecharge(Double.valueOf(cardStr.optString("single_limit")));
			listCard.add(card);
		}
		JSONArray cards2 = objData.optJSONArray("unbc");
		for (int i = 0; i < cards2.length(); i++) {
			Card card = new Card();
			JSONObject cardStr = cards2.optJSONObject(i);
			card.setBankName(cardStr.optString("bank"));
			card.setBoundTime(cardStr.optLong("boundT"));
			card.setUid(cardStr.optString("uid"));
			card.setNumber(cardStr.optString("card"));
			card.setProvince(cardStr.optString("province"));
			card.setCity(cardStr.optString("city"));
			card.setMobile(cardStr.optString("mobile"));
			card.setIsUnbound(cardStr.optInt("isUnBC"));
			listCard.add(card);
		}
		return listCard;
	}

	/**
	 * <p>
	 * Title: parseAssembleDetail
	 * </p>
	 * <p>
	 * Description: 解析支持绑定银行卡列表JSON
	 * </p>
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<Card> parseSupportCardList(JSONObject objData) throws JSONException {

		ArrayList<Card> listCard = new ArrayList<Card>();
		JSONArray cards = objData.optJSONArray("cards");
		for (int i = 0; i < cards.length(); i++) {
			Card card = new Card();
			JSONObject cardStr = cards.optJSONObject(i);
			card.setBankName(cardStr.optString("bank"));
			card.setBankCode(cardStr.optString("bankcode"));
			card.setCapitalMode(cardStr.optString("capitalmode"));
			card.setLimitDailyRecharge(Double.valueOf(cardStr.optString("daily_limit")));
			card.setLimitRecharge(Double.valueOf(cardStr.optString("single_limit")));
			card.setBankState(cardStr.optInt("state"));
			listCard.add(card);
		}
		return listCard;
	}

	/**
	 * 赋值方案的基金列表数据
	 * 
	 * @param detail
	 */
	public static void setAssemFundList(AssembleDetail detail) {
		AssembleConfig assembInfo = detail.getAssembleConfig();
		List<String> fdCodes = assembInfo.getFdcodes();
		List<String> abbrevs = assembInfo.getAbbrevs();
		List<String> sharetypes = assembInfo.getSharetypes();
		List<String> ratios = assembInfo.getRatios();
		ArrayList<Fund> fundList = new ArrayList<Fund>();
		for (int i = 0; i < fdCodes.size(); i++) {
			Fund fund = new Fund();
			fund.code = fdCodes.get(i);
			fund.name = abbrevs.get(i);
			fund.shareType = sharetypes.get(i);
			fund.rate = Double.parseDouble(ratios.get(i));
			fund.color = ViewUtil.ARRAY_FUND_COLOR[i];
			fundList.add(fund);
		}
		assembInfo.setFundList(fundList);
	}

	/**
	 * 创建单条基金控件
	 * 
	 * @param activity
	 * @param fund
	 * @return
	 */
	public static AssembleNewItemLayout createFundItem(final Activity activity, final Fund fund) {
		AssembleNewItemLayout fundItem = new AssembleNewItemLayout(activity);
		fundItem.initData(fund.color, fund.name,
				activity.getString(R.string.left_brackets) + fund.code + activity.getString(R.string.right_brackets),
				StringHelper.formatString(String.valueOf(fund.rate * 100), StringFormat.FORMATE_1));
		fundItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, FundDetailsActivity.class);
				intent.putExtra("fdcode", fund.code);
				intent.putExtra("from_asemble", true);
				activity.startActivity(intent);
			}
		});
		return fundItem;
	}

	/** 
	* @description 计算组合配置列表中的单只基金申购金额
	* @author fangyan 
	* @param sum 组合总金额
	* @param radio 基金占比
	* @return
	*/
	public static String calFundMoney(double sum, double radio) {
		BigDecimal value = new BigDecimal(sum).multiply(new BigDecimal(radio));
		String money = StringHelper.formatString(String.valueOf(value), StringFormat.FORMATE_2);
		return money;
	}

	public static String getBuyState(ArrayList<FundResponseItem> list) {
		String stateStr = "";
		switch (getBuyStateCode(list)) {
		case 1:
			stateStr = "扣款成功";
			break;
		case 11:
			stateStr = "部分扣款成功";
			break;
		case 0:
			stateStr = "交易申请已受理";
			break;
		case 10:
			stateStr = "部分交易申请已受理";
			break;
		case 4:
			stateStr = "扣款失败";
			break;
		default:
			stateStr = "申购失败";
			break;
		}
		return stateStr;
	}

	public static int getBuyStateCode(ArrayList<FundResponseItem> list) {
		int[] arr = null;
		if (list != null && list.size() > 0) {
			arr = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = list.get(i).fdstate;
			}
		}
		return getResultCode(arr);
	}

	/*
	 * 申购获取状态码的处理 状态 0:提交 1:申购中 2:赎回中 3:成功 4:失败 5:撤单 申购结果页 交易记录和交易详情页 全是3 返回3 -- 申购成功 有一个3 返回13 --
	 * 部分申购成功 全是1 返回1 扣款成功 申购中 有一个1 返回11 部分扣款成功 部分申购中 全是0 返回0 交易申请已受理 受理中 有一个0 返回10 部分交易申请已受理 部分受理中
	 * 全是4 返回4 扣款失败 申购失败 其余情况 返回-1或者-2 申购失败 申购失败
	 */
	private static int getResultCode(int[] arr) {// 返回值为负数为获取状态码失败
		int flag = -1;
		int max = -1;
		int min = -1;
		String str = "";
		if (arr == null) {
			return flag;
		}
		for (int i = 0; i < arr.length; i++) {
			str = str + arr[i];
			if (i == 0) {// 赋数组第一个值
				flag = arr[i];
				max = arr[i];
				min = arr[i];
			}
			if (min > arr[i]) {// 记录最小值
				min = arr[i];
			}
			if (max < arr[i]) {//
				max = arr[i];
			}
		}
		if (min == max && max == 2) {
			flag = -2;
			return flag;
		}
		if (min == max) {

		} else {
			if (str.contains("3") && max >= 3) {
				flag = 13;
				return flag;
			}
			if (str.contains("1") && max >= 1) {
				flag = 11;
				return flag;
			}
			if (str.contains("0") && max >= 0) {
				flag = 10;
				return flag;
			}
			flag = -2;
			return flag;
		}
		if (flag < 5 && flag >= 0) {// 若不等式0<=flag<5不成立 flag置为-2；

		} else if (flag < 15 && flag >= 10) {

		} else {
			flag = -2;
		}
		return flag;
	}

	/**
	 * @description 检索是否有未读消息
	 * @author fangyan
	 */
	public static boolean hasUnread(List<Information> listInform) {
		if (listInform != null)
			for (Information inform : listInform) {
				if (PrefManager.getInstance().getInt("inform_" + inform.id, -1) != 0)
					return true;
			}
		return false;
	}

}
