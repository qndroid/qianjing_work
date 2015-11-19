
package com.qianjing.finance.ui.activity.assemble.redeem;

import com.qianjing.finance.model.redeem.FundItemState;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.view.assembledetailview.AssembleReasonItemLayout;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 组合赎回结果页
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AssembleRedeemResultActivity extends BaseActivity implements OnClickListener {
    private TextView titleView;
    private TextView finishView;
    /**
     * 虚拟赎回成功UI
     */
    private LinearLayout successLayout;
    private TextView sumView;
    private TextView feeView;
    private TextView remainView;

    /**
     * 正在赎回UI
     */
    private ScrollView redeemLayout;
    private LinearLayout reasonLayout;
    private TextView opterationTitleView;
    private TextView opDateTimeView;
    private TextView profitTimeView;
    private TextView profitArriveView;
    private TextView bankNameView;
    private TextView confirmTimeWeekView;
    private TextView finalTimeWeekView;
    private TextView confirmMsgView;
    private TextView finalMsgView;
    private ImageView finalImageView;
    private TextView takeMoneyView;
    private TextView takeMoneyValueView;
    private TextView feeValueView;
    private LinearLayout redeemTitleLayout;

    /**
     * 赎回失败
     */
    private ScrollView failedLayout;
    private LinearLayout failedReasonLayout;
    /**
     * data
     */
    private int type; // 赎回结果
    private String sum; // 赎回总金额
    private String fee; // 赎回手续费
    private double remain; // 剩余金额
    private long optTime;
    private long confirTime;
    private long arriveTime;
    // private String confirmDay;
    private String arriveDay;
    private String cardNum;
    private String cardName;
    private ArrayList<FundItemState> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_redeem_result);
        initData();
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        sum = intent.getStringExtra("sum");
        fee = intent.getStringExtra("fee");
        remain = intent.getDoubleExtra("amount", -1);
        optTime = intent.getLongExtra("opdate", -1);
        confirTime = intent.getLongExtra("confirm_time", -1);
        arriveTime = intent.getLongExtra("arrive_time", -1);
        arriveDay = intent.getStringExtra("arrive_day");
        cardNum = intent.getStringExtra("cardnumber");
        cardName = intent.getStringExtra("card");

        stateList = (ArrayList<FundItemState>) intent
                .getSerializableExtra("state");
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.title_middle_text);
        titleView.setText(getString(R.string.result_info));
        finishView = (TextView) findViewById(R.id.right_title_view);
        finishView.setVisibility(View.VISIBLE);
        finishView.setText(getString(R.string.complete));
        finishView.setOnClickListener(this);

        switch (type) {
            case 0:
                /**
                 * 虚拟布局
                 */
                successLayout = (LinearLayout) findViewById(R.id.success_layout);
                sumView = (TextView) findViewById(R.id.virtual_sum);
                remainView = (TextView) findViewById(R.id.virtual_remain);
                feeView = (TextView) findViewById(R.id.virtual_fee);
                successLayout.setVisibility(View.VISIBLE);
                sumView.setText(StringHelper.formatString(sum, StringFormat.FORMATE_2));
                feeView.setText(String.valueOf(StringHelper.formatString(fee,
                        StringFormat.FORMATE_2)));
                remainView
                        .setText(String.valueOf(StringHelper.formatString(String.valueOf(remain),StringFormat.FORMATE_2)));
                break;
            case 3:
                /**
                 * 完全失败
                 */
                failedLayout = (ScrollView) findViewById(R.id.failed_layout);
                failedReasonLayout = (LinearLayout) findViewById(R.id.fail_content_layout);
                failedLayout.setVisibility(View.VISIBLE);
                /**
                 * 循环添加状态
                 */
                for (int i = 0; i < stateList.size(); i++) {
                    AssembleReasonItemLayout reasonItem = new AssembleReasonItemLayout(
                            this);
                    reasonItem.initData(stateList.get(i).abbrev,
                            stateList.get(i).redeemFen,
                            getString(R.string.fail), stateList.get(i).reason);
                    failedReasonLayout.addView(reasonItem);
                }
                break;
            default:
                /**
                 * 真实赎回结果布局
                 */
                redeemLayout = (ScrollView) findViewById(R.id.redeem_layout);
                ViewStub redeemResultLayout = (ViewStub) findViewById(R.id.include_ok);
                View redeemView = redeemResultLayout.inflate();
                opDateTimeView = (TextView) redeemView
                        .findViewById(R.id.commit_time_view);
                profitTimeView = (TextView) redeemView
                        .findViewById(R.id.confirm_time_view);
                profitArriveView = (TextView) redeemView
                        .findViewById(R.id.final_time_view);
                bankNameView = (TextView) redeemView
                        .findViewById(R.id.bank_name_view);
                confirmTimeWeekView = (TextView) redeemView
                        .findViewById(R.id.confirm_time_value_view);
                finalTimeWeekView = (TextView) redeemView
                        .findViewById(R.id.final_time_value_view);
                confirmMsgView = (TextView) findViewById(R.id.confirm_time_msg_view);
                finalMsgView = (TextView) findViewById(R.id.final_time_msg_view);
                takeMoneyView = (TextView) findViewById(R.id.deduct_money_view);
                takeMoneyValueView = (TextView) findViewById(R.id.deduct_money_value_view);
                opterationTitleView = (TextView) findViewById(R.id.opreate_title_view);
                feeView = (TextView) findViewById(R.id.fee_money_view);
                feeValueView = (TextView) findViewById(R.id.fee_money_value_view);
                reasonLayout = (LinearLayout) findViewById(R.id.content_layout);
                finalImageView = (ImageView) findViewById(R.id.third_part);
                redeemTitleLayout = (LinearLayout) findViewById(R.id.redeem_info_title_layout);

                redeemLayout.setVisibility(View.VISIBLE);
                opterationTitleView.setText(type == 2
                        ? getString(R.string.redeemp_part_hading)
                        : getString(R.string.redeemp_all_hading));
                bankNameView.setText(cardName
                        + getString(R.string.left_brackets)
                        + getString(R.string.wei_hao)
                        + StringHelper.showCardLast4(cardNum)
                        + getString(R.string.right_brackets));
                opDateTimeView.setText(DateFormatHelper.formatDate(
                        String.valueOf(optTime * 1000), DateFormat.DATE_9));
                profitTimeView.setText(DateFormatHelper.formatDate(
                        String.valueOf(confirTime * 1000), DateFormat.DATE_1));
                confirmTimeWeekView.setText(DateFormatHelper
                        .formatDate(
                                String.valueOf(confirTime * 1000),
                                DateFormat.DATE_6));
                confirmMsgView.setCompoundDrawables(null, null, null, null);
                confirmMsgView.setText(getString(R.string.redeem_wait_confirm));
                profitArriveView.setText(DateFormatHelper
                        .formatDate(
                                String.valueOf(arriveTime * 1000),
                                DateFormat.DATE_1));
                finalTimeWeekView.setText(DateFormatHelper
                        .formatDate(
                                String.valueOf(arriveTime * 1000),
                                DateFormat.DATE_6));
                finalMsgView.setCompoundDrawables(null, null, null, null);
                finalMsgView.setText(getString(R.string.money_daoda));
                takeMoneyView
                        .setText(getString(R.string.redeemp_evaluate_yuan));
                takeMoneyValueView.setText(getString(R.string.ren_ming_bi)
                        + StringHelper.formatString(sum, StringFormat.FORMATE_1));
                feeView.setText(getString(R.string.redeemp_evaluate_fee));
                feeValueView.setText(getString(R.string.ren_ming_bi)
                        + StringHelper.formatString(fee, StringFormat.FORMATE_1));
                finalImageView.setBackgroundResource(arriveDay.equals("2")
                        ? R.drawable.icon_day_two
                        : R.drawable.icon_day_four);
                if (type == 1) {
                    reasonLayout.setVisibility(View.GONE);
                    redeemTitleLayout.setVisibility(View.GONE);
                } else {
                    /**
                     * 循环添加状态
                     */
                    for (int i = 0; i < stateList.size(); i++) {
                        AssembleReasonItemLayout reasonItem = new AssembleReasonItemLayout(
                                this);
                        reasonItem.initData(stateList.get(i).abbrev, stateList
                                .get(i).redeemFen,
                                stateList.get(i).fdstate == 2
                                        ? getString(R.string.redeeming)
                                        : getString(R.string.fail), stateList
                                        .get(i).reason);
                        reasonLayout.addView(reasonItem);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_title_view:
            case R.id.btn_submit:
                setResult(ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
