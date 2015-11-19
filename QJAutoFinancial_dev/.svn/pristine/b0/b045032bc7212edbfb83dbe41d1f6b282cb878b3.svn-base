
package com.qianjing.finance.ui.activity.assemble.quickbuy;

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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.RewardListActivity;
import com.qianjing.finance.util.CheckTools;
import com.qianjing.finance.util.Utility;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.ViewHolder;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

/**
 * <p>Title: AssembleBuyDetailActivity</p>
 * <p>Description: 组合申购详情页面</p>
 * <p>Company: www.qianjing.com</p> 
 * @author liuchen
 * @date 2015年4月27日
 */
public class QuickBuyConfirmActivity extends BaseActivity {

	private static final int SELECT_RED_BAG = 0x04;
	private Bundle bundle;
	private RedBag currentBag = null;
	private RelativeLayout rewardLayout;
	private TextView rewardView;

	private ArrayList<String> fundList;
	private HashMap<String, Float> fundMap;
	private ListView lv_buy_details;
	private BuyDetailsAdapter detailAdapter;
	private Button nextBuy;

	private String sopid;
	private String sid;
	/** 推荐组合申购 */
	private boolean isFromRecommend;
	private String mProductName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bundle = getIntent().getExtras();
		isFromRecommend = getIntent().getBooleanExtra("isFromRecommend", false);
		mProductName = getIntent().getStringExtra("product_name");

		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		setContentView(R.layout.activity_assemble_buy_detail);

		setTitleWithId(R.string.title_assemble_buy_detail);

		setTitleBack();

		fundList = new ArrayList<String>();
		fundMap = new HashMap<String, Float>();

		requestPurchaseDetails();

		lv_buy_details = (ListView) findViewById(R.id.lv_buy_details);

		TextView groupName = (TextView) findViewById(R.id.tv_group_name);
		if (isFromRecommend)
			groupName.setText(mProductName);
		else
			groupName.setText(getGroupName(bundle.getInt("type")));

