package com.qianjing.finance.ui.activity.history;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.model.history.TradeLogs;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

public class WalletHistoryDetails extends BaseActivity{

	private RelativeLayout rlArriveItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_wallet_details);
		setTitleBack();
		initView();
	}

	private void initView() {
		
		TradeLogs schemaoplogsInfo = (TradeLogs) getIntent().getSerializableExtra("schemaoplogs");
		setTitleWithString("活期宝");
		
		TextView valueTitle = (TextView) findViewById(R.id.tv_wallet_value_title);
		TextView value = (TextView) findViewById(R.id.tv_wallet_value);
		TextView walletName = (TextView) findViewById(R.id.tv_wallet_name);
		TextView walletType = (TextView) findViewById(R.id.tv_wallet_type);
		TextView walletTime = (TextView) findViewById(R.id.tv_wallet_time);
		TextView walletBank = (TextView) findViewById(R.id.tv_wallet_bank);
		TextView walletATime = (TextView) findViewById(R.id.tv_wallet_arrive_time);
		TextView walletATimeT = (TextView) findViewById(R.id.tv_wallet_arrive_title);
		rlArriveItem = (RelativeLayout) findViewById(R.id.rl_arrive_item);
		
		
		ImageView stateIcon = (ImageView) findViewById(R.id.iv_state_icon);
		TextView stateText = (TextView) findViewById(R.id.tv_state_text);
		
		String stateStr = getState(schemaoplogsInfo.operate, schemaoplogsInfo.state);
		stateText.setText(stateStr);
		
		if(stateStr.contains("确认中")){
			stateIcon.setBackgroundResource(R.drawable.history_ing);
		}else if(stateStr.contains("成功")){
			stateIcon.setBackgroundResource(R.drawable.history_success);
		}else if(stateStr.contains("失败")){
			stateIcon.setBackgroundResource(R.drawable.history_fail);
		}else if(stateStr.contains("已到账")){
			stateIcon.setBackgroundResource(R.drawable.history_success);
		}
		
		
		walletName.setText("活期宝");
		walletTime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.opDate.concat("000"),
				DateFormat.DATE_4));
		walletBank.setText(schemaoplogsInfo.bank+"(尾号"+StringHelper.showCardLast4(schemaoplogsInfo.card)+")");
		
		int operateInt = Integer.parseInt(schemaoplogsInfo.operate);
		if(operateInt==1){
			valueTitle.setText("转入金额");
			value.setText("¥"+schemaoplogsInfo.sum);
			value.setTextColor(0xFFFF3B3B);
			walletType.setText("转入");
			walletATimeT.setText("收益情况");
			walletATime.setText("第一笔收益"+DateFormatHelper.formatDate(schemaoplogsInfo.profit_time
					.concat("000"), DateFormat.DATE_2)+"到账");
		}else if(operateInt==2){
			valueTitle.setText("转出金额");
			value.setText("¥"+schemaoplogsInfo.sum);
			value.setTextColor(0xFF63CD99);
			walletType.setText("快速转出");
			walletATimeT.setText("到账情况");
			walletATime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.profit_arrive
					.concat("000"), DateFormat.DATE_2)+"资金到账");
		}else if(operateInt==3){
			valueTitle.setText("转出金额");
			value.setText("¥"+schemaoplogsInfo.sum);
			value.setTextColor(0xFF63CD99);
			walletType.setText("普通转出");
			walletATimeT.setText("到账情况");
			walletATime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.profit_arrive
					.concat("000"), DateFormat.DATE_2)+"资金到账");
		}else if(operateInt==4){
			rlArriveItem.setVisibility(View.GONE);
			valueTitle.setText("返现金额");
			value.setText("¥"+schemaoplogsInfo.sum);
			value.setTextColor(0xFFFF3B3B);
			walletType.setText("活动返现");
			walletATimeT.setText("到账情况");
			walletATime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.profit_arrive
					.concat("000"), DateFormat.DATE_2)+"资金到账");
		}
	}
	
	private String getState(String operate,String state)
	{
		int type = Integer.parseInt(operate);
		int stateInt = Integer.parseInt(state);
		String typeName = "";
		switch (type)
		{
		case 1:
			typeName = "转入";
			break;
		case 2:
			typeName = "快速转出";
			break;
		case 3:
			typeName = "转出";
			break;
		case 4:
			typeName = "已到账";
			break;
		}
		
		if(type!=4){
			switch (stateInt)
			{
			case 0:
			case 1:
			case 2:
				typeName += "确认中";
				break;
			case 3:
				typeName += "成功";
				break;
			case 4:
				typeName += "失败";
				break;
			}
		}
		
		return typeName;
	}

}
