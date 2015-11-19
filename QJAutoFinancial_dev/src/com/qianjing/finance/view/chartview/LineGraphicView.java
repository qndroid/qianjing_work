package com.qianjing.finance.view.chartview;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：LineGraphicView.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月27日 下午3:05:19
 * @文件描述： 自定义曲线图
 * @修改历史：2015年5月27日创建初始版本
 **********************************************************/
public class LineGraphicView extends View
{
	/**
	 * 公共部分
	 */
	private int outterCircleRadiu;
	private int innerCircleRadiu;

	private static enum Linestyle
	{
		Line, Curve
	}

	private Context mContext;
	private DisplayMetrics dm;
	private Resources res;
	/**
	 * 主画笔
	 */
	private Paint mPaint;
	/**
	 * 阴影画笔
	 */
	private Paint mShadowPaint;
	/**
	 * 阴影路径
	 */
	private Path mPath;
	private Point startp;
	private Point endp;

	/**
	 * data
	 */
	private Linestyle mStyle = Linestyle.Curve;

	private int canvasHeight;
	private int canvasWidth;
	private int bheight = 0;
	private int blwidh;
	private boolean isMeasure = true;

	/**
	 * Y轴最大值
	 */
	private int maxValue;
	/**
	 * Y轴间距值
	 */
	private int averageValue;
	private int marginTop;
	private int marginBottom;

	/**
	 * 曲线上总点数
	 */
	private Point[] mPoints;
	/**
	 * 纵坐标值
	 */
	private ArrayList<Double> yRawData;
	/**
	 * 横坐标值
	 */
	private ArrayList<String> xRawDatas;
	private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
	private int spacingHeight;

	public LineGraphicView(Context context)
	{
		this(context, null);
	}

	public LineGraphicView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView()
	{
		this.res = mContext.getResources();
		this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mPath = new Path();
		/**
		 * 阴影部分画笔
		 */
		this.mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mShadowPaint.setStyle(Style.FILL);
		this.mShadowPaint.setColor(res.getColor(R.color.color_fd4634));
		this.mShadowPaint.setStrokeWidth(4);
		this.mShadowPaint.setAlpha(128);

		dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		if (isMeasure)
		{
			this.canvasHeight = getHeight();
			this.canvasWidth = getWidth();
			marginTop = dip2px(10);
			marginBottom = dip2px(25);
			outterCircleRadiu = dip2px(2.5f);
			innerCircleRadiu = dip2px(1.5f);
			if (bheight == 0)
				bheight = (int) (canvasHeight - marginBottom);
			blwidh = dip2px(30);
			isMeasure = false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		mPaint.setColor(res.getColor(R.color.color_dddddd));
		mPaint.setStrokeWidth(dip2px(0.5f));
		drawAllXLine(canvas);
		// 画直线（纵向）
		drawAllYLine(canvas);
		// 点的操作设置
		mPoints = getPoints();

		mPaint.setColor(res.getColor(R.color.color_fd4634));
		mPaint.setStrokeWidth(dip2px(1.7f));
		mPaint.setStyle(Style.STROKE);
		if (mStyle == Linestyle.Curve)
		{
			drawScrollLine(canvas);
		}
		else
		{
			drawLine(canvas);
		}

		mPaint.setStyle(Style.FILL);
		for (int i = 0; i < mPoints.length; i++)
		{
			mPaint.setColor(res.getColor(R.color.color_fd4634));
			canvas.drawCircle(mPoints[i].x, mPoints[i].y, outterCircleRadiu, mPaint);
			mPaint.setColor(res.getColor(R.color.white));
			canvas.drawCircle(mPoints[i].x, mPoints[i].y, innerCircleRadiu, mPaint);
		}
	}

	/**
	 *  画所有横向表格，包括X轴
	 */
	private void drawAllXLine(Canvas canvas)
	{
		int tempSpace = (bheight / spacingHeight);
		for (int i = 0; i < spacingHeight + 1; i++)
		{
			canvas.drawLine(blwidh, bheight - tempSpace * i + marginTop, canvasWidth, bheight - tempSpace * i
					+ marginTop, mPaint);// Y坐标
			drawText(String.valueOf(averageValue * i), blwidh / 2, bheight - tempSpace * i + marginTop, canvas);
		}
	}

	/**
	 * 画所有纵向表格，包括Y轴	
	 */
	private void drawAllYLine(Canvas canvas)
	{
		/**
		 * Y轴间距
		 */
		int tempSpace = (canvasWidth - blwidh) / yRawData.size();
		for (int i = 0; i < yRawData.size(); i++)
		{
			xList.add(blwidh + tempSpace * i);
			canvas.drawLine(blwidh + tempSpace * i, marginTop + dip2px(0.6f), blwidh + tempSpace * i, bheight
					+ marginTop, mPaint);
			drawText(xRawDatas.get(i), blwidh + tempSpace * i, bheight + marginBottom, canvas);// X坐标
		}
	}

	private void drawScrollLine(Canvas canvas)
	{
		for (int i = 0; i < mPoints.length - 1; i++)
		{
			startp = mPoints[i];
			endp = mPoints[i + 1];
			int wt = (startp.x + endp.x) / 2;
			Point p3 = new Point();
			Point p4 = new Point();
			p3.y = startp.y;
			p3.x = wt;
			p4.y = endp.y;
			p4.x = wt;

			mPath.moveTo(startp.x, bheight + marginTop);
			mPath.lineTo(startp.x, startp.y);
			mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
			mPath.lineTo(endp.x, bheight + marginTop);
			mPath.close();
			canvas.drawPath(mPath, mShadowPaint);

			mPath.reset();
			mPath.moveTo(startp.x, startp.y);
			mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
			canvas.drawPath(mPath, mPaint);

			mPath.reset();

		}
	}

	private void drawLine(Canvas canvas)
	{
		Point startp = new Point();
		Point endp = new Point();
		for (int i = 0; i < mPoints.length - 1; i++)
		{
			startp = mPoints[i];
			endp = mPoints[i + 1];
			canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
		}
	}

	private void drawText(String text, int x, int y, Canvas canvas)
	{
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(dip2px(12));
		p.setColor(res.getColor(R.color.color_999999));
		p.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(text, x, y, p);
	}

	private Point[] getPoints()
	{
		Point[] points = new Point[yRawData.size()];
		for (int i = 0; i < yRawData.size(); i++)
		{
			int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));

			points[i] = new Point(xList.get(i), ph + marginTop);
		}
		return points;
	}

	public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData, int maxValue, int averageValue)
	{
		this.maxValue = maxValue;
		this.averageValue = averageValue;
		this.mPoints = new Point[yRawData.size()];
		this.xRawDatas = xRawData;
		this.yRawData = yRawData;
		this.spacingHeight = maxValue / averageValue;
	}

	public void setTotalvalue(int maxValue)
	{
		this.maxValue = maxValue;
	}

	public void setPjvalue(int averageValue)
	{
		this.averageValue = averageValue;
	}

	public void setMargint(int marginTop)
	{
		this.marginTop = marginTop;
	}

	public void setMarginb(int marginBottom)
	{
		this.marginBottom = marginBottom;
	}

	public void setMstyle(Linestyle mStyle)
	{
		this.mStyle = mStyle;
	}

	public void setBheight(int bheight)
	{
		this.bheight = bheight;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue)
	{
		return (int) (dpValue * dm.density + 0.5f);
	}
}
