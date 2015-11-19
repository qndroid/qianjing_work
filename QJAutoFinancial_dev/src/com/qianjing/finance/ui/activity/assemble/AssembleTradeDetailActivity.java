
package com.qianjing.finance.ui.activity.assemble;

import com.qianjing.finance.model.history.HistoryDetailsInter;
import com.qianjing.finance.model.virtual.AssetsDetailBean;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.virtual.CustomDetailsView;
import com.qianjing.finance.widget.adapter.base.BaseCustomAdapter;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:history details
 * @author liuchen
 */
public class AssembleTradeDetailActivity extends BaseActivity {

    private CustomDetailsView customList;
    private AssetsDetailBean detailsInfo;
    private TextView tv_date;
    private TextView tv_type;
    private TextView tv_status;
    private TextView feeWay;
    private TextView redWay;
    private HistoryDetailsInter detailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myasset_trade_detail);
        initView();
    }

    private void initView() {

        setTitleWithId(R.string.TITLE_DETAIL);
        setTitleBack();

        Intent intent = getIntent();
        detailInfo = (HistoryDetailsInter) intent.getSerializableExtra("detailInfo");

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_status = (TextView) findViewById(R.id.tv_status);
        feeWay = (TextView) findViewById(R.id.tv_feeway);
        redWay = (TextView) findViewById(R.id.tv_way);

        tv_date.setText(detailInfo.dateStr);
        tv_type.setText(detailInfo.operation);
        tv_status.setText(detailInfo.optState);
        // feeWay.setText("");
        // redWay.setText("");

        customList = (CustomDetailsView) findViewById(R.id.cdv_list_view);

        if ("投资".equals(detailInfo.operation.trim())) {
            customList.setTwoItem("申购金额");
        } else if ("赎回".equals(detailInfo.operation.trim())) {
            customList.setTwoItem("赎回份额");
        }

        customList.setCustomAdapter(new BaseCustomAdapter() {

            @Override
            public List<SparseArray<Serializable>> getDetailData() {

                List<SparseArray<Serializable>> dataList = new ArrayList<SparseArray<Serializable>>();

                for (int i = 0; i < detailInfo.historyDetails.size(); i++) {
                    SparseArray<Serializable> sparseArray = new SparseArray<Serializable>();
                    sparseArray.put(1, detailInfo.historyDetails.get(i).get("abbrevs"));
                    sparseArray.put(2, StringHelper.formatString(
                            String.valueOf(detailInfo.historyDetails.get(i).get("fdshares")),
                            StringFormat.FORMATE_2));
                    sparseArray.put(3, detailInfo.historyDetails.get(i).get("fdstates"));
                    dataList.add(sparseArray);
                }

                return dataList;
            }

            @Override
            public void setCustomItemClick(View view,
                    int position) {
                showHintDialog(detailInfo.reasons.get(position));
            }
        });

    }
}
