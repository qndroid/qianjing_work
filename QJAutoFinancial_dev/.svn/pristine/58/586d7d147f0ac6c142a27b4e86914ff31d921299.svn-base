package com.qianjing.finance.view.custom;

import com.qianjing.finance.constant.CustomConstants;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

public class AnimPopupow extends PopupWindow
{

	/**
	 * @author:liuchen
	 * */

	View contentV;
	private boolean IS_RUNNING_ANIMATION = false;

	private DoSomethingSynchronous mDoSomethingSynchronous;
	private boolean isAndroid5;

	public AnimPopupow(boolean isAndroid5, View contentView, int width, int height, boolean focusable)
	{
		super(contentView, width, height, focusable);
		this.contentV = contentView;
		this.isAndroid5 = isAndroid5;
	}

	/**
	 * 销毁的同时执行动画效果
	 * */
	@Override
	public void dismiss()
	{
		if (!IS_RUNNING_ANIMATION)
		{
			IS_RUNNING_ANIMATION = true;
			if (mDoSomethingSynchronous != null)
			{
				mDoSomethingSynchronous.run();
			}
			if (isAndroid5)
			{
				IS_RUNNING_ANIMATION = false;
				realDismiss();
			}
			else
			{
				hideAnimation(contentV);
			}
		}
	}

	public void realDismiss()
	{
		super.dismiss();
	}

	/**
	 * 监听器
	 * */
	public interface DoSomethingSynchronous
	{
		void run();
	}

	/**
	 * 设置窗体销毁动画同步执行的动作
	 * */
	public void setSomethingSynchronous(DoSomethingSynchronous doSomethingSynchronous)
	{
		mDoSomethingSynchronous = doSomethingSynchronous;
	}

	/**
	 * 在设置完popupwindow后立即执行动画
	 * */
	@Override
	public void showAsDropDown(View anchor)
	{
		if (!IS_RUNNING_ANIMATION)
		{
			IS_RUNNING_ANIMATION = true;
			super.showAsDropDown(anchor);
			if (isAndroid5)
			{
				IS_RUNNING_ANIMATION = false;
			}
			else
			{
				showAnimation(contentV);
			}
		}
	}

	/**
	 * 显示popupwindow的动画效果
	 * */
	public void showAnimation(View view)
	{
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -400, 0);
		translateAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.9f, 1.0f);
		alphaAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
		AnimationSet animationSet = new AnimationSet(false);

		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(translateAnimation);
		animationSet.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				IS_RUNNING_ANIMATION = false;
			}
		});

		view.startAnimation(animationSet);
	}

	/**
	 * 隐藏popupwindow的动画效果
	 * */
	public void hideAnimation(View view)
	{
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -400);
		translateAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.9f);
		alphaAnimation.setDuration(CustomConstants.ANIMATION_DURATION);
		AnimationSet animationSet = new AnimationSet(false);

		animationSet.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				IS_RUNNING_ANIMATION = false;
				contentV.post(new Runnable()
				{
					@Override
					public void run()
					{
						realDismiss();
					}
				});
			}
		});

		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(translateAnimation);

		view.startAnimation(animationSet);
	}

}
