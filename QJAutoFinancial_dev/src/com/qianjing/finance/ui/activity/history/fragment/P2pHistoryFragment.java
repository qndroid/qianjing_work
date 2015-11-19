package com.qianjing.finance.ui.activity.history.fragment;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.history.P2pHistoryBean;
import com.qianjing.finance.model.history.Schemaoplogs;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.history.AssembleHistoryDetails;
import com.qianjing.finance.ui.activity.history.adapter.AssemableHistoryAdapter;
import com.qianjing.finance.ui.activity.history.adapter.P2pHistoryAdapter;
import com.qianjing.finance.ui.activity.rebalance.RebalanceHistoryDetails;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.StateUtils;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qjautofinancial.R;

public class P2pHistoryFragment extends BaseFragment {
    
    
    private static final int HISTORY_P2P = 0x02;
    private int type = CustomConstants.DEFAULT_VALUE;
    private ListView refreshableView;
    private LinearLayout topTitle;
    private ImageView ivIcon;
    private TextView tvTitle;
    private PullToRefreshListView ptrsv;
    private TextView tvTotalP;
    private TextView tvTotalR;
    private TextView tvTotalF;
    private FrameLayout defPage;
    /**
     * 请求数据区分集合
     */
    ArrayList<P2pHistoryBean> runningList = new ArrayList<P2pHistoryBean>();
    ArrayList<P2pHistoryBean> doneList = new ArrayList<P2pHistoryBean>();

