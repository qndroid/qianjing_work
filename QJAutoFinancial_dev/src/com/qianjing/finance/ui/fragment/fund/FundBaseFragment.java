package com.qianjing.finance.ui.fragment.fund;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.adapter.fundtype.FundListAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 基金列表父 Fragment
 * @author majinxin
 * @date 2015年8月20日
 */
public abstract class FundBaseFragment extends BaseFragment implements OnItemClickListener
{
	/**
	 * UI
	 */
	protected PullToRefreshListView pullToListView;
	protected ListView searchListView;
	protected FundListAdapter searchAdapter;
	/**
	 * 可以将整个 view缓存起来
	 */
	protected View convertView;
	protected Activity mContext;
	/**
	 * Data
	 */
	protected int pageIndex = 0;
	protected int offset = 0;
	protected int pageSize = 20;
	protected Hashtable<String, Object> params = new Hashtable<String, Object>();
	protected ArrayList<JSONObject> fundLists;
	protected int type = 0;
	protected boolean loadingFlag = true;

	/**
	 * 是否加载更多,是否刷新
	 */
	protected boolean isLoadMore = false;
	protected boolean isRefresh = false;

	protected abstract int getType();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		if (loadingFlag)
		{
			requestFundList(getType());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		/**
		 * 把 view,缓存起来，防止每次重新从xml中读取
		 */
		if (convertView == null)
		{
			convertView = (View) inflater.inflate(R.layout.activity_fund_style_layout, null);
			initView();
		}
		ViewGroup parent = (ViewGroup) convertView.getParent();
		if (parent != null)
		{
			parent.removeView(convertView);
		}
		return convertView;
	}

	private void initView()
	{
		pullToListView = (PullToRefreshListView) convertView.findViewById(R.id.fund_search_list);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		// 只启动下拉加载更多
		pullToListView.setMode(Mode.BOTH);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		searchListView = pullToListView.getRefreshableView();
		searchListView.setOnItemClickListener(this);
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
				onPullUpToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	/**
	 * 下拉刷新逻辑处理
	 * 
	 * @param refreshView
	 */
	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{

	}

	protected void onPullDownRefreshView(PullToRefreshBase<?> refreshView)
	{

	}

	/**
	 * 刷新完成逻辑处理
	 */
	protected void refreshComplete()
	{
		if (pullToListView != null && pullToListView.isRefreshing())
		{
			pullToListView.onRefreshComplete();
		}
	}

	/**
	 * 发送获取基金列表请求
	 */
	protected void requestFundList(int type)
	{
		if (isLoadMore || isRefresh)
		{
		}
		else
		{
			if (getType() == 0 && loadingFlag)
			{
				showLoading();
			}
		}
		params.put("page_number", pageSize);
		params.put("offset", offset);
		params.put("type", type);
		AnsynHttpRequest.requestByPost(mContext, UrlConst.NEW_FUND_LIST, fundTypeCallback, params);
	}

	/**
	 * 请求处理回调接口
	 */
	private HttpCallBack fundTypeCallback = new HttpCallBack()
	{
		@Override
		public void back(String data, int status)
		{
			dismissLoading();
			if (data != null)
			{
				mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
			}
		}
	};

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{
			case 0x01:
				handleFundList(msg.obj.toString());
				break;
			}
		};
	};

	private void handleFundList(String data)
	{
		loadingFlag = false;
		ResponseBase responseBase = ResponseManager.getInstance().parse(data);
		refreshComplete();
		if (responseBase != null)
		{
			if (responseBase.ecode == ErrorConst.EC_OK || StringHelper.isNotBlank(responseBase.strError))
			{
				JSONObject objData = responseBase.jsonObject;
				try
				{
					JSONArray fundList = objData.getJSONArray("funds");
					if (fundList != null && fundList.length() > 0)
					{
						fundLists = new ArrayList<JSONObject>();
						for (int i = 0; i < fundList.length(); i++)
						{
							JSONObject tempFund = (JSONObject) fundList.get(i);
							fundLists.add(tempFund);
						}

						if (searchAdapter == null)
						{
							searchAdapter = new FundListAdapter(mContext, fundLists);
							searchListView.setAdapter(searchAdapter);
						}
						else
						{
							if (isLoadMore)
							{
								searchAdapter.addData(fundLists);
							}

							if (isRefresh)
							{
								searchAdapter.refreshAllData(fundLists);
							}
						}
					}
					initBackStatus();

				}
				catch (Exception e)
				{
					initBackStatus();
					e.printStackTrace();
					showHintDialog("数据解析错误");
				}
			}
			else
			{
				initBackStatus();
				showHintDialog("网络不给力");
				return;
			}

		}
		else
		{
			initBackStatus();
			showHintDialog("网络不给力");
			return;
		}

	}

	private void initBackStatus()
	{
		isLoadMore = false;
		isRefresh = false;
	}

	/**
	 * 列表Item点击事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		JSONObject fund = (JSONObject) searchAdapter.getItem(position);
		Intent intent = new Intent(mContext, FundDetailsActivity.class);
		intent.putExtra("fdcode", fund.optString("fdcode"));
		startActivity(intent);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
