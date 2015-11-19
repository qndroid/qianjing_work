
package com.qianjing.finance.ui.activity.assemble.aip;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.aip.DateFirst;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.InvestPlan;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembleredeem.RedeemPickerateDialog;
import com.qjautofinancial.R;

/**
 * @description 组合定投申购页面
 * @author fangyan
 * @date 2015年8月4日
 */
public class AssembleAIPBuyActivity extends BaseActivity implements OnClickListener {

	private EditText etDownpay;
	private EditText etMonthly;
	private TextView tvOverHint;
	private ImageView imageBank;
	private TextView tvBankName;
	private TextView tvCard;
	private TextView tvUplimite;
	private ImageView imageArrow;
	private LinearLayout llBank;
	private LinearLayout llDownpay;
	private EditText etSelectDay;
	private TextView tvFirstDay;
	private Button btnBuyNext;
	private RedeemPickerateDialog picker;

	/** 当前所选定投日 */
	private int mCurrentDay = 1;
	/** 当前选定银行卡实例 */
	private Card mCurrentCard;
	/** 投资计划 */
	private InvestPlan investPlan;
	/** 定投首付 */
	private double downpay;
	/** 定投月付 */
	private double monthly;
	/** 定投首次扣款日期时间戳 */
	private String mFirstDayStamp;
	/** 定投首次扣款日期 */
	private String mFirstDay;
	/** 最低定投首付 */
	private double mixDownpayment;
	/** 最低定投月付 */
	private double mixMonthly;
	/** 银行卡容器实例 */
	private ArrayList<Card> mListCard;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;

	private ArrayList<Float> feeList = new ArrayList<Float>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();

