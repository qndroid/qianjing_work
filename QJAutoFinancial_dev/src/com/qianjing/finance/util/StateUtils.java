package com.qianjing.finance.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qjautofinancial.R;

public class StateUtils {

	private static final String SUCCESS = "3+";
	private static final String REDEEMING = "2+";
	private static final String FAIL = "4+";
	private static final String SUBMIT = "0+";
	private static final String PURCHASE = "1+";

	/**
	 * 拼接字符串
	 * */
	public static String getAssembleStateStr(int operate,
			ArrayList<Integer> fdstate) {
		String str = "";
		String startStr = "";
		String middleStr = "";
		String endStr = "";
		if (operate == 1) {
			middleStr = "申购";
		} else {
			middleStr = "赎回";
		}

		for (int i = 0; i < fdstate.size(); i++) {
			str += fdstate.get(i) + "";
		}

		if (matchPatternAll(SUCCESS, str)) {
			endStr = "成功";
			return startStr + middleStr + endStr;
		} else if (matchPatternPart(SUCCESS, str)) {
			endStr = "成功";
			startStr = "部分";
			return startStr + middleStr + endStr;
		} else if (matchPatternAll(PURCHASE, str)) {
			endStr = "中";
			return startStr + middleStr + endStr;
		} else if (matchPatternPart(PURCHASE, str)) {
			endStr = "中";
			startStr = "部分";
			return startStr + middleStr + endStr;
		} else if (matchPatternAll(REDEEMING, str)) {
			endStr = "中";
			return startStr + middleStr + endStr;
		} else if (matchPatternPart(REDEEMING, str)) {
			endStr = "中";
			startStr = "部分";
			return startStr + middleStr + endStr;
		} else if (matchPatternAll(SUBMIT, str)) {
			endStr = "受理中";
			return startStr + middleStr + endStr;
		} else if (matchPatternPart(SUBMIT, str)) {
			endStr = "受理中";
			startStr = "部分";
			return startStr + middleStr + endStr;
		} else if (matchPatternAll(FAIL, str)) {
			endStr = "失败";
			return startStr + middleStr + endStr;
		}
		return "状态解析失败";
	}

	public static boolean matchPatternAll(String regex, String str) {
		Pattern mPattern = Pattern.compile(regex);
		Matcher mMatcher = mPattern.matcher(str);
		return mMatcher.matches();
	}

	public static boolean matchPatternPart(String regex, String str) {
		Pattern mPattern = Pattern.compile(regex);
		Matcher mMatcher = mPattern.matcher(str);
		return mMatcher.find();
	}

	// 判断是否是进行中的操作
	public static boolean isRunningState(int operate, int state,
			ArrayList<Integer> fdstate) {

		if (operate == 1 || operate == 2) {
			
			if (getAssembleStateStr(operate, fdstate).contains("中")) {
				return true;
			} else {
				return false;
			}
		} else {
			if (state == 10) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static String getRebalanceStateStr(int state){
		switch(state){
		case 0:
		    return "尚未平衡";
		case 10:
			return "平衡中";
		case 20:
			return "再平衡成功";
		case 21:
			return "再平衡部分成功";
		case 30:
			return "再平衡失败";
		default: return "未知平衡状态";
		}
	}
	
	public static int getRebalanceResoure(int state){
		switch(state){
		case 10:
			return R.drawable.rebalance_ing;
		case 20:
			return R.drawable.rebalance_over;
		case 21:
			return R.drawable.history_some_success;
		case 30:
			return R.drawable.history_fail;
		default: return -1;
		}
	}
}