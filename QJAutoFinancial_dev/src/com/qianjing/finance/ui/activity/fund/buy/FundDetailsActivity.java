package com.qianjing.finance.ui.activity.fund.buy;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.WriteLog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 基金详情页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class FundDetailsActivity extends BaseActivity implements OnClickListener {
	/**
	 * common
	 */
	private static final int FUND_NORMAL = 2; // 可申购基金
	private static final int FUND_PAUSE = 3; // 暂停申购基金
	private static final int FUND_INVALIDATE = 10; // 不可交易基金
	/**
	 * UI
	 */
	private TextView fundTitleText;
	private TextView fundTypeText;
	private TextView fundUpdateTimeText;
	private TextView fundProfitTitle;
	private TextView fundProfitText;
	private TextView fundRoseTitle;
	private TextView fundRoseText;
	private TextView fundRosePrecentView;
	private TextView minMoneyText;
	private TextView buyConfirmText;
	private TextView buybackText;
	private RelativeLayout feiRateLayout;
	private TextView feeRateText;
	private TextView oldFeeText;
	private TextView titleText;
	private Button shenGouBtn;
	private Button btnBack;
	/**
	 * 基金代码
	 */
	private String fdCode;
	private String fdName;
	private String fundType;
	private Hashtable<String, Object> params = new Hashtable<String, Object>();
	private String minScript;
	private boolean isFromAssemble;
	private String[] fundTypeArray;
	private String fee = "0.0";
	private int fundState;// 标示基金是否可以交易。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fund_details);
		initData();
		initView();
		requestFundDetail();
	}

	private void requestFundDetail() {
		showLoading();
		params.put("fund_code", fdCode);
		AnsynHttpRequest.requestByPost(this, UrlConst.URL_FUND_INFO,
				fundDetailHandle, params);
	}

	private HttpCallBack fundDetailHandle = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			dismissLoading();
			ResponseBase responseBase = ResponseManager.getInstance().parse(
					data);
			if (responseBase != null) {
				if (responseBase.ecode == ErrorConst.EC_OK
						|| StringHelper.isNotBlank(responseBase.strError)) {
					JSONObject objData = responseBase.jsonObject;
					/**
					 * upateUI
					 */
					mHandler.sendMessage(mHandler.obtainMessage(0x01, objData));
				} else {
					finish();
					showHintDialog("网络不给力");
					return;
				}
			} else {
				finish();
				showHintDialog("网络不给力");
				return;
			}
		}
	};

	/**
	 * 获取银行卡列表
	 */
	private void requestCardList() {
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_LIST,
				cardListHandle, null);
	}

	private HttpCallBack cardListHandle = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			dismissLoading();
			ResponseBase responseBase = ResponseManager.getInstance().parse(
					data);
			if (responseBase != null) {
				if (responseBase.ecode == ErrorConst.EC_OK
						|| StringHelper.isNotBlank(responseBase.strError)) {
					JSONObject objData = responseBase.jsonObject;
					/**
					 * upateUI
					 */
					mHandler.sendMessage(mHandler.obtainMessage(0x02, objData));
				} else {
					showHintDialog("网络不给力");
					return;
				}
			} else {
				showHintDialog("网络不给力");
				return;
			}

		}
	};

	private void requestHYCard(String cardnum) {
		// 发送银行卡还原接口请求
		showLoading();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("cd", cardnum);
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_HTYCARD,
				hyCardCallback, map);
	}

	private HttpCallBack hyCardCallback = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			dismissLoading();
			ResponseBase responseBase = ResponseManager.getInstance().parse(
					data);
			if (responseBase != null) {
				if (responseBase.ecode == ErrorConst.EC_OK
						|| StringHelper.isNotBlank(responseBase.strError)) {
					JSONObject objData = responseBase.jsonObject;
					/**
					 * upateUI
					 */
					mHandler.sendMessage(mHandler.obtainMessage(0x03, objData));
				} else {
					showHintDialog("网络不给力");
					return;
				}
			} else {
				showHintDialog("网络不给力");
				return;
			}
		}
	};
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0x01 :
					handleFundDetail(msg.obj);
					break;
				case 0x02 :
					handleCardList(msg.obj);
					break;
				case 0x03 :
					handleHYCard(msg.obj);
					break;
			}
		}
	};

	/**
	 * 处理银行卡返回数据
	 * 
	 * @param obj
	 */
	private void handleCardList(Object obj) {
		JSONObject data = (JSONObject) obj;
		try {
			JSONArray cards = data.getJSONArray("cards");
			/**
			 * 已经有银行卡信息，直接跳转
			 */
			if (cards != null && cards.length() > 0) {
				startAnotherActivity();
			} else {
				JSONArray arrayUnboundCard = data.getJSONArray("unbc");
				/**
				 * 有未解绑银行卡
				 */
				if (arrayUnboundCard != null && arrayUnboundCard.length() > 0) {
					recoverCard(arrayUnboundCard);
				} else {
					showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
					startActivity(new Intent(this, QuickBillActivity.class));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showHintDialog("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson="
					+ obj.toString());
		}

	};

	private void handleHYCard(Object data) {
		JSONObject obj = (JSONObject) data;
		int ecode = obj.optInt("ecode");
		String emsg = obj.optString("emsg");
		if (ecode == 0) {
			showToast("银行卡已成功还原");
			startAnotherActivity();
		} else {
			showHintDialog(emsg);
		}
	}

	private void startAnotherActivity() {
		Intent intent = new Intent(this, FundBuyActivity.class);
		intent.putExtra("min_script", minScript);
		intent.putExtra("fdcode", fdCode);
		intent.putExtra("fdname", fdName);
		intent.putExtra("fdtype", fundType);
		intent.putExtra("isShuHui", false);
		intent.putExtra("fee", fee);
		startActivityForResult(intent, Const.FUND_BUY_FLOW);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Const.FUND_BUY_FLOW :
				if (PrefManager.getInstance().getBoolean(
						PrefManager.HAVE_MORE_THAN_ACTIVITY, false)) {
					PrefManager.getInstance().putBoolean(
							PrefManager.HAVE_MORE_THAN_ACTIVITY, false);
					// 显示对话框
					final LotteryActivity mActivity = (LotteryActivity) data
							.getSerializableExtra("activity");
					if (mActivity != null) {
						showTwoButtonDialog(
								getString(R.string.more_than_activity_title),
								mActivity.strMessage,
								getString(R.string.get_red_bag),
								getString(R.string.zhi_dao_l),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 去抽奖页面,还要确定类型
										Intent intent = new Intent(
												FundDetailsActivity.this,
												WebActivity.class);
										intent.putExtra("url", mActivity.strUrl);
										intent.putExtra("webType", 8);
										startActivity(intent);
									}
								}, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
					}
				}
				break;
		}
	}

	private void recoverCard(JSONArray arrayUnboundCard) {
		ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
		final Card maxOldCard = new Card();
		maxOldCard.setBoundTime(9223372036854775806L);

		for (int i = 0; i < arrayUnboundCard.length(); i++) {
			JSONObject jsonOb = (JSONObject) arrayUnboundCard.opt(i);
			String bank = jsonOb.optString("bank");
			String card = jsonOb.optString("card");
			long boundTime = Long.valueOf(jsonOb.optString("boundT"));
			Card info = new Card();
			info.setNumber(card);
			info.setBankName(bank);
			info.setBoundTime(boundTime);
			listUnboundedCard.add(info);
		}
		/**
		 * 取出最先解绑的卡
		 */
		for (Card tempInfo : listUnboundedCard) {
			if (maxOldCard.getBoundTime() > tempInfo.getBoundTime()) {
				maxOldCard.setNumber(tempInfo.getNumber());
				maxOldCard.setBankName(tempInfo.getBankName());
				maxOldCard.setBoundTime(tempInfo.getBoundTime());
			}
		}
		String strMsg = "您已经有解绑过的" + maxOldCard.getBankName() + "卡，卡号为"
				+ StringHelper.hintCardNum(maxOldCard.getNumber()) + "，是否直接还原该卡？";

		showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 发送银行卡还原接口请求
				showLoading();
				requestHYCard(maxOldCard.getNumber());

				dialog.dismiss();
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 根据UI
	 */
	private void handleFundDetail(Object obj) {
		JSONObject data = (JSONObject) obj;
		fdName = data.optString("abbrev");
		fundType = data.optString("type");
		fundTitleText
				.setText(fdName + getString(R.string.left_brackets)
						+ data.optString("fdcode")
						+ getString(R.string.right_brackets));
		minScript = data.optString("minSubscript");
		minMoneyText.setText(minScript + getString(R.string.str_yuan));
		fundUpdateTimeText.setText(DateFormatHelper.formatDate(data
				.optString("last_time").concat("000"), DateFormat.DATE_5));
		buybackText.setText(data.optString("redemp_arrive")
				+ getString(R.string.str_work_day));
		buyConfirmText.setText(data.optString("confirm_day")
				+ getString(R.string.str_work_day));
		fundTypeText.setText(getFundName());

		/**
		 * 货币基金
		 */
		if (data.optString("type").equals("3")) {
			fundProfitTitle.setText(getString(R.string.str_wanfenshouyi));
			fundRoseTitle.setText(getString(R.string.str_sevenchangedate));
			fundProfitText.setText(data.optString("profit10K"));
			fundRoseText.setText(data.optString("rate7Day"));
			feiRateLayout.setVisibility(View.GONE);
		} else {
			fundProfitText.setText(data.optString("nav"));
			double rateMonth = 0.0;
			if (data.optString("rateMonth") != null
					&& !data.optString("rateMonth").equals("")) {
				rateMonth = Double.parseDouble(data.optString("rateMonth"));
			}
			fundRoseText.setTextColor(rateMonth < 0 ? getResources().getColor(
					R.color.color_307d1b) : getResources().getColor(
					R.color.color_e41f23));
			fundRoseText.setText(StringHelper.formatString(String.valueOf(rateMonth),StringFormat.FORMATE_2));
			fundRosePrecentView.setTextColor(rateMonth < 0 ? getResources()
					.getColor(R.color.color_307d1b) : getResources().getColor(
					R.color.color_e41f23));

			feeRateText.setText(StringHelper.formatString(data
					.optString("fee"),StringFormat.FORMATE_2) + getString(R.string.str_percent));
			oldFeeText.setText(StringHelper.formatString(data
					.optString("original_fee"),StringFormat.FORMATE_2)
					+ getString(R.string.str_percent));
			oldFeeText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			fee = data.optString("fee");
		}

		fundState = data.optInt("purchaseStatus");
		switch (fundState) {
			case FUND_PAUSE :
				shenGouBtn.setEnabled(false);
				shenGouBtn.setTextColor(getResources().getColor(
						R.color.color_999999));
				shenGouBtn.setText(getString(R.string.pause_buy_fund));
				break;
			case FUND_INVALIDATE :
				shenGouBtn.setEnabled(false);
				shenGouBtn.setTextColor(getResources().getColor(
						R.color.color_999999));
				shenGouBtn.setText(getString(R.string.pause_buy_invalidate));
				break;
			default :
				break;
		}
	}

	private void initView() {
		fundTitleText = (TextView) findViewById(R.id.fund_title);
		fundTypeText = (TextView) findViewById(R.id.fund_type_text);
		fundUpdateTimeText = (TextView) findViewById(R.id.fund_upate_time);
		fundProfitTitle = (TextView) findViewById(R.id.net_value_title);
		fundProfitText = (TextView) findViewById(R.id.net_value_text);
		fundRoseTitle = (TextView) findViewById(R.id.net_rose_title);
		fundRoseText = (TextView) findViewById(R.id.net_rose_value);
		fundRosePrecentView = (TextView) findViewById(R.id.precent_view);
		minMoneyText = (TextView) findViewById(R.id.minmoney_text);
		buyConfirmText = (TextView) findViewById(R.id.confirm_buy_text);
		buybackText = (TextView) findViewById(R.id.confirm_back_text);
		feiRateLayout = (RelativeLayout) findViewById(R.id.fei_rate);
		feeRateText = (TextView) findViewById(R.id.buy_rate_value);
		oldFeeText = (TextView) findViewById(R.id.old_rate_value);
		shenGouBtn = (Button) findViewById(R.id.shengou_btn);
		btnBack = (Button) findViewById(R.id.bt_back);
		titleText = (TextView) findViewById(R.id.title_middle_text);
		titleText.setText(getString(R.string.fund_detail));
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		shenGouBtn.setOnClickListener(this);

		if (isFromAssemble) {
			shenGouBtn.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		fdCode = intent.getStringExtra("fdcode");
		isFromAssemble = intent.getBooleanExtra("from_asemble", false);
		fundTypeArray = getResources().getStringArray(R.array.fund_type);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.shengou_btn :
				if (UserManager.getInstance().getUser() != null) {
					requestCardList();
				} else {
					Intent intent = new Intent(this, LoginActivity.class);
					intent.putExtra("LoginTarget",
							LoginTarget.FUND_DETAIL.getValue());
					startActivity(intent);
				}
				break;
			case R.id.bt_back :
				finish();
				break;
		}
	}

	/*
	 * 基金类型转化为基金真实类型
	 */
	private String getFundName() {
		String name = null;
		if (TextUtils.equals("1", fundType)) {
			name = fundTypeArray[0];
		} else if (TextUtils.equals("2", fundType)) {
			name = fundTypeArray[1];
		} else if (TextUtils.equals("3", fundType)) {
			name = fundTypeArray[2];
		} else if (TextUtils.equals("4", fundType)) {
			name = fundTypeArray[3];
		} else if (TextUtils.equals("7", fundType)) {
			name = fundTypeArray[4];
		} else if (TextUtils.equals("8", fundType)) {
			name = fundTypeArray[5];
		}
		return name;
	}
}
