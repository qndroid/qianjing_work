
package com.qianjing.finance.ui.activity.wallet;

import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 活期宝取现页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class WalletCashActivity extends BaseActivity implements OnClickListener {
    /**
     * 常量
     */
    private static final int TYPE_CARD_LIST = 100;
    private static final int TYPE_CASH_FAST = 102;
    private static final int TYPE_CASH_COMMON = 103;
    private static final int FAST_CASH_SUCCESS = 0x01;
    private static final int COMMON_CASH_SUCCESS = 0x02;
    private static final float MIN_CASH_CALUE = 1.0f;
    /**
     * UI
     */
    private Button backBtn;
    private TextView titleTextView;
    private Button fastCashBtn;
    private Button commonCashBtn;
    private ScrollView cashLayout;
    private EditText moneyEditText;
    private TextView redeemCountView;
    private ImageView iconBankImageView;
    private TextView nameBankTextView;
    private TextView numBankTextView;
    private ImageView rightArrowView;
    private TextView alertTextView;
    private ImageView helpView;
    private LinearLayout selectedBankLayout;

    /**
     * 结果页UI
     */
    private TextView opterationTitleView;
    private TextView opDateTimeView;
    private TextView profitTimeView;
    private TextView profitArriveView;
    private TextView bankNameView;
    private TextView confirmTimeWeekView;
    private TextView finalTimeWeekView;
    private TextView confirmMsgView;
    private TextView finalMsgView;
    private TextView takeMoneyView;
    private TextView takeMoneyValueView;
    private TextView finishView;
    private ImageView confirmImageView;
    private ImageView finalImageView;
    /**
     * data
     */
    private int isQuickRedeemp;// 是否可以快速赎回
    private ArrayList<Card> listCard = new ArrayList<Card>();
    private Card currentCard;
    private boolean isQuickFlag = false;
    private boolean isCommonFlag = false;
    private boolean inputMoneyFlag = false;
    private TypedArray bankImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UMengStatics.onCurrentRedeemPage1View(this);
        setContentView(R.layout.activity_wallet_cash);
        initView();
        // 加载绑卡数据
        requestCardList();
    }

    private void initView() {
        setLoadingUncancelable();
        backBtn = (Button) findViewById(R.id.bt_back);
        backBtn.setVisibility(View.VISIBLE);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        titleTextView.setText(R.string.str_fromwallet);
        helpView = (ImageView) findViewById(R.id.im_help);
        helpView.setVisibility(View.VISIBLE);

        alertTextView = (TextView) findViewById(R.id.tv_alerttext);
        rightArrowView = (ImageView) findViewById(R.id.im_rightarrow);
        iconBankImageView = (ImageView) findViewById(R.id.iv_image);
        nameBankTextView = (TextView) findViewById(R.id.tv_bankname);
        numBankTextView = (TextView) findViewById(R.id.tv_bankcard);
        redeemCountView = (TextView) findViewById(R.id.tv_redemption_count);
        cashLayout = (ScrollView) findViewById(R.id.sc_main);
        selectedBankLayout = (LinearLayout) findViewById(R.id.ll_selectedbank);
        fastCashBtn = (Button) findViewById(R.id.bt_fastcash);
        commonCashBtn = (Button) findViewById(R.id.bt_commoncash);
        fastCashBtn.setOnClickListener(this);
        commonCashBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        helpView.setOnClickListener(this);
        selectedBankLayout.setOnClickListener(this);

        moneyEditText = (EditText) findViewById(R.id.edit_money);
        moneyEditText.addTextChangedListener(watcher);

        bankImage = getResources().obtainTypedArray(R.array.bank_image);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = moneyEditText.getText().toString();
            if (!text.equals("")) {
                if (text.equals(".")) {
                    moneyEditText.setText("");
                    return;
                }

                double textMoney = Double.parseDouble(text);
                if (textMoney < MIN_CASH_CALUE) {
                    inputMoneyFlag = false;
                    alertTextView.setText(getString(R.string.min_chash_value));
                    decideCanRedeem();
                    return;
                }
                if (textMoney <= currentCard.getAssetsUsable()) {
                    double leftMoney = currentCard.getAssetsUsable()
                            - textMoney;
                    if (leftMoney == 0) {
                        // 可以赎回
                        inputMoneyFlag = true;
                        alertTextView.setText("");
                        decideCanRedeem();
                        return;
                    }
                    if (leftMoney >= 1) {
                        // ke yi shuhui
                        inputMoneyFlag = true;
                        alertTextView.setText("");
                        decideCanRedeem();
                    } else {
                        // bu ke shuhui
                        inputMoneyFlag = false;
                        alertTextView.setText(getString(R.string.get_money));
                        decideCanRedeem();
                    }
                    return;
                }

                if (textMoney > currentCard.getAssetsUsable()) {
                    inputMoneyFlag = false;
                    alertTextView.setText(getString(R.string.max_crash_money)
                            + currentCard.getAssetsUsable()
                            + getString(R.string.YUAN));
                    decideCanRedeem();
                    return;
                }
                if (textMoney > currentCard.getLimitRedeem()) {
                    inputMoneyFlag = false;
                    alertTextView
                            .setText(getString(R.string.max_redemp_limit_money)
                                    + currentCard.getLimitRedeem()
                                    + getString(R.string.YUAN));
                    decideCanRedeem();
                    return;
                }
            } else {
                inputMoneyFlag = false;
                decideCanRedeem();
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            dismissLoading();
            String strJson = (String) msg.obj;
            if (strJson == null || "".equals(strJson)) {
                showHintDialog("网络不给力！");
                return;
            }

            switch (msg.what) {
                case TYPE_CARD_LIST:
                    handCardListJson(msg.obj.toString());
                    break;
                case TYPE_CASH_FAST:
                    handFastCashJson(msg.obj.toString());
                    break;
                case TYPE_CASH_COMMON:
                    handCommonCashJson(msg.obj.toString());
                    break;
            }
        }
    };

    private void requestCardList() {
        showLoading();
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                TYPE_CARD_LIST, data));
                    }
                }, null);
    }

    // 处理活期宝快速取现回复数据
    private void handFastCashJson(String strJson) {
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            JSONObject data = object.optJSONObject("data");
            if (ecode == 0) {
                UMengStatics.onCurrentRedeemPage3View(this);
                // 快速取现成功
                JSONObject trade_log = data.optJSONObject("trade_log");
                showResultPage(FAST_CASH_SUCCESS, trade_log);
            } else {
                showHintDialog(emsg);
            }

        } catch (JSONException e) {
            showToast("数据解析错误");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }
    }

    // 处理活期宝普通取现回复数据
    private void handCommonCashJson(String strJson) {
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            JSONObject data = object.optJSONObject("data");
            if (ecode == 0) {
                UMengStatics.onCurrentRedeemPage3View(this);
                // 普通取现成功
                JSONObject trade_log = data.optJSONObject("trade_log");
                showResultPage(COMMON_CASH_SUCCESS, trade_log);
            } else {
                showHintDialog(emsg);
            }

        } catch (JSONException e) {
            showToast("数据解析错误");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }

    }

    // 处理银行卡列表回复数据
    private void handCardListJson(String jsonStr) {
        dismissLoading();
        if (jsonStr == null || "".equals(jsonStr)) {
            showToast("网络不给力");
            return;
        }
        try {
            JSONObject object = new JSONObject(jsonStr);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            String data = object.optString("data");
            if (ecode == 0) {
                JSONObject objData = new JSONObject(data);
                // 解析已绑定银行卡数据
                JSONArray arrayCard = objData.optJSONArray("assets_list");

                isQuickRedeemp = objData.optInt("is_quick_redemp");
                for (int i = 0; i < arrayCard.length(); i++) {
                    JSONObject jsonOb = (JSONObject) arrayCard.opt(i);
                    String bank = jsonOb.optString("bank_name");
                    String card = jsonOb.optString("card");
                    int redemp_num = jsonOb.optInt("redemp_num");
                    double redemp_limit = jsonOb.optDouble("redemp_limit");

                    double usable_assets = jsonOb.optDouble("usable_assets");
                    double assets = jsonOb.optDouble("assets");
                    Card info = new Card();
                    info.setLimitRedeem(redemp_limit);
                    info.setNumber(card);
                    info.setBankName(bank);
                    info.setRedeemNumber(redemp_num);
                    info.setAssets(assets);
                    info.setAssetsUsable(usable_assets);
                    listCard.add(info);
                }
                updateUI(listCard.get(0));
            } else {
                showHintDialog(emsg);
            }

        } catch (JSONException e) {
            showToast("数据解析错误");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
        }
    }

    /*
     * 处理快速取现点击事件
     */
    private void handleClickFastCash() {
        if (StringHelper.isBlank(moneyEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.input_all_msg),
                    Toast.LENGTH_LONG).show();
            return;
        }
        final InputDialog.Builder inputDialog = new InputDialog.Builder(this,
                null);
        inputDialog.setTitle(getString(R.string.input_pwd));
        inputDialog.setNagetiveButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        inputDialog.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = inputDialog.getEditText().getText()
                                .toString();
                        if (StringHelper.isBlank(text)) {
                            Toast.makeText(WalletCashActivity.this,
                                    getString(R.string.pwd_no_empty),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            requesFastCash(text);
                        }

                        dialog.dismiss();
                    }
                });
        inputDialog.create().show();

    }

    private void requesFastCash(String pass) {
        showLoading();
        /**
         * 活期宝取现 card 银行卡 sum 取现金额 password 交易密码 type 取现类型 2快速取现，3普通取现
         */
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", currentCard.getNumber());
        upload.put("sum", moneyEditText.getText());
        upload.put("password", pass);
        upload.put("type", "2");
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CASH,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                TYPE_CASH_FAST, data));
                    }
                }, upload);
    }

    /*
     * 处理普通取现点击事件
     */
    private void handleClickCommonCash() {
        if (StringHelper.isBlank(moneyEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.input_all_msg),
                    Toast.LENGTH_LONG).show();
            return;
        }
        final InputDialog.Builder inputDialog = new InputDialog.Builder(this,
                null);
        inputDialog.setTitle(getString(R.string.input_pwd));
        inputDialog.setNagetiveButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        inputDialog.setPositiveButton(getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = inputDialog.getEditText().getText()
                                .toString();
                        if (StringHelper.isBlank(text)) {
                            Toast.makeText(WalletCashActivity.this,
                                    getString(R.string.pwd_no_empty),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            requestCommonCash(text);
                        }

                        dialog.dismiss();
                    }
                });
        inputDialog.create().show();
    }

    private void requestCommonCash(String pass) {
        showLoading();
        /**
         * 活期宝取现 card 银行卡 sum 取现金额 password 交易密码 type 取现类型 2快速取现，3普通取现
         */
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", currentCard.getNumber());
        upload.put("sum", moneyEditText.getText());
        upload.put("password", pass);
        upload.put("type", "3");
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CASH,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                TYPE_CASH_COMMON, data));
                    }
                }, upload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_title_view:
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_fastcash:
                UMengStatics.onCurrentRedeemPage2View(this);
                handleClickFastCash();
                break;
            case R.id.bt_commoncash:
                UMengStatics.onCurrentRedeemPage2View(this);
                handleClickCommonCash();
                break;

            case R.id.ll_selectedbank:

                if (listCard != null && listCard.size() == 1) {
                    return;
                }
                Intent intent = new Intent(this, CardListActivity.class);
                intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listCard);
                intent.putExtra(Flag.EXTRA_BEAN_CARD_CURRENT, currentCard);
                startActivityForResult(intent, Const.FUND_SELECT_BANK);
                break;
            case R.id.im_help:// 帮助
                Bundle bundle3 = new Bundle();
                bundle3.putInt("webType", 3);
                openActivity(WebActivity.class, bundle3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.FUND_SELECT_BANK:
                if (resultCode == RESULT_OK) {
                    Card card = (Card) data
                            .getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);
                    updateUI(card);
                }
                break;
        }
    }

    private void updateUI(Card card) {
        resetState();
        currentCard = card;
        if (listCard != null && listCard.size() <= 1) {
            rightArrowView.setVisibility(View.GONE);
        } else {
            rightArrowView.setVisibility(View.VISIBLE);
        }
        /**
         * 工作日，服务器返回不可快赎
         */
        if (isQuickRedeemp == 0) {
            redeemCountView.setText("0");
            isQuickFlag = false;
        } else {
            if (card.getRedeemNumber() == 0) {
                redeemCountView.setText("0");
                isQuickFlag = false;
            } else {
                redeemCountView.setText(String.valueOf(card.getRedeemNumber()));
                isQuickFlag = true;
            }
        }
        iconBankImageView.setBackgroundResource(bankImage.getResourceId(
                getBankImage(card.getBankName()), -1));
        nameBankTextView.setText(card.getBankName());
        numBankTextView.setText(StringHelper.hintCardNum(card.getNumber()));

        if (currentCard.getAssetsUsable() <= 0) {
            isQuickFlag = false;
            isCommonFlag = false;
            moneyEditText.setHint(getString(R.string.redeem_empty));
        } else {
            isCommonFlag = true;
            if (currentCard.getAssetsUsable() < currentCard.getLimitRedeem()) {
                moneyEditText.setHint(getString(R.string.redeem_max)
                        + StringHelper.formatString(
                                String.valueOf(currentCard.getAssetsUsable()),
                                StringFormat.FORMATE_2)
                        + getString(R.string.YUAN));
            } else {
                moneyEditText.setHint(getString(R.string.redeem_max)
                        + StringHelper.formatString(
                                String.valueOf(currentCard.getLimitRedeem()),
                                StringFormat.FORMATE_2)
                        + getString(R.string.YUAN));
            }
        }
    }

    /**
     * 充值状态
     */
    private void resetState() {
        fastCashBtn.setEnabled(false);
        commonCashBtn.setEnabled(false);
        isQuickFlag = false;
        isCommonFlag = false;
        inputMoneyFlag = false;
        moneyEditText.setText("");
        alertTextView.setText("");
    }

    /**
     * 决定按钮是否可用
     */
    private void decideCanRedeem() {
        if (isQuickFlag && inputMoneyFlag) {
            fastCashBtn.setEnabled(true);
        } else {
            fastCashBtn.setEnabled(false);
        }

        if (isCommonFlag && inputMoneyFlag) {
            commonCashBtn.setEnabled(true);
        } else {
            commonCashBtn.setEnabled(false);
        }
    }

    /**
     * 显示结果页
     * 
     * @param resultType
     * @param data
     */
    private void showResultPage(int resultType, JSONObject data) {
        cashLayout.setVisibility(View.GONE);
        backBtn.setVisibility(View.GONE);
        helpView.setVisibility(View.GONE);
        titleTextView.setText(getString(R.string.title_result));

        ViewStub stubLayout = (ViewStub) findViewById(R.id.include_ok);
        stubLayout.inflate();
        opDateTimeView = (TextView) findViewById(R.id.commit_time_view);
        profitTimeView = (TextView) findViewById(R.id.confirm_time_view);
        profitArriveView = (TextView) findViewById(R.id.final_time_view);
        bankNameView = (TextView) findViewById(R.id.bank_name_view);
        confirmTimeWeekView = (TextView) findViewById(R.id.confirm_time_value_view);
        finalTimeWeekView = (TextView) findViewById(R.id.final_time_value_view);
        confirmMsgView = (TextView) findViewById(R.id.confirm_time_msg_view);
        finalMsgView = (TextView) findViewById(R.id.final_time_msg_view);
        takeMoneyView = (TextView) findViewById(R.id.deduct_money_view);
        takeMoneyValueView = (TextView) findViewById(R.id.deduct_money_value_view);
        opterationTitleView = (TextView) findViewById(R.id.opreate_title_view);
        finishView = (TextView) findViewById(R.id.right_title_view);
        finishView.setVisibility(View.VISIBLE);
        finishView.setOnClickListener(this);
        confirmImageView = (ImageView) findViewById(R.id.second_part);
        finalImageView = (ImageView) findViewById(R.id.third_part);

        finalMsgView.setCompoundDrawables(null, null, null, null);
        confirmMsgView.setCompoundDrawables(null, null, null, null);
        opDateTimeView.setText(DateFormatHelper.formatDate(data
                .optString("opDate").concat("000"), DateFormat.DATE_9));
        bankNameView.setText(currentCard.getBankName()
                + getString(R.string.left_brackets)
                + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(currentCard.getNumber())
                + getString(R.string.right_brackets));
        takeMoneyValueView.setText(getString(R.string.ren_ming_bi)
                + StringHelper.formatString(moneyEditText.getText().toString(),
                        StringFormat.FORMATE_1));
        switch (resultType) {
            case FAST_CASH_SUCCESS:
                opterationTitleView
                        .setText(getString(R.string.quick_take_money));
                confirmMsgView.setText(getString(R.string.fast_request_handle));
                finalMsgView.setText(getString(R.string.fast_request_time));
                takeMoneyView.setText(getString(R.string.take_money_mount));
                confirmImageView
                        .setBackgroundResource(R.drawable.icon_fast_cash_second);
                finalImageView
                        .setBackgroundResource(R.drawable.icon_fast_cash_third);
                break;
            case COMMON_CASH_SUCCESS:
                profitTimeView.setText(DateFormatHelper.formatDate(
                        data.optString("profit_time").concat("000"),
                        DateFormat.DATE_1));
                confirmTimeWeekView.setText(DateFormatHelper
                        .formatDate(data.optString("profit_time")
                                .concat("000"), DateFormat.DATE_6));
                confirmMsgView
                        .setText(getString(R.string.fund_company_confirm));
                profitArriveView.setText(DateFormatHelper
                        .formatDate(
                                data.optString("profit_arrive").concat("000"),
                                DateFormat.DATE_1));
                finalTimeWeekView.setText(DateFormatHelper
                        .formatDate(
                                data.optString("profit_arrive").concat("000"),
                                DateFormat.DATE_6));
                finalMsgView.setText(getString(R.string.money_daoda));
                takeMoneyView.setText(getString(R.string.take_money_mount));
                break;
        }
    }

    private int getBankImage(String bankName) {
        String[] mBankNameArray = getResources().getStringArray(
                R.array.bank_name);
        for (int i = 0; i < mBankNameArray.length; i++) {
            if (mBankNameArray[i].equals(bankName)) {
                return i;
            }
        }
        return mBankNameArray.length;
    }
}
