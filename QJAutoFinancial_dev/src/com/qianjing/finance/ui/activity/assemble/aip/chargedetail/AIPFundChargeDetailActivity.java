
package com.qianjing.finance.ui.activity.assemble.aip.chargedetail;

import com.qianjing.finance.model.aip.chargedetail.AIPChargeDetail;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.widget.adapter.aip.AIPChargeDetailAdapter;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @description 指定组合盈亏详情页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AIPFundChargeDetailActivity extends BaseActivity {
    /**
     * UI
     */
    private TextView mTitleView;
    private ListView mProfitListView;
    private AIPChargeDetailAdapter mAdapter;
    /**
     * data
     */
    private AIPChargeDetail mDayProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aip_fund_detail);
        initData();
        initView();
    }

    private void initData() {
        mDayProfit = (AIPChargeDetail) getIntent().getSerializableExtra(
                "fundDetail");
    }

    private void initView() {
        setTitleBack();
        mTitleView = (TextView) findViewById(R.id.title_middle_text);
        mTitleView.setSingleLine();
        mTitleView.setText(mDayProfit.fundName
                + getString(R.string.aip_charge_history));
        mProfitListView = (ListView) findViewById(R.id.charge_list_view);
        mAdapter = new AIPChargeDetailAdapter(this, mDayProfit.timeList);
        mProfitListView.setAdapter(mAdapter);
    }
}
