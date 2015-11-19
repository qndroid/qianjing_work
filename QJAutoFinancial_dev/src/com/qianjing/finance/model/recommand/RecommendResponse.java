
package com.qianjing.finance.model.recommand;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;

/** 
* @description 封装推荐组合信息
* @author fangyan
* @date 2015年9月16日
*/ 
public class RecommendResponse extends BaseModel {

    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    public Recommend recommend;

    public static class Recommend implements Serializable {

        private static final long serialVersionUID = 1L;

        @SerializedName("bid")
        public String bid;

        @SerializedName("title")
        public String name;

        @SerializedName("deal_rate")
        public String ratio;

        @SerializedName("rate_name")
        public String ratioName;

        @SerializedName("type")
        public int type;

        @SerializedName("attr_first")
        public String attrFirst;

        @SerializedName("attr_second")
        public String attrSecond;

    }

}