		initView();
	}

	private void initData() {
		try {
			Intent intent = getIntent();
			mAssembleDetail = intent.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
			mListCard = intent.getParcelableArrayListExtra(Flag.EXTRA_BEAN_CARD_LIST);
			investPlan = intent.getParcelableExtra(Flag.EXTRA_BEAN_INVESTPLAN);

			AssembleBase assemble = mAssembleDetail.getAssembleBase();
			mixDownpayment = assemble.getMinLimit();
			downpay = Flag.NO_VALUE;
			if (investPlan.getOne_invest() > 0)
				downpay = investPlan.getOne_invest();
			monthly = mixMonthly = investPlan.getMonth_fixed();

			// ???此处处理有漏洞，需要判断第一张卡绑定状态，后续添加 
			mCurrentCard = mListCard.get(0);
		} catch (Exception e) {
			showToast(getString(R.string.error_param));
			finish();
		}
	}

	private void initView() {

		setContentView(R.layout.activity_assemble_aip_buy);

		setTitleWithId(R.string.title_assemble_aip);

		setTitleBack();
		setLoadingUncancelable();

		imageBank = (ImageView) findViewById(R.id.iv_image);
		imageArrow = (ImageView) findViewById(R.id.iv_right_arrow);
		tvBankName = (TextView) findViewById(R.id.tv_bank);
		tvCard = (TextView) findViewById(R.id.tv_card);
		tvUplimite = (TextView) findViewById(R.id.tv_uplimit);
		tvOverHint = (TextView) findViewById(R.id.tv_over_hint);

		etMonthly = (EditText) findViewById(R.id.et_monthly);
		llDownpay = (LinearLayout) findViewById(R.id.ll_downpayment);
		etDownpay = (EditText) findViewById(R.id.et_downpayment);

		llBank = (LinearLayout) findViewById(R.id.ll_bank_card);
		llBank.setOnClickListener(this);

		etSelectDay = (EditText) findViewById(R.id.et_day_select);
		etSelectDay.setOnClickListener(this);

		tvFirstDay = (TextView) findViewById(R.id.tv_first_hint);

		btnBuyNext = (Button) findViewById(R.id.btn_buy_next);
		btnBuyNext.setOnClickListener(this);
		btnBuyNext.setEnabled(false);

		picker = new RedeemPickerateDialog(this, 1, 30, 0);
		picker.setConfirmListener(listener);

		initCardView(mCurrentCard);

		if (mListCard.size() == 1) {
			imageArrow.setVisibility(View.GONE);
			llBank.setClickable(false);
		} else {
			imageArrow.setVisibility(View.VISIBLE);
			llBank.setClickable(true);
		}

		if (monthly != Flag.NO_VALUE)
			etMonthly.setText(StringHelper.formatDecimal(monthly));
		if (downpay == Flag.NO_VALUE)
			llDownpay.setVisibility(View.GONE);
		else
			etDownpay.setText(StringHelper.formatDecimal(downpay));
		etDownpay.addTextChangedListener(mWatcher);

		if (mAssembleDetail.getAssembleBase().getType() != Const.ASSEMBLE_DREAM)
			requestFee();
	}

	@SuppressLint("Recycle")
	private void initCardView(Card card) {
		if (card == null)
			return;
		imageArrow.setVisibility(View.VISIBLE);
		TypedArray bankImage = getResources().obtainTypedArray(R.array.bank_image);
		int imageId = ViewUtil.getBankImageByName(this, card.getBankName());
		imageBank.setImageResource(bankImage.getResourceId(imageId, -1));
		tvBankName.setText(card.getBankName());
		tvCard.setText(StringHelper.hintCardNum(card.getNumber()));
		String strNum = StringHelper.formatString(String.valueOf(card.getLimitRecharge()), StringFormat.FORMATE_2);
		tvUplimite.setText("该卡单次支付上限为" + strNum + "元");
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonData = (String) msg.obj;
			analysisFeeMessage(jsonData);
		}
	};

	protected void analysisFeeMessage(String jsonData) {
		dismissLoading();

		if (jsonData == null || "".equals(jsonData)) {
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			int ecode = jsonObj.optInt("ecode");
			String emsg = jsonObj.optString("emsg");
			if (ecode == 0) {

				JSONArray data = jsonObj.optJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					float fee = (float) data.getJSONObject(i).optDouble("fee");
					feeList.set(i, feeList.get(i) * (fee / (100 + fee)));
				}
				checkSum();
			} else {
				showHintDialog(emsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_bank_card:
			if (mListCard != null && mListCard.size() > 1) {
				int countCard = 0;
				for (Card card : mListCard) {
					if (card.getIsUnbound() == 0) {
						countCard++;
					}
				}
				if (countCard == 1)
					return;
			} else
				return;
			Intent intent = new Intent(AssembleAIPBuyActivity.this, CardListActivity.class);
			// 传递已绑定银行卡容器
			ArrayList<Card> listTemp = new ArrayList<Card>();
			for (Card card : mListCard) {
				if (card.getIsUnbound() == 0) {
					listTemp.add(card);
				}
			}
			intent.putExtra(Flag.EXTRA_BEAN_CARD_CURRENT, mCurrentCard);
			intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listTemp);
			startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			break;
		case R.id.et_day_select:
			picker.show();
			picker.setTitleText("请选择定投日期");
			picker.setSelectedNumber(mCurrentDay);
			break;
		case R.id.btn_buy_next:
			Intent intent2 = new Intent(this, AssembleAIPBuyDetailActivity.class);
			try {
				downpay = Double.valueOf(etDownpay.getText().toString().trim());
				monthly = Double.valueOf(etMonthly.getText().toString().trim());
			} catch (Exception e) {
			}
			intent2.putExtra(Flag.EXTRA_BEAN_CARD_COMMON, mCurrentCard);
			intent2.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
			intent2.putExtra(Flag.EXTRA_BEAN_INVESTPLAN, investPlan);
			intent2.putExtra(Flag.EXTRA_VALUE_DOWNPAYMENT, downpay);
			intent2.putExtra(Flag.EXTRA_VALUE_MONTHLY, monthly);
			intent2.putExtra(Flag.EXTRA_VALUE_DATE, mCurrentDay);
			intent2.putExtra(Flag.EXTRA_VALUE_FIRST_DAY, mFirstDayStamp);
			startActivityForResult(intent2, ViewUtil.REQUEST_CODE);
			break;
		}
	}

	/**
	 * 对话框确定按钮回调
	 */
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			updateUI(picker.getSelectedNumber());

			picker.dismiss();
		}
	};

	private void updateUI(int selectDay) {
		if (mCurrentDay == selectDay)
			return;

		mCurrentDay = selectDay;

		etSelectDay.setText("每月" + mCurrentDay + "日");
		btnBuyNext.setEnabled(true);

		//		String time = "1444724437";
		//		mFirstDay = DateFormatHelper.formatDate(time, DateFormat.DATE_5);
		//		tvFirstDay.setVisibility(View.VISIBLE);
		//		tvFirstDay.setText("首次扣款日期：" + mFirstDay);

		// 首次月付日期
		showLoading();
		Hashtable<String, Object> tableParam = new Hashtable<String, Object>();
		tableParam.put("day", mCurrentDay);
		RequestManager.request(this, UrlConst.ASSEMBLE_AIP_DATE_FIRST, tableParam, DateFirst.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				dismissLoading();
				if (model.stateCode == ErrorConst.EC_OK) {
					DateFirst date = (DateFirst) model;
					mFirstDayStamp = date.dateFirst;
					mFirstDay = DateFormatHelper.formatDate(mFirstDayStamp.concat("000"), DateFormat.DATE_5);
					tvFirstDay.setVisibility(View.VISIBLE);
					tvFirstDay.setText("首次扣款日期：" + mFirstDay);
				}
			}

			@Override
			public void fail() {
				dismissLoading();
			}
		});
	}

	private TextWatcher mWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkSum();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	/**
	 * @description 检查申购金额
	 * @author fangyan
	 */
	private void checkSum() {

		float minLimit = mAssembleDetail.getAssembleBase().getMinLimit();
		float maxLimit = (float) mCurrentCard.getLimitRecharge();

		String value = etDownpay.getText().toString().trim();

		tvOverHint.setVisibility(View.INVISIBLE);
		// 数额不合法的情况
		if (StringHelper.isBlank(value))
			return;
		if (TextUtils.equals(".", value) || Float.parseFloat(value) == 0) {
			tvOverHint.setVisibility(View.INVISIBLE);
			etDownpay.setText("");
			return;
		}

		float money = Float.parseFloat(value);
		String selectDay = etSelectDay.getText().toString();
		if (money >= minLimit && money <= maxLimit && StringHelper.isNotBlank(selectDay))
			btnBuyNext.setEnabled(true);
		else
			btnBuyNext.setEnabled(false);

		// 首付金额小于最低额
		if (money < minLimit) {
			tvOverHint.setVisibility(View.VISIBLE);
			tvOverHint.setTextColor(ORANGE);
			tvOverHint.setText(getString(R.string.below_able_buy_value) + minLimit + getString(R.string.money_unit));
		} else if (money > maxLimit) { // 首付金额高于最高额
			tvOverHint.setVisibility(View.VISIBLE);
			tvOverHint.setTextColor(ORANGE);
			tvOverHint.setText(getString(R.string.beyond_bank_limit));
		} else {
			// 首付金额符合限制而且非梦想基金组合
			if (mAssembleDetail.getAssembleBase().getType() != Const.ASSEMBLE_DREAM) {
				// 首付金额符合限制则显示手续费金额
				downpay = money;
				double fee = 0;
				for (int i = 0; i < feeList.size(); i++) {
					fee += downpay * feeList.get(i);
				}
				tvOverHint.setVisibility(View.VISIBLE);
				tvOverHint.setTextColor(R.color.blue_deep);
				tvOverHint.setText("预付手续费：" + StringHelper.formatDecimal(fee) + "元");
			}
		}
	}

	private final int ORANGE = 0XFFFF5000;

	private void requestFee() {
		String buffer = "";
		AssembleConfig config = mAssembleDetail.getAssembleConfig();
		ArrayList<Fund> listFund = config.getFundList();
		for (Fund fund : listFund) {
			feeList.add(Float.valueOf(fund.ratio));
			buffer += fund.code + ",";
		}
		if (buffer.endsWith(","))
			buffer = buffer.substring(0, buffer.length() - 1);

		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("fdcodes", buffer);
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.ASSEMBLE_BUY_FEE, new HttpCallBack() {
			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, hashTable);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 选择银行卡返回
		if (resultCode == RESULT_OK) {
			mCurrentCard = data.getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);
			initCardView(mCurrentCard);
			checkSum();
		}
		// 申购结果返回
		else if (resultCode == ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE, data);
			finish();
		}
	}

}
