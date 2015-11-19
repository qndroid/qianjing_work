package com.qianjing.finance.view.custom;

import com.qianjing.finance.view.custom.listener.ScrollViewListener;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**********************************************************
 * @文件名称：VIPFreeScrollView.java
 * @文件作者：liuchen
 * @创建时间：2015年5月29日 下午1:58:00
 * @文件描述：可上下滑动ScrollView
 * @修改历史：2015年5月29日创建初始版本
 **********************************************************/
public class VIPFreeScrollView extends ScrollView
{

	private static final float MOVE_FACTOR = 0.5f;
	private static final int ANIM_DURING = 300;
	private View contentView;
	private float startY;
	private Rect originalRect = new Rect();

	private boolean canPullDown = false;
	private boolean canPullUp = false;
	private boolean isMoved = false;
	private ScrollViewListener scrollViewListener = null;

	public VIPFreeScrollView(Context context)
	{
		super(context);
	}

	public VIPFreeScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	// private int getScrollRange(){
	// return super.getScrollRange();
	// }

	@Override
	protected void onFinishInflate()
	{
		if (getChildCount() > 0)
		{
			contentView = getChildAt(0);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);

		if (contentView == null)
			return;

		originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
	}

	/**
	 * dispatch event
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{

		if (contentView == null)
		{
			return super.dispatchTouchEvent(ev);
		}

		int action = ev.getAction();

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:

			canPullDown = isCanPullDown();
			canPullUp = isCanPullUp();

			startY = ev.getY();
			break;

		case MotionEvent.ACTION_UP:

			if (!isMoved)
				break;

			TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
			anim.setDuration(ANIM_DURING);

			contentView.startAnimation(anim);

			contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);

			canPullDown = false;
			canPullUp = false;
			isMoved = false;

			break;
		case MotionEvent.ACTION_MOVE:

			if (!canPullDown && !canPullUp)
			{
				startY = ev.getY();
				canPullDown = isCanPullDown();
				canPullUp = isCanPullUp();

				break;
			}

			float nowY = ev.getY();
			int deltaY = (int) (nowY - startY);

			boolean shouldMove = (canPullDown && deltaY > 0) || (canPullUp && deltaY < 0) || (canPullUp && canPullDown);

			if (shouldMove)
			{
				int offset = (int) (deltaY * MOVE_FACTOR);
				contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right,
						originalRect.bottom + offset);

				isMoved = true;
			}

			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	private boolean isCanPullDown()
	{
		return getScrollY() == 0 || contentView.getHeight() < getHeight() + getScrollY();
	}

	private boolean isCanPullUp()
	{
		return contentView.getHeight() <= getHeight() + getScrollY();
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
			int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
	{
		// TODO Auto-generated method stub
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
				maxOverScrollY, isTouchEvent);
	}
	
	public void setScrollViewListener(ScrollViewListener scrollViewListener)
	{
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy)
	{
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null)
		{
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
