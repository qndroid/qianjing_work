
package com.qianjing.finance.ui.activity.assemble;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.assemble.AssembleAssets;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.AssembleFixed;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.fund.MyFundAssets;
import com.qianjing.finance.model.redeem.RedeemModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestRebalanceHelper;
import com.qianjing.finance.net.ihandle.IHandleBase;
import com.qianjing.finance.net.ihandle.IHandleRebalanceDetail;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.net.response.model.ResponseRebalanceDetail;
import com.qianjing.finance.ui.activity.assemble.aip.chargedetail.AIPChargeDetailActivity;
import com.qianjing.finance.ui.activity.assemble.aip.modify.AssembleAIPDetailActivity;
import com.qianjing.finance.ui.activity.assemble.profit.AssembelDayProfitActivity;
import com.qianjing.finance.ui.activity.assemble.redeem.AssembleRedeemDetailActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.ui.activity.history.AssembleHistoryActivity;
import com.qianjing.finance.ui.activity.rebalance.RebalanceActivity;
import com.qianjing.finance.ui.activity.rebalance.RebalanceHistoryDetails;
import com.qianjing.finance.util.Common;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.QJColor;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.AnimLScrollView;
import com.qianjing.finance.view.dialog.ActionSheet;
import com.qianjing.finance.view.dialog.ActionSheet.OnSheetItemClickListener;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author liuchen
 */
