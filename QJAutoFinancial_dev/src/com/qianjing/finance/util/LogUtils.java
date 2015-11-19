package com.qianjing.finance.util;

import android.util.Log;

public class LogUtils {

	private static boolean debug = true;

    public static void i(Object obj,String msg){
    	
        if(debug){
            Log.i(obj.getClass().getSimpleName(),msg);
        }
        
    }
    
    public static void syso(String msg){
    	
        if(debug){
            System.out.println(msg);
        }
        
    }
	
	
}
