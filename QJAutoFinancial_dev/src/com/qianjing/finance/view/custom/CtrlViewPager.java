package com.qianjing.finance.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**********************************************************
 * @文件名称：CtrlViewPager.java
 * @文件作者：liuchen
 * @创建时间：2015年5月29日 下午4:05:51
 * @文件描述：根据状态控制滑动ViewPager
 * @修改历史：2015年5月29日创建初始版本
 **********************************************************/
public class CtrlViewPager extends ViewPager {

	/**
	 * ViewPager滑动的三种状态，完全可滑，半边可滑，和不可滑
	 * */
	public static final int HALF_ENABLE_SCROLL = 1;
	public static final int ALL_ENABLE_SCROLL = 0;
	public static final int ALL_DISABLE_SCROLL = 2;
	
	private int stateCode = ALL_ENABLE_SCROLL;
	private int startX;

	
	public CtrlViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CtrlViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			
			if(stateCode==HALF_ENABLE_SCROLL){
				if (moveX - startX < 0) {
					startX = (int) ev.getX();
					return false;
				}else{
					startX = (int) ev.getX();
					return super.onInterceptTouchEvent(ev);
				}
			}else if(stateCode==ALL_DISABLE_SCROLL){
				return false;
			}
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	
	
}
