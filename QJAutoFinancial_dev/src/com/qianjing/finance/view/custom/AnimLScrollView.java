package com.qianjing.finance.view.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


/**********************************************************
 * @文件名称：VIPFreeScrollView.java
 * @文件作者：liuchen
 * @创建时间：2015年5月29日 下午1:58:00
 * @文件描述：可上下滑动ScrollView
 * @修改历史：2015年5月29日创建初始版本
 **********************************************************/
public class AnimLScrollView extends VIPFreeScrollView {

	private ScrollViewListener scrollViewListener = null;
	private int DELAY_TIME = 500;

	public AnimLScrollView(Context context) {
		super(context);
		initView();
	}
	
	private void initView() {
		
		this.setOnTouchListener(new OnTouchListener() {
			
			private int lastY = 0;
			private int touchEventId = 2015721;
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					View scroller = (View) msg.obj;
					if (msg.what == touchEventId) {
						if (lastY == scroller.getScrollY()) {
						    if(scrollViewListener!=null){
	                            scrollViewListener.onScrollViewStop();
						    }
						} else {
							handler.sendMessageDelayed(handler.obtainMessage(
									touchEventId, scroller), DELAY_TIME);
							lastY = scroller.getScrollY();
						}
					}
				}
			};
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					handler.sendMessageDelayed(
							handler.obtainMessage(touchEventId, v), DELAY_TIME);
				}
				return false;
			}
		});
	}

	public AnimLScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public void setOnScrollViewListener(
			ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}
	
	public interface ScrollViewListener {
		void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
		void onScrollViewStop();
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
