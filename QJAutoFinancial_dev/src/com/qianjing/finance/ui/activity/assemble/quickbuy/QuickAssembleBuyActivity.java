
package com.qianjing.finance.ui.activity.assemble.quickbuy;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.util.CheckTools;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

/**
 * <p>Title: AssembleBuyActivity</p>
 * <p>Description: 组合申购页面</p>
 * <p>Company: www.qianjing.com</p>
 * @author liuchen
 * @date 2015年4月27日
 */
public class QuickAssembleBuyActivity extends BaseActivity implements OnClickListener {

	// 最小申购金额的值
	private float minPurchaseValue;
	// 单笔最大限额
	private float maxPurchaseValue;
	/** 推荐组合申购 */
	private boolean isFromRecommend;

	private float money = -1;
	private final int CARD_MESSAGE = 1;
	private final int FUND_MESSAGE = 2;
	private final int FEE_MESSAGE = 3;
	private final int ORANGE = 0XFFFF5000;
	private final int BLUE = 0XFF5BA7E1;
	private boolean LOAD_OVER = false;

	private String mProductName;
	private ArrayList<Double> feeList = new ArrayList<Double>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_assemble_buy);

		// 获取组合的风险类型
		Intent intent = getIntent();
		isFromRecommend = intent.getBooleanExtra("isFromRecommend", false);
		mProductName = intent.getStringExtra("product_name");
		riskType = intent.getIntExtra("type", -1);
		String minStr = intent.getStringExtra("min_value");
		minPurchaseValue = Float.parseFloat(minStr);
		bundle = new Bundle();
		bundle.putInt("type", riskType);

		requestPurchaseDetails();

		initEvent();
		initView();

		cardList = new ArrayList<Card>();
	}

	private void initEvent() {
		setTitleWithId(R.string.title_assemble_buy);

		setTitleBack();

		btnBuy = (Button) findViewById(R.id.btn_buy_next);
		btnBuy.setEnabled(false);
		ll_bank_card = (LinearLayout) findViewById(R.id.ll_bank_card);
		ImageView iv_uplimite = (ImageView) findViewById(R.id.iv_uplimite);
		ImageView ensure_bv = (ImageView) findViewById(R.id.iv_ensure_bv);

		tvUplimite = (TextView) findViewById(R.id.tv_uplimite);

		bankPic = (ImageView) findViewById(R.id.iv_image);
		bankname = (TextView) findViewById(R.id.tv_bankname);
		bankCard = (TextView) findViewById(R.id.tv_bankcard);
		rightArrow = (ImageView) findViewById(R.id.iv_right_arrow);

		overMsg = (TextView) findViewById(R.id.tv_over_hint);

		btnBuy.setOnClickListener(this);
		ll_bank_card.setOnClickListener(this);
		iv_uplimite.setOnClickListener(this);
		ensure_bv.setOnClickListener(this);

		moneyValue = (EditText) findViewById(R.id.et_downpayment);
		moneyValue.setHint("本配置下最低申购金额为" + minPurchaseValue + "元");
		moneyValue.addTextChangedListener(new TextWatcher() {

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
		});
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
		String value = moneyValue.getText().toString().trim();

		if (".".equals(value)) {
			moneyValue.setText("");
			return;
		}

		if (!"".equals(value)) {

			money = Float.parseFloat(value);

			if (money >= minPurchaseValue && money <= maxPurchaseValue) {
				btnBuy.setEnabled(true);
			} else {
				btnBuy.setEnabled(false);
			}
			if (money < minPurchaseValue) {
				overMsg.setVisibility(View.VISIBLE);
				overMsg.setTextColor(ORANGE);
				overMsg.setText(getString(R.string.below_able_buy_value) + minPurchaseValue + getString(R.string.money_unit));

			} else if (money > maxPurchaseValue) {
				overMsg.setTextColor(ORANGE);
				overMsg.setVisibility(View.VISIBLE);
				overMsg.setText(getString(R.string.beyond_bank_limit));
			} else {
				float fee = getFeeViewValue(money);
				if (fee != 0) {
					overMsg.setTextColor(BLUE);
					overMsg.setText("预估手续费：" + StringHelper.formatDecimal(fee) + "元");
					overMsg.setVisibility(View.VISIBLE);
				} else
					overMsg.setVisibility(View.INVISIBLE);
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

	/**
	 * 初始化界面
	 */
	private void initView() {
		// 如果银行卡为空，请求银行卡列表数据
		if (mCard == null) {
			requestBankCard();
		} else {
			bundle.putString("cd", mCard.getNumber());
			bankPic.setImageResource(CommonUtil.getBankIconId(mCard.getBankName()));
			bankname.setText(mCard.getBankName());
			bankCard.setText(StringHelper.hintCardNum(mCard.getNumber()));

			maxPurchaseValue = (float) mCard.getLimitRecharge();
			tvUplimite.setText("该卡单次支付上限为" + StringHelper.formatDecimal(maxPurchaseValue) + "元");

			if (cardList.size() <= 1) {
				rightArrow.setVisibility(View.GONE);
				ll_bank_card.setClickable(false);
			} else {
				rightArrow.setVisibility(View.VISIBLE);
				ll_bank_card.setClickable(true);
			}

			if (money != -1) {
				if (money > maxPurchaseValue) {
					overMsg.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_buy_next:
			bundle.putFloat("sum", money);
			Intent intent = new Intent(this, QuickBuyConfirmActivity.class);
			intent.putExtra("isFromRecommend", isFromRecommend);
			intent.putExtra("product_name", mProductName);
			intent.putExtras(bundle);
			startActivityForResult(intent, CustomConstants.QUICK_PUECHASE);
			QuickAssembleBuyActivity.this.setResult(CustomConstants.SHUDDOWN_ACTIVITY);
			break;
		case R.id.ll_bank_card:
			selectBankCard();
			break;
		case R.id.iv_uplimite:
			showToast("银行卡上限信息");
			break;
		case R.id.iv_ensure_bv:
			showToast("确认银行卡余额息");
			break;
		}
	}

	public void selectBankCard() {
		Intent intent = new Intent(this, CardListActivity.class);
		intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, cardList);
		intent.putExtra(Flag.EXTRA_BEAN_CARD_CURRENT, mCard);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_BUY_RESULT_CODE, data);
			finish();
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		try {
			mCard = data.getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);
		} catch (Exception e) {
		}

		initView();
		checkSum();

		super.onActivityResult(requestCode, resultCode, data);
	}

	// 请求银行卡列表
	private void requestBankCard() {
		showLoading();
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.WALLET_CARD_LIST, cardCallBack, null);
	}

	// 获取组合详情
	private void requestPurchaseDetails() {
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		if (isFromRecommend) {
			hashTable.put("risk", riskType);
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.RECOMMAND_FUND_DETAIL, detailCallback, hashTable);
		} else {
			hashTable.put("risk_type", riskType);
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.QUICK_BUY_DETAIL, detailCallback, hashTable);
		}
	}

	private HttpCallBack detailCallback = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = FUND_MESSAGE;
			handler.sendMessage(msg);
		}
	};

	// 请求银行卡列表
	private void requestFee(String fdcodes) {
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("fdcodes", fdcodes);
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.ASSEMBLE_BUY_FEE, new HttpCallBack() {

			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = FEE_MESSAGE;
				handler.sendMessage(msg);
			}
		}, hashTable);
	}

	HttpCallBack cardCallBack = new HttpCallBack() {

		@Override
		public void back(String data, int status) {
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = CARD_MESSAGE;
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			String jsonData = (String) msg.obj;
			switch (msg.what) {
			case CARD_MESSAGE:
				analysisBankCard(jsonData);
			case FUND_MESSAGE:
				analysisPurchaseDetails(jsonData);
				break;
			case FEE_MESSAGE:
				analysisFeeMessage(jsonData);
				break;
			}
		};
	};

	private EditText moneyValue;
	private Button btnBuy;
	private TextView overMsg;
	private ArrayList<Card> cardList;
	private ImageView bankPic;
	private TextView bankname;
	private TextView bankCard;
	private Card mCard;
	private TextView tvUplimite;
	private Bundle bundle;
	private ImageView rightArrow;
	private LinearLayout ll_bank_card;
	private int riskType;

	protected void analysisBankCard(String jsonData) {

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

				JSONObject data1 = jsonObj.optJSONObject("data");
				JSONArray cards = data1.optJSONArray("cards");

				for (int i = 0; i < cards.length(); i++) {

					JSONObject cardStr = cards.optJSONObject(i);

					Card card = new Card();
					card.setBankName(CheckTools.checkJson(cardStr.optString("bank")));
					card.setNumber(CheckTools.checkJson(cardStr.optString("card")));
					card.setLimitRecharge(Double.parseDouble(CheckTools.checkJson(cardStr.optString("single_limit"))));
					card.setMobile(CheckTools.checkJson(cardStr.optString("mobile")));
					card.setUid(CheckTools.checkJson(cardStr.optString("uid")));

					cardList.add(card);
				}

				if (cardList != null) {
					// 设置默认的银行卡
					mCard = cardList.get(0);
					initView();

				} else {
					// 跳转到绑定银行卡界面
				}
				dismissLoading();

			} else {
				dismissLoading();
				showHintDialog(emsg);
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
					feeList.set(i, feeList.get(i) * (fee / 100) / ((fee / 100) + 1));
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
		}
	}

	protected void analysisPurchaseDetails(String jsonData) {

		if (jsonData == null || "".equals(jsonData)) {
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}

		try {
			// 这里只要获取基金名称和占比即可
			JSONObject jsonObj = new JSONObject(jsonData);
			int ecode = jsonObj.optInt("ecode");
			String emsg = jsonObj.optString("emsg");
			if (ecode == 0) {

				JSONObject assemble = jsonObj.optJSONObject("data").optJSONObject("assembly");
				JSONArray fdcodes = assemble.optJSONArray("fdcodes");
				JSONArray ratios = assemble.optJSONArray("ratios");

				String buffer = "";
				for (int i = 0; i < fdcodes.length(); i++) {
					double share = ratios.getDouble(i);
					String fdcode = fdcodes.getString(i);
					feeList.add(share);
					if (i == fdcodes.length() - 1) {
						buffer += fdcode;
					} else {
						buffer += fdcode + ",";
					}
				}
				requestFee(buffer);
			} else {
				dismissLoading();
				showHintDialog(emsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			dismissLoading();
		}
	}

}
