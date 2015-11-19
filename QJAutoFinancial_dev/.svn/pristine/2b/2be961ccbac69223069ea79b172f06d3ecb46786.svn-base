
package com.qianjing.finance.ui.activity.profit.fragment;

import com.qianjing.finance.model.history.ProfitBean;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.profit.SteadyProfitActivity;
import com.qianjing.finance.ui.activity.profit.adapter.ProfitAdapter;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

/**
 * @description 组合盈亏列表
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class AssemableProfitFragment extends ProfitBaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestProfitList(UrlConst.ASSEMBLE_EVERYDAY_PROFIT);
    }

    @Override
    protected void initView() {
        super.initView();
        iconImageView.setBackgroundResource(R.drawable.icon_ying_kui);
    }
    
    @Override
    protected void initHeader() 
    {
        
        SteadyEnterClick steadyEnterClick = new SteadyEnterClick();
        steadyProfitEnter.setVisibility(View.VISIBLE);
        steadyProfitEnter.setOnClickListener(steadyEnterClick);
        
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
        LinearLayout parentHeader = new LinearLayout(getActivity());
        parentHeader.setLayoutParams(lp);
        parentHeader.setOrientation(LinearLayout.VERTICAL);
        
        View itemEnter = View.inflate(getActivity(), R.layout.item_history_enter, parentHeader);
        TextView enterTxt = (TextView) itemEnter.findViewById(R.id.tv_enter_txt);
        enterTxt.setText(getString(R.string.steady_profit_enter_txt));
        itemEnter.setOnClickListener(steadyEnterClick);
        
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.profit_header_layout, parentHeader, true);
        iconImageView = (ImageView) mHeaderView.findViewById(R.id.icon_flag_view);
        profitView = (TextView) mHeaderView.findViewById(R.id.ying_kui_text);
        
        currentListView.addHeaderView(parentHeader);
    }
    
    /**
     * 保本收益入口
     * */
    private class SteadyEnterClick implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent steadyIntent = new Intent(getActivity(),SteadyProfitActivity.class);
            startActivity(steadyIntent);
        }
        
    }

    /**
     * 下拉刷新逻辑处理
     * 
     * @param refreshView
     */
    @Override
    protected void onPullUpToRefreshView(PullToRefreshBase<ListView> refreshView) {
        /**
         * 数据不足，不再请求
         */
        if (currentPageSize < pageSize) {
            refreshComplete();
        } else {
            isLoadMore = true;
            isRefresh = false;
            pageIndex += 1;
            offset = (pageIndex) * pageSize;
            requestProfitList(UrlConst.ASSEMBLE_EVERYDAY_PROFIT);
        }
    }

    @Override
    protected void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView) {
        isLoadMore = false;
        isRefresh = true;
        pageIndex = 0;
        offset = 0;
        requestProfitList(UrlConst.ASSEMBLE_EVERYDAY_PROFIT);
    }

    @Override
    protected void handleProfitData(String data) {
        refreshComplete();

        try {
            JSONObject jsonObj = new JSONObject(data);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            switch (ecode) {
                case 0:
                    JSONObject dataObj = jsonObj.optJSONObject("data");
                    totalProfit = dataObj.optString("total_profit");
                    JSONArray dateLists = dataObj.optJSONArray("date_list");
                    if (dateLists != null && dateLists.length() > 0) {
                        currentPageSize = dateLists.length();
                        profitLists = new ArrayList<ProfitBean>();
                        for (int i = 0; i < dateLists.length(); i++) {
                            JSONObject dateObject = (JSONObject) dateLists
                                    .get(i);
                            ProfitBean profitBeanTime = new ProfitBean();
                            profitBeanTime.setTime(dateObject.optLong("date"));
                            profitBeanTime.setType(0);
                            profitLists.add(profitBeanTime);

                            JSONArray dayList = dateObject
                                    .optJSONArray("day_list");
                            for (int j = 0; j < dayList.length(); j++) {
                                JSONObject dayObject = (JSONObject) dayList
                                        .get(j);
                                ProfitBean profitBeanContent = new ProfitBean();
                                profitBeanContent.setTime(dayObject
                                        .optLong("date"));
                                profitBeanContent.setsID(dayObject
                                        .optString("sid"));
                                profitBeanContent.setDayProfit(dayObject
                                        .optString("day_profit").equals("null")
                                        ? "0.00"
                                        : dayObject.optString("day_profit"));
                                profitBeanContent.setName(dayObject
                                        .optString("name"));
                                profitBeanContent.setType(1);
                                profitLists.add(profitBeanContent);
                            }
                            ProfitBean profitBeanTotal = new ProfitBean();
                            profitBeanTotal.setDayTotalProfit(dateObject
                                    .optDouble("day_total_profit"));
                            profitBeanTotal.setType(2);
                            profitLists.add(profitBeanTotal);
                        }
                        updateUI();
                    }
                    break;
                default:
                    showHintDialog(emsg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // showHintDialog("数据解析错误");
        }
    }

    private void updateUI() {
        String currentTotal;
        if (Double.parseDouble(totalProfit) >= 0) {
            profitView.setTextColor(getResources().getColor(R.color.red_VI));
            currentTotal = getString(R.string.jia_hao)
                    + StringHelper.formatString(totalProfit, StringFormat.FORMATE_1);
        } else {
            profitView.setTextColor(getResources().getColor(
                    R.color.color_66b48e));
            currentTotal = StringHelper.formatString(totalProfit, StringFormat.FORMATE_1);
        }
        profitView.setText(currentTotal);
        if (adapter == null) {
            adapter = new ProfitAdapter(mContext, profitLists, false);
            currentListView.setAdapter(adapter);
        } else {
            if (isLoadMore) {
                adapter.addData(profitLists);
            }
            
            if (isRefresh) {
                adapter.refreshAllData(profitLists);
            }
        }
        
        initBackStatus();
    }
    
    @Override
    protected String getTypeStr() {
        return String.format(getResources().getString(R.string.empty_fund_profit),"组合");
    }
}
