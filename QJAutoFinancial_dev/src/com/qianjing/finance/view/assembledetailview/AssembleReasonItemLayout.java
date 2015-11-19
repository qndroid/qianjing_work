package com.qianjing.finance.view.assembledetailview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：AssembleNewItemLayout.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月26日 下午3:23:16
 * @文件描述：组合赎回原因 item
 * @修改历史：2015年5月26日创建初始版本
 **********************************************************/
public class AssembleReasonItemLayout extends RelativeLayout
{
	private Context context;
	private RelativeLayout mContentView;
	/**
	 * 基金名称 View
	 */
	private TextView fundNameView;
	/**
	 * 赎回份额View
	 */
	private TextView fundRedeemView;
	/**
	 * 赎回状态View
	 */
	private TextView stateView;
	/**
	 * 失败原因View
	 */
	private TextView failReasonView;

	/**
	 * data
	 */
	private boolean isFailed;

	public AssembleReasonItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		initView();
	}

	public AssembleReasonItemLayout(Context context)
	{
		this(context, null);
	}

	private void initView()
	{
		mContentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_assemble_layout, this);
		fundNameView = (TextView) mContentView.findViewById(R.id.fund_name_view);
		fundRedeemView = (TextView) mContentView.findViewById(R.id.fund_redeem_view);
		stateView = (TextView) mContentView.findViewById(R.id.fund_percent_view);
		failReasonView = (TextView) mContentView.findViewById(R.id.reason_view);
		if (isFailed)
		{
			failReasonView.setVisibility(View.GONE);
		}
	}

	public void initData(String fundName, String fundRedeem, String state, String reason)
	{
		fundNameView.setText(fundName);
		fundRedeemView.setText(fundRedeem);
		stateView.setText(state);
		if (reason == null || reason.equals(""))
		{
			failReasonView.setVisibility(View.GONE);
		}
		else
		{
			failReasonView.setText(reason);
		}
	}
}
