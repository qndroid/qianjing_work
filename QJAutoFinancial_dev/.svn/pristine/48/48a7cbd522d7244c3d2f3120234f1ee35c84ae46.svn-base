
package com.qianjing.finance.ui.activity.profit.adapter;

import com.qianjing.finance.model.history.ProfitBean;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 盈亏列表通用 Adapter
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class ProfitAdapter extends BaseAdapter {
    private Context mContext;
    private ViewHolder holderTitle;
    private ViewHolder holderContent;
    private ViewHolder holderDayProfit;
    private LayoutInflater inflater;
    private ArrayList<ProfitBean> lists;
    private ProfitBean bean;
    private boolean isWallet;

    public ProfitAdapter(Context mContext, ArrayList<ProfitBean> lists,
            boolean isWallet) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.lists = lists;
        this.isWallet = isWallet;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        bean = (ProfitBean) getItem(position);

        if (convertView == null) {
            switch (type) {
                case 0:
                    holderTitle = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_profit_time,
                            parent, false);
                    holderTitle.timeView = (TextView) convertView
                            .findViewById(R.id.time_view);
                    convertView.setTag(holderTitle);
                    break;
                case 1:
                    holderContent = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_profit_detail,
                            parent, false);
                    holderContent.nameView = (TextView) convertView
                            .findViewById(R.id.name_view);
                    holderContent.moneyView = (TextView) convertView
                            .findViewById(R.id.money_view);
                    convertView.setTag(holderContent);
                    break;
                case 2:
                    holderDayProfit = new ViewHolder();
                    convertView = inflater.inflate(
                            R.layout.item_profit_daytotal, parent, false);
                    holderDayProfit.dayTotalView = (TextView) convertView
                            .findViewById(R.id.day_total_view);
                    holderDayProfit.totalView = (TextView) convertView
                            .findViewById(R.id.total_view);
                    convertView.setTag(holderDayProfit);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    holderTitle = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    holderContent = (ViewHolder) convertView.getTag();
                    break;
                case 2:
                    holderDayProfit = (ViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case 0:
                holderTitle.timeView.setText(DateFormatHelper
                        .formatDate(
                                String.valueOf(bean.getTime())
                                        .concat("000"), DateFormat.DATE_5));
                break;
            case 1:
                holderContent.nameView.setText(bean.getName());
                holderContent.moneyView.setText(Double.parseDouble(bean
                        .getDayProfit()) > 0
                        ? mContext.getString(R.string.jia_hao)
                                + StringHelper.formatString(bean.getDayProfit(),
                                        StringFormat.FORMATE_1)
                        : StringHelper.formatString(bean.getDayProfit(),
                                StringFormat.FORMATE_1));
                break;
            case 2:
                holderDayProfit.totalView.setText(isWallet ? mContext
                        .getString(R.string.huo_qi_bao) : mContext
                        .getString(R.string.day_total_profit));
                if (bean.getDayTotalProfit() >= 0) {
                    holderDayProfit.dayTotalView.setTextColor(mContext
                            .getResources().getColor(R.color.red_VI));
                    holderDayProfit.dayTotalView.setText(bean
                            .getDayTotalProfit() == 0 ? StringHelper
                            .formatString(String.valueOf(bean
                                    .getDayTotalProfit()), StringFormat.FORMATE_1) : mContext
                            .getString(R.string.jia_hao)
                            + StringHelper
                                    .formatString(String.valueOf(bean
                                            .getDayTotalProfit()), StringFormat.FORMATE_1));
                } else {
                    holderDayProfit.dayTotalView.setTextColor(mContext
                            .getResources().getColor(R.color.color_66b48e));
                    holderDayProfit.dayTotalView.setText(StringHelper
                            .formatString(String.valueOf(bean
                                    .getDayTotalProfit()), StringFormat.FORMATE_1));
                }
                break;
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        bean = (ProfitBean) getItem(position);
        if (bean.getType() == 0) {
            return 0;
        }
        if (bean.getType() == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    public void addData(ArrayList<ProfitBean> profitLists) {
        this.lists.addAll(profitLists);
        notifyDataSetChanged();
    }

    public void refreshAllData(ArrayList<ProfitBean> profitLists) {
        this.lists = profitLists;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView timeView;
        private TextView moneyView;
        private TextView nameView;
        private TextView dayTotalView;
        private TextView totalView;
    }
}
