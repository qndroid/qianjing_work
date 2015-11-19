package com.qianjing.finance.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class RotateTextView extends TextView {

    public RotateTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    
    
    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public RotateTextView(Context context) {
        super(context);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-30, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
    

}
