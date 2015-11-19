
package com.qianjing.finance.ui.activity.rebalance;

import com.qianjing.finance.model.rebalance.RebalanceFund;
import com.qianjing.finance.model.rebalance.RebalanceHoldingCompare;
import com.qianjing.finance.net.helper.RequestRebalanceHelper;
import com.qianjing.finance.net.ihandle.IHandleError;
import com.qianjing.finance.net.ihandle.IHandleRebalanceHoldingCompare;
import com.qianjing.finance.net.response.model.ResponseRebalanceHoldingCompare;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @Description:再平衡持仓界面
 * @author liuchen
 */

public class RebalanceHoldingActivity extends BaseActivity {

    private ArrayList<RebalanceFund> listFund;
    private TextView beforeTime;
    private TextView afterTime;
    private LinearLayout beforeHolding;
    private LinearLayout afterHolding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        RequestRebalanceHelper.requestRebalanceHoldingCompare(this, getIntent()
                .getStringExtra("mRsid"), new IHandleRebalanceHoldingCompare() {

            @Override
            public void handleResponse(ResponseRebalanceHoldingCompare response) {

                RebalanceHoldingCompare details = response.holdingCompare;

                if (details != null) {
                    beforeTime.setText("("
                            + DateFormatHelper.formatDate(
                                    (details.beforeTime + "")
                                            .concat("000"), DateFormat.DATE_1)
                            + ")");

                    afterTime.setText("("
                            + DateFormatHelper.formatDate(
                                    (details.afterTime + "")
                                            .concat("000"), DateFormat.DATE_1)
                            + ")");
                    int beforeSize = details.listBeforeFund.size();
                    int afterSize = details.listAfterFund.size();
                    for (int i = 0; i < beforeSize; i++) {
                        setDetailsContent(beforeHolding,
                                details.listBeforeFund.get(i).name,
                                (float) details.listBeforeFund.get(i).rate,
                                (float) details.listBeforeFund.get(i).shares);
                    }
                    for (int i = 0; i < afterSize; i++) {
                        setDetailsContent(afterHolding,
                                details.listAfterFund.get(i).name,
                                (float) details.listAfterFund.get(i).rate,
                                (float) details.listAfterFund.get(i).shares);
                    }
                } else {
                    showHintDialog(response.strError);
                }
            }
        }, new IHandleError() {

            @Override
            public void handleError(int state) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        setContentView(R.layout.activity_rebalance_holding);
        setTitleWithId(R.string.before_and_after_holding);
        setTitleBack();
        beforeTime = (TextView) findViewById(R.id.tv_before_time);
        afterTime = (TextView) findViewById(R.id.tv_after_time);
        beforeHolding = (LinearLayout) findViewById(R.id.ll_before_holding);
        afterHolding = (LinearLayout) findViewById(R.id.ll_after_holding);

    }

    private void setDetailsContent(LinearLayout ll, String abbrev, float item2,
            float item3) {

        View view = View.inflate(this, R.layout.history_details_item, null);
        TextView historyItem1 = (TextView) view
                .findViewById(R.id.tv_history_item1);
        TextView historyItem2 = (TextView) view
                .findViewById(R.id.tv_history_item2);
        TextView historyItem3 = (TextView) view
                .findViewById(R.id.tv_history_item3);

        historyItem1.setText(abbrev);

        historyItem2.setText(StringHelper.formatString(String.valueOf(item2),
                StringFormat.FORMATE_2) + "%");
        historyItem3.setText(StringHelper.formatString(String.valueOf(item3),
                StringFormat.FORMATE_2) + "份");

        ll.addView(view);
    }
}
