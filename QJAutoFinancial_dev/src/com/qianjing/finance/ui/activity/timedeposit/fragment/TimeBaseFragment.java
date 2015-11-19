package com.qianjing.finance.ui.activity.timedeposit.fragment;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.timedespority.TimeAsset;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.widget.adapter.timedespority.TimeAssetAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

/** 
* @description 定存资产基类Fragment
* @author majinxin
* @date 2015年9月7日
*/
public abstract class TimeBaseFragment extends BaseFragment
{
	/**
	 * UI
	 */
	protected Context mContext;
	protected View mContentView;
	protected PullToRefreshListView mPullToListView;
	protected ListView mListView;
	protected TimeAssetAdapter mAdapter;

	/**
	 * data
	 */
	protected ArrayList<TimeAsset> mList;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState)
	{
		mContentView = inflater.inflate(R.layout.fragment_profit_assemable, null);
		initView();
		return mContentView;
	}

	protected void initView()
	{
		mPullToListView = (PullToRefreshListView) mContentView.findViewById(R.id.pull_to_refresh);
		mPullToListView.setMode(Mode.PULL_FROM_START);
		mPullToListView.setShowIndicator(false);
		mPullToListView.setOnRefreshListener(initListRefreshListener());
		mListView = mPullToListView.getRefreshableView();
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
			}

		};
		return refreshListener;
	}

	/**
	 * 下拉刷新逻辑处理
	 */
	protected void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView)
	{
		requestData(getType(), false);
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

	/**
	 * 发送获取基金列表请求
	 */
	protected void requestData(int type, boolean isShow)
	{
		if (isShow)
		{
			showLoading();
		}
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("mobile", UserManager.getInstance().getUser().getMobile());
		params.put("type", type);
		AnsynHttpRequest.requestByPost(mContext, UrlConst.TIME_DESPORITY_ASSET_DETAIL, new HttpCallBack()
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
				handleList(msg.obj.toString());
				break;
			}
		};
	};

	private void handleList(String data)
	{
		dismissLoading();
		refreshComplete();
		try
		{
			JSONObject object = new JSONObject(data);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONArray array = object.optJSONArray("data");
				if (array != null && array.length() > 0)
				{
					mList = new ArrayList<TimeAsset>();
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject obj = array.getJSONObject(i);
						TimeAsset asset = new TimeAsset();
						asset.dealID = obj.optString("deal_id");
						asset.load = obj.optString("load");
						asset.dealName = obj.optString("deal_name");
						asset.sumProfit = obj.optString("sumprofit");
						asset.hkTime = obj.optString("hkTime");

						mList.add(asset);
					}
					updateUI();
				}
				break;
			default:
				showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void updateUI()
	{
		if (mAdapter == null)
		{
			mAdapter = new TimeAssetAdapter(mContext, mList);
			mListView.setAdapter(mAdapter);
		}
		else
		{
			mAdapter.refreshAllData(mList);
		}
	}

	public abstract int getType();
}