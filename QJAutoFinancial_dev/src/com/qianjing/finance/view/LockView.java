package com.qianjing.finance.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.Util;
import com.qjautofinancial.R;

/**
 * Created by majinxin on 2015/3/3.
 */
public class LockView extends View {
	// 密码校验输入次数限制
	private int inputCount = 5;
	// 设置密码暂存变量
	private String tempStr = null;
	private LockViewListener lockViewListener;
	private Canvas canvas;
	public static final int POINT_STATE_NORMAL = 0;
	public static final int POINT_STATE_DRAWING = 1;
	public static final int POINT_STATE_ERRO = 3;
	private boolean poitIsInit = false, isErro = false, isDown = false,
			isUp = false, isNeedVerify = false;
	private MyPoint[][] points = new MyPoint[3][3];
	private Paint paint = new Paint();
	private ArrayList<MyPoint> drawingPoints = new ArrayList<MyPoint>();
	private Bitmap bitmapNormal, bitmapDrawing, bitmapErro;
	private float width, height, poitOffset, offsetX, offsetY, bitmapoffset,
			poitR, currX, currY;
	private boolean MODE_HIDE = false;
	private  int TOUCH_MODE = 3;
	public final int MODE_STANDRD = 0;
	public final int MODE_ONE = 1;
	public final int MODE_TWO = 2;
	public final int MODE_THREE = 3;
	/**
	 * mode one
	 * */
//	private int currentCircleR = 20;
//    private int currentLastCircleR = 40;
//    private int MINCIRCLER = 20;
//    private int MAXCIRCLER = 40;
    /**
     * mode three
     * */
    
//  private int currentCircleR = 10;
//  private int currentLastCircleR = 50;
    private boolean isAnimed = false;
    
	public void setIsNeedVerify(boolean isNeedVerify) {
		this.isNeedVerify = isNeedVerify;
	}
	
	public boolean getIsNeedVerify() {
		return this.isNeedVerify;
	}
	
	private class MyPoint {
		public MyPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public MyPoint(float x, float y, float i,float j,int key) {
			this.x = x;
			this.y = y;
			this.i = i;
			this.j = j;
			this.sKey = key;
		}
		public boolean isAnimBack = false;
		public boolean isUsedAnim;
		public boolean isInAnimation;
		public int sKey;
		int state = POINT_STATE_NORMAL;
		public float x;
		public float y;
		public float i;
		public float j;
		
		
	}

	public LockView(Context context) {
		this(context, null);
		initView();
		
		
	}

	public LockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        displayMetrics = getResources().getDisplayMetrics();
        
        int beforeAnim = 32;
        int afterAnim = 38;
        
