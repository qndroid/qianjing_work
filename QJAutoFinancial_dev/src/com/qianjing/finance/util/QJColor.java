/**
 * Project Name:QJAutoFinancial_dev
 * File Name:QJColor.java
 * Package Name:com.qianjing.finance.util
 * Date:2015年9月29日下午5:05:07
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
*/

package com.qianjing.finance.util;
/**
 * ClassName:QJColor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年9月29日 下午5:05:07 <br/>
 * @author   liuchen
 * @version
 * @see
 */
public enum QJColor {
    
    PROFIT_RED(0xFFFF3B3B),PROFIT_GREEN(0xFF63CD99),MIDDLE_GREY(0XFF666666),ASSETS_TEXT(0XFF9098B4);
    private int color;
    private QJColor(int color) {
        this.color = color;
    }
    public int getColor() {
        return color;
    }
}

