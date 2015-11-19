
package com.qianjing.finance.widget.adapter.fundtype;

import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**********************************************************
 * @文件名称：FundListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月11日 下午2:21:33
 * @文件描述：基金列表适配器
 * @修改历史：2015年6月11日创建初始版本
 **********************************************************/
public class FundListAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<JSONObject> mListData;
    private ViewHolder holder;
    private LayoutInflater inflater;

    public FundListAdapter(Context context, ArrayList<JSONObject> listData)
    {
        this.mContext = context;
        this.mListData = listData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mListData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mListData.get(position);
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
            convertView = inflater.inflate(R.layout.activity_fund_style_item_layout, null);
            holder.fundTitleTextView = (TextView) convertView.findViewById(R.id.fund_title);
            holder.fundProfitTextView = (TextView) convertView.findViewById(R.id.fund_shouyi);
            holder.fundRateTextView = (TextView) convertView.findViewById(R.id.fund_ratechange);
            holder.fundRateTitleTextView = (TextView) convertView
                    .findViewById(R.id.fund_ratechange_title);
            holder.fundStartBuyTextView = (TextView) convertView.findViewById(R.id.qi_gou_money);
            holder.fundProfitLabel = (TextView) convertView.findViewById(R.id.fund_wanfeng);
            holder.fundPrecertView = (TextView) convertView.findViewById(R.id.pretect_view);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        // 根据数据初始化item
        JSONObject fund = (JSONObject) getItem(position);
        holder.fundTitleTextView.setText(fund.opt("abbrev").toString()
                + mContext.getString(R.string.left_brackets)
                + fund.optString("fdcode") + mContext.getString(R.string.right_brackets));

        String minScript = fund.optString("minSubscript");
        if (Double.parseDouble(minScript) > 100000)
        {

            holder.fundStartBuyTextView.setText("￥" + (int) (Double.parseDouble(minScript) / 10000)
                    + mContext.getString(R.string.wan));
        }
        else
        {
            holder.fundStartBuyTextView.setText("￥" + minScript);
        }
        // 非货币型
        if (!fund.optString("type").equals("3"))
        {
            holder.fundProfitTextView.setText(fund.optString("nav"));
            holder.fundProfitLabel.setText(mContext.getString(R.string.zui_xin_jin_zhi));
            holder.fundRateTitleTextView.setText(mContext.getString(R.string.yue_die_zhang_fu));
            if (Double.parseDouble(fund.optString("rateMonth")) < 0.0)
            {
                holder.fundPrecertView.setTextColor(mContext.getResources().getColor(
                        R.color.color_307d1b));
                holder.fundRateTextView.setTextColor(mContext.getResources().getColor(
                        R.color.color_307d1b));
            }
            else
            {
                holder.fundPrecertView.setTextColor(mContext.getResources().getColor(
                        R.color.color_e41f23));
                holder.fundRateTextView.setTextColor(mContext.getResources().getColor(
                        R.color.color_e41f23));
            }
            holder.fundRateTextView.setText(StringHelper.formatString(
                    fund.optString("rateMonth"), StringFormat.FORMATE_2));
        }
        else
        {
            // 货币型
            holder.fundPrecertView.setTextColor(mContext.getResources().getColor(
                    R.color.color_e41f23));
            holder.fundRateTextView.setText(fund.optString("rate7Day").toString());
            holder.fundProfitTextView.setText(fund.optString("profit10K"));
            holder.fundProfitLabel.setText(mContext.getString(R.string.str_wanfenshouyi));
            holder.fundRateTitleTextView.setText(mContext.getString(R.string.str_sevenchangedate));
        }
        return convertView;
    }

    public void addData(ArrayList<JSONObject> tempData)
    {
        this.mListData.addAll(tempData);
        notifyDataSetChanged();
    }

    public void refreshAllData(ArrayList<JSONObject> allData)
    {
        this.mListData = allData;
        notifyDataSetChanged();
    }

    private static class ViewHolder
    {
        private TextView fundTitleTextView;
        private TextView fundProfitTextView;
        private TextView fundProfitLabel;
        private TextView fundRateTextView;
        private TextView fundRateTitleTextView;
        private TextView fundStartBuyTextView;
        private TextView fundPrecertView;
    }
}
