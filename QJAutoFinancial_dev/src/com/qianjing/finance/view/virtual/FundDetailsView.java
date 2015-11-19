
package com.qianjing.finance.view.virtual;

import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qianjing.finance.view.indictorview.IndiactorView;
import com.qianjing.finance.view.indictorview.IndiactorView.IndictorClickListener;
import com.qianjing.finance.widget.adapter.base.BaseDetailsAdapter;
import com.qjautofinancial.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FundDetailsView extends RelativeLayout
{

    /**
     * 自定义组合详情View anthor:liuchen
     */

    /**
     * 配置颜色等级
     */
    private final int[] colors = new int[]
    {
            R.color.color_5ba7e1, R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7,
            R.color.color_a6d0e6
    };

    private String setDetail;
    private TextView tvSetDetail;
    private List<SparseArray<Object>> dataList;
    private Context context;
    private IndiactorView indictorView;
    private PieGraph pieGraph;

    private LinearLayout llContent;

    private int position;

    private AssembleNewItemLayout assembleItem;

    public FundDetailsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        initView(context);
    }

    public FundDetailsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initView(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FundDetailView);
        setDetail = ta.getString(R.styleable.FundDetailView_setting_detail);

        tvSetDetail.setText(setDetail);
    }

    public FundDetailsView(Context context)
    {
        super(context);
        this.context = context;
        initView(context);
    }

    public void setDetailAdapter(final BaseDetailsAdapter baseDetailsAdapter)
    {

        llContent.removeAllViews();
        /**
         * init risk view
         */
        int riskTypeValue = baseDetailsAdapter.getRiskTypeValue();
        indictorView.setPosition(riskTypeValue);
        indictorView.setIndictorClickListener(new IndictorClickListener()
        {
            @Override
            public void onClick()
            {
                baseDetailsAdapter.setRiskTypeClick();
            }
        });
        /**
         * init pie view
         */
        dataList = baseDetailsAdapter.getDetailData();
        ArrayList<PieSlice> pieNodes = new ArrayList<PieSlice>();
        for (int i = 0; i < dataList.size(); i++)
        {
            float value = Float.parseFloat(dataList.get(i).get(2) + "");

            PieSlice pieSlice = new PieSlice();
            pieSlice.setColor(getResources().getColor(colors[i]));
            pieSlice.setValue(value * 100);
            pieNodes.add(pieSlice);

            assembleItem = new AssembleNewItemLayout(context);
            assembleItem.initData(
                    colors[i],
                    dataList.get(i).get(1) + "",
                    context.getString(R.string.left_brackets)
                            + dataList.get(i).get(3) + context.getString(R.string.right_brackets),
                    StringHelper.formatString((Float.parseFloat(dataList.get(i).get(2)
                            + "") * 100)
                            + "", StringFormat.FORMATE_1));

            final int position = i;
            assembleItem.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    baseDetailsAdapter.setDetailsItemClick(position);
                }
            });

            llContent.addView(assembleItem);

        }

        pieGraph.setDrawText(baseDetailsAdapter.getNostock_text(),
                baseDetailsAdapter.getNostockRatio() + context.getString(R.string.str_percent),
                baseDetailsAdapter.getStock_text(),
                baseDetailsAdapter.getStockRatio() + context.getString(R.string.str_percent),
                pieNodes);

    }

    private void initView(Context context)
    {
        View view = View.inflate(context, R.layout.fund_detail_view, this);
        tvSetDetail = (TextView) view.findViewById(R.id.tv_setting_details);
        pieGraph = (PieGraph) view.findViewById(R.id.pg_pie_graph);
        indictorView = (IndiactorView) view.findViewById(R.id.iv_indictor_view);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);

    }
}
