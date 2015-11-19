package com.qianjing.finance.ui.activity.history;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.ui.activity.history.fragment.AssemableHistoryFragment;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qjautofinancial.R;

public class AssembleHistoryActivity extends BaseHistoryActivity implements OnClickListener{

	
	public int CURRENT_POPUPITEM = 1;

	public static final int ASSEMBLE_HISTORY = 1;
	public static final int FUND_HISTORY = 2;
	public static final int WALLET_HISTORY = 3;

	private HistoryPopupowItem popupItem1;
	private HistoryPopupowItem popupItem2;
	private HistoryPopupowItem popupItem3;


	private AssemableHistoryFragment assemable1;
	private AssemableHistoryFragment assemable2;
	private AssemableHistoryFragment assemable3;
	
	private int mSid = CustomConstants.DEFAULT_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_total);
		setTitleBack();
		
		String sid = getIntent().getStringExtra("sid");
		if(!StringHelper.isBlank(sid)){
			setTitleWithString("交易记录");
			mSid = Integer.parseInt(sid);
		}else{
			initView();
			setFleibleTitleWithString("所有交易");
		}
		setCurrentFragment(1);
	}
	
	@Override
	public void initView() {
		super.initView();
	}
	
	
	
	
	@Override
	public void initPopupView(View popupowView) {
		
		popupItem1 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item1);
		popupItem2 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item2);
		popupItem3 = (HistoryPopupowItem) popupowView
				.findViewById(R.id.hpi_popup_item3);
		
		popupItem1.setText("所有交易");
		popupItem2.setText("申购");
		popupItem3.setText("赎回");
		
		popupItem1.setIcon(R.drawable.history_all_pupow);
		popupItem2.setIcon(R.drawable.history_purchase_pupow);
		popupItem3.setIcon(R.drawable.history_redeem_pupow);
		
		setPopupowItemSelected(CURRENT_POPUPITEM);
		
		popupItem1.setOnClickListener(this);
		popupItem2.setOnClickListener(this);
		popupItem3.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.hpi_popup_item1:
			setPopupowItemSelected(ASSEMBLE_HISTORY);
			CURRENT_POPUPITEM = ASSEMBLE_HISTORY;
			setTitleWithString("所有交易");
			setCurrentFragment(1);
			break;
		case R.id.hpi_popup_item2:
			setPopupowItemSelected(FUND_HISTORY);
			CURRENT_POPUPITEM = FUND_HISTORY;
			setTitleWithString("申购");
			setCurrentFragment(2);
			break;
		case R.id.hpi_popup_item3:
			setPopupowItemSelected(WALLET_HISTORY);
			CURRENT_POPUPITEM = WALLET_HISTORY;
			setTitleWithString("赎回");
			setCurrentFragment(3);
			break;
		}
		
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	private void setCurrentFragment(int index) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		
		if(assemable1!=null){
			beginTransaction.hide(assemable1);
		}
		if(assemable2!=null){
			beginTransaction.hide(assemable2);
		}
		if(assemable3!=null){
			beginTransaction.hide(assemable3);
		}
		
		switch(index){
		case 1:
			if(assemable1==null){
				assemable1 = new AssemableHistoryFragment();
				assemable1.setAssemableParameter(CustomConstants.DEFAULT_VALUE
						, mSid);
				beginTransaction.add(R.id.fl_content, assemable1);
			}else{
				beginTransaction.show(assemable1);
			}
			break;
		case 2:
			if(assemable2==null){
				assemable2 = new AssemableHistoryFragment();
				assemable2.setAssemableParameter(1
						, mSid);
				beginTransaction.add(R.id.fl_content, assemable2);
			}else{
				beginTransaction.show(assemable2);
			}
			break;
		case 3:
			if(assemable3==null){
				assemable3 = new AssemableHistoryFragment();
				assemable3.setAssemableParameter(2
						, mSid);
				beginTransaction.add(R.id.fl_content, assemable3);
			}else{
				beginTransaction.show(assemable3);
			}
			break;
		}
		
		beginTransaction.commit();
		
	}

	public void setPopupowItemSelected(int index) {
		
		popupItem1.setChecked(false);
		popupItem2.setChecked(false);
		popupItem3.setChecked(false);
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
		}
	}
	
}
