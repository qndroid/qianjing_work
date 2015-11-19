package com.qianjing.finance.ui.activity.assemble.recommand;

import com.qianjing.finance.model.recommand.P2pRedeemResult;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @description 推荐组合取现页面
 * @author majinxin
 * @date 2015年9月15日
 */
public class RecommandRedeemResultActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private TextView mFinishView;
	private TextView mOPerationTimeView;
	private TextView cardNumberTextView;
	private TextView mMoneyView;
	private TextView mArriveTimeView;
	private TextView mArriveInfoView;

	/**
	 * data
	 */
	private P2pRedeemResult mResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommand_redeem_result);
		initData();
		initView();
	}

	private void initData()
	{
		Intent intent = getIntent();
		mResult = (P2pRedeemResult) intent.getSerializableExtra("result");
	}

	private void initView()
	{
		setTitleWithId(R.string.result_info);
		mFinishView = (TextView) findViewById(R.id.right_title_view);
		mFinishView.setVisibility(View.VISIBLE);
		mFinishView.setOnClickListener(this);
		mOPerationTimeView = (TextView) findViewById(R.id.commit_time_view);
		cardNumberTextView = (TextView) findViewById(R.id.bank_name_view);
		mMoneyView = (TextView) findViewById(R.id.deduct_money_view);
		mArriveTimeView = (TextView) findViewById(R.id.final_time_view);
		mArriveInfoView = (TextView) findViewById(R.id.final_time_msg_view);

		/**
		 * update UI
		 */
		mOPerationTimeView.setText(DateFormatHelper.formatDate(mResult.applyTime.concat("000"), DateFormat.DATE_9));
		cardNumberTextView.setText(mResult.bank + getString(R.string.left_brackets) + getString(R.string.wei_hao)
				+ StringHelper.showCardLast4(mResult.card) + getString(R.string.right_brackets));
		mMoneyView.setText(getString(R.string.jin_e) + getString(R.string.ren_ming_bi)
				+ StringHelper.formatString(mResult.sum, StringFormat.FORMATE_1));
		mArriveTimeView.setText(getString(R.string.zui_wan)
				+ DateFormatHelper.formatDate(mResult.arriveTime.concat("000"), DateFormat.DATE_1)
				+ getString(R.string.week) + mResult.weekDay);
		mArriveInfoView.setText(getString(R.string.all_momey_arrive));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.right_title_view:
			setResult(RESULT_OK);
			finish();
			break;
		}
	}
}
