package com.qianjing.finance.model.recommand;

import com.qianjing.finance.model.common.BaseModel;

import java.util.ArrayList;

/** 
* @description 推荐详情实体
* @author majinxin
* @date 2015年9月14日
*/

public class RecommandDetailModel extends BaseModel
{
	private static final long serialVersionUID = 1L;

	public String mProductName;
	public String mProductInfo;
	public String mRate;
	public String mUsable;
	public String mBuyLimit;
	public String mMinBuy;
	public String mBoundName;
	public String mBoundRate;
	public String mNoBoundName;
	public String mNoBoundRate;
	public String mBid;

	public ArrayList<RecommandConfig> mList;

}
