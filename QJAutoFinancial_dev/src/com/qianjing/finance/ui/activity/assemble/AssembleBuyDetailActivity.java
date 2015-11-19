
package com.qianjing.finance.ui.activity.assemble;

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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.RewardListActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

/** 
* @description 申购确认界面
* @author fangyan
* @date 2015年4月18日
*/
public class AssembleBuyDetailActivity extends BaseActivity {
	private static final int SELECT_RED_BAG = 0x02;
	/** 基金列表 */
	private ListView lvFund;

	private String sopid;
	private String sid;

	/** 申购银行卡实例 */
	private Card mCard;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;
	private RelativeLayout rewardLayout;
	private TextView rewardView;
	private RedBag currentBag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UMengStatics.onPBuyPage2View(this);

		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_assemble_buy_detail);

		setTitleWithId(R.string.title_buy_confirm);

		setTitleBack();

		mAssembleDetail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		mCard = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);

		lvFund = (ListView) findViewById(R.id.lv_buy_details);
		setAdapterValue(getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0));

		Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPasswordDialog();
			}
		});

		rewardLayout = (RelativeLayout) findViewById(R.id.reward_layout);
		rewardLayout.setVisibility(View.VISIBLE);
		rewardView = (TextView) findViewById(R.id.reward_money_view);
		if (mAssembleDetail.getAssembleBase().getType() == Const.ASSEMBLE_DREAM) {
			rewardLayout.setVisibility(View.GONE);
		} else {
			rewardLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AssembleBuyDetailActivity.this, RewardListActivity.class);
					intent.putExtra("selected_redbag", currentBag);
					intent.putExtra("buy_account", String.valueOf(getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0)));
					startActivityForResult(intent, SELECT_RED_BAG);
				}
			});
		}
		initAssembleName();
	}

	/**
	 * <p>
	 * Title: initAssembleName
	 * </p>
	 * <p>
	 * Description: 设置组合名称
	 * </p>
	 */
	private void initAssembleName() {
		String strAssembleName = "";
		AssembleDetail detail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		AssembleBase assemble = detail.getAssembleBase();
		if (assemble != null) {
			switch (assemble.getType()) {
			case Const.ASSEMBLE_INVEST:
				strAssembleName = getString(R.string.title_assemble_invest);
				break;
			case Const.ASSEMBLE_PENSION:
				strAssembleName = getString(R.string.title_assemble_pension);
				break;
			case Const.ASSEMBLE_HOUSE:
				strAssembleName = getString(R.string.title_assemble_house);
				break;
			case Const.ASSEMBLE_CHILDREN:
				strAssembleName = getString(R.string.title_assemble_children);
				break;
			case Const.ASSEMBLE_MARRY:
				strAssembleName = getString(R.string.title_assemble_marry);
				break;
			case Const.ASSEMBLE_DREAM:
				strAssembleName = getString(R.string.title_assemble_dream);
				break;
			case Const.ASSEMBLE_SPESIAL:
				strAssembleName = getString(R.string.title_assemble_special);
			}
			TextView tvAssembleName = (TextView) findViewById(R.id.tv_group_name);
			if (!StringHelper.isBlank(strAssembleName))
				tvAssembleName.setText(strAssembleName + "组合");
		}
	}

	private void showPasswordDialog() {

		UMengStatics.onPBuyPage2Password(this);

		final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
		inputDialog.setTitle("支付"
				+ StringHelper.formatString(String.valueOf(getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0)), StringFormat.FORMATE_2)
				+ "元");
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
					String sid = mAssembleDetail.getAssembleConfig().getSid();
					if (!StringHelper.isBlank(sid) && !TextUtils.equals("0", sid)) {
						requestAddPurchase(text);
					} else {
						requestPurchase(text);
					}
				}
				dialog.dismiss();
			}
		});
		inputDialog.create().show();
	}

	// 追加申购接口
	private void requestAddPurchase(String pwd) {

		showLoading();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", mAssembleDetail.getAssembleConfig().getSid());
		upload.put("sum", String.valueOf(getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0)));
		upload.put("cd", mCard.getNumber());
		upload.put("pwd", pwd);
		AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_BUY_ADD, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = TAG_ASSEMBLE_BUY;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	// 申购接口
	private void requestPurchase(String pwd) {

		showLoading();

		Hashtable<String, Object> upload = initParams(pwd);
		AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_BUY, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = TAG_ASSEMBLE_BUY;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private Hashtable<String, Object> initParams(String pwd) {

		float sum = getIntent().getFloatExtra(Flag.EXTRA_VALUE_ASSEMBLE_BUY, 0);
		String cardNumber = mCard.getNumber();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		// upload.put("sid", getIntent().getStringExtra("sid")); //1.3.4版起更改组合申购接口定义
		upload.put("sum", String.valueOf(sum));
		upload.put("cd", cardNumber);
		upload.put("pwd", pwd);
		/*
		 * 添加组合接口的参数
		 */
		AssembleDetail detail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		AssembleBase assemble = detail.getAssembleBase();
		if (assemble != null) {

			upload.put("type", assemble.getType()); // 组合类型
			upload.put("nm", ""); // 组合名
			upload.put("rate", 0);// 期望收益率

			switch (assemble.getType()) {
			case Const.ASSEMBLE_INVEST:
				upload.put("init", assemble.getInvestInitSum()); // 初始投资金额
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
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", assemble.getHouseInitSum());// 目标金额
				upload.put("year", assemble.getHouseYears());// 投资年数
				upload.put("nmonth", 0);// 投资月数
				break;
			case Const.ASSEMBLE_CHILDREN:
				upload.put("init", ""); // 初始投资金额
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
				upload.put("risk", 0);// 风险偏好
				upload.put("age", 0);// 年龄
				upload.put("retire", 0);// 退休年龄
				upload.put("month", 0);// 月定投金额
				upload.put("money", 0);// 目标金额
				upload.put("year", 0);// 投资年数
				upload.put("nmonth", assemble.getDreamMonths());// 投资月数
				break;
			case Const.ASSEMBLE_SPESIAL:
				upload.put("age", assemble.getSpecialAge());
				upload.put("init", assemble.getSpecialInitSum());
				upload.put("risk", assemble.getSpecialRisk());
				upload.put("preference", assemble.getSpecialPref());
				// upload.put("money", assemble.getSpecialMoney());
				break;
			}
		}
		return upload;
	}

	protected void handleAssembleBuy(String obj) {

		if (obj == null || "".equals(obj)) {
			showHintDialog("网络不给力！");
			dismissLoading();
			return;
		}

		try {
			JSONObject object = new JSONObject(obj);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String mdata = object.optString("data");
			if (ecode == 0) {
				JSONObject object2 = new JSONObject(mdata);
				String subobject2 = object2.optString("schemaLog");
				JSONObject object3 = new JSONObject(subobject2);
				sopid = object3.optString("sopid");
				sid = object3.optString("sid");

				requestBankBack(sopid, sid);
			} else {
				dismissLoading();
				showHintDialog(emsg);
			}
		} catch (Exception e) {
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

				ArrayList<FundResponseItem> infoList = new ArrayList<FundResponseItem>();
				for (int i = 0; i < fdcodes.length(); i++) {
					FundResponseItem responseItemInfo = new FundResponseItem(fdcodes.optString(i), abbrevs.optString(i), fdsums.optString(i),
							fdshares.optString(i), fdstates.optInt(i), reasons.optString(i));
					infoList.add(responseItemInfo);
				}

				String opDateShow = jsonObject3.optString("opDate");
				String confirmTime = jsonObject3.optString("confirm_time");
				String arriveTime = jsonObject3.optString("arrive_time");
				String estimateSum = jsonObject3.optString("estimate_sum");
				String estimateFee = jsonObject3.optString("estimate_fee");
				String bank = jsonObject3.optString("bank");
				String card = jsonObject3.optString("card");

				/*
				 * 判断是否购买成功
				 */
				dismissLoading();
				if (currentBag != null) {
					RequestActivityHelper.requestAssembleRedBag(this, sopid, String.valueOf(currentBag.id), new IHandleBase() {
						@Override
						public void handleResponse(ResponseBase responseBase, int status) {
						}
					});
				}
				Intent intent = new Intent(this, AssembleBuyResultActivity.class);
				intent.putExtra("sid", sid);
				intent.putExtra("sopid", sopid);
				intent.putExtra("opDateShow", opDateShow);
				intent.putExtra("profitTimeShow", confirmTime);
				intent.putExtra("profitArriveShow", arriveTime);
				intent.putExtra("fee", estimateFee);
				intent.putExtra("money", estimateSum);
				intent.putExtra("card", card);
				intent.putExtra("bank", bank);
				intent.putExtra("msg", CommonUtil.getBuyState(infoList));
				intent.putExtra("stateCode", CommonUtil.getBuyStateCode(infoList));
				intent.putParcelableArrayListExtra("infoList", infoList);
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

	private static final int TAG_ASSEMBLE_BUY = 3;
	private static final int TAG_BANK_BACK_COMFIRM = 4;
	private static final int TAG_BANK_BACK_COMFIRM_LOOP = 5;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			String strResponse = (String) msg.obj;

			switch (msg.what) {
			case TAG_ASSEMBLE_BUY:
				// 确认付款
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

	/**
	 * 设置adapter数据源
	 * 
	 * @param sum 输入的金额
	 */
	private void setAdapterValue(double sum) {
		ArrayList<Fund> fundList = mAssembleDetail.getAssembleConfig().getFundList();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (fundList != null) {
			// 添加列表头
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "基金名称");
			map.put("ratio", "占比(%)");
			map.put("money", "投入金额(元)");
			list.add(map);
			for (int i = 0; i < fundList.size(); i++) {
				Fund info = fundList.get(i);
				if (info.rate > 0) {
					map = new HashMap<String, String>();
					map.put("name", info.name);
					map.put("ratio", StringHelper.formatString(String.valueOf(info.rate * 100), StringFormat.FORMATE_1));
					map.put("money", CommonUtil.calFundMoney(sum, info.rate));
					list.add(map);
				}
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.three_weight_item, new String[] { "name", "ratio", "money" }, new int[] {
				R.id.tv_handle, R.id.tv_abbrev, R.id.tv_shares });
		lvFund.setAdapter(adapter);
		ViewUtil.setListViewFullHeight(lvFund, adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, data);
			finish();
		}

		switch (requestCode) {
		case SELECT_RED_BAG:
			if (resultCode == RESULT_OK) {
				currentBag = (RedBag) data.getSerializableExtra("redbag");
				/**
				 * updateUI
				 */
				if (currentBag != null) {
					rewardView.setTextColor(getResources().getColor(R.color.red_VI));
					rewardView.setText(getString(R.string.ren_ming_bi) + currentBag.val + getString(R.string.dai_jin_quan));
				}
			} else {
				currentBag = null;
				rewardView.setTextColor(getResources().getColor(R.color.color_6c6c6c));
				rewardView.setText(getString(R.string.use_dai_jin_quan));
			}
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
