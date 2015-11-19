package com.qianjing.finance.model.virtual;

import com.qianjing.finance.model.common.BaseModel;



public class SchemaAssetsBean extends BaseModel{

	public Schema schema = new Schema();
	public Assets assets = new Assets();
	
	public class Schema extends BaseModel{
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
		
		public float sid;
		public float state;
		public String type;
		public String uid;
		public float updateT;
	}
	public class Assets{
		public float assets;
		public float income;
		public float invest;
		public float moditm;
		public float profit;
		public float profitYesday;
		public float redemping;
		public String sid;
		public float subscripting;
		public String uid;
		public float unpaid;
	}
		
}
