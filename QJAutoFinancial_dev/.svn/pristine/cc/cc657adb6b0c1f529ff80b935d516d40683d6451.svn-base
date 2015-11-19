package com.qianjing.finance.model.fund;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Title: Fund
 * Description: 基金信息
 */
public class Fund implements Parcelable {
	
	/** 基金代码 */
    @SerializedName("fdcode")
	public String code;
	
	/** 基金简称 */
    @SerializedName("abbrev")
	public String name = "未命名";

	/** 基金拼音简称 */
	public String spell;

	/** 基金类型 */
	public int type = -1;
	
	/** 分红方式 */
	public String shareType;
	
	/** 所占比例 */
	public double rate = -1.0;

	/** 手续费费率 */
	public double fee = -1.0;

	/** 市值 */
	public double marketValue = -1.0;

	/** 净值 */
	public double nav = -1.0;
	
	/** 份额数 */
	public double shares = -1.0;

	/** 申购份额（再平衡） */
	public double subscriptShares = -1.0;

	/** 申购金额（再平衡） */
	public double subscriptSum = -1.0;

	/** 赎回份额（再平衡） */
	public double redeemShares = -1.0;

	/** 赎回金额（再平衡） */
	public double redeemSum = -1.0;

	/** 操作描述（再平衡）  */
	public String handle;

	/** 操作类型（再平衡）  */
	public int operate = -1;

	/** 状态（再平衡）  */
	public int state = -1;

	/** 色值 */
	public int color;
	
	/**日收益*/
	public String date_rate;
	
	public String ratio;
	
	/**基金排名*/
	public String rank;
	/**推荐理由*/
    public String recomm_reason;
    /**辰星评级*/
    public String star;
    /**总排名*/
    public String total_rank;
    
	
	@Override  
    public String toString(){  
        return "Fund [code=" + code + ", name=" + name + ", spell=" + spell 
        		+ ", type=" + type + ", shareType=" + shareType + ", rate=" + rate + "]";  
    }  
	
	public Fund(){}

	public Fund(String code, String name, String spell, int type) {
		super();
		this.code = code;
		this.name = name;
		this.spell = spell;
		this.type = type;
	}

	public Fund(String code, String name, String shareType, double rate, int color) {
		super();
		this.code = code;
		this.name = name;
		this.shareType = shareType;
		this.rate = rate;
		this.color = color;
	}


    
	private Fund(Parcel in) {
		code = in.readString();  
		name = in.readString();
		spell = in.readString();
		date_rate = in.readString();
        ratio = in.readString();
        rank = in.readString();
        recomm_reason = in.readString();
        star = in.readString();
        total_rank = in.readString();
		type = in.readInt();
		shareType = in.readString();  
		rate = in.readDouble();
		fee = in.readDouble();
		marketValue = in.readDouble();
		nav = in.readDouble();
		shares = in.readDouble();
		subscriptShares = in.readDouble();
		subscriptSum = in.readDouble();
		redeemShares = in.readDouble();
		redeemSum = in.readDouble();
		handle = in.readString();
		operate = in.readInt();
		state = in.readInt();
		color = in.readInt();
		
	}  
	
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(code);
		dest.writeString(name);
		dest.writeString(spell);
		dest.writeString(date_rate);
        dest.writeString(ratio);
        dest.writeString(rank);
        dest.writeString(recomm_reason);
        dest.writeString(star);
        dest.writeString(total_rank);
		dest.writeInt(type);
		dest.writeString(shareType);
		dest.writeDouble(rate);
		dest.writeDouble(fee);
		dest.writeDouble(marketValue);
		dest.writeDouble(nav);
		dest.writeDouble(shares);
		dest.writeDouble(subscriptShares);
		dest.writeDouble(subscriptSum);
		dest.writeDouble(redeemShares);
		dest.writeDouble(redeemSum);
		dest.writeString(handle);
		dest.writeInt(operate);
		dest.writeInt(state);
		dest.writeInt(color);
		
	}
	
	public static final Parcelable.Creator<Fund> CREATOR = new Creator<Fund>() {

		@Override
		public Fund createFromParcel(Parcel source) {
			return new Fund(source);
		}

		@Override
		public Fund[] newArray(int size) {
			return new Fund[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}	
	
}
