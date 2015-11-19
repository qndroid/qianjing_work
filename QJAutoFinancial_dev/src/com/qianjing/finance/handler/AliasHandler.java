/**
 * Project Name:QJAutoFinancial_dev
 * File Name:AliasHandler.java
 * Package Name:com.qianjing.finance.handler
 * Date:2015年7月30日下午2:14:06
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
 */

package com.qianjing.finance.handler;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.qianjing.finance.util.helper.StringHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Set;

/**
 * ClassName:AliasHandler <br/>
 * Function: 处理注册推送别名功能. <br/>
 * Date: 2015年7月30日 下午2:14:06 <br/>
 * 
 * @author fangyan
 * @version 1.0
 */
public class AliasHandler extends Handler {

    /**
     * @fields mContext: 发起组件
     */
    private Context mContext;
    /**
     * @fields strMobile: 需注册的别名
     */
    private String strMobile;
    /**
     * @fields count: 注册行为计数器
     */
    private int count;
    /**
     * @fields LIMIT_COUNT: 注册行为次数上限
     */
    private static final int LIMIT_COUNT = 10;

    public AliasHandler(Context context, String strMobile) {
        this.mContext = context;
        this.strMobile = strMobile;
    }

    @Override
    public void handleMessage(final Message message) {

        if (mContext == null || StringHelper.isBlank(strMobile) || count > LIMIT_COUNT)
            return;

        // 注册推送别名
        JPushInterface.setAlias(mContext, strMobile, new TagAliasCallback() {
            @Override
            public void gotResult(int status, String arg1, Set<String> arg2) {
                // 状态码为0则注册成功
                if (status != 0) {
                    sendEmptyMessageDelayed(0, 1 * 1000);
                }
            }
        });

        count++;
    }

}