        currentCircleR = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, beforeAnim, displayMetrics);
        currentLastCircleR = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, afterAnim, displayMetrics);
        MINCIRCLER = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, beforeAnim, displayMetrics);
        MAXCIRCLER = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, afterAnim, displayMetrics);
        
        outerCircle = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, beforeAnim, displayMetrics);
        innerCircle = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
        ringWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);
        
        poitR = currentCircleR;
        
        currentLastCircleRs = new int[] {currentLastCircleR,currentLastCircleR,currentLastCircleR
                ,currentLastCircleR,currentLastCircleR,currentLastCircleR
                ,currentLastCircleR,currentLastCircleR,currentLastCircleR};
        
    }

    public void setLockViewListener(LockViewListener lockViewListener) {
		this.lockViewListener = lockViewListener;
	}
	

	private void initPoint(Canvas canvas) {
		this.canvas = canvas;
		width = getWidth();
		height = getHeight();
		bitmapNormal = getBitmapFor(R.drawable.img_lock_normal);
		bitmapDrawing = getBitmapFor(R.drawable.img_lock_drawing);
		bitmapErro = getBitmapFor(R.drawable.img_lock_erro);
		
		if(TOUCH_MODE==MODE_STANDRD){
		    poitR = bitmapNormal.getWidth() / 2;
		}
		
		bitmapoffset = bitmapDrawing.getWidth() / 2;
		offsetX = width / 4 * 3;
		offsetY = (height - width) / 2;
		poitOffset = width / 4;
		offsetX = width / 4 - bitmapoffset;
		offsetY = (height - width) / 2;

		points[0][0] = new MyPoint(offsetX, offsetY + poitOffset,0,0, 1);
        points[0][1] = new MyPoint(offsetX + poitOffset, offsetY + poitOffset,
                0,1,2);
        points[0][2] = new MyPoint(offsetX + poitOffset * 2, offsetY
                + poitOffset, 0,2,3);

        points[1][0] = new MyPoint(offsetX, offsetY + poitOffset * 2,1,0, 4);
        points[1][1] = new MyPoint(offsetX + poitOffset, offsetY + poitOffset
                * 2, 1,1,5);
        points[1][2] = new MyPoint(offsetX + poitOffset * 2, offsetY
                + poitOffset * 2,1,2, 6);

        points[2][0] = new MyPoint(offsetX, offsetY + poitOffset * 3,2,0, 7);
        points[2][1] = new MyPoint(offsetX + poitOffset, offsetY + poitOffset
                * 3, 2,1,8);
        points[2][2] = new MyPoint(offsetX + poitOffset * 2, offsetY
                + poitOffset * 3,2,2, 9);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				// canvas.drawBitmap(bitmapNormal,points[i][j].x,points[i][j].y,paint);
				drawBitmap(points[i][j], canvas);
			}
		paint.setARGB(255, 255, 255, 255);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(poitR / 6);
		poitIsInit = true;
	}
	
	private void drawLine(MyPoint pointStart, MyPoint PointEnd, Canvas canvas) {
//	    int poitR = 0;
		canvas.drawLine(pointStart.x + poitR, pointStart.y + poitR, PointEnd.x
				+ poitR, PointEnd.y + poitR, paint);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    switch(msg.what){
		        case 0:
		            resetDrawedPointAndShow();
		            break;
		        case 1:
		            executeAnimation();
		            break;
		        case 2:
		            executeBackAnimation((MyPoint)msg.obj);
		            break;
		    }
		}
	};
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!poitIsInit) {
			initPoint(canvas);
		}
		
		for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                drawBitmap(points[i][j], canvas);
            }
        }
		
		if(!MODE_HIDE){
		    paint.setColor(Color.WHITE);
		    paint.setStrokeWidth(20);
		    for (int i = 0; i < drawingPoints.size() - 1; i++) {
	            drawLine(drawingPoints.get(i), drawingPoints.get(i + 1), canvas);
	        }
	        if (drawingPoints.size() > 0 && isDown == true && isUp == false
	                && !(currX == 0 || currY == 0)) {
	            int index = drawingPoints.size() - 1;
	            canvas.drawLine(drawingPoints.get(index).x + poitR,
	                    drawingPoints.get(index).y + poitR, currX, currY, paint);
	        }
		}
		
		
            
	}
	
	



    @Override
    public boolean onHoverEvent(MotionEvent event) {
        if (isEnabled()) {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    event.setAction(MotionEvent.ACTION_DOWN);
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    event.setAction(MotionEvent.ACTION_MOVE);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    event.setAction(MotionEvent.ACTION_UP);
                    break;
            }
            onTouchEvent(event);
            event.setAction(action);
        }
        return super.onHoverEvent(event);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isDown = true;
			isUp = false;
			handleActionDown(event);
			return true;
		case MotionEvent.ACTION_MOVE:
		    isDown = true;
            isUp = false;
			handleActionMove(event);
			return true;
		case MotionEvent.ACTION_UP:
			isDown = false;
			isUp = true;
			hadleActionUp(event);
			return true;
		case MotionEvent.ACTION_CANCEL:
