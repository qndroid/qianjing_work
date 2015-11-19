
package com.qianjing.finance.ui.activity.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.history.adapter.SteadyHistoryAdapter;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.HistoryPopupowItem;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

public class SteadyHistoryActivity extends BaseHistoryActivity implements OnClickListener,
        AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    /**
     * UI
     * */
    private ListView refreshableView;
    private PullToRefreshListView ptrsv;
    private TextView tvTotalP;
    private TextView tvTotalR;
    private TextView tvTotalF;
    private FrameLayout defPage;
    private HistoryPopupowItem popupItem1;
    private HistoryPopupowItem popupItem2;
    private HistoryPopupowItem popupItem3;
    
    /**
     * DATA
     * */
    private SteadyHistoryAdapter historyAdapter;
    private static final int HISTORY_WALLET = 0x02;
    private static final int ALL_HISTORY = 0;
    private static final int IN_HISTORY = 1;
    private static final int OUT_HISTORY = 2;
    private int CURRENT_POPUPITEM = 0;
    /**
     * PAGING
     */
    private int[] count = {
            0, 0, 0
    };
    private boolean[] isLoadAll = {
            false, false, false
    };
    private boolean IS_PULL_DOWN_TO_REFRESH = false;
    private ArrayList<HashMap<String, Object>> dataList;
    
    /**
     * 总集合
     */
    private ArrayList<HashMap<String, Object>> totalList = new ArrayList<HashMap<String, Object>>();
    /**
     * 投资集合
     */
    private ArrayList<HashMap<String, Object>> buyList = new ArrayList<HashMap<String, Object>>();
    /**
     * 回款集合
     */
    private ArrayList<HashMap<String, Object>> reedemList = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_steady);
        initView();
        setTitleBack();
        setFleibleTitleWithString("所有交易");
        initUIView();
    }
    
    @Override
    public void initView() {
        super.initView();
    }
    
    /** 
    * @description 初始化界面
    * @author liuchen
    */ 
    private void initUIView() {
        
        defPage = (FrameLayout) findViewById(R.id.in_default_page);
        ImageView defaultPic = (ImageView) findViewById(R.id.iv_deault_page_pic);
        TextView defaultTxt = (TextView) findViewById(R.id.tv_deault_page_text);

        defPage.setVisibility(View.GONE);
        defaultPic.setBackgroundResource(R.drawable.img_history_empty);
        defaultTxt.setText(R.string.history_steady_empty);

        ptrsv = (PullToRefreshListView) findViewById(R.id.ptrflv);
        ptrsv.setMode(Mode.BOTH);
        ptrsv.setShowIndicator(false);
        ptrsv.setOnRefreshListener(this);
        refreshableView = ptrsv.getRefreshableView();
        refreshableView.setOnItemClickListener(this);
        initHeader();
        showLoading();
        requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count[CURRENT_POPUPITEM],
                CustomConstants.HISTORY_PAGE_NUMBER);
    }

    /** 
    * @description 初始化列表头信息
    * @author liuchen
    */ 
    public void initHeader() {

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
        LinearLayout parentHeader = new LinearLayout(this);
        parentHeader.setLayoutParams(lp);
        parentHeader.setOrientation(LinearLayout.VERTICAL);

        View view = View.inflate(this, R.layout.history_header, parentHeader);

        ImageView ivTotalP = (ImageView) view.findViewById(R.id.iv_total_purchase);
        TextView tvTotalPt = (TextView) view.findViewById(R.id.tv_total_purchase_t);
        tvTotalP = (TextView) view.findViewById(R.id.tv_total_purchase);

        ImageView ivTotalR = (ImageView) view.findViewById(R.id.iv_total_redeem);
        TextView tvTotalRt = (TextView) view.findViewById(R.id.tv_total_redeem_t);
        tvTotalR = (TextView) view.findViewById(R.id.tv_total_redeem);

        ImageView ivTotalF = (ImageView) view.findViewById(R.id.iv_total_fee);
        TextView tvTotalFt = (TextView) view.findViewById(R.id.tv_total_fee_t);
        tvTotalF = (TextView) view.findViewById(R.id.tv_total_fee);

        LinearLayout llItem3 = (LinearLayout) view.findViewById(R.id.ll_item3);

        ivTotalF.setBackgroundResource(R.drawable.history_quick);

        tvTotalPt.setText("累计转入(元)");
        tvTotalRt.setText("累计转出(元)");
        llItem3.setVisibility(View.GONE);

//        View view1 = View.inflate(this, R.layout.history_state_title, parentHeader);
//        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//         ivIcon.setBackgroundResource(0);
//         tvTitle.setText("");
//        ivIcon.setBackgroundResource(R.drawable.be_sured);
//        tvTitle.setText("已确认");

        refreshableView.addHeaderView(parentHeader);
    }

    /** 
    * @description 请求历史记录列表
    * @author liuchen
    * @param of
    * @param nm
    */ 
    public void requestHistoryList(int of, int nm) {
        
        Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
        hashTable.put("ln", nm);
        hashTable.put("st", of);
        switch (CURRENT_POPUPITEM) {
            case ALL_HISTORY:
                hashTable.put("type", 0);
                break;
            case IN_HISTORY:
                hashTable.put("type", 1);
                break;
            case OUT_HISTORY:
                hashTable.put("type", 2);
                break;
        }
        hashTable.put("mobile", UserManager.getInstance().getUser().getMobile());
        
        AnsynHttpRequest.requestByPost(this,
                UrlConst.JCM_HISTORY_LIST, new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
                        // LogUtils.syso("保本保息历史组合详情:"+data);
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = HISTORY_WALLET;
                        handler.sendMessage(msg);
                    }
                }, hashTable);
    }

    /** 
     * @description 本页面handler实例
     * @author liuchen
     * @date 2015年9月25日
     */ 
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonStr = (String) msg.obj;
            switch (msg.what) {
                case HISTORY_WALLET:
                    analysisHistoryData(jsonStr);
                    break;
            }
        };
    };
    private SteadyHistoryAdapter totalListAdapter;
    private SteadyHistoryAdapter buyListAdapter;
    private SteadyHistoryAdapter reedemListAdapter;

    /** 
    * @description 解析历史列表信息
    * @author liuchen
    * @param jsonStr
    */ 
    protected void analysisHistoryData(String jsonStr) {
        
        if (jsonStr == null || "".equals(jsonStr)) {
            dismissLoading();
            showHintDialog(getString(R.string.net_prompt));
            ptrsv.onRefreshComplete();
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            if (ecode == 0) {

                JSONObject data = jsonObj.optJSONObject("data");

                if (StringHelper.isBlank(data.optString("list"))) {
                    defPage.setVisibility(View.VISIBLE);
                    dismissLoading();
                    ptrsv.onRefreshComplete();
                    return;
                }
                
                JSONArray tradeLog = data.optJSONArray("list");

                if (IS_PULL_DOWN_TO_REFRESH) {
                    switch (CURRENT_POPUPITEM) {
                        case ALL_HISTORY:
                            totalList.clear();
                            break;
                        case IN_HISTORY:
                            buyList.clear();
                            break;
                        case OUT_HISTORY:
                            reedemList.clear();
                            break;
                    }
                }
                
                if (tradeLog.length() < CustomConstants.HISTORY_PAGE_NUMBER) {
                    isLoadAll[CURRENT_POPUPITEM] = true;
                    ptrsv.setMode(Mode.PULL_FROM_START);
                }
                
                for (int i = 0; i < tradeLog.length(); i++) {
                    
                    JSONObject obj = tradeLog.getJSONObject(i);
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("createTime", obj.optString("createTime"));
                    hashMap.put("money", obj.optString("money"));
                    hashMap.put("orderSn", obj.optString("orderSn"));
                    hashMap.put("status", obj.optString("status"));

                    switch (CURRENT_POPUPITEM) {
                        case ALL_HISTORY:
                            totalList.add(hashMap);
                            break;
                        case IN_HISTORY:
                            buyList.add(hashMap);
                            break;
                        case OUT_HISTORY:
                            reedemList.add(hashMap);
                            break;
                    }
                }
                
                if (!IS_PULL_DOWN_TO_REFRESH) {
                    count[CURRENT_POPUPITEM]++;
                }
                
                switch (CURRENT_POPUPITEM) {
                    case ALL_HISTORY:
                        if (totalList.size() == 0) {
                            defPage.setVisibility(View.VISIBLE);
                        } else {
                            defPage.setVisibility(View.GONE);
                        }
                        break;
                    case IN_HISTORY:
                        if (buyList.size() == 0) {
                            defPage.setVisibility(View.VISIBLE);
                        } else {
                            defPage.setVisibility(View.GONE);
                        }
                        break;
                    case OUT_HISTORY:
                        if (reedemList.size() == 0) {
                            defPage.setVisibility(View.VISIBLE);
                        } else {
                            defPage.setVisibility(View.GONE);
                        }
                        break;
                }
                
                initAdapter();

                FormatNumberData.simpleFormatNumber(
                        (float) Double.parseDouble(data.optString("sum_repay")), tvTotalR);
                FormatNumberData.simpleFormatNumber(
                        (float) Double.parseDouble(data.optString("sum_order")), tvTotalP);
                
                ptrsv.onRefreshComplete();
                dismissLoading();
                
            } else {
                dismissLoading();
                ptrsv.onRefreshComplete();
                showHintDialog(emsg);
            }
            
        } catch (JSONException e) {
            dismissLoading();
            ptrsv.onRefreshComplete();
            e.printStackTrace();
        }
    }
    
    /**
    * @description 刷新列表
    * @author liuchen
    */ 
    private void initAdapter() {
        
        switch (CURRENT_POPUPITEM) {
            case ALL_HISTORY:
                if(totalListAdapter==null){
                    totalListAdapter = new SteadyHistoryAdapter(this, totalList);
                    historyAdapter = totalListAdapter;
                    ptrsv.setAdapter(historyAdapter);
                }else{
                    if(historyAdapter == totalListAdapter){
                        historyAdapter.notifyDataSetChanged();
                    }else{
                        historyAdapter = totalListAdapter;
                        ptrsv.setAdapter(historyAdapter);
                    }
                }
                
                break;
            case IN_HISTORY:
                if(buyListAdapter==null){
                    buyListAdapter = new SteadyHistoryAdapter(this, buyList);
                    historyAdapter = buyListAdapter;
                    ptrsv.setAdapter(historyAdapter);
                }else{
                    if(historyAdapter == buyListAdapter){
                        historyAdapter.notifyDataSetChanged();
                    }else{
                        historyAdapter = buyListAdapter;
                        ptrsv.setAdapter(historyAdapter);
                    }
                }
                
                break;
            case OUT_HISTORY:
                if(reedemListAdapter==null){
                    reedemListAdapter = new SteadyHistoryAdapter(this, reedemList);
                    historyAdapter = reedemListAdapter;
                    ptrsv.setAdapter(historyAdapter);
                }else{
                    if(historyAdapter == reedemListAdapter){
                        historyAdapter.notifyDataSetChanged();
                    }else{
                        historyAdapter = reedemListAdapter;
                        ptrsv.setAdapter(historyAdapter);
                    }
                }
                break;
        }
        
    }

    /**
     * 初始化popup菜单
     * @see com.qianjing.finance.ui.activity.history.BaseHistoryActivity#initPopupView(android.view.View)
     */
    @Override
    public void initPopupView(View popupowView) {
        
        popupItem1 = (HistoryPopupowItem) popupowView
                .findViewById(R.id.hpi_popup_item1);
        popupItem2 = (HistoryPopupowItem) popupowView
                .findViewById(R.id.hpi_popup_item2);
        popupItem3 = (HistoryPopupowItem) popupowView
                .findViewById(R.id.hpi_popup_item3);
        
        popupItem1.setText("所有交易");
        popupItem2.setText("存入");
        popupItem3.setText("取现");
        
        popupItem1.setIcon(R.drawable.history_all_pupow);
        popupItem2.setIcon(R.drawable.history_purchase_pupow);
        popupItem3.setIcon(R.drawable.history_redeem_pupow);

        setPopupowItemSelected(CURRENT_POPUPITEM);

        popupItem1.setOnClickListener(this);
        popupItem2.setOnClickListener(this);
        popupItem3.setOnClickListener(this);

    }

    /** 
    * @description popup选择条目
    * @author liuchen
    * @param index
    */ 
    public void setPopupowItemSelected(int index) {

        popupItem1.setChecked(false);
        popupItem2.setChecked(false);
        popupItem3.setChecked(false);
        switch (index) {
            case ALL_HISTORY:
                popupItem1.setChecked(true);
                break;
            case IN_HISTORY:
                popupItem2.setChecked(true);
                break;
            case OUT_HISTORY:
                popupItem3.setChecked(true);
                break;
        }
    }

    /**
     * popup菜单点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hpi_popup_item1:
                setPopupowItemSelected(ALL_HISTORY);
                CURRENT_POPUPITEM = ALL_HISTORY;
                setTitleWithString("所有交易");
                
                break;
            case R.id.hpi_popup_item2:
                setPopupowItemSelected(IN_HISTORY);
                CURRENT_POPUPITEM = IN_HISTORY;
                setTitleWithString("存入");
                break;
            case R.id.hpi_popup_item3:
                setPopupowItemSelected(OUT_HISTORY);
                CURRENT_POPUPITEM = OUT_HISTORY;
                setTitleWithString("取现");
                break;
        }
        updateData(CURRENT_POPUPITEM);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /** 
    * @description 更新list种类
    * @author liuchen
    * @param allHistory  CURRENT_POPUPITEM
    */ 
    private void updateData(int allHistory) {
        
        if(isLoadAll[CURRENT_POPUPITEM]){
            ptrsv.setMode(Mode.PULL_FROM_START);
        }else{
            ptrsv.setMode(Mode.BOTH);
        }
        
        switch (allHistory) {
            case ALL_HISTORY:
//                Toast.makeText(this, "totalList.size()"+totalList.size(), 1).show();
                if (totalList.size() == 0) {
                    if(defPage.getVisibility()==View.GONE){
                        defPage.setVisibility(View.VISIBLE);
                    }
                    showLoading();
                    requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count[CURRENT_POPUPITEM],
                            CustomConstants.HISTORY_PAGE_NUMBER);
                } else {
                    if(defPage.getVisibility()==View.VISIBLE){
                        defPage.setVisibility(View.GONE);
                    }
                    initAdapter();
                }
                break;
            case IN_HISTORY:
//                Toast.makeText(this, "buyList.size()"+buyList.size(), 1).show();
                if (buyList.size() == 0) {
                    if(defPage.getVisibility()==View.GONE){
                        defPage.setVisibility(View.VISIBLE);
                    }
                    showLoading();
                    requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count[CURRENT_POPUPITEM],
                            CustomConstants.HISTORY_PAGE_NUMBER);
                } else {
                    if(defPage.getVisibility()==View.VISIBLE){
                        defPage.setVisibility(View.GONE);
                    }
                    initAdapter();
                }
                break;
            case OUT_HISTORY:
