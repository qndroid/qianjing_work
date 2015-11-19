package com.qianjing.finance.model.assemble;

import java.io.Serializable;

import com.qianjing.finance.model.common.BaseModel;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * <p>Title: AssembleBase</p>
 * <p>Description: 组合基本信息</p>
 * @author fangyan
 * @date 2015年4月10日
 */
public class AssembleBase implements Parcelable {
	/** 用户id */
	private String uid="";
	/** 组合id */
	private String sid="";
	/** 组合名称 */
	private String name="";
	/** 组合类型 */
	private int type;
	/** 最小申购额 */
	private float minLimit;
	/** 是否需要再平衡 */
	private int balanceState;
	/** 再平衡状态 */
	private int balanceOpState;
	/** 平衡中交易记录sopid */
	private String sopid;
	
	/**投资计划*/
	private InvestPlan plan1;
	private InvestPlan plan2;
	private InvestPlan plan3;
	
	/** 投资 */
	private String investInitSum = "";
	private String investRate = "";
	private String investRisk = "";

	/** 养老 */
	private String pensionInitSum = "";
	private String pensionMonthAmount = "";
	private String pensionCurrentAge = "";
	private String pensionRetireAge = "";

	/** 购房 */
	private String houseInitSum = "";
	private String houseGoalSum = "";
	private String houseYears = "";

	/** 育儿 */
	private String childYears = "";
	private String childGoalSum = "";

	/** 结婚 */
	private String marryInitSum = "";
	private String marryGoalSum = "";
	private String marryYears = "";
	
	/** 梦想 */
	private String dreamInitSum = "";
	private String dreamMonths = "";

	/** 定制 */
	private String specialAge;
	private String specialMoney;
	private String specialInitSum;
	private String specialPref = "";
	private String specialRisk = "";
	private int specialRiskLevel;

	/** 组合资产信息 */
	public AssembleAssets assets;
	

	public String getSopid() {
		return sopid;
	}

	public void setSopid(String sopid) {
		this.sopid = sopid;
	}

	public int getBalanceState() {
		return balanceState;
	}

	public void setBalanceState(int balanceState) {
		this.balanceState = balanceState;
	}

	public int getBalanceOpState() {
		return balanceOpState;
	}

	public void setBalanceOpState(int balanceOpState) {
		this.balanceOpState = balanceOpState;
	}

