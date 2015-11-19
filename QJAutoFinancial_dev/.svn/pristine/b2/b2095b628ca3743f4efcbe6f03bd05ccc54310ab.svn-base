package com.qianjing.finance.widget.adapter.aip;

import com.qianjing.finance.model.aip.chargedetail.AIPChargeDetail;
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
 * @文件描述：盈亏列表Adapter
 * @修改历史：2015年8月12日创建初始版本
 **********************************************************/
public class AIPChargeAdapter extends BaseAdapter
{
	private Context mContext;
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private ArrayList<AIPChargeDetail> mDataList;

	public AIPChargeAdapter(Context mContext, ArrayList<AIPChargeDetail> mDataList)
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
			convertView = layoutInflater.inflate(R.layout.item_aip_charge, null);
			holder.fundNameView = (TextView) convertView.findViewById(R.id.fund_name_view);
			holder.moneyView = (TextView) convertView.findViewById(R.id.money_view);
			holder.timesView = (TextView) convertView.findViewById(R.id.charge_time_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		AIPChargeDetail chargeDetail = (AIPChargeDetail) getItem(position);
		holder.fundNameView.setText(chargeDetail.fundName);
		holder.moneyView.setText(chargeDetail.chargeMoney);
		holder.timesView.setText(chargeDetail.successTimes + "/" + chargeDetail.allTimes);
		return convertView;
	}

	private static class ViewHolder
	{
		private TextView fundNameView;
		private TextView moneyView;
		private TextView timesView;
	}
}