public class AssembleDetailActivity extends BaseActivity implements OnClickListener,
        AnimLScrollView.ScrollViewListener {

    
    /**
     * UI
     * */
    private TextView tvTotalValue;
    private TextView tvPurchasing;
    private TextView tvYetProfit;
    private TextView totalProfit;
    private TextView totalInvest;
    private TextView unpaidProfit;
    private LinearLayout llBottomBtn;
    private ImageView unpaidExplain;
    private LineChart lineView;
    private RelativeLayout rlRebalance;
    private TextView tvRebalanceIcon;

    /**
     * DATA 
     * */
    private String[] holdingTitle = {
            "基金名称", "最新市值(元)"
            , "市值占比", "最新净值(元)", "日涨跌"
    };
    private String[] fixedStateStr = {
            "正常", "暂停", "停止", "新增失败", "部分定投成功"
    };
    private int[] colors = {
            0XFFFD4634, 0XFF00D700
    };
    private float yAxisMax = -1.0f;
    private float yAxisMin = 100.0f;
    private float yAxisGap = 0f;
    private int yAxislabelNum = 5;
    private String sid;
    private boolean isScroll = false;
    /** 组合详情实例 */
    private AssembleDetail mAssembleDetail;
    private AssembleFixed assembleFixed;
    /**
     * 中灰字
     */
    private ArrayList<Integer> dateList = new ArrayList<Integer>();
    private ArrayList<String> profitList = new ArrayList<String>();
    /**
     * static 
     * */
    private static final int FLAG_ADD_CHART = 0;
    private static final int FLAG_ASSEMBLE_MIN_REEDOM = 1;
    private static final int FLAG_CARD_LIST = 2;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_detail_2_1);
        sid = getIntent().getStringExtra("sid");
        initView();
        requestHistoryProfit();
    }
    
    
    
    /** 
    * @description 初始化界面
    * @author liuchen
    */ 
    private void initView() {
        
        llBottomBtn = (LinearLayout) findViewById(R.id.ll_bottom_buttom);
        RelativeLayout rlProfit = (RelativeLayout) findViewById(R.id.rl_profit);
        rlProfit.setOnClickListener(this);
        tvTotalValue = (TextView) findViewById(R.id.tv_value);
        tvPurchasing = (TextView) findViewById(R.id.tv_purchasing);
        tvYetProfit = (TextView) findViewById(R.id.tv_yet_profit);
        totalProfit = (TextView) findViewById(R.id.tv_total_profit);
        totalInvest = (TextView) findViewById(R.id.tv_total_invest);
        unpaidProfit = (TextView) findViewById(R.id.tv_unpaid_profit);

        ImageView ivRebalance = (ImageView) findViewById(R.id.iv_rebalance);
        tvRebalanceIcon = (TextView) findViewById(R.id.tv_balance_icon);

        ivRebalance.setOnClickListener(this);

        rlRebalance = (RelativeLayout) findViewById(R.id.rl_rebalance_reason);
        rlRebalance.setOnClickListener(this);

        lineView = (LineChart) findViewById(R.id.lc_chart);
        setTitleBack();
        initAssembleDetail();

        ImageView imageRight = (ImageView) findViewById(R.id.title_right_image);
        imageRight.setImageResource(R.drawable.title_edit);
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setOnClickListener(this);

        View ll_trade = (View) findViewById(R.id.ll_trade);
        ll_trade.setOnClickListener(this);

        Button tv_buy = (Button) findViewById(R.id.tv_buy);
        Button tv_reedom = (Button) findViewById(R.id.tv_reedom);
        tv_buy.setOnClickListener(this);
        tv_reedom.setOnClickListener(this);

        /**
         * 未付收益说明
         */

        unpaidExplain = (ImageView) findViewById(R.id.iv_unpaid_explain);
        unpaidExplain.setOnClickListener(this);

        /**
         * 动画效果
         */
        AnimLScrollView alsvScroll = (AnimLScrollView) findViewById(R.id.alsv_scroll);
        alsvScroll.setOnScrollViewListener(this);

        /**
         * 当前持仓
         */
        LinearLayout llNameContent = (LinearLayout) findViewById(R.id.ll_fund_name);
        ListView lvHolding = (ListView) findViewById(R.id.lv_holding);
        if (mAssembleDetail != null) {
            ArrayList<Fund> fundList = mAssembleDetail.getAssembleConfig().getFundList();
            // 填充title
            for (int i = 0; i < fundList.size() + 1; i++) {

                View view = View.inflate(AssembleDetailActivity.this,
                        R.layout.item_assemble_holding_title, null);
                TextView fundName = (TextView) view.findViewById(R.id.tv_fund_name);
                TextView fundShare = (TextView) view.findViewById(R.id.tv_share);
                if (i == 0) {
                    fundShare.setVisibility(View.GONE);
                    fundName.setText(holdingTitle[0]);
                    // 设置中灰字
                    fundName.setTextColor(QJColor.MIDDLE_GREY.getColor());

                    RelativeLayout rlContentTitle = (RelativeLayout) view
                            .findViewById(R.id.rl_content_title);
                    android.view.ViewGroup.LayoutParams layoutParams = rlContentTitle
                            .getLayoutParams();
                    layoutParams.height = (int) getResources().getDimension(
                            R.dimen.holding_title_height);
                    rlContentTitle.setLayoutParams(layoutParams);

                } else {
                    Fund fund = fundList.get(i - 1);
                    fundName.setText(fund.name);
                    fundShare.setText(fund.shares + "份");
                }
                llNameContent.addView(view);
            }
            lvHolding.setAdapter(new HoldingAdapter(this, fundList));
            Util.setListViewHeightBasedOnChildren(lvHolding);
        }

        /**
         * 定投
         */
        assembleFixed = mAssembleDetail.getAssembleFixed();
        View inCludeFixed = findViewById(R.id.include_fixed);
        TextView fixedDate = (TextView) findViewById(R.id.tv_fixed_date);
        TextView fixedNextDate = (TextView) findViewById(R.id.tv_fixed_next_date);
        TextView fixedProcess = (TextView) findViewById(R.id.tv_fixed_process);
        TextView fixedState = (TextView) findViewById(R.id.tv_fixed_state);
        TextView fixedValue = (TextView) findViewById(R.id.tv_fixed_value);
        TextView fixedViewDetail = (TextView) findViewById(R.id.tv_fixed_view_details);
        TextView fixedDetails = (TextView) findViewById(R.id.tv_invest_details);
        ProgressBar fixedProgress = (ProgressBar) findViewById(R.id.tv_fixed_progressbar);
        fixedDetails.setOnClickListener(this);
        
        
        if (assembleFixed.getIsFixedExist() == 1) {

            fixedDate.setText(assembleFixed.getDay() + "日");
            fixedNextDate.setText(DateFormatHelper.formatDate(assembleFixed.getNext_day() + "000",
                    DateFormatHelper.DateFormat.DATE_7));
            fixedProcess.setText((int) (Float.parseFloat(assembleFixed.getSuccess_ratio()) * 100)
                    + "%");
            fixedValue.setText(assembleFixed.getMonth_sum());

            fixedState.setText(fixedStateStr[Integer.parseInt(assembleFixed.getState()) - 1]);
            fixedProgress
                    .setProgress((int) (Float.parseFloat(assembleFixed.getSuccess_ratio()) * 100));
            fixedViewDetail.setOnClickListener(this);

        } else {
            inCludeFixed.setVisibility(View.GONE);
        }
    }
    
    /** 
    * @description 初始化详情数据
    * @author liuchen
    */ 
    private void initAssembleDetail() {

        mAssembleDetail = getIntent().getParcelableExtra(
                Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
        
        if (mAssembleDetail == null) {
            showToast("初始化组合详情数据失败");
            finish();
            return;
        }

        tvTotalValue.setText(StringHelper.formatString(mAssembleDetail
                .getAssembleAssets().getAssets()
                , StringHelper.StringFormat.FORMATE_1));
        String subscripting = mAssembleDetail.getAssembleAssets().getSubscripting();
        if ("".equals(subscripting) || Float.parseFloat(subscripting) == 0) {
            tvPurchasing.setVisibility(View.GONE);
        } else {
            tvPurchasing.setText(StringHelper.formatString(subscripting
                    , StringHelper.StringFormat.FORMATE_2));
        }

        tvYetProfit.setText(StringHelper.formatString(mAssembleDetail
                .getAssembleAssets().getProfitYesday()
                , StringHelper.StringFormat.FORMATE_2));
        totalProfit.setText(StringHelper.formatString(mAssembleDetail
                .getAssembleAssets().getProfit()
                , StringHelper.StringFormat.FORMATE_2));
        totalInvest.setText(StringHelper.formatString(mAssembleDetail
                .getAssembleAssets().getInvest()
                , StringHelper.StringFormat.FORMATE_2));
        unpaidProfit.setText(StringHelper.formatString(mAssembleDetail
                .getAssembleAssets().getUnpaid()
                , StringHelper.StringFormat.FORMATE_2));

        AssembleConfig config = mAssembleDetail.getAssembleConfig();
        AssembleAssets assets = mAssembleDetail.getAssembleAssets();
        setTitleWithString(config.getSname());

        // TODO balance
        if (mAssembleDetail != null) {

            if (mAssembleDetail.getAssembleBase().getBalanceState() == 1) {
                int optState = mAssembleDetail.getAssembleBase()
                        .getBalanceOpState();
                if (optState == 0) {
                    rlRebalance.setVisibility(View.GONE);
                    // Toast.makeText(this, "隐藏"+optState, 1).show();
                } else if (optState == 1) {
                    rlRebalance.setVisibility(View.VISIBLE);
                    tvRebalanceIcon
                            .setBackgroundResource(R.drawable.sharp_rebalance_icon);
                    tvRebalanceIcon
                            .setText(getString(R.string.rebalance_right_now));
                    // Toast.makeText(this, "再平衡"+optState, 1).show();
                } else if (optState == 2) {
                    rlRebalance.setVisibility(View.VISIBLE);
                    tvRebalanceIcon
                            .setBackgroundResource(R.drawable.sharp_rebalance_icon_blue);
                    tvRebalanceIcon.setText(getString(R.string.rebalance_ing));
                    // Toast.makeText(this, "平衡中"+optState, 1).show();
                }
            } else {
                rlRebalance.setVisibility(View.GONE);
            }
        }
    }

    /** 
    * @description 请求近一个月的收益列表
    * @author liuchen
    */ 
    private void requestHistoryProfit() {
        showLoading();
        Hashtable<String, Object> table = new Hashtable<String, Object>();
        table.put("nm", 30);
        table.put("of", 0);
        table.put("sid", mAssembleDetail.getAssembleConfig().getSid());

        AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_DAY_PROFIT, new HttpCallBack() {

            @Override
            public void back(String data, int status) {
                // LogUtils.syso("sid"+sid+"累计收益qian:"+data);
                analysisHistoryProfit(data);

            }
        }, table);
    }

    /** 
    * @description 解析近一个月收益列表数据
    * @author liuchen
    * @param jsonStr 待解析的Json字符串
    */ 
    protected void analysisHistoryProfit(String jsonStr) {

        if (jsonStr == null || "".equals(jsonStr)) {
            showHintDialog(getString(R.string.net_prompt));
            return;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.optInt("ecode") == 0) {
                
                String data = jsonObj.optString("data");
                
                JSONArray optJSONArray = new JSONObject(data).optJSONArray("list");
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject Obj = optJSONArray.getJSONObject(i);
                    dateList.add(Obj.optInt("dt"));
                    profitList.add(Obj.optString("day_profit"));
                }
                
                /**
                 * 近一月累计盈亏走势
                 */
                dismissLoading();
                if (dateList != null && dateList.size() > 0) {
                    mHandler.sendEmptyMessage(FLAG_ADD_CHART);
                }

            } else {
                showHintDialog(jsonObj.optString("ecode"));
                dismissLoading();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dismissLoading();
        }
    }

    /** 
    * @description 请求银行卡列表
    */ 
    private void requestCardList() {
        showLoading();
        AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD_LIST,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = FLAG_CARD_LIST;
                        mHandler.sendMessage(msg);
                    }
                }, null);
    }

    /** 
    * @description 请求组合最小赎回金额
    */ 
    private void requestAssembleMinReedom() {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sid", mAssembleDetail.getAssembleConfig().getSid());
        AnsynHttpRequest.requestByPost(this, UrlConst.REDEMP_VIEW,
                new HttpCallBack() {
                    @Override
                    public void back(String data, int url) {
                        Message msg = new Message();
                        msg.obj = data;
                        msg.what = FLAG_ASSEMBLE_MIN_REEDOM;
                        mHandler.sendMessage(msg);
                    }
                }, upload);
    }

    /** 
     * @description 本页面的handler实例
     * @author liuchen
     * @date 2015年9月25日
     */ 
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String strResponse = (String) msg.obj;
            switch(msg.what){
                case FLAG_ADD_CHART:
                    addLineChartView();
                    break;
                case FLAG_ASSEMBLE_MIN_REEDOM:
                    handleMinReedomResponse(strResponse);
                    break;
                case FLAG_CARD_LIST:
                    handleBankCard(strResponse);
                    break;
            }
        }
    };

    /**
     * 解析最小赎回金额数据
     * */
    protected void handleMinReedomResponse(String obj) {
        
        dismissLoading();
        
        if (obj == null || "".equals(obj)) {
            showHintDialog(getString(R.string.net_prompt));
            return;
        }

        try {
            JSONObject object = new JSONObject(obj);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            if (ecode == 0) {
                JSONObject data = object.optJSONObject("data");
                Double total_assets = data.optDouble("total_assets");
                JSONObject interval = data.optJSONObject("interval");
                double min = interval.optDouble("min");
                double max = interval.optDouble("max");
                double minRange = data.optJSONObject("range").optDouble("min");
                double maxRange = data.optJSONObject("range").optDouble("max");
                JSONArray fundAssets = data.optJSONArray("assets");
                if (total_assets == 0) {
                    showHintDialog("您好，当前可赎回余额为0，无法赎回！");
                    return;
                }
                // 赎回
                ArrayList<MyFundAssets> fundAssetList = new ArrayList<MyFundAssets>();
                for (int i = 0; i < fundAssets.length(); i++) {
                    MyFundAssets asset = new MyFundAssets();
                    asset.setShares(fundAssets.getJSONObject(i).optString(
                            "shares"));
                    asset.setUsableShares(fundAssets.getJSONObject(i)
                            .optString("usable_shares"));
                    asset.setAbbrev(fundAssets.getJSONObject(i).optString(
                            "abbrev"));
                    asset.setNav(fundAssets.getJSONObject(i).optString("nav"));
                    fundAssetList.add(asset);
                }

                AssembleConfig config = mAssembleDetail.getAssembleConfig();
                Intent intent = new Intent(this,
                        AssembleRedeemDetailActivity.class);
                RedeemModel model = new RedeemModel();
                model.setsId(mAssembleDetail.getAssembleConfig().getSid());
                model.setAssembleName(config.getSname());
                model.setCardName(config.getBank());
                model.setCardNumber(config.getCard());
                model.setMinValue(min);
                model.setMaxValue(max);
                model.setMaxRange(maxRange);
                model.setMinRange(minRange);
                model.setUsableAsset(total_assets);
                model.setVirtual(false);
                model.setFundAssetsList(fundAssetList);
                intent.putExtra("redeemInfo", model);
                startActivityForResult(intent, ViewUtil.REQUEST_CODE);
            } else {
                showHintDialog(emsg);
            }
        } catch (JSONException e) {
        }
    }

    /**
     * 解析银行卡数据
     * */
    protected void handleBankCard(String strResponse) {

        dismissLoading();

        if (StringHelper.isBlank(strResponse)) {
            showHintDialog("网络不给力！");
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject(strResponse);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");

            if (ecode == 0) {

                JSONObject objData = jsonObj.optJSONObject("data");
                ArrayList<Card> listCard = Common.parseCardList(objData);

                AssembleConfig config = mAssembleDetail.getAssembleConfig();
                for (Card card : listCard) {
                    // 银行卡列表中有组合对应银行卡信息
                    if (TextUtils.equals(config.getCard(), card.getNumber())) {
                        Intent intent = new Intent(AssembleDetailActivity.this,
                                AssembleBuyActivity.class);
                        intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG,
                                ViewUtil.ASSEMBLE_ADD_BUY);
                        intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL,
                                mAssembleDetail);
                        intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listCard);
                        startActivityForResult(intent, ViewUtil.REQUEST_CODE);
                    }
                    // 组合对应银行卡已解绑
                    else {

                    }
                }

            } else {
                showHintDialog(emsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    

    /** 
    * @description 内部类  收益对应的adapter
    * @author liuchen
    * @date 2015年9月25日
    */ 
    
    private class HoldingAdapter extends BaseAdapter {

        private ArrayList<Fund> fundList;
        private Context context;

        public HoldingAdapter(Context context, ArrayList<Fund> fundList) {
            this.context = context;
            this.fundList = fundList;
        }

        @Override
        public int getCount() {
            return fundList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.item_assemble_holding, null);

            TextView tvData1 = (TextView) view.findViewById(R.id.tv_data1);
            TextView tvData2 = (TextView) view.findViewById(R.id.tv_data2);
            TextView tvData3 = (TextView) view.findViewById(R.id.tv_data3);
            TextView tvData4 = (TextView) view.findViewById(R.id.tv_data4);

            if (position == 0) {

                RelativeLayout rlContent = (RelativeLayout) view.findViewById(R.id.rl_content);
                android.view.ViewGroup.LayoutParams layoutParams = rlContent.getLayoutParams();
                layoutParams.height = (int) getResources().getDimension(
                        R.dimen.holding_title_height);
                rlContent.setLayoutParams(layoutParams);

                tvData1.setTextColor(QJColor.MIDDLE_GREY.getColor());
                tvData2.setTextColor(QJColor.MIDDLE_GREY.getColor());
                tvData3.setTextColor(QJColor.MIDDLE_GREY.getColor());
                tvData4.setTextColor(QJColor.MIDDLE_GREY.getColor());
                
                tvData1.setText(holdingTitle[1]);
                tvData2.setText(holdingTitle[2]);
                tvData3.setText(holdingTitle[3]);
                tvData4.setText(holdingTitle[4]);

            } else {

                Fund fund = fundList.get(position - 1);

                tvData1.setText(fund.marketValue + "");
                tvData2.setText(fund.ratio);
                tvData3.setText(fund.nav + "");
                tvData4.setText(fund.date_rate);
            }

            return view;
        }
    }

    /** 
    * @description 添加图表方法
    * @author liuchen
    */ 
    private void addLineChartView() {

        Resources resource = getResources();

        lineView.setDescription("");
        lineView.setScaleEnabled(false);
        lineView.getAxisRight().setEnabled(true);
        lineView.setDrawGridBackground(true);
        lineView.setTouchEnabled(true);
        lineView.getLegend().setEnabled(false);
        // lineView.setHardwareAccelerationEnabled(true);
        lineView.setGridBackgroundColor(0XFFEDF4FC);
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        lineView.setMarkerView(mv);
        // lineView.setBackgroundColor(0XFFEDF4FC);
        lineView.setAutoScaleMinMaxEnabled(false);
        lineView.setHighLightIndicatorEnabled(false);

        ArrayList<Entry> yRawData = new ArrayList<Entry>();
        ArrayList<String> xRawDatas = new ArrayList<String>();
        int index = 0;
        int size = dateList.size();
        for (int i = 0; i < size; i++) {
            yRawData.add(new Entry(Float.parseFloat(profitList.get(i)), index));
            if (i != 0) {
                xRawDatas.add(DateFormatHelper.formatDate(dateList.get(size - i - 1)
                        + "000",
                        DateFormat.DATE_1));
            } else {
                xRawDatas.add("");
            }
            index++;
            initMaxMin(Float.parseFloat(profitList.get(i)));
        }
        /**
         * x轴样式设置
         */
        XAxis xAxis = lineView.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);// 设置x轴在底部显示
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setSpaceBetweenLabels(0); // x轴间距
        xAxis.setTextColor(resource.getColor(R.color.grey_low_txt));
        xAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(resource.getColor(R.color.color_dddddd));
        xAxis.setLabelsToSkip(3);
        /**
         * y轴样式设置
         */
        YAxis leftAxis = lineView.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setLabelCount(yAxislabelNum, true);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setTextColor(resource.getColor(R.color.grey_low_txt));
        leftAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(resource.getColor(R.color.color_dddddd));
        // leftAxis.setXOffset(10);

        yAxisGap = (yAxisMax - yAxisMin) / 8;
        float yAxisM = Math.abs(yAxisMax + yAxisGap) > Math.abs(yAxisMin - yAxisGap)
                ? Math.abs(yAxisMax + yAxisGap) : Math.abs(yAxisMin - yAxisGap);
        if (yAxisM < 1) {
            yAxisM = 1f;
        }
        
        leftAxis.setAxisMaxValue(yAxisM);
        leftAxis.setAxisMinValue(-yAxisM);
        
        YAxis rightAxis = lineView.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
        /**
         * 曲线样式设置
         */
        LineDataSet set = new LineDataSet(yRawData, "");
        // 允许平滑
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setLineWidth(Util.dip2px(this, 1.0f));
        set.setColor(0XFF6CAAF3);
        set.setCircleSize(Util.dip2px(this, 1.0f));
        set.setFillColor(0XFFABCBED);
        set.setFillAlpha(128);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setDrawDoubleColor(true);
        set.setCircleColors(colors);
        set.setFillColor(0XFFABCBED);
        set.setDrawFillMiddleValueEnable(true);
        set.setDrawFillMiddleValue(-1000000);

        set.setDrawHighlightIndicators(true);

        if (yAxisMax > 1000 || yAxisMin < -1000) {
            set.setCubicIntensity(0.15f);
        } else {
            set.setCubicIntensity(0.05f);
        }

        LineData data = new LineData(xRawDatas, set);
        lineView.setData(data);
        lineView.invalidate();
    }

    /** 
    * @description 内部类 图表对应的游标
    * @author liuchen
    * @date 2015年9月25日
    */ 
    
    public class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
            } else {
                tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
            }
        }

        @Override
        public void refreshContent(Entry arg0, Highlight arg1, String arg2) {
        }
    }

    /**
     * 获取最大最小年化率
     * @param currentNum
     */
    private void initMaxMin(float currentNum) {
        if (currentNum >= yAxisMax) {
            yAxisMax = currentNum;
        }

        if (currentNum < yAxisMin) {
            yAxisMin = currentNum;
        }
    }
    
    
    /**
     * 处理页面的点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_profit:
                jump2ProfitActivity();
                break;
            case R.id.iv_rebalance:
                showBalanceSureDialog();
                break;
            case R.id.rl_rebalance_reason:
                jumpToRebalanceDetail();
                break;
            case R.id.title_right_image:
                showAssembleMenu();
                break;
            case R.id.ll_trade:
                jump2AssembleHistory();
                break;
            case R.id.tv_buy:
                jump2Buy();
                break;
            case R.id.tv_reedom:
                jump2Reedem();
                break;
            case R.id.iv_unpaid_explain:
                showUnpaidExplainDialog();
                break;
            case R.id.tv_invest_details:
                jump2FixedDetail();
                break;
            case R.id.tv_fixed_view_details:
                jump2ChargeDetail();
                break;
        }
    }

    /** 
    * @description 跳转到定投扣款详情
    * @author liuchen
    */ 
    private void jump2ChargeDetail() {
        Intent intent = new Intent(AssembleDetailActivity.this,
                AIPChargeDetailActivity.class);
        intent.putExtra("sid", assembleFixed.getSid());
        startActivity(intent);
    }



    /** 
    * @description 跳转到定投详情
    * @author liuchen
    */ 
    private void jump2FixedDetail() {
        Intent intent = new Intent(AssembleDetailActivity.this,AssembleAIPDetailActivity.class);
        intent.putExtra("sid", assembleFixed.getSid());
        startActivity(intent);
    }



    /** 
    * @description 显示未付收益说明文案
    * @author liuchen
    */ 
    private void showUnpaidExplainDialog() {
        showHintDialog(
                getString(R.string.unpaid_profit_title),
                getString(R.string.unpaid_profit),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                }, getString(R.string.zhi_dao_l));
    }

    /** 
    * @description 跳转到赎回流程
    * @author liuchen
    */ 
    private void jump2Reedem() {
        if (mAssembleDetail.getAssembleBase().getBalanceOpState() == 2) {
            showHintDialog(getString(R.string.rebalance_rp_explain));
        } else {
            requestAssembleMinReedom();
        }
    }

    /** 
    * @description 跳转到申购流程
    * @author liuchen
    */ 
    private void jump2Buy() {
        if (mAssembleDetail.getAssembleBase().getBalanceOpState() == 2) {
            showHintDialog(getString(R.string.rebalance_rp_explain));
        } else {
            requestCardList();
        }
    }

    /** 
    * @description 跳转到历史记录页面
    * @author liuchen
    */ 
    private void jump2AssembleHistory() {
        Intent intent = new Intent(AssembleDetailActivity.this,
                AssembleHistoryActivity.class);
        intent.putExtra("sid", mAssembleDetail.getAssembleConfig()
                .getSid());
        startActivity(intent);
    }
    
    /** 
    * @description 再平衡跳转逻辑
    * @author liuchen
    */ 
    private void jumpToRebalanceDetail() {

        if (mAssembleDetail != null) {
            // TODO 平衡中的闪退：没有取到sopid导致
            if (mAssembleDetail.getAssembleBase().getBalanceOpState() == 2) {
                // TODO params exception
                Intent balanceIntent = new Intent(
                        AssembleDetailActivity.this,
                        RebalanceHistoryDetails.class);
                balanceIntent.putExtra("mSopid", mAssembleDetail
                        .getAssembleBase().getSopid());
                startActivity(balanceIntent);
            } else {
                if (mAssembleDetail.getAssembleAssets() != null
                        && (!StringHelper.isBlankZero(mAssembleDetail
                                .getAssembleAssets().getSubscripting()) || !StringHelper
                                .isBlankZero(mAssembleDetail.getAssembleAssets()
                                        .getRedemping()))) {
                    showHintDialog(getString(R.string.rebalance_purchaseing_text));
                } else {

                    if (mAssembleDetail.getAssembleBase().getBalanceOpState() == 1) {
                        RequestRebalanceHelper.requestRebalanceDetail(AssembleDetailActivity.this,
                                mAssembleDetail
                                        .getAssembleBase().getSid(), new IHandleRebalanceDetail() {

                                    @Override
                                    public void handleResponse(ResponseRebalanceDetail response) {

                                        if (response.ecode == 0) {
                                            Intent balanceIntent = new Intent(
                                                    AssembleDetailActivity.this,
                                                    RebalanceActivity.class);
                                            balanceIntent.putExtra("mName", mAssembleDetail
                                                    .getAssembleBase().getName());
                                            balanceIntent.putExtra("mSid", mAssembleDetail
                                                    .getAssembleBase().getSid());
                                            startActivity(balanceIntent);
                                        } else {
                                            showHintDialog(response.strError);
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }

    /** 
    * @description 显示操作组合的底部menu
    * @author liuchen
    */ 
    private void showAssembleMenu() {
        ActionSheet actionSheet = new ActionSheet(
                AssembleDetailActivity.this).build()
                .setCancleable(false).setCancleOnTouchOutside(true);

        actionSheet.addSheetItem("再平衡",
                getResources().getColor(R.color.actionsheet_blue),
                new OnSheetItemClickListener() {

                    @Override
                    public void onClick(int which) {
                        if (mAssembleDetail.getAssembleBase().getBalanceOpState() == 1
                                || mAssembleDetail.getAssembleBase()
                                        .getBalanceOpState() == 2) {
                            jumpToRebalanceDetail();
                        } else {
                            showHintDialog(getString(R.string.can_not_rebalance));
                        }
                    }
                });

        actionSheet.addSheetItem("修改名称",
                getResources().getColor(R.color.actionsheet_blue),
                new OnSheetItemClickListener() {

                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(
                                AssembleDetailActivity.this,
                                AssembleModifyNameActivity.class);
                        intent.putExtra(
                                Flag.EXTRA_BEAN_ASSEMBLE_DETAIL,
                                mAssembleDetail);
                        startActivityForResult(intent,
                                ViewUtil.REQUEST_CODE);
                    }
                });
        actionSheet.show();
    }
    
    /** 
    * @description 关闭再平衡
    * @author liuchen
    * @param mAssembleDetail
    */ 
    private void closeBalanceItem(AssembleDetail mAssembleDetail) {
        
        RequestRebalanceHelper.requestRebalanceClose(
                AssembleDetailActivity.this, mAssembleDetail.getAssembleBase()
                        .getSid(), new IHandleBase() {
                    @Override
                    public void handleResponse(ResponseBase responseBase,
                            int status) {
                        if (responseBase.ecode == 0) {
                            rlRebalance.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /** 
    * @description 再平衡确认弹框
    * @author liuchen
    */ 
    private void showBalanceSureDialog() {
        showTwoButtonDialog(getString(R.string.balance_close_explain),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        if (mAssembleDetail != null) {
                            closeBalanceItem(mAssembleDetail);
                            dialog.dismiss();
                        }
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                });
    }

    /** 
    * @description 跳转到收益列表activity
    * @author liuchen
    */ 
    private void jump2ProfitActivity() {
        Intent intentP = new Intent(AssembleDetailActivity.this,
                AssembelDayProfitActivity.class);
        intentP.putExtra("sid", sid);
        AssembleDetailActivity.this.startActivity(intentP);
    }

    /**
     * 监听ScrollView改变
     * @see com.qianjing.finance.view.custom.AnimLScrollView.ScrollViewListener#onScrollChanged(android.widget.ScrollView, int, int, int, int)
     */
    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (!isScroll) {
            isScroll = true;
            Animation loadAnimation = AnimationUtils
                    .loadAnimation(AssembleDetailActivity.this, R.anim.actionsheet_out);
            llBottomBtn.startAnimation(loadAnimation);
        }
    }
    
    /**
     * 监听ScrollView停止
     * @see com.qianjing.finance.view.custom.AnimLScrollView.ScrollViewListener#onScrollViewStop()
     */
    @Override
    public void onScrollViewStop() {
        if (isScroll) {
            Animation loadAnimation = AnimationUtils
                    .loadAnimation(AssembleDetailActivity.this, R.anim.actionsheet_in);
            llBottomBtn.startAnimation(loadAnimation);
            isScroll = false;
        }
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // 修改名称
        if (resultCode == ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE) {
            if (data != null && data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL) != null) {
                mAssembleDetail = data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
                setTitleWithString(mAssembleDetail.getAssembleBase().getName());
                setResult(ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE);
            }
        }
        else if (resultCode == ViewUtil.RESULT_CODE || resultCode == ViewUtil.FIND_RESULT_CODE
                || resultCode == ViewUtil.MYASSETS_RESULT_CODE
                || resultCode == ViewUtil.SET_RESULT_CODE
                || resultCode == ViewUtil.LOGIN_RESULT_CODE) {
            setResult(resultCode, data);
            finish();
        }
        else if (resultCode == ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE) {
            setResult(ViewUtil.ASSEMBLE_REDEEM_RESULT_CODE);
            finish();
        }
        
        if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE){
            // 申购结果返回
            if (PrefManager.getInstance().getBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, true)){
                PrefManager.getInstance().putBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, false);
                // 显示对话框
                final LotteryActivity lottery = (LotteryActivity) data
                        .getSerializableExtra("lottery");
                if (lottery != null){
                    showTwoButtonDialog(getString(R.string.more_than_activity_title),
                            lottery.strMessage,
                            getString(R.string.get_red_bag), getString(R.string.zhi_dao_l),
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    // 去抽奖页面
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("webType", 8);
                                    bundle.putString("url", lottery.strUrl);
                                    AssembleDetailActivity.this.openActivity(WebActivity.class,
                                            bundle);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                }
                            });
                }
            }
        }
    }
    
    
}