	public float getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(float minLimit) {
		this.minLimit = minLimit;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public String getInvestInitSum() {
		return investInitSum;
	}

	public void setInvestInitSum(String investInitSum) {
		this.investInitSum = investInitSum;
	}

	public String getInvestRate() {
		return investRate;
	}

	public void setInvestRate(String investRate) {
		this.investRate = investRate;
	}

	public String getInvestRisk() {
		return investRisk;
	}

	public void setInvestRisk(String investRisk) {
		this.investRisk = investRisk;
	}

	public String getPensionInitSum() {
		return pensionInitSum;
	}

	public void setPensionInitSum(String pensionInitSum) {
		this.pensionInitSum = pensionInitSum;
	}

	public String getPensionMonthAmount() {
		return pensionMonthAmount;
	}

	public void setPensionMonthAmount(String pensionMonthAmount) {
		this.pensionMonthAmount = pensionMonthAmount;
	}

	public String getPensionCurrentAge() {
		return pensionCurrentAge;
	}

	public void setPensionCurrentAge(String pensionCurrentAge) {
		this.pensionCurrentAge = pensionCurrentAge;
	}

	public String getPensionRetireAge() {
		return pensionRetireAge;
	}

	public void setPensionRetireAge(String pensionRetireAge) {
		this.pensionRetireAge = pensionRetireAge;
	}

	public String getHouseInitSum() {
		return houseInitSum;
	}

	public void setHouseInitSum(String houseInitSum) {
		this.houseInitSum = houseInitSum;
	}

	public String getHouseGoalSum() {
		return houseGoalSum;
	}

	public void setHouseGoalSum(String houseGoalSum) {
		this.houseGoalSum = houseGoalSum;
	}

	public String getHouseYears() {
		return houseYears;
	}

	public void setHouseYears(String houseYears) {
		this.houseYears = houseYears;
	}

	public String getChildYears() {
		return childYears;
	}

	public void setChildYears(String childYears) {
		this.childYears = childYears;
	}

	public String getChildGoalSum() {
		return childGoalSum;
	}

	public void setChildGoalSum(String childGoalSum) {
		this.childGoalSum = childGoalSum;
	}

	public String getMarryInitSum() {
		return marryInitSum;
	}

	public void setMarryInitSum(String marryInitSum) {
		this.marryInitSum = marryInitSum;
	}

	public String getMarryGoalSum() {
		return marryGoalSum;
	}

	public void setMarryGoalSum(String marryGoalSum) {
		this.marryGoalSum = marryGoalSum;
	}

	public String getMarryYears() {
		return marryYears;
	}

	public void setMarryYears(String marryYears) {
		this.marryYears = marryYears;
	}

	public String getDreamInitSum() {
		return dreamInitSum;
	}

	public void setDreamInitSum(String dreamInitSum) {
		this.dreamInitSum = dreamInitSum;
	}

	public String getDreamMonths() {
		return dreamMonths;
	}

	public void setDreamMonths(String dreamMonths) {
		this.dreamMonths = dreamMonths;
	}

	public String getSpecialPref() {
		return specialPref;
	}

	public void setSpecialPref(String specialPref) {
		this.specialPref = specialPref;
	}

	public String getSpecialRisk() {
		return specialRisk;
	}

	public void setSpecialRisk(String specialRisk) {
		this.specialRisk = specialRisk;
	}
	
	public String getSpecialAge() {
		return specialAge;
	}

	public void setSpecialAge(String specialAge) {
		this.specialAge = specialAge;
	}

	public String getSpecialInitSum() {
		return specialInitSum;
	}

	public void setSpecialInitSum(String specialInitSum) {
		this.specialInitSum = specialInitSum;
	}

	public String getSpecialMoney() {
		return specialMoney;
	}

	public void setSpecialMoney(String specialMoney) {
		this.specialMoney = specialMoney;
	}

	public int getSpecialRiskLevel() {
		return specialRiskLevel;
	}

	public void setSpecialRiskLevel(int specialRiskLevel) {
		this.specialRiskLevel = specialRiskLevel;
	}
	
	

	public InvestPlan getPlan1() {
        return plan1;
    }

    public void setPlan1(InvestPlan plan1) {
        this.plan1 = plan1;
    }

    public InvestPlan getPlan2() {
        return plan2;
    }

    public void setPlan2(InvestPlan plan2) {
        this.plan2 = plan2;
    }

    public InvestPlan getPlan3() {
        return plan3;
    }

    public void setPlan3(InvestPlan plan3) {
        this.plan3 = plan3;
    }

    @Override
	public String toString() {
		return "AssembleBase [uid=" + uid + ", sid=" + sid + ", name=" + name
				+ ", type=" + type + ", minLimit=" + minLimit  + ", investInitSum=" + investInitSum
				+ ", investRate=" + investRate + ", investRisk=" + investRisk
				+ ", pensionInitSum=" + pensionInitSum + ", pensionMonthAmount=" + pensionMonthAmount
				+ ", pensionCurrentAge=" + pensionCurrentAge + ", pensionRetireAge=" + pensionRetireAge
				+ ", houseInitSum=" + houseInitSum + ", houseGoalSum=" + houseGoalSum
				+ ", houseYears=" + houseYears + ", childYears=" + childYears
				+ ", childGoalSum=" + childGoalSum + ", marryInitSum=" + marryInitSum
				+ ", marryGoalSum=" + marryGoalSum + ", marryYears=" + marryYears
				+ ", dreamInitSum=" + dreamInitSum +", dreamMonthAmount=" + dreamMonths
				+ ", specialAge=" + specialAge +", specialInitSum=" + specialInitSum
				+ ", specialPref=" + specialPref +", specialRisk=" + specialRisk 
				+ ", specialRiskLevel=" + specialRiskLevel + "]";
	}

	public AssembleBase() {}

	private AssembleBase(Parcel in) {
		uid = in.readString();
		sid = in.readString();
		name = in.readString();
		type = in.readInt();
		minLimit = in.readFloat();
		balanceState = in.readInt();
		balanceOpState = in.readInt();
		sopid = in.readString();
		investInitSum = in.readString();
		investRate = in.readString();
		investRisk = in.readString();
		pensionInitSum = in.readString();
		pensionMonthAmount = in.readString();
		pensionCurrentAge = in.readString();
		pensionRetireAge = in.readString();
		houseInitSum = in.readString();
		houseGoalSum = in.readString();
		houseYears = in.readString();
		childYears = in.readString();
		childGoalSum = in.readString();
		marryInitSum = in.readString();
		marryGoalSum = in.readString();
		marryYears = in.readString();
		dreamInitSum = in.readString();
		dreamMonths = in.readString();
		specialAge = in.readString();
		specialMoney = in.readString();
		specialInitSum = in.readString();
		specialPref = in.readString();
		specialRisk = in.readString();
		specialRiskLevel = in.readInt();
		
		plan1 = in.readParcelable(InvestPlan.class.getClassLoader());
		plan2 = in.readParcelable(InvestPlan.class.getClassLoader());
		plan3 = in.readParcelable(InvestPlan.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(uid);
		dest.writeString(sid);
		dest.writeString(name);
		dest.writeInt(type);
		dest.writeFloat(minLimit);
		dest.writeInt(balanceState);
		dest.writeInt(balanceOpState);
		dest.writeString(sopid);
		dest.writeString(investInitSum);
		dest.writeString(investRate);
		dest.writeString(investRisk);
		dest.writeString(pensionInitSum);
		dest.writeString(pensionMonthAmount);
		dest.writeString(pensionCurrentAge);
		dest.writeString(pensionRetireAge);
		dest.writeString(houseInitSum);
		dest.writeString(houseGoalSum);
		dest.writeString(houseYears);
		dest.writeString(childYears);
		dest.writeString(childGoalSum);
		dest.writeString(marryInitSum);
		dest.writeString(marryGoalSum);
		dest.writeString(marryYears);
		dest.writeString(dreamInitSum);
		dest.writeString(dreamMonths);
		dest.writeString(specialAge);
		dest.writeString(specialMoney);
		dest.writeString(specialInitSum);
		dest.writeString(specialPref);
		dest.writeString(specialRisk);
		dest.writeInt(specialRiskLevel);
		
		dest.writeParcelable(plan1, arg1);
		dest.writeParcelable(plan2, arg1);
		dest.writeParcelable(plan3, arg1);
	}

	public static final Parcelable.Creator<AssembleBase> CREATOR = new Creator<AssembleBase>() {

		@Override
		public AssembleBase createFromParcel(Parcel source) {
			return new AssembleBase(source);
		}

		@Override
		public AssembleBase[] newArray(int size) {
			return new AssembleBase[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
