
package com.qianjing.finance.ui.activity.assemble.redeem;

import com.qianjing.finance.model.redeem.AssembleShares;
import com.qianjing.finance.model.redeem.FundItemState;
import com.qianjing.finance.model.redeem.RedeemModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.view.assembledetailview.AssembleReasonItemLayout;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author renzhiqiang
 * @function 组合赎回确认页面(包含虚拟组合)
 */
public class AssembleRedeemConfirmActivity extends BaseActivity implements OnClickListener
{
    private static final int REDEEM_PART_SUCCESS = 2;
    private static final int REDEEM_ALL_SUCCESS = 1;
    private static final int REDEEM_FAILED = 3;
    /**
     * UI
     */
    private LinearLayout contentView;
    private TextView redeemConfirmBtn;
    private Button backBtn;
    private TextView titleTextView;

    /**
     * data
     */
    private RedeemModel redeemModel;
    private boolean isVirtual;
    private String sum;
    private String radio;
    private ArrayList<AssembleShares> sharesList;
    private ArrayList<FundItemState> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        UMengStatics.onPRedeemPage2View(this);

        setContentView(R.layout.activity_assemble_redeem_confirm_new_layout);
        initData();
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initData()
    {
        Intent intent = getIntent();
        redeemModel = (RedeemModel) intent.getSerializableExtra("assemble_model");
        sharesList = (ArrayList<AssembleShares>) intent.getSerializableExtra("fund_share");
        sum = intent.getStringExtra("sum");
        radio = intent.getStringExtra("radio");
        isVirtual = redeemModel.isVirtual();
    }

