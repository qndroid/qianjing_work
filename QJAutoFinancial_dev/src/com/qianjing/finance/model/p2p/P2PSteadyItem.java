
package com.qianjing.finance.model.p2p;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/** 
* @description 封装保本组合中配置信息
* @author fangyan
* @date 2015年9月14日
*/
public class P2PSteadyItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 名称 */
	@SerializedName("name")
	public String name;

	/** 占比 */
	@SerializedName("percent")
	public int ratio;

	/** 类型 */
	@SerializedName("type")
	public String type;

}
