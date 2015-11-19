
package com.qianjing.finance.ui.activity.purchase;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.ViewHolder;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * <p>
 * Title: AssembleBuyDetailActivity
 * </p>
 * <p>
 * Description: 组合申购详情页面
 * </p>
 * <p>
 * Company: www.qianjing.com
 * </p>
 * 
 * @author liuchen
 * @date 2015年4月27日
 */
public class PurchaseConfirmActivity extends BaseActivity {

    private ListView lv_buy_details;
    private BuyDetailsAdapter detailAdapter;
    private Button nextBuy;
    private PurchaseModel pModel;

    private ArrayList<String> fundNameList = new ArrayList<String>();
    private HashMap<String, Float> fundMap = new HashMap<String, Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        setContentView(R.layout.activity_assemble_buy_detail);
        setTitleWithId(R.string.title_assemble_buy_detail);
        setTitleBack();

        sumValue = getIntent().getFloatExtra("sum", 0);
        pModel = (PurchaseModel) getIntent().getSerializableExtra("purchaseInfo");

        lv_buy_details = (ListView) findViewById(R.id.lv_buy_details);
        TextView groupName = (TextView) findViewById(R.id.tv_group_name);
        TextView feeWay = (TextView) findViewById(R.id.tv_fee_way);
        TextView payWay = (TextView) findViewById(R.id.tv_pay_way);

        groupName.setText(pModel.getSchemaName());
        feeWay.setText(pModel.getFeeWay());
        payWay.setText(pModel.getPayWay());

