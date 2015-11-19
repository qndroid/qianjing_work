/**
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
*/

package com.qianjing.finance.model.common.user;

import com.google.gson.annotations.SerializedName;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.User;

/** 
* @description 用户信息请求实例
* @author fangyan
* @date 2015年8月27日
*/ 
public class UserRequest extends BaseModel {

    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    public User user;

}