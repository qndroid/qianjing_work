package com.qianjing.finance.model.common;

/**
 * Title: AccountInfo Description:
 * 
 * @author zhangqi
 * @date 2014-3-20
 */
public class Account {
	/** uuid */
	private String uid;

	/** 卡号 */
	private String card;

	/** 卡所属银行 */
	private String bank;

	/** 所在省 */
	private String province;

	/** 所在市 */
	private String city;

	/** 手机号 */
	private String mobile;

	/** 已解绑时间 */
	private long boundTime;

	public long getBoundTime() {
		return boundTime;
	}

	public void setBoundTime(long boundTime) {
		this.boundTime = boundTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Account(String uid, String card, String bank, String province,
			String city, String mobile) {
		super();
		this.uid = uid;
		this.card = card;
		this.bank = bank;
		this.province = province;
		this.city = city;
		this.mobile = mobile;
	}

	public Account() {
	}

}
