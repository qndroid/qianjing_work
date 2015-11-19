
package com.qianjing.finance.ui.activity.assemble.aip.modify;

import com.qianjing.finance.model.aip.AIPDetail;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @description 组合定投详情页面
 * @author renzhiqiang
 * @date 2015年8月6日
 */
public class AssembleAIPModifyResultActivity extends BaseActivity
        implements
        OnClickListener {
    /**
     * UI
     */
    private TextView mFinishView;
    private TextView mDayView;
    private TextView mMoneyView;
    private TextView mCardView;

    /**
     * data
     */
    private AIPDetail mAipDetail;
    private String monthSum;
    private int currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_aip_modify_result);
        initData();
        initView();
    }

    private void initData() {
        mAipDetail = (AIPDetail) getIntent().getSerializableExtra("AIPDetail");
        monthSum = getIntent().getStringExtra("currentMoney");
        currentDay = getIntent().getIntExtra("currentDay", -1);
    }

    private void initView() {
        setLoadingUncancelable();
        setTitleWithId(R.string.title_result);
        mFinishView = (TextView) findViewById(R.id.right_title_view);
        mFinishView.setVisibility(View.VISIBLE);
        mFinishView.setText(getString(R.string.str_finish));
        mFinishView.setOnClickListener(this);

        mDayView = (TextView) findViewById(R.id.timing_modify_date_value);
        mMoneyView = (TextView) findViewById(R.id.timing_modify_money_value);
        mCardView = (TextView) findViewById(R.id.timing_modify_card_value);

        mDayView.setText(currentDay + getString(R.string.timing_modify_day));
        mMoneyView.setText(getString(R.string.ren_ming_bi)
                + StringHelper.formatString(monthSum,
                        StringFormat.FORMATE_2));
        mCardView.setText(mAipDetail.bank + getString(R.string.left_brackets)
                + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(mAipDetail.card)
                + getString(R.string.right_brackets));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.right_title_view:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}