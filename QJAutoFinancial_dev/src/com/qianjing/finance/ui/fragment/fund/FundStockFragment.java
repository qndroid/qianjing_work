package com.qianjing.finance.ui.fragment.fund;

import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;


/** 
* @description 股票型基金列表 Fragment
* @author majinxin
* @date 2015年8月20日
*/ 
public class FundStockFragment extends FundBaseFragment
{
	/**
	 * 下拉刷新逻辑处理
	 * 
	 * @param refreshView
	 */
	@Override
	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
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
		return 1;
	}
}
