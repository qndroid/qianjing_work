
package com.qianjing.finance.ui.activity.fund.buy;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.ui.activity.fund.redeem.FundRedeemConfirmActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.Util;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 基金申购页面
 */
public class FundBuyActivity extends BaseActivity implements OnClickListener {
    /**
     * UI
     */
    private ImageView bankIconImageView;
    private TextView bankNameTextView;
    private TextView cardNumberTextView;
    private TextView oneMaxMoneyTextView;
    private LinearLayout zhiFuShangXianLayout;
    private LinearLayout tipLayout;
    private TextView alterTextView;
    private EditText inputMoneyeditText;
    private Button confirmBuybutton;
    private LinearLayout selectcardLayout;
    private Button backButton;
    private TextView titleTextView;
    private TextView moneyTitleTextView;
    private TextView shuHuiAllView;
    private RelativeLayout juEShuHuiLayout;
    private CheckBox juEShunYanCheckBox;
    private ImageView juEShuHuiTipView;
    private ImageView cardArrowView;
    /**
     * data
     */
    private HashMap<String, String> cardAssetMap;
    private ArrayList<Card> listCard;
    private boolean isLegal;
    private String maxMoney;
    private String minScript;
    private Card currentCard;
    private String fdCode;
    private String fdName;
    private String fdType;
    private boolean isShuHui;
    private String minHold; // 可赎回份额
    private String minRedeemp; // 最小起赎
    private double nav; // 净值
    private double rate; // 手续费率
    private boolean allShuHui;
    private TypedArray bankImage;
    private String fee; // 单只基金申购费率

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_buy_layout);
        initData();
        initView();
        requestCardlist();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Intent intent = getIntent();
        cardAssetMap = (HashMap<String, String>) intent
                .getSerializableExtra("card_asset");
        minScript = intent.getStringExtra("min_script"); // 最小保留份额
        minRedeemp = intent.getStringExtra("min_redeem"); // 最低赎回
        fdCode = intent.getStringExtra("fdcode");
        fdName = intent.getStringExtra("fdname");
        fdType = intent.getStringExtra("fdtype");
        isShuHui = intent.getBooleanExtra("isShuHui", false);
        nav = intent.getDoubleExtra("pure_value", -1);
        fee = intent.getStringExtra("fee");
        bankImage = getResources().obtainTypedArray(R.array.bank_image);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        bankIconImageView = (ImageView) findViewById(R.id.iv_image);
        bankNameTextView = (TextView) findViewById(R.id.tv_bankname);
        cardNumberTextView = (TextView) findViewById(R.id.tv_bankcard);
        oneMaxMoneyTextView = (TextView) findViewById(R.id.tv_single_limit);
        inputMoneyeditText = (EditText) findViewById(R.id.edit_money);
        inputMoneyeditText.addTextChangedListener(watcher);
        zhiFuShangXianLayout = (LinearLayout) findViewById(R.id.zhi_fu_shang_xian);
        tipLayout = (LinearLayout) findViewById(R.id.tip_layout);
        confirmBuybutton = (Button) findViewById(R.id.btn_recharge);
        confirmBuybutton.setOnClickListener(this);
        confirmBuybutton.setEnabled(false);
        alterTextView = (TextView) findViewById(R.id.tv_alerttext);
        selectcardLayout = (LinearLayout) findViewById(R.id.ll_selectedbank);
        selectcardLayout.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.bt_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(getString(R.string.fund_buy));
        moneyTitleTextView = (TextView) findViewById(R.id.money_title);
        shuHuiAllView = (TextView) findViewById(R.id.shu_hui_all);
        shuHuiAllView.setOnClickListener(this);
        juEShuHuiLayout = (RelativeLayout) findViewById(R.id.ju_e_layout);
        juEShunYanCheckBox = (CheckBox) findViewById(R.id.ju_e_check_box);
        juEShuHuiTipView = (ImageView) findViewById(R.id.shu_hui_msg_view);
        juEShuHuiTipView.setOnClickListener(this);
        cardArrowView = (ImageView) findViewById(R.id.im_rightarrow);

        inputMoneyeditText.setHint(getString(R.string.fund_min_money)
                + minScript + getString(R.string.YUAN));
        if (isShuHui) {
            zhiFuShangXianLayout.setVisibility(View.INVISIBLE);
            tipLayout.setVisibility(View.INVISIBLE);
            shuHuiAllView.setVisibility(View.VISIBLE);
            titleTextView.setText(getString(R.string.jin_ji_shu_hui));
            moneyTitleTextView.setText(getString(R.string.fen_e));
            juEShuHuiLayout.setVisibility(View.VISIBLE);
        }

        listCard = new ArrayList<Card>();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (inputMoneyeditText.getText().toString() != null
                    && !inputMoneyeditText.getText().toString().equals("")
                    && !inputMoneyeditText.getText().toString().equals(".")) {
                if (isShuHui) {
                    if (Double.parseDouble(inputMoneyeditText.getText()
                            .toString()) > Double.parseDouble(minHold)) {
                        alterTextView.setTextColor(getResources().getColor(
                                R.color.orange_alerttext));
                        showMessage(getString(R.string.shu_hui_fen_e_chao_xian)
                                + minHold + getString(R.string.fen));
                        return;
                    }

                    /*
                     * 如果是全部赎回
                     */
                    if (allShuHui) {
                        if (Double.parseDouble(inputMoneyeditText.getText()
                                .toString()) > 0) {
                            alterTextView.setVisibility(View.VISIBLE);
                            alterTextView.setTextColor(getResources().getColor(
                                    R.color.color_5a79b7));
                            if (fdType.equals("3")) {
                                alterTextView
                                        .setText(getString(R.string.yu_ji_dao_zhang)
                                                + getForwardPrice()
                                                + getString(R.string.YUAN));
                            } else {
                                alterTextView
                                        .setText(getString(R.string.yu_ji_dao_zhang)
                                                + getForwardPrice()
                                                + getString(R.string.yu_deng_yu)
                                                + getMaxForwardPrice()
                                                + getString(R.string.YUAN));
                            }
                            confirmBuybutton.setEnabled(true);
                            isLegal = true;
                        }
                        return;
                    }

                    if (Double.parseDouble(inputMoneyeditText.getText()
                            .toString()) == Double.parseDouble(minHold)) {
                        alterTextView.setVisibility(View.VISIBLE);
                        alterTextView
                                .setText(getString(R.string.yu_ji_dao_zhang)
                                        + getForwardPrice()
                                        + getString(R.string.YUAN));
                        confirmBuybutton.setEnabled(true);
                        isLegal = true;
                        allShuHui = false;
                        return;
                    }

                    if ((Double.parseDouble(minHold) - Double
                            .parseDouble(inputMoneyeditText.getText()
                                    .toString())) < Double
                            .parseDouble(minScript)) {
                        alterTextView.setTextColor(getResources().getColor(
                                R.color.orange_alerttext));
                        showMessage(getString(R.string.di_yu_xui_xiao_shu_hui_fen_e)
                                + minScript + getString(R.string.fen));
                        return;
                    }
                    if (Double.parseDouble(inputMoneyeditText.getText()
                            .toString()) < Double.parseDouble(minRedeemp)) {
                        alterTextView.setTextColor(getResources().getColor(
                                R.color.orange_alerttext));
                        showMessage(getString(R.string.shu_hui_fen_e_bu_de_di_yu)
                                + minRedeemp + getString(R.string.fen));
                        return;

                    } else {
                        alterTextView.setVisibility(View.VISIBLE);
                        alterTextView.setTextColor(getResources().getColor(
                                R.color.color_5a79b7));
                        if (fdType.equals("3")) {
                            alterTextView
                                    .setText(getString(R.string.yu_ji_dao_zhang)
                                            + getForwardPrice()
                                            + getString(R.string.YUAN));
                        } else {
                            alterTextView
                                    .setText(getString(R.string.yu_ji_dao_zhang)
                                            + getForwardPrice()
                                            + getString(R.string.yu_deng_yu)
                                            + getMaxForwardPrice()
                                            + getString(R.string.YUAN));
                        }
                        confirmBuybutton.setEnabled(true);
                        isLegal = true;
                    }

                } else {
                    alterTextView.setTextColor(getResources().getColor(
                            R.color.orange_alerttext));
                    if (Double.parseDouble(inputMoneyeditText.getText()
                            .toString()) > Double.parseDouble(maxMoney)) {
                        showMessage(getString(R.string.shen_gou_beyond));
                        return;
                    }

                    if (Double.parseDouble(inputMoneyeditText.getText()
                            .toString()) < Double.parseDouble(minScript)) {
                        showMessage(getString(R.string.xiao_yu_qi_gou_jin_e)
                                + minScript + getString(R.string.YUAN));
                        return;
                    } else {
                        if (!fdType.equals("3")) {
                            alterTextView.setVisibility(View.VISIBLE);
                            alterTextView.setTextColor(getResources().getColor(
                                    R.color.color_5a79b7));
                            alterTextView
                                    .setText(getString(R.string.redeemp_evaluate_fee)
                                            + getFundFee()
                                            + getString(R.string.YUAN));
                        } else {
                            alterTextView.setVisibility(View.INVISIBLE);
                        }
                        confirmBuybutton.setEnabled(true);
                        isLegal = true;
                    }
                }
            } else {
                alterTextView.setVisibility(View.INVISIBLE);
                confirmBuybutton.setEnabled(false);
                isLegal = false;
            }
        }
    };

    private String getForwardPrice() {
        rate = fdType.equals("3") ? 1 : Const.FUND_TYPE_MONET
                * Const.FUND_TYPE_MIN_RATE;
        return StringHelper.formatString(String.valueOf(nav
                * Double.parseDouble(inputMoneyeditText.getText().toString())
                * rate), StringFormat.FORMATE_1);
    }

    private String getMaxForwardPrice() {
        rate = Const.FUND_TYPE_MAX_RATE;
        return StringHelper.formatString(String.valueOf(nav
                * Double.parseDouble(inputMoneyeditText.getText().toString())
                * rate), StringFormat.FORMATE_1);
    }

    private String getFundFee() {
        double tempFee = Double.parseDouble(fee);
        return StringHelper.formatString(String.valueOf(Double
                .parseDouble(inputMoneyeditText.getText().toString())
                * tempFee
                / (100 + tempFee)), StringFormat.FORMATE_1);
    }

    private void showMessage(String msg) {
        alterTextView.setVisibility(View.VISIBLE);
        alterTextView.setText(msg);
        confirmBuybutton.setEnabled(false);
        isLegal = false;
    }

    private void requestCardlist() {
        // 银行卡信息处理
        showLoading();
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD_LIST,
                cardListHandler, null);
    }

    private HttpCallBack cardListHandler = new HttpCallBack() {
        @Override
        public void back(String data, int type) {
            dismissLoading();
            ResponseBase responseBase = ResponseManager.getInstance().parse(
                    data);
            if (responseBase != null) {
                if (responseBase.ecode == ErrorConst.EC_OK
                        || StringHelper.isNotBlank(responseBase.strError)) {
                    JSONObject objData = responseBase.jsonObject;
                    /**
                     * upateUI
                     */
                    mHandler.sendMessage(mHandler.obtainMessage(0x01, objData));
                } else {
                    finish();
                    return;
                }
            } else {
                finish();
                showHintDialog("网络不给力");
                return;
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    handleCardList(msg.obj);
                    break;
            }
        }
    };

    private void handleCardList(Object obj) {

        JSONObject object = (JSONObject) obj;
        // 解析已绑定银行卡数据
        try {
            JSONArray arrayCard = object.getJSONArray("cards");

            for (int i = 0; i < arrayCard.length(); i++) {
                JSONObject jsonOb = (JSONObject) arrayCard.opt(i);
                String bank = jsonOb.optString("bank");
                String card = jsonOb.optString("card");
                double single_limit = jsonOb.optDouble("single_limit");
                double daily_limit = jsonOb.optDouble("daily_limit");
                Card info = new Card();
                info.setNumber(card);
                info.setBankName(bank);
                info.setLimitRecharge(single_limit);
                info.setLimitDailyRecharge(daily_limit);
                if (isShuHui) {
                    if (cardAssetMap.get(info.getNumber()) != null) {
                        listCard.add(info);
                    }
                } else {
                    listCard.add(info);
                }
            }
            updateUI(listCard.get(0));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(Card card) {
        inputMoneyeditText.setText("");
        currentCard = card;
        if (listCard != null && listCard.size() == 1) {
            cardArrowView.setVisibility(View.GONE);
        }
        bankIconImageView.setImageResource(bankImage.getResourceId(
                getBankImage(currentCard.getBankName()), -1));
        bankNameTextView.setText(currentCard.getBankName());
        cardNumberTextView
                .setText(StringHelper.hintCardNum(currentCard.getNumber()));
        maxMoney = StringHelper.formatString(
                String.valueOf(currentCard.getLimitRecharge()), StringFormat.FORMATE_2);
        oneMaxMoneyTextView.setText(maxMoney);

        if (isShuHui) {
            minHold = cardAssetMap.get(currentCard.getNumber());
            if (minHold != null && !minHold.equals("")
                    && Double.parseDouble(minHold) > 0) {
                inputMoneyeditText
                        .setHint(getString(R.string.zui_duo_ke_shu_hui_fen_e)
                                + minHold + getString(R.string.fen));
                shuHuiAllView.setEnabled(true);
            } else {
                inputMoneyeditText.setText("");
                alterTextView.setVisibility(View.INVISIBLE);
                inputMoneyeditText
                        .setHint(getString(R.string.mei_you_ke_shu_hui_fen_e));
                confirmBuybutton.setEnabled(false);
                shuHuiAllView.setEnabled(false);
            }
        } else {
            if (inputMoneyeditText.getText().toString() != null
                    && !inputMoneyeditText.getText().toString().equals("")) {
                alterTextView.setTextColor(getResources().getColor(
                        R.color.orange_alerttext));
                if (Double.parseDouble(inputMoneyeditText.getText().toString()) > currentCard
                        .getLimitRecharge()) {
                    showMessage(getString(R.string.shen_gou_beyond));
                } else {
                    alterTextView.setVisibility(View.INVISIBLE);
                    confirmBuybutton.setEnabled(true);
                    isLegal = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                if (isLegal) {
                    // 跳转到
                    if (isShuHui) {
                        Intent intent = new Intent(this,
                                FundRedeemConfirmActivity.class);
                        intent.putExtra("card", currentCard);
                        intent.putExtra("fdcode", fdCode);
                        intent.putExtra("fdname", fdName);
                        intent.putExtra("isShunYan",
                                juEShunYanCheckBox.isChecked());
                        intent.putExtra("buy_account", inputMoneyeditText
                                .getText().toString().trim());
                        startActivityForResult(intent, Const.FUND_REDEEM_FLOE);
                    } else {
                        Intent intent = new Intent(this,
                                PurchaseDetailActivity.class);
                        intent.putExtra("card", currentCard);
                        intent.putExtra("fdcode", fdCode);
                        intent.putExtra("fdname", fdName);
                        intent.putExtra("fdtype", fdType);
                        intent.putExtra("fee", getFundFee());
                        intent.putExtra("buy_account", inputMoneyeditText
                                .getText().toString().trim());
                        startActivityForResult(intent, Const.FUND_BUY_FLOW);
                    }
                }
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
            case R.id.bt_back:
                Util.hideSoftInputMethod(this, v);
                finish();
                break;
            case R.id.shu_hui_all:
                allShuHui = true;
                inputMoneyeditText.setText(String.valueOf(minHold));
                break;
            case R.id.shu_hui_msg_view:
                showHintDialog(getString(R.string.ju_e_shu_hui_shuo_ming),
                        getString(R.string.ju_e_shu_hui_shuo_ming_xiang_qing),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                // 设置你的操作事项
                            }
                        });
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
            case Const.FUND_BUY_FLOW:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    if (this != null) {
                        finish();
                    }
                }
                break;
            case Const.FUND_REDEEM_FLOE:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    if (this != null) {
                        finish();
                    }
                }
                break;
        }
    }

    /**
     * 获取银行卡icon
     * 
     * @param bankName
     * @return
     */
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
