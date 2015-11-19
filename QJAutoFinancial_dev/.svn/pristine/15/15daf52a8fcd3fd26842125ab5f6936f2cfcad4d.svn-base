
package com.qianjing.finance.ui.activity.rebalance;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.rebalance.RebalanceDetail;
import com.qianjing.finance.net.helper.RequestRebalanceHelper;
import com.qianjing.finance.net.ihandle.IHandleRebalanceDetail;
import com.qianjing.finance.net.response.model.ResponseRebalanceDetail;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Description:再平衡界面
 * @author liuchen
 */

public class RebalanceActivity extends BaseActivity implements OnClickListener {

    private TextView realBtn;
    private TextView virtualBtn;
    private TextView tvTitle;
    private LinearLayout llBalanceContent;
    private TextView balanceReason;
    private LinearLayout balanceOpt;
    private TextView balanceFee;
    private CheckBox cbAgreement;
    private TextView tvAgreement;
    private Button btnAgree;
    private RebalanceDetail details;
    private String mSid;
    private TextView item2Title;
    private TextView item3Title;
    private String BALANCEP = "http://i.qianjing.com/tool/other/re_balance/agree.php";
    private String BALANCEI = "http://i.qianjing.com/tool/other/re_balance/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebalance);
        initView();
        initData();
        initAssetsView();
    }

    private void initData() {
        mSid = getIntent().getStringExtra("mSid");
        String title = getIntent().getStringExtra("mName");
        tvTitle.setText(title);
        RequestRebalanceHelper.requestRebalanceDetail(this, mSid, new IHandleRebalanceDetail() {

            @Override
            public void handleResponse(ResponseRebalanceDetail response) {

                details = response.detail;
                if (details != null) {
                    balanceFee.setText(details.fee + "");
                    balanceReason.setText(details.reason);
                    for (int i = 0; i < details.listBeforeFund.size(); i++) {
                        setDetailsContent1(
                                details.listBeforeFund.get(i).name
                                ,
                                StringHelper.formatString(
                                        String.valueOf(details.listBeforeFund.get(i).rate),
                                        StringFormat.FORMATE_2) + "%"
                                , StringHelper.formatString(
                                        String.valueOf(details.listBeforeFund.get(i).shares),
                                        StringFormat.FORMATE_2));
                    }
                    for (int i = 0; i < details.listHandleFund.size(); i++) {
                        if (details.listHandleFund.get(i).subscriptShares < 0) {
                            setDetailsContent2(
                                    false
                                    ,
                                    details.listHandleFund.get(i).name
                                    ,
                                    StringHelper.formatString(String
                                            .valueOf(details.listHandleFund.get(i).redeemShares),
                                            StringFormat.FORMATE_2)
                                            + "份");
                        } else {
                            setDetailsContent2(
                                    true
                                    ,
                                    details.listHandleFund.get(i).name
                                    ,
                                    StringHelper.formatString(String
                                            .valueOf(details.listHandleFund.get(i).subscriptSum),
                                            StringFormat.FORMATE_2)
                                            + "元");
                        }
                    }
                } else {
                    showHintDialog(response.strError);
                }
            }
        });
    }

    private void initView() {
        setTitleBack();
        setTitleWithString("再平衡详情");
        tvTitle = (TextView) findViewById(R.id.tv_balance_title);
        llBalanceContent = (LinearLayout) findViewById(R.id.ll_balance_content);
        balanceReason = (TextView) findViewById(R.id.tv_balance_reason);
        balanceOpt = (LinearLayout) findViewById(R.id.ll_balance_opt);
        balanceFee = (TextView) findViewById(R.id.tv_balance_fee);
        cbAgreement = (CheckBox) findViewById(R.id.cb_agreement);
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        btnAgree = (Button) findViewById(R.id.btn_agree);

        item2Title = (TextView) findViewById(R.id.tv_item2_title);
        item3Title = (TextView) findViewById(R.id.tv_item3_title);
        ImageView rightImage = (ImageView) findViewById(R.id.title_right_image);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setBackgroundResource(R.drawable.chinapay_help_click);

        rightImage.setOnClickListener(this);
        cbAgreement.setOnClickListener(this);
        tvAgreement.setOnClickListener(this);
        btnAgree.setOnClickListener(this);

        cbAgreement.setChecked(true);
        btnAgree.setEnabled(true);
    }

    private void initAssetsView()
    {
        realBtn = (TextView) findViewById(R.id.tv_real_trade);
        virtualBtn = (TextView) findViewById(R.id.tv_virtual_trade);
        realBtn.setSelected(true);
        virtualBtn.setSelected(false);
        realBtn.setTextColor(Color.WHITE);
        virtualBtn.setTextColor(getResources().getColor(R.color.blue_deep_grey));
        realBtn.setOnClickListener(this);
        virtualBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_real_trade:
                realBtn.setSelected(true);
                virtualBtn.setSelected(false);
                realBtn.setTextColor(Color.WHITE);
                virtualBtn.setTextColor(getResources().getColor(R.color.blue_deep_grey));

                item3Title.setText("持仓份额");
                item2Title.setText("市值占比");
                if (details != null) {
                    llBalanceContent.removeAllViews();
                    for (int i = 0; i < details.listBeforeFund.size(); i++) {
                        setDetailsContent1(
                                details.listBeforeFund.get(i).name
                                ,
                                StringHelper.formatString(
                                        String.valueOf(details.listBeforeFund.get(i).rate),
                                        StringFormat.FORMATE_2) + "%"
                                , StringHelper.formatString(
                                        String.valueOf(details.listBeforeFund.get(i).shares),
                                        StringFormat.FORMATE_2));
                    }
                }

                break;
            case R.id.tv_virtual_trade:
                realBtn.setSelected(false);
                virtualBtn.setSelected(true);
                realBtn.setTextColor(getResources().getColor(R.color.blue_deep_grey));
                virtualBtn.setTextColor(Color.WHITE);

                item2Title.setText("配置占比");
                item3Title.setText("预估市值");
                if (details != null) {
                    llBalanceContent.removeAllViews();
                    for (int i = 0; i < details.listAfterFund.size(); i++) {
                        setDetailsContent1(
                                details.listAfterFund.get(i).name
                                ,
                                StringHelper.formatString(
                                        String.valueOf(details.listAfterFund.get(i).rate),
                                        StringFormat.FORMATE_2) + "%"
                                , StringHelper.formatString(
                                        String.valueOf(details.listAfterFund.get(i).shares),
                                        StringFormat.FORMATE_2));
                    }
                }
                break;

            case R.id.cb_agreement:
                if (cbAgreement.isChecked()) {
                    btnAgree.setEnabled(true);
                } else {
                    btnAgree.setEnabled(false);
                }
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(RebalanceActivity.this, WebActivity.class);
                intent.putExtra("url", BALANCEP);
                intent.putExtra("webType", 8);
                startActivity(intent);
                break;
            case R.id.btn_agree:
                showPasswordDialog();
                break;
            case R.id.title_right_image:
                Intent intent0 = new Intent(RebalanceActivity.this, WebActivity.class);
                intent0.putExtra("url", BALANCEI);
                intent0.putExtra("webType", 8);
                startActivity(intent0);
                break;
        }
    }

    private void submitRebalance(String passWord) {

        RequestRebalanceHelper.requestRebalanceSubmit(RebalanceActivity.this, mSid, passWord,
                new IHandleRebalanceDetail() {

                    @Override
                    public void handleResponse(ResponseRebalanceDetail response) {
                        // 判断response是否是Null
                        if (response != null) {
                            if (response.detail != null) {
                                RebalanceDetail rebalanceDetails = response.detail;
                                if (rebalanceDetails != null) {
                                    Intent agreeIntent = new Intent(RebalanceActivity.this,
                                            RebalanceResultActivity.class);
                                    agreeIntent.putExtra("rebalanceDetails", rebalanceDetails);
                                    startActivityForResult(agreeIntent, 0);
                                }
                            }
                            else {
                                showHintDialog(response.strError);
                            }
                        }
                    }
                });
    }

    public RebalanceDetail simulationData(RebalanceDetail rebalanceDetails) {
        rebalanceDetails.fee = 100;
        rebalanceDetails.purchaseConfirmTime = 1432715521;
        rebalanceDetails.purchaseTime = 1432715521;
        rebalanceDetails.reason = "这个是测试数据";
        rebalanceDetails.redeemArriveTime = 1432715521;
        rebalanceDetails.redeemConfirmTime = 1432715521;
        rebalanceDetails.strRsid = "123456";

        for (int i = 0; i < 6; i++) {

            Fund fund = new Fund();
            if (i % 2 == 0) {
                fund.redeemShares = 5;
            }
            rebalanceDetails.listHandleFund.add(fund);

        }

        return rebalanceDetails;
    }

    private void showPasswordDialog() {

        final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle("请输入登陆密码");
        inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = inputDialog.getEditText().getText().toString().trim();
                if (StringHelper.isBlank(text)) {
                    Toast.makeText(RebalanceActivity.this, "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    submitRebalance(text);
                }
                ;
                dialog.dismiss();
            }
        });
        inputDialog.create().show();
    }

    private void setDetailsContent1(String abbrev, String value, String share) {

        View view = View.inflate(this, R.layout.balance_item1, null);
        TextView balanceItem1 = (TextView) view.findViewById(R.id.tv_balance_item1);
        TextView balanceItem2 = (TextView) view.findViewById(R.id.tv_balance_item2);
        TextView balanceItem3 = (TextView) view.findViewById(R.id.tv_balance_item3);

        balanceItem1.setText(abbrev);
        balanceItem2.setText(value);
        balanceItem3.setText(share);

        llBalanceContent.addView(view);
    }

    private void setDetailsContent2(boolean isPurchase, String abbrev, String value) {

        View view = View.inflate(this, R.layout.balance_item2, null);
        ImageView balanceItem1 = (ImageView) view.findViewById(R.id.tv_balance_item1);
        TextView balanceItem2 = (TextView) view.findViewById(R.id.tv_balance_item2);
        TextView balanceItem3 = (TextView) view.findViewById(R.id.tv_balance_item3);

        if (isPurchase) {
            balanceItem1.setBackgroundResource(R.drawable.balance_purchase);
        } else {
            balanceItem1.setBackgroundResource(R.drawable.balance_redeem);
        }
        balanceItem2.setText(abbrev);
        balanceItem3.setText(value);

        balanceOpt.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CustomConstants.SHUDDOWN_ACTIVITY) {
            finish();
        }
    }

}
