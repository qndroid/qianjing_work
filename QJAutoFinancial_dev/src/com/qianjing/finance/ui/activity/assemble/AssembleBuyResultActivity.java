
package com.qianjing.finance.ui.activity.assemble;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.model.mend.MendCheck;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleLotteryStatus;
import com.qianjing.finance.net.response.model.ResponseLotteryStatus;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.ViewUtil;
import com.qjautofinancial.R;

/**
 * @description 组合申购结果界面
 * @author fangyan
 * @date 2015年4月18日
 */
public class AssembleBuyResultActivity extends BaseActivity {

    int num = -1;
    private ArrayList<FundResponseItem> infoList;
    private LinearLayout whyFail;
    private LinearLayout detailsContent;
    private RelativeLayout resultFail;
    private LinearLayout resultSuccess;
    private LinearLayout mendLayout;
    private String sid;
    private String sopid;
    private String opDateTime;
    private String profitTimeShow;
    private String profitArriveShow;
    private String fee;
    private String money;
    private String card;
    private String bank;
    private boolean isFromMend;
    private static int[] stateStr = {
            R.string.submit, R.string.purchasing
            , R.string.redeeming, R.string.success
            , R.string.fail, R.string.cancel_order
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        initView();
        
        if (!isFromMend)
            requestMendCheck();
        
        requestLotteryStatus();
    }

    private void initData() {
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        sopid = intent.getStringExtra("sopid");
        num = intent.getIntExtra("stateCode", -1);
        infoList = intent.getParcelableArrayListExtra("infoList");
        opDateTime = intent.getStringExtra("opDateShow");
        profitTimeShow = intent.getStringExtra("profitTimeShow");
        profitArriveShow = intent.getStringExtra("profitArriveShow");
        fee = intent.getStringExtra("fee");
        money = intent.getStringExtra("money");
        card = intent.getStringExtra("card");
        bank = intent.getStringExtra("bank");
        isFromMend = intent.getBooleanExtra("isFromMend", false);
    }

