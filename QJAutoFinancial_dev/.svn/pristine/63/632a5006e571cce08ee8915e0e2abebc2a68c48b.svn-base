package com.qianjing.finance.ui.activity.assemble.profit;

import com.qianjing.finance.model.assemble.AssembleDayProfit;
import com.qianjing.finance.model.assemble.AssembleDayProfitDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.widget.adapter.dayprofit.DayProfitAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 组合每日盈亏列表页
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AssembelDayProfitActivity extends BaseActivity implements OnItemClickListener
{
	/**
	 * UI
	 */
	private TextView mTitleView;
	private PullToRefreshListView mPullListView;
	private ListView mProfitListView;
	private DayProfitAdapter mAdapter;

	/**
	 * data
	 */
	private String sid; // 组合 ID
	private int pageNumber = 30;
	private int offset = 0;
	private int pageIndex = 0;
	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private ArrayList<AssembleDayProfit> mAssemleList;
	private ArrayList<AssembleDayProfitDetail> mAssemleDetailList;
	private int currentPageIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assemble_day_profit);
		initData();
		initView();
		requestProfitData();
	}

	private void initData()
	{
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
	}

	private void initView()
	{
		setTitleBack();
		mTitleView = (TextView) findViewById(R.id.title_middle_text);
		mTitleView.setText(getString(R.string.day_profit_title));

		mPullListView = (PullToRefreshListView) findViewById(R.id.profit_list_view);
		mPullListView.setOnRefreshListener(initListRefreshListener());
		mPullListView.setMode(Mode.BOTH);
		mPullListView.setShowIndicator(false);

		mProfitListView = mPullListView.getRefreshableView();
		mProfitListView.setOnItemClickListener(this);
	}

	private OnRefreshListener2<ListView> initListRefreshListener()
	{
		OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullUpToRefreshView(refreshView);
			}

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullDownToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	private void onPullDownToRefreshView(PullToRefreshBase<ListView> refreshView)
	{
		isLoadMore = false;
		isRefresh = true;
		currentPageIndex = pageIndex;
		pageIndex = 0;
		offset = 0;
		requestProfitData();
	}

	private void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		isRefresh = false;
		if (mAdapter != null)
		{
			pageIndex += 1;
		}
		offset = (pageIndex) * pageNumber;
		requestProfitData();
	}

	protected void refreshComplete()
	{
		if (mPullListView != null && mPullListView.isRefreshing())
		{
			mPullListView.onRefreshComplete();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		AssembleDayProfit dayProfit = (AssembleDayProfit) mAdapter.getItem(position);
		Intent intent = new Intent(this, AssembleDayProfitDetailActivity.class);
		intent.putExtra("dayProfit", dayProfit);
		startActivity(intent);
	}

	private void requestProfitData()
	{
		if (!isLoadMore && !isRefresh)
		{
			showLoading();
		}
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("sid", sid);
		params.put("nm", pageNumber);
		params.put("of", offset);
		AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_DAY_PROFIT, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				dismissLoading();
				if (data == null || data.equals(""))
				{
					mHandler.obtainMessage(0x02, data).sendToTarget();
				}
				else
				{
					mHandler.obtainMessage(0x01, data).sendToTarget();
				}
			}
		}, params);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0x01:
				handleProfitDetail(msg.obj.toString());
				break;
			case 0x02:
				refreshComplete();
				if (mAdapter != null)
				{
					if (isRefresh)
					{
						pageIndex = currentPageIndex;
					}
					else
					{
						pageIndex -= 1;
					}
				}
				isLoadMore = false;
				isRefresh = false;
				showHintDialog("网络不给力!");
				break;
			}
		};
	};

	private void handleProfitDetail(String strJson)
	{
		refreshComplete();
		try
		{
			JSONObject allObject = new JSONObject(strJson);
			int ecode = allObject.getInt("ecode");
			if (ecode == 0)
			{
				JSONArray dataObjectArray = allObject.optJSONObject("data").optJSONArray("list");
				if (dataObjectArray != null && dataObjectArray.length() > 0)
				{
					mAssemleList = new ArrayList<AssembleDayProfit>();
					for (int i = 0; i < dataObjectArray.length(); i++)
					{
						JSONObject obj = (JSONObject) dataObjectArray.get(i);
						AssembleDayProfit dayProfit = new AssembleDayProfit();
						dayProfit.setUid(obj.optString("uid"));
						dayProfit.setDt(obj.optLong("dt"));
						dayProfit.setDayProfit(obj.optString("day_profit"));
						dayProfit.setSid(obj.optString("sid"));

						JSONArray jsonDetail = obj.getJSONArray("fund_details");
						mAssemleDetailList = new ArrayList<AssembleDayProfitDetail>();
						for (int j = 0; j < jsonDetail.length(); j++)
						{
							JSONObject objDetail = (JSONObject) jsonDetail.get(j);
							AssembleDayProfitDetail profitDetail = new AssembleDayProfitDetail();
							profitDetail.setFundName(objDetail.optString("abbrev"));
							profitDetail.setFundDayProfit(objDetail.optDouble("day_profit"));
							profitDetail.setFundDayRate(objDetail.optString("day_rate"));
							mAssemleDetailList.add(profitDetail);
						}
						dayProfit.setProfitList(mAssemleDetailList);
						mAssemleList.add(dayProfit);
					}
					if (mAdapter == null)
					{
						mAdapter = new DayProfitAdapter(AssembelDayProfitActivity.this, mAssemleList);
						mProfitListView.setAdapter(mAdapter);
					}
					else
					{
						if (isLoadMore)
						{
							mAdapter.addNewData(mAssemleList);
						}
						if (isRefresh)
						{
							mAdapter.refreshAllData(mAssemleList);
						}
					}
				}
				else
				{
					/**
					 * 回退Index
					 */
					if (mAdapter != null)
					{
						if (isRefresh)
						{
							pageIndex = currentPageIndex;
						}
						else
						{
							pageIndex -= 1;
						}
					}
				}
			}
			else
			{
				if (mAdapter != null)
				{
					if (isRefresh)
					{
						pageIndex = currentPageIndex;
					}
					else
					{
						pageIndex -= 1;
					}
				}
			}
		}
		catch (JSONException e)
		{
			if (mAdapter != null)
			{
				if (isRefresh)
				{
					pageIndex = currentPageIndex;
				}
				else
				{
					pageIndex -= 1;
				}
			}
		}
		isLoadMore = false;
		isRefresh = false;
	}
}