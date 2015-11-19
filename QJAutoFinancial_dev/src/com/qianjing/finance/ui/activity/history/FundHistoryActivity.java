package com.qianjing.finance.ui.activity.history;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.ui.activity.history.fragment.AssemableHistoryFragment;
import com.qianjing.finance.ui.activity.history.fragment.FundHistoryFragment;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class FundHistoryActivity extends BaseHistoryActivity implements OnClickListener{

	public int CURRENT_POPUPITEM = 1;

	public static final int ALL_HISTORY = 1;
	public static final int PURCHASE_HISTORY = 2;
	public static final int REDEEM_HISTORY = 3;


	private HistoryPopupowItem popupItem1;
	private HistoryPopupowItem popupItem2;
	private HistoryPopupowItem popupItem3;

	private FundHistoryFragment fund1;
	private FundHistoryFragment fund2;
	private FundHistoryFragment fund3;
	
	private String mFdcode = "";
	private boolean isAllHistory = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_total);
		setTitleBack();
		
		String fdcode = getIntent().getStringExtra("fdcode");
		if(StringHelper.isBlank(fdcode)){
			initView();
			setFleibleTitleWithString("所有交易");
		}else{
			setTitleWithString("交易记录");
			mFdcode = fdcode;
			
			isAllHistory = false;
		}
		setCurrentFragment(1);
	}
	
	@Override
	public void initView() {
		super.initView();
	}
	
	
	private void setCurrentFragment(int index) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		
		if(fund1!=null){
			beginTransaction.hide(fund1);
		}
		if(fund2!=null){
			beginTransaction.hide(fund2);
		}
		if(fund3!=null){
			beginTransaction.hide(fund3);
		}
		
		switch(index){
		case 1:
			if(fund1==null){
				fund1 = new FundHistoryFragment();
				fund1.setAssemableParameter(isAllHistory,CustomConstants.DEFAULT_VALUE,mFdcode);
				beginTransaction.add(R.id.fl_content, fund1);
			}else{
				beginTransaction.show(fund1);
			}
			break;
		case 2:
			if(fund2==null){
				fund2 = new FundHistoryFragment();
				fund2.setAssemableParameter(isAllHistory,1,mFdcode);
				beginTransaction.add(R.id.fl_content, fund2);
			}else{
				beginTransaction.show(fund2);
			}
			break;
		case 3:
			if(fund3==null){
				fund3 = new FundHistoryFragment();
				fund3.setAssemableParameter(isAllHistory,2,mFdcode);
				beginTransaction.add(R.id.fl_content, fund3);
			}else{
				beginTransaction.show(fund3);
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
		
		popupItem1.setText("所有交易");
		popupItem2.setText("申购");
		popupItem3.setText("赎回");
		
		popupItem1.setIcon(R.drawable.history_all_pupow);
		popupItem2.setIcon(R.drawable.history_purchase_pupow);
		popupItem3.setIcon(R.drawable.history_redeem_pupow);
		
		popupItem1.setOnClickListener(this);
		popupItem2.setOnClickListener(this);
		popupItem3.setOnClickListener(this);

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
			setTitleWithString("申购");
			setCurrentFragment(2);
			break;
		case R.id.hpi_popup_item3:
			setPopupowItemSelected(REDEEM_HISTORY);
			CURRENT_POPUPITEM = REDEEM_HISTORY;
			setTitleWithString("赎回");
			setCurrentFragment(3);
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
		switch (index) {
		case ALL_HISTORY:
			popupItem1.setChecked(true);
			break;
		case PURCHASE_HISTORY:
			popupItem2.setChecked(true);
			break;
		case REDEEM_HISTORY:
			popupItem3.setChecked(true);
			break;
		}
	}

}