    private void initView() {

        setContentView(R.layout.activity_assemble_buy_result);
        setTitleWithString("结果详情");
        TextView rightText = (TextView) findViewById(R.id.title_right_text);
        rightText.setText("完成");
        rightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE);
                finish();
            }
        });

        whyFail = (LinearLayout) findViewById(R.id.ll_why_fail);
        whyFail.setVisibility(View.GONE);
        whyFail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle3 = new Bundle();
                bundle3.putInt("webType", 7);
                openActivity(WebActivity.class, bundle3);
            }
        });

        detailsContent = (LinearLayout) findViewById(R.id.ll_history_details_content);
        resultFail = (RelativeLayout) findViewById(R.id.result_fail);
        resultSuccess = (LinearLayout) findViewById(R.id.result_success);

        mendLayout = (LinearLayout) findViewById(R.id.ll_mend);

        TextView optationTitleView = (TextView) findViewById(R.id.opreate_title_view);
        TextView opdateTimeView = (TextView) findViewById(R.id.commit_time_view);
        TextView profitTimeView = (TextView) findViewById(R.id.confirm_time_view);
        TextView profitWeekView = (TextView) findViewById(R.id.confirm_time_value_view);
        TextView profitMsgView = (TextView) findViewById(R.id.confirm_time_msg_view);
        ImageView profitMsgViewIcon = (ImageView) findViewById(R.id.confirm_time_msg_view_icon);
        TextView profitArriveView = (TextView) findViewById(R.id.final_time_view);
        TextView profitArriveWeekView = (TextView) findViewById(R.id.final_time_value_view);
        TextView profitArriveMsgView = (TextView) findViewById(R.id.final_time_msg_view);
        ImageView profitArriveMsgViewIcon = (ImageView) findViewById(R.id.final_time_msg_view_icon);
        TextView bankNameView = (TextView) findViewById(R.id.bank_name_view);
        TextView takeMoneyView = (TextView) findViewById(R.id.deduct_money_view);
        TextView takeMoneyValueView = (TextView) findViewById(R.id.deduct_money_value_view);
        TextView feeMoneyView = (TextView) findViewById(R.id.fee_money_view);
        TextView feeMoneyValueView = (TextView) findViewById(R.id.fee_money_value_view);
        ImageView firstPart = (ImageView) findViewById(R.id.first_part);

        opdateTimeView.setText(DateFormatHelper.formatDate(opDateTime.concat("000"),
                DateFormat.DATE_9));
        profitTimeView
                .setText(DateFormatHelper.formatDate(profitTimeShow.concat("000"),
                        DateFormat.DATE_1));
        profitWeekView
                .setText(DateFormatHelper.formatDate(profitTimeShow.concat("000"),
                        DateFormat.DATE_6));
        profitMsgView.setCompoundDrawables(null, null, null, null);
        profitArriveView.setText(DateFormatHelper.formatDate(
                profitArriveShow.concat("000"),
                DateFormat.DATE_1));
        profitArriveWeekView.setText(DateFormatHelper.formatDate(
                profitArriveShow.concat("000"),
                DateFormat.DATE_6));
        profitArriveMsgView.setCompoundDrawables(null, null, null, null);

        bankNameView.setText(getString(R.string.dao_zhang_ying_hang_ka) + bank
                + getString(R.string.left_brackets) + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(card) + getString(R.string.right_brackets));

        takeMoneyView.setText(getString(R.string.real_cut_money));
        feeMoneyView.setText(getString(R.string.redeemp_evaluate_fee));
        takeMoneyValueView.setText("¥"
                + StringHelper.formatString(money, StringFormat.FORMATE_2));
        feeMoneyValueView.setText("¥"
                + StringHelper.formatString(fee, StringFormat.FORMATE_2));

        profitMsgView.setText(getString(R.string.redeem_wait_confirm));
        profitArriveMsgView.setText(getString(R.string.purchase_result_final_msg));

        profitMsgViewIcon.setVisibility(View.VISIBLE);
        profitArriveMsgViewIcon.setVisibility(View.VISIBLE);

        profitMsgViewIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showHintDialog(getString(R.string.share_explain_title),
                        getString(R.string.share_explain));
            }
        });
        profitArriveMsgViewIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showHintDialog(getString(R.string.profit_explain_title),
                        getString(R.string.profit_explain));
            }
        });

        switch (num) {
            case 1:
                optationTitleView.setText("支付成功");
                resultSuccess.setVisibility(View.VISIBLE);
                resultFail.setVisibility(View.GONE);
                break;
            case 11:
                optationTitleView.setText("部分支付成功");
                resultSuccess.setVisibility(View.VISIBLE);
                resultFail.setVisibility(View.GONE);
                detailsContent.setVisibility(View.GONE);
                firstPart.setBackgroundResource(R.drawable.icon_part_success);
                break;
            case 0:
                optationTitleView.setText("申购已受理");
                resultSuccess.setVisibility(View.VISIBLE);
                resultFail.setVisibility(View.GONE);
                firstPart.setBackgroundResource(R.drawable.icon_submit);
                break;
            case 10:
                optationTitleView.setText("部分申购已受理");
                resultSuccess.setVisibility(View.VISIBLE);
                resultFail.setVisibility(View.GONE);
                firstPart.setBackgroundResource(R.drawable.icon_part_success);
                break;
            case 4:
                optationTitleView.setText("支付失败");
                resultSuccess.setVisibility(View.GONE);
                resultFail.setVisibility(View.VISIBLE);
                whyFail.setVisibility(View.VISIBLE);
                break;
            default:
                optationTitleView.setText("部分扣款成功");
                resultSuccess.setVisibility(View.VISIBLE);
                resultFail.setVisibility(View.GONE);
                whyFail.setVisibility(View.VISIBLE);
                break;
        }
        if (infoList.size() != 0) {
            for (int i = 0; i < infoList.size(); i++) {
                setDetailsContent(infoList.get(i).abbrev
                        , Float.parseFloat(infoList.get(i).fdsum),
                        getString(stateStr[infoList.get(i).fdstate])
                        , infoList.get(i).fdstate, infoList.get(i).reason);
            }
        }
    }

    /**
     * @description 是否需要补购请求
     * @author fangyan
     */
    private void requestMendCheck() {
      Hashtable<String, Object> upload = new Hashtable<String, Object>();
      upload.put("sopid", sopid);
      upload.put("sid", sid);
      RequestManager.request(this, UrlConst.MEND_CHECK, upload, MendCheck.class,
              new XCallBack() {
                  @Override
                  public void success(final BaseModel model) {
                      
                      if (model.stateCode == ErrorConst.EC_OK) {
                          mendLayout.setVisibility(View.VISIBLE);
                          mendLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MendCheck mendCheck = (MendCheck) model;
                                Intent intent = new Intent();
                                intent.putExtra("bank", bank);
                                intent.putExtra("card", card);
                                intent.putExtra("sid", sid);
                                intent.putExtra("sopid", sopid);
                                intent.putExtra("MendCheck", mendCheck);
                                intent.setClass(AssembleBuyResultActivity.this, AssembleMendDetailActivity.class);
                                startActivity(intent);
                            }
                          });
                      }
                  }
                  @Override
                  public void fail() {
                  }
              });
    }

    private LotteryActivity mLottery;

    /**
     * @description 判断用户抽奖资格
     * @author fangyan
     */
    private void requestLotteryStatus() {

        RequestActivityHelper.requestLotteryStatus(this, new IHandleLotteryStatus()
        {
            @Override
            public void handleResponse(ResponseLotteryStatus response)
            {
                // LotteryActivity lottery = response.lottery;
                // lottery.status = true;
                // lottery.strMessage = "土豪，您获取了抽奖资格！";
                // lottery.strUrl = "http://www.baidu.com";
                if (response.lottery != null && response.lottery.status)
                {
                    PrefManager spManager = PrefManager.getInstance();
                    spManager.putBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, true);
                    spManager.putString(PrefManager.ACTIVITY_LOTTERY_MESSAGE,
                            response.lottery.strMessage);
                    mLottery = response.lottery;
                }
            }
        });
    }

    private void setDetailsContent(String abbrev, float item2, String item3, int state,
            String reason) {

        View view = View.inflate(this, R.layout.history_details_item, null);
        TextView historyItem1 = (TextView) view.findViewById(R.id.tv_history_item1);
        TextView historyItem2 = (TextView) view.findViewById(R.id.tv_history_item2);
        TextView historyItem3 = (TextView) view.findViewById(R.id.tv_history_item3);

        TextView historyReason = (TextView) view.findViewById(R.id.tv_history_reason);
        historyItem1.setText(abbrev);
        FormatNumberData.simpleFormatNumber(item2, historyItem2);
        historyItem3.setText(item3);
        if (state == 4) {
            historyReason.setVisibility(View.VISIBLE);
            historyReason.setText("失败原因:" + reason);
        }
        detailsContent.addView(view);
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = getIntent();
        if (mLottery != null) {
            mIntent.putExtra("lottery", mLottery);
        }
        setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, mIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 申购结果返回
        if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) {
            setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, data);
            finish();
        }
    }

}
