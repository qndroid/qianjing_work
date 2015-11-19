
package com.qianjing.finance.ui.activity.assemble.aip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.InvestPlan;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

/**
 * @description 组合定投申购详情页面
 * @author fangyan
 * @date 2015年8月4日
 */
public class AssembleAIPBuyDetailActivity extends BaseActivity implements OnClickListener {

	/**
	 * UI
	 */
	/** 组合名称 */
	private TextView tvAssembleName;
	/** 银行卡名称 */
	private TextView tvCardName;
	/** 定投首付 */
	private RelativeLayout rlDownpay;
	/** 定投首付 */
	private TextView tvDownpay;
	/** 定投月付 */
	private TextView tvMonthly;
	/** 月付日期 */
	private TextView tvDate;
	/** 首次月付日期 */
	private TextView tvDateFirst;
	/** 基金列表 */
	private ListView lvFund;
	private Button mBtnConfirm;

	/**
	 * data
	 */
	/** 定投首付 */
	private double downpay = Flag.NO_VALUE;
	/** 定投月付 */
	private double monthly = Flag.NO_VALUE;
	/** 当前所选定投日 */
	private int mCurrentDay = 0;
	/** 定投首次扣款日期 */
	private String mFirstDayStamp;
	/** 定投首次扣款日期 */
	private String mFirstDay;
	/** 申购银行卡实例 */
	private Card mCard;
	/** 投资计划 */
	private InvestPlan investPlan;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;

	private ArrayList<FundResponseItem> mListFund;

