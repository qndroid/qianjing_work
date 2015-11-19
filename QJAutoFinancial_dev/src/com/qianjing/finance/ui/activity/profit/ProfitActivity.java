package com.qianjing.finance.ui.activity.profit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.qianjing.finance.ui.activity.history.BaseHistoryActivity;
import com.qianjing.finance.ui.activity.profit.fragment.AssemableProfitFragment;
import com.qianjing.finance.ui.activity.profit.fragment.FundProfitFragment;
import com.qianjing.finance.ui.activity.profit.fragment.WalletProfitFragment;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：ProfitActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月25日 上午10:56:51
 * @文件描述：累计盈亏列表 Activity
 * @修改历史：2015年6月25日创建初始版本
 **********************************************************/
public class ProfitActivity extends BaseHistoryActivity implements OnClickListener
{
	public int CURRENT_POPUPITEM = 1;
	public static final int ASSEMBLE_PROFIT = 1;
	public static final int FUND_PROFIT = 2;
	public static final int WALLET_PROFIT = 3;

	private HistoryPopupowItem popupItem1;
	private HistoryPopupowItem popupItem2;
	private HistoryPopupowItem popupItem3;
	private int type = 0;// 不同入口类型

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_total);
		initData();
		initView();
	}

	private void initData()
	{
		type = getIntent().getIntExtra("type", 0);
	}

	@Override
	public void initView()
	{
		super.initView();
		setTitleBack();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		switch (type)
		{
		case 0:
			setFleibleTitleWithString("每日组合盈亏");
			beginTransaction.replace(R.id.fl_content, new AssemableProfitFragment(), "profit");
			break;
		case 1:
			setFleibleTitleWithString("每日基金盈亏");
			beginTransaction.replace(R.id.fl_content, new FundProfitFragment(), "profit");
			break;
		case 2:
			setFleibleTitleWithString("每日活期宝收益");
			beginTransaction.replace(R.id.fl_content, new WalletProfitFragment(), "profit");
			break;
		}
		beginTransaction.commit();
	}

	private Fragment getFragment(int index)
	{
		Fragment fragment = null;
		switch (index)
		{
		case 1:
			fragment = new AssemableProfitFragment();
			break;
		case 2:
			fragment = new FundProfitFragment();
			break;
		case 3:
			fragment = new WalletProfitFragment();
			break;
		}
		return fragment;
	}

	@Override
	public void initPopupView(View popupowView)
	{
		popupItem1 = (HistoryPopupowItem) popupowView.findViewById(R.id.hpi_popup_item1);
		popupItem2 = (HistoryPopupowItem) popupowView.findViewById(R.id.hpi_popup_item2);
		popupItem3 = (HistoryPopupowItem) popupowView.findViewById(R.id.hpi_popup_item3);
		popupItem1.setText("每日组合盈亏");
		popupItem2.setText("每日基金盈亏");
		popupItem3.setText("每日活期宝收益");
		setCurrentSelected(CURRENT_POPUPITEM);

		popupItem1.setOnClickListener(this);
		popupItem2.setOnClickListener(this);
		popupItem3.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();

		switch (v.getId())
		{
		case R.id.hpi_popup_item1:
			setCurrentSelected(ASSEMBLE_PROFIT);
			CURRENT_POPUPITEM = ASSEMBLE_PROFIT;
			setTitleWithString("每日组合盈亏");
			break;
		case R.id.hpi_popup_item2:
			setCurrentSelected(FUND_PROFIT);
			CURRENT_POPUPITEM = FUND_PROFIT;
			setTitleWithString("每日基金盈亏");
			break;
		case R.id.hpi_popup_item3:
			setCurrentSelected(WALLET_PROFIT);
			CURRENT_POPUPITEM = WALLET_PROFIT;
			setTitleWithString("每日活期宝收益");
			break;
		}

		beginTransaction.replace(R.id.fl_content, getFragment(CURRENT_POPUPITEM), "profit");
		beginTransaction.commit();
		if (popupWindow != null)
		{
			popupWindow.dismiss();
		}
	}

	public void setCurrentSelected(int index)
	{
		popupItem1.setChecked(false);
		popupItem2.setChecked(false);
		popupItem3.setChecked(false);
		switch (index)
		{
		case ASSEMBLE_PROFIT:
			popupItem1.setChecked(true);
			break;
		case FUND_PROFIT:
			popupItem2.setChecked(true);
			break;
		case WALLET_PROFIT:
			popupItem3.setChecked(true);
			break;
		}
	}
}
