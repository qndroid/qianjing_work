
package com.qianjing.finance.ui.activity.assemble.aip;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.assembleredeem.AipDetailDialog;
import com.qjautofinancial.R;

/**
 * @description 组合定投申购结果页面
 * @author fangyan
 * @date 2015年8月4日
 */
public class AssembleAIPBuyResultActivity extends BaseActivity implements OnClickListener {

	private static int[] stateStr = { R.string.submit, R.string.purchasing, R.string.redeeming, R.string.success, R.string.fail,
			R.string.cancel_order };

	/**
	 * UI
	 */
	/** 定投申购Title文案 */
	private TextView tvAIPTitle;
	/** 操作时间文案 */
	private TextView tvOpTime;
	/** 提示文案索引1 */
	private TextView tvPromptIndex1;
	/** 提示文案1 */
	private TextView tvPrompt1;
	/** 提示文案2布局 */
	private LinearLayout llProment2;
	/** 提示文案2 */
	private TextView tvPrompt2;
	/** 定投申购详情 */
	private LinearLayout llBuyDetail;

	/**
	 * data
	 */
	/** 申购银行卡实例 */
	private Card mCard;
	/** 定投首付 */
	private double downpay;
	/** 定投首付申购状态值 */
	int stateCodeDownpay = -1;
	/** 定投月付 */
	private double monthly;
	/** 当前所选定投日 */
	private int mCurrentDay = 0;
	/** 定投首次扣款日期 */
	private String mFirstDay;
	/** 定投首次扣款日期时间戳 */
	private String mFirstDayStamp;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;
	/**  */
	private ArrayList<FundResponseItem> listFund;

	private String confirmTime;
	private String arriveTime;
	private String opTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();

