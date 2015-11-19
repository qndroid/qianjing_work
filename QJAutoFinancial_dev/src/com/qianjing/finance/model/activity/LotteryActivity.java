package com.qianjing.finance.model.activity;

/**
 * <p>Title: LotteryActivity</p>
 * <p>Description: 申购满额抽奖活动信息</p>
 * @author fangyan
 * @date 2015年7月10日
 */
public class LotteryActivity extends Activity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** message（抽奖通知） */
	public String strMessage;

	/** url（抽奖通知） */
	public String strUrl;

	/** 用户是否有抽奖资格（抽奖通知） */
	public boolean status;

}
