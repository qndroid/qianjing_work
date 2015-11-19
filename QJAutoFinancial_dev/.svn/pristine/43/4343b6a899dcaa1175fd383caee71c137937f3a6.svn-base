package com.qianjing.finance.ui.activity.history;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.ui.activity.history.fragment.WalletHistoryFragment;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qjautofinancial.R;

public class WalletHistoryActivity extends BaseHistoryActivity implements
		OnClickListener {

	public int CURRENT_POPUPITEM = 1;

	public static final int ALL_HISTORY = 1;
	public static final int PURCHASE_HISTORY = 2;
	public static final int QUICK_HISTORY = 3;
	public static final int REDEEM_HISTORY = 4;

	private HistoryPopupowItem popupItem1;
	private HistoryPopupowItem popupItem2;
	private HistoryPopupowItem popupItem3;
	private HistoryPopupowItem popupItem4;

	private WalletHistoryFragment wallet1;

	private WalletHistoryFragment wallet2;

	private WalletHistoryFragment wallet3;

	private WalletHistoryFragment wallet4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_total);
		initView();
		setTitleBack();
		setFleibleTitleWithString("所有交易");
	}

	@Override
	public void initView() {
		super.initView();
		setCurrentFragment(1);
	}

	private void setCurrentFragment(int index) {

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();

		if (wallet1 != null) {
			beginTransaction.hide(wallet1);
		}
		if (wallet2 != null) {
			beginTransaction.hide(wallet2);
		}
		if (wallet3 != null) {
			beginTransaction.hide(wallet3);
		}
		if (wallet4 != null) {
			beginTransaction.hide(wallet4);
		}

		switch (index) {
		case 1:
			if (wallet1 == null) {
				wallet1 = new WalletHistoryFragment();
				wallet1.setWalletParameter(CustomConstants.DEFAULT_VALUE);
				beginTransaction.add(R.id.fl_content, wallet1);
			} else {
				beginTransaction.show(wallet1);
			}
			break;
		case 2:
			if (wallet2 == null) {
				wallet2 = new WalletHistoryFragment();
				wallet2.setWalletParameter(1);
				beginTransaction.add(R.id.fl_content, wallet2);
			} else {
				beginTransaction.show(wallet2);
			}
			break;
		case 3:
			if (wallet3 == null) {
				wallet3 = new WalletHistoryFragment();
				wallet3.setWalletParameter(2);
				beginTransaction.add(R.id.fl_content, wallet3);
			} else {
				beginTransaction.show(wallet3);
			}
			break;
		case 4:
			if (wallet4 == null) {
				wallet4 = new WalletHistoryFragment();
				wallet4.setWalletParameter(3);
				beginTransaction.add(R.id.fl_content, wallet4);
			} else {
				beginTransaction.show(wallet4);
			}
			break;
		}

		beginTransaction.commit();

	}

	@Override
	public void initPopupView(View popupowView) {

		popupItem1 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item1);
		popupItem2 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item2);
		popupItem3 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item3);
		popupItem4 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item4);
		View vHide = popupowView.findViewById(R.id.v_hide_view);
		vHide.setVisibility(View.VISIBLE);
		popupItem4.setVisibility(View.VISIBLE);

		popupItem1.setText("所有交易");
		popupItem2.setText("累计转入");
		popupItem3.setText("快速转出");
		popupItem4.setText("普通转出");

		popupItem1.setIcon(R.drawable.history_all_pupow);
		popupItem2.setIcon(R.drawable.history_purchase_pupow);
		popupItem3.setIcon(R.drawable.history_quick_pupow);
		popupItem4.setIcon(R.drawable.history_redeem_pupow);

		popupItem1.setOnClickListener(this);
		popupItem2.setOnClickListener(this);
		popupItem3.setOnClickListener(this);
		popupItem4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.hpi_popup_item1:
			setPopupowItemSelected(ALL_HISTORY);
			CURRENT_POPUPITEM = ALL_HISTORY;
			setTitleWithString("所有交易");
			setCurrentFragment(1);
			break;
		case R.id.hpi_popup_item2:
			setPopupowItemSelected(PURCHASE_HISTORY);
			CURRENT_POPUPITEM = PURCHASE_HISTORY;
			setTitleWithString("累计转入");
			setCurrentFragment(2);
			break;
		case R.id.hpi_popup_item3:
			setPopupowItemSelected(QUICK_HISTORY);
			CURRENT_POPUPITEM = QUICK_HISTORY;
			setTitleWithString("快速转出");
			setCurrentFragment(3);
			break;
		case R.id.hpi_popup_item4:
			setPopupowItemSelected(REDEEM_HISTORY);
			CURRENT_POPUPITEM = REDEEM_HISTORY;
			setTitleWithString("普通转出");
			setCurrentFragment(4);
			break;
		}

		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	public void setPopupowItemSelected(int index) {

		popupItem1.setChecked(false);
		popupItem2.setChecked(false);
		popupItem3.setChecked(false);
		popupItem4.setChecked(false);
		switch (index) {
		case ALL_HISTORY:
			popupItem1.setChecked(true);
			break;
		case PURCHASE_HISTORY:
			popupItem2.setChecked(true);
			break;
		case QUICK_HISTORY:
			popupItem3.setChecked(true);
			break;
		case REDEEM_HISTORY:
			popupItem4.setChecked(true);
			break;
		}
	}
}
