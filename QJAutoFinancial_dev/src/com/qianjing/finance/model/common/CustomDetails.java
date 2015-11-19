package com.qianjing.finance.model.common;

import java.util.ArrayList;

public class CustomDetails extends BaseModel {

	//risk
	public int risk;
	//货基
	public int nostock_ratio;
	public String nostock_text;
	//股基
	public int stock_ratio;
	public String stock_text;
	//fund name
	public ArrayList<String> abbrevs = new ArrayList<String>();
	public ArrayList<String> fdcodes = new ArrayList<String>();
	public ArrayList<Float> ratios = new ArrayList<Float>();
	
}
