
package com.qianjing.finance.model.p2p;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

public class P2PCardResponse extends BaseModel {

	private static final long serialVersionUID = 1L;

	/*
	 * { "data": { "acctName": "王松烽", "bankCode": "03030000", "bankName": "光大银行", "branchName": "",
	 * "cardNo": "6226620209358654", "idNo": "410182198403272936" }, "ecode": 0, "emsg": "" }
	 */
	@SerializedName("data")
	public P2PCard card;

	public static class P2PCard implements Serializable {

		private static final long serialVersionUID = 1L;

		@SerializedName("acctName")
		public String masterName;

		@SerializedName("bankCode")
		public String bankCode;

		@SerializedName("bankName")
		public String bankName;

		@SerializedName("branchName")
		public String branchName;

		@SerializedName("cardNo")
		public String cardNum;

		@SerializedName("idNo")
		public String masterId;
	}

}
