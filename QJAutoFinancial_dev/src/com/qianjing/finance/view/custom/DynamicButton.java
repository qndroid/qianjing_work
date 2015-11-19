package com.qianjing.finance.view.custom;

import com.qianjing.finance.constant.CustomConstants;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class DynamicButton extends ImageView {

	private boolean isChecked = false;
	
	public DynamicButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public DynamicButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public DynamicButton(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		
	}
	
	public void useAnimation(){
		if(isChecked){
			showView(DynamicButton.this);
			isChecked = false;
		}else{
			hideView(DynamicButton.this);
			isChecked = true;
		}
	};

	public void setChecked(boolean isChecked){
		
		this.isChecked = isChecked;
		useAnimation();
	}
	
	public boolean isChecked(){
		return isChecked;
	}
	
	protected void hideView(DynamicButton dynamicButton) {
		RotateAnimation rotateAnim = new RotateAnimation(180, 360, dynamicButton.getWidth()/2, dynamicButton.getHeight()/2);
		rotateAnim.setDuration(CustomConstants.ANIMATION_DURATION);
		rotateAnim.setFillAfter(true);
		dynamicButton.startAnimation(rotateAnim);
	}


	protected void showView(DynamicButton dynamicButton) {
		RotateAnimation rotateAnim = new RotateAnimation(0, 180, dynamicButton.getWidth()/2, dynamicButton.getHeight()/2);
		rotateAnim.setDuration(CustomConstants.ANIMATION_DURATION);
		rotateAnim.setFillAfter(true);
		dynamicButton.startAnimation(rotateAnim);
		
	}

}