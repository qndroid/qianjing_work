
package com.qianjing.finance.ui.activity.assemble.asset.fragment;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.asset.SchemeAssetData;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.p2p.P2PSteadyDetailReponse;
import com.qianjing.finance.model.p2p.P2PSteadyDetailReponse.P2PPortDetail;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.helper.RequestAssembleHelper;
import com.qianjing.finance.net.ihandle.IHandleAssembleDetail;
import com.qianjing.finance.net.response.model.ResponseAssembleDetail;
import com.qianjing.finance.ui.activity.assemble.AssembleDetailActivity;
import com.qianjing.finance.ui.activity.assemble.AssembleSteadyDetailActivity;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.widget.adapter.assemble.AssembleHoleListAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 组合有无持仓基类Fragment
 * @author majinxin
 * @date 2015年9月8日
 */
public abstract class AssembleHoldBaseFragment extends BaseFragment implements OnItemClickListener {
    /**
     * UI
     */
    private Context mContext;
    private View contentView;
    protected PullToRefreshListView pullToListView;
    protected ListView mListView;
    protected AssembleHoleListAdapter mAdapter;
    /**
     * Data
     */
    private P2PPortDetail beanPortDetail;
    protected ArrayList<Serializable> mList;
    protected int pageIndex = 0;
    protected int offset = 0;
    protected int pageSize = 20;
    protected int type = 0;
    protected boolean loadingFlag = true;

    /**
     * 是否加载更多,是否刷新
     */
    protected boolean isLoadMore = false;
    protected boolean isRefresh = false;

