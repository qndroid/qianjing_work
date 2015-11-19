
package com.qianjing.finance.ui.activity.history;

import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.history.Schemaoplogs;
import com.qianjing.finance.model.mend.MendCheck;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.AssembleMendDetailActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.StateUtils;
import com.qjautofinancial.R;

public class AssembleHistoryDetails extends BaseActivity {

    private Schemaoplogs schemaoplogsInfo;
    private LinearLayout detailsContent;
    private TextView stateText;
    private ImageView stateIcon;
    private RelativeLayout mendLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initView();

        requestMendCheck();
       
    }

    private void initView() {
        setContentView(R.layout.activity_history_assemable_details);
        setTitleBack();

        schemaoplogsInfo = (Schemaoplogs) getIntent().getSerializableExtra("schemaoplogs");
        setTitleWithString(schemaoplogsInfo.sname);

        TextView valueTitle = (TextView) findViewById(R.id.tv_history_value_title);
        TextView value = (TextView) findViewById(R.id.tv_history_value);
        TextView arriveTime = (TextView) findViewById(R.id.tv_history_arrive_time);
        TextView bank = (TextView) findViewById(R.id.tv_history_bank);
        TextView historyDate = (TextView) findViewById(R.id.tv_history_time);
        TextView historyFee = (TextView) findViewById(R.id.tv_history_fee);
        TextView historyName = (TextView) findViewById(R.id.tv_history_name);
        TextView sureTitle = (TextView) findViewById(R.id.tv_history_sure_title);
        TextView sureTime = (TextView) findViewById(R.id.tv_history_sure_time);
        TextView historyType = (TextView) findViewById(R.id.tv_history_type);
        RelativeLayout lastItem = (RelativeLayout) findViewById(R.id.rl_last_item);
        mendLayout = (RelativeLayout) findViewById(R.id.rl_mend);

        TextView tvItem2Title = (TextView) findViewById(R.id.tv_item2_title);
        TextView tvItem3Title = (TextView) findViewById(R.id.tv_item3_title);

        stateIcon = (ImageView) findViewById(R.id.iv_state_icon);
        stateText = (TextView) findViewById(R.id.tv_state_text);

        detailsContent = (LinearLayout) findViewById(R.id.ll_history_details_content);

        if (!(schemaoplogsInfo.state == 1) && !(schemaoplogsInfo.state == 2)) {
            if (!StringHelper.isBlank(schemaoplogsInfo.fee)
                    && !(Float.parseFloat(schemaoplogsInfo.fee) == 0)) {
                historyFee.setText("¥" + StringHelper.formatString(schemaoplogsInfo.fee, StringFormat.FORMATE_1));
            }
        }

        historyName.setText(schemaoplogsInfo.sname);
        historyDate.setText(DateFormatHelper.formatDate(
                schemaoplogsInfo.opDate.concat("000"),
                DateFormat.DATE_4));
        bank.setText(schemaoplogsInfo.bank + "(尾号" + StringHelper.showCardLast4(schemaoplogsInfo.card)
                + ")");
        if (schemaoplogsInfo.state == 1 && schemaoplogsInfo.sum != 0) {
            FormatNumberData.formatNumberPRUNIT(Float.parseFloat(schemaoplogsInfo.estimate_sum),
                    value, schemaoplogsInfo.operate);
        } else {
            FormatNumberData.formatNumberPR(value, schemaoplogsInfo.operate);
        }

        String stateStr = StateUtils.getAssembleStateStr(schemaoplogsInfo.operate
                , schemaoplogsInfo.fdstate);

        stateText.setText(stateStr);
        if (stateStr.contains("受理中")) {
            FormatNumberData.formatNumberPRUNIT(0, value, schemaoplogsInfo.operate);
            stateIcon.setBackgroundResource(R.drawable.history_submit);
        } else if (stateStr.contains("中")) {
            stateIcon.setBackgroundResource(R.drawable.history_ing);
        } else if (stateStr.contains("部分") && stateStr.endsWith("成功")) {
            stateIcon.setBackgroundResource(R.drawable.history_some_success);
            FormatNumberData.formatNumberPRUNIT(Float.parseFloat(schemaoplogsInfo.estimate_sum)
                    , value, schemaoplogsInfo.operate);
        } else if (stateStr.endsWith("成功")) {
            FormatNumberData.formatNumberPRUNIT(Float.parseFloat(schemaoplogsInfo.estimate_sum)
                    , value, schemaoplogsInfo.operate);
            stateIcon.setBackgroundResource(R.drawable.history_success);
        } else if (stateStr.endsWith("失败")) {
            FormatNumberData.formatNumberPRUNIT(0, value, schemaoplogsInfo.operate);
            stateIcon.setBackgroundResource(R.drawable.history_fail);
        }

        if (schemaoplogsInfo.operate == 1) {

            valueTitle.setText("实扣金额");
            lastItem.setVisibility(View.GONE);
            sureTitle.setText("盈亏情况");
            historyType.setText("组合申购");
            tvItem2Title.setText("申购金额(元)");
            tvItem3Title.setText("确认份额(份)");

            if (schemaoplogsInfo.state != 4) {
                sureTime.setText(DateFormatHelper.formatDate(
                        schemaoplogsInfo.confirm_time
                                .concat("000"), DateFormat.DATE_2) + "产生盈亏");
            }

            for (int i = 0; i < schemaoplogsInfo.abbrev.size(); i++) {
                setDetailsContent(schemaoplogsInfo.abbrev.get(i)
                        , schemaoplogsInfo.fdsum.get(i)
                        , schemaoplogsInfo.fdshare.get(i)
                        , schemaoplogsInfo.fdstate.get(i)
                        , schemaoplogsInfo.reason.get(i));
            }

        } else if (schemaoplogsInfo.operate == 2) {

            valueTitle.setText("确认金额");
            historyType.setText("组合赎回");

            tvItem2Title.setText("赎回份额(份)");
            tvItem3Title.setText("确认金额(元)");

            if (schemaoplogsInfo.state != 4) {
                sureTime.setText(DateFormatHelper.formatDate(
                        schemaoplogsInfo.confirm_time.concat("000"), DateFormat.DATE_1) + "确认");
                arriveTime.setText(DateFormatHelper.formatDate(
                        schemaoplogsInfo.arrive_time.concat("000"), DateFormat.DATE_1) + "资金到账");
            }

            for (int i = 0; i < schemaoplogsInfo.abbrev.size(); i++) {
                setDetailsContent(schemaoplogsInfo.abbrev.get(i)
                        , schemaoplogsInfo.fdshare.get(i)
                        , schemaoplogsInfo.fdsum.get(i)
                        , schemaoplogsInfo.fdstate.get(i)
                        , schemaoplogsInfo.reason.get(i));
            }
        }
    }

    private void setDetailsContent(String abbrev, float item2, float item3, int state, String reason) {

        View view = View.inflate(this, R.layout.history_details_item, null);
        TextView historyItem1 = (TextView) view.findViewById(R.id.tv_history_item1);
        TextView historyItem2 = (TextView) view.findViewById(R.id.tv_history_item2);
        TextView historyItem3 = (TextView) view.findViewById(R.id.tv_history_item3);

        TextView historyReason = (TextView) view.findViewById(R.id.tv_history_reason);

        historyItem1.setText(abbrev);
        FormatNumberData.simpleFormatNumber(item2, historyItem2);

        if (item3 == 0) {
            historyItem3.setText("--");
        } else {
            FormatNumberData.simpleFormatNumber(item3, historyItem3);
        }

        if (state == 4) {
            historyReason.setVisibility(View.VISIBLE);
            historyReason.setText("失败原因:" + reason);
        }
        detailsContent.addView(view);
    }

    /**
     * @description 是否需要补购请求
     * @author fangyan
     */
    private void requestMendCheck() {
      Hashtable<String, Object> upload = new Hashtable<String, Object>();
      upload.put("sopid", schemaoplogsInfo.sopid);
      upload.put("sid", schemaoplogsInfo.sid);
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
                                intent.putExtra("bank", schemaoplogsInfo.bank);
                                intent.putExtra("card", schemaoplogsInfo.card);
                                intent.putExtra("sid", String.valueOf(schemaoplogsInfo.sid));
                                intent.putExtra("sopid", String.valueOf(schemaoplogsInfo.sopid));
                                intent.putExtra("MendCheck", mendCheck);
                                    intent.setClass(AssembleHistoryDetails.this, AssembleMendDetailActivity.class);
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
    
}
