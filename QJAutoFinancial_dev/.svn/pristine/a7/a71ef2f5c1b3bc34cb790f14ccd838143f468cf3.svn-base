
package com.qianjing.finance.ui.activity.assemble.profit;

import com.qianjing.finance.model.assemble.AssembleDayProfit;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.widget.adapter.dayprofit.DayProfitDetailAdapter;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @description 指定组合盈亏详情页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AssembleDayProfitDetailActivity extends BaseActivity {
    /**
     * UI
     */
    private TextView mTitleView;
    private ListView mProfitListView;
    private DayProfitDetailAdapter mAdapter;
    /**
     * data
     */
    private AssembleDayProfit mDayProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_day_profit_detail);
        initData();
        initView();
    }

    private void initData() {
        mDayProfit = (AssembleDayProfit) getIntent().getSerializableExtra(
                "dayProfit");
    }

    private void initView() {
        setTitleBack();
        mTitleView = (TextView) findViewById(R.id.title_middle_text);
        mTitleView.setText(DateFormatHelper.formatDate(String
                .valueOf(mDayProfit.getDt()).concat("000"), DateFormat.DATE_1)
                + getString(R.string.day_profit_detail));
        mProfitListView = (ListView) findViewById(R.id.profit_detail_list_view);
        mAdapter = new DayProfitDetailAdapter(this, mDayProfit.getProfitList());
        mProfitListView.setAdapter(mAdapter);
    }
}
