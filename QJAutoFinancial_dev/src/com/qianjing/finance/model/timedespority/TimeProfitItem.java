package com.qianjing.finance.model.timedespority;

import java.io.Serializable;

/** 
* @description 定存收益明细Item
* @author majinxin
* @date 2015年9月2日
*/
public class TimeProfitItem implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String dealId;
	public String sumProfit;
	public String sumLoad;
	public String dsProfit;
	public String repayTime;
	public String dealName;

}
