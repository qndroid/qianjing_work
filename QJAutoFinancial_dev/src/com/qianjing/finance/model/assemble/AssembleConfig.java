package com.qianjing.finance.model.assemble;

import java.util.ArrayList;
import java.util.List;

import com.qianjing.finance.model.fund.Fund;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * <p>Title: AssembleConfig</p>
 * <p>Description: 组合配置信息</p>
 * @author fangyan
 * @date 2015年5月26日
 */
public class AssembleConfig implements Parcelable {

	/** 用户id */
	private String uid;
	/** 组合id */
	private String sid;
	/** 组合名称 */
	private String sname;
	/** 交易帐号 */
	private String tradeacco;
	/** 卡号 */
	private String card;
	/** 银行名称 */
	private String bank;
	/** 对应银行卡是否解绑 */
	private boolean isUnBC;
	/** 时间戳 */
	private String assembT;
	/** 货股基占比 */
	private String hgRatio;
	/** 初始投资金额 */
	private String init;
	/** 月投入 */
	private String month;
	/** 风险评分 */
	private String risk;
	/** 预期收益（货基） */
	private String earnings;
	/** 非股基占比 */
	private double ratioNonStock;
	/** 非股基名称 */
	private String strNonStock;
	/** 股基占比 */
	private double ratioStock;
	/** 股基名称 */
	private String strStock;
	/**投资建议*/
	private String comment;
	
	/** 基金代码 */
	private List<String> fdcodes = new ArrayList<String>();
	/** 基金名称 */
	private List<String> abbrevs = new ArrayList<String>();
	/** 基金收费方式 */
	private List<String> sharetypes = new ArrayList<String>();
	/** 基金配比 */
	private List<String> ratios = new ArrayList<String>();
	/** 基金实例 */
	private ArrayList<Fund> fundList = new ArrayList<Fund>();

	public boolean isUnBC() {
		return isUnBC;
	}

	public void setUnBC(boolean isUnBC) {
		this.isUnBC = isUnBC;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
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

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getTradeacco() {
		return tradeacco;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAssembT() {
		return assembT;
	}

	public void setAssembT(String assembT) {
		this.assembT = assembT;
	}

	public String getHgRatio() {
		return hgRatio;
	}

	public void setHgRatio(String hgRatio) {
		this.hgRatio = hgRatio;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public ArrayList<Fund> getFundList() {
		return fundList;
	}

	public void setFundList(ArrayList<Fund> fundList) {
		this.fundList = fundList;
	}
	
	 public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public List<String> getFdcodes() {
		return fdcodes;
	}

	public void setFdcodes(List<String> fdcodes) {
		this.fdcodes = fdcodes;
	}

	public List<String> getAbbrevs() {
		return abbrevs;
	}

	public void setAbbrevs(List<String> abbrevs) {
		this.abbrevs = abbrevs;
	}

	public List<String> getSharetypes() {
		return sharetypes;
	}

	public void setSharetypes(List<String> sharetypes) {
		this.sharetypes = sharetypes;
	}

	public List<String> getRatios() {
		return ratios;
	}

	public void setRatios(List<String> ratios) {
		this.ratios = ratios;
	}

	public double getRatioNonStock() {
		return ratioNonStock;
	}

	public void setRatioNonStock(double ratioNonStock) {
		this.ratioNonStock = ratioNonStock;
	}

	public String getStrNonStock() {
		return strNonStock;
	}

	public void setStrNonStock(String strNonStock) {
		this.strNonStock = strNonStock;
	}

	public double getRatioStock() {
		return ratioStock;
	}

	public void setRatioStock(double ratioStock) {
		this.ratioStock = ratioStock;
	}

	public String getStrStock() {
		return strStock;
	}

	public void setStrStock(String strStock) {
		this.strStock = strStock;
	}
	

	public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override  
     public String toString(){  
         return "AssembInfo [uid=" + uid + ", sid=" + sid + ", sname=" + sname + 
         		", tradeacco=" + tradeacco + ", bank=" + bank + ", assembT=" + assembT + 
        		", hgRatio=" + hgRatio + ", init=" + init + ", month=" + month + 
        		", risk=" + risk + ", fdcodes=" + fdcodes + ", abbrevs=" + abbrevs + 
        		", sharetypes=" + sharetypes + ", ratios=" + ratios + ", earnings=" + earnings
        		 + ", ratioNonStock=" + ratioNonStock + ", ratioStock=" + ratioStock
         		+ ", strNonStock=" + strNonStock + ", strStock=" + strStock + "]";  
     }  

	@SuppressWarnings("unchecked")
    private AssembleConfig(Parcel in) {
		uid = in.readString();
		sid = in.readString();
		sname = in.readString();
		tradeacco = in.readString();
		card = in.readString();
		bank = in.readString();
		assembT = in.readString();
		hgRatio = in.readString();
		init = in.readString();
		month = in.readString();
		risk = in.readString();
		earnings = in.readString();
		comment = in.readString(); //
		ratioNonStock = in.readDouble();
		strNonStock = in.readString();
		ratioStock = in.readDouble();
		strStock = in.readString();
		in.readStringList(fdcodes);
		in.readStringList(abbrevs);
		in.readStringList(sharetypes);
		in.readStringList(ratios);
        in.readTypedList(fundList, Fund.CREATOR); 
	}

	public AssembleConfig() {
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(uid);
		dest.writeString(sid);
		dest.writeString(sname);
		dest.writeString(tradeacco);
		dest.writeString(card);
		dest.writeString(bank);
		dest.writeString(assembT);
		dest.writeString(hgRatio);
		dest.writeString(init);
		dest.writeString(month);
		dest.writeString(risk);
		dest.writeString(earnings);
		dest.writeString(comment);
		dest.writeDouble(ratioNonStock);
		dest.writeString(strNonStock);
		dest.writeDouble(ratioStock);
		dest.writeString(strStock);
		dest.writeStringList(fdcodes);
		dest.writeStringList(abbrevs);
		dest.writeStringList(sharetypes);
		dest.writeStringList(ratios);
        dest.writeTypedList(fundList);
	}

	public static final Parcelable.Creator<AssembleConfig> CREATOR = new Creator<AssembleConfig>() {

		@Override
		public AssembleConfig createFromParcel(Parcel source) {
			return new AssembleConfig(source);
		}

		@Override
		public AssembleConfig[] newArray(int size) {
			return new AssembleConfig[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
}
