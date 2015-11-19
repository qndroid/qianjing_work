/**
 * Project Name:QJAutoFinancial_dev
 * File Name:TestUtil.java
 * Package Name:com.qianjing.finance.util
 * Date:2015年7月31日上午11:48:07
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
 */

package com.qianjing.finance.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtil {
    
    /**
     * 读取本地文件中JSON字符串
     * 
     * @param fileName
     * @return
     */
    public static String getJson(Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("json.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
