
package com.qianjing.finance.model.fund;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: FundResponseItem
 * Description: 购买返回的基金信息
 * @author zhangqi
 * @date 2014-7-8
 */
public class FundResponseItem implements Parcelable {

	/** 基金代码 */
	public String fdcode = "";
	/** 基金简称*/
	public String abbrev = "";
	/** 投资金额*/
	public String fdsum = "0";
	/** 投资份额*/
	public String fdshare = "";
	/** 申购状态 */
	public int fdstate;
	/** 失败原因 */
	public String reason;

	/** 定投申购状态 */
	public int monthState;
	/** 定投月金额 */
	public double monthSum;
	/** 定投失败原因 */
	public String monthReason;

	public FundResponseItem() {
	}

	public FundResponseItem(String fdcode, String abbrev, String fdsum, String fdshare, int fdstate, String reason) {
		super();
		this.fdcode = fdcode;
		this.abbrev = abbrev;
		this.fdsum = fdsum;
		this.fdshare = fdshare;
		this.fdstate = fdstate;
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "FundResponseItemInfo [fdcode=" + fdcode + ", abbrev=" + abbrev + ", fdsum=" + fdsum + ", fdshare=" + fdshare + ", fdstate=" + fdstate
				+ ", reason=" + reason + "]";
	}

	private FundResponseItem(Parcel in) {
		fdcode = in.readString();
		abbrev = in.readString();
		fdsum = in.readString();
		fdshare = in.readString();
		fdstate = in.readInt();
		reason = in.readString();
		monthState = in.readInt();
		monthSum = in.readDouble();
		monthReason = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(fdcode);
		dest.writeString(abbrev);
		dest.writeString(fdsum);
		dest.writeString(fdshare);
		dest.writeInt(fdstate);
		dest.writeString(reason);
		dest.writeInt(monthState);
		dest.writeDouble(monthSum);
		dest.writeString(monthReason);
	}

	public static final Parcelable.Creator<FundResponseItem> CREATOR = new Creator<FundResponseItem>() {

		@Override
		public FundResponseItem createFromParcel(Parcel source) {
			return new FundResponseItem(source);
		}

		@Override
		public FundResponseItem[] newArray(int size) {
			return new FundResponseItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
