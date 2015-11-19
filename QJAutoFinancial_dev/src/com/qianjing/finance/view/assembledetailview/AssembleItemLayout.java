package com.qianjing.finance.view.assembledetailview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：AssembleItemLayout.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月16日 下午7:26:37
 * @文件描述：组和赎回列表项
 * @修改历史：2015年6月16日创建初始版本
 **********************************************************/
public class AssembleItemLayout extends RelativeLayout
{
	private Context context;
	private RelativeLayout mContentView;
	private TextView fundNameView;
	private TextView fundPrecentView;
	private TextView fundRedeemView;

	public AssembleItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		initView();
	}

	public AssembleItemLayout(Context context)
	{
		this(context, null);
	}

	private void initView()
	{
		mContentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.assemble_item_layout, this);
		fundNameView = (TextView) mContentView.findViewById(R.id.fund_name_view);
		fundPrecentView = (TextView) findViewById(R.id.fund_percent_view);
		fundRedeemView = (TextView) findViewById(R.id.fund_redeem_view);
	}

	public void initData(String fundName, String percent)
	{
		fundNameView.setText(fundName);
		fundPrecentView.setText(percent);
	}

	public void initData(String fundName, String redeemPrecent, String percent)
	{
		fundRedeemView.setText(redeemPrecent);
		fundNameView.setText(fundName);
		fundPrecentView.setText(percent);
	}

	public void setRedeemText(String redeemPrecent)
	{
		fundRedeemView.setText(redeemPrecent);
	}

	public String getRedeemText()
	{
		return fundRedeemView.getText().toString();
	}
}
