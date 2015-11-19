
package com.qianjing.finance.ui.activity.assemble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.assemble.AssembleAssets;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.net.helper.RequestAssembleHelper;
import com.qianjing.finance.net.ihandle.IHandleAssembleAssets;
import com.qianjing.finance.net.ihandle.IHandleAssembleDetail;
import com.qianjing.finance.net.ihandle.IHandleAssembleList;
import com.qianjing.finance.net.response.model.ResponseAssembleAssets;
import com.qianjing.finance.net.response.model.ResponseAssembleDetail;
import com.qianjing.finance.net.response.model.ResponseAssembleList;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.history.AssembleHistoryActivity;
import com.qianjing.finance.ui.activity.profit.ProfitActivity;
import com.qianjing.finance.ui.activity.rebalance.RebalanceListActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.adapter.AssetsAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

/**
 * @description 我的组合资产和列表界面
 */
public class AssembleMineActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout defPage;
    private ImageView defaultPic;
    private TextView defaultTxt;
    private PullToRefreshListView ptrlv;
    private ListView refreshableView;
    private View mHeaderView;
    private LinearLayout cumulativeLayout;
    private TextView assetsAssembleBtn;
    private TextView noAssetsAssembleBtn;
    
    private RelativeLayout balanceEnter;
    private TextView balanceEnterTxt;
    
    private List<Map<String, String>> mListAssetsAssemble;
    private List<Map<String, String>> mListNoAssetsAssemble;
    private AssetsAdapter mAssetsAdapter;
    private AssetsAdapter mNoAssetsAdapter;

    /**
     * @fields flagCurrentListType: 当前列表类型标识位.0为有持仓列表（默认）,1为没有持仓列表
     */
    private int flagCurrentListType;
    /**
     * @fields offsetAssets: 有持仓组合列表数据偏移量
     */
    private int offsetAssets;
    /**
     * @fields offsetNoAssets: 没有持仓组合列表数据偏移量
     */
    private int offsetNoAssets;
    /**
     * @fields pageNum: 列表单次加载数据数
     */
    private int pageNum = CustomConstants.ASSETS_PAGE_NUMBER;

    /**
     * @fields rebalanceCount: 再平衡组合数
     */
    private int rebalanceCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_assemble_mine);

        setTitleWithId(R.string.title_comb);

        setTitleBack();

        TextView btnReg = (TextView) findViewById(R.id.title_right_text);

        /**
         * set title history
         */
        btnReg.setVisibility(View.VISIBLE);
        btnReg.setText(R.string.bill);
        btnReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(AssembleMineActivity.this, AssembleHistoryActivity.class);
                startActivityForResult(intent, ViewUtil.REQUEST_CODE);
            }
        });

        /**
         * add the empty page
         */
        defPage = (FrameLayout) findViewById(R.id.in_default_page);
        defaultPic = (ImageView) findViewById(R.id.iv_deault_page_pic);
        defaultTxt = (TextView) findViewById(R.id.tv_deault_page_text);

        defaultPic.setBackgroundResource(R.drawable.img_assets);
        defaultTxt.setText(R.string.empty_group_assets);

        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrflv_list_view);
        ptrlv.setMode(Mode.BOTH);
        ptrlv.setShowIndicator(false);
        ptrlv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                requestAssets();
                if (flagCurrentListType == 0) {
                    requestAssembleList(0, offsetAssets + CustomConstants.ASSETS_PAGE_NUMBER);
                } else if (flagCurrentListType == 1) {
                    requestNoAssembleList(0, offsetNoAssets + CustomConstants.ASSETS_PAGE_NUMBER);
                }
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (flagCurrentListType == 0) {
                    offsetAssets += CustomConstants.ASSETS_PAGE_NUMBER;
                    requestAssembleList(-1, -1);
                } else if (flagCurrentListType == 1) {
                    offsetNoAssets += CustomConstants.ASSETS_PAGE_NUMBER;
                    requestNoAssembleList(-1, -1);
                }
            }
        });
        
        refreshableView = ptrlv.getRefreshableView();
        refreshableView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                String strSid = "";
                if(flagCurrentListType==0){
                    strSid = mListAssetsAssemble.get(position - 1).get("sid");
                }else{
                    strSid = mListNoAssetsAssemble.get(position - 1).get("sid");
                }
                
                boolean debug = true;
                if(debug){
                    Intent intent = new Intent();
                    intent.setClass(AssembleMineActivity.this,
                            AssembleDetailActivity.class);
                    intent.putExtra("sid", strSid);
                    startActivityForResult(intent, ViewUtil.REQUEST_CODE);
                    return;
                }
                RequestAssembleHelper.requestAssembleDetail(AssembleMineActivity.this, strSid,
                        new IHandleAssembleDetail() {
                            @Override
                            public void handleResponse(ResponseAssembleDetail response) {
                                
                                AssembleDetail detail = response.detail;
                                if (detail != null) {
                                    
                                    Intent intent = new Intent();
                                    intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, detail);
                                    // 未申购组合
                                    if (StringHelper.isBlank(detail.getAssembleConfig().getBank()))
                                    {
                                        intent.putExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL,
                                                ViewUtil.FROM_ASSETS);
                                        intent.setClass(AssembleMineActivity.this,
                                                AssembleAIPConfigActivity.class);
                                        startActivityForResult(intent, ViewUtil.REQUEST_CODE);
                                    }
                                    else
                                    {
                                        // 已申购组合
                                        intent.setClass(AssembleMineActivity.this,
                                                AssembleDetailActivity.class);
                                        startActivityForResult(intent, ViewUtil.REQUEST_CODE);
                                    }
                                }
                            }
                        });
            }
        });
        
        mHeaderView = View.inflate(this, R.layout.assets_total, null);
        ((RelativeLayout) mHeaderView.findViewById(R.id.rl_history))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(AssembleMineActivity.this, AssembleHistoryActivity.class);
                        startActivityForResult(intent, ViewUtil.REQUEST_CODE);
                    }
                });
        cumulativeLayout = (LinearLayout) mHeaderView.findViewById(R.id.cumulative_layout);
        cumulativeLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AssembleMineActivity.this, ProfitActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        balanceEnter = (RelativeLayout) mHeaderView.findViewById(R.id.rl_balance__list_enter);
        balanceEnterTxt = (TextView) mHeaderView.findViewById(R.id.tv_balance__list_enter);
        balanceEnter.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (rebalanceCount != -1) {
                    Intent intent = new Intent(AssembleMineActivity.this,
                            RebalanceListActivity.class);
                    intent.putExtra("count", rebalanceCount + "");
                    startActivity(intent);
                }
            }
        });
        
        assetsAssembleBtn = (TextView) mHeaderView.findViewById(R.id.tv_assemble_assets);
        noAssetsAssembleBtn = (TextView) mHeaderView.findViewById(R.id.tv_assemble_no_assets);
        assetsAssembleBtn.setSelected(true);
        noAssetsAssembleBtn.setSelected(false);
        assetsAssembleBtn.setTextColor(Color.WHITE);
        noAssetsAssembleBtn.setTextColor(R.color.color_5a6572);
        assetsAssembleBtn.setOnClickListener(this);
        noAssetsAssembleBtn.setOnClickListener(this);
        
        refreshableView.addHeaderView(mHeaderView);
        
        mListAssetsAssemble = new ArrayList<Map<String, String>>();
        mListNoAssetsAssemble = new ArrayList<Map<String, String>>();
        
        
        // 获取组合总资产数据
        requestAssets();

        // 获取持仓组合列表数据
        requestAssembleList(-1, -1);

        // 获取没有持仓列表数据
        requestNoAssembleList(-1, -1);
    }


    /** 
    * @description 加载组合总资产数据
    */ 
    private void requestAssets() {
        RequestAssembleHelper.requestAssembleAssets(this, new IHandleAssembleAssets() {
            @Override
            public void handleResponse(ResponseAssembleAssets response) {

                AssembleAssets assets = response.assets;
                if (assets != null) {

                    // 总市值
                    String strAssets = assets.getAssets();
                    // 累计盈亏
                    String strProfit = assets.getProfit();
                    // 在途资金
                    String strSub = assets.getSubscripting();

                    if (mHeaderView != null) {

                        if (assets.getBalanceState() == 1) {
                            balanceEnter.setVisibility(View.VISIBLE);
                            rebalanceCount = assets.getBalanceCount();
                            balanceEnterTxt.setText(String
                                    .format(getString(R.string.balance_list_title_one),
                                            rebalanceCount + ""));
                        }

                        TextView tvAssets = (TextView) mHeaderView.findViewById(R.id.tv_assets);
                        TextView tvSub = (TextView) mHeaderView.findViewById(R.id.tv_subscripting);
                        TextView tvProfit = (TextView) mHeaderView.findViewById(R.id.tv_profit);
                        LinearLayout inPurchase = (LinearLayout) mHeaderView
                                .findViewById(R.id.ll_in_purchase);

                        tvAssets.setText(StringHelper.formatString(strAssets,StringFormat.FORMATE_2));
                        if (null != strSub && Double.parseDouble(strSub) != 0) {
                            tvSub.setText(StringHelper.formatString(strSub,StringFormat.FORMATE_2));
                        }
                        else {
                            inPurchase.setVisibility(View.GONE);
                        }
                        if (!StringHelper.isBlank(strProfit)) {
                            FormatNumberData.formatNumberPM(Float.parseFloat(strProfit), tvProfit);
                        }
                    }
                }
                else
                    ptrlv.onRefreshComplete();
            }
        });
    }

    private void requestAssembleList(int of, int nm) {

        if (of == 0)
            mListAssetsAssemble.clear();

        final int startN;
        int pageN;

        if (of != -1 && nm != -1) {
            startN = of;
            pageN = nm;
        }
        else {
            startN = offsetAssets;
            pageN = pageNum;
        }
        
        RequestAssembleHelper.requestAssetsAssembleList(this, startN, pageN,
                new IHandleAssembleList() {
                    @Override
                    public void handleResponse(ResponseAssembleList response) {
                        
                        ptrlv.onRefreshComplete();
                        
                        ArrayList<AssembleBase> listAssemble = response.listAssemble;
                        
                        if (listAssemble != null && listAssemble.size() > 0) {
                            
                            if (listAssemble.size() < CustomConstants.ASSETS_PAGE_NUMBER) {
                                ptrlv.setMode(Mode.PULL_FROM_START);
                            }
                            
                            for (int i = 0; i < listAssemble.size(); i++) {
                                
                                AssembleBase assemble = listAssemble.get(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("sid", assemble.getSid());
                                map.put("name", assemble.getName());
                                if (assemble.assets != null) {
                                    map.put("assets", assemble.assets.getAssets());
                                    map.put("profit", assemble.assets.getProfit());
                                    map.put("subscripting", assemble.assets.getSubscripting());
                                    map.put("isbalance", assemble.getBalanceState() + "");
                                    map.put("balanceopt", assemble.getBalanceOpState() + "");
                                }
                                mListAssetsAssemble.add(map);
                            }
                            if (flagCurrentListType == 0)
                                initAssetsAdapter();
                        }
                        else {
                            if (startN == 0) {
                                // 如果为空,加载默认空布局
                                ptrlv.setVisibility(View.GONE);
                                defPage.setVisibility(View.VISIBLE);
                            }
                            else {
                                ptrlv.setMode(Mode.PULL_FROM_START);
                            }
                        }
                    }
                });
    }

    private void requestNoAssembleList(int of, int nm) {
        
        if (of == 0)
            mListNoAssetsAssemble.clear();

        final int startN;
        int pageN;

        if (of != -1 && nm != -1) {
            startN = of;
            pageN = nm;
        }
        else {
            startN = offsetNoAssets;
            pageN = pageNum;
        }

        RequestAssembleHelper.requestNoAssetsAssembleList(this, startN, pageN,
                new IHandleAssembleList() {
                    @Override
                    public void handleResponse(ResponseAssembleList response) {

                        ptrlv.onRefreshComplete();

                        ArrayList<AssembleBase> listAssemble = response.listAssemble;

                        if (listAssemble != null && listAssemble.size() > 0) {

                            if (listAssemble.size() < CustomConstants.ASSETS_PAGE_NUMBER) {
                                ptrlv.setMode(Mode.PULL_FROM_START);
                            }

                            for (int i = 0; i < listAssemble.size(); i++) {

                                AssembleBase assemble = listAssemble.get(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("sid", assemble.getSid());
                                map.put("name", assemble.getName());
                                if (assemble.assets != null) {
                                    map.put("assets", assemble.assets.getAssets());
                                    map.put("profit", assemble.assets.getProfit());
                                    map.put("subscripting", assemble.assets.getSubscripting());
                                    map.put("isbalance", assemble.getBalanceState() + "");
                                    map.put("balanceopt", assemble.getBalanceOpState() + "");
                                }
                                mListNoAssetsAssemble.add(map);
                            }
                            if (flagCurrentListType == 1)
                                initNoAssetsAdapter();
                        } 
                        else {
                            if (startN == 0) {
                                // 如果为空,加载默认空布局
                                ptrlv.setVisibility(View.GONE);
                                defPage.setVisibility(View.VISIBLE);
                            }
                            else {
                                ptrlv.setMode(Mode.PULL_FROM_START);
                            }
                        }
                    }
                });
    }

    /** 
    * @description 设置有持仓列表适配器
    * @author fangyan 
    */ 
    public void initAssetsAdapter() {
        
        if (mAssetsAdapter == null) {
            mAssetsAdapter = new AssetsAdapter(this, mListAssetsAssemble);
            ptrlv.setAdapter(mAssetsAdapter);
        }
        else {
            mAssetsAdapter.notifyDataSetChanged();
        }
    }

    /** 
    * @description 设置没有持仓列表适配器
    * @author fangyan 
    */ 
    public void initNoAssetsAdapter() {
        
        if (mNoAssetsAdapter == null) {
            mNoAssetsAdapter = new AssetsAdapter(this, mListNoAssetsAssemble);
            ptrlv.setAdapter(mNoAssetsAdapter);
        }
        else {
            mNoAssetsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_assemble_assets:
                if (flagCurrentListType == 0)
                    return;
                
                // 重置列表加载模式
                ptrlv.setMode(Mode.BOTH);
                flagCurrentListType = 0;
                mAssetsAdapter = null;
                
                assetsAssembleBtn.setSelected(true);
                noAssetsAssembleBtn.setSelected(false);
                assetsAssembleBtn.setTextColor(Color.WHITE);
                noAssetsAssembleBtn.setTextColor(R.color.color_5a6572);
                initAssetsAdapter();
                break;
            case R.id.tv_assemble_no_assets:
                if (flagCurrentListType == 1)
                    return;
                
                // 重置列表加载模式
                ptrlv.setMode(Mode.BOTH);
                flagCurrentListType = 1;
                mNoAssetsAdapter = null;
                
                assetsAssembleBtn.setSelected(false);
                noAssetsAssembleBtn.setSelected(true);
                assetsAssembleBtn.setTextColor(R.color.color_5a6572);
                noAssetsAssembleBtn.setTextColor(Color.WHITE);
                initNoAssetsAdapter();
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 申购结果返回
        if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE
                || resultCode == ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE
                || resultCode == ViewUtil.ASSEMBLE_DELETE_RESULT_CODE) {
            requestAssets();
            requestAssembleList(0, offsetAssets + CustomConstants.ASSETS_PAGE_NUMBER);
            requestNoAssembleList(0, offsetNoAssets + CustomConstants.ASSETS_PAGE_NUMBER);
        }
    }

}
