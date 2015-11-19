package com.qianjing.finance.util;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.widget.TextView;

public class FormatNumberData {

	public static int PROFIT_RED  = 0xFFFF3B3B;
	public static int PROFIT_GREEN  = 0xFF63CD99;
	//88,888,888.88
	public static void formatNumber(float num,TextView tv){
		
		DecimalFormat myformat = new DecimalFormat();
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		String result = myformat.format(num);
		tv.setText(result);
		if(num==0){
			//金额大于0时字体设为红色
			tv.setTextColor(PROFIT_RED);
			tv.setText("0.00");
		}
		else if(num>0){
			//金额大于0时字体设为红色
			tv.setTextColor(PROFIT_RED);	
		}else{
			//金额小于0时字体设为绿色
			tv.setTextColor(PROFIT_GREEN);
		}
	}
	
	//+888.88 or -888.88
	public static void formatNumberPM(float num,TextView tv){
		
		DecimalFormat myformat = new DecimalFormat();
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		String result = myformat.format(num);
		
		if(num>=0){
			if(num==0){
				tv.setText("0.00");
			}else{
				//金额大于0时字体设为红色
				tv.setText("+"+result);
			}
			tv.setTextColor(PROFIT_RED);	
			
		}else{
			//金额小于0时字体设为绿色
			tv.setText(result);
			tv.setTextColor(PROFIT_GREEN);
		}
	}
	
	
	public static void simpleFormatNumber(float num,TextView tv){
		
		DecimalFormat myformat = new DecimalFormat();
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		
		String result = myformat.format(num);
		if(num==0){
			tv.setText("0.00");
		}else{
			tv.setText(result);
		}
	}
	
	public static void simpleFormatNumberPM(float num,TextView tv){
		
		DecimalFormat myformat = new DecimalFormat();
		
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		
		String result = myformat.format(num);
		
		if(num==0){
			tv.setText("0.00");
		}else if(num>0){
			tv.setText("+"+result);
		}else{
			tv.setText(result);
		}
	}
	
	public static void noneFormat(float num,TextView tv){
		tv.setText(num+"");
	}
	
	public static void simpleFormatNumberUnit(float num,TextView tv){
		
		DecimalFormat myformat = new DecimalFormat();
		
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		
		String result = myformat.format(num);
		
		if(num==0){
			tv.setText("¥0.00");
		}else{
			tv.setText("¥"+result);
		}
	}
	
	
	public static void formatNumberPR(float num,TextView tv,int operate){
		
		DecimalFormat myformat = new DecimalFormat();
		
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		
		String result = myformat.format(num);
		
		if(num==0){
			result = "0.00";
		}
		
		if(operate==2){
			tv.setText("-"+result);
			tv.setTextColor(PROFIT_GREEN);
		}else if(operate==1){
		    tv.setText("+"+result);
            tv.setTextColor(PROFIT_RED);
		}else if(operate==3){
		    tv.setText("-"+result);
            tv.setTextColor(PROFIT_GREEN);
		}else{
			tv.setText("+"+result);
			tv.setTextColor(PROFIT_RED);
		}
	}
	
	public static void formatNumberPR(TextView tv,int operate){
		
		if(operate==1){
			tv.setText("--");
			tv.setTextColor(PROFIT_RED);
		}else{
			tv.setText("--");
			tv.setTextColor(PROFIT_GREEN);
		}
	}
	
	public static void formatNumberPRUNIT(float num,TextView tv,int operate){
		
		DecimalFormat myformat = new DecimalFormat();
		
		if(num<1 && num>-1){
			myformat.applyPattern("0.##");
		}else{
			myformat.applyPattern("###,###,###.00");
		}
		
		String result = myformat.format(num);
		
		if(num==0){
			result = "0.00";
		}
		
		if(operate==1){
			tv.setText("¥"+result);
			tv.setTextColor(PROFIT_RED);
		}else{
			tv.setText("¥"+result);
			tv.setTextColor(PROFIT_GREEN);
		}
	}
	
}
