
package com.qianjing.finance.model.aip;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;


/** 
* @description 定投首次扣款时间实体类
* @author fangyan
* @date 2015年9月9日
*/ 
public class DateFirst extends BaseModel {

    private static final long serialVersionUID = 1L;

    /*
   {
    "ecode": 0,
    "emsg": "",
    "data": 1445961600
    }
     */
    @SerializedName("data")
    public String dateFirst;

}
