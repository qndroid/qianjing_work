/**
 * Project Name:QJAutoFinancial_dev
 * File Name:ChartBackground.java
 * Package Name:com.qianjing.finance.view.chartview
 * Date:2015年10月8日下午4:33:24
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
*/

package com.qianjing.finance.view.chartview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * ClassName:ChartBackground <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年10月8日 下午4:33:24 <br/>
 * @author   liuchen
 * @version  
 * @see 	 
 */
public class ChartBackground extends View {
    
    private Paint mPaint;
    private int VeriLineNum = 5;
    private int leftOffset = 0;
    private int rightOffset = 0;
    private int topOffset = 0;
    private Context context;
    
    public ChartBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
    }

    public ChartBackground(Context context, AttributeSet attrs) {
        
        super(context, attrs);
        this.context = context;
        initPaint();
        
    }

    public ChartBackground(Context context) {
        
        super(context);
        this.context = context;
        initPaint();
    }
    
    private void initPaint(){
        
        leftOffset = (int) convertDpToPixel(50f);
        rightOffset = (int) convertDpToPixel(10f);
        topOffset = (int) convertDpToPixel(20f);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0XFF5A6572);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]
                {convertDpToPixel(4f)
                , convertDpToPixel(2f)
                , convertDpToPixel(4f)
                , convertDpToPixel(2f)}
        , 0));
        mPaint.setStrokeWidth(convertDpToPixel(1f));
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        
        Path path = new Path();
        path.moveTo(0, topOffset);
        path.lineTo(getWidth(), topOffset);
        
        path.moveTo(0, getHeight());
        path.lineTo(getWidth(), getHeight());
        
        int cellWidth = (getWidth()-leftOffset-rightOffset)/(VeriLineNum-1);
        
        for (int i = 0; i < VeriLineNum; i++) {
            
            int mOffset = getWidth()-rightOffset - i*cellWidth;
            path.moveTo(mOffset, topOffset);
            path.lineTo(mOffset, getHeight());
        }
        canvas.drawPath(path, mPaint);
    }
    
    private float convertDpToPixel(float dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