	private String sopid;
	private String sid;

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
			investPlan = intent.getParcelableExtra(Flag.EXTRA_BEAN_INVESTPLAN);
			downpay = intent.getDoubleExtra(Flag.EXTRA_VALUE_DOWNPAYMENT, Flag.NO_VALUE);
			monthly = intent.getDoubleExtra(Flag.EXTRA_VALUE_MONTHLY, Flag.NO_VALUE);
			mAssembleDetail = intent.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
			mCurrentDay = intent.getIntExtra(Flag.EXTRA_VALUE_DATE, 1);
			mFirstDayStamp = intent.getStringExtra(Flag.EXTRA_VALUE_FIRST_DAY);
		} catch (Exception e) {
			showToast(getString(R.string.error_param));
			finish();
		}
	}

	private void initView() {

		setContentView(R.layout.activity_assemble_aip_buy_detail);

		setTitleWithId(R.string.title_assemble_aip_detail);

		setTitleBack();
		setLoadingUncancelable();

		tvAssembleName = (TextView) findViewById(R.id.tv_assemble_name);
		tvCardName = (TextView) findViewById(R.id.tv_card_name);
		rlDownpay = (RelativeLayout) findViewById(R.id.rl_downpayment);
		tvDownpay = (TextView) findViewById(R.id.tv_downpayment);
		tvMonthly = (TextView) findViewById(R.id.tv_monthly);
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvDateFirst = (TextView) findViewById(R.id.tv_date_first);

		lvFund = (ListView) findViewById(R.id.lv_buy_details);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(this);

		// 组合名称
		int type = mAssembleDetail.getAssembleBase().getType();
		tvAssembleName.setText(ViewUtil.getPortfolioName(this, type));

		// 银行卡名称和尾号
		String strTail = ViewUtil.getCardTail(mCard.getNumber());
		tvCardName.setText(mCard.getBankName() + " ( 尾号" + strTail + " ) ");

		// 定投首付
		String strSumbol = getString(R.string.ren_ming_bi);
		if (downpay == Flag.NO_VALUE) {
			rlDownpay.setVisibility(View.GONE);
		} else {
			tvDownpay.setText(strSumbol + StringHelper.formatDecimal(downpay));
		}

		// 定投月付
		tvMonthly.setText(strSumbol + StringHelper.formatDecimal(monthly));

		// 月付日期
		tvDate.setText(String.valueOf(mCurrentDay) + "日");

		if (mFirstDayStamp != null) {
			mFirstDay = DateFormatHelper.formatDate(mFirstDayStamp.concat("000"), DateFormat.DATE_10);
			tvDateFirst.setText(mFirstDay);
		}

		// 基金配置列表
		setAdapterValue(downpay == Flag.NO_VALUE ? 0 : downpay, monthly);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:

			showPasswordDialog();
			break;
		}
	}

	private void showPasswordDialog() {
		float value = getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0);
		final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
		inputDialog.setTitle("支付" + StringHelper.formatDecimal(value) + "元");
		inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = inputDialog.getEditText().getText().toString().trim();
				if (StringHelper.isBlank(text)) {
					showToast("输入登录密码不能为空");
				} else {

					requestPurchase(text);
				}
				dialog.dismiss();
			}
		});
		inputDialog.create().show();
	}

	private void requestPurchase(String pwd) {

		showLoading();

		Hashtable<String, Object> upload = initParams(pwd);
		AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_AIP_BUY, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = TAG_ASSEMBLE_BUY;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private static final int TAG_ASSEMBLE_BUY = 3;
	private static final int TAG_BANK_BACK_COMFIRM = 4;
	private static final int TAG_BANK_BACK_COMFIRM_LOOP = 5;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			String strResponse = (String) msg.obj;

			switch (msg.what) {
			case TAG_ASSEMBLE_BUY:

				handleAssembleBuy(strResponse);
				break;
			case TAG_BANK_BACK_COMFIRM:

				handleBankBackComfirm(strResponse);
				break;
			case TAG_BANK_BACK_COMFIRM_LOOP:

				requestBankBack(sopid, sid);
				break;
			}
		}
	};

	protected void handleAssembleBuy(String obj) {

		if (obj == null || "".equals(obj)) {
			showHintDialog("网络不给力！");
			dismissLoading();
			return;
		}

		//		obj = TestUtil.getJson(this);

		try {
			JSONObject object = new JSONObject(obj);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String mdata = object.optString("data");
			if (ecode == 0) {
				JSONObject object2 = new JSONObject(mdata);
				JSONObject object3 = new JSONObject(object2.optString("schemaLog"));
				sopid = object3.optString("sopid");
				sid = object3.optString("sid");
				JSONArray arrayFund = object3.optJSONArray("list");
				mListFund = new ArrayList<FundResponseItem>();
				for (int i = 0; i < arrayFund.length(); i++) {
					JSONObject objFund = arrayFund.getJSONObject(i);
					FundResponseItem fund = new FundResponseItem();
					fund.abbrev = objFund.optString("abbrev");
					fund.monthState = objFund.optInt("month_state");
					fund.monthSum = objFund.optDouble("month_sum");
					fund.monthReason = objFund.optString("month_reason");
					mListFund.add(fund);
				}
				String opTime = object3.optString("opDate");
				String confirmTime = object3.optString("confirm_time");
				String arriveTime = object3.optString("arrive_time");

				//				downpay = Flag.NO_VALUE;

				if (downpay == Flag.NO_VALUE) {
					Intent intent = new Intent(this, AssembleAIPBuyResultActivity.class);
					intent.putExtra("sid", sid);
					intent.putExtra("sopid", sopid);
					intent.putExtra("opTime", opTime);
					intent.putExtra("confirmTime", confirmTime);
					intent.putExtra("arriveTime", arriveTime);
					intent.putParcelableArrayListExtra("listFund", mListFund);
					intent.putExtra(Flag.EXTRA_VALUE_DOWNPAYMENT, downpay);
					intent.putExtra(Flag.EXTRA_VALUE_FIRST_DAY, mFirstDayStamp);
					startActivityForResult(intent, ViewUtil.REQUEST_CODE);
					return;
				}

				requestBankBack(sopid, sid);
			} else {
				dismissLoading();
				showHintDialog(emsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dismissLoading();
			showHintDialog("网络不给力！");
		}
	}

	protected void handleBankBackComfirm(String obj) {

		if (obj == null || "".equals(obj)) {
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(obj);
			int ecode4 = jsonObject.optInt("ecode");
			String emsg4 = jsonObject.optString("emsg");
			if (ecode4 == 0) {
				String data4 = jsonObject.optString("data");
				JSONObject jsonObject2 = new JSONObject(data4);
				String schemaLog4 = jsonObject2.optString("schemaLog");
				JSONObject jsonObject3 = new JSONObject(schemaLog4);
				JSONArray fdcodes = jsonObject3.optJSONArray("fdcodes");
				JSONArray abbrevs = jsonObject3.optJSONArray("abbrevs");
				JSONArray fdsums = jsonObject3.optJSONArray("fdsums");
				JSONArray fdshares = jsonObject3.optJSONArray("fdshares");
				JSONArray fdstates = jsonObject3.optJSONArray("fdstates");
				JSONArray reasons = jsonObject3.optJSONArray("reasons");

				for (int i = 0; i < fdcodes.length(); i++) {
					FundResponseItem fund = mListFund.get(i);
					fund.fdsum = fdsums.optString(i);
					fund.fdshare = fdshares.optString(i);
					fund.fdstate = fdstates.optInt(i);
					fund.reason = reasons.optString(i);
				}

				String opTime = jsonObject3.optString("opDate");
				String confirmTime = jsonObject3.optString("confirm_time");
				String arriveTime = jsonObject3.optString("arrive_time");
				String estimateSum = jsonObject3.optString("estimate_sum");
				String estimateFee = jsonObject3.optString("estimate_fee");
				String bank = jsonObject3.optString("bank");
				String card = jsonObject3.optString("card");

				Intent intent = new Intent(this, AssembleAIPBuyResultActivity.class);
				intent.putExtra("sid", sid);
				intent.putExtra("sopid", sopid);
				intent.putExtra("opTime", opTime);
				intent.putExtra("confirmTime", confirmTime);
				intent.putExtra("arriveTime", arriveTime);
				intent.putExtra(Flag.EXTRA_VALUE_DOWNPAYMENT, downpay);
				intent.putExtra(Flag.EXTRA_VALUE_FIRST_DAY_STAMP, mFirstDayStamp);
				intent.putExtra(Flag.EXTRA_VALUE_FIRST_DAY, mFirstDay);
				intent.putExtra(Flag.EXTRA_VALUE_MONTHLY, monthly);
				intent.putExtra(Flag.EXTRA_BEAN_CARD_COMMON, mCard);
				intent.putExtra(Flag.EXTRA_VALUE_DATE, mCurrentDay);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);

				intent.putExtra("fee", estimateFee);
				intent.putExtra("money", estimateSum);
				intent.putExtra("card", card);
				intent.putExtra("bank", bank);
				intent.putExtra("msg", CommonUtil.getBuyState(mListFund));
				intent.putExtra("stateCode", CommonUtil.getBuyStateCode(mListFund));
				intent.putParcelableArrayListExtra("listFund", mListFund);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			} else {
				// 如果继续等待则1.5秒后再次确认结果
				if (ecode4 == 212) {
					Message msg4 = Message.obtain();
					msg4.what = TAG_BANK_BACK_COMFIRM_LOOP;
					mHandler.sendMessageDelayed(msg4, 1500);
				} else {
					dismissLoading();
					showHintDialog(emsg4);
				}
			}

		} catch (JSONException e) {
			dismissLoading();
			showHintDialog("数据解析错误");
		}
	}

	private void requestBankBack(String sopid, String sid) {

		showLoading();

		Hashtable<String, Object> mmupload = new Hashtable<String, Object>();
		mmupload.put("sid", sid);
		mmupload.put("sopid", sopid);
		AnsynHttpRequest.requestByPost(this, UrlConst.BANK_BACK, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = TAG_BANK_BACK_COMFIRM;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, mmupload);
	}

	/**
	 * @description 设置基金列表数据源
	 * @author fangyan
	 * @param sum
	 */
	private void setAdapterValue(double downpaySum, double monthlySum) {
		String NAME = "name", DWONPAY = "downpayment", MONTHLY = "monthly";
		ArrayList<Fund> fundList = mAssembleDetail.getAssembleConfig().getFundList();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (fundList != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 添加列表头
			map.put(NAME, "基金名称");
			map.put(DWONPAY, "首次投入(元)");
			map.put(MONTHLY, "每期投入(元)");
			list.add(map);
			for (int i = 0; i < fundList.size(); i++) {
				Fund info = fundList.get(i);
				if (!StringHelper.isBlank(info.name)) {
					map = new HashMap<String, String>();
					map.put(NAME, info.name);
					map.put(DWONPAY, CommonUtil.calFundMoney(downpaySum, Double.valueOf(info.ratio)));
					map.put(MONTHLY, CommonUtil.calFundMoney(monthlySum, Double.valueOf(info.ratio)));
					list.add(map);
				}
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.three_weight_item, new String[] { NAME, DWONPAY, MONTHLY }, new int[] {
				R.id.tv_handle, R.id.tv_abbrev, R.id.tv_shares });
		lvFund.setAdapter(adapter);
		ViewUtil.setListViewFullHeight(lvFund, adapter);
		adapter.notifyDataSetChanged();
	}

	private Hashtable<String, Object> initParams(String pwd) {

		Hashtable<String, Object> upload = new Hashtable<String, Object>();

		upload.put("sum", downpay == Flag.NO_VALUE ? 0 : downpay);
		upload.put("cd", mCard.getNumber());
		upload.put("pwd", pwd);
		upload.put("plan", investPlan.getIndex());
		upload.put("first_date", mFirstDay);
		AssembleBase assemble = mAssembleDetail.getAssembleBase();
		if (assemble != null) {
			upload.put("type", assemble.getType()); // 组合类型
			switch (assemble.getType()) {
			case Const.ASSEMBLE_PENSION:
				upload.put("init", assemble.getPensionInitSum()); // 初始投资金额
				upload.put("age", assemble.getPensionCurrentAge());// 年龄
				upload.put("retire", assemble.getPensionRetireAge());// 退休年龄
				upload.put("month", assemble.getPensionMonthAmount());// 月定投金额
				break;
			case Const.ASSEMBLE_HOUSE:
				upload.put("init", assemble.getHouseInitSum()); // 初始投资金额
				upload.put("money", assemble.getHouseGoalSum());// 目标金额
				upload.put("year", assemble.getHouseYears());// 投资年数
				break;
			case Const.ASSEMBLE_CHILDREN:
				upload.put("money", assemble.getChildGoalSum());// 目标金额
				upload.put("year", assemble.getChildYears());// 投资年数
				break;
			case Const.ASSEMBLE_MARRY:
				upload.put("init", assemble.getMarryInitSum()); // 初始投资金额
				upload.put("money", assemble.getMarryGoalSum());// 目标金额
				upload.put("year", assemble.getMarryYears());// 投资年数
				break;
			}
		}
		return upload;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE, data);
			finish();
		}
	}

}
