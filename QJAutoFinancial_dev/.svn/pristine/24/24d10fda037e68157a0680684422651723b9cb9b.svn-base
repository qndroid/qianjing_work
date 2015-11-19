package com.qianjing.finance.widget.adapter.redbag;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：RewardAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年7月13日 下午4:38:53
 * @文件描述：红包活动Adpater
 * @修改历史：2015年7月13日创建初始版本
 **********************************************************/
public class RewardAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater inflater;
	private ViewHolder holder;
	/**
	 * data
	 */
	private RedBag currentBag;
	private ArrayList<RedBag> listData;
	private float buyTotalMoney;

	public RewardAdapter(Context mContext, ArrayList<RedBag> listData, RedBag currentBag, String buyAccount)
	{
		this.buyTotalMoney = Float.valueOf(buyAccount);
		this.mContext = mContext;
		this.listData = listData;
		this.currentBag = currentBag;
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return listData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return listData.get(position);
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
			convertView = inflater.inflate(R.layout.item_redbag_layout, null);
			holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.content_layout);
			holder.redBagLayout = (RelativeLayout) convertView.findViewById(R.id.redbag_layout);
			holder.selectedView = (ImageView) convertView.findViewById(R.id.selected_view);
			holder.redBagValueView = (TextView) convertView.findViewById(R.id.redbag_value);
			holder.redBagPriceView = (TextView) convertView.findViewById(R.id.price_view);
			holder.redBagLimitView = (TextView) convertView.findViewById(R.id.redbag_limit);
			holder.redBagTimeView = (TextView) convertView.findViewById(R.id.redbag_time);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		RedBag redBag = (RedBag) getItem(position);

		/**
		 * 表示当前金额下可用的代金券
		 */
		if (buyTotalMoney >= redBag.limitSum)
		{
			holder.selectedView.setVisibility(View.VISIBLE);
			if (currentBag != null && redBag.id == currentBag.id)
			{
				holder.selectedView.setBackgroundResource(R.drawable.icon_redbag_selected);
			}
			else
			{
				holder.selectedView.setBackgroundResource(R.drawable.icon_redbag_select_no);
			}
			holder.contentLayout.setBackgroundResource(R.color.white);
			holder.redBagLayout.setBackgroundResource(R.drawable.bg_redbag);
			holder.redBagLimitView.setTextColor(mContext.getResources().getColor(R.color.color_333333));
			holder.redBagTimeView.setTextColor(mContext.getResources().getColor(R.color.color_999999));
			holder.redBagValueView.setTextColor(mContext.getResources().getColor(R.color.color_fffa89));
			holder.redBagPriceView.setTextColor(mContext.getResources().getColor(R.color.color_fffa89));
		}
		else
		{
			holder.selectedView.setVisibility(View.INVISIBLE);
			holder.contentLayout.setBackgroundResource(R.color.color_dadada);
			holder.redBagLayout.setBackgroundResource(R.drawable.bg_redbag_unable);
			holder.redBagLimitView.setTextColor(mContext.getResources().getColor(R.color.color_969696));
			holder.redBagTimeView.setTextColor(mContext.getResources().getColor(R.color.color_b6b6b6));
			holder.redBagValueView.setTextColor(mContext.getResources().getColor(R.color.color_e2e2e2));
			holder.redBagPriceView.setTextColor(mContext.getResources().getColor(R.color.color_e2e2e2));
		}

		holder.redBagValueView.setText(String.valueOf(redBag.val));
		holder.redBagLimitView.setText(mContext.getString(R.string.enough_title) + (long) redBag.limitSum
				+ mContext.getString(R.string.enough_end));
		holder.redBagTimeView.setText(mContext.getString(R.string.redbag_vaildate_time)
				+ DateFormatHelper.formatDate(String.valueOf(redBag.endTime).concat("000"),
						DateFormat.DATE_10));
		return convertView;
	}

	public void updateRedBag(RedBag redBag)
	{
		this.currentBag = redBag;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private RelativeLayout contentLayout;
		private RelativeLayout redBagLayout;
		private ImageView selectedView;
		private TextView redBagValueView;
		private TextView redBagPriceView;
		private TextView redBagLimitView;
		private TextView redBagTimeView;
	}
}