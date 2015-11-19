
package com.qianjing.finance.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>Title: Card</p>
 * <p>Description: 银行卡和银行信息</p>
 */
public class Card implements Parcelable {

	/** uid */
	private String uid;
	/** 卡号  */
	private String number;
	/** 卡所属银行 */
	private String bankName;
	/** 银行代号 */
	private String bankCode;
	/** 开户银行支行号 */
	private String bankBrach;
	/** 银行状态 */
	private int bankState;
	/** 是否已解绑 */
	private int isUnbound;
	/** 绑卡方式：3-银联;l-块钱;M-通联*/
	private String capitalMode;
	/** 快捷支付 */
	private String payment;
	/** 所在省 */
	private String province;
	/** 所在市 */
	private String city;
	/** 手机号 */
	private String mobile;
	/** 绑卡时间戳 */
	private long boundTime;
	/** 当前交易账号总资产 */
	private double assets;
	/** 交易账号可用总资产 */
	private double assetsUsable;
	/** 单笔充值限额 */
	private double limitRecharge;
	/** 每日充值限额 */
	private double limitDailyRecharge;
	/** 单笔最大可赎 */
	private double limitRedeem;
	/** 快速取现剩余次数 */
	private int redeemNumber;
	/** 持有者姓名 */
	private String masterName;
	/** 是否是P2P卡 */
	private int isP2P = 0;

	public Card() {
	}

	public Card(String uid, String card, String bankName, double limitRecharge) {
		this.uid = uid;
		this.number = card;
		this.bankName = bankName;
		this.limitRecharge = limitRecharge;
	}

	private Card(Parcel in) {
		uid = in.readString();
		number = in.readString();
		bankName = in.readString();
		bankCode = in.readString();
		bankBrach = in.readString();
		bankState = in.readInt();
		isUnbound = in.readInt();
		capitalMode = in.readString();
		payment = in.readString();
		province = in.readString();
		city = in.readString();
		mobile = in.readString();
		boundTime = in.readLong();
		assets = in.readDouble();
		assetsUsable = in.readDouble();
		limitRecharge = in.readDouble();
		limitDailyRecharge = in.readDouble();
		limitRedeem = in.readDouble();
		redeemNumber = in.readInt();
		masterName = in.readString();
		isP2P = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(uid);
		dest.writeString(number);
		dest.writeString(bankName);
		dest.writeString(bankCode);
		dest.writeString(bankBrach);
		dest.writeInt(bankState);
		dest.writeInt(isUnbound);
		dest.writeString(capitalMode);
		dest.writeString(payment);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(mobile);
		dest.writeLong(boundTime);
		dest.writeDouble(assets);
		dest.writeDouble(assetsUsable);
		dest.writeDouble(limitRecharge);
		dest.writeDouble(limitDailyRecharge);
		dest.writeDouble(limitRedeem);
		dest.writeInt(redeemNumber);
		dest.writeString(masterName);
		dest.writeInt(isP2P);
	}

	public static final Parcelable.Creator<Card> CREATOR = new Creator<Card>() {

		@Override
		public Card createFromParcel(Parcel source) {
			return new Card(source);
		}

		@Override
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankBrach() {
		return bankBrach;
	}

	public void setBankBrach(String bankBrach) {
		this.bankBrach = bankBrach;
	}

	public int getBankState() {
		return bankState;
	}

	public void setBankState(int bankState) {
		this.bankState = bankState;
	}

	public String getCapitalMode() {
		return capitalMode;
	}

	public void setCapitalMode(String capitalMode) {
		this.capitalMode = capitalMode;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getIsUnbound() {
		return isUnbound;
	}

	public void setIsUnbound(int isBound) {
		this.isUnbound = isBound;
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

	public long getBoundTime() {
		return boundTime;
	}

	public void setBoundTime(long boundTime) {
		this.boundTime = boundTime;
	}

	public double getAssets() {
		return assets;
	}

	public void setAssets(double assets) {
		this.assets = assets;
	}

	public double getAssetsUsable() {
		return assetsUsable;
	}

	public void setAssetsUsable(double assetsUsable) {
		this.assetsUsable = assetsUsable;
	}

	public double getLimitRecharge() {
		return limitRecharge;
	}

	public void setLimitRecharge(double limitRecharge) {
		this.limitRecharge = limitRecharge;
	}

	public double getLimitDailyRecharge() {
		return limitDailyRecharge;
	}

	public void setLimitDailyRecharge(double limitDailyRecharge) {
		this.limitDailyRecharge = limitDailyRecharge;
	}

	public double getLimitRedeem() {
		return limitRedeem;
	}

	public void setLimitRedeem(double limitRedeem) {
		this.limitRedeem = limitRedeem;
	}

	public int getRedeemNumber() {
		return redeemNumber;
	}

	public void setRedeemNumber(int redeemNumber) {
		this.redeemNumber = redeemNumber;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public int getIsP2P() {
		return isP2P;
	}

	public void setIsP2P(int isP2P) {
		this.isP2P = isP2P;
	}

}
