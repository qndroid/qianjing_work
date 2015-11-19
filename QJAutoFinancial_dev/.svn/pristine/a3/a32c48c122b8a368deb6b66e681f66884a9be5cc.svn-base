package com.qianjing.finance.ui.activity.history;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.model.history.Schemaoplogs;
import com.qianjing.finance.model.history.TradeLogs;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

public class FundHistoryDetails extends BaseActivity {

	private ImageView stateIcon;
	private TextView stateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_fund_details);
		setTitleBack();
		initView();
	}

	private void initView() {
		
		TradeLogs schemaoplogsInfo = (TradeLogs) getIntent().getSerializableExtra("schemaoplogs");
		setTitleWithString(schemaoplogsInfo.abbrev);
		
		TextView tvFundValue = (TextView) findViewById(R.id.tv_fund_value);
		TextView tvFundValueTitle = (TextView) findViewById(R.id.tv_fund_value_title);
		TextView tvFundFee = (TextView) findViewById(R.id.tv_fund_fee);
		TextView tvFundEFee = (TextView) findViewById(R.id.tv_fund_estimate_fee);
		TextView tvFundName = (TextView) findViewById(R.id.tv_fund_name);
		TextView tvFundType = (TextView) findViewById(R.id.tv_fund_type);
		TextView tvFundTime = (TextView) findViewById(R.id.tv_fund_time);
		TextView tvFundBank = (TextView) findViewById(R.id.tv_fund_bank);
		TextView tvFundSureT = (TextView) findViewById(R.id.tv_fund_sure_title);
		TextView tvFundSure = (TextView) findViewById(R.id.tv_fund_sure);
		TextView tvFundATime = (TextView) findViewById(R.id.tv_fund_arrive_time);
		TextView tvSureValue = (TextView) findViewById(R.id.tv_sure_value_title);
		
		RelativeLayout lastItem = (RelativeLayout) findViewById(R.id.rl_fund_last_item);

		stateIcon = (ImageView) findViewById(R.id.iv_state_icon);
		stateText = (TextView) findViewById(R.id.tv_state_text);
		
		
		tvFundName.setText(schemaoplogsInfo.abbrev);
		tvFundTime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.opDate.concat("000"),
				DateFormat.DATE_4));
		tvFundBank.setText(schemaoplogsInfo.bank+"(尾号"+StringHelper.showCardLast4(schemaoplogsInfo.card)+")");
		if(!StringHelper.isBlank(schemaoplogsInfo.fee) && !"0.00".equals(schemaoplogsInfo.fee)){
			tvFundEFee.setText("¥"+schemaoplogsInfo.fee);
		}
		
		String stateStr = ViewUtil.getFundState(this,schemaoplogsInfo.state, schemaoplogsInfo.operate).trim();
		
		stateText.setText(stateStr);
		if(stateStr.contains("受理中")){
			stateIcon.setBackgroundResource(R.drawable.history_submit);
		}else if(stateStr.contains("中")){
			stateIcon.setBackgroundResource(R.drawable.history_ing);
		}else if(stateStr.endsWith("成功")){
			stateIcon.setBackgroundResource(R.drawable.history_success);
		}else if(stateStr.endsWith("失败")){
			stateIcon.setBackgroundResource(R.drawable.history_fail);
		}
		
		if("1".equals(schemaoplogsInfo.operate.trim())){
			tvFundValueTitle.setText("申购金额");
			tvSureValue.setText("确认份额");
			tvFundValue.setText("¥"+schemaoplogsInfo.sum);
			tvFundValue.setTextColor(0xFFFF3B3B);
			if(!"0.00".equals(schemaoplogsInfo.shares)){
				tvFundFee.setText(schemaoplogsInfo.shares+"份");
			}
			
			tvFundType.setText("基金申购");
			
			tvFundSureT.setText("盈亏情况");
			if(!"4".equals(schemaoplogsInfo.state)){
				tvFundSure.setText(DateFormatHelper.formatDate(schemaoplogsInfo.confirm_time
						.concat("000"), DateFormat.DATE_2)+"产生盈亏");
			}
		}else{
			tvFundValueTitle.setText("赎回份额");
			tvSureValue.setText("确认金额");
			tvFundValue.setText(schemaoplogsInfo.shares+"份");
			tvFundValue.setTextColor(0xFF63CD99);
			if(!"0.00".equals(schemaoplogsInfo.sum)){
				tvFundFee.setText("¥"+schemaoplogsInfo.sum);
			}
			
			
			tvFundType.setText("基金赎回");
			
			lastItem.setVisibility(View.VISIBLE);
			if(!"4".equals(schemaoplogsInfo.state)){
				tvFundSure.setText(DateFormatHelper.formatDate(schemaoplogsInfo.confirm_time.concat("000"), DateFormat.DATE_1)+"确认");
				tvFundATime.setText(DateFormatHelper.formatDate(schemaoplogsInfo.arrive_time.concat("000"), DateFormat.DATE_1)+"资金到账");
			}
			
		}
	}
}
