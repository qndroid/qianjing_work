
package com.qianjing.finance.model.common;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 实体基类
 * @author fangyan
 * @date 2015年8月1日
 */
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	public int stateCode = -1;

	@SerializedName("ecode")
	public String strStateCode;

	@SerializedName("emsg")
	public String strErrorMessage;

}