		initView();
	}

	private void initData() {
		try {
			Intent intent = getIntent();
			mCard = intent.getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);
			downpay = intent.getDoubleExtra(Flag.EXTRA_VALUE_DOWNPAYMENT, Flag.NO_VALUE);
			monthly = intent.getDoubleExtra(Flag.EXTRA_VALUE_MONTHLY, Flag.NO_VALUE);
			mCurrentDay = intent.getIntExtra(Flag.EXTRA_VALUE_DATE, 1);
			mFirstDay = intent.getStringExtra(Flag.EXTRA_VALUE_FIRST_DAY);
			mFirstDayStamp = intent.getStringExtra(Flag.EXTRA_VALUE_FIRST_DAY_STAMP);
			mAssembleDetail = intent.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
			listFund = intent.getParcelableArrayListExtra("listFund");
			arriveTime = intent.getStringExtra("arriveTime");
			confirmTime = intent.getStringExtra("confirmTime");
			opTime = intent.getStringExtra("opTime");
			stateCodeDownpay = intent.getIntExtra("stateCode", -1);
		} catch (Exception e) {
			showToast(getString(R.string.error_param));
			finish();
		}
	}

	private void initView() {

		setContentView(R.layout.activity_assemble_aip_buy_result);

		setTitleWithId(R.string.title_result);

		setLoadingUncancelable();

		TextView rightText = (TextView) findViewById(R.id.title_right_text);
		rightText.setText("完成");
		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE);
				finish();
			}
		});

		tvAIPTitle = (TextView) findViewById(R.id.tv_group_name);

		String strOpTime = DateFormatHelper.formatDate(opTime.concat("000"), DateFormat.DATE_3);
		tvOpTime = (TextView) findViewById(R.id.tv_op_time);
		tvOpTime.setText(strOpTime);

		String strPromptDownpay = "首次申购" + getStrStateDownpay();
		if (stateCodeDownpay == 4)
			strPromptDownpay += "。";
		else
			strPromptDownpay += "，预计" + DateFormatHelper.formatDate(confirmTime, DateFormat.DATE_1) + "确认份额，"
					+ DateFormatHelper.formatDate(arriveTime, DateFormat.DATE_1) + "显示盈亏。";

		String strPromptAIP = "首次定投扣款日期为" + mFirstDayStamp + "，扣款金额￥" + getAIPSum() + "。请确保卡上余额充足，以顺利实现您的理财目标。";
		if (!getAIPState())
			strPromptAIP = "定投申请开通失败";

		tvAIPTitle.setText(strPromptAIP);

		tvPromptIndex1 = (TextView) findViewById(R.id.tv_prompt_index_1);
		tvPrompt1 = (TextView) findViewById(R.id.tv_prompt_1);
		llProment2 = (LinearLayout) findViewById(R.id.ll_proment_2);
		tvPrompt2 = (TextView) findViewById(R.id.tv_prompt_2);
		if (downpay == Flag.NO_VALUE) {
			tvPromptIndex1.setVisibility(View.INVISIBLE);
			tvPrompt1.setText(strPromptAIP);
			llProment2.setVisibility(View.GONE);
		} else {
			tvPrompt1.setText(strPromptDownpay);
			tvPrompt2.setText(strPromptAIP);
		}

		llBuyDetail = (LinearLayout) findViewById(R.id.ll_history_detail);
		if (!listFund.isEmpty()) {
			for (int i = 0; i < listFund.size(); i++) {
				FundResponseItem fund = listFund.get(i);
				View view = View.inflate(this, R.layout.item_history_detail, null);
				TextView historyItem1 = (TextView) view.findViewById(R.id.tv_history_item1);
				TextView historyItem2 = (TextView) view.findViewById(R.id.tv_history_item2);
				TextView historyItem3 = (TextView) view.findViewById(R.id.tv_history_item3);
				TextView historyReason = (TextView) view.findViewById(R.id.tv_history_reason);
				historyItem1.setText(fund.abbrev);
				FormatNumberData.simpleFormatNumber(Float.parseFloat(fund.fdsum), historyItem2);
				historyItem3.setText(String.valueOf(fund.monthSum));
				String strReason = "";
				if (fund.fdstate == 4)
					strReason += ("首次申购失败原因：" + fund.reason);
				if (fund.monthState == 4)
					strReason += ("\n" + "定投失败原因：" + fund.monthReason);
				historyReason.setVisibility(View.VISIBLE);
				historyReason.setText(strReason);
				llBuyDetail.addView(view);
			}
		}

		Button btnAIPDetail = (Button) findViewById(R.id.btn_detail);
		btnAIPDetail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String strAssembleType = ViewUtil.getPortfolioName(AssembleAIPBuyResultActivity.this, mAssembleDetail.getAssembleBase().getType());
				String strCardName = mCard.getBankName() + "(" + StringHelper.showCardLast4(mCard.getNumber()) + ")";
				AipDetailDialog detailDialog = new AipDetailDialog(AssembleAIPBuyResultActivity.this, strAssembleType, strCardName, "￥" + monthly,
						mCurrentDay + "日", getAIPState() ? "成功" : "失败");
				detailDialog.show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE);
		finish();
	}

	/** 
	* @description 获取定投状态。暂时只判断成功和失败两种状态。默认成功
	* @return
	*/
	private boolean getAIPState() {
		boolean aipState = true;
		for (FundResponseItem fund : listFund) {
			if (fund.monthState == 4)
				aipState = false;
		}
		return aipState;
	}

	/** 
	* @description 获取定投金额
	* @return
	*/
	private float getAIPSum() {
		float aipSum = 0;
		for (FundResponseItem fund : listFund)
			aipSum += fund.monthSum;
		return aipSum;
	}

	private String getStrStateDownpay() {
		String strStateDownpay = "";
		if (stateCodeDownpay == 1)
			strStateDownpay = "成功";
		else if (stateCodeDownpay == 11)
			strStateDownpay = "部分成功";
		else if (stateCodeDownpay == 0)
			strStateDownpay = "已受理";
		else if (stateCodeDownpay == 10)
			strStateDownpay = "部分已受理";
		else if (stateCodeDownpay == 4)
			strStateDownpay = "失败";
		else
			strStateDownpay = "部分成功";
		return strStateDownpay;
	}

}
