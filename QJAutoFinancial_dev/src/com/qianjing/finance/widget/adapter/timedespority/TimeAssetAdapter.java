package com.qianjing.finance.widget.adapter.timedespority;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qianjing.finance.model.timedespority.TimeAsset;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

/** 
* @description 资产明细Adapter
* @author majinxin
* @date 2015年9月7日
*/
public class TimeAssetAdapter extends BaseAdapter
{

	private Context mContext;
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private ArrayList<TimeAsset> mDataList;

	public TimeAssetAdapter(Context mContext, ArrayList<TimeAsset> mDataList)
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
			convertView = layoutInflater.inflate(R.layout.item_time_deposity_asset, null);
			holder.productNameView = (TextView) convertView.findViewById(R.id.product_name);
			holder.hkTimeView = (TextView) convertView.findViewById(R.id.hk_time);
			holder.sumLoadView = (TextView) convertView.findViewById(R.id.total_laod_value);
			holder.lejiProfitView = (TextView) convertView.findViewById(R.id.total_profit_value);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		TimeAsset detail = (TimeAsset) getItem(position);
		holder.productNameView.setText(detail.dealName);
		holder.sumLoadView.setText(StringHelper.formatString(detail.load, StringFormat.FORMATE_1));
		holder.hkTimeView.setText(mContext.getString(R.string.time_disport_hktime)
				+ DateFormatHelper.formatDate(detail.hkTime.concat("000"), DateFormat.DATE_1));
		holder.lejiProfitView.setText(mContext.getString(R.string.jia_hao)
				+ StringHelper.formatString(detail.sumProfit, StringFormat.FORMATE_1));

		return convertView;
	}

	public void refreshAllData(ArrayList<TimeAsset> mAssemleList)
	{
		this.mDataList = mAssemleList;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView productNameView;
		private TextView hkTimeView;
		private TextView sumLoadView;
		private TextView lejiProfitView;
	}
}