    private void initView()
    {
        backBtn = (Button) findViewById(R.id.bt_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        titleTextView.setVisibility(View.VISIBLE);
        contentView = (LinearLayout) findViewById(R.id.content_layout);
        redeemConfirmBtn = (TextView) findViewById(R.id.redeem_btn_view);
        redeemConfirmBtn.setOnClickListener(this);
        titleTextView.setText(getString(R.string.redeem_confirm));

        for (int i = 0; i < sharesList.size(); i++)
        {
            AssembleReasonItemLayout layout = new AssembleReasonItemLayout(this);
            layout.initData(sharesList.get(i).getAssembleName(),
                    StringHelper.formatString(sharesList.get(i).getShare(),
                            StringFormat.FORMATE_2),
                    StringHelper.formatString(sharesList.get(i).getShareMoney(),
                            StringFormat.FORMATE_2),
                    null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
            contentView.addView(layout);
        }
    }

    private void requestRedeem(String pwd)
    {
        showLoading();
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("sid", redeemModel.getsId());
        params.put("sum", sum);
        params.put("pwd", pwd);
        if (isVirtual)
        {
            AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_FUND_REDEEM, new HttpCallBack()
            {
                @Override
                public void back(String data, int status)
                {
                    dismissLoading();
                    if (data == null || data.equals(""))
                    {
                        showHintDialog("网络不给力!");
                    }
                    else
                    {
                        mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
                    }
                }
            }, params);
        }
        else
        {
            params.put("radio", radio);
            AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_REDEEM_V3, new HttpCallBack()
            {
                @Override
                public void back(String data, int status)
                {
                    dismissLoading();
                    if (data == null || data.equals(""))
                    {
                        showHintDialog("网络不给力!");
                    }
                    else
                    {
                        mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
                    }
                }
            }, params);
        }
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    handleRedeem(msg.obj.toString());
                    break;
                case 0x02:
                    handleVirtualredeem(msg.obj.toString());
            }
        };
    };

    private void handleVirtualredeem(String strJson)
    {
        try
        {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            switch (ecode)
            {
                case 0:
                    JSONObject data = object.optJSONObject("data");
                    JSONObject sechemLog = data.optJSONObject("schemaLog");
                    int state = sechemLog.optInt("state");
                    switch (state)
                    {
                    /**
                     * 赎回成功
                     */
                        case 3:
                            double fee = sechemLog.optDouble("fee");
                            double amount = sechemLog.optDouble("amount");
                            Intent intent = new Intent(this, AssembleRedeemResultActivity.class);
                            intent.putExtra("sum", sum);
                            intent.putExtra("fee", String.valueOf(fee));
                            intent.putExtra("amount", amount);
                            intent.putExtra("type", 0);
                            startActivityForResult(intent, Const.ASSEMBLE_REDEEM_FLOW);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    showHintDialog(emsg);
                    break;
            }

        } catch (Exception e)
        {
            showHintDialog("数据解析异常!");
            finish();
        }
    }

    private void handleRedeem(String strJson)
    {
        try
        {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            switch (ecode)
            {
                case 0:
                    JSONObject data = object.optJSONObject("data");
                    JSONObject sechemLog = data.optJSONObject("schemaLog");
                    /**
                     * 统计部分失败原因
                     */
                    JSONArray abbrevs = sechemLog.optJSONArray("abbrevs");
                    JSONArray fdstates = sechemLog.optJSONArray("fdstates");
                    JSONArray reasons = sechemLog.optJSONArray("reasons");
                    JSONArray fdShares = sechemLog.optJSONArray("fdshares");
                    stateList = new ArrayList<FundItemState>();
                    int isPart = 0;
                    for (int i = 0; i < fdstates.length(); i++)
                    {
                        /**
                         * 统计失败次数
                         */
                        if (fdstates.optInt(i) == 4)
                        {
                            isPart++;
                        }
                        FundItemState responseItemInfo = new FundItemState(abbrevs.optString(i),
                                fdstates.optInt(i),
                                reasons.optString(i), fdShares.optString(i));
                        stateList.add(responseItemInfo);
                    }
                    // Log.e("---->isPart:", isPart + "XX");
                    long opationDate = sechemLog.optLong("opDate");
                    long confirmTime = sechemLog.optLong("confirm_time");
                    long arriveTime = sechemLog.optLong("arrive_time");
                    String confirmDay = sechemLog.optString("confirm_day"); // 基金公司确认时间
                    String arriveDay = sechemLog.optString("arrive_day"); // 到账时间
                    String fee = sechemLog.optString("estimate_fee"); // 预估手续费
                    String sumMoney = sechemLog.optString("estimate_sum"); // 预估到账
                    Intent intent = new Intent(this, AssembleRedeemResultActivity.class);
                    intent.putExtra("state", stateList);
                    intent.putExtra("opdate", opationDate);
                    intent.putExtra("sum", sumMoney);
                    intent.putExtra("fee", fee);
                    intent.putExtra("confirm_time", confirmTime);
                    intent.putExtra("arrive_time", arriveTime);
                    intent.putExtra("confirm_day", confirmDay);
                    intent.putExtra("arrive_day", arriveDay);
                    intent.putExtra("card", redeemModel.getCardName());
                    intent.putExtra("cardnumber", redeemModel.getCardNumber());
                    if (isPart == abbrevs.length())
                    {
                        intent.putExtra("type", REDEEM_FAILED);
                    }
                    if (isPart == 0)
                    {
                        intent.putExtra("type", REDEEM_ALL_SUCCESS);
                    }
                    else
                    {
                        intent.putExtra("type", REDEEM_PART_SUCCESS);
                    }
                    startActivityForResult(intent, Const.ASSEMBLE_REDEEM_FLOW);
                    break;
                case 132:
                    showHintDialog(getString(R.string.error_pwd));
                    break;
                default:
                    showHintDialog(emsg);
                    break;
            }
        } catch (JSONException e)
        {
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_back:
                finish();
                break;
            case R.id.redeem_btn_view:

                UMengStatics.onPRedeemPage2Password(this);

                redeemMethod();
                break;
        }
    }

    private void redeemMethod()
    {
        final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle(getString(R.string.assemble_redeem_confirm));
        inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String text = inputDialog.getEditText().getText().toString();
                if (StringHelper.isBlank(text))
                {
                    Toast.makeText(AssembleRedeemConfirmActivity.this, "输入登录密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    requestRedeem(text);
                }

                dialog.dismiss();
            }
        });
        inputDialog.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        // 赎回结果返回
        if (resultCode == ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE)
        {
            setResult(ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE);
            finish();
            return;
        }
    }
}
