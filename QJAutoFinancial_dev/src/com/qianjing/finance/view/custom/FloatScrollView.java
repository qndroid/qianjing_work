package com.qianjing.finance.view.custom;

import com.qianjing.finance.view.scrollview.InnerScrollView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author liuchen
 *
 */
public class FloatScrollView extends AnimLScrollView {

    private View floatView;
    private Context context;
    final int[] locations = new int[2];
    private boolean isFloat = false;
    private int originIndex;
    private boolean getMess = true;
    private View mInsteadView = null;
    private View underView;
    private int mHeightInScreen;
    /**内部scrollView是否滑动到了边界*/
    private boolean isAtBourdary = true;
    private boolean isUpScroll = true;
    
    public FloatScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public FloatScrollView(Context context) {
        super(context);
        this.context = context;
    }
    
    /**
     * @param floatView
     * @param underView
     * @category this View must be floatView's grandparent
     */
    public void setFloatView(View floatView,View underView){
        this.floatView = floatView;
        this.underView = underView;
        
        floatView.setTag("float");
        mInsteadView = floatView;
        initFloatView(floatView);
    }
    
    /**
     * @param floatView
     */
    private void initFloatView(View floatView){
//        ((ViewGroup)this.getChildAt(0)).removeView(floatView);
        LayoutParams lp = (LayoutParams) this.getLayoutParams();
        ViewGroup group = (ViewGroup) this.getParent();
        FrameLayout container = new FrameLayout(context);
        
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index,lp);
        container.removeAllViews();
        container.addView(this);
        group.invalidate();
    }
    
    /**
     * @param floatView
     */
    private void applyToFloat(View floatView){
        
        originIndex = ((ViewGroup)this.getChildAt(0)).indexOfChild(floatView);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) floatView.getLayoutParams();
        View v = new View(context);
        v.setTag("view");
        ((ViewGroup)this.getChildAt(0)).removeView(floatView);
        ((ViewGroup)this.getChildAt(0)).addView(v, originIndex, lp);
        ViewGroup group = (ViewGroup) this.getParent();
        ((ViewGroup)group).addView(floatView);
        mInsteadView = v;
        group.invalidate();
    }
    
    /**
     * @param floatView
     */
    private void applyToNormal(View floatView){
        ViewGroup group = (ViewGroup) this.getParent();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((ViewGroup)this
                .getChildAt(0)).getChildAt(originIndex).getLayoutParams();
        ((ViewGroup)group).removeView(floatView);
        ((ViewGroup)this.getChildAt(0)).removeViewAt(originIndex);
        ((ViewGroup)this.getChildAt(0)).addView(floatView,originIndex,lp);
        mInsteadView = floatView;
        group.invalidate();
    }
    
    
   
    Handler handler = new Handler(){
        private int floatHeight;
        
        public void handleMessage(android.os.Message msg) {
            
            
            if(getMess && "float".equals(mInsteadView.getTag())){
              //this is float view and parent height minus in screen
                FloatScrollView.this.getLocationOnScreen(locations);
                mHeightInScreen = locations[1];
                floatHeight = floatView.getHeight();
                getMess = false;
            }
            
            underView.getLocationOnScreen(locations);
            int y2 = locations[1]-floatHeight;
            
            
            if(y2<=mHeightInScreen){
                if(!isFloat){
//                    Toast.makeText(context, "执行浮动"+"y2:"+y2+",y1:"+mHeightInScreen+",isFloat:"+isFloat, 1).show();
                    applyToFloat(floatView);
                    isFloat = true;
                    return ;
                }
            }
            if(y2>mHeightInScreen){
                if(isFloat){
//                    Toast.makeText(context, "取消浮动"+"y2:"+y2+",y1:"+mHeightInScreen+",isFloat:"+isFloat, 1).show();
                    applyToNormal(floatView);
                    if(isResetInnerPage && innerScrollView!=null){
                        innerScrollView.scrollToTarget(0);
                    }
                    isFloat = false;
                    return ;
                }
            }
        };
    };
    
    private int y1 = 0;
    private InnerScrollView innerScrollView;
    private boolean isResetInnerPage = false;
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        
        
        
        
        
        switch(ev.getAction()){
            
            case MotionEvent.ACTION_DOWN:
                y1 = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                
                int y2 = (int) ev.getY();
                
                if(y2>y1){
//                    Toast.makeText(context, "y2:"+y2+"y1:"+y1, 1).show();
                    //上滑
                    isUpScroll = true;
                    y1 = (int) ev.getY();
                    
                    return super.dispatchTouchEvent(ev);
                }else{
                    isUpScroll = false;
                    y1 = (int) ev.getY();
//                    Toast.makeText(context, "move"+isUpScroll, 1).show();
                    return super.dispatchTouchEvent(ev);
                }
                
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
        
    };
    
    @Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        
        underView.getLocationInWindow(locations);
        int yHeight = underView.getHeight();
        int y0 = locations[1];
        
        switch (ev.getAction()) {
            
            case MotionEvent.ACTION_DOWN:
                break;
                
                
            case MotionEvent.ACTION_MOVE:
                int y1 = (int) ev.getRawY();
                
                /**
                 * 在viewpager范围内放行事件的传递，当浮动效果触发时，子scrollView获得事件处理权限
                 * 子view滑动到边界时，改scrollView重新拦截到事件
                 * */
                if(y1>y0 && y1<y0+yHeight){
                    if(isFloat){
                        //only apply to special type screen
                        ViewGroup pagerGroup = ((ViewGroup)underView);
                        int count = pagerGroup.getChildCount();
                        
                        ViewPager viewPager = ((ViewPager)underView);
                        int currentItem = viewPager.getCurrentItem();
                        if(currentItem==0){
                            for(int i=0;i<count;i++){
                                
                                if(pagerGroup.getChildAt(i) instanceof InnerScrollView){
                                    isResetInnerPage = false;
                                    innerScrollView = (InnerScrollView)pagerGroup.getChildAt(i);
                                    if(innerScrollView.getCurrentY()<10){
                                        isAtBourdary = true;
                                    }else{
                                        isAtBourdary = false;
                                    }
                                    
                                    if(isAtBourdary && isUpScroll){
                                         return super.onInterceptTouchEvent(ev);
                                    }else{
                                        return false;
                                    }
                                    
                                }
                            }
                        }else{
                            isResetInnerPage = true;
                            return super.onInterceptTouchEvent(ev);
                        }
                        return false;
                    }
                }else{
                    
                    return super.onInterceptTouchEvent(ev);
                }
//              Toast.makeText(context, "move", 1).show();
                break;
            case MotionEvent.ACTION_UP:
//                Toast.makeText(context, "up", 1).show();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    };
    
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        handler.sendEmptyMessage(0);
//        Toast.makeText(context, "y="+y, 1).show();
    }
    
}
