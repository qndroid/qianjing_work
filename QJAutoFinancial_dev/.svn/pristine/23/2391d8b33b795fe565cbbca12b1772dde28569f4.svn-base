package com.qianjing.finance.model.common;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * <p>Title: QuickBean</p>
 * <p>Description: 封装绑卡信息</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年4月3日
 */
public class CardBound implements Parcelable {
	
	public String card;//银行卡号
	public String identityno;//证件号码
	public String realname;//真实姓名
	public String bankserial;//银行编号
	public String mobile;//手机号
	public String bankname;//银行名称
	public String brach;//支行
	public String merorderid;//银联商户订单号
	
	public CardBound(){}
	
	private CardBound(Parcel in) {
		card = in.readString();  
		identityno = in.readString();  
		realname = in.readString();  
		bankserial = in.readString();  
		mobile = in.readString();  
		bankname = in.readString();  
		brach = in.readString();  
		merorderid = in.readString();
	} 
	
	@Override
	public String toString() {
		return "QuickBean [card=" + card + ", identityno=" + identityno
				+ ", realname=" + realname + ", bankserial=" + bankserial
				+ ", mobile=" + mobile + ", bankname=" + bankname
				+ ", brach=" + brach + "]";
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<CardBound> CREATOR = new Creator<CardBound>() {
		
		@Override
		public CardBound[] newArray(int size) {
			return new CardBound[size];
		}
		
		@Override
		public CardBound createFromParcel(Parcel source) {
			return new CardBound(source);
		}
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(card);
		dest.writeString(identityno);
		dest.writeString(realname);
		dest.writeString(bankserial);
		dest.writeString(mobile);
		dest.writeString(bankname);
		dest.writeString(brach);
		dest.writeString(merorderid);
	}

}
