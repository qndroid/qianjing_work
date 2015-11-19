package com.qianjing.finance.widget.adapter.aip;

import com.qianjing.finance.model.aip.chargedetail.AIPFundChargeDetail;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 定投扣款详情页面
 * @author majinxin
 * @date 2015年8月31日
 */

public class AIPChargeDetailAdapter extends BaseAdapter
{
	private Context mContext;
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private ArrayList<AIPFundChargeDetail> mDataList;

	public AIPChargeDetailAdapter(Context mContext, ArrayList<AIPFundChargeDetail> mDataList)
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
			convertView = layoutInflater.inflate(R.layout.item_aip_charge_detail, null);
			holder.timeView = (TextView) convertView.findViewById(R.id.time_view);
			holder.moneyView = (TextView) convertView.findViewById(R.id.money_view);
			holder.statueView = (TextView) convertView.findViewById(R.id.statue_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		AIPFundChargeDetail detail = (AIPFundChargeDetail) getItem(position);
		holder.timeView.setText(DateFormatHelper.formatDate(detail.optDate.concat("000"), DateFormat.DATE_5));
		holder.moneyView.setText(detail.sum);
		// switch (detail.state)
		// {
		// case "1":
		// holder.statueView.setText(getSpannableString(mContext.getString(R.string.kou_kuang_cheng_gong)
		// + mContext.getString(R.string.aip_wait_statue)));
		// break;
		// case "3":
		// holder.statueView.setText(getSpannableString(mContext.getString(R.string.kou_kuang_cheng_gong)
		// + mContext.getString(R.string.left_brackets)
		// + StringHelper.formatString(detail.shares, StringFormat.FORMATE_2)
		// + mContext.getString(R.string.fen) + mContext.getString(R.string.right_brackets)));
		// break;
		// case "4":
		// holder.statueView.setText(getSpannableString(mContext.getString(R.string.aip_failed_statue)
		// + mContext.getString(R.string.aip_failed_statue_info)));
		// break;
		// }

		if (detail.state.equals("1"))
		{
			holder.statueView.setText(getSpannableString(mContext.getString(R.string.kou_kuang_cheng_gong)
					+ mContext.getString(R.string.aip_wait_statue)));
		}
		if (detail.state.equals("3"))
		{
			holder.statueView.setText(getSpannableString(mContext.getString(R.string.kou_kuang_cheng_gong)
					+ mContext.getString(R.string.left_brackets)
					+ StringHelper.formatString(detail.shares, StringFormat.FORMATE_2)
					+ mContext.getString(R.string.fen) + mContext.getString(R.string.right_brackets)));
		}
		if (detail.state.equals("4"))
		{
			holder.statueView.setText(getSpannableString(mContext.getString(R.string.aip_failed_statue)
					+ mContext.getString(R.string.aip_failed_statue_info)));
		}
		return convertView;
	}

	/**
	 * 利用SpannableString实现大小不同的文字
	 */
	private SpannableString getSpannableString(String source)
	{

		SpannableString resultString = new SpannableString(source);
		resultString.setSpan(new AbsoluteSizeSpan(12, true), 4, source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return resultString;
	}

	private static class ViewHolder
	{
		private TextView timeView;
		private TextView moneyView;
		private TextView statueView;
	}
}
