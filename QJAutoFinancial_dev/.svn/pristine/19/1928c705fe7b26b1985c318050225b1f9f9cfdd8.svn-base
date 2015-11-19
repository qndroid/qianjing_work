package com.qianjing.finance.ui.activity.fund.asset;

import com.qianjing.finance.model.fund.MyFundAssets;
import com.qianjing.finance.model.fund.MyFundHoldingDetails;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.fund.BaseFundHistory;
import com.qianjing.finance.ui.activity.fund.buy.FundBuyActivity;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.activity.history.FundHistoryActivity;
import com.qianjing.finance.util.CheckTools;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.Network;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.widget.ViewHolder;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * this is holding details page author:liuchen
 */

public class FundMineHoldingDetails extends BaseFundHistory implements OnClickListener
{

	private TextView fundName;

	private TextView fundCode;

	private LinearLayout yetView;

	private TextView yetProfit;

	private TextView totalView;

	private TextView purchaseValue;

	private TextView redempingValue;

	private TextView unpaidValue;

	private TextView cumulateView;

	private TextView cumulateProfit;

	private TextView holdingShares;

	private LinearLayout moreHistory;

	private Button btnRedeem;

	private Button btnPurchure;

	private MyFundAssets assetInfo;
	private List<MyFundAssets> holdingList;
	private HashMap<String, String> cardAssetMap;
	private String fdcode;
	private String uid;
	private TextView typeTitle;
	private LinearLayout switchHide;
	private View line;
	private String type;
	private String fee = "0.0";
	private SparseArray<Float> sa;
	private String fdname;

	private String nav;
	private String profit10k;

