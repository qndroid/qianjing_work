
package com.qianjing.finance.view.assembleredeem;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.qjautofinancial.R;

/**
 * @description 定投详情对话框
 * @author renzhiqiang
 * @date 2015年10月14日
 */
public class AipDetailDialog extends Dialog implements View.OnClickListener {
	/**
	 * Common
	 */
	private Context mContext;
	private TextView mTvAssembleName;
	private TextView mTvCard;
	private TextView mTvAIPMoney;
	private TextView mTvAIPDay;
	private TextView mTvAIPState;
	private TextView mTvOk;

	private String mStrAssembleName;
	private String mStrCard;
	private String mStrAIPMoney;
	private String mStrAIPDay;
	private String mStrAIPState;

	public AipDetailDialog(Context context, String strAssembleName, String strCard, String strAIPMoney, String strAIPDay, String strAIPState) {
		super(context, R.style.Dialog);
		this.mContext = context;
		mStrAssembleName = strAssembleName;
		mStrCard = strCard;
		mStrAIPMoney = strAIPMoney;
		mStrAIPDay = strAIPDay;
		mStrAIPState = strAIPState;
		initView();
	}

	private void initView() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_aip_detail);

		setCanceledOnTouchOutside(true);

		mTvAssembleName = (TextView) findViewById(R.id.tv_assemble_name_value);
		mTvCard = (TextView) findViewById(R.id.tv_card_value);
		mTvAIPMoney = (TextView) findViewById(R.id.tv_aip_money_value);
		mTvAIPDay = (TextView) findViewById(R.id.tv_aip_day_value);
		mTvAIPState = (TextView) findViewById(R.id.tv_aip_state_value);
		mTvOk = (TextView) findViewById(R.id.tv_ok);

		mTvAssembleName.setText(mStrAssembleName);
		mTvCard.setText(mStrCard);
		mTvAIPMoney.setText(mStrAIPMoney);
		mTvAIPDay.setText(mStrAIPDay);
		mTvAIPState.setText(mStrAIPState);

		mTvOk.setOnClickListener(this);
	}

	public void setConfirmListener(View.OnClickListener clickListener) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			dismiss();
			break;
		}
	}
}
