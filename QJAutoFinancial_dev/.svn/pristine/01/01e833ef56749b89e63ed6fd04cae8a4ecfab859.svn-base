package com.qianjing.finance.view.assembledetailview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.view.custom.DynamicButton;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：AssembleNewItemLayout.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月26日 下午3:23:16
 * @文件描述：组合配比item
 * @修改历史：2015年5月26日创建初始版本
 **********************************************************/
public class AssembleNewItemLayout extends RelativeLayout
{

	private Context context;
	private Resources res;
	private RelativeLayout mContentView;
	/**
	 * 基金名称 View
	 */
	private TextView fundNameView;
	/**
	 * 基金代码View
	 */
	private TextView fdCodeView;

	/**
	 * 基金百分比View
	 */
	private TextView fundPrecentView;
	/**
	 * %号
	 */
	private TextView precentView;
	/**
	 * 基金指示器View
	 */
	private View indictorView;
    private ImageView rightArrow;

    public AssembleNewItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		res = getResources();
		initView();
	}

	public AssembleNewItemLayout(Context context)
	{
		this(context, null);
	}

	private void initView()
	{

		mContentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.assemble_new_item_layout, this);
		fundNameView = (TextView) mContentView.findViewById(R.id.fund_name_view);
		fdCodeView = (TextView) mContentView.findViewById(R.id.fund_code_view);
		fundPrecentView = (TextView) mContentView.findViewById(R.id.fund_percent_view);
		indictorView = (View) mContentView.findViewById(R.id.indictor_view);
		precentView = (TextView) mContentView.findViewById(R.id.percent_view);
		rightArrow = (ImageView) mContentView.findViewById(R.id.iv_right_arrow);
	}
	
	public void initData(int color, String fundName, String fundCode, String percent)
	{
		indictorView.setBackgroundColor(res.getColor(color));
		fundNameView.setText(fundName);
		fdCodeView.setText(fundCode);
		fundPrecentView.setTextColor(res.getColor(color));
		fundPrecentView.setText(percent);
		precentView.setTextColor(res.getColor(color));
	}
}