//		    LogUtils.syso("锁屏滑中断。。。。");
		    invalidate();
		    return true;
		}
		return false;
	}
	
	private void handleActionDown(MotionEvent event) {
		currX = 0;
		currY = 0;
		lockViewListener.onDrawingStart();
		setPointsToNormal();
		invalidate();
		float x = event.getX();
		float y = event.getY();
		MyPoint mPoint = touchedPoint(x, y);
		if (mPoint != null) {
			drawingPoints.add(mPoint);
			invalidate();
		}
		// if (mPoint!=null)drawBitmap(mPoint, canvas);
	}
	
	private void handleActionMove(MotionEvent event) {
		currX = event.getX();
		currY = event.getY();
		MyPoint mPoint = touchedPoint(currX, currY);
		if (mPoint != null){
		    
		    /**
	         * 加入相关点的判断
	         * */
	        if(drawingPoints.size()>0){
	            addThroughtPoint(drawingPoints.get(drawingPoints.size()-1)
	                    ,mPoint);
	        }
		    
			if (!drawingPoints.contains(mPoint)) {
				mPoint.state = LockView.POINT_STATE_DRAWING;
				drawingPoints.add(mPoint);
			}
		}
		invalidate();
	}
	
	
	
	private void addThroughtPoint(MyPoint startPoint,MyPoint endPoint) {
	    
	    float i1 = (startPoint.i + endPoint.i)/2;
	    int i2 = (int) i1;
	    float j1 = (startPoint.j + endPoint.j)/2;
        int j2 = (int) j1;
        
        
	    if(i1==(float)i2 && j1==(float)j2){
	        if(i2>=0 && i2<=2 && j2>=0 && j2<=2 && !(i2==0 && j2==0)){
	            MyPoint myPoint = points[i2][j2];
	            if(myPoint!=null && !drawingPoints.contains(myPoint)){
	                myPoint.state = LockView.POINT_STATE_DRAWING;
	              drawingPoints.add(myPoint);
	            }
	        }
	    }
    }
	
	
    private void hadleActionUp(MotionEvent event) {
		currX = 0;
		currY = 0;
		lockViewListener.onDrawingFinished();
		
		if (drawingPoints.size() < 1)
			return;
		String str = "";
		for (MyPoint dpointt : drawingPoints) {
			str = str + dpointt.sKey;
		}
		Message obtain = Message.obtain();
        obtain.what = 0;
		if (isNeedVerify) {
			if (str.equals(PrefUtil.getKey(getContext()).trim())) {
				// TODO 验证正确
				lockViewListener.onVerifyIsOK(true, -2);
				inputCount = 5;
				// System.out.println("验证正确");
				
				handler.sendMessageDelayed(obtain, 500);
				return;
			} else {// 验证错误
				inputCount--;
				lockViewListener.onVerifyIsOK(false, inputCount);
				for (int i = 0; i < drawingPoints.size(); i++) {
					drawingPoints.get(i).state = LockView.POINT_STATE_ERRO;
				}
				invalidate();
				handler.sendMessageDelayed(obtain, 500);
				return;
			}
		} else {
		    
			if (str.length() < 4) {
				lockViewListener.onInputIsTwoShort();
				return;
			}
			if (tempStr == null) {
				tempStr = str;
				lockViewListener.onIsNeededInput();
			} else {
				if (str.equals(tempStr)) {
					lockViewListener.onSetPassOK();
					PrefUtil.saveKey(getContext(), str);
					tempStr = null;
					
				} else {
					for (int i = 0; i < drawingPoints.size(); i++) {
						drawingPoints.get(i).state = LockView.POINT_STATE_ERRO;
					}
					invalidate();
					lockViewListener.onIsNotMatchFirst();
				}
			}
		}
		handler.sendEmptyMessage(0);
		lockViewListener.onDrawingFinishedKeyStr(str);
	}

	/**
	 * 判断触摸带你是否在点的半径之内 在半径之内则返回点对象 不是则返回null
	 */
	private MyPoint touchedPoint(float rx, float ry) {
	    
	    for (int i = 0; i < 3; i++){
	        for (int j = 0; j < 3; j++) {
	            points[i][j].isInAnimation = false;
	        }
	    }
        
	    
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				if ((points[i][j].x - rx + poitR)
						* (points[i][j].x - rx + poitR)
						+ (points[i][j].y - ry + poitR)
						* (points[i][j].y - ry + poitR) < poitR * poitR) {
					points[i][j].state = LockView.POINT_STATE_DRAWING;
					points[i][j].isInAnimation = true;
					points[i][j].isUsedAnim = true;
					return points[i][j];
				}
			}
		isAnimed = false;
		return null;
	}

	public void resetDrawedPointAndShow() {
		setPointsToNormal();
		invalidate();
	}
	
	private void setPointsToNormal() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				points[i][j].state = LockView.POINT_STATE_NORMAL;
				drawingPoints.clear();
			}
	}
	
	
	
	
    private DisplayMetrics displayMetrics;
    private int currentCircleR;
    private int currentLastCircleR;
    private int MINCIRCLER;
    private int MAXCIRCLER;
    private int outerCircle;
    private int innerCircle;
    private int ringWidth;
    private int[] currentLastCircleRs;
	
	private void drawBitmap(MyPoint point, Canvas canvas) {
	    
	    if(TOUCH_MODE==MODE_STANDRD){
	        switch (point.state) {
	            case LockView.POINT_STATE_NORMAL:
	                canvas.drawBitmap(bitmapNormal, point.x, point.y, paint);
	                break;
	            case LockView.POINT_STATE_DRAWING:
	                canvas.drawBitmap(bitmapDrawing, point.x, point.y, paint);
	                break;
	            case LockView.POINT_STATE_ERRO:
	                canvas.drawBitmap(bitmapErro, point.x, point.y, paint);
	                break;
	            }
	        
//	        
	        
	    }else if(TOUCH_MODE==MODE_ONE){
	        switch (point.state) {
                case LockView.POINT_STATE_NORMAL:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    
                    canvas.drawCircle(point.x+poitR, point.y+poitR, 10, paint);
                    
                    
                    break;
                case LockView.POINT_STATE_DRAWING:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    if(point.isInAnimation){
                        if(isAnimed){
                            canvas.drawCircle(point.x+poitR, point.y+poitR, currentCircleR, paint);
                        }else{
                            handler.sendEmptyMessage(1);
                        }
                    }else{
                        if(point.isUsedAnim){
                            if(!point.isAnimBack){
                                point.isAnimBack = true;
                                Message msg = Message.obtain();
                                msg.obj = point;
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }else{
                                canvas.drawCircle(point.x+poitR, point.y+poitR, currentLastCircleRs[point.sKey-1], paint);
                            }
                        }else{
                            canvas.drawCircle(point.x+poitR, point.y+poitR, MINCIRCLER, paint);
                        }
                    }
                    
                    break;
                case LockView.POINT_STATE_ERRO:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, 10, paint);
                    
                    break;
                }
	    }else if(TOUCH_MODE==MODE_TWO){
	        switch (point.state) {
                case LockView.POINT_STATE_NORMAL:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, 10, paint);
                    break;
                case LockView.POINT_STATE_DRAWING:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, 20, paint);
                    
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(10);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, outerCircle, this.paint);  
                    
                    break;
                case LockView.POINT_STATE_ERRO:
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, 10, paint);
                    
                    paint.setColor(Color.RED);
                    paint.setStrokeWidth(10);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(point.x+poitR, point.y+poitR, outerCircle, this.paint);  
