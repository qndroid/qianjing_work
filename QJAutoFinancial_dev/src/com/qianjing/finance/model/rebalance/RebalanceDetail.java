package com.qianjing.finance.model.rebalance;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.qianjing.finance.model.fund.Fund;



/**
 * <p>Title: RebalanceDetail</p>
 * <p>Description: 再平衡详情</p>
 * @author fangyan
 * @date 2015年7月11日
 */
public class RebalanceDetail implements Parcelable {

	/** 再平衡ID（提交后） */
	public String strRsid;

	/** 赎回确认日期（提交后） */
	public long redeemArriveTime;

	/** 赎回份额到帐日期（提交后） */
	public long redeemConfirmTime;

	/** 发起申购日期（提交后） */
	public long purchaseTime;

	/** 申购份额到帐日期（提交后） */
	public long purchaseConfirmTime;

	/** 再平衡操作总计手续费率 */
	public double fee = -1.0;

	/** 再平衡说明 */
	public String reason;
	
	/** 再平衡前基金配置 */
	public ArrayList<Fund> listBeforeFund = new ArrayList<Fund>();

	/** 再平衡后基金配置 */
	public ArrayList<Fund> listAfterFund = new ArrayList<Fund>();

	/** 再平衡调整基金配置 */
	public ArrayList<Fund> listHandleFund = new ArrayList<Fund>();
	
	
	public RebalanceDetail() {}

	public RebalanceDetail(Parcel in) {
		strRsid = in.readString();
		redeemArriveTime = in.readLong();
		redeemConfirmTime = in.readLong();
		purchaseTime = in.readLong();
		purchaseConfirmTime = in.readLong();
		fee = in.readDouble();
		reason = in.readString();
		in.readTypedList(listBeforeFund, Fund.CREATOR); 
		in.readTypedList(listAfterFund, Fund.CREATOR); 
		in.readTypedList(listHandleFund, Fund.CREATOR); 
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(strRsid);
		dest.writeLong(redeemArriveTime);
		dest.writeLong(redeemConfirmTime);
		dest.writeLong(purchaseTime);
		dest.writeLong(purchaseConfirmTime);
		dest.writeDouble(fee);
		dest.writeString(reason);
		dest.writeTypedList(listBeforeFund);
		dest.writeTypedList(listAfterFund);
		dest.writeTypedList(listHandleFund);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<RebalanceDetail> CREATOR = new Creator<RebalanceDetail>() {

		@Override
		public RebalanceDetail createFromParcel(Parcel source) {
			return new RebalanceDetail(source);
		}

		@Override
		public RebalanceDetail[] newArray(int size) {
			return new RebalanceDetail[size];
		}
	};
	
}
