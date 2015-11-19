
package com.qianjing.finance.net.connection;

import com.qianjing.finance.model.common.BaseModel;


/**
 * @description 网络请求数据回调接口
 * @author fangyan
 * @date 2015年8月1日
 */
public interface CommonCallBack {

    /** 
    * @description 请求成功回调方法
    * @author fangyan 
    * @param model
    */ 
    public void success(BaseModel model);

    /** 
    * @description 请求失败回调方法
    * @author fangyan 
    * @param status
    */ 
    public void fail();
    
}
