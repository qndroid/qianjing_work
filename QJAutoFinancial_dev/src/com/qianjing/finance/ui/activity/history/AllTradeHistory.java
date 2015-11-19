package com.qianjing.finance.ui.activity.history;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.qianjing.finance.ui.activity.history.fragment.AssemableHistoryFragment;
import com.qianjing.finance.ui.activity.history.fragment.FundHistoryFragment;
import com.qianjing.finance.ui.activity.history.fragment.P2pHistoryFragment;
import com.qianjing.finance.ui.activity.history.fragment.WalletHistoryFragment;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qjautofinancial.R;

public class AllTradeHistory extends BaseHistoryActivity implements
		OnClickListener {

	private HistoryPopupowItem popupItem1;
	private HistoryPopupowItem popupItem2;
	private HistoryPopupowItem popupItem3;

	public int CURRENT_POPUPITEM = 1;

	public static final int ASSEMBLE_HISTORY = 1;
	public static final int FUND_HISTORY = 2;
	public static final int WALLET_HISTORY = 3;
	public static final int P2P_HISTORY = 4;
	private AssemableHistoryFragment assemableHistoryFragment;
	private FundHistoryFragment fundHistoryFragment;
	private WalletHistoryFragment walletHistoryFragment;
    private HistoryPopupowItem popupItem4;
    private P2pHistoryFragment p2pHistoryFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_total);
		initView();
		setTitleBack();
		setFleibleTitleWithString("组合交易记录");
	}
	
	@Override
	public void initView() {
		super.initView();
		setCurrentFragment(CURRENT_POPUPITEM);
	}
	
	private void setCurrentFragment(int index){
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		
		if(assemableHistoryFragment!=null){
			beginTransaction.hide(assemableHistoryFragment);
		}
		if(fundHistoryFragment!=null){
			beginTransaction.hide(fundHistoryFragment);
		}
		if(walletHistoryFragment!=null){
			beginTransaction.hide(walletHistoryFragment);
		}
		if(p2pHistoryFragment!=null){
		    beginTransaction.hide(p2pHistoryFragment);
		}
		
		
		switch(index){
		case ASSEMBLE_HISTORY:
			if(assemableHistoryFragment==null){
				assemableHistoryFragment = new AssemableHistoryFragment();
				beginTransaction.add(R.id.fl_content, assemableHistoryFragment);
			}else{
				beginTransaction.show(assemableHistoryFragment);
			}
			
			break;
		case FUND_HISTORY:
			if(fundHistoryFragment==null){
				fundHistoryFragment = new FundHistoryFragment();
				beginTransaction.add(R.id.fl_content, fundHistoryFragment);
			}else{
				beginTransaction.show(fundHistoryFragment);
			}
			break;
		case WALLET_HISTORY:
			if(walletHistoryFragment==null){
				walletHistoryFragment = new WalletHistoryFragment();
				beginTransaction.add(R.id.fl_content, walletHistoryFragment);
			}else{
				beginTransaction.show(walletHistoryFragment);
			}
			break;
		case P2P_HISTORY:
		    if(p2pHistoryFragment==null){
		        p2pHistoryFragment = new P2pHistoryFragment();
                beginTransaction.add(R.id.fl_content, p2pHistoryFragment);
            }else{
                beginTransaction.show(p2pHistoryFragment);
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
		
		popupowView.findViewById(R.id.v_hide_view).setVisibility(View.VISIBLE);
		popupItem4.setVisibility(View.VISIBLE);
		
		setPopupowItemSelected(CURRENT_POPUPITEM);
		
		popupItem1.setOnClickListener(this);
		popupItem2.setOnClickListener(this);
		popupItem3.setOnClickListener(this);
		popupItem4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		
		switch (v.getId()) {
		case R.id.hpi_popup_item1:
			setPopupowItemSelected(ASSEMBLE_HISTORY);
			setCurrentFragment(ASSEMBLE_HISTORY);
			CURRENT_POPUPITEM = ASSEMBLE_HISTORY;
			setTitleWithString("组合交易记录");
			break;
		case R.id.hpi_popup_item2:
			setPopupowItemSelected(FUND_HISTORY);
			setCurrentFragment(FUND_HISTORY);
			CURRENT_POPUPITEM = FUND_HISTORY;
			setTitleWithString("基金交易记录");
			break;
		case R.id.hpi_popup_item3:
			setPopupowItemSelected(WALLET_HISTORY);
			setCurrentFragment(WALLET_HISTORY);
			CURRENT_POPUPITEM = WALLET_HISTORY;
			setTitleWithString("活期宝交易记录");
			break;
		case R.id.hpi_popup_item4:
            setPopupowItemSelected(P2P_HISTORY);
            setCurrentFragment(P2P_HISTORY);
            CURRENT_POPUPITEM = P2P_HISTORY;
            setTitleWithString("定存交易记录");
            break;
		}
		
//		openActivity(WalletHistoryDetails.class);
		
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
		case ASSEMBLE_HISTORY:
			popupItem1.setChecked(true);
			break;
		case FUND_HISTORY:
			popupItem2.setChecked(true);
			break;
		case WALLET_HISTORY:
			popupItem3.setChecked(true);
			break;
		case P2P_HISTORY:
		    popupItem4.setChecked(true);
            break;
		}
	}
}
