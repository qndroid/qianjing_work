package com.qianjing.finance.widget.adapter.assemble;

import com.qianjing.finance.model.assemble.asset.SchemeAssetData;
import com.qianjing.finance.model.p2p.P2PSteadyDetailReponse.P2PPortDetail;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**********************************************************
 * @文件名称：FundHoleListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月11日 下午2:21:33
 * @文件描述：基金持仓列表适配器
 * @修改历史：2015年6月11日创建初始版本
 **********************************************************/
public class AssembleHoleListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<Serializable> mListData;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public AssembleHoleListAdapter(Context context, ArrayList<Serializable> listData)
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
			holder.nameView = (TextView) convertView.findViewById(R.id.product_name);
			holder.buyingView = (TextView) convertView.findViewById(R.id.buying_view);
			holder.marketValueView = (TextView) convertView.findViewById(R.id.total_laod_value);
			holder.leiJiProfitView = (TextView) convertView.findViewById(R.id.total_profit_value);
			holder.balanceStateView = (TextView) convertView.findViewById(R.id.balance_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据数据初始化item
		if (getItem(position) instanceof SchemeAssetData)
		{
			SchemeAssetData detail = (SchemeAssetData) getItem(position);
			holder.nameView.setText(detail.name);
			holder.buyingView.setText(mContext.getString(R.string.fund_buying)
					+ StringHelper.formatString(detail.subscripting, StringFormat.FORMATE_1));
			holder.marketValueView.setText(StringHelper.formatString(detail.assets, StringFormat.FORMATE_1));
			if (Double.parseDouble(detail.profit) >= 0)
			{
				holder.leiJiProfitView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
				if (Double.parseDouble(detail.profit) > 0)
				{
					holder.leiJiProfitView.setText(mContext.getString(R.string.jia_hao)
							+ StringHelper.formatString(detail.profit, StringFormat.FORMATE_1));
				}
				else
				{
					holder.leiJiProfitView.setText(StringHelper.formatString(detail.profit, StringFormat.FORMATE_1));
				}
			}
			else
			{
				holder.leiJiProfitView.setTextColor(mContext.getResources().getColor(R.color.color_307d1b));
				holder.leiJiProfitView.setText(StringHelper.formatString(detail.profit, StringFormat.FORMATE_1));
			}
			// 平衡中，始终显示
			if (detail.balanceOperationState.equals("2"))
			{
				holder.balanceStateView.setBackgroundResource(R.drawable.balance_blue);
			}
			else
			{
				if (detail.balanceOperationState.equals("1"))
				{
					// 可操作，未打叉，显示
					if (detail.balanceState.equals("1"))
					{
						holder.balanceStateView.setBackgroundResource(R.drawable.balance_red);
					}
					else
					{ // 可操作，打岔，不显示
						holder.balanceStateView.setBackgroundResource(R.color.trans);
					}
				}
				// 无操作，都不显示
				else
				{
					holder.balanceStateView.setBackgroundResource(R.color.trans);
				}
			}
		}
		else
		{
			P2PPortDetail detail = (P2PPortDetail) getItem(position);
			holder.nameView.setText(detail.name);
			holder.marketValueView.setText(StringHelper.formatString(String.valueOf(detail.assets),
					StringFormat.FORMATE_1));
			if (detail.profit >= 0)
			{
				holder.leiJiProfitView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
				if (detail.profit > 0)
				{
					holder.leiJiProfitView.setText(mContext.getString(R.string.jia_hao)
							+ StringHelper.formatString(String.valueOf(detail.profit), StringFormat.FORMATE_1));
				}
				else
				{
					holder.leiJiProfitView.setText(StringHelper.formatString(String.valueOf(detail.profit),
							StringFormat.FORMATE_1));
				}
			}
			else
			{
				holder.leiJiProfitView.setTextColor(mContext.getResources().getColor(R.color.color_307d1b));
				holder.leiJiProfitView.setText(StringHelper.formatString(String.valueOf(detail.profit),
						StringFormat.FORMATE_1));
			}
		}
		return convertView;
	}

	public void updateData(ArrayList<Serializable> listData)
	{
		this.mListData = listData;
		notifyDataSetChanged();
	}

	public void addData(ArrayList<Serializable> assetsList)
	{
		this.mListData.addAll(assetsList);
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView nameView;
		private TextView balanceStateView;
		private TextView buyingView;
		private TextView marketValueView;
		private TextView leiJiProfitView;
	}
}