    protected abstract int getType();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mList = new ArrayList<Serializable>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        contentView = (View) inflater.inflate(
                R.layout.activity_fund_style_layout, null);
        initView();
        return contentView;
    }

    private void initView() {
        pullToListView = (PullToRefreshListView) contentView
                .findViewById(R.id.fund_search_list);
        pullToListView.setOnRefreshListener(initListRefreshListener());
        // 只启动下拉加载更多
        pullToListView.setMode(Mode.BOTH);
        // 不显示指示器
        pullToListView.setShowIndicator(false);
        mListView = pullToListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
    }

    private OnRefreshListener2<ListView> initListRefreshListener() {
        OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                onPullDownRefreshView(refreshView);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                onPullUpToRefreshView(refreshView);
            }
        };
        return refreshListener;
    }

    /**
     * 下拉刷新逻辑处理
     * 
     * @param refreshView
     */
    protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView) {
        isLoadMore = true;
        isRefresh = false;
        if (loadingFlag) {
            pageIndex = 0;
        } else {
            pageIndex += 1;
        }
        offset = (pageIndex) * pageSize;
        requestAssembleList(getType(), false);
    }

    protected void onPullDownRefreshView(PullToRefreshBase<?> refreshView) {
        isLoadMore = false;
        isRefresh = true;
        pageIndex = 0;
        offset = 0;
        // requestAssembleList(getType(), false);
        requestP2pDetail(false);
    }

    /**
     * 刷新完成逻辑处理
     */
    protected void refreshComplete() {
        if (pullToListView != null && pullToListView.isRefreshing()) {
            pullToListView.onRefreshComplete();
        }
    }

    /**
     * 发送获取基金列表请求
     */
    protected void requestAssembleList(int type, boolean isShow) {
        if (isShow) {
            showLoading();
        }
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("nm", pageSize);
        params.put("of", offset);
        params.put("type", type);
        AnsynHttpRequest.requestByPost(mContext, UrlConst.ASSEMBLE_LIST,
                fundTypeCallback, params);
    }

    /**
     * 请求处理回调接口
     */
    private HttpCallBack fundTypeCallback = new HttpCallBack() {
        @Override
        public void back(String data, int status) {
            dismissLoading();
            if (data != null) {
                mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    handleFundList(msg.obj.toString());
                    break;
            }
        };
    };

    public void requestP2pDetail(boolean isShow) {
        if (isShow) {
            showLoading();
        }

        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("mobile", UserManager.getInstance().getUser().getMobile());
        RequestManager.request(mContext, UrlConst.P2P_PORT_DETAIL, upload,
                P2PSteadyDetailReponse.class, new XCallBack() {
                    @Override
                    public void success(BaseModel model) {
                        if (model.stateCode == ErrorConst.EC_OK) {
                            beanPortDetail = ((P2PSteadyDetailReponse) model).portDetail;
                            if (beanPortDetail != null) {
                                mList.clear();
                                // 有资产且当前是有资产列表
                                if (beanPortDetail.assets > 0 && getType() == 1) {

                                    mList.add(beanPortDetail);
                                }
                                // 只有收益，没有资产情况
                                if (beanPortDetail.assets <= 0 && beanPortDetail.profit != 0 && getType() == 2) {

                                    mList.add(beanPortDetail);
                                }
                            }

                            requestAssembleList(getType(), false);
                        }

                    }

                    @Override
                    public void fail() {
                        mList.clear();
                        requestAssembleList(getType(), false);
                    }
                });
    }

    private void handleFundList(String data) {
        dismissLoading();
        refreshComplete();
        loadingFlag = false;
        try {
            JSONObject object = new JSONObject(data);
            int ecode = object.optInt("ecode");
            String reason = object.optString("emsg");
            switch (ecode) {
                case ErrorConst.EC_OK:
                    JSONObject result = object.optJSONObject("data");
                    JSONArray array = result.optJSONArray("schemas-assetses");
                    if (array != null & array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            SchemeAssetData scheme = new SchemeAssetData();
                            JSONObject jsonObj = array.getJSONObject(i);
                            JSONObject schemeJson = jsonObj
                                    .optJSONObject("schema");
                            JSONObject assetsJson = jsonObj
                                    .optJSONObject("assets");

                            scheme.sid = schemeJson.optString("sid");
                            scheme.uid = schemeJson.optString("uid");
                            scheme.name = schemeJson.optString("name");
                            scheme.state = schemeJson.optString("state");
                            scheme.balanceState = schemeJson
                                    .optString("balance_note_state");
                            scheme.balanceOperationState = schemeJson
                                    .optString("balance_operation_state");
                            /**
                             * 只有有持仓才解析此部分数据
                             */
                            if (getType() == 1) {
                                scheme.assets = assetsJson.optString("assets");
                                scheme.profitYesterDay = assetsJson
                                        .optString("profitYesday");
                                scheme.subscripting = assetsJson
                                        .optString("subscripting");
                                scheme.redemping = assetsJson
                                        .optString("redemping");
                                scheme.profit = assetsJson.optString("profit");
                            }

                            mList.add(scheme);
                        }
                        updateUI();
                    }
                    initBackStatus();
                    break;
                default:
                    initBackStatus();
                    showHintDialog(reason);
                    break;
            }
        } catch (Exception e) {
            initBackStatus();
            e.printStackTrace();
        }
    }

    private void initBackStatus() {
        isLoadMore = false;
        isRefresh = false;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new AssembleHoleListAdapter(mContext, mList);
            mListView.setAdapter(mAdapter);
        } else {
            if (isLoadMore) {
                mAdapter.addData(mList);
            }

            if (isRefresh) {
                mAdapter.updateData(mList);
            }
        }
    }

    /**
     * 列表Item点击事件处理
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (mAdapter.getItem(position) instanceof SchemeAssetData) {
            SchemeAssetData data = (SchemeAssetData) mAdapter.getItem(position);
            RequestAssembleHelper.requestAssembleDetail((Activity) mContext,
                    data.sid, new IHandleAssembleDetail() {
                        @Override
                        public void handleResponse(
                                ResponseAssembleDetail response) {
                            AssembleDetail detail = response.detail;
                            if (detail != null) {
                                Intent intent = new Intent();
                                intent.putExtra(
                                        Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, detail);
                                // 已申购组合
                                intent.setClass(mContext,
                                        AssembleDetailActivity.class);
                                startActivityForResult(intent,
                                        ViewUtil.REQUEST_CODE);
                            }
                        }
                    });
        } else {
            if (beanPortDetail != null
                    && (beanPortDetail.assets != 0 || beanPortDetail.order != 0)) {
                Intent intent = new Intent(mContext,
                        AssembleSteadyDetailActivity.class);
                intent.putExtra(Flag.EXTRA_BEAN_P2P_PORT_DETAIL, beanPortDetail);
                startActivityForResult(intent, ViewUtil.REQUEST_CODE);
            }
        }
    }
}
