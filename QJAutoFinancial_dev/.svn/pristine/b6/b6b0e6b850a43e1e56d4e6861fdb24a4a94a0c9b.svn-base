
package com.qianjing.finance.ui.activity.rebalance;

import com.qianjing.finance.model.rebalance.RebalanceHistoryDetail;
import com.qianjing.finance.net.helper.RequestRebalanceHelper;
import com.qianjing.finance.net.ihandle.IHandleRebalanceHistoryDetail;
import com.qianjing.finance.net.response.model.ResponseRebalanceHistoryDetail;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.StateUtils;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RebalanceHistoryDetails extends BaseActivity {

    private LinearLayout detailsContent;
    private TextView stateText;
    private ImageView stateIcon;
    private RebalanceHistoryDetail rebalanceHistoryDetails;
    private TextView historyName;
    private TextView historyFee;
    private TextView historyDate;
    private TextView bank;
    private TextView value;
    private TextView tvHoldingView;
    private TextView tvBalanceReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_rebalance_details);
        setTitleBack();
        initView();
        initData();
    }

    /**
     * deal with data
     */

    private void initData() {

        String sopid = getIntent().getStringExtra("mSopid");
        RequestRebalanceHelper.requestRebalanceHistoryDetail(
                RebalanceHistoryDetails.this, sopid,
                new IHandleRebalanceHistoryDetail() {

                    @Override
                    public void handleResponse(
                            ResponseRebalanceHistoryDetail response) {
                        rebalanceHistoryDetails = response.historyDetail;
                        if (rebalanceHistoryDetails != null) {
                            setTitleWithString(rebalanceHistoryDetails.sname);
                            historyName.setText(rebalanceHistoryDetails.sname);
                            historyFee.setText("¥"
                                    + StringHelper
                                            .formatString(String
                                                    .valueOf(rebalanceHistoryDetails.estimate_fee),
                                                    StringFormat.FORMATE_1));
                            historyDate.setText(DateFormatHelper
                                    .formatDate(
                                            (rebalanceHistoryDetails.opDate + "")
                                                    .concat("000"),
                                            DateFormat.DATE_4));
                            bank.setText(rebalanceHistoryDetails.bank
                                    + "(尾号"
                                    + StringHelper
                                            .showCardLast4(rebalanceHistoryDetails.card)
                                    + ")");
                            if (!StringHelper
                                    .isBlank(rebalanceHistoryDetails.market_value)) {
                                value.setText("¥"
                                        + StringHelper.formatString(
                                                rebalanceHistoryDetails.market_value,
                                                StringFormat.FORMATE_2));
                            }

                            tvBalanceReason
                                    .setText(rebalanceHistoryDetails.reason);

                            int state = Integer
                                    .parseInt(rebalanceHistoryDetails.state);
                            stateText.setText(StateUtils
                                    .getRebalanceStateStr(state));

                            int resource = StateUtils
                                    .getRebalanceResoure(state);
                            if (resource != -1) {
                                stateIcon.setBackgroundResource(resource);
                            } else {
                                stateIcon.setVisibility(View.GONE);
                            }

                            int size = rebalanceHistoryDetails.listFund.size();
                            // detailsContent.removeAllViews();
                            for (int j = 0; j < size; j++) {
                                String shares = rebalanceHistoryDetails.listFund
                                        .get(j).shares;
                                String sums = rebalanceHistoryDetails.listFund
                                        .get(j).sum;
                                String shareStr = StringHelper.isBlankZero(shares) ? "--"
                                        : shares;
                                String sumStr = StringHelper.isBlankZero(sums) ? "--"
                                        : sums;

                                boolean isPurchase = Integer
                                        .parseInt(rebalanceHistoryDetails.listFund
                                                .get(j).operate) == 1;

                                setDetailsContent2(
                                        isPurchase ? true : false,
                                        rebalanceHistoryDetails.listFund.get(j).abbrev,
                                        isPurchase ? formatString(
                                                sumStr
                                                        + getString(R.string.money_unit),
                                                shareStr
                                                        + getString(R.string.fen))
                                                : formatString(
                                                        shareStr
                                                                + getString(R.string.fen),
                                                        sumStr
                                                                + getString(R.string.money_unit)));
                            }
                        } else {
                            showHintDialog(response.strError);
                        }
                    }
                });
    }

    private String formatString(String str1, String str2) {
        return String.format(getString(R.string.balance_share_value), str1,
                str2);
    }

    /**
     * UI
     */

    private void initView() {

        value = (TextView) findViewById(R.id.tv_history_value);
        bank = (TextView) findViewById(R.id.tv_history_bank);
        historyDate = (TextView) findViewById(R.id.tv_history_time);
        historyFee = (TextView) findViewById(R.id.tv_history_fee);
        historyName = (TextView) findViewById(R.id.tv_history_name);
        stateIcon = (ImageView) findViewById(R.id.iv_state_icon);
        stateText = (TextView) findViewById(R.id.tv_state_text);
        detailsContent = (LinearLayout) findViewById(R.id.ll_history_details_content);
        tvHoldingView = (TextView) findViewById(R.id.tv_rebalance_holding_view);
        tvBalanceReason = (TextView) findViewById(R.id.tv_balance_reason);

        tvHoldingView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (rebalanceHistoryDetails != null) {
                    String rsid = rebalanceHistoryDetails.rsid;
                    if (rsid != null) {
                        Intent intent = new Intent(
                                RebalanceHistoryDetails.this,
                                RebalanceHoldingActivity.class);
                        intent.putExtra("mRsid", rebalanceHistoryDetails.rsid);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * view list data
     */
    private void setDetailsContent2(boolean isPurchase, String abbrev,
            String value) {

        View view = View.inflate(this, R.layout.balance_item3, null);
        ImageView balanceIcon = (ImageView) view
                .findViewById(R.id.tv_balance_item1);
        TextView balanceItem2 = (TextView) view
                .findViewById(R.id.tv_balance_item2);
        TextView balanceItem3 = (TextView) view
                .findViewById(R.id.tv_balance_item3);

        if (isPurchase) {
            balanceIcon.setBackgroundResource(R.drawable.balance_purchase);
        } else {
            balanceIcon.setBackgroundResource(R.drawable.balance_redeem);
        }
        balanceItem2.setText(abbrev);
        balanceItem3.setText(value);

        detailsContent.addView(view);
    }

}
