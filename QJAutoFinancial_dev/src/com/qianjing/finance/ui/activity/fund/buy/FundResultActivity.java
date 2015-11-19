package com.qianjing.finance.ui.activity.fund.buy;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleLotteryStatus;
import com.qianjing.finance.net.response.model.ResponseLotteryStatus;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @description TODO（描述这个类的作用）
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class FundResultActivity extends BaseActivity implements OnClickListener {
	private PrefManager spManager;
	/**
	 * data
	 */
	private int type;
	private String opDateTime;
	private String profitTimeShow;
	private String profitArriveShow;
	private String fee;
	private String money;
	private String fdType; // 基金类型
	private Card card;

	/**
	 * title UI
	 */
	private TextView titleTextView;
	private TextView finishView;
	/**
	 * success
	 */
	private LinearLayout successLayout;
	private TextView optationTitleView;
	private TextView opdateTimeView;
	private TextView bankNameView;
	private TextView profitTimeView;
	private TextView profitWeekView;
	private TextView profitMsgView;
	private TextView profitArriveView;
	private TextView profitArriveWeekView;
	private TextView profitArriveMsgView;
	private TextView takeMoneyView;
	private TextView takeMoneyValueView;
	private TextView feeMoneyView;
	private TextView feeMoneyValueView;

	private Intent mIntent;
	private LotteryActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fund_buy_result_layout);
		initData();
		initView();
		requestActicity();
	}

	/**
	 * 发送8888活动通知
	 */
	private void requestActicity() {
		if (type == Const.FUND_BUY_SUCCESS) {
			RequestActivityHelper.requestLotteryStatus(this,
					new IHandleLotteryStatus() {
						@Override
						public void handleResponse(
								ResponseLotteryStatus response) {
							if (response.lottery != null
									&& response.lottery.status) {
								spManager.putBoolean(
										PrefManager.HAVE_MORE_THAN_ACTIVITY,
										true);
								mActivity = response.lottery;
							}
						}
					});
		}
	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.title_middle_text);
		finishView = (TextView) findViewById(R.id.right_title_view);
		finishView.setVisibility(View.VISIBLE);
		finishView.setOnClickListener(this);
		/**
		 * 成功UI
		 */
		successLayout = (LinearLayout) findViewById(R.id.result_success);
		optationTitleView = (TextView) findViewById(R.id.opreate_title_view);
		opdateTimeView = (TextView) findViewById(R.id.commit_time_view);
		profitTimeView = (TextView) findViewById(R.id.confirm_time_view);
		profitWeekView = (TextView) findViewById(R.id.confirm_time_value_view);
		profitMsgView = (TextView) findViewById(R.id.confirm_time_msg_view);
		profitArriveView = (TextView) findViewById(R.id.final_time_view);
		profitArriveWeekView = (TextView) findViewById(R.id.final_time_value_view);
		profitArriveMsgView = (TextView) findViewById(R.id.final_time_msg_view);
		bankNameView = (TextView) findViewById(R.id.bank_name_view);
		takeMoneyView = (TextView) findViewById(R.id.deduct_money_view);
		takeMoneyValueView = (TextView) findViewById(R.id.deduct_money_value_view);
		feeMoneyView = (TextView) findViewById(R.id.fee_money_view);
		feeMoneyValueView = (TextView) findViewById(R.id.fee_money_value_view);
		successLayout.setVisibility(View.VISIBLE);
		opdateTimeView.setText(DateFormatHelper.formatDate(
				opDateTime.concat("000"), DateFormat.DATE_9));
		profitTimeView.setText(DateFormatHelper.formatDate(
				profitTimeShow.concat("000"), DateFormat.DATE_1));
		profitWeekView.setText(DateFormatHelper.formatDate(
				profitTimeShow.concat("000"), DateFormat.DATE_6));
		profitMsgView.setCompoundDrawables(null, null, null, null);
		profitArriveView.setText(DateFormatHelper.formatDate(
				profitArriveShow.concat("000"), DateFormat.DATE_1));
		profitArriveWeekView.setText(DateFormatHelper.formatDate(
				profitArriveShow.concat("000"), DateFormat.DATE_6));
		profitArriveMsgView.setCompoundDrawables(null, null, null, null);
		switch (type) {
			case Const.FUND_BUY_HANDLE :
				optationTitleView
						.setText(getString(R.string.fund_buy_handling));
				bankNameView.setText(card.getBankName()
						+ getString(R.string.left_brackets)
						+ getString(R.string.wei_hao)
						+ StringHelper.showCardLast4(card.getNumber())
						+ getString(R.string.right_brackets));
				profitMsgView
						.setText(getString(R.string.start_calculate_shouyi));
				profitArriveMsgView.setText(getString(R.string.first_shouyi));
				// takeMoneyView.setText(getString(R.string.real_cut_money));
				// takeMoneyValueView.setText(getString(R.string.ren_ming_bi) +
				// money);
				// feeMoneyView.setText(getString(R.string.redeemp_evaluate_fee));
				// feeMoneyValueView.setText(getString(R.string.ren_ming_bi) +
				// fee);
				break;
			case Const.FUND_REDEEM_SUCCESS :
				optationTitleView
						.setText(getString(R.string.redeemp_all_hading));
				bankNameView.setText(getString(R.string.dao_zhang_ying_hang_ka)
						+ card.getBankName()
						+ getString(R.string.left_brackets)
						+ getString(R.string.wei_hao)
						+ StringHelper.showCardLast4(card.getNumber())
						+ getString(R.string.right_brackets));
				profitMsgView.setText(getString(R.string.redeem_wait_confirm));
				profitArriveMsgView.setText(getString(R.string.money_daoda));
				takeMoneyView
						.setText(getString(R.string.redeemp_evaluate_yuan));
				takeMoneyValueView.setText(getString(R.string.ren_ming_bi)
						+ money);
				feeMoneyView.setText(getString(R.string.redeemp_evaluate_fee));
				feeMoneyValueView
						.setText(getString(R.string.ren_ming_bi) + fee);
				break;
			case Const.FUND_BUY_SUCCESS :
				if (fdType.equals("3")) {
					feeMoneyView.setVisibility(View.INVISIBLE);
					feeMoneyValueView.setVisibility(View.INVISIBLE);
				}
				optationTitleView.setText(getString(R.string.pay_success));
				bankNameView.setText(card.getBankName()
						+ getString(R.string.left_brackets)
						+ getString(R.string.wei_hao)
						+ StringHelper.showCardLast4(card.getNumber())
						+ getString(R.string.right_brackets));
				profitMsgView.setText(getString(R.string.redeem_wait_confirm));
				profitArriveMsgView
						.setText(getString(R.string.look_for_yesterday_profit));
				takeMoneyView.setText(getString(R.string.real_cut_money));
				takeMoneyValueView.setText(getString(R.string.ren_ming_bi)
						+ money);
				feeMoneyView.setText(getString(R.string.redeemp_evaluate_fee));
				feeMoneyValueView
						.setText(getString(R.string.ren_ming_bi) + fee);
				break;
		}
		titleTextView.setText(getString(R.string.title_result));
	}

	private void initData() {
		mIntent = getIntent();
		type = mIntent.getIntExtra("type", -1);
		opDateTime = mIntent.getStringExtra("opDateShow");
		profitTimeShow = mIntent.getStringExtra("profitTimeShow");
		profitArriveShow = mIntent.getStringExtra("profitArriveShow");
		fee = mIntent.getStringExtra("fee");
		money = mIntent.getStringExtra("money");
		card = mIntent.getParcelableExtra("card");
		fdType = mIntent.getStringExtra("fdtype");

		spManager = PrefManager.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.right_title_view :
				if (mActivity != null) {
					mIntent.putExtra("activity", mActivity);
				}
				setResult(RESULT_OK, mIntent);
				finish();
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mActivity != null) {
				mIntent.putExtra("activity", mActivity);
			}
			setResult(RESULT_OK, mIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}