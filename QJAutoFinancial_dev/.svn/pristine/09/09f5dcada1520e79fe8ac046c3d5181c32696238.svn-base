package com.qianjing.finance.widget.adapter.dayprofit;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qianjing.finance.model.assemble.AssembleDayProfit;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：DayProfitAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年8月12日 上午10:27:43
 * @文件描述：盈亏列表Adapter
 * @修改历史：2015年8月12日创建初始版本
 **********************************************************/
public class DayProfitAdapter extends BaseAdapter
{
	private Context mContext;
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private ArrayList<AssembleDayProfit> mDataList;

	public DayProfitAdapter(Context mContext, ArrayList<AssembleDayProfit> mDataList)
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
			convertView = layoutInflater.inflate(R.layout.item_day_profit, null);
			holder.dayView = (TextView) convertView.findViewById(R.id.day_time_view);
			holder.moneyView = (TextView) convertView.findViewById(R.id.money_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		AssembleDayProfit dayProfit = (AssembleDayProfit) getItem(position);
		holder.dayView.setText(DateFormatHelper.formatDate(String.valueOf(dayProfit.getDt()).concat("000"),
				DateFormat.DATE_7));
		if (Double.parseDouble(dayProfit.getDayProfit()) >= 0)
		{
			holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
			holder.moneyView.setText(mContext.getString(R.string.jia_hao) + dayProfit.getDayProfit());
		}
		else
		{
			holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.color_66b48e));
			holder.moneyView.setText(dayProfit.getDayProfit());
		}
		return convertView;
	}

	public void addNewData(ArrayList<AssembleDayProfit> mAssemleList)
	{
		this.mDataList.addAll(mAssemleList);
		notifyDataSetChanged();
	}

	public void refreshAllData(ArrayList<AssembleDayProfit> mAssemleList)
	{
		this.mDataList = mAssemleList;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView dayView;
		private TextView moneyView;
	}

}