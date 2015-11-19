package com.qianjing.finance.widget.adapter.virtual;

import com.qianjing.finance.model.virtual.SchemaAssetsBean;
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

/** 
* @description 虚拟资产适配器
* @author majinxin
* @date 2015年9月23日
*/
public class VirtualListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<SchemaAssetsBean> mListData;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public VirtualListAdapter(Context context, ArrayList<SchemaAssetsBean> listData)
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
			convertView = inflater.inflate(R.layout.item_assemble_hold_asset, null);
			holder.fundNameView = (TextView) convertView.findViewById(R.id.product_name);
			holder.fundSharesView = (TextView) convertView.findViewById(R.id.shares_view);
			holder.fundWanFenProfitView = (TextView) convertView.findViewById(R.id.wanfen_profit_view);
			holder.fundLoadView = (TextView) convertView.findViewById(R.id.total_laod_value);
			holder.fundLeijiProfitView = (TextView) convertView.findViewById(R.id.total_profit_value);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据数据初始化item
		SchemaAssetsBean detail = (SchemaAssetsBean) getItem(position);

		holder.fundNameView.setText(detail.schema.name);
		// holder.fundSharesView.setText(mContext.getString(R.string.fund_shares)
		// + StringHelper.formatString(detail.getShares(), StringFormat.FORMATE_1));
		// holder.fundWanFenProfitView.setText(mContext.getString(R.string.fund_wanfen_profit)
		// + StringHelper.formatString(detail.getProfit10K(), StringFormat.FORMATE_3));
		holder.fundLoadView.setText(StringHelper.formatString(String.valueOf(detail.assets.assets),
				StringFormat.FORMATE_1));
		if (detail.assets.profit >= 0)
		{
			holder.fundLeijiProfitView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
			holder.fundLeijiProfitView.setText(mContext.getString(R.string.jia_hao)
					+ StringHelper.formatString(String.valueOf(detail.assets.profit), StringFormat.FORMATE_1));
		}
		else
		{
			holder.fundLeijiProfitView.setTextColor(mContext.getResources().getColor(R.color.color_307d1b));
			holder.fundLeijiProfitView.setText(StringHelper.formatString(String.valueOf(detail.assets.profit),
					StringFormat.FORMATE_1));
		}
		return convertView;
	}

	public void updateData(ArrayList<SchemaAssetsBean> listData)
	{
		this.mListData = listData;
		notifyDataSetChanged();
	}

	public void addData(ArrayList<SchemaAssetsBean> assetsList)
	{
		this.mListData.addAll(assetsList);
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView fundNameView;
		private TextView fundSharesView;
		private TextView fundWanFenProfitView;
		private TextView fundLoadView;
		private TextView fundLeijiProfitView;
	}
}
