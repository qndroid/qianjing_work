
package com.qianjing.finance.ui.fragment.assets;

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
import com.qianjing.finance.model.assemble.AssembleAssets;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestAssembleHelper;
import com.qianjing.finance.net.ihandle.IHandleAssembleAssets;
import com.qianjing.finance.net.response.model.ResponseAssembleAssets;
import com.qianjing.finance.ui.activity.assemble.asset.AssembleAssetActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.MainActivity;
import com.qianjing.finance.ui.activity.common.P2PWebActivity;
import com.qianjing.finance.ui.activity.fund.asset.FundAssetActivity;
import com.qianjing.finance.ui.activity.fund.search.FundActivity;
import com.qianjing.finance.ui.activity.history.AllTradeHistory;
import com.qianjing.finance.ui.activity.profit.ProfitActivity;
import com.qianjing.finance.ui.activity.profit.SteadyProfitActivity;
import com.qianjing.finance.ui.activity.timedeposit.TimeDepositAssetActivity;
import com.qianjing.finance.ui.activity.wallet.WalletActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.QJColor;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/** 
* @description 组合总资产界面fragment
* @author liuchen
* @date 2015年9月24日
*/ 

public class AssembleFragment extends Fragment implements OnClickListener,
        SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = "AssetsFragment";
    /**
     * DATA
     * */
    private BaseActivity baseActivity;
    private static final int CANCEL_LOADING = 2;
    private static final int UPDATE_CHART = 1;
    private static final int UPDATE_ASSETS = 0;
    private static final int UPDATE_P2P_ASSETS = 3;
    private int[] isInvests = {
            -1, -1 ,-1
    };
    private String[] rules1 = {"."};
    private String[] rules2 = {":","."};
    
    private float yAxisMax = Float.MIN_VALUE;
    private float yAxisMin = Float.MAX_VALUE;
    private float yAxisGap = 0.1f;
    private boolean isBalance = false;
    private int balanceCount = 0;
    /**
     * UI
     * */
    private AssetsMarkerView mv;
    private LineChart lineView;
    private TextView startTime;
    private TextView endTime;
    private TextView updateTime;
    private TextView totalValue;
    private TextView profitTotal;
    private TextView purchasing;
    private TextView profitYest;
    private TextView assembleYestProfit;
    private TextView assembleValue;
    private TextView fundYestProfit;
    private TextView fundValue;
    private TextView baoProfit;
    private TextView baoValue;
    private TextView walletProfit;
    private TextView walletValue;
    private SwipeRefreshLayout qjsrl;
    private View balanceEnter;
    private TextView tvBalance;

    /** 
    * @fields profitDataList: 收益列表
    */ 
    ArrayList<HashMap<String, String>> profitDataList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        baseActivity.showLoading();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.activity_myasset_3_0, null);
        
        lineView = (LineChart) view.findViewById(R.id.lc_chart);
        qjsrl = (SwipeRefreshLayout) view.findViewById(R.id.srl_view);
        qjsrl.setColorSchemeColors(QJColor.PROFIT_RED.getColor());
        qjsrl.setOnRefreshListener(this);
        initView(view);
        return view;
    }
    
    /**
    * @description 初始化界面
    * @author liuchen
    * @param view
    */
    private void initView(View view) {
        
        startTime = (TextView) view.findViewById(R.id.tv_start_time);
        endTime = (TextView) view.findViewById(R.id.tv_end_time);
        updateTime = (TextView) view.findViewById(R.id.tv_update_date);
        totalValue = (TextView) view.findViewById(R.id.tv_assets_total_value);
        profitTotal = (TextView) view.findViewById(R.id.tv_assets_cumulative_value);
        purchasing = (TextView) view.findViewById(R.id.tv_purching);
        profitYest = (TextView) view.findViewById(R.id.profit_view);
        
        assembleYestProfit = (TextView) view.findViewById(R.id.yesterday_profit_view);
        assembleValue = (TextView) view.findViewById(R.id.tv_assemble_value);
        fundYestProfit = (TextView) view.findViewById(R.id.yesterday_fund_profit_view);
        fundValue = (TextView) view.findViewById(R.id.tv_fund_value);
        baoProfit = (TextView) view.findViewById(R.id.time_profit_view);
        baoValue = (TextView) view.findViewById(R.id.tv_bao_value);
        walletProfit = (TextView) view.findViewById(R.id.money_profit_view);
        walletValue = (TextView) view.findViewById(R.id.tv_wallet_value);
        balanceEnter = view.findViewById(R.id.in_balance_enter);
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        
        RelativeLayout rlAssemble = (RelativeLayout) view.findViewById(R.id.rl_assemble);
        RelativeLayout rlFund = (RelativeLayout) view.findViewById(R.id.rl_fund);
        RelativeLayout rlBao = (RelativeLayout) view.findViewById(R.id.rl_bao);
        RelativeLayout rlWallet = (RelativeLayout) view.findViewById(R.id.rl_wallet);

        RelativeLayout totalAssetsBtn = (RelativeLayout) view.findViewById(R.id.rl_total_assets);
        RelativeLayout totalProfitBtn = (RelativeLayout) view.findViewById(R.id.rl_total_profit);
        
        profitYest.setOnClickListener(this);
        totalProfitBtn.setOnClickListener(this);
        totalAssetsBtn.setOnClickListener(this);
        
        rlAssemble.setOnClickListener(this);
        rlFund.setOnClickListener(this);
        rlBao.setOnClickListener(this);
        rlWallet.setOnClickListener(this);
        
        requestProfitList();
        requestTotalAssetsData();
        requestP2pAssetsData();
        requestCombAssets();
        
    }
    
    private void requestCombAssets() {
        RequestAssembleHelper.requestAssembleAssets(baseActivity,
                new IHandleAssembleAssets() {
                    @Override
                    public void handleResponse(ResponseAssembleAssets response) {
                        
                        AssembleAssets assets = response.assets;
                        if (assets != null) {
                            if (assets.getBalanceState() == 1) {
                                isBalance = true;
                                balanceCount = assets.getBalanceCount();
                                balanceEnter.setVisibility(View.VISIBLE);
                                tvBalance.setText(String.format(getString(R.string.balance_list_title_one), balanceCount));
                            }else{
                                balanceEnter.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    /**
     * @description 近一个月盈亏变化信息请求解析
     * @author liuchen
     */
    private void requestProfitList() {
        AnsynHttpRequest.requestByPost(baseActivity, UrlConst.ASSETS_PROFIT_LIST,
                new HttpCallBack() {

                    @Override
                    public void back(String data, int status) {
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = UPDATE_CHART;
                        analysisProfitListData(data);
                        handler.sendMessage(msg);
                    }
                    
                }, null);
    }
    
    /**
     * @description 总资产请求与数据解析
     * @author liuchen
     */
    private void requestTotalAssetsData() {
        
        AnsynHttpRequest.requestByPost(baseActivity, UrlConst.ASSETS_TOTAL_DATA,
                new HttpCallBack() {
                    
                    @Override
                    public void back(String data, int status) {
                        
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = UPDATE_ASSETS;
                        handler.sendMessage(msg);
                        
                    }

                }, null);
    }
    
    /** 
    * @description 请求p2p资产信息
    * @author liuchen
    */ 
    private void requestP2pAssetsData(){
        
        AnsynHttpRequest.requestByPost(baseActivity, UrlConst.P2P_ASSETS_MSG,
                new HttpCallBack() {
                    
                    @Override
                    public void back(String data, int status) {
                        
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = UPDATE_P2P_ASSETS;
                        handler.sendMessage(msg);
                    }

                }, null);
        
    }
    
    
    /** 
    * @description 解析近一个月收益信息
    * @author liuchen
    * @param data JSON字符串
    */ 
    private void analysisProfitListData(String data) {
        if (data == null || "".equals(data)) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(getString(R.string.net_prompt));
            return;
        }
        
        try {
            
            JSONObject jsonObj = new JSONObject(data);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            
            if (ecode == 0) {
                
                JSONArray optJSONArray = jsonObj.optJSONArray("data");
                
                profitDataList.clear();
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject obj = optJSONArray.getJSONObject(i);
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("assetsG", obj.optString("assetsG"));
                    dataMap.put("day_profit", obj.optString("day_profit"));
                    dataMap.put("dt", obj.optString("dt"));
                    dataMap.put("uid", obj.optString("uid"));
                    
                    profitDataList.add(dataMap);
                }
                
                Message msg = Message.obtain();
                msg.what = CANCEL_LOADING;
                handler.sendMessageDelayed(msg, 1000);
            } else {
                baseActivity.dismissLoading();
                baseActivity.showHintDialog(emsg);
            }
        } catch (Exception e) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(baseActivity
                    .getString(R.string.net_data_error));
        }
    }
    
    /** 
    * @description 解析总资产信息
    * @author liuchen
    * @param data JSON字符串
    */ 
    private void analysisAssetsData(String data) {
//         LogUtils.syso("总资产信息:"+data);
        if (data == null || "".equals(data)) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(baseActivity.getString(R.string.net_prompt));
            return;
        }

        try {
            
            JSONObject jsonObj = new JSONObject(data);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            if (ecode == 0) {
                
                JSONObject objData = jsonObj.optJSONObject("data");
                JSONObject assembly = objData.optJSONObject("assembly");
                JSONObject bao = objData.optJSONObject("bao");
                JSONObject date = objData.optJSONObject("date");
                JSONObject fund = objData.optJSONObject("fund");
                JSONObject p2p = objData.optJSONObject("p2p");
                JSONObject total = objData.optJSONObject("total");

                applyAssembleData(assembly.optInt("is_invest"),
                        (float) assembly.optDouble("max_rate")
                        , (float) assembly.optDouble("profit_yesday")
                        , (float) assembly.optDouble("yesday_rate")
                        , (float) assembly.optDouble("assets")
                        , (float) assembly.optDouble("subscripting"));

                applyFundData(fund.optInt("is_invest"),
                        (float) fund.optDouble("max_rate")
                        , (float) fund.optDouble("profit_yesday")
                        , (float) fund.optDouble("yesday_rate")
                        , (float) fund.optDouble("assets")
                        , (float) fund.optDouble("subscripting"));

//                applyP2pData((float) p2p.optDouble("income"),
//                        (float) p2p.optDouble("market_value"));

                applyTime(date.optString("update_time"),
                        date.optString("next_update"));

                applyTotalData((float) total.optDouble("market_value")
                        , (float) total.optDouble("profit_yesday")
                        , (float) total.optDouble("profit")
                        , (float) total.optDouble("subscripting"));

                applyWalletData((float) bao.optDouble("rate7day"),
                        (float) bao.optDouble("market_value"));

                
            } else {
                baseActivity.dismissLoading();
                baseActivity.showHintDialog(emsg);
            }
        } catch (JSONException e) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(baseActivity
                    .getString(R.string.net_data_error));
        }
    }
    
    
    protected void analysisP2pAssets(String data) {
        if (data == null || "".equals(data)) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(baseActivity.getString(R.string.net_prompt));
            return;
        }
        try {
            
            JSONObject jsonObj = new JSONObject(data);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            if (ecode == 0) {
                
                JSONObject optJSONObject = jsonObj.optJSONObject("data");
                applyP2pData((float) optJSONObject.optDouble("avarRate"),
                        (float) optJSONObject.optDouble("yester"),optJSONObject.optInt("isLoad"));
                
            } else {
                baseActivity.dismissLoading();
                baseActivity.showHintDialog(emsg);
            }
        } catch (JSONException e) {
            baseActivity.dismissLoading();
            baseActivity.showHintDialog(baseActivity
                    .getString(R.string.net_data_error));
        }
   }
        
    

    
    /**
     * @description 更新总资产数据
     * @author liuchen
     * @param total     总资产数据
     * @param yestPro   昨日收益数据
     * @param totalPro  总收益数据
     * @param purch     申购中数据
     */
    protected void applyTotalData(float total, float yestPro, float totalPro, float purch) {

        String totalStr = StringHelper
                .formatString(total + "", StringHelper.StringFormat.FORMATE_1);
        String yestProStr = StringHelper.formatString(yestPro + "",
                StringHelper.StringFormat.FORMATE_1);
        String totalProStr = StringHelper.formatString(totalPro + "",
                StringHelper.StringFormat.FORMATE_1);
        String purchStr = StringHelper
                .formatString(purch + "", StringHelper.StringFormat.FORMATE_1);
        /**
         * 申购中的资产
         * */
        if (purch != 0) {
            purchasing.setText(getActivity().getString(R.string.purchasing) + ":" + purchStr);
        } else {
            purchasing.setVisibility(View.GONE);
        }

        totalValue.setText(StringHelper.spannableFormat(totalStr,rules1
                , Util.dip2px(getActivity(), 28),Color.WHITE, Util.dip2px(getActivity(), 15),Color.WHITE));
        
        
        profitYest.setText(StringHelper.spannableFormat(
                getActivity().getString(R.string.fund_yesterday_profit)+ ":     " + (yestPro>=0?"+":"")+yestProStr
                , rules2
                ,Util.dip2px(getActivity(), 18),QJColor.ASSETS_TEXT.getColor()
                , Util.dip2px(getActivity(), 20),(yestPro>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())
                ,Util.dip2px(getActivity(), 15),(yestPro>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())));
        
        
        /**
         * 总收益
         * */
        profitTotal.setText(StringHelper.spannableFormat((totalPro>=0?"+":"") + totalProStr
                ,rules1
                , Util.dip2px(getActivity(), 22),Color.WHITE
                , Util.dip2px(getActivity(), 15),Color.WHITE));    
       
    }

    /**
     * @description 更新组合数据
     * @author liuchen
     * @param isInvest    是否已有投资 1，已有投资 2，尚未投资 
     * @param maxRate     今年最高收益率
     * @param yestProfit  昨日组合盈亏
     * @param yestRate    昨日组合盈亏率
     * @param assets      组合总资产
     * @param purchasing  组合申购中的资产
     */
    private void applyAssembleData(int isInvest, float maxRate, float yestProfit, float yestRate,
            float assets, float purchasing) {
        
        isInvests[0] = isInvest;
        
        float yestProfitRate = (yestProfit*100)/(assets+purchasing);
        
        if (isInvest == 1) {
            assembleYestProfit.setText(String.format(
                    getActivity().getString(R.string.yest_profit_rate),
                    StringHelper.formatDecimal(yestProfitRate)+"%"));
        } else {
            assembleYestProfit.setText(String.format(getActivity().getString(R.string.max_profit),
                    StringHelper.formatDecimal(maxRate)));
        }
        
        assembleValue.setText(StringHelper.spannableFormat((assets>=0?"+":"") + StringHelper.formatDecimal(assets) + ""
                ,rules1
                , Util.dip2px(getActivity(), 22),(assets>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())
                , Util.dip2px(getActivity(), 15),(assets>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())));    
        
    }

    /**
     * @description 更新基金数据
     * @author liuchen
     * @param isInvest     是否已有投资 1，已有投资 2，尚未投资 
     * @param maxRate      基金今年最高收益率
     * @param yestProfit   昨日基金盈亏
     * @param yestRate     昨日基金盈亏率
     * @param assets       昨日基金总资产
     * @param purchasing   基金申购中的资产
     */
    private void applyFundData(int isInvest, float maxRate, float yestProfit, float yestRate,
            float assets, float purchasing) {

        isInvests[1] = isInvest;
        
        float yestProfitRate = (yestProfit*100)/(assets+purchasing);

        if (isInvest == 1) {
            fundYestProfit.setText(String.format(
                    getActivity().getString(R.string.yest_profit_rate),
                    StringHelper.formatDecimal(yestProfitRate)+"%"));
        } else {
            fundYestProfit.setText(String.format(
                    getActivity().getString(R.string.max_month_profit),
                    StringHelper.formatDecimal(maxRate)));
        }
        fundValue.setText(StringHelper.spannableFormat((assets>=0?"+":"") + StringHelper.formatDecimal(assets) + ""
                ,rules1
                , Util.dip2px(getActivity(), 22),(assets>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())
                , Util.dip2px(getActivity(), 15),(assets>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())));    

    }

    /**
     * @description 更新活期宝数据
     * @author liuchen
     * @param rate    七日年化收益率
     * @param value   活期宝总资产
     */
    private void applyWalletData(float rate, float value) {
        walletProfit.setText(String.format(getActivity().getString(R.string.seven_day_year),
                StringHelper.formatDecimal(rate)+"%"));
            
        walletValue.setText(StringHelper.spannableFormat((value>=0?"+":"") + StringHelper.formatDecimal(value) + ""
                ,rules1
                , Util.dip2px(getActivity(), 22),(value>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())
                , Util.dip2px(getActivity(), 15),(value>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())));    

    }

    /**
     * @description 更新p2p数据
     * @author liuchen
     * @param rate     七日年化收益率
     * @param value    定存宝总资产
     * @param isLoad 
     */
    private void applyP2pData(float rate, float value, int isLoad) {
        if(isLoad == 0){
            isInvests[2] = 0;
        }else{
            isInvests[2] = 1;
        }
        baoProfit.setText(String.format(getActivity().getString(R.string.max_year_profit),
                StringHelper.formatDecimal(rate)+"%"));

        baoValue.setText(StringHelper.spannableFormat((value>=0?"+":"") + StringHelper.formatDecimal(value) + ""
                ,rules1
                , Util.dip2px(getActivity(), 22),(value>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())
                , Util.dip2px(getActivity(), 15),(value>=0?QJColor.PROFIT_RED.getColor():QJColor.PROFIT_GREEN.getColor())));    
    }

    /**
     * @description 更新时间
     * @author liuchen
     * @param startTimeStr   更新时间
     * @param endTimeStr     下次更新时间
     */
    private void applyTime(String startTimeStr, String endTimeStr) {

        String updateTimeStr = String.format(
                getActivity().getResources().getString(R.string.update_time),
                DateFormatHelper.formatDate(startTimeStr + "000",
                        DateFormatHelper.DateFormat.DATE_9)
                , DateFormatHelper.formatDate(endTimeStr + "000",
                        DateFormatHelper.DateFormat.DATE_9));
        
        updateTime.setText(updateTimeStr);
        
    }

    
    
    /**
     * @description 添加图表View
     */

    private void addLineChartView() {

        Resources resource = getResources();

        lineView.setDescription("");
        lineView.setScaleEnabled(false);
        lineView.getAxisRight().setEnabled(true);
        lineView.setDrawGridBackground(true);
        lineView.setTouchEnabled(true);
        lineView.getLegend().setEnabled(false);
        lineView.setHardwareAccelerationEnabled(true);
        lineView.setGridBackgroundColor(0X00EDF4FC);
        lineView.setSpecialMarkerView(true);
        lineView.setSpecialMarkerCircle(BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_point_green));
        lineView.setHighlightAutoDismiss(true);
        //
        lineView.setForceTouchEventEnable(true);
        
        mv = new AssetsMarkerView(getActivity(), R.layout.custom_marker_assets_view);
        lineView.setMarkerView(mv);
        lineView.setHighLightIndicatorEnabled(false);
        
        ArrayList<Entry> yRawData = new ArrayList<Entry>();
        ArrayList<String> xRawDatas = new ArrayList<String>();
        /**
         * 设置图表数据
         */
        setChartData(yRawData, xRawDatas);
