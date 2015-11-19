
package com.qianjing.finance.util.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @description 字符串工具类
 * @author renzhiqiang
 * @date 2015年8月21日
 */
public final class StringHelper {

	/**
	 * @description 工程中所有的字符串格式
	 * @author renzhiqiang
	 * @date 2015年8月21日
	 */
	public enum StringFormat {

		FORMATE_1("###,##0.00"), FORMATE_2("##0.00"), FORMATE_3("##0.0000");
		private String value;

		private StringFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * @description 将字符串转化为指定的格式
	 */
	public static String formatString(String source, StringFormat stringFormate) {

		DecimalFormat df = new DecimalFormat(stringFormate.getValue());
		return isBlank(source) ? "" : df.format(new BigDecimal(source));
	}

	// 非小数首位不能为0
	public static boolean isSpecialMoney(String money) {
		String strPattern = "";
		if (isNotBlank(money)) {
			if (money.contains(".")) {// 包含小数点
										// strPattern =
										// "^[0-9]+(\\.[0-9]{1,2})?$";
				strPattern = "^([0-9]|([1-9][0-9]+))(\\.[0-9]{1,2})?$";
			} else {
				strPattern = "^0|([1-9]\\d*)$";
			}
			Pattern pattern = Pattern.compile(strPattern);
			Matcher matcher = pattern.matcher(money);
			// 包含特殊字符
			if (!matcher.matches()) {
				return !matcher.matches();
			} else {// 不包含特殊字符但值小于等于0
				if (Double.parseDouble(money) <= 0)
					return true;
				else
					return false;
			}
		}
		return true;
	}

	/**
	 * 身份证显示规则 显示头三位和最后一位，其他*显示
	 */
	public static String hideCertificate(String value) {

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			if (i <= 2 || i == value.length() - 1) {// 前三位或最后一位
				stringBuffer.append(value.charAt(i));
			} else {
				stringBuffer.append("*");
			}
		}

		return stringBuffer.toString();
	}

	/**
	 * 手机号隐藏中间四位
	 */
	public static String hideMobileNum(String value) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(value.substring(0, 3));
		stringBuffer.append("****");
		stringBuffer.append(value.substring(value.length() - 4));
		return stringBuffer.toString();
	}

	/**
	 * 银行卡号只显示后4位
	 * 
	 * @param value
	 * @return
	 */
	public static String showCardLast4(String value) {
		if (isNotBlank(value)) {
			return value.substring(value.length() - 4);
		}
		return "";
	}

	/**
	 * 银行卡号显示前4位和后4位
	 */
	public static String hintCardNum(String value) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(value.substring(0, 4));
		stringBuffer.append(" **** **** ");
		stringBuffer.append(value.substring(value.length() - 4));
		return stringBuffer.toString();
	}

	/**
	 * 判断是否是数字字符串
	 */
	public static boolean isNumberString(String s) {

		if (isNotBlank(s)) {

			if (s.matches("[\\d]+\\.[\\d]+")) {

				return true;
			}
		}

		return false;
	}

	/**
	 * 抓取短信中的验证码
	 */
	public static String getVerCode(String content) {

		Pattern pattern = Pattern.compile("\\d{4}");
		Matcher matcher = pattern.matcher(content);

		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * 判断字符串是否为空或为零
	 */
	public static boolean isBlankZero(String str) {
		boolean isNull = isBlank(str);
		if (isNotBlank(str)) {
			if (str.equals("0")) {
				isNull = true;
			}
		}
		return isNull;
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isBlank(String str) {
		return (str == null || "".equals(str.trim()) || "null".equals(str));
	}

	/**
	 * 判断字符串是否不为空
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 判断指定字符串是否等于给定长度
	 */
	public static boolean checkTheLength(String value, int... lengths) {

		for (int arg : lengths) {

			if (value.length() == arg) {

				return true;
			}
		}
		return false;
	}

	/** 
	* @description 将字符串数值转化为两位小数数值
	* @author fangyan 
	* @param source
	* @return
	*/
	public static String formatDecimal(String source) {
		DecimalFormat df = new DecimalFormat(StringFormat.FORMATE_2.getValue());
		return isBlank(source) ? "" : df.format(new BigDecimal(source));
	}

	/** 
	* @description 将double数值转化为两位小数数值
	* @author fangyan 
	* @param source
	* @return
	*/
	public static String formatDecimal(double source) {
		if (StringHelper.isBlank(String.valueOf(source)))
			return "";
		DecimalFormat df = new DecimalFormat(StringFormat.FORMATE_2.getValue());
		try {
			return df.format(new BigDecimal(source));
		} catch (Exception e) {
			return "";
		}
	}

	/** 
	* @description 将字符串进行特殊格式化
	* @author liuchen
	* @param source 待分割得字符串
	* @param rules  数组，存放分割字符串的字符如  . 或  : 等等
	* @param params 分割的字符串属性  第一个表示字符大小，第二个表示该分割字段的颜色，依次类推
	* @return
	*/
	public static SpannableString spannableFormat(String source, String[] rules, int... params) {
		SpannableString totalSpan = new SpannableString(source);
		for (int i = 0; i < rules.length + 1; i++) {
			totalSpan.setSpan(new AbsoluteSizeSpan(params[i * 2]), i == 0 ? 0 : source.indexOf(rules[i - 1]) + 1, i == rules.length ? source.length()
					: source.indexOf(rules[i]) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			totalSpan.setSpan(new ForegroundColorSpan(params[i * 2 + 1]), i == 0 ? 0 : source.indexOf(rules[i - 1]) + 1,
					i == rules.length ? source.length() : source.indexOf(rules[i]) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return totalSpan;
	}

	/** 
	* @description 判断字符串中是否有中文字符
	* @author fangyan 
	* @param strName
	* @return
	*/
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	// GENERAL_PUNCTUATION 判断中文的“号  
	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号  
	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号  
	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

}
