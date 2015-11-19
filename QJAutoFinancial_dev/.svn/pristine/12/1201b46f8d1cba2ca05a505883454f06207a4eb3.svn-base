/**
 * Project Name:QJAutoFinancial_v2.2
 * File Name:SteadyProfitActivity.java
 * Package Name:com.qianjing.finance.ui.activity.profit
 * Date:2015年9月11日下午4:32:02
 * Copyright (c) 2015, www.qianjing.com All Rights Reserved.
 *
 */

package com.qianjing.finance.ui.activity.profit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.zip.DataFormatException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qjautofinancial.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SteadyProfitActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015年9月11日 下午4:32:02 <br/>
 * 
 * @author liuchen
 * @version
 * @see
 */
public class SteadyProfitActivity extends BaseActivity {

    private PullToRefreshListView ptrfl;
    private SteadyProfitAdapter steadyProfit;
    private boolean IS_PULL_DOWN_TO_REFRESH = false;
    private int count = 0;
    private ArrayList<HashMap<String, Object>> dataList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setLayoutParams(lp);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(0XFFEFEFF4);
        
        setContentView(rootLayout);
        
        // DisplayMetrics dm = getResources().getDisplayMetrics();
        LayoutParams lpv = new LayoutParams(LayoutParams.MATCH_PARENT
                , (int) getResources().getDimension(R.dimen.title_height));
        View view = View.inflate(this, R.layout.include_title, null);
        initTitleView(view);
        view.setLayoutParams(lpv);

        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ptrfl = new PullToRefreshListView(this);
        ptrfl.setLayoutParams(lps);
        initPullToRefresh(ptrfl);

