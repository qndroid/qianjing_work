package com.qianjing.finance.model.timedespority;

import com.qianjing.finance.model.common.BaseModel;

import java.util.ArrayList;

/** 
* @description 定存收益明细实体
* @author majinxin
* @date 2015年9月2日
*/

public class TimeProfitData extends BaseModel
{
	private static final long serialVersionUID = 1L;

	public String updateTime;
	public ArrayList<TimeProfitItem> profitList;
}
