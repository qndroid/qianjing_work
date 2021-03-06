
package com.qianjing.finance.ui.activity.fund.asset;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.history.FundHistoryActivity;
import com.qianjing.finance.ui.activity.profit.ProfitActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
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

/**
 * @description 基金资产页面
 * @author majinxin
 * @date 2015年9月2日
 */
public class FundAssetActivity extends BaseActivity implements OnClickListener
{
    /**
     * UI
     */
    private Button mBackButton;
    private TextView mKouKuanDetailView;
    private RelativeLayout mProfitInfoLayout;
    private TextView mProfitView;
    private TextView mWaitingProfitView;
    private TextView mYesterdayView;
    private RelativeLayout mTotalInfoLayout;
    private TextView mAllProfitView;
    private TextView mBuyingView;
    private TextView mRedeemView;
    private TextView mScanAssertView;
    private PullToRefreshScrollView pullScrollView;

    /**
     * data
     */
    private String fundAsset; // 总资产
    private String profitYesday;
    private String totalProfit;
    private String profitTime;
    private String redeempIng;
    private String buying;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_asset);
        initView();

        requestTimeDespoitData(true);
    }

    private void initView()
    {
        setTitleWithId(R.string.fund_asset_title);
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
        mYesterdayView = (TextView) findViewById(R.id.lei_ji_value_view);
        mTotalInfoLayout = (RelativeLayout) findViewById(R.id.total_layout);
        mAllProfitView = (TextView) findViewById(R.id.all_assert_value);
        mBuyingView = (TextView) findViewById(R.id.buying_view);
        mRedeemView = (TextView) findViewById(R.id.redeem_view);
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
        AnsynHttpRequest.requestByPost(this, UrlConst.URL_FUND_MINE, new HttpCallBack()
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
        }, null);
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
                    fundAsset = result.optString("market_value");
                    profitYesday = result.optString("profitYesday");
                    totalProfit = result.optString("profit");
                    profitTime = result.optString("profitT");
                    redeempIng = result.optString("redemping");
                    buying = result.optString("subscripting");
                    updateUI();
                    break;
                default:
                    showHintDialog(reason);
                    break;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateUI()
    {
        mYesterdayView.setText(DateFormatHelper.formatDate(profitTime.concat("000"), DateFormat.DATE_5));
        if (Double.parseDouble(profitYesday) >= 0)
        {
            if (Double.parseDouble(profitYesday) > 0)
            {
                mProfitView.setText(getSpannableString(
                        getString(R.string.jia_hao) + StringHelper.formatString(profitYesday, StringFormat.FORMATE_1),
                        1, 2, 56, 28));
            }
            else
            {
                mProfitView.setText(getSpannableString(StringHelper.formatString(profitYesday, StringFormat.FORMATE_1),
                        0, 2, 56, 28));
            }
        }
        else
        {
            mProfitView.setTextColor(getResources().getColor(R.color.color_307d1b));
            mProfitView.setText(getSpannableString(profitYesday, 1, 2, 56, 28));
        }

        mWaitingProfitView.setText(getString(R.string.fund_profit) + totalProfit);
        mAllProfitView.setText(getSpannableString(StringHelper.formatString(fundAsset, StringFormat.FORMATE_1), 0, 2,
                45, 22));
        mBuyingView
                .setText(getString(R.string.fund_buying) + StringHelper.formatString(buying, StringFormat.FORMATE_1));
        mRedeemView.setText(getString(R.string.fund_redeeming)
                + StringHelper.formatString(redeempIng, StringFormat.FORMATE_1));
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
        Intent intent;
        switch (v.getId())
        {
            case R.id.leiji_layout:
                intent = new Intent(this, ProfitActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.total_layout:
                intent = new Intent(this, FundHistoryActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.scan_assert_view:
                startActivity(new Intent(this, FundAssetListActivity.class));
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.right_title_view:
                // 去交易记录
                intent = new Intent(this, FundHistoryActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
    }
}