	private TextView typeValue;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		getPreviousData();
		// 用来存放展示数据
		sa = new SparseArray<Float>();
		holdingList = new ArrayList<MyFundAssets>();
		cardAssetMap = new HashMap<String, String>();
		requestFundHoldingDetails(fdcode);
		requestHistory(0, 4, fdcode);
	}

	private void getPreviousData()
	{
		// form previous page
		Bundle bundle = getIntent().getExtras();
		MyFundHoldingDetails fundDetails = (MyFundHoldingDetails) bundle.getSerializable("fundDtails");
		fdcode = fundDetails.getFdcode();
		type = fundDetails.getType();
		fdname = fundDetails.getAbbrev();
		nav = fundDetails.getNav();
		profit10k = fundDetails.getProfit10K();
		minAddition = fundDetails.getMin_addition();
		minHold = fundDetails.getMin_hold();
		minRedemp = fundDetails.getMin_redemp();
	}

	private void initView()
	{
		// init base view
		setContentView(R.layout.activity_holding_details);
		setTitleWithId(R.string.title_fund_holding_details);
		setTitleBack();
		showLoading();
		initEvent();

	}

	private void initCurrentPage()
	{
		// 基金名称
		fundName.setText(fdname);
		// 基金代码
		fundCode.setText("(" + fdcode + ")");

		if ("3".equals(type))
		{

			// 如果是货币基金
			typeTitle.setText("万份收益(元)");
			typeValue.setText(profit10k);
			switchHide.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			purevalueTitle.setText("累计申购(元)");
			// 持仓份额
			FormatNumberData.simpleFormatNumber(sa.get(5), holdingShares);
			// 未付收益
			FormatNumberData.simpleFormatNumber(sa.get(4), unpaidValue);
			// 累计申购
			FormatNumberData.simpleFormatNumber(sa.get(6), cumulateView);

		}
		else
		{

			// 如果是非货币基金
			typeTitle.setText("最新净值(元)");
			typeValue.setText(nav);
			switchHide.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			purevalueTitle.setText("持仓份额(份)");
			FormatNumberData.noneFormat(sa.get(5), cumulateView);

		}

		// 共同数据
		// 总市值
		FormatNumberData.simpleFormatNumber(sa.get(1), totalView);
		if (sa.get(2) == 0)
		{
			llPurchase.setVisibility(View.GONE);
		}
		else
		{
			llPurchase.setVisibility(View.VISIBLE);
			// 申购中的值
			FormatNumberData.simpleFormatNumber(sa.get(2), purchaseValue);
		}

		if (sa.get(3) == 0)
		{
			llRedemping.setVisibility(View.GONE);
		}
		else
		{
			llRedemping.setVisibility(View.VISIBLE);
			// 赎回中的值
			FormatNumberData.simpleFormatNumber(sa.get(3), redempingValue);
		}

		// 累计盈亏
		FormatNumberData.simpleFormatNumberPM(sa.get(7), cumulateProfit);

	}

	private void initEvent()
	{

		// 基金名称
		fundName = (TextView) findViewById(R.id.tv_fund_name_details);
		// 基金代码
		fundCode = (TextView) findViewById(R.id.tv_fund_code_details);

		// 总市值
		totalView = (TextView) findViewById(R.id.tv_total_value_details);
		// 申购中的值
		purchaseValue = (TextView) findViewById(R.id.tv_purchase_value);
		// 赎回中的值
		redempingValue = (TextView) findViewById(R.id.tv_redemping_value);
		// 累计盈亏
		cumulateProfit = (TextView) findViewById(R.id.tv_holdingshare_value);
		// 更多交易记录
		moreHistory = (LinearLayout) findViewById(R.id.tv_more_history);
		moreHistory.setOnClickListener(this);
		// 赎回按钮
		btnRedeem = (Button) findViewById(R.id.btn_redeemback);
		btnRedeem.setOnClickListener(this);
		// 申购按钮
		btnPurchure = (Button) findViewById(R.id.btn_purchure);
		btnPurchure.setOnClickListener(this);
		typeTitle = (TextView) findViewById(R.id.tv_million_profit);
		typeValue = (TextView) findViewById(R.id.tv_million_profit_value);
		switchHide = (LinearLayout) findViewById(R.id.ll_type_switch_hide);
		line = findViewById(R.id.v_switch_line);
		// 累计申购--在股票基金的情况下变为持仓份额
		cumulateView = (TextView) findViewById(R.id.tv_purevalue_value);
		purevalueTitle = (TextView) findViewById(R.id.tv_purevalue_title);

		// 未付收益--在股票基金的情况下隐藏
		unpaidValue = (TextView) findViewById(R.id.tv_unpaid_value);
		// 持仓份额--在股票基金的情况下隐藏
		holdingShares = (TextView) findViewById(R.id.tv_cumulative_value);

		lvHistory = (ListView) findViewById(R.id.lv_holding_history);

		LinearLayout fundDetails = (LinearLayout) findViewById(R.id.ll_fund_details);
		fundDetails.setOnClickListener(this);

		llPurchase = (LinearLayout) findViewById(R.id.ll_purchase);
		llRedemping = (LinearLayout) findViewById(R.id.ll_redemping);

	}

	private void requestFundHoldingDetails(String fdcode)
	{
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("fund_code", fdcode);
		AnsynHttpRequest.requestByPost(mApplication, UrlConst.URL_FUND_MINE_HOLD_DETAIL, detailsCallback, hashTable);
	}

	HttpCallBack detailsCallback = new HttpCallBack()
	{

		@Override
		public void back(String data, int status)
		{
			Message msg = Message.obtain();
			msg.obj = data;
			msg.what = 0x01;
			handler.sendMessage(msg);

		}
	};

	Handler handler = new Handler()
	{

		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0x01:
				String jsonStr = (String) msg.obj;
				analysisDetails(jsonStr);
				dismissLoading();
				break;
			case 0x02:
				handleFundDetail(msg.obj.toString());
				break;
			}

		};
	};

	private HistoryAdapter historyAdapter;

	private ListView lvHistory;

	private String minAddition;

	private String minHold;

	private String minRedemp;

	private double usableAssets;

	private TextView purevalueTitle;

	private LinearLayout llPurchase;

	private LinearLayout llRedemping;

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_more_history:
			Intent intent01 = new Intent(this, FundHistoryActivity.class);
			intent01.putExtra("fdcode", fdcode);
			startActivity(intent01);
			break;

		case R.id.btn_purchure:

			if (Network.checkNetWork(getApplicationContext()))
			{
				requestFundDetail();
			}
			else
			{
				showHintDialog(getString(R.string.net_prompt));
			}

			break;
		case R.id.btn_redeemback:

			if (!Network.checkNetWork(getApplicationContext()))
			{
				showHintDialog(getString(R.string.net_prompt));
				break;
			}
			Intent intent0 = new Intent(this, FundBuyActivity.class);
			intent0.putExtra("min_script", minHold); // 最小保留额
			intent0.putExtra("min_redeem", minRedemp); // 最小保留额
			intent0.putExtra("fdcode", fdcode);
			intent0.putExtra("fdname", fdname);
			intent0.putExtra("fdtype", type);
			intent0.putExtra("pure_value", Double.parseDouble(nav));
			intent0.putExtra("isShuHui", true);
			/**
			 * 银行卡持有份额
			 */
			for (int i = 0; i < holdingList.size(); i++)
			{

				String cardName = holdingList.get(i).getCard();
				String usableAssets = holdingList.get(i).getUsable_assets();
				if (Double.parseDouble(usableAssets) > 0)
				{

					cardAssetMap.put(cardName, usableAssets);
				}
			}
			if (cardAssetMap.size() <= 0)
			{
				showHintDialog("没有份额可以赎回!");
			}
			else
			{
				intent0.putExtra("card_asset", cardAssetMap);
				startActivityForResult(intent0, Const.FUND_REDEEM_FLOE);
			}
			break;
		case R.id.ll_fund_details:

			if (Network.checkNetWork(getApplicationContext()))
			{
				Intent intent1 = new Intent(this, FundDetailsActivity.class);
				intent1.putExtra("fdcode", fdcode);
				startActivity(intent1);
			}
			else
			{
				showHintDialog(getString(R.string.net_prompt));
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		switch (requestCode)
		{

		case Const.FUND_BUY_FLOW:
			if (resultCode == RESULT_OK)
			{
				if (this != null)
				{
					finish();
				}
			}
			break;
		case Const.FUND_REDEEM_FLOE:
			if (resultCode == RESULT_OK)
			{
				if (this != null)
				{
					finish();
				}
			}
			break;
		}
	}

	protected void analysisDetails(String jStr)
	{

		if (jStr == null || "".equals(jStr))
		{
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try
		{

			JSONObject jsonObj = new JSONObject(jStr);
			int ecode = jsonObj.optInt("ecode");
			String emsg = jsonObj.optString("emsg");

			if (ecode == 0)
			{

				JSONObject data = jsonObj.optJSONObject("data");
				JSONArray fundAssets = data.optJSONArray("assets_details");

				for (int i = 0; i < fundAssets.length(); i++)
				{

					assetInfo = new MyFundAssets();

					JSONObject jItem = fundAssets.getJSONObject(i);

					assetInfo.setCard(CheckTools.checkJson(jItem.optString("card")));
					assetInfo.setIncome(CheckTools.checkJson(jItem.optString("income")));
					assetInfo.setInvest(CheckTools.checkJson(jItem.optString("invest")));
					assetInfo.setMarketValue(CheckTools.checkJson(jItem.optString("market_value")));
					assetInfo.setNav(CheckTools.checkJson(jItem.optString("nav")));
					assetInfo.setProfit(CheckTools.checkJson(jItem.optString("profit")));
					assetInfo.setRedemping(CheckTools.checkJson(jItem.optString("redemping")));
					assetInfo.setShares(CheckTools.checkJson(jItem.optString("shares")));
					assetInfo.setSubscripting(CheckTools.checkJson(jItem.optString("subscripting")));
					assetInfo.setUid(CheckTools.checkJson(jItem.optString("uid")));
					assetInfo.setUnpaid(CheckTools.checkJson(jItem.optString("unpaid")));
					assetInfo.setUsable_assets(CheckTools.checkJson(jItem.optString("usable_assets")));
					holdingList.add(assetInfo);

				}

				initPlayData();
				initCurrentPage();

			}
			else
			{
				dismissLoading();
				showHintDialog(emsg);
			}

		}
		catch (Exception e)
		{
			dismissLoading();
			showHintDialog(getString(R.string.net_data_error));
		}
	}

	private void initPlayData()
	{

		sa.clear();
		// init the view data
		for (int i = 0; i < holdingList.size(); i++)
		{

			// 总市值
			float marketValue = Float.parseFloat(holdingList.get(i).getMarketValue());

			// 申购中
			float subscripting = Float.parseFloat(holdingList.get(i).getSubscripting());
			// 赎回中
			float redemping = Float.parseFloat(holdingList.get(i).getRedemping());
			// 未付收益
			float unpaid = Float.parseFloat(holdingList.get(i).getUnpaid());
			// 持仓份额
			float shares = Float.parseFloat(holdingList.get(i).getShares());
			// 累计申购
			float invest = Float.parseFloat(holdingList.get(i).getInvest());
			// 累计盈亏
			float profit = Float.parseFloat(holdingList.get(i).getProfit());

			// 将多张卡的收益累加
			storeMsg(1, marketValue);
			storeMsg(2, subscripting);
			storeMsg(3, redemping);
			storeMsg(4, unpaid);
			storeMsg(5, shares);
			storeMsg(6, invest);
			storeMsg(7, profit);

		}
	}

	private void storeMsg(int i, float j)
	{

		if (sa.get(i) != null)
		{
			sa.put(i, j + sa.get(i));
		}
		else
		{
			sa.put(i, j);
		}
	}

	@Override
	protected void initHistoryAdapter()
	{

		if (historyList != null)
		{
			if (historyAdapter == null)
			{
				historyAdapter = new HistoryAdapter();
				lvHistory.setAdapter(historyAdapter);
				Util.setListViewHeightBasedOnChildren(lvHistory);
			}
			else
			{
				historyAdapter.notifyDataSetChanged();
			}
		}

		// lvHistory.setOnItemClickListener(new OnItemClickListener()
		// {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		// {
		//
		// int opid = Integer.parseInt(historyList.get(position).getOpid());
		// Bundle bundle = new Bundle();
		// bundle.putInt("opid", opid);
		// openActivity(FundHistoryDetails.class, bundle);
		//
		// }
		// });

	}

	private class HistoryAdapter extends BaseHistoryAdapter
	{

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			if (convertView == null)
			{
				convertView = View.inflate(mApplication, R.layout.item_fund_history, null);
			}

			TextView opt = ViewHolder.get(convertView, R.id.tv_operation);
			TextView bank = ViewHolder.get(convertView, R.id.tv_fund_bank);
			TextView cardNum = ViewHolder.get(convertView, R.id.tv_last_num);
			TextView optState = ViewHolder.get(convertView, R.id.tv_opt_state);
			TextView optDate = ViewHolder.get(convertView, R.id.tv_history_date);
			TextView optValue = ViewHolder.get(convertView, R.id.tv_history_value);

			opt.setText(getOptType(Integer.parseInt(historyList.get(position).getOperate())));
			bank.setText(historyList.get(position).getBank());

			String cardStr = historyList.get(position).getCard();
			int length = cardStr.length();

			cardNum.setText(cardStr.substring(length - 4, length));
			optState.setText(getOptState(Integer.parseInt(historyList.get(position).getState())));
			optDate.setText(DateFormatHelper.formatDate(historyList.get(position).getOpDate().concat("000"),
					DateFormat.DATE_5));
			optValue.setText("¥" + historyList.get(position).getSum());

			return convertView;
		}

	}

	@Override
	protected void stopPullLoad()
	{
	}

	private void requestFundDetail()
	{
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("fund_code", fdcode);
		AnsynHttpRequest.requestByPost(this, UrlConst.URL_FUND_INFO, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				dismissLoading();
				;
				if (data != null)
				{
					handler.sendMessage(handler.obtainMessage(0x02, data));
				}
				else
				{
					showHintDialog(getString(R.string.net_prompt));
				}
			}
		}, hashTable);
	}

	private void handleFundDetail(String obj)
	{
		JSONObject jsonObj;
		try
		{
			jsonObj = new JSONObject(obj);
			int ecode = jsonObj.optInt("ecode");
			String emsg = jsonObj.optString("emsg");
			if (ecode == 0)
			{
				JSONObject dataObj = jsonObj.optJSONObject("data");
				fee = dataObj.optString("fee");
				Intent intent = new Intent(FundMineHoldingDetails.this, FundBuyActivity.class);
				intent.putExtra("min_script", minAddition);
				intent.putExtra("fdcode", fdcode);
				intent.putExtra("fdname", fdname);
				intent.putExtra("isShuHui", false);
				intent.putExtra("fdtype", type);
				intent.putExtra("fee", fee);
				startActivityForResult(intent, Const.FUND_BUY_FLOW);
			}
			else
			{
				showHintDialog(emsg);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

	}
}
