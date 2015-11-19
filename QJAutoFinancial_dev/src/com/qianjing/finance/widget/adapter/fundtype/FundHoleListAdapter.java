package com.qianjing.finance.widget.adapter.fundtype;

import com.qianjing.finance.model.fund.MyFundHoldingDetails;
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

/**********************************************************
 * @文件名称：FundHoleListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月11日 下午2:21:33
 * @文件描述：基金持仓列表适配器
 * @修改历史：2015年6月11日创建初始版本
 **********************************************************/
public class FundHoleListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<MyFundHoldingDetails> mListData;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public FundHoleListAdapter(Context context, ArrayList<MyFundHoldingDetails> listData)
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
			convertView = inflater.inflate(R.layout.item_fund_hold_list_asset, null);
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
		MyFundHoldingDetails detail = (MyFundHoldingDetails) getItem(position);
		holder.fundNameView.setText(detail.getAbbrev() + mContext.getString(R.string.left_brackets)
				+ detail.getFdcode() + mContext.getString(R.string.right_brackets));
		holder.fundSharesView.setText(mContext.getString(R.string.fund_shares)
				+ StringHelper.formatString(detail.getShares(), StringFormat.FORMATE_1));
		holder.fundWanFenProfitView.setText(mContext.getString(R.string.fund_wanfen_profit)
				+ StringHelper.formatString(detail.getProfit10K(), StringFormat.FORMATE_3));
		holder.fundLoadView.setText(StringHelper.formatString(detail.getMarket_value(),
				StringFormat.FORMATE_1));
		if (Double.parseDouble(detail.getProfit()) >= 0)
		{
			holder.fundLeijiProfitView.setTextColor(mContext.getResources().getColor(R.color.red_VI));
			holder.fundLeijiProfitView.setText(mContext.getString(R.string.jia_hao)
					+ StringHelper.formatString(detail.getProfit(), StringFormat.FORMATE_1));
		}
		else
		{
			holder.fundLeijiProfitView.setTextColor(mContext.getResources().getColor(R.color.color_307d1b));
			holder.fundLeijiProfitView.setText(StringHelper.formatString(detail.getProfit(),
					StringFormat.FORMATE_1));
		}
		return convertView;
	}

	public void updateData(ArrayList<MyFundHoldingDetails> listData)
	{
		this.mListData = listData;
		notifyDataSetChanged();
	}

	public void addData(ArrayList<MyFundHoldingDetails> assetsList)
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