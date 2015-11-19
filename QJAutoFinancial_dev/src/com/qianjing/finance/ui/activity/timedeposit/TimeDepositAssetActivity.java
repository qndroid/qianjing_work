/**
 * Project Name:QJAutoFinancial_dev_final
 * File Name:TimeDepositAssetActivity.java
 * Package Name:com.qianjing.finance.ui.activity.timedeposit
 * Date:2015年9月2日上午9:37:04
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
 */

package com.qianjing.finance.ui.activity.timedeposit;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshScrollView;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * @description 定存宝资产页面
 * @author majinxin
 * @date 2015年9月2日
 */
public class TimeDepositAssetActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private TextView mKouKuanDetailView;
	private RelativeLayout mProfitInfoLayout;
	private TextView mProfitView;
	private TextView mWaitingProfitView;
	private RelativeLayout mTotalInfoLayout;
	private TextView mAllProfitView;
	private TextView mScanAssertView;
	private PullToRefreshScrollView pullScrollView;

	/**
	 * data
	 */
	private String mProperty; // 累计收益
	private String mWaitingProfit; // 等待收益
	private String mTotalAset; // 总资产

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_desposit_asset);
		initView();

		requestTimeDespoitData(true);
	}

	private void initView()
	{
		setTitleWithId(R.string.time_disport_title);
		setLoadingUncancelable();
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);

		mKouKuanDetailView = (TextView) findViewById(R.id.right_title_view);
		mKouKuanDetailView.setVisibility(View.VISIBLE);
		mKouKuanDetailView.setText(getString(R.string.title_fund_history));
		mKouKuanDetailView.setOnClickListener(this);

		mProfitInfoLayout = (RelativeLayout) findViewById(R.id.leiji_layout);
		mProfitView = (TextView) findViewById(R.id.leiji_value);
		mWaitingProfitView = (TextView) findViewById(R.id.dai_shouyi_value);
		mTotalInfoLayout = (RelativeLayout) findViewById(R.id.total_layout);
		mAllProfitView = (TextView) findViewById(R.id.all_assert_value);
		mScanAssertView = (TextView) findViewById(R.id.scan_assert_view);
		pullScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh_scrollview);
		pullScrollView.setOnRefreshListener(refreshListener);
		mProfitInfoLayout.setOnClickListener(this);
		mTotalInfoLayout.setOnClickListener(this);
		mScanAssertView.setOnClickListener(this);
	}

	private OnRefreshListener<ScrollView> refreshListener = new OnRefreshListener<ScrollView>()
	{
		@Override
		public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
		{
			requestTimeDespoitData(false);
		}
	};

	private void requestTimeDespoitData(boolean isShow)
	{
		if (isShow)
		{
			showLoading();
		}
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("mobile", UserManager.getInstance().getUser().getMobile());

		AnsynHttpRequest.requestByPost(this, UrlConst.TIME_DESPORITY_ASSET, new HttpCallBack()
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
		pullScrollView.onRefreshComplete();
		try
		{
			JSONObject object = new JSONObject(string);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject result = object.optJSONObject("data");
				mProperty = result.optString("property");
				mWaitingProfit = result.optString("dsProfit");
				mTotalAset = result.optString("totalAsset");
				updateUI();
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
		if (Double.parseDouble(mProperty) >= 0)
		{
			if (Double.parseDouble(mProperty) > 0)
			{
				mProfitView.setText(getSpannableString(
						getString(R.string.jia_hao) + StringHelper.formatString(mProperty, StringFormat.FORMATE_1), 1,
						2, 56, 28));
			}
			else
			{
				mProfitView.setText(getSpannableString(StringHelper.formatString(mProperty, StringFormat.FORMATE_1), 0,
						2, 56, 28));
			}
		}
		else
		{
			
			mProfitView.setText(getSpannableString(mProperty, 1, 2, 56, 28));
		}
		mWaitingProfitView.setText(getString(R.string.time_disport_waiting_profit)
				+ StringHelper.formatString(String.valueOf(mWaitingProfit), StringFormat.FORMATE_1));
		mAllProfitView.setText(getSpannableString(StringHelper.formatString(mTotalAset, StringFormat.FORMATE_1), 0, 2,
				45, 22));
	}

	private SpannableString getSpannableString(String source, int start, int end, int bigSize, int smallSize)
	{
		SpannableString result = new SpannableString(source);
		/**
		 * 前面的小字体
		 */
		if (start >= 0)
		{
			result.setSpan(new AbsoluteSizeSpan(smallSize, true), 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		// 中间大字体
		result.setSpan(new AbsoluteSizeSpan(bigSize, true), start, source.length() - end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		/**
		 * 结尾小字体
		 */
		result.setSpan(new AbsoluteSizeSpan(smallSize, true), source.length() - end, source.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return result;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.leiji_layout:
			// 去定存宝收益记录
			startActivity(new Intent(this, TimeDepositProfitActivity.class));
			break;
		case R.id.total_layout:
			// 去交易记录
			break;
		case R.id.scan_assert_view:
			startActivity(new Intent(this, TimeDepositDetailActivity.class));
			break;
		case R.id.bt_back:
			finish();
			break;
		case R.id.right_title_view:
			// 去交易记录
			break;
		}
	}
}