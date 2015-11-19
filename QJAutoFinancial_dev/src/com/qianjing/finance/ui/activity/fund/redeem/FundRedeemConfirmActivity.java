
package com.qianjing.finance.ui.activity.fund.redeem;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.buy.FundResultActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * @description 基金赎回确认页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class FundRedeemConfirmActivity extends BaseActivity implements OnClickListener {
    /**
     * UI
     */
    private TextView fundNameTextView;
    private TextView redeemTextView;
    private TextView destationCardNumberTextView;
    private TextView juEShunYanTextView;
    private Button confirmButton;
    private TextView titleTextView;
    private Button backButton;

    /**
     * data
     */
    private Card card;
    private String buyAccount;
    private String fdCode;
    private String fdName;
    private boolean isShunYan;
    private String fee = "0.00";
    private String money = "0.00";
    /**
     * 操作相关变量
     */
    private String opDateShow;
    private String profitTimeShow;
    private String profitArriveShow;
    private String reason;
    private InputDialog.Builder inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_back_confirm_layout);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        buyAccount = intent.getStringExtra("buy_account");
        card = (Card) intent.getParcelableExtra("card");
        fdCode = intent.getStringExtra("fdcode");
        fdName = intent.getStringExtra("fdname");
        isShunYan = intent.getBooleanExtra("isShunYan", false);
    }

    private void initView() {
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        backButton = (Button) findViewById(R.id.bt_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        fundNameTextView = (TextView) findViewById(R.id.fund_name);
        redeemTextView = (TextView) findViewById(R.id.shu_hui_fen_e);
        destationCardNumberTextView = (TextView) findViewById(R.id.dao_zhang_ying_hang_ka);
        juEShunYanTextView = (TextView) findViewById(R.id.shu_hui_shun_yan);
        confirmButton = (Button) findViewById(R.id.btn_buy_next);
        confirmButton.setOnClickListener(this);

        titleTextView.setText(getString(R.string.shu_hui_confirm));
        fundNameTextView.setText(fdName);
        redeemTextView.setText(buyAccount + getString(R.string.fen));
        destationCardNumberTextView.setText(card.getBankName()
                + getString(R.string.left_brackets)
                + getString(R.string.wei_hao)
                + card.getNumber().substring(card.getNumber().length() - 4,
                        card.getNumber().length())
                + getString(R.string.right_brackets));
        juEShunYanTextView.setText(isShunYan
                ? getString(R.string.shun_yan)
                : getString(R.string.bu_shun_yan));
    }

    private void requestRedeem(String password) {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", card.getNumber());
        upload.put("shares", buyAccount);
        upload.put("password", password);
        upload.put("fund_code", fdCode);
        AnsynHttpRequest.requestByPost(this, UrlConst.FUND_REDEEM,
                redeemCallback, upload);
    }

    private HttpCallBack redeemCallback = new HttpCallBack() {
        @Override
        public void back(String data, int status) {
            if (data != null && !data.equals("")) {
                mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
            } else {
                dismissLoading();
                showHintDialog("网络不给力，请重试!");
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    handleRedeem(msg.obj.toString());
                    break;
            }
        };
    };

    private void handleRedeem(String data) {
        dismissLoading();
        try {
            JSONObject object = new JSONObject(data);
            int ecode = object.optInt("ecode");
            reason = object.optString("emsg");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    JSONObject result = object.optJSONObject("data");
                    opDateShow = result.optString("opDate");
                    profitTimeShow = result.optString("profit_time");
                    profitArriveShow = result.optString("profit_arrive");
                    fee = result.optString("estimate_fee");
                    money = result.optString("estimate_sum");
                    goResultPage();
                    break;
                default:
                    showHintDialog(reason);
                    break;
            }
        } catch (Exception e) {
            // 不跳转
            dismissLoading();
            showHintDialog("数据解析异常!");
        }
    }

    private void goResultPage() {
        Intent intent = new Intent(this, FundResultActivity.class);
        intent.putExtra("opDateShow", opDateShow);
        intent.putExtra("profitTimeShow", profitTimeShow);
        intent.putExtra("profitArriveShow", profitArriveShow);
        intent.putExtra("type", Const.FUND_REDEEM_SUCCESS);
        intent.putExtra("card", card);
        intent.putExtra("fee", StringHelper.formatString(fee, StringFormat.FORMATE_1));
        intent.putExtra("money", StringHelper.formatString(money, StringFormat.FORMATE_1));
        startActivityForResult(intent, Const.FUND_REDEEM_FLOE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.FUND_REDEEM_FLOE:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.btn_buy_next:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle("赎回" + buyAccount + getString(R.string.fen));
        inputDialog.setNagetiveButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = inputDialog.getEditText().getText()
                                .toString();
                        if (StringHelper.isBlank(text)) {
                            Toast.makeText(FundRedeemConfirmActivity.this,
                                    "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            requestRedeem(text);
                        }

                        dialog.dismiss();
                    }
                });
        inputDialog.create().show();
    }
}
