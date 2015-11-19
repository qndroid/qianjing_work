/**
 * Project Name:QJAutoFinancial
 * File Name:AssembleMendDetailActivity.java
 * Package Name:com.qianjing.finance.ui.activity.assemble
 * Date:2015年7月29日上午11:25:14
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 */

package com.qianjing.finance.ui.activity.assemble;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.fund.FundResponseItem;
import com.qianjing.finance.model.mend.Fund;
import com.qianjing.finance.model.mend.MendCheck;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * ClassName:AssembleMendDetailActivity <br/>
 * Function: 补单详情界面 <br/>
 * Date: 2015年7月29日 上午11:25:14 <br/>
 * 
 * @author fangyan
 * @version
 * @see
 */
public class AssembleMendDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBank;
    private TextView tvBankName;
    private TextView tvPrompt;
    private Button btnComfirm;
    private LinearLayout fundDetailLayout;

    private String sid;
    private String sopid;
    private MendCheck mMendCheck;
    private String bank;
    private String card;
    private String pwd;
    private float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        bank = intent.getStringExtra("bank");
        card = intent.getStringExtra("card");
        sid = intent.getStringExtra("sid");
        sopid = intent.getStringExtra("sopid");
        mMendCheck = (MendCheck) intent.getSerializableExtra("MendCheck");
    }

    @SuppressLint("Recycle")
    private void initView() {
        setContentView(R.layout.activity_mend_detail);

        setTitleWithId(R.string.mend_title);

        setTitleBack();

        setTitleImgRight(R.drawable.cpay_title_btn2, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("webType", 8);
                bundle.putString("url", "http://www.qianjing.com/other/rebuy/qa4.html");
                openActivity(WebActivity.class, bundle);
            }
        });

        fundDetailLayout = (LinearLayout) findViewById(R.id.ll_fund_detail);
        // 设置基金列表
        if (mMendCheck != null) {
            List<Fund> listFund = mMendCheck.listFund;
            for (Fund fund : listFund) {
                setDetailsContent(fund.name, fund.ratio*100, fund.sum);
                // 计算总申购额
                sum += fund.sum;
            }
        }

        // 设置银行icon
        ivBank = (ImageView) findViewById(R.id.iv_bank);
        TypedArray bankImage = getResources().obtainTypedArray(R.array.bank_image);
        int bankImageId = ViewUtil.getBankImageByName(this, bank);
        ivBank.setImageResource(bankImage.getResourceId(bankImageId, 0));

        // 设置银行名称和卡号
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);
        tvBankName.setText(bank + getString(R.string.left_brackets) + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(card) + getString(R.string.right_brackets));

        // 设置申购数额提示
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        tvPrompt.setText(getString(R.string.mend_prompt_1)
                + StringHelper.formatString(String.valueOf(sum), StringFormat.FORMATE_2));

        btnComfirm = (Button) findViewById(R.id.btn_mend_confirm);
        btnComfirm.setOnClickListener(this);
    }

    private void setDetailsContent(String abbrev, float ratio, float item3) {

        View view = View.inflate(this, R.layout.item_fund_detail, null);
        TextView tv1 = (TextView) view.findViewById(R.id.tv_history_item1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_history_item2);
        TextView tv3 = (TextView) view.findViewById(R.id.tv_history_item3);

        tv1.setText(abbrev);
        FormatNumberData.simpleFormatNumber(ratio, tv2);
        if (item3 == 0) {
            tv3.setText("--");
        } else {
            FormatNumberData.simpleFormatNumber(item3, tv3);
        }
        fundDetailLayout.addView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mend_confirm:
                showPasswordDialog();
                break;
        }
    }

    private void showPasswordDialog() {

        final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle("支付" + StringHelper.formatString(String.valueOf(sum),StringFormat.FORMATE_2) + "元");
        inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pwd = inputDialog.getEditText().getText().toString().trim();
                if (StringHelper.isBlank(pwd)) {
                    showToast("输入登录密码不能为空");
                }
                else {
                    requestMendSubmit();
                }
                dialog.dismiss();
            }
        });
        inputDialog.create().show();
    }

    private void requestMendSubmit() {

        Hashtable<String, Object> table = new Hashtable<String, Object>();
        table.put("sid", sid);
        table.put("sopid", sopid);
        table.put("cd", card);
        table.put("pwd", pwd);
        AnsynHttpRequest.requestByPost(this, UrlConst.MEND_COMMIT, new HttpCallBack() {
            @Override
            public void back(String data, int url) {
                Message msg = Message.obtain();
                msg.what = TAG_ASSEMBLE_BUY;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        }, table);
    }

    private static final int TAG_ASSEMBLE_BUY = 1;
    private static final int TAG_BANK_BACK_COMFIRM = 2;
    private static final int TAG_BANK_BACK_COMFIRM_LOOP = 3;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            String strResponse = (String) msg.obj;

            switch (msg.what) {
                case TAG_ASSEMBLE_BUY:
                    // 确认付款
                    handleAssembleBuy(strResponse);
                    break;
                case TAG_BANK_BACK_COMFIRM:

                    handleBankBackComfirm(strResponse);
                    break;
                case TAG_BANK_BACK_COMFIRM_LOOP:

                    requestBankBack(sopid, sid);
                    break;
            }
        }
    };

    protected void handleAssembleBuy(String obj) {

        if (StringHelper.isBlank(obj)) {
            dismissLoading();
            showToast("网络不给力");
            return;
        }

        try
        {
            JSONObject object = new JSONObject(obj);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            String mdata = object.optString("data");
            if (ecode == 0)
            {
                JSONObject object2 = new JSONObject(mdata);
                String subobject2 = object2.optString("schemaLog");
                JSONObject object3 = new JSONObject(subobject2);
                sopid = object3.optString("sopid");
                sid = object3.optString("sid");

                requestBankBack(sopid, sid);
            }
            else
            {
                dismissLoading();
                showHintDialog(emsg);
            }
        } catch (Exception e)
        {
            dismissLoading();
            showToast("网络不给力");
        }
    }

    protected void handleBankBackComfirm(String obj)
    {

        if (StringHelper.isBlank(obj)) {
            dismissLoading();
            showToast("网络不给力");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(obj);
            int ecode4 = jsonObject.optInt("ecode");
            String emsg4 = jsonObject.optString("emsg");
            if (ecode4 == ErrorConst.EC_OK) {
                dismissLoading();

                String data4 = jsonObject.optString("data");
                JSONObject jsonObject2 = new JSONObject(data4);
                String schemaLog4 = jsonObject2.optString("schemaLog");
                JSONObject jsonObject3 = new JSONObject(schemaLog4);
                JSONArray fdcodes = jsonObject3.optJSONArray("fdcodes");
                JSONArray abbrevs = jsonObject3.optJSONArray("abbrevs");
                JSONArray fdsums = jsonObject3.optJSONArray("fdsums");
                JSONArray fdshares = jsonObject3.optJSONArray("fdshares");
                JSONArray fdstates = jsonObject3.optJSONArray("fdstates");
                JSONArray reasons = jsonObject3.optJSONArray("reasons");

                ArrayList<FundResponseItem> infoList = new ArrayList<FundResponseItem>();
                for (int i = 0; i < fdcodes.length(); i++)
                {
                    FundResponseItem responseItemInfo = new FundResponseItem(fdcodes.optString(i),
                            abbrevs.optString(i), fdsums.optString(i), fdshares.optString(i),
                            fdstates.optInt(i), reasons.optString(i));
                    infoList.add(responseItemInfo);
                }

                String opDateShow = jsonObject3.optString("opDate");
                String confirmTime = jsonObject3.optString("confirm_time");
                String arriveTime = jsonObject3.optString("arrive_time");
                String estimateSum = jsonObject3.optString("estimate_sum");
                String estimateFee = jsonObject3.optString("estimate_fee");
                String bank = jsonObject3.optString("bank");
                String card = jsonObject3.optString("card");

                Intent intent = new Intent(this, AssembleBuyResultActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("sopid", sopid);
                intent.putExtra("opDateShow", opDateShow);
                intent.putExtra("profitTimeShow", confirmTime);
                intent.putExtra("profitArriveShow", arriveTime);
                intent.putExtra("fee", estimateFee);
                intent.putExtra("money", estimateSum);
                intent.putExtra("card", card);
                intent.putExtra("bank", bank);
                intent.putExtra("msg", CommonUtil.getBuyState(infoList));
                intent.putExtra("stateCode", CommonUtil.getBuyStateCode(infoList));
                intent.putExtra("isFromMend", true);
                intent.putParcelableArrayListExtra("infoList", infoList);
                startActivityForResult(intent, ViewUtil.REQUEST_CODE);
            }
            else {
                // 如果继续等待则1.5秒后再次确认结果
                if (ecode4 == ErrorConst.EC_BANKBACK_CONTINUE) {
                    Message msg4 = Message.obtain();
                    msg4.what = TAG_BANK_BACK_COMFIRM_LOOP;
                    mHandler.sendMessageDelayed(msg4, 1500);
                }
                else {
                    dismissLoading();
                    showHintDialog(emsg4);
                }
            }

        } catch (JSONException e) {
            dismissLoading();
            showToast("网络不给力");
        }
    }

    private void requestBankBack(String sopid, String sid) {
        showLoading();

        Hashtable<String, Object> table = new Hashtable<String, Object>();
        table.put("rebuy", 1); // 补购类型
        table.put("sid", sid);
        table.put("sopid", sopid);
        AnsynHttpRequest.requestByPost(this, UrlConst.BANK_BACK, new HttpCallBack() {
            @Override
            public void back(String data, int url) {
                Message msg = new Message();
                msg.what = TAG_BANK_BACK_COMFIRM;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        }, table);
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
