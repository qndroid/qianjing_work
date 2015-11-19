package com.qianjing.finance.model.virtual;

import java.util.ArrayList;

import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.fund.MyFundAssets;

/**********************************************************
 * @文件名称：AssetsDetailBean.java
 * @文件作者：liuchen
 * @创建时间：2015年6月24日 下午2:08:39
 * @文件描述：组合实体详情
 * @修改历史：2015年6月24日创建初始版本
 **********************************************************/
public class AssetsDetailBean extends BaseModel
{

	public Assembly assembly = new Assembly();
	public Assets assets = new Assets();
	public InvestReminder investReminder = new InvestReminder();
	public Schema schema = new Schema();
	public ArrayList<MyFundAssets> fundAssetList;

	public class Assembly extends BaseModel
	{
		public float DeltaR;
		public float Rn;
		public float Vn;
		public ArrayList<String> abbrevs = new ArrayList<String>();
		public float assembT;
		public String bank;
		public String card;
		public String earnings;
		public ArrayList<String> fdcodes = new ArrayList<String>();
		public float hgRatio;
		public float init;
		public float month;
		public ArrayList<Float> ratios = new ArrayList<Float>();
		public float risk;
		public ArrayList<String> sharetypes = new ArrayList<String>();
		public float sid;
		public String sname;
		public String tradeacco;
		public String uid;
		public int nostock_ratio;
		public int stock_ratio;
		public String stock_text;
		public String nostock_text;
	}

	public class Assets extends BaseModel
	{
		public float assets;
		public float income;
		public float invest;
		public float moditm;
		public float profit;
		public float profitYesday;
		public float redemping;
		public float sid;
		public float subscripting;
		public String uid;
		public float unpaid;
	}

	public class InvestReminder extends BaseModel
	{
		public String message;
	}

	public class Schema extends BaseModel
	{
		public float createT;
		public String name;
		/**2
		 * 存钱养老组合
		 * */
		public float pension_init;
		public float pension_month;
		public float pension_retire;
		public float pension_age;
		/**6
		 * 梦想基金组合
		 * */
		public float small_init;
		public float small_month;
		public float small_nmonth;
		/**3
		 * 存钱购房组合
		 * */
		public float house_init;
		public float house_money;
		public float house_year;
		/**1
		 *理财增值组合 
		 * */
		public float financial_init;
		public float financial_rate;
		public float financial_risk;
		/**5
		 * 存钱结婚组合
		 * */
		public float married_init;
		public float married_money;
		public float married_year;
		/**4
		 * 存钱育儿组合
		 * */
		public float education_money;
		public float education_year;
		/**
		 * 深度定制
		 * */
		public int financial2_age;
		public float financial2_init;
		public float financial2_preference;
		public float financial2_risk;
		public float financial2_risklevel;

		public int sid;
		public int real_sid;
		public float state;
		public String type;
		public String uid;
		public float updateT;
	}

}
