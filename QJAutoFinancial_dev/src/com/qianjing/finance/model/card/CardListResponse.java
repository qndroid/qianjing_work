
package com.qianjing.finance.model.card;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

/** 
* @description 封装银行卡列表请求回复
* @author fangyan
* @date 2015年9月25日
*/
public class CardListResponse extends BaseModel {

	private static final long serialVersionUID = 1L;

	@SerializedName("data")
	public Cards cards;

	public static class Cards implements Serializable {

		private static final long serialVersionUID = 1L;

		@SerializedName("cards")
		public ArrayList<Card> listBoundCard;

		@SerializedName("unbc")
		public ArrayList<Card> listUnboundCard;

		//		public ArrayList<Card> getAllCard() {
		//			ArrayList<Card> listCard = new ArrayList<Card>();
		//
		//			return listCard;
		//		}

	}

	public static class Card implements Serializable {

		private static final long serialVersionUID = 1L;

		/** uid */
		@SerializedName("uid")
		public String uid;

		/** 卡号  */
		@SerializedName("card")
		public String number;

		/** 卡所属银行 */
		@SerializedName("bank")
		public String bankName;

		/** 银行代号 */
		@Expose
		public String bankCode;

		/** 开户银行支行号 */
		@SerializedName("brach")
		public String bankBrach;

		/** 是否已解绑 */
		@SerializedName("isUnBC")
		public int isUnbound;

		/** 绑卡方式：3-银联;l-块钱;M-通联*/
		@SerializedName("capitalmode")
		public String capitalMode;

		/** 手机号 */
		@SerializedName("mobile")
		public String mobile;

		/** 绑卡时间戳 */
		@SerializedName("boundT")
		public long boundTime;

		/** 持有者姓名 */
		@SerializedName("acctName")
		public String masterName;

		/** 是否是P2P卡 */
		@Expose
		private int isP2P = 0;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBankCode() {
			return bankCode;
		}

		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}

		public String getBankBrach() {
			return bankBrach;
		}

		public void setBankBrach(String bankBrach) {
			this.bankBrach = bankBrach;
		}

		public int getIsUnbound() {
			return isUnbound;
		}

		public void setIsUnbound(int isUnbound) {
			this.isUnbound = isUnbound;
		}

		public String getCapitalMode() {
			return capitalMode;
		}

		public void setCapitalMode(String capitalMode) {
			this.capitalMode = capitalMode;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public long getBoundTime() {
			return boundTime;
		}

		public void setBoundTime(long boundTime) {
			this.boundTime = boundTime;
		}

		public String getMasterName() {
			return masterName;
		}

		public void setMasterName(String masterName) {
			this.masterName = masterName;
		}

		public int getIsP2P() {
			return isP2P;
		}

		public void setIsP2P(int isP2P) {
			this.isP2P = isP2P;
		}

	}
}
