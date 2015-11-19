
package com.qianjing.finance.model.aip.buy;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

/** 
* @description 定投申购实体类
* @author fangyan
* @date 2015年9月9日
*/ 
public class AIPBuyResponse extends BaseModel {

    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    public AIPBuy aipBuy;
    
    public static class AIPBuy {
        
        
        
    }
    
}