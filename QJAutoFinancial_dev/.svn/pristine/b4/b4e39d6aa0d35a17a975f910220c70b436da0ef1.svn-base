package com.qianjing.finance.ui.fragment.assets;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.asset.VirtualAssetListActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshScrollView;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
* @description 虚拟账户 Fragment
* @author majinxin
* @date 2015年9月11日
*/
public class VirtualAssetsFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener
{
	/**
	 * UI
	 */
	private BaseActivity mCurrentActivity;
	private View mContentView;
	private SwipeRefreshLayout mRefreshLayout;
	private PullToRefreshScrollView mPullScrollView;
	private TextView mYesterDayView;
	private TextView mYesterProfitView;
	private TextView mTotalProfitView;
	private TextView mVirtualTotalView;
	private TextView mBuyingView;
	private TextView mRedeemView;
	private TextView mMarketValueView;
	private TextView mVirtualMoneyView;
	private TextView mScanAssetView;

	/**
	 * data
	 */
	private String virtualAsset; // 总资产
	private String profitYesday;
	private String totalProfit;
	private String profitTime;
	private String redeempIng;
	private String buying;
	private String marketValue;
	private String usableValue;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mCurrentActivity = (BaseActivity) getActivity();
		requsetVirtualTotalAssets(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mContentView = inflater.inflate(R.layout.fragment_virtual_assets, null);
		initView();
		return mContentView;
	}

	private void initView()
	{
		mRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_layout);
		mRefreshLayout.setOnRefreshListener(this);
		mRefreshLayout.setColorSchemeResources(R.color.red_VI, R.color.red_VI, R.color.red_VI, R.color.red_VI);

		mYesterDayView = (TextView) mContentView.findViewById(R.id.yesterday_time_value);
		mYesterProfitView = (TextView) mContentView.findViewById(R.id.yesterday_profit_value);
		mTotalProfitView = (TextView) mContentView.findViewById(R.id.leiji_profit_view);
		mVirtualTotalView = (TextView) mContentView.findViewById(R.id.virtual_value_view);
		mBuyingView = (TextView) mContentView.findViewById(R.id.buying_view);
		mRedeemView = (TextView) mContentView.findViewById(R.id.redeming_view);
		mMarketValueView = (TextView) mContentView.findViewById(R.id.market_value_view);
		mVirtualMoneyView = (TextView) mContentView.findViewById(R.id.virtual_money_view);
		mScanAssetView = (TextView) mContentView.findViewById(R.id.scan_assert_view);

		mScanAssetView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.scan_assert_view:
			Intent intent = new Intent(mCurrentActivity, VirtualAssetListActivity.class);
			intent.putExtra("usable_asset", usableValue);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onRefresh()
	{
		requsetVirtualTotalAssets(false);
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
			default:
				mRefreshLayout.setRefreshing(false);
				break;

			}
		};
	};

	private void requsetVirtualTotalAssets(boolean isShow)
	{
		if (isShow)
		{
			mCurrentActivity.showLoading();
		}
		AnsynHttpRequest.requestByPost(getActivity(), UrlConst.VIRTUAL_TOTAL_ASSETS, new HttpCallBack()
		{

			@Override
			public void back(String data, int status)
			{
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = 0x01;
				mHandler.sendMessage(msg);
			}
		}, null);
	}

	private void handleDetail(String data)
	{
		mCurrentActivity.dismissLoading();
		// mPullScrollView.onRefreshComplete();
		mRefreshLayout.setRefreshing(false);
		try
		{
			JSONObject object = new JSONObject(data);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject result = object.optJSONObject("data").optJSONObject("assets");
				profitTime = result.optString("moditm");
				profitYesday = result.optString("profitYesday");
				totalProfit = result.optString("profit");
				virtualAsset = result.optString("total_assets");
				redeempIng = result.optString("redemping");
				buying = result.optString("subscripting");
				marketValue = result.optString("assets");
				usableValue = result.optString("usable_assets");
				updateUI();
				break;
			default:
				mCurrentActivity.showHintDialog(reason);
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
		mYesterDayView.setText(DateFormatHelper.formatDate(profitTime.concat("000"), DateFormat.DATE_5));
		if (Double.parseDouble(profitYesday) >= 0)
		{
			if (Double.parseDouble(profitYesday) > 0)
			{
				mYesterProfitView.setText(getSpannableString(
						getString(R.string.jia_hao) + StringHelper.formatString(profitYesday, StringFormat.FORMATE_1),
						1, 2, 56, 28));
			}
			else
			{
				mYesterProfitView.setText(getSpannableString(
						StringHelper.formatString(profitYesday, StringFormat.FORMATE_1), 0, 2, 56, 28));
			}
		}
		else
		{
			mYesterProfitView.setTextColor(getResources().getColor(R.color.color_307d1b));
			mYesterProfitView.setText(getSpannableString(profitYesday, 1, 2, 56, 28));
		}

		mTotalProfitView.setText(getString(R.string.fund_profit)
				+ StringHelper.formatString(totalProfit, StringFormat.FORMATE_1));
		mVirtualTotalView.setText(getSpannableString(StringHelper.formatString(virtualAsset, StringFormat.FORMATE_1),
				0, 2, 45, 22));
		mBuyingView
				.setText(getString(R.string.fund_buying) + StringHelper.formatString(buying, StringFormat.FORMATE_1));
		mRedeemView.setText(getString(R.string.fund_redeeming)
				+ StringHelper.formatString(redeempIng, StringFormat.FORMATE_1));
		mMarketValueView.setText(mCurrentActivity.getString(R.string.virtual_market_value)
				+ StringHelper.formatString(marketValue, StringFormat.FORMATE_1));
		mVirtualMoneyView.setText(mCurrentActivity.getString(R.string.virtual_usable_money)
				+ StringHelper.formatString(usableValue, StringFormat.FORMATE_1));
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
}