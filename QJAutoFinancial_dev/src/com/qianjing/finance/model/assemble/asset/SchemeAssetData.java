package com.qianjing.finance.model.assemble.asset;

import com.qianjing.finance.model.common.BaseModel;

/** 
* @description 组合列表数据模型
* @author majinxin
* @date 2015年9月9日
*/
public class SchemeAssetData extends BaseModel
{
	private static final long serialVersionUID = 1L;

	public String sid;
	public String uid;
	public String name;
	public String type;
	public String state;
	public String balanceState;
	public String balanceOperationState;

	public String assets = "0";
	public String profitYesterDay = "0";
	public String profit = "0";
	public String subscripting = "0";
	public String redemping = "0";
}
