package com.qianjing.finance.ui.activity.rebalance;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.rebalance.RebalanceDetail;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

public class RebalanceResultActivity extends BaseActivity {

	private RebalanceDetail rebalanceDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_rebalance_result);
		setTitleWithString(getString(R.string.result_info));
		TextView rightText = (TextView) findViewById(R.id.title_right_text);
		rightText.setText(getString(R.string.str_finish));
		rightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(CustomConstants.SHUDDOWN_ACTIVITY);
				finish();
			}
		});
		
		rebalanceDetails = (RebalanceDetail) getIntent().getParcelableExtra(
				"rebalanceDetails");
		TextView tvAdvice = (TextView) findViewById(R.id.tv_advice);
		TextView dateRedeem = (TextView) findViewById(R.id.tv_date_redeem);
		TextView datePurchase = (TextView) findViewById(R.id.tv_date_purchase);
		TextView dateCompleted = (TextView) findViewById(R.id.tv_date_completed);

		String redeemDate = DateFormatHelper.formatDate(
				(rebalanceDetails.redeemArriveTime + "").concat("000"),
				DateFormat.DATE_1);
		String redeemArriveDate = DateFormatHelper.formatDate(
				(rebalanceDetails.redeemConfirmTime + "").concat("000"),
				DateFormat.DATE_2);
		String purchaseDate = DateFormatHelper.formatDate(
				(rebalanceDetails.purchaseTime + "").concat("000"),
				DateFormat.DATE_1);
		String purchaseArriveDate = DateFormatHelper.formatDate(
				(rebalanceDetails.purchaseConfirmTime + "").concat("000"),
				DateFormat.DATE_1);

		dateRedeem.setText(DateFormatHelper.formatDate(
				(System.currentTimeMillis()+""),
				DateFormat.DATE_1)
				+ "\n"
				+ DateFormatHelper.formatDate(
						(System.currentTimeMillis()+""),
						DateFormat.DATE_11));
		datePurchase.setText(purchaseDate);
		dateCompleted.setText(purchaseArriveDate);

		LinearLayout redeemContent = (LinearLayout) findViewById(R.id.ll_redeem_content);
		LinearLayout purchaseContent = (LinearLayout) findViewById(R.id.ll_purchase_content);

		for (int i = 0; i < rebalanceDetails.listHandleFund.size(); i++) {
			if (rebalanceDetails.listHandleFund.get(i).redeemShares > 0) {
				setDetailsContent(redeemContent,
						rebalanceDetails.listHandleFund.get(i).name,
						rebalanceDetails.listHandleFund.get(i).redeemShares
								+ "份");
			} else {
				setDetailsContent(purchaseContent,
						rebalanceDetails.listHandleFund.get(i).name,
						rebalanceDetails.listHandleFund.get(i).subscriptSum
								+ "元");
			}
		}

		TextView redeemItem = (TextView) findViewById(R.id.tv_redeem_item);
		TextView purchaseItem = (TextView) findViewById(R.id.tv_purchase_item);

		String advice = String.format(
				getResources().getString(R.string.balance_result_advice),
				redeemDate);
		String redeemStr = String.format(
				getResources().getString(R.string.balance_redeem_reuslt),
				DateFormatHelper.formatDate(
						(rebalanceDetails.redeemArriveTime + "").concat("000"),
						DateFormat.DATE_2));
		String purchaseStr = String.format(
				getResources().getString(R.string.balance_purchase_reuslt),
				DateFormatHelper.formatDate(
						(rebalanceDetails.purchaseTime + "").concat("000"),
						DateFormat.DATE_2), DateFormatHelper
						.formatDate(
								(rebalanceDetails.purchaseConfirmTime + "")
										.concat("000"), DateFormat.DATE_2));
		tvAdvice.setText(Html.fromHtml(advice));
		redeemItem.setText(Html.fromHtml(redeemStr));
		purchaseItem.setText(Html.fromHtml(purchaseStr));
	}

	private void setDetailsContent(LinearLayout ll, String name, String value) {

		View redeemItem = View.inflate(this, R.layout.rebalance_result_item,
				null);
		TextView tvFundName = (TextView) redeemItem
				.findViewById(R.id.tv_redeem_fund);
		TextView tvFundValue = (TextView) redeemItem
				.findViewById(R.id.tv_redeem_value);
		tvFundName.setText(name);
		tvFundValue.setText(value);
		ll.addView(redeemItem);

	}

	@Override
	public void onBackPressed() {
		setResult(CustomConstants.SHUDDOWN_ACTIVITY);
		finish();
	}

}