//                    canvas.drawBitmap(bitmapErro, point.x, point.y, paint);
                    break;
                }
	    }else if(TOUCH_MODE==MODE_THREE){
	        switch (point.state) {
              case LockView.POINT_STATE_NORMAL:
                  paint.setColor(Color.WHITE);
//                  paint.setStyle(Paint.Style.FILL_AND_STROKE);
//                  canvas.drawCircle(point.x+poitR, point.y+poitR, 10, paint);
                  
                  paint.setStrokeWidth(ringWidth);
                  paint.setStyle(Paint.Style.STROKE);
                  canvas.drawCircle(point.x+poitR, point.y+poitR, outerCircle, paint);
                  break;
              case LockView.POINT_STATE_DRAWING:
                  paint.setColor(Color.WHITE);
                  paint.setStyle(Paint.Style.FILL_AND_STROKE);
                  paint.setStrokeWidth(ringWidth);
                  canvas.drawCircle(point.x+poitR, point.y+poitR, innerCircle, paint);
                  
                  paint.setColor(Color.WHITE);
                  paint.setStrokeWidth(ringWidth);
                  paint.setStyle(Paint.Style.STROKE);
//                canvas.drawCircle(point.x+poitR, point.y+poitR, innerCircle, this.paint);  
                  
                  if(point.isInAnimation){
                      if(isAnimed){
                          canvas.drawCircle(point.x+poitR, point.y+poitR, currentCircleR, paint);
                      }else{
                          handler.sendEmptyMessage(1);
                      }
                  }else{
                      if(point.isUsedAnim){
                          
                          if(!point.isAnimBack){
                              point.isAnimBack = true;
                              Message msg = Message.obtain();
                              msg.obj = point;
                              msg.what = 2;
                              handler.sendMessage(msg);
                          }else{
                              canvas.drawCircle(point.x+poitR, point.y+poitR, currentLastCircleRs[point.sKey-1], paint);
                          }
                           
                           
                      }else{
                          canvas.drawCircle(point.x+poitR, point.y+poitR, MINCIRCLER, paint);
                      }
                  }
                  
                  
                  break;
              case LockView.POINT_STATE_ERRO:
                  paint.setColor(Color.WHITE);
                  paint.setStyle(Paint.Style.FILL_AND_STROKE);
                  canvas.drawCircle(point.x+poitR, point.y+poitR, innerCircle, paint);
                  
                  paint.setColor(Color.RED);
                  paint.setStrokeWidth(ringWidth);
                  paint.setStyle(Paint.Style.STROKE);
                  canvas.drawCircle(point.x+poitR, point.y+poitR, outerCircle, this.paint);  
                  break; 
              }
	    }
	}
	
	protected void executeAnimation() {
	    
	    if(currentCircleR==MAXCIRCLER){
	        if(!isAnimed){
	            currentCircleR = MINCIRCLER;
	            handler.sendEmptyMessage(1);
	        }
	        return;
	    }
	    isAnimed = true;
	    currentCircleR ++ ;
	    invalidate();
        Message obtain = Message.obtain();
        obtain.what = 1;
        handler.sendMessageDelayed(obtain, 5);
        
    }
	
    protected void executeBackAnimation(MyPoint point) {
        
        if(point==null){
            return ;
        }
        
        if(!point.isUsedAnim){
            return ;
        }
        
        if(currentLastCircleRs[point.sKey-1]==MINCIRCLER){
            currentLastCircleRs[point.sKey-1] = MAXCIRCLER;
            point.isUsedAnim = false;
            point.isAnimBack = false;
            return;
        }
//        LogUtils.syso("点力回弹。"+currentLastCircleRs[point.sKey-1]);
        currentLastCircleRs[point.sKey-1] -- ;
        invalidate();
        Message obtain = Message.obtain();
        obtain.what = 2;
        obtain.obj = point;
        handler.sendMessageDelayed(obtain, 5);
        
    }
	
    
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private Bitmap getBitmapFor(int resId) {
		return BitmapFactory.decodeResource(getContext().getResources(), resId);
	}

	public static interface LockViewListener {
		/**
		 * 设置密码第二次与第一次不一样调用
		 */
		public void onIsNotMatchFirst();

		/**
		 * 设置密码成功调用
		 */
		public void onSetPassOK();

		/**
		 * 设置密码需要再次输入密码
		 */
		public void onIsNeededInput();

		/**
		 * 设置密码输入的密码小于4位调用
		 */
		public void onInputIsTwoShort();

		/**
		 * 图形密码绘制开始调用 成功为true 失败为false
		 * 
		 * @param isOK
		 */
		public void onVerifyIsOK(boolean isOK, int count);

		/**
		 * 图形密码绘制开始调用
		 */
		public void onDrawingStart();

		/**
		 * 图形密码绘制结束调用
		 */
		public void onDrawingFinished();

		/**
		 * 获取绘制好的密码值字符串
		 * 
		 * @param keyStr
		 */
		public void onDrawingFinishedKeyStr(String keyStr);
	}

}
