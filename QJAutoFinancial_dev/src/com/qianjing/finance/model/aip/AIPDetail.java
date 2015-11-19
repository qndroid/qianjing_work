/**
 * Project Name:QJAutoFinancial_dev_final
 * File Name:AipDetail.java
 * Package Name:com.qianjing.finance.model.aip
 * Date:2015年8月25日下午4:55:39
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
*/

package com.qianjing.finance.model.aip;

import com.qianjing.finance.model.common.BaseModel;

import java.util.ArrayList;

/** 
* @description TODO（描述这个类的作用）
* @author majinxin
* @date 2015年8月25日
*/

public class AIPDetail extends BaseModel
{
	private static final long serialVersionUID = 1L;

	public String sdid;
	public String uid;
	public String sname;
	public String sid;
	public String ctime;
	public String utime;
	public String cworkday;
	public String stop_date;
	public String state;
	public String month_sum;
	public String first_date;
	public String day;
	public String tradeacco;
	public String bank;
	public String card;
	public String next_day;
	public String min_sum;

	public ArrayList<AIPFundItem> aipItem;
}