//                Toast.makeText(this, "reedemList.size()"+reedemList.size(), 1).show();
                if (reedemList.size() == 0) {
                    if(defPage.getVisibility()==View.GONE){
                        defPage.setVisibility(View.VISIBLE);
                    }
                    showLoading();
                    requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count[CURRENT_POPUPITEM],
                            CustomConstants.HISTORY_PAGE_NUMBER);
                } else {
                    if(defPage.getVisibility()==View.VISIBLE){
                        defPage.setVisibility(View.GONE);
                    }
                    initAdapter();
                }
                break;
        }
    }

    /**
     * 条目点击事件
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            return;
        }

        switch (CURRENT_POPUPITEM) {
            case ALL_HISTORY:
                dataList = totalList;
                break;
            case IN_HISTORY:
                dataList = buyList;
                break;
            case OUT_HISTORY:
                dataList = reedemList;
                break;
        }
        
        if (dataList != null && dataList.size() > 0) {
            
            Intent steadyIntent = new Intent(SteadyHistoryActivity.this, SteadyHistoryDetails.class);
            steadyIntent.putExtra("orderSn", (String) dataList.get(position - 1).get("orderSn"));
            steadyIntent.putExtra("status", (String) dataList.get(position - 1).get("status"));
            steadyIntent.putExtra("type", "1");
            startActivity(steadyIntent);

        }
    }
    
    /**
     * 下拉刷新
     * @see com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2#onPullDownToRefresh(com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase)
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        /**
         * pull down to refresh
         */
        IS_PULL_DOWN_TO_REFRESH = true;
        requestHistoryList(0, count[CURRENT_POPUPITEM]
                * CustomConstants.HISTORY_PAGE_NUMBER);
    }
    
    /**
     * 上拉加载
     * @see com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2#onPullUpToRefresh(com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase)
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        /**
         * pull up to load
         */
        IS_PULL_DOWN_TO_REFRESH = false;
        requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count[CURRENT_POPUPITEM],
                CustomConstants.HISTORY_PAGE_NUMBER);
    }
}
