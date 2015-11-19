package com.qianjing.finance.ui.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.FormatNumberData;
import com.qjautofinancial.R;
/**
 * @category virtual purchase result page
 * @author liuchen
 * */

public class PurchaseVirtualResult extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virtual_result);
		setTitleWithId(R.string.purchase_result);
		
		Intent intent = getIntent();
		double totalValue = intent.getDoubleExtra("sum",0);
		double fee = intent.getDoubleExtra("fee",0);
		double amount = intent.getDoubleExtra("amount",0);
		
		TextView tvTotal = (TextView) findViewById(R.id.tv_virtual_purchase_total);
		TextView tvFee = (TextView) findViewById(R.id.tv_virtual_purchase_fee);
		TextView tvUseable = (TextView) findViewById(R.id.tv_virtual_purchase_useable);
		
		FormatNumberData.simpleFormatNumber((float)totalValue, tvTotal);
		FormatNumberData.simpleFormatNumber((float)fee, tvFee);
		FormatNumberData.simpleFormatNumber((float)amount, tvUseable);
		
		Button resultSure = (Button) findViewById(R.id.btn_result_close);
		resultSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomConstants.REFESH_VIRTUAL_LIST = true;
				PurchaseVirtualResult.this.setResult(CustomConstants.SHUDDOWN_ACTIVITY);
				finish();
			}
		});
	}
	@Override
	public void onBackPressed() {
	}
}