        rootLayout.addView(view);
        rootLayout.addView(ptrfl);
        requestEveryDayData(0,CustomConstants.HISTORY_PAGE_NUMBER);
    }
    
    
    private void requestEveryDayData(int startOf,int num){
        
        Hashtable<String , Object> table = new Hashtable<String, Object>();
        table.put("mobile", UserManager.getInstance().getUser().getMobile());
        table.put("st", startOf);
        table.put("ln", num);
        
        AnsynHttpRequest.requestByPost(this, UrlConst.JCM_EVERYDAY_PROFIT, new HttpCallBack() {
            
            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, table);
    }
    
    
    
    private void analyzeJsonData(String data) {
        if (data == null || "".equals(data)) {
            dismissLoading();
            showHintDialog(getString(R.string.net_prompt));
            ptrfl.onRefreshComplete();
            return;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(data);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            if (ecode == 0) {
                
                if(dataList==null){
                    dataList = new ArrayList<HashMap<String,Object>>();
                }
                
                JSONObject data0 = jsonObj.optJSONObject("data");
                JSONArray tradeLog = data0.optJSONArray("everyday_profit");
                if(IS_PULL_DOWN_TO_REFRESH){
                    dataList.clear();
                }
                
                if(tradeLog.length()<CustomConstants.HISTORY_PAGE_NUMBER){
                    ptrfl.setMode(Mode.PULL_FROM_START);
                }
                
                FormatNumberData.formatNumberPM(Float.parseFloat(data0.optString("total_profit")), profitView);
                //解析数据
                for (int i = 0; i < tradeLog.length(); i++) {
                    
                    JSONObject obj = tradeLog.getJSONObject(i);
                    HashMap<String , Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("dt", obj.optString("dt"));
                    hashMap.put("baoben_profit", obj.optString("baoben_profit"));
                    dataList.add(hashMap);
                }
                
                if(!IS_PULL_DOWN_TO_REFRESH){
                    count++;
                }
                
                initAdapter();
                
                ptrfl.onRefreshComplete();
                dismissLoading();
            } else {
                dismissLoading();
                ptrfl.onRefreshComplete();
                showHintDialog(emsg);
            }
        } catch (JSONException e) {
            dismissLoading();
            ptrfl.onRefreshComplete();
            e.printStackTrace();
        }
    }
    
    
    protected void initAdapter() {
        
        if(steadyProfit==null){
            steadyProfit = new SteadyProfitAdapter();
            ptrfl.setAdapter(steadyProfit);
        }else{
            steadyProfit.notifyDataSetChanged();
        }
    }
    
    Handler handler = new Handler(){
        
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 1:
                    analyzeJsonData((String)msg.obj);
                    break;
            }
        };
    };
    
    private TextView profitView;
    
    private void initTitleView(View view) {
        ImageView imageLeft = (ImageView) view.findViewById(R.id.title_left_image);
        imageLeft.setImageResource(R.drawable.title_left);
        imageLeft.setVisibility(View.VISIBLE);
        imageLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTitle = (TextView) view.findViewById(R.id.title_middle_text);
        tvTitle.setText("保本组合每日收益");
    }

    @SuppressWarnings("unchecked")
    private void initPullToRefresh(PullToRefreshListView ptrfl) {
        ptrfl.setMode(Mode.BOTH);
        ptrfl.setShowIndicator(false);
        ptrfl.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ptrfl.setVerticalScrollBarEnabled(false);
        ptrfl.setOnRefreshListener(new OnRefreshListener2() {
            
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                IS_PULL_DOWN_TO_REFRESH = true;
                requestEveryDayData(0, count*CustomConstants.HISTORY_PAGE_NUMBER);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                IS_PULL_DOWN_TO_REFRESH = false;
                requestEveryDayData(CustomConstants.HISTORY_PAGE_NUMBER*count,CustomConstants.HISTORY_PAGE_NUMBER);
            }
        });
        ListView refreshableView = ptrfl.getRefreshableView();
        refreshableView.addHeaderView(initHeaderView());
        refreshableView.setDivider(null);
        refreshableView.setVerticalScrollBarEnabled(false);
        steadyProfit = new SteadyProfitAdapter();
        ptrfl.setAdapter(steadyProfit);
    }

    private View initHeaderView() {

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LayoutParams.MATCH_PARENT
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 62, getResources()
                        .getDisplayMetrics()));
        LinearLayout headerView = new LinearLayout(this);
        headerView.setLayoutParams(lp);
        headerView.setBackgroundColor(Color.WHITE);

        View mHeaderView = LayoutInflater.from(this).inflate(R.layout.profit_header_layout, null,
                false);
        ImageView iconImageView = (ImageView) mHeaderView.findViewById(R.id.icon_flag_view);
        profitView = (TextView) mHeaderView.findViewById(R.id.ying_kui_text);
        mHeaderView.setLayoutParams(lp);
        
        return mHeaderView;
    }

    private class SteadyProfitAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(dataList!=null && dataList.size()>0){
                return dataList.size();
            }else{
                return CustomConstants.HISTORY_PAGE_NUMBER;
            }
        }
        
        @Override
        public Object getItem(int position) {
            return null;
        }
        
        @Override
        public long getItemId(int position) {
            return 0;
        }
        
        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT
                        , LayoutParams.WRAP_CONTENT);
                LinearLayout childItem = new LinearLayout(SteadyProfitActivity.this);
                childItem.setLayoutParams(lps);
                childItem.setOrientation(LinearLayout.VERTICAL);
                LayoutParams lpv = new LayoutParams(LayoutParams.MATCH_PARENT
                        , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                                getResources().getDisplayMetrics()));
                View view = new View(SteadyProfitActivity.this);
                view.setLayoutParams(lpv);
                childItem.addView(view);

                View itemView = View.inflate(SteadyProfitActivity.this,
                        R.layout.item_profit_detail, null);
                LayoutParams lpi = new LayoutParams(LayoutParams.MATCH_PARENT
                        , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34,
                                getResources().getDisplayMetrics()));
                itemView.setLayoutParams(lpi);
                itemView.setBackgroundColor(0XFFD6E0F1);
                TextView name = (TextView) itemView.findViewById(R.id.name_view);
                TextView money = (TextView) itemView.findViewById(R.id.money_view);
                name.setTextSize(17);
                name.setTextColor(0XFF5A6572);
                name.setText("时间");
                money.setTextColor(0XFF5A6572);
                money.setTextSize(17);
                money.setText("收益");
                childItem.addView(itemView);

                return childItem;
            }
            
            LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT
                    , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54,
                            getResources().getDisplayMetrics()));
            View childItem = View.inflate(SteadyProfitActivity.this, R.layout.item_profit_detail,
                    null);
            childItem.setLayoutParams(lps);
            
            TextView name = (TextView) childItem.findViewById(R.id.name_view);
            TextView money = (TextView) childItem.findViewById(R.id.money_view);
            name.setTextSize(17);
            money.setTextSize(17);
            name.setTextColor(0XFF5A6572);
            
            if(dataList!=null && dataList.size()>0){
                name.setText(DateFormatHelper.formatDate(
                        (String)dataList.get(position-1).get("dt")+"000", DateFormatHelper.DateFormat.DATE_5));
                double num = Double.parseDouble((String)dataList.get(position-1).get("baoben_profit"));
                if(num>=0){
                    money.setTextColor(0XFFFF3B3B);
                    money.setText("+"+StringHelper.formatDecimal(num));
                }else{
                    money.setTextColor(0xFF63CD99);
                    money.setText(StringHelper.formatDecimal(num));
                }
            }else{
                name.setText("");
                money.setText("");
            }
            return childItem;
        }
    }
}
