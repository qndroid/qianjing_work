package com.qianjing.finance.ui.fragment.fund;

import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
* @description QDII基金界面
* @author majinxin
* @date 2015年8月20日
*/ 
public class FundQDIIFragment extends FundBaseFragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// requestFundList(getType());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View contentView = inflater.inflate(R.layout.fund_no_data_layout, null);
		return contentView;
	}

	/**
	 * 下拉刷新逻辑处理
	 * @param refreshView
	 */
	@Override
	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		isRefresh = false;
		pageIndex += 1;
		offset = (pageIndex) * pageSize;
		requestFundList(getType());
	}

	@Override
	protected void onPullDownRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = false;
		isRefresh = true;
		pageIndex = 0;
		offset = 0;
		requestFundList(getType());
	}

	/**
	 * 
	 * 刷新完成逻辑处理
	 */
	protected void refreshComplete()
	{
		if (pullToListView != null && pullToListView.isRefreshing())
		{
			pullToListView.onRefreshComplete();
		}
	}

	@Override
	protected int getType()
	{
		return 8;
	}
}
