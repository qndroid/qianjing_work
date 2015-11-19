package com.qianjing.finance.ui.activity.profit.fragment;

import com.qianjing.finance.model.history.ProfitBean;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.profit.adapter.ProfitAdapter;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 盈亏列表父 Fragment
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public abstract class ProfitBaseFragment extends BaseFragment {
	/**
	 * UI
	 */
	protected Context mContext;
	protected View mContentView;
	protected View mHeaderView;
	protected ImageView iconImageView;
	protected TextView profitView;
	protected ListView currentListView;
	protected ProfitAdapter adapter;
	protected PullToRefreshListView pullToListView;
	protected View steadyProfitEnter;
	protected View defaultPage;
	/**
	 * Data
	 */
	protected int currentPageSize;
	protected int pageIndex = 0;
	protected int offset = 0;
	protected int pageSize = 30;
	protected Hashtable<String, Object> params = new Hashtable<String, Object>();
	protected ArrayList<ProfitBean> profitLists;
	protected int type;
	protected String totalProfit;
	/**
	 * 是否加载更多,是否刷新
	 */
	protected boolean isLoadMore = false;
	protected boolean isRefresh = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public View onCreateView(LayoutInflater inflater,
			android.view.ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_profit_assemable,
				null);
		initView();
		return mContentView;
	}

	protected void initView() {
		pullToListView = (PullToRefreshListView) mContentView
				.findViewById(R.id.pull_to_refresh);
		pullToListView.setMode(Mode.BOTH);
		pullToListView.setShowIndicator(false);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		currentListView = pullToListView.getRefreshableView();
		
		defaultPage = mContentView.findViewById(R.id.in_default_page);
        steadyProfitEnter = mContentView.findViewById(R.id.in_history_enter);
        TextView defaultText = (TextView) mContentView.findViewById(R.id.tv_deault_page_text);
        TextView enterText = (TextView) mContentView.findViewById(R.id.tv_enter_txt);
        defaultText.setText(getTypeStr());
        enterText.setText(getResources().getString(R.string.steady_profit_enter_txt));

		initHeader();
	}
	
	protected abstract String getTypeStr();
	
	protected void initHeader(){
	    mHeaderView = LayoutInflater.from(mContext).inflate(
                R.layout.profit_header_layout, currentListView, false);
        iconImageView = (ImageView) mHeaderView
                .findViewById(R.id.icon_flag_view);
        profitView = (TextView) mHeaderView.findViewById(R.id.ying_kui_text);
        currentListView.addHeaderView(mHeaderView);
    }

	private OnRefreshListener2<ListView> initListRefreshListener() {
		OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				onPullDownRefreshView(refreshView);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				onPullUpToRefreshView(refreshView);
			}

		};
		return refreshListener;
	}

	/**
	 * 上拉加载更多逻辑处理
	 */
	protected void onPullUpToRefreshView(PullToRefreshBase<ListView> refreshView) {

	}

	/**
	 * 下拉刷新逻辑处理
	 */
	protected void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView) {

	}

	/**
	 * 刷新完成逻辑处理
	 */
	protected void refreshComplete() {
		if (pullToListView != null && pullToListView.isRefreshing()) {
			pullToListView.onRefreshComplete();
		}
	}
	
	/**
	 * 发送获取基金列表请求
	 */
	protected void requestProfitList(String url) {
		if (isLoadMore || isRefresh) {
		} else {
			showLoading();
		}
		params.put("nm", pageSize);
		params.put("of", offset);
		AnsynHttpRequest.requestByPost(mContext, url, new HttpCallBack() {
			@Override
			public void back(String data, int status) {
				dismissLoading();
				if (data != null) {
					mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
				} else {
					mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
				}
			}
		}, params);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0x01 :
					handleProfitData(msg.obj.toString());
					break;
				case 0x02 :
					handleError();
					break;
			}
		};
	};
    

	protected void initBackStatus() {
		isLoadMore = false;
		isRefresh = false;
	}

	protected abstract void handleProfitData(String data);

	protected void handleError() {
		if (pullToListView != null) {
			refreshComplete();
		}
	}
}
