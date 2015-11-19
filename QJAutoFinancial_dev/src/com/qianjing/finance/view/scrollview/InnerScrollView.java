package com.qianjing.finance.view.scrollview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class InnerScrollView extends ScrollView {

    private Context context;
    private int RANGE = 100;
    private int y1;
    private int range = 10;
    private int moveUnit = -1;
    
    public InnerScrollView(Context context) {
        super(context);
        this.context = context;
    }
    
    public InnerScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    public int getCurrentY(){
        return computeVerticalScrollOffset();
    }
    
    public void scrollToTarget(int target){
        scrollTo(0, target);
    }
    
    
    
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        
//        switch(ev.getAction()){
//            
//            case MotionEvent.ACTION_DOWN:
//                y1 = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int y2 = (int) ev.getY();
//                if(y2-y1>range){
//                    scrollToTarget();
//                }else{
//                    
//                }
//                
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//    
//    public void scrollToTarget(){
//        count = computeVerticalScrollOffset()/moveUnit;
//        handler.postDelayed(new TRunnable(), 100);
//        
//        
//    }
//    
//    Handler handler= new Handler();
//    private int count;
//    private class TRunnable implements Runnable{
//        @Override
//        public void run() {
//            if(count>0){
//                count--;
//                scrollBy(0, moveUnit);
//                handler.postDelayed(this, 100);
//            }else{
//            }
//        }
//    }
    
    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
    
}
