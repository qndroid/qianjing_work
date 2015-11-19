package com.qianjing.finance.net.response;

import com.qianjing.finance.model.activity.Activity;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.activity.NewHandActivity;
import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.model.assemble.AssembleAssets;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.AssembleFixed;
import com.qianjing.finance.model.assemble.AssembleReminder;
import com.qianjing.finance.model.assemble.InvestPlan;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.fund.FundSearchModel;
import com.qianjing.finance.model.rebalance.RebalanceDetail;
import com.qianjing.finance.model.rebalance.RebalanceFund;
import com.qianjing.finance.model.rebalance.RebalanceHistoryDetail;
import com.qianjing.finance.model.rebalance.RebalanceHoldingCompare;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseRebalanceDetail;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ParseUtil {


	/**
	 * <p>Title: parseAssembleDetail</p>
	 * <p>Description: 解析组合详情</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static AssembleDetail parseAssembleDetail(ResponseBase responseBase) throws JSONException {
		
		if (responseBase == null) return null;

		if (responseBase.ecode == 0) {
			
			AssembleDetail detail = new AssembleDetail(new AssembleBase(), new AssembleConfig(), new AssembleAssets(), new AssembleReminder(),new AssembleFixed());
			
			int type = 0;
			
			JSONObject objData = responseBase.jsonObject;
			LogUtils.syso("组合详情数据:"+objData.toString());
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
				if (objSchema.has("balance_note_state"))
					assemble.setBalanceState(objSchema.getInt("balance_note_state"));
				if (objSchema.has("balance_operation_state"))
					assemble.setBalanceOpState(objSchema.getInt("balance_operation_state"));
				if (objSchema.has("sopid")) {
					String strSopid = objSchema.getString("sopid");
					if (!StringHelper.isBlank(strSopid) && !TextUtils.equals("0", strSopid))
						assemble.setSopid(strSopid);
				}
				
				
				
				if(objSchema.has("plan")){
				    JSONObject planObj = objSchema.optJSONObject("plan");
				    if(planObj.has("plan1") && !"[]".equals(planObj.optString("plan1")) 
				            && !"{}".equals(planObj.optString("plan1"))){
				        InvestPlan plan1 = new InvestPlan();
				        JSONObject plan1Obj = planObj.optJSONObject("plan1");
				        plan1.setIndex(1);
				        plan1.setExcept_max((float)plan1Obj.optDouble("except_max"));
				        plan1.setExcept_min((float)plan1Obj.optDouble("except_min"));
				        plan1.setExcept_profit((float)plan1Obj.optDouble("except_profit"));
				        plan1.setOne_invest((float)plan1Obj.optDouble("one_invest"));
				        assemble.setPlan1(plan1);
				    }
				    if(planObj.has("plan2") && !"[]".equals(planObj.optString("plan2"))
				            && !"{}".equals(planObj.optString("plan2"))){
				        InvestPlan plan2 = new InvestPlan();
				        JSONObject plan1Obj = planObj.optJSONObject("plan2");
                        plan2.setIndex(2);
				        plan2.setExcept_max((float)plan1Obj.optDouble("except_max"));
				        plan2.setExcept_min((float)plan1Obj.optDouble("except_min"));
				        plan2.setExcept_profit((float)plan1Obj.optDouble("except_profit"));
				        plan2.setMonth_fixed((float)plan1Obj.optDouble("month_fixed"));
				        plan2.setOne_invest((float)plan1Obj.optDouble("one_invest"));
				        plan2.setMonth_minsum((float)plan1Obj.optDouble("month_minsum"));
				        plan2.setInvest_year(plan1Obj.optInt("invest_year"));
				        assemble.setPlan2(plan2);
                    }
				    if(planObj.has("plan3") && !"[]".equals(planObj.optString("plan3"))
				            && !"{}".equals(planObj.optString("plan3"))){
				        InvestPlan plan3 = new InvestPlan();
				        JSONObject plan1Obj = planObj.optJSONObject("plan3");
                        plan3.setIndex(3);
				        plan3.setExcept_max((float)plan1Obj.optDouble("except_max"));
				        plan3.setExcept_min((float)plan1Obj.optDouble("except_min"));
				        plan3.setExcept_profit((float)plan1Obj.optDouble("except_profit"));
				        plan3.setMonth_fixed((float)plan1Obj.optDouble("month_fixed"));
                        plan3.setMonth_minsum((float)plan1Obj.optDouble("month_minsum"));
                        plan3.setInvest_year(plan1Obj.optInt("invest_year"));
				        assemble.setPlan3(plan3);
				    }
				}
				
				
				// limit
				if (objSchema.has("limit")) {
					
					if("[]".equals(objSchema.optString("limit"))){
						
					}else{
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
				if(objAssemb.has("comment")){
	                config.setComment(objAssemb.getString("comment"));
				}
				if(objAssemb.has("risk")){
				    config.setRisk(String.valueOf(objAssemb.getInt("risk")));
				}
				if(objAssemb.has("init")){
				    config.setInit(String.valueOf(objAssemb.getDouble("init")));
				}
				
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
	
//				List<String> listFundCode = config.getFdcodes();
//				List<String> listFundName = config.getAbbrevs();
//				List<String> listFundShareType = config.getSharetypes();
//				List<String> listFundRatio = config.getRatios();
//				JSONArray arrFdcodes = objAssemb.getJSONArray("fdcodes");
//				JSONArray arrAbbrevs = objAssemb.getJSONArray("abbrevs");
//				JSONArray arrShareTypes = objAssemb.getJSONArray("sharetypes");
//				JSONArray arrRatios = objAssemb.getJSONArray("ratios");
//				for (int i = 0; i < arrAbbrevs.length(); i++) {
//					listFundCode.add(arrFdcodes.getString(i));
//					listFundName.add(arrAbbrevs.getString(i));
//					listFundShareType.add(arrShareTypes.getString(i));
//					listFundRatio.add(String.valueOf(arrRatios.getDouble(i)));
//				}
				
				List<Fund> listFundCode = config.getFundList();
                
                JSONArray list = objAssemb.getJSONArray("list");
                
                for (int i = 0; i < list.length(); i++) {
                    Fund fund = new Fund();
                    JSONObject fundObj = list.getJSONObject(i);
                    fund.name = fundObj.optString("abbrev");
                    fund.code = fundObj.optString("fdcode");
                    fund.date_rate = fundObj.optString("day_rate");
                    fund.marketValue = fundObj.optDouble("market_value");
                    fund.ratio = fundObj.optString("ratio");
                    fund.shares = fundObj.optDouble("shares");
                    fund.nav = fundObj.optDouble("nav");
                    fund.rank = fundObj.optString("rank");
                    fund.recomm_reason = fundObj.optString("recomm_reason");
                    fund.total_rank = fundObj.optString("total_rank");
                    fund.star = fundObj.optString("star");
                    listFundCode.add(fund);
                }
//				setAssemFundList(detail);
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
			
			/** AssembleFixed */
			AssembleFixed assembleFixed = detail.getAssembleFixed();
            String fixedStr = objData.optString("dingtou");
            
            if ("".equals(fixedStr) || "{}".equals(fixedStr)) {
                assembleFixed.setIsFixedExist(0);
            }else{
                JSONObject fixedInvest = new JSONObject(fixedStr);
                assembleFixed.setSdid(fixedInvest.optString("sdid"));
                assembleFixed.setUid(fixedInvest.optString("uid"));
                assembleFixed.setSid(fixedInvest.optString("sid"));
                assembleFixed.setCtime(fixedInvest.optString("ctime"));
                assembleFixed.setUtime(fixedInvest.optString("utime"));
                assembleFixed.setCworkday(fixedInvest.optString("cworkday"));
                assembleFixed.setStop_date(fixedInvest.optString("stop_date"));
                assembleFixed.setState(fixedInvest.optString("state"));
                assembleFixed.setMonth_sum(fixedInvest.optString("month_sum"));
                assembleFixed.setFirst_date(fixedInvest.optString("first_date"));
                assembleFixed.setTradeacco(fixedInvest.optString("tradeacco"));
                assembleFixed.setBank(fixedInvest.optString("bank"));
                assembleFixed.setCard(fixedInvest.optString("card"));
                assembleFixed.setReason(fixedInvest.optString("reason"));
                assembleFixed.setSuccess_ratio(fixedInvest.optString("success_ratio"));
                assembleFixed.setNext_day(fixedInvest.optString("next_day"));
                
            }
	
			/** AssembleReminder */
		
			return detail;
		}
		
		
		return null;
	}

	/**
	 * <p>Title: parseAssembleAssets</p>
	 * <p>Description: 解析用户信息</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static AssembleAssets parseAssembleAssets(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			AssembleAssets assets = new AssembleAssets();
			JSONObject jsonObject = responseBase.jsonObject;
			String strAssets = jsonObject.optString("assets");
			JSONObject objAssets = new JSONObject(strAssets);
			assets.setAssets(objAssets.optString("assets"));
			assets.setIncome(objAssets.getString("income"));
			assets.setInvest(objAssets.getString("invest"));
			assets.setModitm(objAssets.getString("moditm"));
			assets.setProfit(objAssets.optString("profit"));
			assets.setProfitYesday(objAssets.optString("profitYesday"));
			assets.setRedemping(objAssets.optString("redemping"));
			assets.setSubscripting(objAssets.optString("subscripting"));
			assets.setUid(objAssets.getString("uid"));
			assets.setUnpaid(objAssets.getString("unpaid"));
			if (objAssets.has("balance_note_state"))
				assets.setBalanceState(objAssets.getInt("balance_note_state"));
			if (objAssets.has("balance_count"))
				assets.setBalanceCount(objAssets.getInt("balance_count"));
			return assets;
		}
		return null;
	}

	/**
	 * <p>Title: parseFundSearch</p>
	 * <p>Description: 解析基金全量搜索数据</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<FundSearchModel> parseFundSearch(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			ArrayList<FundSearchModel> listFund = new ArrayList<FundSearchModel>();
			
			JSONObject jsonObject = responseBase.jsonObject;
			JSONArray arrayList = jsonObject.optJSONArray("list");

			for (int i = 0; i < arrayList.length(); i++) {
				JSONObject objFund = arrayList.optJSONObject(i);
				if (null != objFund) {
					FundSearchModel fund = new FundSearchModel(objFund.optString("abbrev"), 
							objFund.optString("fdcode"), objFund.optString("spell"), objFund.optInt("type"));
					listFund.add(fund);
				}
			}
			return listFund;
		}
		return null;
	}

	/**
	 * <p>Title: parseFundSearchLatestUpdate</p>
	 * <p>Description: 解析基金搜索数据最新更新时间</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static long parseFundSearchLatestUpdate(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return -1;
		
		if (responseBase.ecode == 0) {
			JSONObject jsonObject = responseBase.jsonObject;
			long updateTime = jsonObject.optLong("uptime");
			return updateTime;
		}
		return -1;
	}

	/**
	 * <p>Title: parseAssembleList</p>
	 * <p>Description: 解析组合列表</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<AssembleBase> parseAssembleList(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			ArrayList<AssembleBase> listAssemble = new ArrayList<AssembleBase>();
			
			JSONObject jsonObject = responseBase.jsonObject;
			JSONArray schemasAssetses = jsonObject.optJSONArray("schemas-assetses");

			for (int i = 0; i < schemasAssetses.length(); i++) {
				AssembleBase assemble = new AssembleBase();
				JSONObject objAssemble = schemasAssetses.optJSONObject(i).optJSONObject("schema");
				if (null != objAssemble) {
					assemble.setSid(String.valueOf(objAssemble.optInt("sid")));
					assemble.setUid(objAssemble.optString("uid"));
					assemble.setName(objAssemble.optString("name"));
					assemble.setType(Integer.valueOf(objAssemble.optString("type")));
					if (objAssemble.has("balance_note_state"))
						assemble.setBalanceState(objAssemble.optInt("balance_note_state"));
					if (objAssemble.has("balance_operation_state"))
						assemble.setBalanceOpState(objAssemble.optInt("balance_operation_state"));
				}
				JSONObject objAssets = schemasAssetses.optJSONObject(i).optJSONObject("assets");
				if (null != objAssets) {
					assemble.assets = new AssembleAssets();
					AssembleAssets assets = assemble.assets;
					if (objAssets.has("assets"))
						assets.setAssets(String.valueOf(objAssets.optDouble("assets")));
					if (objAssets.has("profitYesday"))
						assets.setProfitYesday(String.valueOf(objAssets.optDouble("profitYesday")));
					if (objAssets.has("unpaid"))
						assets.setUnpaid(String.valueOf(objAssets.optDouble("unpaid")));
					if (objAssets.has("invest"))
						assets.setInvest(String.valueOf(objAssets.optDouble("invest")));
					if (objAssets.has("income"))
						assets.setIncome(String.valueOf(objAssets.optDouble("income")));
					if (objAssets.has("profit"))
						assets.setProfit(String.valueOf(objAssets.optDouble("profit")));
					if (objAssets.has("subscripting"))
                        assets.setSubscripting(objAssets.optString("subscripting"));
				}
				listAssemble.add(assemble);
			}
			return listAssemble;
		}
		return null;
	}

	/**
	 * <p>Title: parseRebalanceHistoryDetail</p>
	 * <p>Description: 解析再平衡详情数据</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static RebalanceHistoryDetail parseRebalanceHistoryDetail(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			RebalanceHistoryDetail detail = new RebalanceHistoryDetail();
			JSONObject jsonObject = responseBase.jsonObject;
			
			if (jsonObject.has("list")) {
				detail.listFund = new ArrayList<RebalanceFund>();
				JSONArray arrayFund = jsonObject.optJSONArray("list");
				for (int i=0; i<arrayFund.length(); i++) {
					JSONObject objFund = arrayFund.optJSONObject(i);
					if (null != objFund) {
						RebalanceFund fund = new RebalanceFund();
						detail.listFund.add(initRebalanceFund(objFund, fund));
					}
				}
			}
			if (jsonObject.has("schema")) {
				String strSchema = jsonObject.optString("schema");
				if (StringHelper.isBlank(strSchema))
					return detail;
				JSONObject objSchema = new JSONObject(strSchema);
				// 暂时未解析
				if (objSchema.has("abbrevs")) {
					JSONArray arrayAbbrevs = jsonObject.optJSONArray("abbrevs");
					if (arrayAbbrevs != null)
						for (int i=0; i<arrayAbbrevs.length(); i++) {
							JSONObject objFund = arrayAbbrevs.optJSONObject(i);
							if (null != objFund) {
								
							}
						}
				}
				if (objSchema.has("arrive_time"))
					detail.arrive_time = objSchema.optString("arrive_time");
				if (objSchema.has("bank"))
					detail.bank = objSchema.optString("bank");
				if (objSchema.has("card"))
					detail.card = objSchema.optString("card");
				if (objSchema.has("market_value"))
					detail.market_value = objSchema.optString("market_value");
				if (objSchema.has("opDate"))
					detail.opDate = objSchema.optLong("opDate");
				if (objSchema.has("operate"))
					detail.operate = objSchema.optInt("operate");
				if (objSchema.has("reason"))
					detail.reason = objSchema.optString("reason");
				if (objSchema.has("rsid"))
					detail.rsid = objSchema.optString("rsid");
				if (objSchema.has("sid"))
					detail.sid = objSchema.optString("sid");
				if (objSchema.has("sname"))
					detail.sname = objSchema.optString("sname");
				if (objSchema.has("sopid"))
					detail.sopid = objSchema.optString("sopid");
				if (objSchema.has("state"))
					detail.state = objSchema.optString("state");
				if (objSchema.has("sum"))
					detail.sum = objSchema.optDouble("sum");
				if (objSchema.has("uid"))
					detail.uid = objSchema.optString("uid");
			}
			return detail;
		}
		return null;
	}

	private static RebalanceFund initRebalanceFund(JSONObject objFund, RebalanceFund fund) {
		if (objFund.has("abbrev"))
			fund.abbrev = objFund.optString("abbrev");
		if (objFund.has("applyserial"))
			fund.applyserial = objFund.optString("applyserial");
		if (objFund.has("bank"))
			fund.bank = objFund.optString("bank");
		if (objFund.has("card"))
			fund.card = objFund.optString("card");
		if (objFund.has("ctime"))
			fund.ctime = objFund.optString("ctime");
		if (objFund.has("fdcode"))
			fund.fdcode = objFund.optString("fdcode");
		if (objFund.has("id"))
			fund.id = objFund.optString("id");
		if (objFund.has("nav"))
			fund.nav = objFund.optString("nav");
		if (objFund.has("operate"))
			fund.operate = objFund.optString("operate");
		if (objFund.has("opid"))
			fund.opid = objFund.optString("opid");
		if (objFund.has("remark"))
			fund.remark = objFund.optString("remark");
		if (objFund.has("rsid"))
			fund.rsid = objFund.optString("rsid");
		if (objFund.has("shares"))
			fund.shares = objFund.optString("shares");
		if (objFund.has("sid"))
			fund.sid = objFund.optString("sid");
		if (objFund.has("sopid"))
			fund.sopid = objFund.optString("sopid");
		if (objFund.has("state"))
			fund.state = objFund.optString("state");
		if (objFund.has("sum"))
			fund.sum = objFund.optString("sum");
		if (objFund.has("uid"))
			fund.uid = objFund.optString("uid");
		if (objFund.has("utime"))
			fund.utime = objFund.optString("utime");
		return fund;
	}

	/**
	 * <p>Title: parseRebalanceDetail</p>
	 * <p>Description: 解析再平衡详情数据</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static ResponseBase parseRebalanceDetail2(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;

		ResponseRebalanceDetail responseDetail = new ResponseRebalanceDetail();
		responseDetail.ecode = responseBase.ecode;
		responseDetail.strError = responseBase.strError;
		responseDetail.detail = null;
		if (responseBase.ecode == 0) {
			responseDetail.detail = new RebalanceDetail();
			JSONObject jsonObject = responseBase.jsonObject;
			
			if (jsonObject.has("before_assets")) {
				JSONArray arrayBeforeFund = jsonObject.optJSONArray("before_assets");
				if (arrayBeforeFund != null)
					for (int i=0; i<arrayBeforeFund.length(); i++) {
						JSONObject objFund = arrayBeforeFund.optJSONObject(i);
						if (null != objFund) {
							Fund fund = new Fund();
							responseDetail.detail.listBeforeFund.add(initFund(objFund, fund));
						}
					}
			}
			if (jsonObject.has("after_assets")) {
				JSONArray arrayAfterFund = jsonObject.optJSONArray("after_assets");
				if (arrayAfterFund != null)
					for (int i=0; i<arrayAfterFund.length(); i++) {
						JSONObject objFund = arrayAfterFund.optJSONObject(i);
						if (null != objFund) {
							Fund fund = new Fund();
							responseDetail.detail.listAfterFund.add(initFund(objFund, fund));
						}
					}
			}
			if (jsonObject.has("estimate_time")) {
				JSONObject objTime = new JSONObject(jsonObject.optString("estimate_time"));
				if (objTime.has("rsid"))
					responseDetail.detail.strRsid = objTime.optString("rsid");
				if (objTime.has("redemp_arrive_time"))
					responseDetail.detail.redeemArriveTime = objTime.optLong("redemp_arrive_time");
				if (objTime.has("subscript_time"))
					responseDetail.detail.purchaseTime = objTime.optLong("subscript_time");
				if (objTime.has("subscript_confirm_time"))
					responseDetail.detail.purchaseConfirmTime = objTime.optLong("subscript_confirm_time");
			}
			if (jsonObject.has("handle_list")) {
				JSONArray arrayHandleFund = jsonObject.optJSONArray("handle_list");
				if (arrayHandleFund != null)
					for (int i=0; i<arrayHandleFund.length(); i++) {
						JSONObject objFund = arrayHandleFund.optJSONObject(i);
						if (null != objFund) {
							Fund fund = new Fund();
							responseDetail.detail.listHandleFund.add(initFund(objFund, fund));
						}
					}
			}
			responseDetail.detail.fee = jsonObject.optDouble("fee");
			responseDetail.detail.reason = jsonObject.optString("reason");
		}
		return responseDetail;
	}

	/**
	 * <p>Title: parseRebalanceHoldingCompare</p>
	 * <p>Description: 解析再平衡持仓对比数据</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static RebalanceHoldingCompare parseRebalanceHoldingCompare(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			RebalanceHoldingCompare holdingCompare = new RebalanceHoldingCompare();
			JSONObject jsonObject = responseBase.jsonObject;

			if (jsonObject.has("before")) {
				JSONArray arrayBeforeFund = jsonObject.optJSONArray("before");
				if (arrayBeforeFund != null)
					for (int i=0; i<arrayBeforeFund.length(); i++) {
						JSONObject objFund = arrayBeforeFund.optJSONObject(i);
						if (null != objFund) {
							Fund fund = new Fund();
							holdingCompare.listBeforeFund.add(initFund(objFund, fund));
						}
					}
			}
			if (jsonObject.has("after")) {
				JSONArray arrayAfterFund = jsonObject.optJSONArray("after");
				if (arrayAfterFund != null)
					for (int i=0; i<arrayAfterFund.length(); i++) {
						JSONObject objFund = arrayAfterFund.optJSONObject(i);
						if (null != objFund) {
							Fund fund = new Fund();
							holdingCompare.listAfterFund.add(initFund(objFund, fund));
						}
					}
			}
			if (jsonObject.has("before_time"))
				holdingCompare.beforeTime = jsonObject.optLong("before_time");
			if (jsonObject.has("after_time"))
				holdingCompare.afterTime = jsonObject.optLong("after_time");
			return holdingCompare;
		}
		return null;
	}

	/**
	 * <p>Title: parseRebalanceDetail</p>
	 * <p>Description: 解析再平衡详情数据</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static RebalanceDetail parseRebalanceDetail(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			RebalanceDetail detail = new RebalanceDetail();
			JSONObject jsonObject = responseBase.jsonObject;
			
			if (jsonObject.has("before_assets")) {
				JSONArray arrayBeforeFund = jsonObject.optJSONArray("before_assets");
				for (int i=0; i<arrayBeforeFund.length(); i++) {
					JSONObject objFund = arrayBeforeFund.optJSONObject(i);
					if (null != objFund) {
						Fund fund = new Fund();
						detail.listBeforeFund.add(initFund(objFund, fund));
					}
				}
			}
			if (jsonObject.has("after_assets")) {
				JSONArray arrayAfterFund = jsonObject.optJSONArray("after_assets");
				for (int i=0; i<arrayAfterFund.length(); i++) {
					JSONObject objFund = arrayAfterFund.optJSONObject(i);
					if (null != objFund) {
						Fund fund = new Fund();
						detail.listAfterFund.add(initFund(objFund, fund));
					}
				}
			}
			if (jsonObject.has("estimate_time")) {
				JSONObject objTime = new JSONObject(jsonObject.optString("estimate_time"));
				if (objTime.has("rsid"))
					detail.strRsid = objTime.optString("rsid");
				if (objTime.has("redemp_arrive_time"))
					detail.redeemArriveTime = objTime.optLong("redemp_arrive_time");
				if (objTime.has("subscript_time"))
					detail.purchaseTime = objTime.optLong("subscript_time");
				if (objTime.has("subscript_confirm_time"))
					detail.purchaseConfirmTime = objTime.optLong("subscript_confirm_time");
			}
			if (jsonObject.has("handle_list")) {
				JSONArray arrayHandleFund = jsonObject.optJSONArray("handle_list");
				for (int i=0; i<arrayHandleFund.length(); i++) {
					JSONObject objFund = arrayHandleFund.optJSONObject(i);
					if (null != objFund) {
						Fund fund = new Fund();
						detail.listHandleFund.add(initFund(objFund, fund));
					}
				}
			}
			detail.fee = jsonObject.optDouble("fee");
			detail.reason = jsonObject.optString("reason");
			return detail;
		}
		return null;
	}
	
	private static Fund initFund(JSONObject objFund, Fund fund) {
		fund.name = objFund.optString("abbrev");
		fund.code = objFund.optString("fdcode");
		fund.nav = Double.valueOf(objFund.optString("nav"));
		fund.shares = objFund.optDouble("shares");
		if (objFund.has("marketvalue"))
			fund.marketValue = Double.valueOf(objFund.optString("marketvalue"));
		if (objFund.has("fee"))
			fund.fee = Double.valueOf(objFund.optString("fee"));
		if (objFund.has("handle_ratio"))
			fund.rate = objFund.optDouble("handle_ratio");
		if (objFund.has("ratio"))
			fund.rate = objFund.optDouble("ratio");
		if (objFund.has("subscript_shares"))
			fund.subscriptShares = objFund.optDouble("subscript_shares");
		if (objFund.has("subscript_sum"))
			fund.subscriptSum = objFund.optDouble("subscript_sum");
		if (objFund.has("redemp_shares"))
			fund.redeemShares = objFund.optDouble("redemp_shares");
		if (objFund.has("redemp_sum"))
			fund.redeemSum = objFund.optDouble("redemp_sum");
		if (objFund.has("operate"))
			fund.operate = objFund.optInt("operate");
		if (objFund.has("state"))
			fund.state = objFund.optInt("state");
		return fund;
	}

	/**
	 * <p>Title: parseUser</p>
	 * <p>Description: 解析用户信息</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static User parseUser(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			JSONObject jsonObject = responseBase.jsonObject;
			User user = new User();
			user.setUid(jsonObject.getString("uid"));
			user.setMobile(jsonObject.getString("mobile"));
			user.setAddr(jsonObject.getString("addr"));
			return user;
		}
		return null;
	}


	/**
	 * <p>Title: parseActivityState</p>
	 * <p>Description: 解析运营活动信息</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<Activity> parseActivityState(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		if (responseBase.jsonObject == null) return null;
		
		if (responseBase.ecode == 0) {
			ArrayList<Activity> listActivity = new ArrayList<Activity>();
			JSONObject jsonObject = responseBase.jsonObject;
			if (!jsonObject.has("activities"))
			        return null;
			JSONObject objActivitys = jsonObject.getJSONObject("activities");
			// 新手任务
			if (objActivitys.has("newbee")) {
				NewHandActivity newHand = new NewHandActivity();
				JSONObject objNewHand = objActivitys.getJSONObject("newbee");
				newHand.name = objNewHand.getString("desc");
				newHand.isOpen = objNewHand.getBoolean("open");
				newHand.strIntoduceUrl = objNewHand.getString("introduce_url");
				newHand.strThemeUrl = objNewHand.getString("theme_url");
				listActivity.add(newHand);
			}
			// 抽奖
			if (objActivitys.has("lottery1")) {
				LotteryActivity lottery = new LotteryActivity();
				JSONObject objLottery = objActivitys.getJSONObject("lottery1");
				lottery.name = objLottery.getString("desc");
				lottery.isOpen = objLottery.getBoolean("open");
				listActivity.add(lottery);
			}
			return listActivity;
		}
		return null;
	}

	/**
	 * <p>Title: parseLotteryStatus</p>
	 * <p>Description: 解析用户抽奖资格</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static LotteryActivity parseLotteryStatus(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			LotteryActivity lottery = new LotteryActivity();
			JSONObject jsonObject = responseBase.jsonObject;
			lottery.status = jsonObject.getBoolean("lottery");
			lottery.strMessage = jsonObject.getString("message");
			lottery.strUrl = jsonObject.getString("url");
			return lottery;
		}
		return null;
	}

	/**
	 * <p>Title: parseRedBagList</p>
	 * <p>Description: 解析红包列表</p>
	 * @param responseBase
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<RedBag> parseRedBagList(ResponseBase responseBase) throws JSONException {

		if (responseBase == null) return null;
		
		if (responseBase.ecode == 0) {
			ArrayList<RedBag> listRedBag = new ArrayList<RedBag>();
			JSONObject jsonObject = responseBase.jsonObject;
			JSONArray arrayRedBags = jsonObject.getJSONArray("usable");
			
			for (int i=0; i<arrayRedBags.length(); i++) {
				
				RedBag redBag = new RedBag();
				JSONObject objRedBag = arrayRedBags.optJSONObject(i);
				redBag.id = objRedBag.optInt("id");
				redBag.type = objRedBag.optInt("type");
				redBag.val = objRedBag.optInt("val");
				redBag.rate = objRedBag.optInt("rate");
				redBag.limitSum = objRedBag.optDouble("limit");
				redBag.limitDay = objRedBag.optInt("time_limit");
				redBag.startIime = objRedBag.optLong("ctime");
				redBag.endTime = objRedBag.optLong("end_time");
				listRedBag.add(redBag);
			}
			return listRedBag;
		}
		return null;
	}
	
	
	
	

	/**
	 * 赋值方案的基金列表数据
	 * @param detail
	 */
	public static void setAssemFundList(AssembleDetail detail){
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

}
