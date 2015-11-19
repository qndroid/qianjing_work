
package com.qianjing.finance.ui.activity.profit.fragment;

import com.qianjing.finance.model.history.ProfitBean;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.profit.adapter.ProfitAdapter;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @description 活期宝盈亏列表Fragment
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class WalletProfitFragment extends ProfitBaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestProfitList(UrlConst.WALLET_EVERYDAY_PROFIT);
    }

    @Override
    protected void initView() {
        super.initView();
        iconImageView.setBackgroundResource(R.drawable.icon_profit);
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
            requestProfitList(UrlConst.WALLET_EVERYDAY_PROFIT);
        }
    }

    @Override
    protected void onPullDownRefreshView(PullToRefreshBase<ListView> refreshView) {
        isLoadMore = false;
        isRefresh = true;
        pageIndex = 0;
        offset = 0;
        requestProfitList(UrlConst.WALLET_EVERYDAY_PROFIT);
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
                    JSONArray dateLists = dataObj.optJSONArray("profit_list");
                    if (dateLists != null && dateLists.length() > 0) {
                        currentPageSize = dateLists.length();
                        profitLists = new ArrayList<ProfitBean>();
                        for (int i = 0; i < dateLists.length(); i++) {
                            JSONObject dateObject = (JSONObject) dateLists
                                    .get(i);
                            ProfitBean profitBeanTime = new ProfitBean();
                            profitBeanTime.setTime(dateObject.optLong("dt"));
                            profitBeanTime.setType(0);
                            profitLists.add(profitBeanTime);

                            ProfitBean profitDayTotal = new ProfitBean();
                            profitDayTotal
                                    .setDayTotalProfit(Double
                                            .parseDouble(dateObject
                                                    .optString("profit")));
                            profitDayTotal.setType(2);
                            profitLists.add(profitDayTotal);
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
            adapter = new ProfitAdapter(mContext, profitLists, true);
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
        return "您还没有转入过活期宝，赶紧选购吧~";
    }
}
