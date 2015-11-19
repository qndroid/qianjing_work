
package com.qianjing.finance.ui.activity.virtual;

import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.model.virtual.Assetses;
import com.qianjing.finance.model.virtual.HoldingDetailsBean;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.virtual.CustomDetailsView;
import com.qianjing.finance.widget.adapter.base.BaseCustomAdapter;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HoldingDetails extends BaseActivity implements OnClickListener {

    private CustomDetailsView cdv;
    private Button btnInvest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_virtual_holding_details);
        setTitleWithId(R.string.title_holding_detail);
        setTitleBack();

        Intent intent = getIntent();
        pModel = (PurchaseModel) intent.getSerializableExtra("purchaseInfo");

        cdv = (CustomDetailsView) findViewById(R.id.cdv_list_view);
        /**
		 * 
		 * */
        btnInvest = (Button) findViewById(R.id.btn_invest);
        btnInvest.setVisibility(View.GONE);
        btnInvest.setOnClickListener(this);

        requestVirtualDetails();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_invest:
                // jump2PurchasePage();
                break;
        }
    }

    /**
     * request virtual assets details data
     */
    public void requestVirtualDetails() {
        showLoading();
        Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
        // System.out.println("sid是"+pModel.getSid());
        // System.out.println("接口是"+pModel.getHoldDetailInterface());
        hashTable.put("sid", pModel.getSid());
        AnsynHttpRequest.requestByPost(this, pModel.getHoldDetailInterface(), new HttpCallBack() {

            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, hashTable);
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String jsonStr = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    LogUtils.syso("虚拟持仓明细：" + jsonStr);
                    analysisDetailsData(jsonStr);
                    break;
            }
        };
    };
    private PurchaseModel pModel;
    private HoldingDetailsBean detailsInfo;

    protected void analysisDetailsData(String jsonStr) {

        if (jsonStr == null || "".equals(jsonStr)) {
            dismissLoading();
            showHintDialog(getString(R.string.net_prompt));
            return;
        }

        try {

            JSONObject jobj = new JSONObject(jsonStr);
            int ecode = jobj.optInt("ecode");
            String emsg = jobj.optString("emsg");

            if (ecode == 0) {

                String data = jobj.optString("data");
                // Gson gson = new Gson();
                // detailsInfo = gson.fromJson(data, HoldingDetailsBean.class);

                JSONObject dataObj = new JSONObject(data);
                JSONArray assetesArray = dataObj.optJSONArray("assetses");
                detailsInfo = new HoldingDetailsBean();
                for (int i = 0; i < assetesArray.length(); i++) {

                    JSONObject arrayObj = assetesArray.getJSONObject(i);

                    Assetses assetsInfo = new Assetses();

                    assetsInfo.income = (float) arrayObj.optDouble("income");
                    assetsInfo.invest = (float) arrayObj.optDouble("invest");
                    assetsInfo.nav = (float) arrayObj.optDouble("nav");
                    assetsInfo.profitT = (float) arrayObj.optDouble("profitT");
                    assetsInfo.profitYesday = (float) arrayObj.optDouble("profitYesday");
                    assetsInfo.shares = (float) arrayObj.optDouble("shares");
                    assetsInfo.unpaid = (float) arrayObj.optDouble("unpaid");

                    assetsInfo.moditm = arrayObj.optInt("moditm");
                    assetsInfo.sid = arrayObj.optInt("sid");

                    assetsInfo.abbrev = arrayObj.optString("abbrev");
                    assetsInfo.bank = arrayObj.optString("bank");
                    assetsInfo.card = arrayObj.optString("card");
                    assetsInfo.fdcode = arrayObj.optString("fdcode");
                    assetsInfo.tradeacco = arrayObj.optString("tradeacco");
                    assetsInfo.uid = arrayObj.optString("uid");

                    detailsInfo.assetses.add(assetsInfo);

                }

                if (detailsInfo != null) {
                    initDataView();
                    dismissLoading();
                }

            } else {
                showHintDialog(emsg);
                dismissLoading();
            }
        } catch (Exception e) {
            showHintDialog(getString(R.string.net_data_error));
            dismissLoading();
        }
    }

    private void initDataView() {

        cdv.setCustomAdapter(new BaseCustomAdapter() {

            @Override
            public List<SparseArray<Serializable>> getDetailData() {

                List<SparseArray<Serializable>> dataList = new ArrayList<SparseArray<Serializable>>();

                for (int i = 0; i < detailsInfo.assetses.size(); i++) {
                    SparseArray<Serializable> sparseArray = new SparseArray<Serializable>();
                    sparseArray.put(1, detailsInfo.assetses.get(i).abbrev);
                    sparseArray.put(2, StringHelper.formatString(
                            String.valueOf(detailsInfo.assetses.get(i).shares),
                            StringFormat.FORMATE_2));
                    sparseArray.put(
                            3,
                            StringHelper.formatString(
                                    String.valueOf(detailsInfo.assetses.get(i).shares
                                            * detailsInfo.assetses.get(i).nav),
                                    StringFormat.FORMATE_2));
                    dataList.add(sparseArray);
                }

                return dataList;
            }

            @Override
            public void setCustomItemClick(View view, int position) {

            }

        });

    }
}