//        Toast.makeText(getActivity(), "yAxisMax"+yAxisMax+";yAxisMin"+yAxisMin, 1).show();
        /**
         * x轴样式设置
         */
        XAxis xAxis = lineView.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);// 设置x轴在底部显示
        //隐藏背景格子
        xAxis.setDrawGridLines(false);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        
        xAxis.setLabelsToSkip(5);
        /**
         * y轴样式设置
         */
        YAxis leftAxis = lineView.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setDrawLabels(false);
        //隐藏背景格子
        leftAxis.setDrawGridLines(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setLabelCount(2, true);
        leftAxis.setDrawAxisLine(false);
        yAxisGap = (yAxisMax - yAxisMin);
        leftAxis.setAxisMaxValue(yAxisMax+yAxisGap); // 设置Y轴最大值
        leftAxis.setAxisMinValue(yAxisMin-yAxisGap);// 设置Y轴最小值
        // leftAxis.setDrawGridLines(false);
        
        YAxis rightAxis = lineView.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
        /**
         * 曲线样式设置
         */
        LineDataSet set = new LineDataSet(yRawData, "");
        // 允许平滑
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setLineWidth(Util.dip2px(getActivity(), 1.0f));
        set.setColor(0XFFFF3B3B);
        // set.setCircleSize(dip2px(this, 2.0f));
        set.setFillAlpha(128);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setDrawCircleHole(false);
        set.setDrawCircles(true);
        // set.setDrawDoubleColor(true);
        // set.setCircleColors(colors);
        set.setFillColor(0XFFABCBED);
        set.setDrawFillMiddleValueEnable(true);
        set.setDrawFillMiddleValue(Float.MIN_VALUE);
        set.setDrawVerticalHighlightIndicator(true);
        set.setFillColorGradualChange(true);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setLastPointIndicator(true,
                BitmapFactory.decodeResource(getResources(), R.drawable.icon_popup));
        set.setLastPointBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.icon_red_point)
                , BitmapFactory.decodeResource(getResources(), R.drawable.icon_red_point));
        set.setLastPointIndecatorUnit("¥");
        
        // 设置平滑度
        set.setCubicIntensity(0.15f);
        LineData data = new LineData(xRawDatas, set);
        lineView.setData(data);
        lineView.invalidate();
    }
    
    /**
    * @description 设置图表数据
    * @author liuchen
    * @param yRawData   y轴数据
    * @param xRawDatas  x轴数据
    */ 
    private void setChartData(ArrayList<Entry> yRawData, ArrayList<String> xRawDatas) {
        
        if (profitDataList != null && profitDataList.size() > 0) {

            for (int i = 0; i < profitDataList.size(); i++) {
                
                if (i == 0) {
                    startTime.setText(DateFormatHelper.formatDate(
                            profitDataList.get(profitDataList.size() - 1 - i).get("dt")
                                    + "000",
                            DateFormat.DATE_1));
                } else if (i == profitDataList.size() - 1) {
                    endTime.setText(DateFormatHelper.formatDate(
                            profitDataList.get(profitDataList.size() - 1 - i).get("dt")
                                    + "000",
                            DateFormat.DATE_1));
                }
                String numStr = profitDataList.get(profitDataList.size() - 1 - i).get("assetsG");
                float num = Float.parseFloat(numStr);

                yRawData.add(new Entry(num, i));
                xRawDatas.add(DateFormatHelper.formatDate(
                        profitDataList.get(profitDataList.size() - 1 - i).get("dt")
                                + "000",
                        DateFormat.DATE_1));
                initMaxMin(num);
            }
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
     * 该页面的handler实例
     */
    Handler handler = new Handler() {
        
        public void handleMessage(Message msg) {
            String data = (String) msg.obj;
            int what = msg.what;
            switch (what) {
                case UPDATE_CHART:
                    analysisProfitListData(data);
                    break;
                case UPDATE_ASSETS:
                    analysisAssetsData(data);
                    break;
                case CANCEL_LOADING:
                    addLineChartView();
                    lineView.setVisibility(View.VISIBLE);
                    qjsrl.setRefreshing(false);
                    break;
                case UPDATE_P2P_ASSETS:
//                    LogUtils.syso("p2p资产信息:"+data);
                    analysisP2pAssets(data);
                    break;
            }
            baseActivity.dismissLoading();
        };
    };
    
    

    /**
     * 定义页面点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        
        switch (v.getId()) {
            case R.id.profit_view:
                // Intent fundassetIntent = new Intent(getActivity(),FundAssetActivity.class);
                // startActivity(fundassetIntent);
                break;
            case R.id.rl_total_profit:
                Intent profitIntent = new Intent(getActivity(), ProfitActivity.class);
                startActivity(profitIntent);
                break;
            case R.id.rl_assemble:
                if (isInvests[0] == -1) {
                    break;
                } else if (isInvests[0] == 1) {
                    Intent assembleIntent = new Intent(getActivity(), AssembleAssetActivity.class);
                    assembleIntent.putExtra("isBalance", isBalance);
                    assembleIntent.putExtra("balanceCount", balanceCount);
                    startActivity(assembleIntent);
                } else {
                    ((MainActivity) getActivity()).setTabSelection(ViewUtil.MAIN_TAB_QUICK);
                }
                break;
            case R.id.rl_fund:
                if (isInvests[1] == -1) {
                    break;
                } else if (isInvests[1] == 1) {
                    Intent fundassetIntent = new Intent(getActivity(), FundAssetActivity.class);
                    startActivity(fundassetIntent);
                } else {
                    Intent fundassetIntent = new Intent(getActivity(), FundActivity.class);
                    startActivity(fundassetIntent);
                }
                break;
            case R.id.rl_bao:
                if(isInvests[2]==-1){
                    break;
                }else if(isInvests[2]==0){
                    Intent p2pIntent = new Intent(getActivity(),P2PWebActivity.class);
                    p2pIntent.putExtra("url", UrlConst.P2P_INVEST);
                    startActivity(p2pIntent);
                }else{
                    Intent timedepositIntent = new Intent(getActivity(), TimeDepositAssetActivity.class);
                    startActivity(timedepositIntent);
                }
                break;
            case R.id.rl_wallet:
                Intent walletintent = new Intent(getActivity(), WalletActivity.class);
                startActivity(walletintent);
                break;
            case R.id.rl_total_assets:
                Intent allHistory = new Intent(getActivity(), AllTradeHistory.class);
                startActivity(allHistory);
                break;
        }
    }
    
    

    /**
     * 刷新方法
     * @see android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh()
     */
    @Override
    public void onRefresh() {
        requestProfitList();
        requestTotalAssetsData();
        requestP2pAssetsData();
    }

    /**
     * @description 内部类 图表中的游动图标样式定义
     * @author liuchen
     * @date 2015年9月24日
     */

    public class AssetsMarkerView extends MarkerView {
        private TextView tvContent;
        private TextView tvDate;

        public AssetsMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
            tvDate = (TextView) findViewById(R.id.tv_date);
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
            // System.out.println("e.getData()" + e.getData() + "index:" + e.getXIndex());
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
            } else {
                tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
            }
        }
        
        @Override
        public void refreshContent(Entry e, Highlight highlight, String xLable) {
            // System.out.println("xLable" + xLable);
            DecimalFormat df = new DecimalFormat("###,##0.00");
            tvDate.setText(xLable);
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                String lastValueStr = df.format(new BigDecimal(ce.getHigh()));
                tvContent.setText("¥" + lastValueStr);
            } else {
                String lastValueStr = df.format(new BigDecimal(e.getVal()));
                tvContent.setText("¥" + lastValueStr);
            }
        }
    }
}
