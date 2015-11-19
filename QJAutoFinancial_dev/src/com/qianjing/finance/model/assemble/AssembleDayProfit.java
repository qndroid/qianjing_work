package com.qianjing.finance.model.assemble;

import com.qianjing.finance.model.common.BaseModel;

import java.util.ArrayList;

public class AssembleDayProfit extends BaseModel
{
	private static final long serialVersionUID = 1L;

	private String uid;
	private long dt;
	private String sid;
	private String dayProfit;
	private ArrayList<AssembleDayProfitDetail> profitList;

	public ArrayList<AssembleDayProfitDetail> getProfitList()
	{
		return profitList;
	}

	public void setProfitList(ArrayList<AssembleDayProfitDetail> profitList)
	{
		this.profitList = profitList;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public long getDt()
	{
		return dt;
	}

	public void setDt(long dt)
	{
		this.dt = dt;
	}

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public String getDayProfit()
	{
		return dayProfit;
	}

	public void setDayProfit(String dayProfit)
	{
		this.dayProfit = dayProfit;
	}
}