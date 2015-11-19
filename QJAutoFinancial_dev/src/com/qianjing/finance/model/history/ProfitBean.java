package com.qianjing.finance.model.history;

import com.qianjing.finance.model.common.BaseModel;

/**********************************************************
 * @文件名称：ProfitBean.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月25日 下午3:47:57
 * @文件描述：盈亏列表Bean
 * @修改历史：2015年6月25日创建初始版本
 **********************************************************/
public class ProfitBean extends BaseModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long time;
	private double dayTotalProfit;
	private String sID;
	private String dayProfit;
	private String name;
	private int type; // 自定义数据类型

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public double getDayTotalProfit()
	{
		return dayTotalProfit;
	}

	public void setDayTotalProfit(double dayTotalProfit)
	{
		this.dayTotalProfit = dayTotalProfit;
	}

	public String getsID()
	{
		return sID;
	}

	public void setsID(String sID)
	{
		this.sID = sID;
	}

	public String getDayProfit()
	{
		return dayProfit;
	}

	public void setDayProfit(String dayProfit)
	{
		this.dayProfit = dayProfit;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}