		nextBuy = (Button) findViewById(R.id.btn_confirm);
		nextBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPasswordDialog();
			}
		});

		rewardLayout = (RelativeLayout) findViewById(R.id.reward_layout);
		rewardLayout.setVisibility(View.VISIBLE);
		rewardView = (TextView) findViewById(R.id.reward_money_view);
		rewardLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent redBagIntent = new Intent(QuickBuyConfirmActivity.this, RewardListActivity.class);
				redBagIntent.putExtra("selected_redbag", currentBag);
				redBagIntent.putExtra("buy_account", bundle.getFloat("sum") + "");
				startActivityForResult(redBagIntent, SELECT_RED_BAG);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_BUY_RESULT_CODE) {
			setResult(ViewUtil.ASSEMBLE_BUY_RESULT_CODE, data);
			finish();
		} else if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("redbag")) {
				currentBag = (RedBag) data.getSerializableExtra("redbag");
				/**
				 * updateUI
				 */
				if (currentBag != null) {
					rewardView.setTextColor(getResources().getColor(R.color.red_VI));
					rewardView.setText(getString(R.string.ren_ming_bi) + currentBag.val + getString(R.string.dai_jin_quan));
				}
			}
		} else {
			currentBag = null;
			rewardView.setTextColor(getResources().getColor(R.color.color_6c6c6c));
			rewardView.setText(getString(R.string.use_dai_jin_quan));
		}
	}

	private String getGroupName(int type) {
		switch (type) {
		case 1:
			return "进取型组合";
		case 2:
			return "稳健性组合";
		case 3:
			return "保守型组合";
		default:
			return "理财增值组合";
		}
	}

	private void showPasswordDialog() {

		final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
		inputDialog.setTitle("支付" + StringHelper.formatDecimal(bundle.getFloat("sum")) + "元");
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
					Toast.makeText(QuickBuyConfirmActivity.this, "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
				} else {
					showLoading();
					requestPurchase(text);
				}
				dialog.dismiss();
			}
		});
		inputDialog.create().show();
	}

	private void initAdapter() {
		if (detailAdapter == null) {
			detailAdapter = new BuyDetailsAdapter();
			lv_buy_details.setAdapter(detailAdapter);
			Utility.setListViewHeightBasedOnChildren(lv_buy_details);
		} else {
			detailAdapter.notifyDataSetChanged();
		}
	}

	// 获取组合详情
	private void requestPurchaseDetails() {
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		if (isFromRecommend) {
			hashTable.put("risk", bundle.getInt("type"));
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.RECOMMAND_FUND_DETAIL, detailsCallBack, hashTable);
		} else {
			hashTable.put("risk_type", bundle.getInt("type"));
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.QUICK_BUY_DETAIL, detailsCallBack, hashTable);
		}
	}

	// 用户申购接口
	private void requestPurchase(String pwd) {

		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("pwd", pwd);
		hashTable.put("cd", bundle.getString("cd"));
		hashTable.put("sum", bundle.getFloat("sum") + "");
		// 申购
		if (isFromRecommend) {
			hashTable.put("risk", bundle.getInt("type"));
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.STEADY_BUY, pruchaseCallBack, hashTable);
		} else {
			hashTable.put("risk_type", bundle.getInt("type"));
			AnsynHttpRequest.requestByPost(mApplication, UrlConst.QUICK_BUY, pruchaseCallBack, hashTable);
		}
	}

	// 用户申购等待结果接口
	private void requestPurchaseWait(String sopid, String sid) {
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("sid", sid);
		hashTable.put("sopid", sopid);
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.WAIT_ORDER, waitCallBack, hashTable);
	}

	HttpCallBack detailsCallBack = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};

	HttpCallBack pruchaseCallBack = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = 2;
			handler.sendMessage(msg);
		}
	};

	HttpCallBack waitCallBack = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = 3;
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonData = (String) msg.obj;
			switch (msg.what) {
			case 1:
				analysisPurchaseDetails(jsonData);
				break;
			case 2:
				analysisPurchase(jsonData);
				break;
			case 3:
				// 申购等待接口
				waitPurchase(jsonData);
				break;
			case 4:
				requestPurchaseWait(sopid, sid);
				break;
			}
		};
	};

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
				JSONArray abbrevs = assemble.optJSONArray("abbrevs");
				JSONArray ratios = assemble.optJSONArray("ratios");

				for (int i = 0; i < abbrevs.length(); i++) {

					String abbname = CheckTools.checkJson(abbrevs.getString(i));
					float share = CheckTools.checkJson((float) ratios.getDouble(i));
					fundList.add(abbname);
					fundMap.put(abbname, share);
				}
				initAdapter();

			} else {
				dismissLoading();
				showHintDialog(emsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void waitPurchase(String jsonData) {

		if (jsonData == null || "".equals(jsonData)) {
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}

		ArrayList<FundResponseItem> infoList = new ArrayList<FundResponseItem>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			int ecode = jsonObject.optInt("ecode");
			String emsg = jsonObject.optString("emsg");
			if (ecode == 0) {
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

				Intent intent = new Intent(this, QuickBuyResultActivity.class);
				intent.putExtra("sid", sid);
				intent.putExtra("sopid", sopid);
				intent.putExtra("opDateShow", opDateShow);
				intent.putExtra("profitTimeShow", confirmTime);
				intent.putExtra("profitArriveShow", arriveTime);
				intent.putExtra("fee", estimateFee);
				intent.putExtra("money", estimateSum);
				intent.putExtra("card", card);
				intent.putExtra("bank", bank);
				intent.putExtra("msg", getBuyState(infoList));
				intent.putExtra("stateCode", getBuyStateCode(infoList));
				intent.putParcelableArrayListExtra("infoList", infoList);

				if (currentBag != null) {
					RequestActivityHelper.requestAssembleRedBag(this, sopid, String.valueOf(currentBag.id), new IHandleBase() {
						@Override
						public void handleResponse(ResponseBase responseBase, int status) {
						}
					});
				}
				startActivityForResult(intent, CustomConstants.QUICK_PUECHASE);
				QuickBuyConfirmActivity.this.setResult(CustomConstants.SHUDDOWN_ACTIVITY);

			} else if (ecode == 212) {
				Message msg = Message.obtain();
				msg.what = 4;
				handler.sendMessageDelayed(msg, 1500);
			}
		} catch (JSONException e) {
			dismissLoading();
			e.printStackTrace();
		}
	}

	protected void analysisPurchase(String jsonData) {

		if (jsonData == null || "".equals(jsonData)) {
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject object = new JSONObject(jsonData);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String mdata = object.optString("data");
			if (ecode == 132) {
				showHintDialog("密码输入不正确,请重新输入");
				dismissLoading();
			} else if (ecode == 0) {

				JSONObject object2 = new JSONObject(mdata);
				String subobject2 = object2.optString("schemaLog");
				JSONObject object3 = new JSONObject(subobject2);

				sopid = object3.optString("sopid");
				sid = object3.optString("sid");

				requestPurchaseWait(sopid, sid);
				// 保存组合

			} else {
				dismissLoading();
				showHintDialog(emsg);
			}
		} catch (Exception e) {
			showHintDialog("网络不给力！");
		}
	}

	private class BuyDetailsAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			if (fundList.size() >= 4) {
				return fundList.size() + 1;
			}
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = View.inflate(mApplication, R.layout.three_weight_item, null);
			}

			TextView fundName = ViewHolder.get(convertView, R.id.tv_handle);
			TextView fundShare = ViewHolder.get(convertView, R.id.tv_abbrev);
			TextView fundValue = ViewHolder.get(convertView, R.id.tv_shares);

			if (position == 0) {
				fundName.setText("基金名称");
				fundShare.setText("占比(%)");
				fundValue.setText("投入金额(元)");
			} else {
				if (position > fundList.size()) {
					fundName.setText("");
					fundShare.setText("");
					fundValue.setText("");
				} else {
					fundName.setText(fundList.get(position - 1));
					fundShare.setText(StringHelper.formatDecimal(fundMap.get(fundList.get(position - 1)) * 100) + "");
					fundValue.setText(StringHelper.formatDecimal((fundMap.get(fundList.get(position - 1)) * bundle.getFloat("sum"))) + "");
				}
			}
			return convertView;
		}
	}

	protected String getBuyState(ArrayList<FundResponseItem> list) {
		String stateStr = "";
		switch (getBuyStateCode(list)) {
		case 1:
			stateStr = "扣款成功";
			break;
		case 11:
			stateStr = "部分扣款成功";
			break;
		case 0:
			stateStr = "交易申请已受理";
			break;
		case 10:
			stateStr = "部分交易申请已受理";
			break;
		case 4:
			stateStr = "扣款失败";
			break;

		default:
			stateStr = "申购失败";
			break;
		}
		return stateStr;
	}

	protected int getBuyStateCode(ArrayList<FundResponseItem> list) {
		int[] arr = null;
		if (list.size() > 0) {
			arr = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = list.get(i).fdstate;
			}
		}
		int stateCode = getResultCode(arr);
		return stateCode;
	}

	protected int getResultCode(int[] arr) {// 返回值为负数为获取状态码失败
		int flag = -1;
		int max = -1;
		int min = -1;
		String str = "";
		if (arr == null) {
			return flag;
		}
		for (int i = 0; i < arr.length; i++) {
			str = str + arr[i];
			if (i == 0) {// 赋数组第一个值
				flag = arr[i];
				max = arr[i];
				min = arr[i];
			}
			if (min > arr[i]) {// 记录最小值
				min = arr[i];
			}
			if (max < arr[i]) {//
				max = arr[i];
			}
		}
		// System.out.println("数字叠加的字符串："+str);
		if (min == max && max == 2) {
			flag = -2;
			return flag;
		}
		if (min == max) {
		} else {
			if (str.contains("3") && max >= 3) {
				flag = 13;
				return flag;
			}
			if (str.contains("1") && max >= 1) {
				flag = 11;
				return flag;
			}
			if (str.contains("0") && max >= 0) {
				flag = 10;
				return flag;
			}

			flag = -2;
			return flag;
		}
		if (flag < 5 && flag >= 0) {// 若不等式0<=flag<5不成立 flag置为-2；

		} else if (flag < 15 && flag >= 10) {

		} else {
			flag = -2;
		}
		return flag;
	}

}
