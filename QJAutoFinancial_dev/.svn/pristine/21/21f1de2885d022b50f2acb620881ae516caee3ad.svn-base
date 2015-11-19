
package com.qianjing.finance.ui.activity.assemble;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

/**
 * <p>
 * Title: AssembleBuyActivity
 * </p>
 * <p>
 * Description: 组合申购金额页面
 * </p>
 * 
 * @author fangyan
 * @date 2015年5月4日
 */
public class AssembleBuyActivity extends BaseActivity {

	private EditText etValue;
	private TextView tvOverHint;
	private ImageView imageBank;
	private TextView tvBankName;
	private TextView tvCard;
	private TextView tvUplimite;
	private ImageView imageArrow;
	private LinearLayout ll_bank_card;
	private Button btnBuyNext;

	/** 当前选定银行卡实例 */
	private Card mCurrentCard;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;
	/** 银行卡容器实例 */
	private ArrayList<Card> mListCard;
	private TypedArray bankImage;

	private final int ORANGE = 0XFFFF5000;
	private final int BLUE = 0XFF5BA7E1;
	private boolean LOAD_OVER = false;
	private ArrayList<Double> feeList = new ArrayList<Double>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UMengStatics.onPBuyPage1View(this);

		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_assemble_buy);

		setTitleWithId(R.string.title_assemble_buy);

		setTitleBack();
		setLoadingUncancelable();

		mAssembleDetail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		if (getIntent().hasExtra(Flag.EXTRA_BEAN_CARD_LIST))
			mListCard = getIntent().getParcelableArrayListExtra(Flag.EXTRA_BEAN_CARD_LIST);

		imageBank = (ImageView) findViewById(R.id.iv_image);
		imageArrow = (ImageView) findViewById(R.id.iv_right_arrow);
		tvBankName = (TextView) findViewById(R.id.tv_bankname);
		tvCard = (TextView) findViewById(R.id.tv_bankcard);
		ll_bank_card = (LinearLayout) findViewById(R.id.ll_bank_card);
		etValue = (EditText) findViewById(R.id.et_downpayment);
		tvUplimite = (TextView) findViewById(R.id.tv_uplimite);
		tvOverHint = (TextView) findViewById(R.id.tv_over_hint);
		btnBuyNext = (Button) findViewById(R.id.btn_buy_next);
		btnBuyNext.setEnabled(false);

		bankImage = getResources().obtainTypedArray(R.array.bank_image);
		int flagBuy = getIntent().getIntExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_BUY);
		if (flagBuy == ViewUtil.ASSEMBLE_BUY || flagBuy == ViewUtil.ASSEMBLE_COPY_VIRTUAL_BUY) {
			initBuyData();
		} else if (flagBuy == ViewUtil.ASSEMBLE_ADD_BUY) {
			initAddBuyData();
		}

		// 获取组合最低申购额
		requestAssembleLimit();

		if (mAssembleDetail.getAssembleBase().getType() != Const.ASSEMBLE_DREAM)
			initFundShares();

		if (etValue != null) {
			etValue.addTextChangedListener(mWatcher);
		}

		ll_bank_card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 追加申购则只有一张对应银行卡
				if (getIntent().getIntExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_BUY) == ViewUtil.ASSEMBLE_ADD_BUY)
					return;
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
				Intent intent = new Intent(AssembleBuyActivity.this, CardListActivity.class);
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
			}
		});
		btnBuyNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AssembleBuyActivity.this, AssembleBuyDetailActivity.class);
				String value = etValue.getText().toString().trim();
				if (StringHelper.isBlank(value))
					return;
				intent.putExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, Float.parseFloat(value));
				intent.putExtra(Flag.EXTRA_BEAN_CARD_COMMON, mCurrentCard);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			}
		});
	}

	private void initBuyData() {
		// ???此处处理有漏洞，需要判断第一张卡绑定状态，后续添加
		mCurrentCard = mListCard.get(0);
		initCardView(mCurrentCard);

		if (mListCard.size() == 1) {
			imageArrow.setVisibility(View.GONE);
			ll_bank_card.setClickable(false);
		} else {
			imageArrow.setVisibility(View.VISIBLE);
			ll_bank_card.setClickable(true);
		}
	}

	private void initAddBuyData() {

		for (Card card : mListCard) {
			if (TextUtils.equals(mAssembleDetail.getAssembleConfig().getCard(), card.getNumber())) {
				mCurrentCard = card;
				initCardView(card);
			}
		}
		imageArrow.setVisibility(View.GONE);
	}

	private void initCardView(Card card) {
		imageArrow.setVisibility(View.VISIBLE);
		imageBank.setImageResource(bankImage.getResourceId(getBankImage(card.getBankName()), -1));
		tvBankName.setText(card.getBankName());
		tvCard.setText(StringHelper.hintCardNum(card.getNumber()));
		tvUplimite.setText("该卡单次支付上限为" + StringHelper.formatString(String.valueOf(card.getLimitRecharge()), StringFormat.FORMATE_2) + "元");
	}

	/**
	 * <p>
	 * Title: checkSum
	 * </p>
	 * <p>
	 * Description: 检查申购金额
	 * </p>
	 */
	private void checkSum() {

		float minLimit = mAssembleDetail.getAssembleBase().getMinLimit();
		float maxLimit = (float) mCurrentCard.getLimitRecharge();

		if (minLimit == 0)
			tvOverHint.setVisibility(View.INVISIBLE);

		String value = etValue.getText().toString().trim();

		if (StringHelper.isBlank(value))
			return;

		if (TextUtils.equals(".", value)) {
			etValue.setText("");
			return;
		} else {
			float money = Float.parseFloat(value);
			if (money >= minLimit && money <= maxLimit)
				btnBuyNext.setEnabled(true);
			else
				btnBuyNext.setEnabled(false);

			if (money < minLimit) {
				tvOverHint.setTextColor(ORANGE);
				tvOverHint.setText(getString(R.string.below_able_buy_value) + minLimit + getString(R.string.money_unit));
				tvOverHint.setVisibility(View.VISIBLE);
			} else if (money > maxLimit) {
				tvOverHint.setTextColor(ORANGE);
				tvOverHint.setText(getString(R.string.beyond_bank_limit));
				tvOverHint.setVisibility(View.VISIBLE);
			} else {
				if (mAssembleDetail.getAssembleBase().getType() != Const.ASSEMBLE_DREAM) {
					float fee = getFeeViewValue(money);
					if (fee != 0) {
						tvOverHint.setTextColor(BLUE);
						tvOverHint.setText("预估手续费：" + StringHelper.formatString(String.valueOf(fee), StringFormat.FORMATE_2) + "元");
						tvOverHint.setVisibility(View.VISIBLE);
					} else
						tvOverHint.setVisibility(View.INVISIBLE);
				} else {
					tvOverHint.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	private float getFeeViewValue(float num) {
		float number = 0;
		if (LOAD_OVER) {
			for (int i = 0; i < feeList.size(); i++) {
				number += feeList.get(i) * num;
			}
		}
		return number;
	}

	/*
	 * 获取组合初始投资金额限制
	 */
	private void requestAssembleLimit() {
		showLoading();

		Hashtable<String, Object> upload = initParams();
		AnsynHttpRequest.requestByPost(this, UrlConst.MINSUB, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = FLAG_ASSEMBLE_LIMIT;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private Hashtable<String, Object> initParams() {

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		/*
		 * 添加计算组合接口的参数
		 */
		AssembleDetail detail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		if (detail != null) {
			AssembleBase assemble = detail.getAssembleBase();
			upload.put("sid", assemble.getSid());
			upload.put("type", assemble.getType()); // 组合类型
			upload.put("nm", ""); // 组合名
			switch (assemble.getType()) {
			case Const.ASSEMBLE_INVEST:
				upload.put("init", assemble.getInvestInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", assemble.getInvestRisk());// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", 0);// 目标金额
				upload.put("year", 0);// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_PENSION:
				upload.put("init", assemble.getPensionInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", 0);// 风险偏好
				upload.put("age", assemble.getPensionCurrentAge());// 年龄
				upload.put("retire", assemble.getPensionRetireAge());// 退休年龄
				upload.put("month", assemble.getPensionMonthAmount());// 月定投金额
				upload.put("money", 0);// 目标金额
				upload.put("year", 0);// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_HOUSE:
				upload.put("init", assemble.getHouseInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", assemble.getHouseInitSum());// 目标金额
				upload.put("year", assemble.getHouseYears());// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_CHILDREN:
				upload.put("init", 0); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", assemble.getChildGoalSum());// 目标金额
				upload.put("year", assemble.getChildYears());// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_MARRY:
				upload.put("init", assemble.getMarryInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", assemble.getMarryGoalSum());// 目标金额
				upload.put("year", assemble.getMarryYears());// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_DREAM:
				upload.put("init", assemble.getDreamInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", 0);// 目标金额
				upload.put("year", 0);// 投资年数
				upload.put("nmonth", assemble.getDreamMonths());// 投资月数
				break;
			case Const.ASSEMBLE_SPESIAL:
				upload.put("init", assemble.getSpecialInitSum()); // 初始投资金额
				upload.put("rate", 0);// 期望收益率
				upload.put("risk", assemble.getSpecialRisk());// 风险偏好
				upload.put("age", assemble.getSpecialAge());// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", 0);// 目标金额
				upload.put("year", 0);// 投资年数
				upload.put("nmonth", 0);// 投资月数
				upload.put("preference", assemble.getSpecialPref());// 投资偏好
				break;
			}
		}
		return upload;
	}

	private void initFundShares() {

		AssembleConfig config = mAssembleDetail.getAssembleConfig();
		List<String> fdcodes = config.getFdcodes();
		List<String> ratios = config.getRatios();

		String buffer = "";
		for (int i = 0; i < fdcodes.size(); i++) {
			feeList.add(Double.valueOf(ratios.get(i)));
			if (i == fdcodes.size() - 1) {
				buffer += fdcodes.get(i);
			} else {
				buffer += fdcodes.get(i) + ",";
			}
		}

		requestFee(buffer);
	}

	private static final int FLAG_ASSEMBLE_LIMIT = 2;
	private static final int FEE_MESSAGE = 4;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonData = (String) msg.obj;
			if (msg.what == FLAG_ASSEMBLE_LIMIT)
				handleAssembleLimit(jsonData);
			else if (msg.what == FEE_MESSAGE)
				analysisFeeMessage(jsonData);
		}
	};

	private void handleAssembleLimit(String strResponse) {

		dismissLoading();

		if (StringHelper.isBlank(strResponse)) {
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			if (jsonObj.optInt("ecode") == 0) {
				String subobject = jsonObj.optString("data");
				double dMinsubSum = new JSONObject(subobject).optDouble("minsubsum");
				float minLimit = Float.valueOf(StringHelper.formatString(String.valueOf(dMinsubSum), StringFormat.FORMATE_2));
				mAssembleDetail.getAssembleBase().setMinLimit(minLimit);
				etValue.setHint("最低申购金额" + minLimit + "元");

			} else {
				showHintDialog("获取最小申购金额失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void analysisFeeMessage(String jsonData) {

		if (jsonData == null || "".equals(jsonData)) {
			dismissLoading();
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
					double fee = data.getJSONObject(i).optDouble("fee");
					feeList.set(i, feeList.get(i) * (fee / (100 + fee)));
				}
				LOAD_OVER = true;
				dismissLoading();
			} else {
				dismissLoading();
				showHintDialog(emsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			dismissLoading();
			;
		}
	}

	private void requestFee(String fdcodes) {
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("fdcodes", fdcodes);
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.ASSEMBLE_BUY_FEE, new HttpCallBack() {
			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = FEE_MESSAGE;
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
		else if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, data);
			finish();
		}
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
	 * 获取银行卡icon
	 * 
	 * @param bankName
	 * @return
	 */
	private int getBankImage(String bankName) {
		String[] mBankNameArray = getResources().getStringArray(R.array.bank_name);
		for (int i = 0; i < mBankNameArray.length; i++) {
			if (mBankNameArray[i].equals(bankName)) {
				return i;
			}
		}
		return mBankNameArray.length;
	}
}
