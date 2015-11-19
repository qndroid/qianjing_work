
package com.qianjing.finance.ui.activity.assemble.aip.modify;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.aip.AIPDetail;
import com.qianjing.finance.model.aip.AIPFundItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembleredeem.RedeemPickerateDialog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 组合定投详情页面
 * @author renzhiqiang
 * @date 2015年8月6日
 */
public class AssembleAIPModifyActivity extends BaseActivity
        implements
        OnClickListener {
    /**
     * UI
     */
    private Button mBackButton;
    private RelativeLayout mTimingLayout;
    private TextView mTimingDateView;
    private TextView mTimingNextDateView;
    private EditText mMoneyView;
    private TextView mMoneyWaningView;
    private TextView mNextView;
    private RedeemPickerateDialog pickerDialog;

    /**
     * data
     */
    private AIPDetail mAipDetail;
    private int mCurrntDay;
    private String monthSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_aip_modify);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mAipDetail = (AIPDetail) intent.getSerializableExtra("AIPDetail");
        mCurrntDay = Integer.parseInt(mAipDetail.day);
        monthSum = mAipDetail.month_sum;
    }

    private void initView() {
        setTitleWithId(R.string.title_aip_modify);
        setLoadingUncancelable();
        mBackButton = (Button) findViewById(R.id.bt_back);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(this);

        mTimingLayout = (RelativeLayout) findViewById(R.id.modify_time_layout);
        mTimingDateView = (TextView) findViewById(R.id.timing_view);
        mTimingNextDateView = (TextView) findViewById(R.id.timing_modify_charge_value);
        mMoneyView = (EditText) findViewById(R.id.money_title_value);
        mMoneyView.addTextChangedListener(watcher);
        mMoneyWaningView = (TextView) findViewById(R.id.timing_money_warning);
        mNextView = (TextView) findViewById(R.id.timing_next_view);
        mTimingLayout.setOnClickListener(this);
        mNextView.setOnClickListener(this);

        mMoneyView.setText(monthSum);
        mTimingDateView.setText(mCurrntDay
                + getString(R.string.timing_modify_day));
        mTimingNextDateView.setText(DateFormatHelper.formatDate(
                mAipDetail.next_day.concat("000"), DateFormat.DATE_5));
        mMoneyWaningView.setText(Html
                .fromHtml("<font color='#666666'>定投金额不能低于</font>"
                        + "<font color= '#ff3b3b'>"
                        + StringHelper.formatString(
                                mAipDetail.min_sum, StringFormat.FORMATE_2)
                        + "元</font>"));
        pickerDialog = new RedeemPickerateDialog(this, 1, 28, mCurrntDay);
        pickerDialog.setConfirmListener(listener);
        pickerDialog.setTitleText(getString(R.string.chang_title));
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
            String text = mMoneyView.getText().toString();
            if (!text.equals("")) {
                if (text.equals(".")) {
                    mMoneyView.setText("");
                    return;
                }

                if (Double.parseDouble(text) < Double
                        .parseDouble(mAipDetail.min_sum)) {
                    mNextView.setEnabled(false);
                } else {
                    mNextView.setEnabled(true);
                }
            }
        }
    };
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

    private void updateUI(int currentDay) {
        mCurrntDay = currentDay;
        mTimingDateView.setText(currentDay
                + getString(R.string.timing_modify_day));
        /**
         * 获取下次扣款日期
         */
        requestNextDate();
    }

    private void requestNextDate() {
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("day", mCurrntDay);
        AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_AIP_DATE_FIRST,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        if (data != null && !data.equals("")) {
                            mHandler.sendMessage(mHandler.obtainMessage(0x02,
                                    data));
                        }
                    }
                }, upload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timing_next_view:
                requestModifyDetail();
                break;
            case R.id.modify_time_layout:
                pickerDialog.setSelectedNumber(mCurrntDay);
                pickerDialog.show();
                break;
            case R.id.bt_back:
                finish();
                break;
        }
    }

    private void requestModifyDetail() {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sdid", mAipDetail.sdid);
        upload.put("day", mCurrntDay);
        upload.put("sum", mMoneyView.getText().toString());

        AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_AIP_PRE_CHANGE,
                new HttpCallBack() {
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
                    handleAipChangeDetail(msg.obj.toString());
                    break;
                case 0x02:
                    handleNextDay(msg.obj.toString());
                    break;
            }
        };
    };

    private void handleNextDay(String string) {
        try {
            JSONObject object = new JSONObject(string);
            int ecode = object.optInt("ecode");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    mTimingNextDateView.setText(DateFormatHelper
                            .formatDate(object.optString("data")
                                    .concat("000"), DateFormat.DATE_5));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleAipChangeDetail(String string) {
        dismissLoading();
        try {
            JSONObject object = new JSONObject(string);
            int ecode = object.optInt("ecode");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    JSONObject result = object.optJSONObject("data");
                    JSONArray array = result.getJSONArray("list");
                    JSONObject detail = result.optJSONObject("info");
                    if (array != null && array.length() > 0) {
                        mAipDetail.state = detail.optString("state");
                        mAipDetail.month_sum = detail.optString("month_sum");
                        mAipDetail.day = detail.optString("day");
                        mAipDetail.next_day = detail.optString("next_day");

                        ArrayList<AIPFundItem> mListData = new ArrayList<AIPFundItem>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jobj = array.getJSONObject(i);
                            AIPFundItem item = new AIPFundItem();
                            item.abbrev = jobj.optString("abbrev");
                            item.state = jobj.optString("state");
                            item.month_sum = jobj.optString("month_sum");
                            item.reason = jobj.optString("reason");
                            item.id = jobj.optString("id");
                            mListData.add(item);
                        }
                        mAipDetail.aipItem = mListData;
                        /**
                         * 页面跳转
                         */
                        Intent intent = new Intent(this,
                                AssembleAIPModifyDetailActivity.class);
                        intent.putExtra("AIPDetail", mAipDetail);
                        intent.putExtra("currentDay", mCurrntDay);
                        intent.putExtra("currentMoney", mMoneyView.getText()
                                .toString());
                        startActivityForResult(intent,
                                Const.ASSEMBLE_TIMING_FLOW);
                    }
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
