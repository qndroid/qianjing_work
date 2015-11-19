
package com.qianjing.finance.widget.adapter;

import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Title: MyAssetsAdapter Description: 我的资产适配
 * 
 * @author zhangqi
 * @date 2014-6-26
 */
public class AssetsAdapter extends BaseAdapter {
    Context mConext;
    private LayoutInflater layoutFlater;
    private List<Map<String, String>> listItemData;

    public AssetsAdapter(Context context, List<Map<String, String>> listItemData) {
        mConext = context;
        layoutFlater = (LayoutInflater) mConext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItemData = listItemData;
    }

    @Override
    public int getCount() {
        return listItemData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutFlater.inflate(R.layout.item_assets, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
            holder.tv_moneyb = (TextView) convertView.findViewById(R.id.tv_moneyb);
            holder.tv_totalb = (TextView) convertView.findViewById(R.id.tv_totalb);
            holder.tv_subway = (TextView) convertView.findViewById(R.id.tv_subway);
            holder.balance = (ImageView) convertView.findViewById(R.id.iv_balance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.shape_white);
        holder.tv_name.setText(listItemData.get(position).get("name"));

        String strAssets = listItemData.get(position).get("assets");
        String strProfit = listItemData.get(position).get("profit");
        String strSubs = listItemData.get(position).get("subscripting");
        // 总市值
        if (!StringHelper.isBlank(strAssets)) {
            if (strAssets.startsWith("-")) {
                holder.tv_money.setTextColor(mConext.getResources().getColor(R.drawable.green));
            } else {
                holder.tv_money.setTextColor(mConext.getResources().getColor(R.color.red));
            }
            holder.tv_money.setText(""
                    + StringHelper.formatString(strAssets, StringFormat.FORMATE_2));
        }
        else {
            holder.tv_money.setTextColor(mConext.getResources().getColor(R.color.red));
            holder.tv_money.setText("0.00");
        }
        // 累计盈亏
        if (!StringHelper.isBlank(strProfit)) {
            if (strProfit.startsWith("-")) {
                holder.tv_total.setTextColor(mConext.getResources().getColor(R.drawable.green));
                holder.tv_total.setText(""
                        + StringHelper.formatString(strProfit, StringFormat.FORMATE_2));
            } else {
                holder.tv_total.setTextColor(mConext.getResources().getColor(R.color.red));
                if (Float.valueOf(strProfit) == 0)
                    holder.tv_total.setText(StringHelper.formatString(strProfit,
                            StringFormat.FORMATE_2));
                else
                    holder.tv_total
                            .setText("+"
                                    + StringHelper.formatString(strProfit,
                                            StringFormat.FORMATE_2));
            }
        }
        else {
            holder.tv_total.setTextColor(mConext.getResources().getColor(R.color.red));
            holder.tv_total.setText("0.00");
        }
        // 申购中金额
        if (null != strSubs && Double.parseDouble(strSubs) != 0) {
            holder.tv_subway.setText("申购中："
                    + StringHelper.formatString(strSubs, StringFormat.FORMATE_2) + "元");
        } else {
            holder.tv_subway.setText("");
        }

        if (!StringHelper.isBlank(listItemData.get(position).get("isbalance"))) {
            if (Integer.parseInt(listItemData.get(position).get("isbalance")) == 1) {
                int balanceState = Integer.parseInt(listItemData.get(position).get("balanceopt"));
                if (balanceState == 0) {
                    holder.balance.setVisibility(View.INVISIBLE);
                } else if (balanceState == 1) {
                    holder.balance.setVisibility(View.VISIBLE);
                    holder.balance.setBackgroundResource(R.drawable.balance_red);
                } else if (balanceState == 2) {
                    holder.balance.setVisibility(View.VISIBLE);
                    holder.balance.setBackgroundResource(R.drawable.balance_blue);
                }
            } else {
                holder.balance.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.balance.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView img;
        ImageView balance;
        TextView tv_name;
        TextView tv_money;
        TextView tv_total;
        TextView tv_moneyb;
        TextView tv_totalb;
        TextView tv_subway;
    }
}
