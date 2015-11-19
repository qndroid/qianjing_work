package com.qianjing.finance.model.recommand;

import com.qianjing.finance.model.common.BaseModel;

import java.util.ArrayList;

public class RecommandBuyDetail extends BaseModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String limitInit;
	private double financialRisk;
	private int vsId;
	private String type;
	private int financialRate;
	private int financialInit;
	private String name;

	private double vn;
	private String uid;
	private String sid;
	private int assemblyInit;
	private double deltaR;
	private String sname;
	private double rn;
	private int risk;
	private ArrayList<String> abbrev;
	private ArrayList<Double> ratios;
	private ArrayList<String> fdCodes;
	private ArrayList<String> shareTypes;
	private int month;
	private int assembT;
	private int hgRatio;
	private double noStockRatio;
	private double stockRatio;
	private String noStockName;
	private String stockName;

	public String getNoStockName()
	{
		return noStockName;
	}

	public void setNoStockName(String noStockName)
	{
		this.noStockName = noStockName;
	}

	public String getStockName()
	{
		return stockName;
	}

	public void setStockName(String stockName)
	{
		this.stockName = stockName;
	}

	public double getNoStockRatio()
	{
		return noStockRatio;
	}

	public void setNoStockRatio(double noStockRatio)
	{
		this.noStockRatio = noStockRatio;
	}

	public double getStockRatio()
	{
		return stockRatio;
	}

	public void setStockRatio(double stockRatio)
	{
		this.stockRatio = stockRatio;
	}

	public String getLimitInit()
	{
		return limitInit;
	}

	public void setLimitInit(String limitInit)
	{
		this.limitInit = limitInit;
	}

	public double getFinancialRisk()
	{
		return financialRisk;
	}

	public void setFinancialRisk(double financialRisk)
	{
		this.financialRisk = financialRisk;
	}

	public int getVsId()
	{
		return vsId;
	}

	public void setVsId(int vsId)
	{
		this.vsId = vsId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getFinancialRate()
	{
		return financialRate;
	}

	public void setFinancialRate(int financialRate)
	{
		this.financialRate = financialRate;
	}

	public int getFinancialInit()
	{
		return financialInit;
	}

	public void setFinancialInit(int financialInit)
	{
		this.financialInit = financialInit;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getVn()
	{
		return vn;
	}

	public void setVn(double vn)
	{
		this.vn = vn;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public int getAssemblyInit()
	{
		return assemblyInit;
	}

	public void setAssemblyInit(int assemblyInit)
	{
		this.assemblyInit = assemblyInit;
	}

	public ArrayList<Double> getRatios()
	{
		return ratios;
	}

	public void setRatios(ArrayList<Double> ratios)
	{
		this.ratios = ratios;
	}

	public double getDeltaR()
	{
		return deltaR;
	}

	public void setDeltaR(double deltaR)
	{
		this.deltaR = deltaR;
	}

	public String getSname()
	{
		return sname;
	}

	public void setSname(String sname)
	{
		this.sname = sname;
	}

	public double getRn()
	{
		return rn;
	}

	public void setRn(double rn)
	{
		this.rn = rn;
	}

	public ArrayList<String> getAbbrev()
	{
		return abbrev;
	}

	public void setAbbrev(ArrayList<String> abbrev)
	{
		this.abbrev = abbrev;
	}

	public int getRisk()
	{
		return risk;
	}

	public void setRisk(int risk)
	{
		this.risk = risk;
	}

	public ArrayList<String> getFdCodes()
	{
		return fdCodes;
	}

	public void setFdCodes(ArrayList<String> fdCodes)
	{
		this.fdCodes = fdCodes;
	}

	public ArrayList<String> getShareTypes()
	{
		return shareTypes;
	}

	public void setShareTypes(ArrayList<String> shareTypes)
	{
		this.shareTypes = shareTypes;
	}

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getAssembT()
	{
		return assembT;
	}

	public void setAssembT(int assembT)
	{
		this.assembT = assembT;
	}

	public int getHgRatio()
	{
		return hgRatio;
	}

	public void setHgRatio(int hgRatio)
	{
		this.hgRatio = hgRatio;
	}
}
