package com.qianjing.finance.model.activity;

import com.qianjing.finance.model.common.BaseModel;

/**
 * <p>Title: RedBag</p>
 * <p>Description: 红包</p>
 * @author fangyan
 * @date 2015年7月13日
 */
public class RedBag extends BaseModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** ID */
	public int id;

	/** 类型	1-条件返现 */
	public int type;

	/** 使用限额 */
	public double limitSum;

	/** 千分之 */
	public int rate;

	/** 有效期/天 */
	public int limitDay;

	/** 面值 */
	public int val;

	/** 获取时间（开始时间） */
	public long startIime;

	/** 截止时间 */
	public long endTime;
}
