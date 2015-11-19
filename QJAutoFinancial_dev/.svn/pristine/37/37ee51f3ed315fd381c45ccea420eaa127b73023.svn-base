package com.qianjing.finance.widget.adapter.timedespority;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qianjing.finance.model.timedespority.TimeProfitItem;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：DayProfitAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年8月12日 上午10:27:43
 * @文件描述：盈亏列表Adapter
 * @修改历史：2015年8月12日创建初始版本
 **********************************************************/
public class TimeProfitAdapter extends BaseAdapter
{
	private Context mContext;
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private ArrayList<TimeProfitItem> mDataList;

	public TimeProfitAdapter(Context mContext, ArrayList<TimeProfitItem> mDataList)
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
			convertView = layoutInflater.inflate(R.layout.item_time_profit, null);
			holder.productNameView = (TextView) convertView.findViewById(R.id.productname_view);
			holder.lejiProfitView = (TextView) convertView.findViewById(R.id.leiji_profit_view);
			holder.daishouProfitView = (TextView) convertView.findViewById(R.id.daishou_profit_view);
			holder.repayInfoView = (TextView) convertView.findViewById(R.id.repay_info_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		TimeProfitItem detail = (TimeProfitItem) getItem(position);
		holder.productNameView.setText(detail.dealName);
		holder.lejiProfitView
				.setText(StringHelper.formatString(detail.sumProfit, StringFormat.FORMATE_1));
		holder.daishouProfitView.setText(StringHelper.formatString(detail.dsProfit,
				StringFormat.FORMATE_1));
		holder.repayInfoView.setText(mContext.getString(R.string.sum_load_title)
				+ StringHelper.formatString(detail.sumLoad, StringFormat.FORMATE_1)
				+ mContext.getString(R.string.sum_load_middle)
				+ DateFormatHelper.formatDate(detail.repayTime.concat("000"), DateFormat.DATE_5)
				+ mContext.getString(R.string.sum_load_end));

		return convertView;
	}

	public void refreshAllData(ArrayList<TimeProfitItem> mAssemleList)
	{
		this.mDataList = mAssemleList;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView productNameView;
		private TextView lejiProfitView;
		private TextView daishouProfitView;
		private TextView repayInfoView;
	}
}