    private P2pHistoryAdapter historyAdapter;
    /**
     * paging
     */
    private int count = 0;
    private boolean IS_PULL_DOWN_TO_REFRESH = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = View.inflate(getActivity(), R.layout.fragment_history_assemable, null);
        initView(view);
        return view;
    }
    
    public void setAssemableParameter(int type) {
        this.type = type;
    }
    
    private void initView(View view) {

        topTitle = (LinearLayout) view.findViewById(R.id.top_title);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        
        defPage = (FrameLayout) view.findViewById(R.id.in_default_page);
        ImageView defaultPic = (ImageView) view.findViewById(R.id.iv_deault_page_pic);
        TextView defaultTxt = (TextView) view.findViewById(R.id.tv_deault_page_text);

        defPage.setVisibility(View.GONE);
        defaultPic.setBackgroundResource(R.drawable.img_history_empty);
        defaultTxt.setText(R.string.history_assemble_empty);
        
        ptrsv = (PullToRefreshListView) view.findViewById(R.id.ptrflv);
        ptrsv.setMode(Mode.BOTH);
        ptrsv.setShowIndicator(false);
        ptrsv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                /**
                 * pull down to refresh
                 */
                IS_PULL_DOWN_TO_REFRESH = true;
                // Toast.makeText(getActivity(), "count="+count, 1).show();
                requestHistoryList(0, count * CustomConstants.HISTORY_PAGE_NUMBER);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                /**
                 * pull up to load
                 */
                IS_PULL_DOWN_TO_REFRESH = false;
                // Toast.makeText(getActivity(), "count="+count, 1).show();
                requestHistoryList(CustomConstants.HISTORY_PAGE_NUMBER * count,
                        CustomConstants.HISTORY_PAGE_NUMBER);
            }
        });
        refreshableView = ptrsv.getRefreshableView();
        // refreshableView.setSelector(R.drawable.bg_history_item);
        refreshableView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

                // Caused by: java.lang.NullPointerException
                if (historyAdapter != null) {

                    if (historyAdapter.getRunningTitlePosition() == -1
                            && historyAdapter.getCompletedTitlePosition() == -1) {
                        topTitle.setVisibility(View.GONE);
                    } else if (historyAdapter.getRunningTitlePosition() == -1) {

                        if (firstVisibleItem > historyAdapter.getCompletedTitlePosition()) {
                            topTitle.setVisibility(View.VISIBLE);
                            ivIcon.setBackgroundResource(R.drawable.be_sured);
                            tvTitle.setText("已确认");
                        } else {
                            topTitle.setVisibility(View.GONE);
                        }

                    } else if (historyAdapter.getCompletedTitlePosition() == -1) {

                        if (firstVisibleItem > historyAdapter.getRunningTitlePosition()) {
                            topTitle.setVisibility(View.VISIBLE);
                            ivIcon.setBackgroundResource(R.drawable.is_running);
                            tvTitle.setText("进行中");
                        } else {
                            topTitle.setVisibility(View.GONE);
                        }

                    } else {
                        
                        if (firstVisibleItem > historyAdapter.getCompletedTitlePosition()) {
                            topTitle.setVisibility(View.VISIBLE);
                            ivIcon.setBackgroundResource(R.drawable.be_sured);
                            tvTitle.setText("已确认");
                        } else if (firstVisibleItem > historyAdapter.getRunningTitlePosition()) {
                            topTitle.setVisibility(View.VISIBLE);
                            ivIcon.setBackgroundResource(R.drawable.is_running);
                            tvTitle.setText("进行中");
                        } else {
                            topTitle.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        refreshableView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // Toast.makeText(getActivity(), "当前条目"+position, 1).show();

                if (historyAdapter != null) {

                    P2pHistoryBean schemaoplogsInfo = null;

                    if (position == 0) {
                        return;
                    }

                    if (historyAdapter.getRunningTitlePosition() == -1
                            && historyAdapter.getCompletedTitlePosition() == -1) {
                        return;
                    } else if (historyAdapter.getRunningTitlePosition() == -1) {

                        if (position == 1) {
                            return;
                        }

                        schemaoplogsInfo = doneList.get(position - 2);

                    } else if (historyAdapter.getCompletedTitlePosition() == -1) {

                        if (position == 1) {
                            return;
                        }

                        schemaoplogsInfo = runningList.get(position - 2);

                    } else {

                        if (position == 1) {
                            return;
                        }

                        if (position == runningList.size() + 2) {
                            return;
                        }

                        if (position > 1 && position < runningList.size() + 2) {
                            schemaoplogsInfo = runningList.get(position - 2);
                        } else if (position > runningList.size() + 2) {
                            schemaoplogsInfo = doneList.get(position - 3 - runningList.size());
                        }
                    }
                    
//                        Intent mintent = new Intent(getActivity(), AssembleHistoryDetails.class);
//                        mintent.putExtra("schemaoplogs", schemaoplogsInfo);
//                        startActivity(mintent);

                }
            }
        });
        initHeader();
        showLoading();
        requestHistoryList(0, CustomConstants.HISTORY_PAGE_NUMBER);
    }

    public void initHeader() {

        View view = View.inflate(getActivity(), R.layout.history_header, null);

        ImageView ivTotalP = (ImageView) view.findViewById(R.id.iv_total_purchase);
        TextView tvTotalPt = (TextView) view.findViewById(R.id.tv_total_purchase_t);
        tvTotalP = (TextView) view.findViewById(R.id.tv_total_purchase);

        ImageView ivTotalR = (ImageView) view.findViewById(R.id.iv_total_redeem);
        TextView tvTotalRt = (TextView) view.findViewById(R.id.tv_total_redeem_t);
        tvTotalR = (TextView) view.findViewById(R.id.tv_total_redeem);

        ImageView ivTotalF = (ImageView) view.findViewById(R.id.iv_total_fee);
        TextView tvTotalFt = (TextView) view.findViewById(R.id.tv_total_fee_t);
        tvTotalF = (TextView) view.findViewById(R.id.tv_total_fee);

        refreshableView.addHeaderView(view);
    }
    
    
    
    

    public void requestHistoryList(int of, int nm) {

        Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
        
        hashTable.put("ln", nm);
        hashTable.put("st", of);
        
        if (type != CustomConstants.DEFAULT_VALUE) {
            hashTable.put("type", type);
        }
        hashTable.put("mobile", UserManager.getInstance().getUser().getMobile());
        
        AnsynHttpRequest.requestByPost(getActivity(),
                /*UrlConst.P2P_HISTORY_LIST*/UrlConst.JCM_HISTORY_LIST, new HttpCallBack() {
                    
                    @Override
                    public void back(String data, int status) {
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = HISTORY_P2P;
                        handler.sendMessage(msg);
                    }
                }, hashTable);
    }
    
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonStr = (String) msg.obj;
            switch (msg.what) {
                case HISTORY_P2P:
//                    WriteLog.recordLog("assemble" + jsonStr);
//                    LogUtils.syso("组合P2p列表头信息:" + jsonStr);
                    analysisHistoryHeaderData(jsonStr);
                    break;
            }
        };
    };

    

    protected void analysisHistoryHeaderData(String jsonStr) {

//        LogUtils.syso("定存记录："+jsonStr);
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
                
                if (data ==null || StringHelper.isBlank(data.optString("list"))) {
                    defPage.setVisibility(View.VISIBLE);
                    dismissLoading();
                    ptrsv.onRefreshComplete();
                    return;
                }
                
                JSONArray schemaoplogsArray = data
                        .optJSONArray("list");
                
                if (IS_PULL_DOWN_TO_REFRESH) {
                    runningList.clear();
                    doneList.clear();
                }
                
                if (schemaoplogsArray.length() < CustomConstants.HISTORY_PAGE_NUMBER) {
                    ptrsv.setMode(Mode.PULL_FROM_START);
                }
                
                for (int i = 0; i < schemaoplogsArray.length(); i++) {
                    
                    JSONObject schemaLogData = schemaoplogsArray
                            .getJSONObject(i);
                    P2pHistoryBean p2pHistoryBean = new P2pHistoryBean();
                    //p2p
                    p2pHistoryBean.create_time = schemaLogData.optString("create_time");
                    p2pHistoryBean.deal_name = schemaLogData.optString("deal_name");
                    p2pHistoryBean.money_change = schemaLogData.optString("money_change");
                    p2pHistoryBean.order_sn = schemaLogData.optString("order_sn");
                    p2pHistoryBean.status = schemaLogData.optString("status");
                    
                    //保本保息
//                    p2pHistoryBean.create_time = schemaLogData.optString("createTime");
//                    p2pHistoryBean.deal_name = schemaLogData.optString("deal_name");
//                    p2pHistoryBean.money_change = schemaLogData.optString("money");
//                    p2pHistoryBean.order_sn = schemaLogData.optString("orderSn");
//                    p2pHistoryBean.status = schemaLogData.optString("status");
                    
                    
                    //TODO 还没有判断进行中和已完成
//                    if (p2pHistoryBean.status) {
//                        runningList.add(schemalogInfo);
//                    } else {
                        doneList.add(p2pHistoryBean);
//                    }
                    // 加入集合
                }

                if (!IS_PULL_DOWN_TO_REFRESH) {
                    count++;
                }
                
                if (runningList.size() + doneList.size() == 0) {
                    defPage.setVisibility(View.VISIBLE);
                }
                
                initAdapter();
                
                FormatNumberData.simpleFormatNumber(
                        (float) (StringHelper.isBlank(data.optString("total_subscribe")
                                ) ? 0.00 : Double.parseDouble(data.optString("total_subscribe")))
                        ,
                        tvTotalP);
                FormatNumberData.simpleFormatNumber(
                        (float) (StringHelper.isBlank(data.optString("total_fee")
                                ) ? 0.00 : Double.parseDouble(data.optString("total_fee"))),
                        tvTotalF);
                FormatNumberData.simpleFormatNumber(
                        (float) (StringHelper.isBlank(data.optString("total_redemp")
                                ) ? 0.00 : Double.parseDouble(data.optString("total_redemp"))),
                        tvTotalR);
                
                ptrsv.onRefreshComplete();
                dismissLoading();

            } else {
                dismissLoading();
                ptrsv.onRefreshComplete();
                showHintDialog(emsg);
            }
            
        } catch (JSONException e) {
            dismissLoading();
            showHintDialog(getActivity().getString(R.string.net_data_error));
            ptrsv.onRefreshComplete();
            e.printStackTrace();
            WriteLog.recordLog(e.toString());
        }
    }
    
    private void initAdapter() {
        if (historyAdapter == null) {
            historyAdapter = new P2pHistoryAdapter(getActivity(), runningList, doneList);
            ptrsv.setAdapter(historyAdapter);
        } else {
            historyAdapter.notifyDataSetChanged();
        }
    }
    
    
    
}
