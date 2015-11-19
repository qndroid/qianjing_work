
package com.qianjing.finance.widget.adapter.dayprofit;

import com.qianjing.finance.model.assemble.AssembleDayProfitDetail;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**********************************************************
 * @文件名称：DayProfitAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年8月12日 上午10:27:43
 * @文件描述：盈亏详情Adapter
 * @修改历史：2015年8月12日创建初始版本
 **********************************************************/
public class DayProfitDetailAdapter extends BaseAdapter
{
    private Context mContext;
    private ViewHolder holder;
    private LayoutInflater layoutInflater;
    private ArrayList<AssembleDayProfitDetail> mDataList;

    public DayProfitDetailAdapter(Context mContext, ArrayList<AssembleDayProfitDetail> mDataList)
    {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_day_detail_profit, null);
            holder.fundNameView = (TextView) convertView.findViewById(R.id.fund_name_view);
            holder.dayRateView = (TextView) convertView.findViewById(R.id.day_rose_view);
            holder.moneyView = (TextView) convertView.findViewById(R.id.profit_view);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AssembleDayProfitDetail dayProfitDetail = (AssembleDayProfitDetail) getItem(position);
        holder.fundNameView.setText(dayProfitDetail.getFundName());
        if (Double.parseDouble(dayProfitDetail.getFundDayRate()) >= 0)
        {
            holder.dayRateView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
            holder.dayRateView.setText(StringHelper.formatString(
                    dayProfitDetail.getFundDayRate(), StringFormat.FORMATE_2)
                    + mContext.getString(R.string.str_percent));
        }
        else
        {
            holder.dayRateView.setTextColor(mContext.getResources().getColor(R.color.color_66b48e));
            holder.dayRateView.setText(StringHelper.formatString(
                    dayProfitDetail.getFundDayRate(), StringFormat.FORMATE_2)
                    + mContext.getString(R.string.str_percent));
        }

        if (dayProfitDetail.getFundDayProfit() >= 0)
        {
            holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
            holder.moneyView.setText(mContext.getString(R.string.jia_hao)
                    + StringHelper.formatString(
                            String.valueOf(dayProfitDetail.getFundDayProfit()),
                            StringFormat.FORMATE_2));
        }
        else
        {
            holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.color_66b48e));
            holder.moneyView.setText(StringHelper.formatString(
                    String.valueOf(dayProfitDetail
                            .getFundDayProfit()), StringFormat.FORMATE_2));
        }
        return convertView;
    }

    private static class ViewHolder
    {
        private TextView fundNameView;
        private TextView dayRateView;
        private TextView moneyView;
    }
}
