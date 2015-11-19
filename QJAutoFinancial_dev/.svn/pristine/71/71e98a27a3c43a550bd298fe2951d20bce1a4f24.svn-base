
package com.qianjing.finance.util.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**********************************************************
 * @文件名称：DateUtil.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月28日 上午11:57:10
 * @文件描述：日期格式化类
 * @修改历史：2015年5月28日创建初始版本
 **********************************************************/
public final class DateFormatHelper {
	/**
	 * ********************************************************
	 * @文件名称：DateFormat.java
	 * @文件作者：renzhiqiang
	 * @创建时间：2015年5月28日 上午10:41:12
	 * @文件描述：工程中目前所有日期类型
	 * @修改历史：2015年5月28日创建初始版本
	 *********************************************************
	 */
	public enum DateFormat {
		DATE_1("MM-dd"), DATE_2("MM月dd日"), DATE_3("MM月dd日 HH:mm"), DATE_4("yyyy-MM-dd HH:mm"), DATE_5("yyyy-MM-dd"), DATE_6("EEEE"), DATE_7(
				"yyyy-MM-dd"), DATE_8("yyyy-MM-dd hh:mm:ss"), DATE_9("MM-dd HH:mm"), DATE_10("yyyy/MM/dd"), DATE_11("HH:mm"), DATE_12("yyyy年MM月dd日");
		private String value;

		private DateFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 将时间措格式化为任意指定格式
	 * @param timeSpace
	 * @param dateFormat
	 * @return
	 */
	public static String formatDate(String timeSpace, DateFormat dateFormat) {
		/**
		 * 把默认时区改为东八区
		 * */
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat formate = null;
		Long time = Long.parseLong(timeSpace);
		Date date = new Date(time);
		formate = new SimpleDateFormat(dateFormat.getValue());
		return formate.format(date);
	}

	/**
	 * 获取昨天日期，输出指定格式
	 * @param dateFormat
	 * @return
	 */
	public static String getYesterdayTime(DateFormat dateFormat) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat(dateFormat.value).format(cal.getTime());
	}

}
