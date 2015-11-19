package com.qianjing.finance.model.rebalance;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.qianjing.finance.model.fund.Fund;


/**
 * <p>Title: RebalanceHoldingCompare</p>
 * <p>Description: 再平衡持仓对比</p>
 * @author fangyan
 * @date 2015年7月20日
 */
public class RebalanceHoldingCompare implements Parcelable {

	/** 调仓前时间 */
	public long beforeTime;

	/** 调仓后时间 */
	public long afterTime;
	
	/** 再平衡前基金配置 */
	public ArrayList<Fund> listBeforeFund = new ArrayList<Fund>();

	/** 再平衡后基金配置 */
	public ArrayList<Fund> listAfterFund = new ArrayList<Fund>();
	
	
	public RebalanceHoldingCompare() {}

	public RebalanceHoldingCompare(Parcel in) {
		beforeTime = in.readLong();
		afterTime = in.readLong();
		in.readTypedList(listBeforeFund, Fund.CREATOR); 
		in.readTypedList(listAfterFund, Fund.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(beforeTime);
		dest.writeLong(afterTime);
		dest.writeTypedList(listBeforeFund);
		dest.writeTypedList(listAfterFund);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<RebalanceHoldingCompare> CREATOR = new Creator<RebalanceHoldingCompare>() {

		@Override
		public RebalanceHoldingCompare createFromParcel(Parcel source) {
			return new RebalanceHoldingCompare(source);
		}

		@Override
		public RebalanceHoldingCompare[] newArray(int size) {
			return new RebalanceHoldingCompare[size];
		}
	};
	
}
