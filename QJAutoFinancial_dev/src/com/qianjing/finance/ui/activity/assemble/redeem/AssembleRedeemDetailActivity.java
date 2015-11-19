
package com.qianjing.finance.ui.activity.assemble.redeem;

import com.qianjing.finance.model.fund.MyFundAssets;
import com.qianjing.finance.model.redeem.AssembleShares;
import com.qianjing.finance.model.redeem.RedeemModel;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleItemLayout;
import com.qianjing.finance.view.assembleredeem.RedeemPickerateDialog;
import com.qjautofinancial.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 真实,虚拟组合赎回详情 Activity
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AssembleRedeemDetailActivity extends BaseActivity implements
        OnClickListener {
    /**
     * UI
     */
    private Button backButton;
    private TextView titleTextView;
    private RelativeLayout precentLayout;
    private TextView evaluateMoneyView;
    private TextView precentView;
    // private TextView maxMoneyView;
    private TextView allRedemView;
    private LinearLayout fundShareLayout;
    private AssembleItemLayout[] assembleItemLayout;
    private TextView nextStepBtn;
    private RedeemPickerateDialog pickerDialog;
    private TextView allRedeempInfoView;
    private ImageView redeempMoneyInfoView;
    private ImageView redeempInfoView;
    // private TextView warningView;
    /**
     * data
     */
    // private boolean isVirtual;
    private double assembleMoney;
    private long defaultPrecent;
    private RedeemModel redeemModel;
    private long finalRatio;
    /**
     * 实际赎回份额列表
     */
    private ArrayList<AssembleShares> sharesList = new ArrayList<AssembleShares>();
    private ArrayList<MyFundAssets> fundAssets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_redeem_detail_new_layout);
        initData();
        initView();
    }

    private void initView() {
        pickerDialog = new RedeemPickerateDialog(this,
                (int) redeemModel.getMinValue(), (int) defaultPrecent);
        pickerDialog.setConfirmListener(listener);
        assembleItemLayout = new AssembleItemLayout[fundAssets.size()];
        backButton = (Button) findViewById(R.id.bt_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        titleTextView.setVisibility(View.VISIBLE);
        fundShareLayout = (LinearLayout) findViewById(R.id.content_layout);
        nextStepBtn = (TextView) findViewById(R.id.next_btn_view);
        nextStepBtn.setOnClickListener(this);
        precentLayout = (RelativeLayout) findViewById(R.id.precent_layout);
        precentLayout.setOnClickListener(this);
        precentView = (TextView) findViewById(R.id.precent_view);
        evaluateMoneyView = (TextView) findViewById(R.id.yu_gu_money_view);
        // maxMoneyView = (TextView) findViewById(R.id.max_money_info_value);
        allRedemView = (TextView) findViewById(R.id.all_redem_view);
        allRedemView.setOnClickListener(this);
        allRedeempInfoView = (TextView) findViewById(R.id.all_redeemp_warning_view);
        redeempMoneyInfoView = (ImageView) findViewById(R.id.icon_msg_view);
        redeempMoneyInfoView.setOnClickListener(this);
        redeempInfoView = (ImageView) findViewById(R.id.redeem_info);
        redeempInfoView.setOnClickListener(this);

        titleTextView.setText(getString(R.string.assemble_redeem));
        precentView.setText(String.valueOf(defaultPrecent));
        // evaluateMoneyView.setText(StrUtil.formatDouble2(defaultPrecent *
        // assembleMoney));
        // maxMoneyView.setText(getString(R.string.max_money_redeem_info)
        // + StrUtil.formatMoney(String.valueOf(assembleMoney)) +
        // getString(R.string.YUAN));
        allRedeempInfoView.setVisibility(defaultPrecent == 100 ? View.VISIBLE
                : View.GONE);
        /**
         * 显示组合中的所有基金项
         */
        updateUI(defaultPrecent);
    }

    /**
     * 对话框确定按钮回调
     */
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updateUI(pickerDialog.getSelectedNumber());
            pickerDialog.dismiss();
        }
    };

    private void initData() {
        Intent intent = getIntent();
        redeemModel = (RedeemModel) intent.getSerializableExtra("redeemInfo");
        assembleMoney = redeemModel.getUsableAsset();
        if (Math.abs(redeemModel.getMaxValue() - redeemModel.getMinValue()) < 2
                || (redeemModel.getMinValue() > redeemModel.getMaxValue())) {
            defaultPrecent = 100;
        } else {
            defaultPrecent = Math
                    .round((100 + redeemModel.getMinValue()) / 2.0);
        }
        fundAssets = redeemModel.getFundAssetsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.precent_layout:
			    pickerDialog.show();
                pickerDialog.setSelectedNumber((int) finalRatio);
                break;
            case R.id.next_btn_view:
                Intent intent = new Intent(this,
                        AssembleRedeemConfirmActivity.class);
                intent.putExtra("fund_share", sharesList);
                intent.putExtra("assemble_model", redeemModel);
                intent.putExtra("sum",
                        String.valueOf(assembleMoney * finalRatio / 100.0));
                intent.putExtra("radio", String.valueOf(finalRatio));
                startActivityForResult(intent, Const.ASSEMBLE_REDEEM_FLOW);
                break;
            case R.id.all_redem_view:
                ArrayList<AssembleShares> allSharesList = new ArrayList<AssembleShares>();
                for (int i = 0; i < fundAssets.size(); i++) {
                    allSharesList.add(new AssembleShares(StringHelper
                            .formatString(fundAssets.get(i)
                                    .getUsableShares(), StringFormat.FORMATE_2), fundAssets.get(i)
                            .getAbbrev(), StringHelper.formatString(String.valueOf(Double
                            .parseDouble(fundAssets.get(i).getUsableShares())
                            * Double.parseDouble(fundAssets.get(i).getNav())),
                            StringFormat.FORMATE_2)));
                }
                Intent intentAll = new Intent(this,
                        AssembleRedeemConfirmActivity.class);
                intentAll.putExtra("fund_share", allSharesList);
                intentAll.putExtra("assemble_model", redeemModel);
                intentAll.putExtra("sum", String.valueOf(assembleMoney));
                intentAll.putExtra("radio", String.valueOf(100));
                startActivityForResult(intentAll, Const.ASSEMBLE_REDEEM_FLOW);
                break;
            case R.id.icon_msg_view:
                showHintDialog(getString(R.string.redeemp_money_info),
                        getString(R.string.redeemp_money_value),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, getString(R.string.zhi_dao_l));
                break;
            case R.id.redeem_info:
                showHintDialog(getString(R.string.redeemp_ratio_info),
                        getString(R.string.redeemp_ratio_value),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, getString(R.string.zhi_dao_l));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 赎回结果返回
        if (resultCode == ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE) {
            setResult(ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE);
            finish();
            return;
        }

        switch (requestCode) {
            case Const.ASSEMBLE_REDEEM_FLOW:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
    }

    private void updateUI(long precent) {
        finalRatio = precent;
        precentView.setText(String.valueOf(precent));
        if (redeemModel.isVirtual()) {
            evaluateMoneyView.setText(StringHelper.formatString(
                    String.valueOf(precent * assembleMoney / 100.0),
                    StringFormat.FORMATE_1) + getString(R.string.YUAN));
        } else {
            evaluateMoneyView
                    .setText(StringHelper.formatString(
                            String.valueOf(precent * redeemModel.getMinRange()
                                    / 100.0), StringFormat.FORMATE_1)
                            + getString(R.string.yu_deng_yu)
                            + StringHelper.formatString(
                                    String.valueOf(precent
                                            * redeemModel.getMaxRange() / 100.0),
                                    StringFormat.FORMATE_1)
                            + getString(R.string.YUAN));
        }
        fundShareLayout.removeAllViews();
        sharesList.clear();
        for (int i = 0; i < redeemModel.getFundAssetsList().size(); i++) {
            assembleItemLayout[i] = new AssembleItemLayout(this);
            assembleItemLayout[i].initData(fundAssets.get(i).getAbbrev(),
                    StringHelper.formatString(fundAssets.get(i)
                            .getUsableShares(), StringFormat.FORMATE_2),
                    StringHelper.formatString(String.valueOf(Double
                            .parseDouble(fundAssets.get(i).getUsableShares())
                            * precent / 100.0), StringFormat.FORMATE_2));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            assembleItemLayout[i].setLayoutParams(params);
            fundShareLayout.addView(assembleItemLayout[i]);
            /**
             * 将修改后的数据保存
             */
            sharesList.add(new AssembleShares(StringHelper.formatString(
                    String.valueOf(Double
                            .parseDouble(fundAssets.get(i).getUsableShares())
                            * precent
                            / 100.0), StringFormat.FORMATE_2), fundAssets.get(i).getAbbrev(),
                    StringHelper
                            .formatString(
                                    String.valueOf(Double.parseDouble(fundAssets.get(i)
                                            .getUsableShares())
                                            * precent
                                            / 100.0
                                            * Double.parseDouble(fundAssets.get(i).getNav())),
                                    StringFormat.FORMATE_2)));
        }
    }

}
