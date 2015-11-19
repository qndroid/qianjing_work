/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.qianjing.finance.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.qianjing.finance.widget.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.qjautofinancial.R;

public class PullToRefreshGridView extends PullToRefreshAdapterViewBase<GridView>
{
	private boolean isShowGridLine = false;

	public PullToRefreshGridView(Context context)
	{
		super(context);
	}

	public PullToRefreshGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullToRefreshGridView(Context context, Mode mode)
	{
		super(context, mode);
	}

	public PullToRefreshGridView(Context context, Mode mode, AnimationStyle style)
	{
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection()
	{
		return Orientation.VERTICAL;
	}

	@Override
	protected final GridView createRefreshableView(Context context, AttributeSet attrs)
	{
		final GridView gv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD)
		{
			gv = new InternalGridViewSDK9(context, attrs);
		}
		else
		{
			gv = new InternalGridView(context, attrs);
		}
		gv.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// Use Generated ID (from res/values/ids.xml)
		gv.setId(R.id.gridview);
		return gv;
	}

	public void setShowGridLine(boolean isShowGridLine)
	{
		this.isShowGridLine = isShowGridLine;
	}

	class InternalGridView extends GridView implements EmptyViewMethodAccessor
	{

		public InternalGridView(Context context, AttributeSet attrs)
		{
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView)
		{
			PullToRefreshGridView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView)
		{
			super.setEmptyView(emptyView);
		}

		@Override
		protected void dispatchDraw(Canvas canvas)
		{
			super.dispatchDraw(canvas);
			if (isShowGridLine)
			{
				View localView1 = getChildAt(0);
				int column = getWidth() / localView1.getWidth();
				int childCount = getChildCount();
				Paint localPaint;
				localPaint = new Paint();
				localPaint.setStyle(Paint.Style.STROKE);
				localPaint.setColor(getContext().getResources().getColor(R.color.grid_line_color));
				for (int i = 0; i < childCount; i++)
				{
					View cellView = getChildAt(i);
					if ((i + 1) % column == 0)
					{
						canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(),
								cellView.getBottom(), localPaint);
					}
					else if ((i + 1) > (childCount - (childCount % column)))
					{
						canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(),
								cellView.getBottom(), localPaint);
					}
					else
					{
						canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(),
								cellView.getBottom(), localPaint);
						canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(),
								cellView.getBottom(), localPaint);
					}
				}
				if (childCount % column != 0)
				{
					for (int j = 0; j < (column - childCount % column); j++)
					{
						View lastView = getChildAt(childCount - 1);
						canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(),
								lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
					}
				}
				// draw bottom line
				View lastView = getChildAt(childCount - 1);
				canvas.drawLine(0, lastView.getBottom(), getWidth(), lastView.getBottom(), localPaint);
			}
		}
	}

	@TargetApi(9)
	final class InternalGridViewSDK9 extends InternalGridView
	{

		public InternalGridViewSDK9(Context context, AttributeSet attrs)
		{
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
		{

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshGridView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}
}
