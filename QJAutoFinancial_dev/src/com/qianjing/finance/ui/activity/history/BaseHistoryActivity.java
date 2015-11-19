package com.qianjing.finance.ui.activity.history;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.view.custom.AnimPopupow;
import com.qianjing.finance.view.custom.AnimPopupow.DoSomethingSynchronous;
import com.qianjing.finance.view.custom.DynamicButton;
import com.qjautofinancial.R;

public abstract class BaseHistoryActivity extends BaseActivity {

	private LinearLayout llHistory;
	private DynamicButton dbArrow;
	public AnimPopupow popupWindow;
	private RelativeLayout iTitle;
	public View popupView;
	private boolean isShow = false;
	private View popupBg;
	private int width;
	private boolean isAndroid5 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Double.parseDouble(android.os.Build.VERSION.RELEASE.charAt(0)+"")>=5){
			isAndroid5 = true;
		}
	}
	
	public void initView(){
		llHistory = (LinearLayout) findViewById(R.id.ll_history);
		iTitle = (RelativeLayout) findViewById(R.id.i_title);
		popupBg = (View) findViewById(R.id.fl_popup_bg);
		
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    width = dm.widthPixels;
		
	    popupView = View.inflate(this, R.layout.popupow_history_layout, null);
		initPopupView(popupView);
		popupWindow = new AnimPopupow(isAndroid5,popupView,width,-2,true);
	};
	
	
	public void showPopupWindow(){
		
		if(popupView!=null && popupWindow!=null){
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			if(isAndroid5){
				popupWindow.setAnimationStyle(R.style.PopupAnimation);
			}
			popupWindow.setSomethingSynchronous(new DoSomethingSynchronous() {
				
				@Override
				public void run() {
					isShow = false;
					dbArrow.setChecked(isShow);
					popupBg.setVisibility(View.GONE);
					hideAnimation(popupBg);
				}
			});
			popupWindow.showAsDropDown(iTitle);
			popupBg.setVisibility(View.VISIBLE);
			showAnimation(popupBg);
		}
	}
	
	/**
	 * 实现该方法，为popupow填充对应数据和点击事件
	 * */
	public abstract void initPopupView(View popupowView);

	// 设置Title bar 标题栏
	protected final void setFleibleTitleWithString(String str)
	{
			TextView tvTitle = (TextView) findViewById(R.id.title_middle_text);
			dbArrow = (DynamicButton) findViewById(R.id.title_arrow);
			LinearLayout dynamicTitle = (LinearLayout) findViewById(R.id.ll_dynamic_title);
			if (tvTitle != null && dbArrow != null)
			{
				tvTitle.setText(str);
				dbArrow.setVisibility(View.VISIBLE);
				dynamicTitle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						isShow = !isShow;
						
						if(isShow){
							showPopupWindow();
						}else{
							if(popupWindow!=null){
								popupWindow.dismiss();
							}
						}
						dbArrow.setChecked(isShow);
					}
				});
			}
		}
	
	public void showAnimation(View view){
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
        view.startAnimation(alphaAnimation);
	}
	
	public void hideAnimation(View view){
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
        view.startAnimation(alphaAnimation);
	}
	
//		public void backgroundAlpha(float bgAlpha)
//	    {
//	        WindowManager.LayoutParams lp = getWindow().getAttributes();
//	        lp.alpha = bgAlpha; //0.0-1.0
//	                getWindow().setAttributes(lp);
//	    }
	
}
