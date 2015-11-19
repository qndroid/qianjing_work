
package com.qianjing.finance.ui.activity.assemble.aip.chargedetail;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.aip.chargedetail.AIPChargeData;
import com.qianjing.finance.model.aip.chargedetail.AIPChargeDetail;
import com.qianjing.finance.model.aip.chargedetail.AIPFundChargeDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.adapter.aip.AIPChargeAdapter;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 定投扣款列表页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AIPChargeDetailActivity extends BaseActivity
        implements
        OnItemClickListener {
    /**
     * UI
     */
    private TextView mTitleView;
    private TextView mGrandTotalView;
    private ListView mProfitListView;
    private AIPChargeAdapter mAdapter;

    /**
     * data
     */
    private String sid; // 组合 ID
    private AIPChargeData mAipChargeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aip_detail);
        initData();
        initView();
        requestProfitData();
    }

    private void initData() {
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        // sid = "199659";
    }

    private void initView() {
        setTitleBack();
        mTitleView = (TextView) findViewById(R.id.title_middle_text);
        mTitleView.setText(getString(R.string.cut_money_detail));

        mGrandTotalView = (TextView) findViewById(R.id.charge_view);
        mProfitListView = (ListView) findViewById(R.id.charge_list_view);
        mProfitListView.setOnItemClickListener(this);

        mAipChargeData = new AIPChargeData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        AIPChargeDetail fundProfit = (AIPChargeDetail) mAdapter
                .getItem(position);
        Intent intent = new Intent(this, AIPFundChargeDetailActivity.class);
        intent.putExtra("fundDetail", fundProfit);
        startActivity(intent);
    }

    private void requestProfitData() {
        showLoading();
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("sid", sid);
        AnsynHttpRequest.requestByPost(this,
                UrlConst.ASSEMBLE_DT_RECORD, new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        if (data == null || data.equals("")) {
                            dismissLoading();
                            showHintDialog(getString(R.string.net_prompt));
                        } else {
                            mHandler.obtainMessage(0x01, data).sendToTarget();
                        }
                    }
                }, params);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    handleProfitDetail(msg.obj.toString());
                    break;
            }
        };
    };

    private void handleProfitDetail(String strJson) {
        dismissLoading();
        try {
            JSONObject allObject = new JSONObject(strJson);
            int ecode = allObject.getInt("ecode");
            String reason = allObject.optString("emsg");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    // 解析数据，更新UI
                    JSONObject dataObj = allObject.optJSONObject("data");
                    JSONArray fundArray = dataObj.optJSONArray("dt_funds");
                    if (fundArray != null && fundArray.length() > 0) {
                        mAipChargeData.allChargeMoney = dataObj
                                .optString("total_success_sum");
                        if (StringHelper.isBlank(mAipChargeData.allChargeMoney)) {

                            mAipChargeData.allChargeMoney = "0.00";
                        }
                        ArrayList<AIPChargeDetail> listData = new ArrayList<AIPChargeDetail>();
                        for (int i = 0; i < fundArray.length(); i++) {
                            JSONObject fundJson = (JSONObject) fundArray.get(i);
                            AIPChargeDetail detail = new AIPChargeDetail();
                            detail.fundName = fundJson.optString("abbrev");
                            detail.chargeMoney = fundJson
                                    .optString("month_sum");
                            detail.successTimes = fundJson
                                    .optString("success_dt_num");
                            detail.allTimes = fundJson
                                    .optString("total_dt_num");

                            JSONArray timeArray = fundJson.optJSONArray("list");
                            ArrayList<AIPFundChargeDetail> timeList = new ArrayList<AIPFundChargeDetail>();
                            for (int j = 0; j < timeArray.length(); j++) {
                                JSONObject timeJson = (JSONObject) timeArray
                                        .get(j);
                                AIPFundChargeDetail fundChargeDetail = new AIPFundChargeDetail();
                                fundChargeDetail.optDate = timeJson
                                        .optString("opDate");
                                fundChargeDetail.sum = timeJson
                                        .optString("sum");
                                fundChargeDetail.state = timeJson
                                        .optString("state");
                                fundChargeDetail.shares = timeJson
                                        .optString("shares");

                                timeList.add(fundChargeDetail);
                            }
                            detail.timeList = timeList;
                            listData.add(detail);
                        }
                        mAipChargeData.mList = listData;
                    }
                    updateUI();
                    break;
                default:
                    showHintDialog(reason);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        if (mAipChargeData.mList != null && mAipChargeData.mList.size() > 0) {
            mGrandTotalView.setText(Html
                    .fromHtml("<font color='#5a6572'>组合累计成功扣款金额</font>"
                            + "<font color= '#ff3b3b'>"
                            + StringHelper.formatString(
                                    mAipChargeData.allChargeMoney,
                                    StringFormat.FORMATE_2) + "元</font>"));
            mAdapter = new AIPChargeAdapter(this, mAipChargeData.mList);
            mProfitListView.setAdapter(mAdapter);
        } else {
            // 显示空界面
        }
    }
}
