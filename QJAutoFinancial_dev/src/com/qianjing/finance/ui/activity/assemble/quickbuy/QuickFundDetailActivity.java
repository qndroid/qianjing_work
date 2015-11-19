
package com.qianjing.finance.ui.activity.assemble.quickbuy;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.quickbuy.FundItemDetails;
import com.qianjing.finance.model.quickbuy.QuickBuyDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.fragment.details.AdviceFragment;
import com.qianjing.finance.ui.fragment.details.ConfigFragment;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.FloatScrollView;
import com.qianjing.finance.view.slidingtabstrip.PagerSlidingTabStrip;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author liuchen
 */
public class QuickFundDetailActivity extends BaseActivity {
    /**
     * 配置颜色等级
     */
    private final int[] colors = new int[] {
            R.color.color_5ba7e1,
            R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7,
            R.color.color_a6d0e6
    };
    /**
     * UI
     */
    private TextView startBuyButton;
    /**
     * data
     */
    private int riskType;
    private QuickBuyDetail quickBuyDetail;
    
    private DisplayMetrics dm;
    private FloatScrollView fsvScroll;
    private PagerSlidingTabStrip pstsTabs;
    private ViewPager vpPager;
    private ViewPagerAdapter adapter;
    private TextView tvMinPurchase;
    private TextView tvRiskTxt;
    private TextView tvProfit;
    private TextView tvProfitTxt;
    private ImageView riskExplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        setTitleBar();
        requestFundDetail();
    }

    private void initData() {
        Intent intent = getIntent();
        riskType = intent.getIntExtra("type", -1);
    }

    private void requestFundDetail() {
        showLoading();
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("risk_type", riskType);
        AnsynHttpRequest.requestByPost(this, UrlConst.QUICK_BUY_DETAIL,
                fundDetailCallback, params);
    }

    private HttpCallBack fundDetailCallback = new HttpCallBack() {
        @Override
        public void back(String data, int status) {

            dismissLoading();
            if (data == null || data.equals("")) {
                showHintDialog("网络不给力!");
            } else {
                mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
            }
        }
    };

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0x01:
                    handleFundDetail(msg.obj.toString());
                    break;
                case 0x02:
                    handleCardList(msg.obj.toString());
                    break;
                case 0x03:
                    handleHYCard(msg.obj.toString());
                    break;
            }
        };
    };

    protected void handleFundDetail(String strJson) {
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            switch (ecode) {

                case 0:
                    quickBuyDetail = new QuickBuyDetail();
                    JSONObject data = object.optJSONObject("data");
                    JSONObject assemblyObj = data.optJSONObject("assembly");
                    JSONObject schemaObj = data.optJSONObject("schema");
                    JSONArray list = assemblyObj.optJSONArray("list");
                    double financialRisk = schemaObj
                            .optDouble("financial_risk");
                    double stockRadio = assemblyObj.optDouble("stock_ratio");
                    double noStockRadio = assemblyObj
                            .optDouble("nostock_ratio");
                    String stockText = assemblyObj.optString("stock_text");
                    String noStockText = assemblyObj.optString("nostock_text");
                    String limitInit = schemaObj.optJSONObject("limit")
                            .optString("init");
                    int atid = assemblyObj.optInt("atid");
                    String comment = assemblyObj.optString("comment");

                    for (int i = 0; i < list.length(); i++) {
                        FundItemDetails itemDetails = new FundItemDetails();
                        JSONObject fundDetail = list.getJSONObject(i);

                        itemDetails.abbrev = fundDetail.optString("abbrev");
                        itemDetails.fdcode = fundDetail.optString("fdcode");
                        itemDetails.rank = fundDetail.optString("rank");
                        itemDetails.ratio = fundDetail.optString("ratio");
                        itemDetails.recomm_reason = fundDetail
                                .optString("recomm_reason");
                        itemDetails.star = fundDetail.optString("star");
                        itemDetails.total_rank = fundDetail
                                .optString("total_rank");

                        quickBuyDetail.list.add(itemDetails);

                    }
                    quickBuyDetail.financialRisk = financialRisk;
                    quickBuyDetail.limitInit = limitInit;
                    quickBuyDetail.noStockRatio = noStockRadio;
                    quickBuyDetail.stockRatio = stockRadio;
                    quickBuyDetail.stockName = stockText;
                    quickBuyDetail.noStockName = noStockText;
                    quickBuyDetail.atid = atid;
                    quickBuyDetail.common = comment;

                    /**
                     * updateUI
                     */
                    updateUI();

                    tvProfit.setText(assemblyObj.optString("yield") + "%");
                    tvProfitTxt.setText(assemblyObj.optString("yield_text"));
                    tvRiskTxt.setText(assemblyObj.optString("risk_text"));
                    tvMinPurchase.setText("¥" + limitInit + "起购");

                    break;
                default:
                    showHintDialog(emsg);
                    // finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showHintDialog("数据解析异常!");
            finish();
        }
    }

    /**
     * 根据请求返回结果，更新UI
     */
    private void setTitleBar() {
        setTitleBack();
        switch (riskType) {
            case 1:
                setTitleWithId(R.string.jing_qu_xing_zu_he);
                break;
            case 2:
                setTitleWithId(R.string.wen_jian_xiang_zu_he);
                break;
            case 3:
                setTitleWithId(R.string.bao_shou_xing_zu_he);
                break;
            case 4:
                setTitleWithId(R.string.ju_dui_shou_yi_zu_he);
                break;
        }
    }

    /**
     * 获取银行卡列表
     */
    private void requestCardList() {
        showLoading();
        // 请求银行卡信息
        AnsynHttpRequest.requestByPost(this, UrlConst.CARD_LIST,
                cardListHandle, null);
    }

    private HttpCallBack cardListHandle = new HttpCallBack() {

        @Override
        public void back(String data, int status) {

            dismissLoading();
            if (data != null && !data.equals("")) {

                mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
            } else {
                showHintDialog("网络不给力!");
            }
        }
    };

    /**
     * 处理银行卡返回数据
     * 
     * @param obj
     */
    private void handleCardList(String obj) {

        try {
            JSONObject object = new JSONObject(obj);
            JSONObject data = object.optJSONObject("data");
            int ecode = data.optInt("ecode");
            String reason = data.optString("emsg");
            switch (ecode) {

                case ErrorConst.EC_OK:
                    JSONArray cards = data.optJSONArray("cards");
                    /**
                     * 已经有银行卡信息，直接跳转
                     */
                    if (cards != null && cards.length() > 0) {

                        startAnotherActivity();
                    } else {

                        JSONArray arrayUnboundCard = data.getJSONArray("unbc");
                        /**
                         * 有未解绑银行卡
                         */
                        if (arrayUnboundCard != null
                                && arrayUnboundCard.length() > 0) {

                            recoverCard(arrayUnboundCard);
                        } else {

                            showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
                            startActivity(new Intent(this,
                                    QuickBillActivity.class));
                        }
                    }
                    break;
                default:
                    dismissLoading();
                    showHintDialog(reason);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showHintDialog("数据解析错误");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson="
                    + obj.toString());
        }
    }

    private void recoverCard(JSONArray arrayUnboundCard) {

        ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
        final Card maxOldCard = new Card();
        maxOldCard.setBoundTime(9223372036854775806L);

        for (int i = 0; i < arrayUnboundCard.length(); i++) {
            JSONObject jsonOb = (JSONObject) arrayUnboundCard.opt(i);
            String bank = jsonOb.optString("bank");
            String card = jsonOb.optString("card");
            long boundTime = Long.valueOf(jsonOb.optString("boundT"));
            Card info = new Card();
            info.setNumber(card);
            info.setBankName(bank);
            info.setBoundTime(boundTime);
            listUnboundedCard.add(info);
        }

        /**
         * 取出最先解绑的卡
         */
        for (Card tempInfo : listUnboundedCard) {
            if (maxOldCard.getBoundTime() > tempInfo.getBoundTime()) {
                maxOldCard.setNumber(tempInfo.getNumber());
                maxOldCard.setBankName(tempInfo.getBankName());
                maxOldCard.setBoundTime(tempInfo.getBoundTime());
            }
        }

        String strMsg = "您已经有解绑过的" + maxOldCard.getBankName() + "卡，卡号为"
                + StringHelper.hintCardNum(maxOldCard.getNumber()) + "，是否直接还原该卡？";

        showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 发送银行卡还原接口请求
                showLoading();
                requestHYCard(maxOldCard.getNumber());

                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void requestHYCard(String cardnum) {

        // 发送银行卡还原接口请求
        showLoading();
        Hashtable<String, Object> map = new Hashtable<String, Object>();
        map.put("cd", cardnum);
        AnsynHttpRequest.requestByPost(this, UrlConst.CARD_HTYCARD,
                hyCardCallback, map);
    }

    private HttpCallBack hyCardCallback = new HttpCallBack() {

        @Override
        public void back(String data, int status) {
            dismissLoading();
            if (data != null && !data.equals("")) {

                mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
            } else {
                showHintDialog("网络不给力");
            }
        }
    };

    private void handleHYCard(String json) {

        try {
            JSONObject data = new JSONObject(json);
            int ecode = data.optInt("ecode");
            String reason = data.optString("emsg");
            switch (ecode) {

                case ErrorConst.EC_OK:
                    showToast("银行卡已成功还原");
                    startAnotherActivity();
                    break;
                default:
                    showHintDialog(reason);
                    break;
            }
        } catch (Exception e) {

            showHintDialog("数据解析异常!");
        }
    }

    /**
     * 跳转到组合申购页面
     */
    private void startAnotherActivity() {

        Intent intent = new Intent(this, QuickAssembleBuyActivity.class);
        intent.putExtra("type", riskType);
        intent.putExtra("min_value", quickBuyDetail.limitInit);
        startActivityForResult(intent, CustomConstants.QUICK_PUECHASE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 申购结果返回
        if (resultCode == ViewUtil.ASSEMBLE_BUY_RESULT_CODE) {
            if (data.hasExtra("lottery")) {
                LotteryActivity lottery = (LotteryActivity) data
                        .getSerializableExtra("lottery");
                if (lottery != null)
                    PrefManager.getInstance().putString(
                            PrefManager.KEY_LOTTERY_URL, lottery.strUrl);
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    

    private void initView() {
        setContentView(R.layout.activity_quick_buy_detail_layout_2_1);
        dm = getResources().getDisplayMetrics();

        fsvScroll = (FloatScrollView) findViewById(R.id.fsv_scroll);
        pstsTabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vpPager = (ViewPager) findViewById(R.id.vp_pager);
        // updateUI();
        fsvScroll.setFloatView(pstsTabs, vpPager);

        tvMinPurchase = (TextView) findViewById(R.id.tv_min_purchase);
        tvRiskTxt = (TextView) findViewById(R.id.tv_risk_txt);
        tvProfit = (TextView) findViewById(R.id.tv_profit);
        tvProfitTxt = (TextView) findViewById(R.id.tv_profit_txt);
        riskExplain = (ImageView) findViewById(R.id.iv_risk_explain);
        riskExplain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showHintDialog(
                        getString(R.string.fen_xian_zhi_shu_shuo_ming),
                        getString(R.string.fen_xian_zhi_shu_shuo_ming_nei_rong),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        }, getString(R.string.zhi_dao_l));
            }
        });

        startBuyButton = (TextView) findViewById(R.id.start_buy_btn);
        startBuyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UserManager.getInstance().getUser() != null) {
                    requestCardList();
                } else {
                    Intent intent = new Intent(QuickFundDetailActivity.this,
                            LoginActivity.class);
                    intent.putExtra("LoginTarget",
                            LoginTarget.QUICK_FUND_DETAIL.getValue());
                    startActivity(intent);
                }
            }
        });

    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            vpPager.setAdapter(adapter);
            pstsTabs.setViewPager(vpPager);
            setTabsValue();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int pstsTabsHeight = pstsTabs.getHeight();
        int fsvScrollHeight = fsvScroll.getHeight();
        LayoutParams layoutParams = vpPager.getLayoutParams();
        layoutParams.height = fsvScrollHeight - pstsTabsHeight;
        vpPager.setLayoutParams(layoutParams);

        super.onWindowFocusChanged(hasFocus);
    }

    public Fragment getFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                ConfigFragment configFragment = new ConfigFragment();
                if (quickBuyDetail != null) {
                    configFragment.setFundDetails(quickBuyDetail);
                }
                fragment = configFragment;
                break;
            case 1:
                AdviceFragment adviceFragment = new AdviceFragment();
                if (quickBuyDetail != null) {
                    adviceFragment.setAdviceText(quickBuyDetail.common);
                }
                fragment = adviceFragment;
                break;
        }
        return fragment;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter
            implements
            PagerSlidingTabStrip.ViewTabProvider {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int arg0) {
            return getFragment(arg0);
        }

        @Override
        public View getPageView(int position) {

            View view = View.inflate(QuickFundDetailActivity.this,
                    R.layout.item_assemble_tab, null);
            ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);

            if (position == 0) {
                ivIcon.setBackgroundResource(R.drawable.selector_icon_details);
                tvTitle.setText(getString(R.string.configure_detail));
            } else {
                ivIcon.setBackgroundResource(R.drawable.selector_icon_advice);
                tvTitle.setText(getString(R.string.expert_advice));
            }
            return view;
        }
    }

    private void setTabsValue() {
        pstsTabs.setShouldExpand(true);
        pstsTabs.setDividerColor(getResources()
                .getColor(R.color.color_00ffffff));
        pstsTabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        pstsTabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        pstsTabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        pstsTabs.setIndicatorColor(getResources().getColor(R.color.low_stroke));
        pstsTabs.setTabBackground(0);
    }

}
