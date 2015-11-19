package com.qianjing.finance.ui.activity.assemble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.history.HistoryDetailsInter;
import com.qianjing.finance.model.history.HistoryInter;
import com.qianjing.finance.model.history.HistoryListBean;
import com.qianjing.finance.model.history.Schemaoplogs;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.widget.adapter.TradeListAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

/**
 * @Description:history
 * @author liuchen
 */

public class AssembleTradeListActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{

	private PullToRefreshListView ptrflv;
	private static final int HISTORYLIST = 1;
	private static final int HISTORYDETAILS = 2;
	private boolean PULL_TO_REFRESH = false;
	/**
	 *	paging
	 * */
	private int startOf = 0;
	private static int COUNT = 0;

	private HistoryListBean listBean;
	private List<Schemaoplogs> infoList = new ArrayList<Schemaoplogs>();
	private List<HashMap<String, String>> historyDataList = new ArrayList<HashMap<String, String>>();
	private int[] stateStr =
	{ R.string.submit, R.string.purchasing, R.string.redeeming, R.string.success, R.string.fail, R.string.cancel_order };

	private int[] optStr =
	{ R.string.purchase, R.string.redeem };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myasset_trade_list);
		setTitleWithId(R.string.bill);
		setTitleBack();

		Intent intent = getIntent();
		historyInter = (HistoryInter) intent.getSerializableExtra("historyInter");

		showLoading();
		requestHistoryList(0, CustomConstants.HISTORY_PAGE_NUMBER);
		initView();
	}

	private void initView()
	{

		ptrflv = (PullToRefreshListView) findViewById(R.id.ptrflv_list);
		ptrflv.setMode(Mode.BOTH);
		ptrflv.setShowIndicator(false);
		ptrflv.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView)
			{
				/**
				 * pull down to refresh
				 * 
				 * */
				PULL_TO_REFRESH = true;
				requestHistoryList(0, CustomConstants.HISTORY_PAGE_NUMBER * COUNT);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView)
			{
				/**
				 * pull up to load
				 * */
				PULL_TO_REFRESH = false;
				startOf = startOf + CustomConstants.HISTORY_PAGE_NUMBER;
				requestHistoryList(startOf, CustomConstants.HISTORY_PAGE_NUMBER);
			}
		});
		refreshableView = ptrflv.getRefreshableView();
		refreshableView.setOnItemClickListener(this);

		defPage = (FrameLayout) findViewById(R.id.in_default_page);
		defaultPic = (ImageView) findViewById(R.id.iv_deault_page_pic);
		defaultTxt = (TextView) findViewById(R.id.tv_deault_page_text);

		ptrflv.setVisibility(View.VISIBLE);
		defPage.setVisibility(View.GONE);
		defaultPic.setBackgroundResource(R.drawable.img_history_empty);
		defaultTxt.setText(R.string.empty_group_history);
	}

	@Override
	public void onClick(View v)
	{

	}

	/**
	 * ask history data
	 * */
	public void requestHistoryList(int startOf, int pageItem)
	{

		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		if (!"".equals(historyInter.getSid()))
		{
			hashTable.put("sid", historyInter.getSid());
		}

		hashTable.put("nm", pageItem);
		hashTable.put("of", startOf);
		AnsynHttpRequest.requestByPost(AssembleTradeListActivity.this, historyInter.getInterfaceName(),
				new HttpCallBack()
				{

					@Override
					public void back(String data, int status)
					{
						Message msg = Message.obtain();
						msg.obj = data;
						msg.what = HISTORYLIST;
						handler.sendMessage(msg);
					}
				}, hashTable);
	}

	Handler handler = new Handler()
	{

		public void handleMessage(Message msg)
		{
			String jsonStr = (String) msg.obj;
			switch (msg.what)
			{
			case HISTORYLIST:
				WriteLog.recordLog(jsonStr);
				LogUtils.syso("组合历史记录数据:"+jsonStr);
				analysisHistoryData(jsonStr);
				break;
			}
		};
	};
	private HashMap<String, String> map;
	private TradeListAdapter tradeListAdapter;
	private ListView refreshableView;
	private HistoryInter historyInter;
	private FrameLayout defPage;
	private ImageView defaultPic;
	private TextView defaultTxt;

	/**
	 * setAdapter and refresh list
	 * */
	public void initAdapter()
	{

		ptrflv.onRefreshComplete();
		if (historyDataList != null)
		{

			if (tradeListAdapter == null)
			{
				tradeListAdapter = new TradeListAdapter(AssembleTradeListActivity.this, historyDataList);
				ptrflv.setAdapter(tradeListAdapter);
			}
			else
			{
				tradeListAdapter.notifyDataSetChanged();
			}

			if (historyDataList.size() == 0)
			{
				ptrflv.setVisibility(View.GONE);
				defPage.setVisibility(View.VISIBLE);
			}
		}

	}

	protected void analysisHistoryData(String jsonStr)
	{

		if (jsonStr == null || "".equals(jsonStr))
		{
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try
		{
			JSONObject jsonObj = new JSONObject(jsonStr);
			int ecode = jsonObj.optInt("ecode");
			String emsg = jsonObj.optString("emsg");
			String data = jsonObj.optString("data");
			if (ecode == 0)
			{

				if (PULL_TO_REFRESH)
				{
					historyDataList.clear();
					infoList.clear();
				}
				else
				{
					COUNT++;
				}

				// listBean = new Gson().fromJson(data, HistoryListBean.class);
				JSONObject dataObj = new JSONObject(data);
				JSONArray schemaoplogsArray = dataObj.optJSONArray("schemaoplogs");
				listBean = new HistoryListBean();
				for (int i = 0; i < schemaoplogsArray.length(); i++)
				{

					JSONObject schemaLogData = schemaoplogsArray.getJSONObject(i);
					Schemaoplogs schemalogInfo = new Schemaoplogs();

					schemalogInfo.bid = schemaLogData.optInt("bid");
					schemalogInfo.initSchedule = schemaLogData.optInt("initSchedule");
					schemalogInfo.operate = schemaLogData.optInt("operate");
                    schemalogInfo.sid = schemaLogData.optString("sid");
                    schemalogInfo.sopid = schemaLogData.optString("sopid");
					schemalogInfo.state = schemaLogData.optInt("state");

					schemalogInfo.opDate = schemaLogData.optString("opDate");
					schemalogInfo.fee = schemaLogData.optString("fee");
					schemalogInfo.sname = schemaLogData.optString("sname");
					schemalogInfo.uid = schemaLogData.optString("uid");

					schemalogInfo.remain = (float) schemaLogData.optDouble("remain");
					schemalogInfo.sum = (float) schemaLogData.optDouble("sum");

					JSONArray abbrevs = schemaLogData.optJSONArray("abbrevs");
					JSONArray fdcodes = schemaLogData.optJSONArray("fdcodes");
					JSONArray fdshares = schemaLogData.optJSONArray("fdshares");
					JSONArray fdstates = schemaLogData.optJSONArray("fdstates");
					JSONArray fdsums = schemaLogData.optJSONArray("fdsums");
					JSONArray reasons = schemaLogData.optJSONArray("reasons");

					for (int j = 0; j < abbrevs.length(); j++)
					{
						schemalogInfo.abbrev.add(abbrevs.getString(j));
					}
					for (int j = 0; j < fdcodes.length(); j++)
					{
						schemalogInfo.fdcode.add(fdcodes.getString(j));
					}
					for (int j = 0; j < fdshares.length(); j++)
					{
						schemalogInfo.fdshare.add((float) fdshares.getDouble(j));
					}
					for (int j = 0; j < fdstates.length(); j++)
					{
						schemalogInfo.fdstate.add(fdstates.getInt(j));
					}
					for (int j = 0; j < fdsums.length(); j++)
					{
						schemalogInfo.fdsum.add((float) fdsums.getDouble(j));
					}
					for (int j = 0; j < reasons.length(); j++)
					{
						schemalogInfo.reason.add(reasons.getString(j));
					}

					listBean.schemaoplogs.add(schemalogInfo);
				}

				if (listBean.schemaoplogs.size() < CustomConstants.HISTORY_PAGE_NUMBER)
				{
					// end paging
					ptrflv.setMode(Mode.PULL_FROM_START);
				}

				for (int i = 0; i < listBean.schemaoplogs.size(); i++)
				{
					map = new HashMap<String, String>();
					//
					// LogUtils.i(AssembleTradeListActivity.this,
					// "获取到的字符串"+getStateStr(listBean.schemaoplogs.get(i).fdstates));
					// LogUtils.i(AssembleTradeListActivity.this,
					// "获取到的字符串2"+getString(stateStr[listBean.schemaoplogs.get(i).state]));
					//

					String stateS = getStateStr(listBean.schemaoplogs.get(i).fdstate) == "" ? getString(stateStr[listBean.schemaoplogs
							.get(i).state]) : getStateStr(listBean.schemaoplogs.get(i).fdstate);
					
					
					map.put("operate", listBean.schemaoplogs.get(i).sname);
					map.put("date", DateFormatHelper.formatDate(
							listBean.schemaoplogs.get(i).opDate.concat("000"), DateFormat.DATE_5));
					map.put("state", getString(optStr[listBean.schemaoplogs.get(i).operate - 1]) + "(" + stateS + ")");
					map.put("value", listBean.schemaoplogs.get(i).sum + "");

					historyDataList.add(map);
				}

				infoList.addAll(listBean.schemaoplogs);

				initAdapter();
				dismissLoading();
			}
			else
			{
				dismissLoading();
				showHintDialog(emsg);
			}

		}
		catch (JSONException e)
		{
			dismissLoading();
			e.printStackTrace();
		}
	}

	public String getStateStr(ArrayList<Integer> fdstates)
	{

		boolean success = false;
		boolean fail = false;

		for (Integer integer : fdstates)
		{
			if (integer == 3)
			{
				success = true;
			}
			else if (integer == 4)
			{
				fail = true;
			}
		}

		if (success && fail)
		{
			return "部分成功";
		}

		return "";

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{

		// Toast.makeText(getApplicationContext(), "列表的长度为:"+historyDataList.size(), 1).show();

		HistoryDetailsInter detailsInfo = new HistoryDetailsInter();
		Intent intent = new Intent(AssembleTradeListActivity.this, AssembleTradeDetailActivity.class);

		ArrayList<String> abbrevs = infoList.get(position).abbrev;
		ArrayList<Float> fdshares = infoList.get(position).fdshare;
		ArrayList<Integer> fdstates = infoList.get(position).fdstate;
		ArrayList<Float> fdsums = infoList.get(position).fdsum;

//		Toast.makeText(this, "历史记录的时间戳2:"+infoList.get(position).opDate, Toast.LENGTH_LONG).show();
		detailsInfo.dateStr = DateFormatHelper.formatDate(infoList.get(position).opDate.concat("000"),
				DateFormat.DATE_4);
		detailsInfo.operation = getString(optStr[infoList.get(position).operate - 1]);
		detailsInfo.optState = getStateStr(fdstates) == "" ? getString(stateStr[infoList.get(position).state])
				: getStateStr(fdstates);

		List<HashMap<String, Serializable>> dataList = new ArrayList<HashMap<String, Serializable>>();

		for (int i = 0; i < abbrevs.size(); i++)
		{
			HashMap<String, Serializable> sparseArray = new HashMap<String, Serializable>();
			sparseArray.put("abbrevs", abbrevs.get(i));
			/**
			 * 投资和赎回时份额的字段不一样
			 * */
			if (infoList.get(position).operate == 1)
			{
				sparseArray.put("fdshares", fdsums.get(i));
			}
			else
			{
				sparseArray.put("fdshares", fdshares.get(i));
			}
			sparseArray.put("fdstates", getString(stateStr[fdstates.get(i)]));
			dataList.add(sparseArray);
		}

		detailsInfo.historyDetails = dataList;
		detailsInfo.reasons = infoList.get(position).reason;

		intent.putExtra("detailInfo", detailsInfo);

		startActivity(intent);

	}
}
