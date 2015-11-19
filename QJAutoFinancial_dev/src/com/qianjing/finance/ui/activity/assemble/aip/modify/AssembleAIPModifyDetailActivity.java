
package com.qianjing.finance.ui.activity.assemble.aip.modify;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.aip.AIPDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleReasonItemLayout;
import com.qianjing.finance.view.dialog.TimingPauseDialog;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * @description 组合定投详情页面
 * @author renzhiqiang
 * @date 2015年8月6日
 */
public class AssembleAIPModifyDetailActivity extends BaseActivity
        implements
        OnClickListener {
    /**
     * UI
     */
    private Button mBackButton;
    private TextView mAssembleNameView;
    private TextView mCardView;
    private TextView mOnceInvestmentView;
    private TextView mTimingDateView;
    private TextView mNextView;
    private TextView mConfirmView;
    private LinearLayout mContentLayout;
    private TimingPauseDialog pwdDialog;
    /**
     * data
     */
    private AIPDetail mAipDetail;
    private String monthSum;
    private int currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_aip_modify_detail);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mAipDetail = (AIPDetail) intent.getSerializableExtra("AIPDetail");
        currentDay = intent.getIntExtra("currentDay", -1);
        monthSum = intent.getStringExtra("currentMoney");
    }

    private void initView() {
        setTitleWithId(R.string.chang_detail);
        setLoadingUncancelable();
        mBackButton = (Button) findViewById(R.id.bt_back);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(this);

        mAssembleNameView = (TextView) findViewById(R.id.assemble_name_value);
        mCardView = (TextView) findViewById(R.id.card_number_value);
        mTimingDateView = (TextView) findViewById(R.id.timing_modify_date_value);
        mOnceInvestmentView = (TextView) findViewById(R.id.timing_money_value);
        mNextView = (TextView) findViewById(R.id.timing_next_paid_value);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout); // 使用sebmleReasonItemLayout
        mConfirmView = (TextView) findViewById(R.id.confirm_changed_view);
        mConfirmView.setOnClickListener(this);

        mAssembleNameView.setText(mAipDetail.sname);
        mCardView.setText(mAipDetail.bank + getString(R.string.left_brackets)
                + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(mAipDetail.card)
                + getString(R.string.right_brackets));
        mTimingDateView.setText(currentDay
                + getString(R.string.timing_modify_day));
        mOnceInvestmentView.setText(getString(R.string.ren_ming_bi)
                + StringHelper.formatString(monthSum,
                        StringFormat.FORMATE_2));
        mNextView.setText(DateFormatHelper.formatDate(
                mAipDetail.next_day.concat("000"), DateFormat.DATE_5));
        for (int i = 0; i < mAipDetail.aipItem.size(); i++) {
            AssembleReasonItemLayout layout = new AssembleReasonItemLayout(this);
            layout.initData(mAipDetail.aipItem.get(i).abbrev, "", StringHelper
                    .formatString(
                            mAipDetail.aipItem.get(i).month_sum,
                            StringFormat.FORMATE_2), "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
            mContentLayout.addView(layout);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_changed_view:
                pwdDialog = new TimingPauseDialog(this,
                        getString(R.string.aip_change_title),
                        getString(R.string.aip_change_info));
                pwdDialog.setConfirmListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pwdDialog.dismiss();
                        requestChangeAip(pwdDialog.getPassword());
                    }
                });
                pwdDialog.show();
                break;
            case R.id.bt_back:
                finish();
                break;
        }
    }

    private void requestChangeAip(String pwd) {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sdid", mAipDetail.sdid);
        upload.put("day", currentDay);
        upload.put("sum", monthSum);
        upload.put("passwd", pwd);
        AnsynHttpRequest.requestByPost(this,
                UrlConst.ASSEMBLE_AIP_CHANGE_RESULT, new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        if (data != null && !data.equals("")) {
                            mHandler.sendMessage(mHandler.obtainMessage(0x01,
                                    data));
                        } else {
                            dismissLoading();
                            showHintDialog("网络不给力，请重试!");
                        }
                    }
                }, upload);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    handleChangeDetail(msg.obj.toString());
                    break;
            }
        };
    };

    private void handleChangeDetail(String data) {
        dismissLoading();
        try {
            JSONObject object = new JSONObject(data);
            int ecode = object.optInt("ecode");
            String reason = object.optString("emsg");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    Intent intent = new Intent(this,
                            AssembleAIPModifyResultActivity.class);
                    intent.putExtra("AIPDetail", mAipDetail);
                    intent.putExtra("currentDay", currentDay);
                    intent.putExtra("currentMoney", monthSum);
                    startActivityForResult(intent, Const.ASSEMBLE_TIMING_FLOW);
                    break;
                case ErrorConst.EC_PASSWD_DISMATCH:
                    showHintDialog(reason);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showHintDialog("数据解析异常!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.ASSEMBLE_TIMING_FLOW:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }
}
