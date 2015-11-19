
package com.qianjing.finance.model.p2p;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

/** 
* @description 封装已投保本组合详情回复信息
* @author fangyan
* @date 2015年9月14日
*/
public class P2PSteadyDetailReponse extends BaseModel {

	private static final long serialVersionUID = 1L;

	/* 
	{
	"data": {
	    "bid": 11,
	    "bond_name": "债券",
	    "bond_ratio": 30,
	    "conf": [
	        {
	            "name": "p2p",
	            "percent": "30",
	            "type": "债券"
	        },
	        {
	            "name": "私人理财",
	            "percent": "50",
	            "type": "类现金"
	        },
	        {
	            "name": "钱景基金",
	            "percent": "20",
	            "type": "类现金"
	        }
	    ],
	    "other_name": "类现金",
	    "other_ratio": 70,
	    "sum_amount": "200.22",
	    "sum_order": "200.00",
	    "sum_profit": "0.22"
	},
	"ecode": 0,
	"emsg": ""
	}
	 */

	@SerializedName("data")
	public P2PPortDetail portDetail;

	public static class P2PPortDetail implements Serializable {

		private static final long serialVersionUID = 1L;

		@Expose
		public String name = "保本保息组合";

		/** 产品ID */
		@SerializedName("bid")
		public double bid;

		/** 总持仓 */
		@SerializedName("sum_amount")
		public double assets;

		/** 累计申购 */
		@SerializedName("sum_order")
		public double order;

		/** 累计盈亏 */
		@SerializedName("sum_profit")
		public double profit;

		/** 债券名称 */
		@SerializedName("bond_name")
		public String bondName;

		/** 债券占比 */
		@SerializedName("bond_ratio")
		public double bondRatio;

		/** 其他名称 */
		@SerializedName("other_name")
		public String otherName;

		/** 其他占比 */
		@SerializedName("other_ratio")
		public double otherRatio;

		/** 组合配置 */
		@SerializedName("conf")
		public List<P2PSteadyItem> listItem;

	}

}
