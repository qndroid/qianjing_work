package com.qianjing.finance.ui.activity.assemble.asset;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.virtual.SchemaAssetsBean;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.virtual.VirtualBuyDetails;
import com.qianjing.finance.ui.activity.virtual.VirtualSchemaDetails;
import com.qianjing.finance.widget.adapter.virtual.VirtualListAdapter;
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
* @description 虚拟资产列表页面
* @author majinxin
* @date 2015年9月7日
*/
public class VirtualAssetListActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private PullToRefreshListView mPullToListView;
	private ListView mListView;
	private VirtualListAdapter mAdapter;

	/**
	 * data
	 */
	private boolean loadingFlag = true;
	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageIndex = 0;
	private int offset = 0;
	private int pageSize = 20;
	private ArrayList<SchemaAssetsBean> schemaList;
	private String mUsableAsset;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asset_list);
		initData();
		initView();

		requestFundAssetList(true);
	}

	private void initData()
	{
		Intent intent = getIntent();
		mUsableAsset = intent.getStringExtra("usable_asset");
	}

	private void initView()
	{
		setTitleWithId(R.string.virtual_asset_title);
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
		AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_ASSETS, new HttpCallBack()
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
				JSONObject data = object.optJSONObject("data");
				JSONArray jsonScheme = data.optJSONArray("schemas-assetses");
				schemaList = new ArrayList<SchemaAssetsBean>();
				if (jsonScheme != null && jsonScheme.length() > 0)
				{
					for (int i = 0; i < jsonScheme.length(); i++)
					{
						JSONObject schemaObj = jsonScheme.getJSONObject(i).optJSONObject("schema");
						JSONObject assetsObj = jsonScheme.getJSONObject(i).optJSONObject("assets");

						SchemaAssetsBean sabean = new SchemaAssetsBean();
						sabean.assets.assets = (float) assetsObj.optDouble("assets");
						sabean.assets.income = (float) assetsObj.optDouble("income");
						sabean.assets.invest = (float) assetsObj.optDouble("invest");
						sabean.assets.moditm = (float) assetsObj.optDouble("moditm");
						sabean.assets.profit = (float) assetsObj.optDouble("profit");
						sabean.assets.profitYesday = (float) assetsObj.optDouble("profitYesday");
						sabean.assets.redemping = (float) assetsObj.optDouble("redemping");
						sabean.assets.unpaid = (float) assetsObj.optDouble("unpaid");
						sabean.assets.subscripting = (float) assetsObj.optDouble("subscripting");
						sabean.assets.sid = assetsObj.optString("sid");
						sabean.assets.uid = assetsObj.optString("uid");
						sabean.schema.createT = (float) schemaObj.optDouble("createT");
						sabean.schema.pension_init = (float) schemaObj.optDouble("pension_init");
						sabean.schema.pension_month = (float) schemaObj.optDouble("pension_month");
						sabean.schema.pension_retire = (float) schemaObj.optDouble("pension_retire");
						sabean.schema.pension_age = (float) schemaObj.optDouble("pension_age");
						sabean.schema.small_init = (float) schemaObj.optDouble("small_init");
						sabean.schema.small_month = (float) schemaObj.optDouble("small_month");
						sabean.schema.small_nmonth = (float) schemaObj.optDouble("small_nmonth");
						sabean.schema.house_init = (float) schemaObj.optDouble("house_init");
						sabean.schema.house_money = (float) schemaObj.optDouble("house_money");
						sabean.schema.house_year = (float) schemaObj.optDouble("house_year");
						sabean.schema.financial_init = (float) schemaObj.optDouble("financial_init");
						sabean.schema.financial_rate = (float) schemaObj.optDouble("financial_rate");
						sabean.schema.financial_risk = (float) schemaObj.optDouble("financial_risk");
						sabean.schema.married_init = (float) schemaObj.optDouble("married_init");
						sabean.schema.married_money = (float) schemaObj.optDouble("married_money");
						sabean.schema.married_year = (float) schemaObj.optDouble("married_year");
						sabean.schema.education_money = (float) schemaObj.optDouble("education_money");
						sabean.schema.education_year = (float) schemaObj.optDouble("education_year");
						sabean.schema.sid = (float) schemaObj.optDouble("sid");
						sabean.schema.state = (float) schemaObj.optDouble("state");
						sabean.schema.updateT = (float) schemaObj.optDouble("updateT");
						sabean.schema.name = schemaObj.optString("name");
						sabean.schema.type = schemaObj.optString("type");
						sabean.schema.uid = schemaObj.optString("uid");
						schemaList.add(sabean);
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
			mAdapter = new VirtualListAdapter(this, schemaList);
			mListView.setAdapter(mAdapter);
		}
		else
		{
			if (isLoadMore)
			{
				mAdapter.addData(schemaList);
			}

			if (isRefresh)
			{
				mAdapter.updateData(schemaList);
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
		SchemaAssetsBean detail = (SchemaAssetsBean) mAdapter.getItem(position);
		Intent intent;
		if (detail.assets == null)
		{
			intent = new Intent(this, VirtualBuyDetails.class);
		}
		else
		{
			intent = new Intent(this, VirtualSchemaDetails.class);
		}
		intent.putExtra("sid", String.valueOf(detail.schema.sid));
		intent.putExtra("useableAssets", Float.valueOf(mUsableAsset));
		startActivity(intent);
	}
}