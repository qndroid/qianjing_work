package com.qianjing.finance.model.rebalance;

public class RebalanceBeforeAssets {

		//基金名称
		private String abbrev;
		//基金代号
		private String fdcode;
		//市值
		private String marketvalue;
		//基金净值
		private String nav;
		//基金占比
		private String ratio;
		//基金份额
		private String shares;
		
		
		public String getAbbrev() {
			return abbrev;
		}
		public void setAbbrev(String abbrev) {
			this.abbrev = abbrev;
		}
		public String getFdcode() {
			return fdcode;
		}
		public void setFdcode(String fdcode) {
			this.fdcode = fdcode;
		}
		public String getMarketvalue() {
			return marketvalue;
		}
		public void setMarketvalue(String marketvalue) {
			this.marketvalue = marketvalue;
		}
		public String getNav() {
			return nav;
		}
		public void setNav(String nav) {
			this.nav = nav;
		}
		public String getRatio() {
			return ratio;
		}
		public void setRatio(String ratio) {
			this.ratio = ratio;
		}
		public String getShares() {
			return shares;
		}
		public void setShares(String shares) {
			this.shares = shares;
		}
		
		
		
		
}
