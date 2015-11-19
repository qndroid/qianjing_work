
package com.qianjing.finance.ui.activity.wallet;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.util.WriteLog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 活期宝收益页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class WalletProfitActivity extends BaseActivity implements OnClickListener {
    private static final int MAX_PRECENT = 100;
    private Button backBtn;
    private TextView titleTextView;
    private TextView profitView;
    private LinearLayout contentLayout;
    /**
     * data
     */
    private String profitYesterdDay;
    private ArrayList<Profit> profitList = new ArrayList<Profit>();
    private double profitMax = 0.01;
    private Profit profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.walletprofitactivity);

        initData();
        initView();
        requestProfit();
    }

    private void initData() {
        Intent intent = getIntent();
        profitYesterdDay = intent.getStringExtra("profit");
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.bt_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title_middle_text);
        contentLayout = (LinearLayout) findViewById(R.id.ll_addview);
        profitView = (TextView) findViewById(R.id.tv_profidnum);

        titleTextView.setText(R.string.str_everydayprofit);
        profitView.setText(profitYesterdDay);
    }

    private void requestProfit() {
        showLoading();
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_EVERYDAY_PROFIT,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        myHandler.sendMessage(myHandler.obtainMessage(1, data));
                    }
                }, null);
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    handleProfitEveryday(msg.obj.toString());
                    break;
            }
        }
    };

    private void handleProfitEveryday(String strJson) {
        dismissLoading();
        if (strJson == null || "".equals(strJson)) {
            showHintDialog("网络不给力！");
            return;
        }
        // 活期宝总资产数据
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            if (ecode == 0) {
                JSONObject data = object.optJSONObject("data");
                JSONArray profit_list = data.optJSONArray("profit_list");
                JSONObject itemObj;
                for (int i = 0; i < profit_list.length(); i++) {
                    itemObj = profit_list.getJSONObject(i);
                    if (itemObj.optDouble("profit") > profitMax) {
                        profitMax = itemObj.optDouble("profit");
                    }

                    profitList.add(new Profit(itemObj.optString("dt"), itemObj
                            .optDouble("profit")));
                }
                /*
                 * 更新UI
                 */
                updateUI();
            } else {
                dismissLoading();
                showHintDialog(emsg);
            }
        } catch (JSONException e) {
            dismissLoading();
            showHintDialog("数据解析异常");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        } catch (NumberFormatException e) {
            dismissLoading();
            showHintDialog("数据解析异常");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }
    }

    private void updateUI() {
        for (int i = 0; i < profitList.size(); i++) {
            contentLayout.addView(createItemLayout(i));
        }
    }

    /**
     * 创建收益项
     * 
     * @param index
     */
    private LinearLayout createItemLayout(int index) {
        profit = profitList.get(index);
        LinearLayout itemView = (LinearLayout) View.inflate(this,
                R.layout.walletprofit_item, null);
        TextView leftTextView = (TextView) itemView.findViewById(R.id.tv_left);
        TextView middleTextView = (TextView) itemView
                .findViewById(R.id.tv_middle);
        TextView rightTextView = (TextView) itemView
                .findViewById(R.id.tv_right);

        leftTextView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0));
        middleTextView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                (float) (profit.getProifit() / profitMax * MAX_PRECENT)));
        rightTextView
                .setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        MAX_PRECENT
                                - (float) (profit.getProifit() / profitMax * MAX_PRECENT)));

        leftTextView.setText(DateFormatHelper.formatDate(profit
                .getDt().concat("000"), DateFormat.DATE_7)
                + "  "
                + StringHelper.formatString(String.valueOf(profit.getProifit()),
                        StringFormat.FORMATE_2));
        if (index != 0) {
            leftTextView.setBackgroundResource(R.drawable.gray_profit);
            middleTextView.setBackgroundResource(R.drawable.gray_profit);
        }
        return itemView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:// 返回
                finish();
                break;
        }
    }

    /**********************************************************
     * @文件名称：Profit.java
     * @文件作者：fanyan
     * @创建时间：2015年6月4日 上午10:37:55
     * @文件描述：活期宝收益单个列表Item对象
     * @修改历史：2015年6月4日创建初始版本
     **********************************************************/
    private class Profit {
        private String dt;
        private double proifit;
        private double profitMax;

        public Profit(String dt, double profit) {
            this.dt = dt;
            this.proifit = profit;
        }

        public String getDt() {
            return dt;
        }

        public double getProifit() {
            return proifit;
        }

        @Override
        public String toString() {
            return "Profit [dt=" + dt + ", proifit=" + proifit + ", profitMax="
                    + profitMax + "]";
        }
    }
}
