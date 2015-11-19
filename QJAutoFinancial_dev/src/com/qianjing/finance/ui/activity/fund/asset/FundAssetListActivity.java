package com.qianjing.finance.ui.activity.fund.asset;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.fund.MyFundHoldingDetails;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.widget.adapter.fundtype.FundHoleListAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 基金资产列表页面
 * @author majinxin
 * @date 2015年9月2日
 */
public class FundAssetListActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private PullToRefreshListView mPullToListView;
	private ListView mListView;
	private FundHoleListAdapter mAdapter;

	/**
	 * data
	 */
	private boolean loadingFlag = true;
	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageIndex = 0;
	private int offset = 0;
	private int pageSize = 20;
	private ArrayList<MyFundHoldingDetails> assetsList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asset_list);
		initView();

		requestFundAssetList(true);
	}

	private void initView()
	{
		setTitleWithId(R.string.fund_profit_detail);
		setLoadingUncancelable();
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);

		mPullToListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh);
		mPullToListView.setMode(Mode.BOTH);
		mPullToListView.setShowIndicator(false);
		mPullToListView.setOnRefreshListener(initListRefreshListener());
		mListView = mPullToListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
	}

	private OnRefreshListener2<ListView> initListRefreshListener()
	{
		OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullDownRefreshView(refreshView);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullUpRefreshView(refreshView);
			}

		};
		return refreshListener;
	}

	/**
	 * 下拉刷新
	 */
	private void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView)
	{
		isLoadMore = false;
		isRefresh = true;
		pageIndex = 0;
		offset = 0;
		requestFundAssetList(false);
	}

	/**
	 * 上拉加载
	 */
	private void onPullUpRefreshView(PullToRefreshBase<ListView> refreshView)
	{
		isLoadMore = true;
		isRefresh = false;
		if (loadingFlag)
		{
			pageIndex = 0;
		}
		else
		{
			pageIndex += 1;
		}
		offset = (pageIndex) * pageSize;
		requestFundAssetList(false);
	}

	/**
	 * 刷新完成逻辑处理
	 */
	protected void refreshComplete()
	{
		if (mPullToListView != null && mPullToListView.isRefreshing())
		{
			mPullToListView.onRefreshComplete();
		}
	}

	private void requestFundAssetList(boolean isShow)
	{
		if (isShow)
		{
			showLoading();
		}

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("page_number", pageSize);
		params.put("offset", offset);
		AnsynHttpRequest.requestByPost(this, UrlConst.URL_FUND_MINE_HOLD, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
				}
				else
				{
					dismissLoading();
					showHintDialog("网络不给力，请重试!");
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
				handleDetail(msg.obj.toString());
				break;
			}
		};
	};

	private void handleDetail(String string)
	{
		dismissLoading();
		refreshComplete();
		loadingFlag = false;
		try
		{
			JSONObject object = new JSONObject(string);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject result = object.optJSONObject("data");
				JSONArray array = result.optJSONArray("fund_assets");
				if (array != null & array.length() > 0)
				{
					assetsList = new ArrayList<MyFundHoldingDetails>();
					for (int i = 0; i < array.length(); i++)
					{
						MyFundHoldingDetails fundInfo = new MyFundHoldingDetails();
						JSONObject jObj = (JSONObject) array.get(i);
						fundInfo.setAbbrev(jObj.optString("abbrev"));
						fundInfo.setFdcode(jObj.optString("fdcode"));
						fundInfo.setIncome(jObj.optString("income"));
						fundInfo.setInvest(jObj.optString("invest"));
						fundInfo.setNav(jObj.optString("nav"));
						fundInfo.setRedemping(jObj.optString("redemping"));
						fundInfo.setShares(jObj.optString("shares"));
						fundInfo.setSubscripting(jObj.optString("subscripting"));
						fundInfo.setUid(jObj.optString("uid"));
						fundInfo.setUnpaid(jObj.optString("unpaid"));
						fundInfo.setMarket_value(jObj.optString("market_value"));
						fundInfo.setProfit(jObj.optString("profit"));
						fundInfo.setProfit10K(jObj.optString("profit10K"));
						fundInfo.setType(jObj.optString("type"));
						fundInfo.setMin_addition(jObj.optString("min_addition"));
						fundInfo.setMin_hold(jObj.optString("min_hold"));
						fundInfo.setMin_redemp(jObj.optString("min_redemp"));
						fundInfo.setMin_subscript(jObj.optString("min_subscript"));

						assetsList.add(fundInfo);
					}
					updateUI();
				}
				initBackStatus();
				break;
			default:
				initBackStatus();
				showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{
			initBackStatus();
			e.printStackTrace();
		}
	}

	private void updateUI()
	{
		if (mAdapter == null)
		{
			mAdapter = new FundHoleListAdapter(this, assetsList);
			mListView.setAdapter(mAdapter);
		}
		else
		{
			if (isLoadMore)
			{
				mAdapter.addData(assetsList);
			}

			if (isRefresh)
			{
				mAdapter.updateData(assetsList);
			}
		}
	}

	private void initBackStatus()
	{
		isLoadMore = false;
		isRefresh = false;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MyFundHoldingDetails fundDtails = (MyFundHoldingDetails) mAdapter.getItem(position);
		Intent intent = new Intent(this, FundMineHoldingDetails.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fundDtails", fundDtails);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}