        nextBuy = (Button) findViewById(R.id.btn_confirm);
        nextBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });

        initAssembleConfigData();

        initAssembleName();
    }

    /**
     * <p>
     * Title: initAssembleConfigData
     * </p>
     * <p>
     * Description: 初始化组合配置数据源
     * </p>
     */
    private void initAssembleConfigData() {
        ArrayList<Fund> infos = getIntent().getParcelableArrayListExtra("fundList");
        for (int i = 0; i < infos.size(); i++) {
            Fund info = infos.get(i);
            fundNameList.add(info.name);
            fundMap.put(info.name, (float) info.rate);
        }
        initAdapter();
    }

    /**
     * <p>
     * Title: initAssembleName
     * </p>
     * <p>
     * Description: 设置组合名称
     * </p>
     */
    private void initAssembleName() {

        String strAssembleName = "";
        AssembleBase assemble = (AssembleBase) getIntent().getParcelableExtra(
                Flag.EXTRA_BEAN_ASSEMBLE_BASE);
        if (assemble != null)
        {
            switch (assemble.getType())
            {
                case Const.ASSEMBLE_INVEST:
                    strAssembleName = getString(R.string.title_assemble_invest);
                    break;
                case Const.ASSEMBLE_PENSION:
                    strAssembleName = getString(R.string.title_assemble_pension);
                    break;
                case Const.ASSEMBLE_HOUSE:
                    strAssembleName = getString(R.string.title_assemble_house);
                    break;
                case Const.ASSEMBLE_CHILDREN:
                    strAssembleName = getString(R.string.title_assemble_children);
                    break;
                case Const.ASSEMBLE_MARRY:
                    strAssembleName = getString(R.string.title_assemble_marry);
                    break;
                case Const.ASSEMBLE_DREAM:
                    strAssembleName = getString(R.string.title_assemble_dream);
                    break;
            }
            TextView tvAssembleName = (TextView) findViewById(R.id.tv_group_name);
            if (!StringHelper.isBlank(strAssembleName))
                tvAssembleName.setText(strAssembleName + "组合");
        }
    }

    private void showPasswordDialog() {

        final InputDialog.Builder inputDialog = new InputDialog.Builder(this, null);
        inputDialog.setTitle("支付"
                + StringHelper.formatString(String.valueOf(sumValue),
                        StringFormat.FORMATE_2) + "元");
        inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = inputDialog.getEditText().getText().toString().trim();
                if (StringHelper.isBlank(text)) {
                    Toast.makeText(PurchaseConfirmActivity.this, "输入登录密码不能为空", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    switch (getIntent().getIntExtra(ViewUtil.ASSEMBLE_BUY_FLAG,
                            ViewUtil.ASSEMBLE_VIRTUAL_BUY))
                    {
                        case ViewUtil.ASSEMBLE_VIRTUAL_BUY:
                            requestPurchase(text);
                            break;
                        case ViewUtil.ASSEMBLE_VIRTUAL_ADD_BUY:
                            requestAddPurchase(text);
                            break;
                    }
                }
                ;

                dialog.dismiss();
            }
        });
        inputDialog.create().show();
    }

    private void initAdapter() {
        if (detailAdapter == null) {
            detailAdapter = new BuyDetailsAdapter();
            lv_buy_details.setAdapter(detailAdapter);
            Util.setListViewHeightBasedOnChildren(lv_buy_details);
        } else {
            detailAdapter.notifyDataSetChanged();
        }
    }

    // 虚拟组合初次申购
    private void requestPurchase(String pwd) {

        Hashtable<String, Object> upload = initParams(pwd);
        AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_BUY, new HttpCallBack() {
            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.obj = data;
                handler.sendMessage(msg);
            }
        }, upload);
    }

    // 虚拟组合追加申购
    private void requestAddPurchase(String pwd) {

        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sid", pModel.getSid());
        upload.put("sum", sumValue + "");
        upload.put("pwd", pwd);
        AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_ADD_BUY, new HttpCallBack() {
            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.obj = data;
                handler.sendMessage(msg);
            }
        }, upload);
    }

    private Hashtable<String, Object> initParams(String pwd) {

        showLoading();

        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        // upload.put("sid", getIntent().getStringExtra("sid")); //1.3.4版起更改组合申购接口定义
        upload.put("sum", String.valueOf(getIntent().getFloatExtra("sum", 0)));
        upload.put("pwd", pwd);
        /*
         * 添加计算组合接口的参数
         */
        AssembleBase assemble = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE);
        if (assemble != null) {
            upload.put("type", assemble.getType()); // 组合类型
            upload.put("nm", ""); // 组合名
            upload.put("init", getIntent().getFloatExtra("sum", 0)); // 初始投资金额
            upload.put("rate", 0);// 期望收益率

            switch (assemble.getType())
            {
                case Const.ASSEMBLE_INVEST:
                    upload.put("risk", assemble.getInvestRisk());// 风险偏好
                    upload.put("age", 0);// 年龄
                    upload.put("retire", 0);// 退休年龄
                    upload.put("month", 0);// 月定投金额
                    upload.put("money", 0);// 目标金额
                    upload.put("year", 0);// 投资年数
                    upload.put("nmonth", 0);// 投资月数
                    break;
                case Const.ASSEMBLE_PENSION:
                    upload.put("risk", 0);// 风险偏好
                    upload.put("age", assemble.getPensionCurrentAge());// 年龄
                    upload.put("retire", assemble.getPensionRetireAge());// 退休年龄
                    upload.put("month", assemble.getPensionMonthAmount());// 月定投金额
                    upload.put("money", 0);// 目标金额
                    upload.put("year", 0);// 投资年数
                    upload.put("nmonth", 0);// 投资月数
                    break;
                case Const.ASSEMBLE_HOUSE:
                    upload.put("risk", 0);// 风险偏好
                    upload.put("age", 0);// 年龄
                    upload.put("retire", 0);// 退休年龄
                    upload.put("month", 0);// 月定投金额
                    upload.put("money", assemble.getHouseInitSum());// 目标金额
                    upload.put("year", assemble.getHouseYears());// 投资年数
                    upload.put("nmonth", 0);// 投资月数
                    break;
                case Const.ASSEMBLE_CHILDREN:
                    upload.put("risk", 0);// 风险偏好
                    upload.put("age", 0);// 年龄
                    upload.put("retire", 0);// 退休年龄
                    upload.put("month", 0);// 月定投金额
                    upload.put("money", assemble.getChildGoalSum());// 目标金额
                    upload.put("year", assemble.getChildYears());// 投资年数
                    upload.put("nmonth", 0);// 投资月数
                    break;
                case Const.ASSEMBLE_MARRY:
                    upload.put("risk", 0);// 风险偏好
                    upload.put("age", 0);// 年龄
                    upload.put("retire", 0);// 退休年龄
                    upload.put("month", 0);// 月定投金额
                    upload.put("money", assemble.getMarryGoalSum());// 目标金额
                    upload.put("year", assemble.getMarryYears());// 投资年数
                    upload.put("nmonth", 0);// 投资月数
                    break;
                case Const.ASSEMBLE_DREAM:

                    upload.put("init", assemble.getDreamInitSum());// 退休年龄
                    // upload.put("month", assemble.getDreamMonths());// 月定投金额
                    upload.put("nmonth", assemble.getDreamMonths());// 投资月数
                    break;
                case Const.ASSEMBLE_SPESIAL:

                    LogUtils.syso(assemble.toString());
                    upload.put("age", assemble.getSpecialAge());
                    upload.put("init", assemble.getSpecialInitSum());
                    upload.put("risk", assemble.getSpecialRisk());
                    upload.put("preference", assemble.getSpecialPref());
                    upload.put("money", assemble.getSpecialMoney());
                    break;
            }
        }
        return upload;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            String jsonData = (String) msg.obj;
            LogUtils.syso("虚拟申购确认详情:" + jsonData);
            analysisVirtualPurchase(jsonData);
        };
    };
    private float sumValue;

    private void analysisVirtualPurchase(String jsonData) {

        dismissLoading();

        if (StringHelper.isBlank(jsonData)) {
            showHintDialog(getString(R.string.net_prompt));
            return;
        }

        try {
            JSONObject jObj = new JSONObject(jsonData);
            int ecode = jObj.optInt("ecode");
            String emsg = jObj.optString("emsg");
            JSONObject data = jObj.optJSONObject("data");
            if (ecode == 0) {
                JSONObject schemaLog = data.optJSONObject("schemaLog");

                Intent intent = new Intent(this, PurchaseVirtualResult.class);
                intent.putExtra("fee", schemaLog.optDouble("fee"));
                intent.putExtra("sum", schemaLog.optDouble("sum"));
                intent.putExtra("amount", schemaLog.optDouble("amount"));
                dismissLoading();
                startActivityForResult(intent, CustomConstants.VIRTUAL_PUTCHASE);
                setResult(CustomConstants.SHUDDOWN_ACTIVITY);
            } else {
                showHintDialog(emsg);
                dismissLoading();
            }

        } catch (JSONException e) {
            dismissLoading();
            showHintDialog(getString(R.string.net_data_error));
        }
    }

    private class BuyDetailsAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            if (fundNameList.size() >= 4) {
                return fundNameList.size() + 1;
            }
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mApplication, R.layout.three_weight_item, null);
            }

            TextView fundName = ViewHolder.get(convertView, R.id.tv_handle);
            TextView fundShare = ViewHolder.get(convertView, R.id.tv_abbrev);
            TextView fundValue = ViewHolder.get(convertView, R.id.tv_shares);

            if (position == 0) {
                fundName.setText("基金名称");
                fundShare.setText("占比%");
                fundValue.setText("投入金额");
            } else {
                if (position > fundNameList.size()) {
                    fundName.setText("");
                    fundShare.setText("");
                    fundValue.setText("");
                } else {
                    fundName.setText(fundNameList.get(position - 1));
                    fundShare.setText(StringHelper.formatString(
                            String.valueOf(fundMap.get(fundNameList.get(position - 1)) * 100),
                            StringFormat.FORMATE_2));
                    fundValue.setText(StringHelper.formatString(
                            String.valueOf(fundMap.get(fundNameList.get(position - 1)) * sumValue),
                            StringFormat.FORMATE_2));
                }
            }
            return convertView;
        }
    }

}
