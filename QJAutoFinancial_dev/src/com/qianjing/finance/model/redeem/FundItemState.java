package com.qianjing.finance.model.redeem;

import com.qianjing.finance.model.common.BaseModel;

public class FundItemState extends BaseModel
{

	private static final long serialVersionUID = 1L;

	/** 基金简称 */
	public String abbrev;
	/** 目前状态 */
	public int fdstate;
	/** 失败原因 */
	public String reason;
	/**
	 * 赎回份额
	 */
	public String redeemFen;

	public FundItemState(String abbrev, int fdstate, String reason, String redeemFen)
	{
		this.abbrev = abbrev;
		this.fdstate = fdstate;
		this.reason = reason;
		this.redeemFen = redeemFen;
	}
}
