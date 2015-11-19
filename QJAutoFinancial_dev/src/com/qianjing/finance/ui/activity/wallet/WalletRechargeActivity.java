
package com.qianjing.finance.ui.activity.wallet;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleLotteryStatus;
import com.qianjing.finance.net.response.model.ResponseLotteryStatus;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.ui.activity.fund.CardListActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 活期宝充值页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class WalletRechargeActivity extends BaseActivity implements OnClickListener {
    /**
     * UI相关
     */
    private ImageView iconBankImageView;
    private TextView nameBankTextView;
    private TextView numBankTextView;
    private Button bt_back;
    private Button btnRecharge;
    private TextView title_middle_text;
    private EditText editMoney;
    private LinearLayout ll_selectedbank;
    private TextView tv_alerttext;
    private ScrollView sc_showcontent;
    private RelativeLayout include_ok;
    private ImageView im_help;
    private TextView tv_arrivaltime;
    /**
     * data相关
     */
    private ArrayList<String> listCardNum = new ArrayList<String>();
    private ArrayList<Card> listCard = new ArrayList<Card>();
    private int selectedPosition = -1;
    private ImageView im_rightarrow;
    private TextView tv_single_limit;
    private String tradeid;
    private int backTime = 0;
    private TypedArray bankImage;
    private LotteryActivity mActivity; // 活动相关
    /*
     * 结果页文案显示id
     */
    private TextView tv_time01;
    private TextView tv_time02;
    private TextView tv_time03;
    private TextView bankNameView;
    private TextView confirmTimeWeekView;
    private TextView finalTimeWeekView;
    private TextView optionTitleView;
    private TextView confirmMsgView;
    private TextView finalMsgView;
    private TextView finishView;
    private TextView takeMoneyView;
    private TextView takeMoneyValueView;
    private String opDateShow;
    private String profit_timeShow;
    private String profit_arriveShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UMengStatics.onCurrentBuyPage1View(this);
        setContentView(R.layout.activity_wallet_recharge);
        initView();
        requestCardlist();
        requestRechargeTime();
    }

    /**
     * 请求充值时间
     */
    private void requestRechargeTime() {
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_RECHARGE_TIME,
                rechargeTimecallback, null);
    }

    private HttpCallBack rechargeTimecallback = new HttpCallBack() {
        @Override
        public void back(String data, int type) {
            Message msg = new Message();
            msg.obj = data;
            msg.what = TYPE_RECHARGETIME;
            mHandler.sendMessage(msg);
        }
    };

    private void requestCardlist() {
        // 银行卡信息处理
        showLoading();
        listCard.clear();
        listCardNum.clear();
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD_LIST,
                callback2, null);
    }

    private void initView() {

        // 设置默认加载框不可取消
        setLoadingUncancelable();
        bankImage = getResources().obtainTypedArray(R.array.bank_image);

        tv_alerttext = (TextView) findViewById(R.id.tv_alerttext);
        sc_showcontent = (ScrollView) findViewById(R.id.sc_showcontent);
        include_ok = (RelativeLayout) findViewById(R.id.include_ok);

        tv_time01 = (TextView) findViewById(R.id.commit_time_view);
        tv_time02 = (TextView) findViewById(R.id.confirm_time_view);
        tv_time03 = (TextView) findViewById(R.id.final_time_view);
        bankNameView = (TextView) findViewById(R.id.bank_name_view);
        confirmTimeWeekView = (TextView) findViewById(R.id.confirm_time_value_view);
        finalTimeWeekView = (TextView) findViewById(R.id.final_time_value_view);
        optionTitleView = (TextView) findViewById(R.id.opreate_title_view);
        confirmMsgView = (TextView) findViewById(R.id.confirm_time_msg_view);
        finalMsgView = (TextView) findViewById(R.id.final_time_msg_view);
        finishView = (TextView) findViewById(R.id.right_title_view);
        finishView.setOnClickListener(this);
        takeMoneyView = (TextView) findViewById(R.id.deduct_money_view);
        takeMoneyValueView = (TextView) findViewById(R.id.deduct_money_value_view);

        tv_arrivaltime = (TextView) findViewById(R.id.tv_arrivaltime);
        bt_back = (Button) findViewById(R.id.bt_back);
        title_middle_text = (TextView) findViewById(R.id.title_middle_text);
        title_middle_text.setText(R.string.str_towallet);
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setOnClickListener(this);
        im_rightarrow = (ImageView) findViewById(R.id.im_rightarrow);
        btnRecharge = (Button) findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(this);
        im_help = (ImageView) findViewById(R.id.im_help);
        im_help.setVisibility(View.VISIBLE);
        im_help.setOnClickListener(this);
        tv_single_limit = (TextView) findViewById(R.id.tv_single_limit);
        editMoney = (EditText) findViewById(R.id.edit_money);
        editMoney.addTextChangedListener(new TextWatcher() {
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
                tv_alerttext.setText("");
                String text = editMoney.getText().toString();
                if (!StringHelper.isBlank(text)) {
                    if (!text.equals(".")) {
                        double textParsedNum = Double.parseDouble(text);
                        if (textParsedNum >= 1
                                && textParsedNum <= listCard.get(
                                        selectedPosition).getLimitRecharge()) {
                            btnRecharge.setEnabled(true);
                        } else {
                            btnRecharge.setEnabled(false);
                            if (textParsedNum < 1)
                                tv_alerttext.setText("转入金额不得低于1元");
                            if (textParsedNum > listCard.get(selectedPosition)
                                    .getLimitRecharge())
                                tv_alerttext.setText("转入金额超出该银行卡支付限额");
                        }
                    }
                } else {
                    btnRecharge.setEnabled(false);
                }

            }
        });
        editMoney.setText("");
        btnRecharge.setEnabled(false);

        ll_selectedbank = (LinearLayout) findViewById(R.id.ll_selectedbank);
        ll_selectedbank.setOnClickListener(this);
        iconBankImageView = (ImageView) findViewById(R.id.iv_image);
        nameBankTextView = (TextView) findViewById(R.id.tv_bankname);
        numBankTextView = (TextView) findViewById(R.id.tv_bankcard);

    }

    private static final int TYPE_CARD_LIST = 100;
    private static final int TYPE_RECHARGE = 101;
    private static final int TYPE_WAITING = 103;
    private static final int TYPE_RECHARGETIME = 104;

    private HttpCallBack callback2 = new HttpCallBack() {
        @Override
        public void back(String data, int type) {
            Message msg = new Message();
            msg.obj = data;
            msg.what = TYPE_CARD_LIST;
            mHandler.sendMessage(msg);
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPE_CARD_LIST:
                    handCardListJson((String) msg.obj);
                    break;
                case TYPE_RECHARGE:
                    handleRechargeJson((String) msg.obj);
                    break;
                case TYPE_WAITING:// 处理充值后请求返回数据的接口数据
                    handleWaiting((String) msg.obj);
                    break;
                case TYPE_RECHARGETIME:// 获取充值时间数据处理
                    handleRechargeTime((String) msg.obj);
                    break;
            }
        }
    };

    // 处理充值回复数据
    private void handleRechargeJson(String strJson) {
        dismissLoading();
        if (strJson == null || "".equals(strJson)) {
            showHintDialog("数据解析错误");
            return;
        }

        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");

            if (ecode == ErrorConst.EC_OK) {
                JSONObject jsonObject = object.optJSONObject("data");
                JSONObject trade_log = jsonObject.optJSONObject("trade_log");
                String remark = trade_log.optString("remark");
                int state = trade_log.optInt("state");
                // 申购中
                if (state == 1) {
                    opDateShow = trade_log.optString("opDate");
                    profit_timeShow = trade_log.optString("profit_time");
                    profit_arriveShow = trade_log.optString("profit_arrive");
                    tradeid = trade_log.optString("id");
                    requestWaiting();
                } else {
                    showHintDialog("抱歉，无法完成转入。失败原因：" + remark);
                }
            } else {
                showHintDialog(emsg);
                return;
            }
        } catch (Exception e) {
            showHintDialog("网络不给力！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择银行卡返回
        if (resultCode == RESULT_OK) {
            mCurrentCard = data.getParcelableExtra(Flag.EXTRA_BEAN_CARD_COMMON);
            editMoney.setText("");

            int index = 0;
            for (int i = 0; i < listCard.size(); i++)
                if (TextUtils.equals(mCurrentCard.getNumber(), listCard.get(i)
                        .getNumber()))
                    index = i;
            setView(listCard, index);
        }
    }

    protected void handleWaiting(String jsonStr) {
        dismissLoading();
        if (jsonStr == null || "".equals(jsonStr)) {
            showHintDialog("数据解析错误");
            return;
        }

        // state: 1:受理中 2:成功 4:失败
        try {
            JSONObject object = new JSONObject(jsonStr);
            int ecode = object.optInt("ecode");
            String strErrorMassage = object.optString("emsg");
            JSONObject data = object.optJSONObject("data");
            if (ecode == ErrorConst.EC_OK) {
                JSONObject trade_log = data.optJSONObject("trade_log");
                int state = trade_log.optInt("state");
                /**
                 * 充值成功
                 */
                if (state == 2) {
                    UMengStatics.onCurrentBuyPage3View(this);

                    Card currentCard = listCard.get(selectedPosition);
                    sc_showcontent.setVisibility(View.GONE);
                    include_ok.setVisibility(View.VISIBLE);
                    bt_back.setVisibility(View.INVISIBLE);
                    finishView.setVisibility(View.VISIBLE);

                    optionTitleView
                            .setText(getString(R.string.recharge_money_success));
                    bankNameView.setText(currentCard.getBankName()
                            + getString(R.string.left_brackets)
                            + getString(R.string.wei_hao)
                            + StringHelper.showCardLast4(currentCard.getNumber())
                            + getString(R.string.right_brackets));
                    takeMoneyView
                            .setText(getString(R.string.huo_qi_bao_recharge));
                    takeMoneyValueView.setText(getString(R.string.ren_ming_bi)
                            + StringHelper.formatString(editMoney.getText()
                                    .toString(), StringFormat.FORMATE_1));
                    tv_time01.setText(DateFormatHelper.formatDate(
                            opDateShow.concat("000"), DateFormat.DATE_9));
                    tv_time02.setText(DateFormatHelper.formatDate(
                            profit_timeShow.concat("000"), DateFormat.DATE_1));
                    confirmTimeWeekView.setText(DateFormatHelper
                            .formatDate(
                                    profit_timeShow.concat("000"),
                                    DateFormat.DATE_6));
                    confirmMsgView.setCompoundDrawables(null, null, null, null);
                    confirmMsgView
                            .setText(getString(R.string.start_calculate_shouyi));
                    tv_time03
                            .setText(DateFormatHelper.formatDate(
                                    profit_arriveShow.concat("000"),
                                    DateFormat.DATE_1));
                    finalTimeWeekView.setText(DateFormatHelper
                            .formatDate(
                                    profit_arriveShow.concat("000"),
                                    DateFormat.DATE_6));
                    finalMsgView.setCompoundDrawables(null, null, null, null);
                    finalMsgView.setText(getString(R.string.first_shouyi));

                    title_middle_text.setText(getString(R.string.title_result));
                    im_help.setVisibility(View.GONE);
                    backTime = 0;
                    /**
                     * 发送是否可抽奖
                     */
                    RequestActivityHelper.requestLotteryStatus(this,
                            new IHandleLotteryStatus() {

                                @Override
                                public void handleResponse(
                                        ResponseLotteryStatus response) {
                                    if (response.lottery != null
                                            && response.lottery.status) {
                                        PrefManager
                                                .getInstance()
                                                .putBoolean(
                                                        PrefManager.HAVE_MORE_THAN_ACTIVITY,
                                                        true);
                                        mActivity = response.lottery;
                                    }
                                }
                            });
                }
                // 充值失败
                else if (state == 4) {
                    showHintDialog("抱歉，无法完成转入。失败原因：" + strErrorMassage);
                }
            } else if (ecode == ErrorConst.EC_BANKBACK_FAIL) {
                showHintDialog("银行返回结果失败");
            } else {
                if (ecode == ErrorConst.EC_BANKBACK_CONTINUE) {
                    showLoading();
                    JSONObject trade_log = data.optJSONObject("trade_log");
                    int state = trade_log.optInt("state");
                    if (state == 1) {
                        if (backTime < 10) {
                            backTime++;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2 * 1000);
                                    } catch (InterruptedException e) {
                                    }

                                    WalletRechargeActivity.this
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    WalletRechargeActivity.this
                                                            .requestWaiting();
                                                }
                                            });
                                }
                            }).start();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            // showHintDialog("数据解析错误");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
        }
    }

    protected void handleRechargeTime(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            showHintDialog("网络不给力");
            return;
        }

        try {
            JSONObject object = new JSONObject(jsonStr);
            int ecode = object.optInt("ecode");
            String data = object.optString("data");
            if (ecode == 0) {
                JSONObject objData = new JSONObject(data);
                String profit_arrive = objData.optString("profit_arrive");
                tv_arrivaltime.setText(DateFormatHelper.formatDate(
                        profit_arrive.concat("000"), DateFormat.DATE_2)
                        + getString(R.string.left_brackets)
                        + DateFormatHelper.formatDate(
                                profit_arrive.concat("000"), DateFormat.DATE_6)
                        + getString(R.string.right_brackets));
            }

        } catch (JSONException e) {
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
        }
    }

    // 请求充值返回结果
    protected void requestWaiting() {
        if (tradeid != null) {
            Hashtable<String, Object> upload = new Hashtable<String, Object>();
            upload.put("tradeid", tradeid);
            AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_WAITBANK,
                    waitingCallback, upload);
        } else {
            dismissLoading();
        }
    }

    private HttpCallBack waitingCallback = new HttpCallBack() {
        @Override
        public void back(String data, int type) {
            Message msg = new Message();
            msg.obj = data;
            msg.what = TYPE_WAITING;
            mHandler.sendMessage(msg);
        }
    };

    // 处理银行卡列表回复数据
    private void handCardListJson(String jsonStr) {
        dismissLoading();
        if (jsonStr == null || "".equals(jsonStr)) {
            showHintDialog("网络不给力");
            return;
        }

        try {
            JSONObject object = new JSONObject(jsonStr);
            int ecode = object.optInt("ecode");
            String data = object.optString("data");
            if (ecode == 0) {
                JSONObject objData = new JSONObject(data);
                // 解析已绑定银行卡数据
                JSONArray arrayCard = objData.getJSONArray("cards");

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
                    listCard.add(info);
                    listCardNum.add(bank + card);
                }
                setView(listCard, 0);
            }
        } catch (JSONException e) {
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
        }
    }

    private Card mCurrentCard;

    private void setView(ArrayList<Card> accountInfos, int position) {
        if (accountInfos.size() >= 1
                && (position <= accountInfos.size() - 1 && position >= 0)) {
            mCurrentCard = accountInfos.get(position);
            // 将位置赋值给selectedPosition做记录
            selectedPosition = position;
            iconBankImageView.setImageResource(bankImage.getResourceId(
                    getBankImage(listCard.get(position).getBankName()), -1));
            nameBankTextView.setText(mCurrentCard.getBankName());
            numBankTextView.setText(StringHelper.hintCardNum(mCurrentCard
                    .getNumber()));
            tv_single_limit.setText(StringHelper.formatString(String.valueOf(mCurrentCard
                    .getLimitRecharge()), StringFormat.FORMATE_2));
        }
        if (accountInfos.size() >= 1) {
            editMoney.setEnabled(true);
            im_rightarrow.setVisibility(View.VISIBLE);
            ll_selectedbank.setClickable(true);
            if (accountInfos.size() <= 1) {
                im_rightarrow.setVisibility(View.INVISIBLE);
                ll_selectedbank.setClickable(false);
            }
        } else {
            im_rightarrow.setVisibility(View.INVISIBLE);
            ll_selectedbank.setClickable(false);
            editMoney.setEnabled(false);
        }
    }

    private HttpCallBack rechargeCallback = new HttpCallBack() {
        @Override
        public void back(String data, int url) {
            Message message = Message.obtain();
            message.obj = data;
            message.what = TYPE_RECHARGE;
            mHandler.sendMessage(message);
        }
    };

    /**
     * <p>
     * Title: handleClickRecharge
     * </p>
     * <p>
     * Description: 处理充值的点击事件
     * </p>
     */
    private void handleClickRecharge(String password) {
        String card = listCard.get(selectedPosition).getNumber();
        String sum = editMoney.getText().toString();

        showLoading();
        /*
         * card 银行卡 sum 取现金额 password 交易密码
         */
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", card);
        upload.put("sum", sum);
        upload.put("password", password);
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_RECHARGE,
                rechargeCallback, upload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                UMengStatics.onCurrentBuyPage2Password(this);
                chargeMethod();
                break;
            case R.id.right_title_view:
            case R.id.bt_back:
                Intent mIntent = getIntent();
                if (mActivity != null) {
                    mIntent.putExtra("activity", mActivity);
                }
                setResult(RESULT_OK, mIntent);
                finish();
                break;
            case R.id.ll_selectedbank:
                // 判断可用银行卡数量
                if (listCard != null && listCard.size() > 1) {
                    int countCard = 0;
                    for (Card card : listCard) {
                        if (card.getIsUnbound() == 0) {
                            countCard++;
                        }
                    }
                    if (countCard == 1)
                        return;
                } else
                    return;

                Intent intent = new Intent(this, CardListActivity.class);
                // 传递已绑定银行卡容器
                ArrayList<Card> listTemp = new ArrayList<Card>();
                for (Card card : listCard) {
                    if (card.getIsUnbound() == 0) {
                        listTemp.add(card);
                    }
                }
                intent.putExtra(Flag.EXTRA_BEAN_CARD_CURRENT, mCurrentCard);
                intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listTemp);
                startActivityForResult(intent, ViewUtil.REQUEST_CODE);

                break;
            case R.id.im_help:// 充值帮助
                Bundle bundle3 = new Bundle();
                bundle3.putInt("webType", 4);
                openActivity(WebActivity.class, bundle3);
                break;
        }
    }

    private void chargeMethod() {
        if (StringHelper.isBlank(editMoney.getText().toString())) {
            Toast.makeText(this, "请输入完全部信息", Toast.LENGTH_LONG).show();
            return;
        }
        final InputDialog.Builder inputDialog = new InputDialog.Builder(this,
                null);
        inputDialog.setTitle("支付"
                + StringHelper.formatString(editMoney.getText().toString() + "元",
                        StringFormat.FORMATE_1));
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
                            Toast.makeText(WalletRechargeActivity.this,
                                    "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            handleClickRecharge(text);
                        }

                        dialog.dismiss();
                    }
                });
        inputDialog.create().show();

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
