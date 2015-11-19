
package com.qianjing.finance.ui.activity.fund.buy;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.fund.RewardListActivity;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * @description 单只基金申购详情页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class PurchaseDetailActivity extends BaseActivity implements OnClickListener {
    private static final int SELECT_RED_BAG = 0x02;
    /**
     * UI
     */
    private TextView fundNameTextView;
    private TextView fundStyleTextView;
    private TextView feeStyleView;
    private TextView awareStyleTextView;
    private TextView bankNameTextView;
    private TextView shenGouMoneyTextView;
    private TextView serviceFeeTextView;
    private Button confirmBuyButton;
    private Button backButton;
    private TextView titleTextView;
    private InputDialog.Builder inputDialog;
    private RelativeLayout feeLayout;
    private RelativeLayout rewardLayout;
    private TextView rewardView;
    /**
     * data
     */
    private String fee; // 上个页面传来的手续费
    private Card card;
    private String buyAccount;
    private String fdCode;
    private String fdName;
    private String fdType;
    private String estimateFee;
    // private String estimateSum;
    private RedBag currentBag = null;
    /**
     * 操作相关变量
     */
    private String opDateShow;
    private String profitTimeShow;
    private String profitArriveShow;
    private int tradeId;
    private String reason;
    private int waitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_layout);
        initData();
        initView();
        requestFundBuyDetail();
    }

    private void initView() {
        fundNameTextView = (TextView) findViewById(R.id.fund_name);
        fundStyleTextView = (TextView) findViewById(R.id.purchase_tyle);
        feeStyleView = (TextView) findViewById(R.id.fee_tyle);
        awareStyleTextView = (TextView) findViewById(R.id.award_tyle);
        bankNameTextView = (TextView) findViewById(R.id.bank_name);
        shenGouMoneyTextView = (TextView) findViewById(R.id.buy_account);
        serviceFeeTextView = (TextView) findViewById(R.id.service_fee);
        feeLayout = (RelativeLayout) findViewById(R.id.fee_layout);
        confirmBuyButton = (Button) findViewById(R.id.btn_buy_next);
        confirmBuyButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.bt_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        titleTextView.setText(getString(R.string.shen_gou_detail));
        rewardLayout = (RelativeLayout) findViewById(R.id.reward_layout);
        rewardView = (TextView) findViewById(R.id.reward_money_view);

        rewardLayout.setOnClickListener(this);
        shenGouMoneyTextView.setText(StringHelper.formatString(buyAccount,
                StringFormat.FORMATE_1)
                + getString(R.string.YUAN));
        fundNameTextView.setText(fdName);
        fundStyleTextView.setText(getString(R.string.shen_gou));
        bankNameTextView.setText(card.getBankName()
                + getString(R.string.left_brackets)
                + getString(R.string.wei_hao)
                + card.getNumber().substring(card.getNumber().length() - 4,
                        card.getNumber().length())
                + getString(R.string.right_brackets));
        if (fdType.equals("3")) {
            feeLayout.setVisibility(View.GONE);
            rewardLayout.setVisibility(View.GONE);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        buyAccount = intent.getStringExtra("buy_account");
        card = (Card) intent.getParcelableExtra("card");
        fdCode = intent.getStringExtra("fdcode");
        fdName = intent.getStringExtra("fdname");
        fdType = intent.getStringExtra("fdtype");
        fee = intent.getStringExtra("fee");
    }

    private void requestFundBuyDetail() {
        // 银行卡信息处理
        showLoading();
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("fund_code", fdCode);
        params.put("sum", buyAccount);
        AnsynHttpRequest.requestByPost(this, UrlConst.FUND_BUY_DETAIL,
                fundDetailHandler, params);
    }

    private HttpCallBack fundDetailHandler = new HttpCallBack() {
        @Override
        public void back(String data, int status) {
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
                    showHintDialog("网络不给力");
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
                    updateUI(msg.obj);
                    break;
                case 0x02: // 响应申购请求
                    handleShengouJson(msg.obj.toString());
                    break;
                case 0x03: // 响应等待请求
                    handleWaitingResponse(msg.obj.toString());
                    break;
                case 0x04:
                    requestWaiting(); // 轮询等待接口
                    break;
            }
        }

    };

    private void updateUI(Object obj) {
        JSONObject data = (JSONObject) obj;
        feeStyleView.setText(getString(R.string.qian_duan_shou_fei));
        awareStyleTextView.setText(data.optInt("bonus_way") == 0
                ? getString(R.string.hong_li_zai_tou_zi)
                : getString(R.string.xian_jin_hong_li));
        serviceFeeTextView.setText(StringHelper.formatString(fee, StringFormat.FORMATE_1)
                + getString(R.string.YUAN));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_next:
                showDialog();
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.reward_layout:
                Intent intent = new Intent(this, RewardListActivity.class);
                intent.putExtra("selected_redbag", currentBag);
                intent.putExtra("buy_account", buyAccount);
                startActivityForResult(intent, SELECT_RED_BAG);
                break;
        }
    }

    private void showDialog() {
        inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle("支付" + buyAccount + getString(R.string.YUAN));
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
                            Toast.makeText(PurchaseDetailActivity.this,
                                    "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            requestShenGou(text);
                        }

                        dialog.dismiss();
                    }
                });
        inputDialog.create().show();

    }

    /**
     * 发送第一次申购申请
     */
    private void requestShenGou(String password) {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", card.getNumber());
        upload.put("sum", buyAccount);
        upload.put("password", password);
        upload.put("fund_code", fdCode);
        AnsynHttpRequest.requestByPost(this, UrlConst.FUND_BUY,
                shenGouCallback, upload);
    }

    private HttpCallBack shenGouCallback = new HttpCallBack() {
        @Override
        public void back(String data, int status) {
            if (data != null && !data.equals("")) {
                mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
            } else {
                dismissLoading();
                showHintDialog("网络不给力，请重试!");
            }
        }
    };

    /**
     * 申购成功时setResult,关闭整个流程页面，回到基金列表 页面
     * 
     * @param strJson
     */
    protected void handleShengouJson(String strJson) {
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            reason = object.optString("emsg");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    JSONObject result = object.optJSONObject("data");
                    JSONObject tradeInfo = result.optJSONObject("log_details");

                    reason = tradeInfo.optString("reason");
                    String state = tradeInfo.optString("state");
                    if (state.equals("0")) {
                        tradeId = tradeInfo.optInt("opid");
                        opDateShow = tradeInfo.optString("opDate");
                        profitTimeShow = tradeInfo.optString("confirm_time");
                        profitArriveShow = tradeInfo.optString("arrive_time");
                        requestWaiting();
                    } else {
                        // 此处应该跳转到结果页面
                        dismissLoading();
                        showHintDialog(reason);
                    }
                    break;
                default:
                    dismissLoading();
                    showHintDialog(reason);
                    break;
            }

        } catch (Exception e) {
            // 不跳转
            dismissLoading();
            showHintDialog("网络不给力,请重试!");
        }
    }

    /**
     * 等待银行返回数据,轮询直到有最后三种结果
     */
    private void requestWaiting() {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("opid", tradeId);
        AnsynHttpRequest.requestByPost(this, UrlConst.FUND_WAITING,
                waitingCallback, params);
    }

    private HttpCallBack waitingCallback = new HttpCallBack() {
        @Override
        public void back(String data, int status) {
            if (data != null && !data.equals("")) {
                mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
            } else {
                dismissLoading();
                showHintDialog("网络不给力，请重试!");
            }
        }
    };

    /**
     * 处理银行返回数据
     * 
     * @param string
     */
    private void handleWaitingResponse(String string) {
        try {
            JSONObject object = new JSONObject(string);
            int ecode = object.optInt("ecode");
            JSONObject data = object.optJSONObject("data");
            reason = data.optString("reason");
            String state = data.optString("state");
            long opid = data.optLong("opid");

            switch (ecode) {
                case ErrorConst.EC_OK:
                    dismissLoading();

                    if (TextUtils.equals("3", state)) {
                        waitTime = 0;
                        estimateFee = data.optString("estimate_fee");
                        sendRedBagRequest(opid);
                        goResultPage(Const.FUND_BUY_SUCCESS);
                    } else if (TextUtils.equals("4", state)) {
                        waitTime = 0;
                        showHintDialog(reason);
                    } else {
                        waitTime = 0;
                        estimateFee = data.optString("estimate_fee");
                        sendRedBagRequest(opid);
                        goResultPage(Const.FUND_BUY_SUCCESS);
                    }
                    break;
                case ErrorConst.EC_BANKBACK_FAIL:
                    dismissLoading();
                    showHintDialog(reason);
                    break;
                case ErrorConst.EC_BANKBACK_CONTINUE:
                    showLoading();
                    /**
                     * 继续调用等待接口
                     */
                    if (waitTime < 30) {
                        waitTime++;
                        mHandler.sendEmptyMessageDelayed(0x04, 2000);
                    } else {
                        // 跳转到结果页面，显示处理中状态
                        goResultPage(Const.FUND_BUY_HANDLE);
                    }
                    break;
            }
        } catch (Exception e) {
            dismissLoading();
            showHintDialog("数据解析错误");
        }
    }

    /**
     * 发送红包使用请求
     * 
     * @param opid
     */
    private void sendRedBagRequest(long opid) {
        if (currentBag != null) {
            RequestActivityHelper.requestFundRedBag(this, String.valueOf(opid),
                    String.valueOf(currentBag.id), new IHandleBase() {
                        @Override
                        public void handleResponse(ResponseBase responseBase,
                                int status) {
                        }
                    });
        }
    }

    /**
     * 中转到银行结果页面
     */
    private void goResultPage(int type) {
        dismissLoading();
        Intent intent = new Intent(this, FundResultActivity.class);
        switch (type) {
            case 1:
                intent.putExtra("opDateShow", opDateShow);
                intent.putExtra("profitTimeShow", profitTimeShow);
                intent.putExtra("profitArriveShow", profitArriveShow);
                break;
            default:
                break;
        }
        intent.putExtra("fdtype", fdType);
        intent.putExtra("type", type);
        intent.putExtra("card", card);
        intent.putExtra("fee",
                StringHelper.formatString(estimateFee, StringFormat.FORMATE_1));
        intent.putExtra("money",
                StringHelper.formatString(buyAccount, StringFormat.FORMATE_1));
        startActivityForResult(intent, Const.FUND_BUY_FLOW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.FUND_BUY_FLOW:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    inputDialog = null;
                    if (this != null) {
                        finish();
                    }
                }
                break;
            case SELECT_RED_BAG:
                if (resultCode == RESULT_OK) {
                    currentBag = (RedBag) data.getSerializableExtra("redbag");
                    /**
                     * updateUI
                     */
                    if (currentBag != null) {
                        rewardView.setTextColor(getResources().getColor(
                                R.color.red_VI));
                        rewardView.setText(getString(R.string.ren_ming_bi)
                                + currentBag.val
                                + getString(R.string.dai_jin_quan));
                    }
                } else {
                    currentBag = null;
                    rewardView.setTextColor(getResources().getColor(
                            R.color.color_6c6c6c));
                    rewardView.setText(getString(R.string.use_dai_jin_quan));
                }
                break;
        }
    }
}
