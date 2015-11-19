package com.qianjing.finance.view.chartview;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：PieGraph.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月26日 下午3:21:24
 * @文件描述：自定义饼图
 * @修改历史：2015年5月26日创建初始版本
 **********************************************************/
public class PieGraph extends View
{

	/**
	 * 绘制部分
	 */
	private Context mContext;
	private Resources res;
	private DisplayMetrics dm;
	private Paint paint;
	private Path outerPath;
	private Path innerPath;
	/**
	 * data部分
	 */
	private ArrayList<PieSlice> slices;
	private int thickness;
	private float midX, midY, radius, innerRadius;
	private float padding = 0;
	private float currentAngle = 270;
	private float currentSweep = 0;
	private float textSize;
	private String key1, value1;
	private String key2, value2;

	private OnSliceClickedListener listener;

	public PieGraph(Context context)
	{
		this(context, null);
	}

	public PieGraph(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView()
	{

		res = mContext.getResources();
		dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		paint = new Paint();
		outerPath = new Path();
		innerPath = new Path();

		slices = new ArrayList<PieSlice>();
		key1 = mContext.getString(R.string.fund_type);
		key2 = mContext.getString(R.string.no_fund_type);
	}

	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.TRANSPARENT);
		paint.reset();
		paint.setAntiAlias(true);
		outerPath.reset();

		int totalValue = 0;

		for (PieSlice slice : slices)
		{
			totalValue += slice.getValue();
		}

		for (PieSlice slice : slices)
		{
			innerPath.reset();
			paint.setColor(slice.getColor());
			currentSweep = (slice.getValue() / totalValue) * (360);
			if (currentSweep == 0)
			{
				continue;
			}
			if (currentSweep == 360)
			{
				innerPath.addArc(new RectF(midX - radius, midY - radius, midX + radius, midY + radius), currentAngle
						+ padding, currentSweep - padding);
				innerPath.addArc(new RectF(midX - innerRadius, midY - innerRadius, midX + innerRadius, midY
						+ innerRadius), (currentAngle + padding) + (currentSweep - padding), -(currentSweep - padding));
				canvas.drawPath(innerPath, paint);
				break;
			}
			else
			{
				innerPath.arcTo(new RectF(midX - radius, midY - radius, midX + radius, midY + radius), currentAngle
						+ padding, currentSweep - padding);
				innerPath.arcTo(new RectF(midX - innerRadius, midY - innerRadius, midX + innerRadius, midY
						+ innerRadius), (currentAngle + padding) + (currentSweep - padding), -(currentSweep - padding));
				innerPath.close();
			}

			canvas.drawPath(innerPath, paint);

			currentAngle = currentAngle + currentSweep;
		}

		/**
		 * 绘制额外部分内容
		 */
		if (value1 != null && value2 != null)
		{
			drawExtraPart(canvas);
		}
	}

	private void drawText(Canvas canvas, String text, float x, float y)
	{
		canvas.drawText(text, x, y, paint);
	}

	/**
	 * 绘制额外部分
	 * 
	 * @param canvas
	 */
	private void drawExtraPart(Canvas canvas)
	{

		paint.setColor(res.getColor(R.color.color_999999));
		float lineStartY = thickness + innerRadius / 2;
		float lineEndY = lineStartY + innerRadius;
		canvas.drawLine(midX, lineStartY, midX, lineEndY, paint);

		paint.setTextSize(textSize);
		float drawTextX = innerRadius / 2 - paint.measureText(key1) / 2 + thickness + getWidth() / 2 - radius;
		float drawTextY = thickness + innerRadius / 2 + (int) (Math.abs(paint.descent() - paint.ascent()));
		drawText(canvas, key1, drawTextX, drawTextY);

		drawTextX = midX + innerRadius / 2 - paint.measureText(key2) / 2;
		drawTextY = thickness + innerRadius / 2 + (int) (Math.abs(paint.descent() - paint.ascent()));
		drawText(canvas, key2, drawTextX, drawTextY);

		paint.setColor(res.getColor(R.color.color_666666));
		drawTextX = innerRadius / 2 - paint.measureText(value1) / 2 + thickness + getWidth() / 2 - radius;
		drawTextY = thickness + innerRadius / 2 * 3;
		drawText(canvas, value1, drawTextX, drawTextY);

		drawTextX = midX + innerRadius / 2 - paint.measureText(value2) / 2;
		drawTextY = thickness + innerRadius / 2 * 3;
		drawText(canvas, value2, drawTextX, drawTextY);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		midX = getWidth() / 2;
		midY = getHeight() / 2;

		if (midX < midY)
		{
			radius = midX;
		}
		else
		{
			radius = midY;
		}

		thickness = dip2px(26);
		radius -= padding;
		innerRadius = radius - thickness;
		textSize = dip2px(12);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		return super.onTouchEvent(event);
	}

	public ArrayList<PieSlice> getSlices()
	{
		return slices;
	}

	public void setSlices(ArrayList<PieSlice> slices)
	{
		this.slices = slices;
		postInvalidate();
	}

	public PieSlice getSlice(int index)
	{
		return slices.get(index);
	}

	public void addSlice(PieSlice slice)
	{
		this.slices.add(slice);
		postInvalidate();
	}

	public void setOnSliceClickedListener(OnSliceClickedListener listener)
	{
		this.listener = listener;
	}

	public int getThickness()
	{
		return thickness;
	}

	public void setThickness(int thickness)
	{
		this.thickness = thickness;
		postInvalidate();
	}

	public void removeSlices()
	{
		for (int i = slices.size() - 1; i >= 0; i--)
		{
			slices.remove(i);
		}
		postInvalidate();
	}

	public void setDrawText(String value1, String value2, ArrayList<PieSlice> slice)
	{
		this.value1 = value1;
		this.value2 = value2;
		this.slices = slice;
		postInvalidate();
	}

	public void setDrawText(String key1, String value1, String key2, String value2, ArrayList<PieSlice> slice)
	{
		this.key1 = key1;
		this.value1 = value1;
		this.key2 = key2;
		this.value2 = value2;
		this.slices = slice;
		postInvalidate();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue)
	{
		return (int) (dpValue * dm.density + 0.5f);
	}

	public static interface OnSliceClickedListener
	{
		public abstract void onClick(int index);
	}

}
