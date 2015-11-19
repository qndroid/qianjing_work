
package com.qianjing.finance.ui.activity.assemble;

import com.qianjing.finance.model.p2p.P2PSteadyDetailReponse.P2PPortDetail;
import com.qianjing.finance.model.p2p.P2PSteadyItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.recommand.RecommandRedeemActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.ui.activity.history.SteadyHistoryActivity;
import com.qianjing.finance.ui.activity.profit.SteadyProfitActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qianjing.finance.view.indictorview.IndiactorView;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description TODO（描述这个类的作用）
 * @author liuchen
 * @date 2015年9月15日
 */

public class AssembleSteadyDetailActivity extends BaseActivity {

    /** 风险指示 */
    private IndiactorView indictorView;
    /** 基金配比环形图 */
    private PieGraph pieGraph;
    /** 基金列表 */
    private LinearLayout contentView;
    /** 组合详情实例 */
    private P2PPortDetail mAssembleDetail;

    public  int[] ARRAY_FUND_COLOR = new int[]
            { 0xff5ba7e1, 0xffb19ee0, 0xffe7a3e5, 0xff5a79b7, 0xffa6d0e6,
                    0xffb19ee0, 0xff5a79b7 };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        setContentView(R.layout.activity_assemble_detail);

        setTitleBack();
        setLoadingUncancelable();
        
        
        
        LinearLayout listContent = (LinearLayout) findViewById(R.id.ll_listcontent);
        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) listContent.getLayoutParams();
        layoutParams.topMargin = 0;
        listContent.setLayoutParams(layoutParams);
        
        indictorView = (IndiactorView) findViewById(R.id.indictor_view);
        indictorView.setVisibility(View.GONE);
        contentView = (LinearLayout) findViewById(R.id.content_view);
        pieGraph = (PieGraph) findViewById(R.id.pie_graph);
        findViewById(R.id.rl_rebalance_reason).setVisibility(View.GONE);
        findViewById(R.id.tl_hold).setVisibility(View.GONE);
        TextView unpaidTitle = (TextView) findViewById(R.id.tv_unpaid_title);
        TextView profitTitle = (TextView) findViewById(R.id.tv_profit_title);
        unpaidTitle.setText("累计存入(元)");
        profitTitle.setText("累计收益(元)");
        findViewById(R.id.ll_trade).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AssembleSteadyDetailActivity.this,SteadyHistoryActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_trade_profit).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AssembleSteadyDetailActivity.this,SteadyProfitActivity.class);
                startActivity(intent);
            }
        });
        
        
        Button tv_buy = (Button) findViewById(R.id.tv_buy);
        Button tv_reedom = (Button) findViewById(R.id.tv_reedom);
        tv_buy.setText("存入");
        tv_reedom.setText("取现");
        tv_buy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                requestToken();
            }
        });

        tv_reedom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // TODO 跳转到赎回金额界面
                if (mAssembleDetail != null) {
                    Intent reedemIntent = new Intent(AssembleSteadyDetailActivity.this,
                            RecommandRedeemActivity.class);
                    reedemIntent.putExtra("max_redeem", mAssembleDetail.assets+"");
                    startActivity(reedemIntent);
                }
            }
        });
        
        // TODO 跳转到交易记录
        View ll_trade = (View) findViewById(R.id.ll_trade);
        ll_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssembleSteadyDetailActivity.this,
                        SteadyHistoryActivity.class);
                startActivity(intent);
            }
        });
        
        // 初始化组合详情数据
        initAssembleDetail();
    }
    
    private void requestToken()
    {
        showLoading();
        AnsynHttpRequest.requestByPost(this, UrlConst.P2P_TOKEN, new HttpCallBack()
        {
            @Override
            public void back(String data, int status)
            {
                if (data != null && !data.equals(""))
                {
                    mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
                }
                else
                {
                    mHandler.sendEmptyMessage(0x02);
                }
            }
        }, null);
    }
    
    protected void handleToken(String string)
    {
        dismissLoading();
        try
        {
            JSONObject object = new JSONObject(string);
            int ecode = object.optInt("ecode");
            String reason = object.optString("emsg");
            switch (ecode)
            {
                case 0:
                    JSONObject jsonObj = object.optJSONObject("data");
                    String mToken = jsonObj.optString("tk");
                    /**
                     * 拿到token,拼接到地址中，跳转到wap页面
                     */
                    StringBuilder url = new StringBuilder(UrlConst.P2P_RECOMMAND_BUY);
                    url.append(mAssembleDetail.bid).append("&tk=").append(mToken);
                    Intent intent = new Intent(this, WebActivity.class);
                    intent.putExtra("webType", 8);
                    intent.putExtra("url", url.toString());
                    startActivity(intent);
                    
                    break;
                default:
                    showHintDialog(reason);
                    break;
            }
        } catch (Exception e)
        {
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            switch (msg.what) {
                case 0x03:
                    handleToken(jsonData);
                    break;
                case 0x02:
                    dismissLoading();
                    break;
            }
        };
    };

    private void initAssembleDetail() {

        mAssembleDetail = (P2PPortDetail) getIntent().getSerializableExtra(
                Flag.EXTRA_BEAN_P2P_PORT_DETAIL);
        
        if (mAssembleDetail == null) {
            showToast("初始化组合详情数据失败");
            finish();
            return;
        }
        
        setTitleWithString(mAssembleDetail.name);
        
        TextView tv_money = (TextView) findViewById(R.id.tv_money);
        TextView tv_total = (TextView) findViewById(R.id.tv_total);
        TextView tvunpaid = (TextView) findViewById(R.id.tv_unpaid);
        tv_money.setText(StringHelper.formatDecimal(mAssembleDetail.assets + ""));
        tv_total.setText(StringHelper.formatDecimal(mAssembleDetail.profit + ""));
        tvunpaid.setText(StringHelper.formatDecimal(mAssembleDetail.order + ""));
        TextView tv_buying = (TextView) findViewById(R.id.tv_buying);
        tv_buying.setVisibility(View.INVISIBLE);
        
        ArrayList<P2PSteadyItem> listFund = (ArrayList<P2PSteadyItem>) mAssembleDetail.listItem;
        ArrayList<PieSlice> listSlice = new ArrayList<PieSlice>();
        for (int i = 0; i < listFund.size(); i++) {
            P2PSteadyItem fund = listFund.get(i);
            contentView.addView(createFundItem(this, fund, i), i);
            listSlice.add(new PieSlice((float) fund.ratio, ARRAY_FUND_COLOR[i]));
        }
        pieGraph.setDrawText(mAssembleDetail.bondName,
                (int) mAssembleDetail.bondRatio + "%", mAssembleDetail.otherName,
                (int) mAssembleDetail.otherRatio + "%", listSlice);

    }

    public static AssembleNewItemLayout createFundItem(final Activity activity,
            final P2PSteadyItem fund, int i) {
        AssembleNewItemLayout fundItem = new AssembleNewItemLayout(activity);
        fundItem.initData(
                ViewUtil.ARRAY_FUND_COLOR[i],
                fund.name,
                "",
                StringHelper.formatDecimal(String.valueOf(fund.ratio)));
        fundItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return fundItem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
