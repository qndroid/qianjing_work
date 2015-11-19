package com.qianjing.finance.model.assemble;

import java.io.Serializable;

import com.qianjing.finance.model.common.BaseModel;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * <p>Title: AssembleAssets</p>
 * <p>Description: 组合资产信息</p>
 * @author fangyan
 * @date 2015年5月26日
 */
public class AssembleAssets implements  Parcelable {
	/** 用户id */
	private String uid;
	/** 组合id */
	private String sid;
	/** 组合总资产 */
	private String assets;
	/** 昨日收益 */
	private String profitYesday;
	/** 修改时间戳 */
	private String moditm;
	/** 未付收益 */
	private String unpaid;
	/** 累计申购金额 */
	private String invest;
	/** 累计赎回金额 */
	private String income;
	/** 申购中金额 */
	private String subscripting;
	/** 赎回中金额 */
	private String redemping;
	/** 累计收益 */
	private String profit;
	/** 有无需要再平衡组合 */
	private int balanceState;
	/** 需要再平衡的组合数量 */
	private int balanceCount;


	public int getBalanceState() {
		return balanceState;
	}

	public void setBalanceState(int balanceState) {
		this.balanceState = balanceState;
	}

	public int getBalanceCount() {
		return balanceCount;
	}

	public void setBalanceCount(int balanceCount) {
		this.balanceCount = balanceCount;
	}

	public String getSubscripting() {
		return subscripting;
	}

	public void setSubscripting(String subscripting) {
		this.subscripting = subscripting;
	}

	public String getRedemping() {
		return redemping;
	}

	public void setRedemping(String redemping) {
		this.redemping = redemping;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getAssets() {
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}

	public String getProfitYesday() {
		return profitYesday;
	}

	public void setProfitYesday(String profitYesday) {
		this.profitYesday = profitYesday;
	}

	public String getModitm() {
		return moditm;
	}

	public void setModitm(String moditm) {
		this.moditm = moditm;
	}

	public String getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(String unpaid) {
		this.unpaid = unpaid;
	}

	public String getInvest() {
		return invest;
	}

	public void setInvest(String invest) {
		this.invest = invest;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	@Override
	public String toString() {
		return "AssetsInfo [uid=" + uid + ", sid=" + sid + ", assets=" + assets
				+ ", profitYesday=" + profitYesday + ", moditm=" + moditm
				+ ", unpaid=" + unpaid + ", invest=" + invest + ", income="
				+ income + ", subscripting=" + subscripting + ", redemping="
				+ redemping + ", profit=" + profit + ", balanceState="
				+ balanceState + ", balanceCount=" + balanceCount + "]";
	}

	public AssembleAssets() {
		super();
	}
	
	private AssembleAssets(Parcel in) {
		uid = in.readString();
		sid = in.readString();
		assets = in.readString();
		profitYesday = in.readString();
		moditm = in.readString();
		unpaid = in.readString();
		invest = in.readString();
		income = in.readString();
		subscripting = in.readString();
		redemping = in.readString();
		profit = in.readString();
		balanceState = in.readInt();
		balanceCount = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uid);
		dest.writeString(sid);
		dest.writeString(assets);
		dest.writeString(profitYesday);
		dest.writeString(moditm);
		dest.writeString(unpaid);
		dest.writeString(invest);
		dest.writeString(income);
		dest.writeString(subscripting);
		dest.writeString(redemping);
		dest.writeString(profit);
		dest.writeInt(balanceState);
		dest.writeInt(balanceCount);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<AssembleAssets> CREATOR = new Creator<AssembleAssets>() {

		@Override
		public AssembleAssets createFromParcel(Parcel source) {
			return new AssembleAssets(source);
		}

		@Override
		public AssembleAssets[] newArray(int size) {
			return new AssembleAssets[size];
		}
	};

}
