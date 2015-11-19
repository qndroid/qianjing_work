package com.qianjing.finance.ui.activity.timedeposit;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.timedespority.TimeProfitData;
import com.qianjing.finance.model.timedespority.TimeProfitItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.widget.adapter.timedespority.TimeProfitAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/** 
* @description 定存盈亏列表页面
* @author majinxin
* @date 2015年9月2日
*/
public class TimeDepositProfitActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private PullToRefreshListView pullToListView;
	private ListView currentListView;
	private TimeProfitAdapter mAdapter;
	private View mHeaderView;
	private TextView mUpdateTimeView;

	/**
	 * data
	 */
	private TimeProfitData profitData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_desposit_profit);
		initView();

		requestProfitData(true);
	}

	private void initView()
	{
		setTitleWithId(R.string.time_disport_profit_title);
		setLoadingUncancelable();
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);

		pullToListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
		pullToListView.setMode(Mode.PULL_FROM_START);
		pullToListView.setShowIndicator(false);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		currentListView = pullToListView.getRefreshableView();

		mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_timedespoity_profit, currentListView, false);
		mUpdateTimeView = (TextView) mHeaderView.findViewById(R.id.text);
		currentListView.addHeaderView(mHeaderView);

		profitData = new TimeProfitData();
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

	// 刷新
	private void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView)
	{
		requestProfitData(false);
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

	private void requestProfitData(boolean isShow)
	{
		if (isShow)
		{
			showLoading();
		}
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("mobile", UserManager.getInstance().getUser().getMobile());

		AnsynHttpRequest.requestByPost(this, UrlConst.TIME_DESPORITY_PROFIT_HISTORY, new HttpCallBack()
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
		}, upload);
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
		try
		{
			JSONObject object = new JSONObject(string);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject result = object.optJSONObject("data");
				JSONArray array = result.optJSONArray("list");
				// 初始化对象模型
				profitData.updateTime = result.optString("up_time");
				if (array != null && array.length() > 0)
				{
					ArrayList<TimeProfitItem> list = new ArrayList<TimeProfitItem>();
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject obj = (JSONObject) array.get(i);
						TimeProfitItem item = new TimeProfitItem();
						item.dealId = obj.optString("deal_id");
						item.dealName = obj.optString("deal_name");
						item.sumProfit = obj.optString("sumprofit");
						item.dsProfit = obj.optString("dsProfit");
						item.repayTime = obj.optString("repay_time");
						item.sumLoad = obj.optString("sumLoad");

						list.add(item);
					}
					profitData.profitList = list;
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

	private void updateUI()
	{
		mUpdateTimeView.setText(getString(R.string.updatetime_title)
				+ DateFormatHelper.formatDate(profitData.updateTime.concat("000"), DateFormat.DATE_1)
				+ getString(R.string.updatetime_end));
		if (mAdapter == null)
		{
			mAdapter = new TimeProfitAdapter(this, profitData.profitList);
			currentListView.setAdapter(mAdapter);
		}
		else
		{
			mAdapter.refreshAllData(profitData.profitList);
		}
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
}