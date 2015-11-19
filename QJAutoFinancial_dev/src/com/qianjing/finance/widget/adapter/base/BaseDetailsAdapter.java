package com.qianjing.finance.widget.adapter.base;

import java.util.List;

import android.util.SparseArray;

public interface BaseDetailsAdapter {

	public int getRiskTypeValue();
	/**
	 * SparseArray是一个Android提供的工具
	 * 这里key=1表示name
	 * key=2表示占比
	 * */
	public void setRiskTypeClick();
	
	public List<SparseArray<Object>> getDetailData();
	
	public void setDetailsItemClick(int position);
	
	public int getNostockRatio();
	
	public int getStockRatio();
	
	public String getStock_text();
	
	public String getNostock_text();
	
}
