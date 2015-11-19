
package com.qianjing.finance.ui.activity.history.adapter;

import com.qianjing.finance.model.history.Schemaoplogs;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.QJColor;
import com.qianjing.finance.util.StateUtils;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.widget.ViewHolder;
import com.qjautofinancial.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AssemableHistoryAdapter extends BaseAdapter {

    private ArrayList<Schemaoplogs> item1List;
    private ArrayList<Schemaoplogs> item2List;
    private Context context;
    public static final int COMMON_ITEM = 0;
    public static final int RUNNING_TITLE = 1;
    public static final int COMPETED_TITLE = 2;

    public AssemableHistoryAdapter(Context context,
            ArrayList<Schemaoplogs> item1List, ArrayList<Schemaoplogs> item2List) {
        super();
        this.context = context;
        this.item1List = item1List;
        this.item2List = item2List;
    }

    @Override
    public int getCount() {

        if (item1List.size() == 0 && item2List.size() == 0) {
            return 0;
        }

        if (item1List.size() == 0) {
            return item2List.size() + 1;
        }

        if (item2List.size() == 0) {
            return item1List.size() + 1;
        }

        return item1List.size() + item2List.size() + 2;
    }

    public int getItemType(int position) {

        if (item1List.size() == 0 && item2List.size() == 0) {
            return COMMON_ITEM;
        }
        if (item1List.size() == 0) {
            if (position == 0) {
                return COMPETED_TITLE;
            } else {
                return COMMON_ITEM;
            }
        }

        if (item2List.size() == 0) {
            if (position == 0) {
                return RUNNING_TITLE;
            } else {
                return COMMON_ITEM;
            }
        }

        if (position == 0) {
            return RUNNING_TITLE;
        } else if (position == item1List.size() + 1) {
            return COMPETED_TITLE;
        } else {
            return COMMON_ITEM;
        }
    }

    public int getRunningTitlePosition() {

        if (item1List.size() == 0 && item2List.size() == 0) {
            return -1;
        }
        if (item1List.size() == 0) {
            return -1;
        }

        if (item2List.size() == 0) {
            return 0;
        }

        return 0;

    }

    public int getCompletedTitlePosition() {

        if (item1List.size() == 0 && item2List.size() == 0) {
            return -1;
        }
        if (item1List.size() == 0) {
            return 0;
        }

        if (item2List.size() == 0) {
            return -1;
        }

        return item1List.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (item1List.size() != 0) {
            if (position == 0) {
                View view = View.inflate(context, R.layout.history_state_title, null);
                ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                ivIcon.setBackgroundResource(R.drawable.is_running);
                tvTitle.setText("进行中");
                return view;
            }
        }

        if (item2List.size() != 0) {

            int index = item1List.size() + 1;
            if (item1List.size() == 0) {
                index = 0;
            }
            if (position == index) {
                View view = View.inflate(context, R.layout.history_state_title, null);
                ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                ivIcon.setBackgroundResource(R.drawable.be_sured);
                tvTitle.setText("已确认");
                return view;
            }
        }

        if (convertView == null || !(convertView instanceof RelativeLayout)) {
            convertView = View.inflate(context, R.layout.history_item, null);
        }

        TextView name = ViewHolder.get(convertView, R.id.tv_history_item_name);
        TextView time = ViewHolder.get(convertView, R.id.tv_time);
        TextView value = ViewHolder.get(convertView, R.id.tv_operation_value);
        TextView state = ViewHolder.get(convertView, R.id.tv_state);

        if (position > 0 && position < item1List.size() + 1) {
            int index = position - 1;
            // 进行中的操作 && 是否是再平衡
            if (item1List.get(index).operate == 3) {

                if (StringHelper.isBlankZero(item1List.get(index).marketValue)) {
                    value.setText("+0.00");
                } else {
                    value.setText("+"
                            + StringHelper.formatString(item1List.get(index).marketValue,
                                    StringFormat.FORMATE_2));
                }
                value.setTextColor(QJColor.PROFIT_RED.getColor());
                state.setText(StateUtils.getRebalanceStateStr(item1List.get(index).state));

            } else {
                if (item1List.get(index).operate == 1 && item1List.get(index).sum != 0) {
                    FormatNumberData.formatNumberPR(item1List.get(index).sum, value,
                            item1List.get(index).operate);
                } else {
                    FormatNumberData.formatNumberPR(value, item1List.get(index).operate);
                }
                state.setText(StateUtils.getAssembleStateStr(item1List.get(index).operate
                        , item1List.get(index).fdstate));
            }

            name.setText(item1List.get(index).sname);
            time.setText(DateFormatHelper.formatDate(
                    item1List.get(index).opDate.concat("000"), DateFormat.DATE_5));

            return convertView;
        }

        int index = position - item1List.size() - 2;

        if (item1List.size() == 0) {
            index = position - 1;
        }

        if (item2List.get(index).operate == 3) {
            if (StringHelper.isBlankZero(item2List.get(index).marketValue)) {
                value.setText("+0.00");
            } else {
                value.setText("+"
                        + StringHelper.formatString(item2List.get(index).marketValue,
                                StringFormat.FORMATE_2));
            }
            value.setTextColor(QJColor.PROFIT_RED.getColor());
            state.setText(StateUtils.getRebalanceStateStr(item2List.get(index).state));

        } else {
            FormatNumberData.formatNumberPR(Float.parseFloat(item2List.get(index).estimate_sum),
                    value, item2List.get(index).operate);
            state.setText(StateUtils.getAssembleStateStr(item2List.get(index).operate
                    , item2List.get(index).fdstate));
        }

        name.setText(item2List.get(index).sname);
        time.setText(DateFormatHelper.formatDate(
                item2List.get(index).opDate.concat("000"), DateFormat.DATE_5));

        return convertView;
    }

}
