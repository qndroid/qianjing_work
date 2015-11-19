package com.qianjing.finance.widget.adapter.fundtype;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：CardListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月10日 下午4:39:01
 * @文件描述：银行卡列表适配器
 * @修改历史：2015年6月10日创建初始版本
 **********************************************************/
public class CardListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<Card> mListData;
	private Card selectedCard;
	private ViewHolder holder;
	private LayoutInflater inflater;
	private TypedArray bankImage;

	public CardListAdapter(Context context, ArrayList<Card> listData, Card selectedCard)
	{
		this.mContext = context;
		this.mListData = listData;
		this.selectedCard = selectedCard;
		inflater = LayoutInflater.from(mContext);
		bankImage = context.getResources().obtainTypedArray(R.array.bank_image);
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
			convertView = inflater.inflate(R.layout.card_list_item_layout, null);
			holder.bankIcon = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.bankName = (TextView) convertView.findViewById(R.id.tv_bankname);
			holder.cardNumber = (TextView) convertView.findViewById(R.id.tv_bankcard);
			holder.selectedView = (ImageView) convertView.findViewById(R.id.selected_flag_view);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据数据初始化item
		Card card = (Card) getItem(position);
		holder.bankIcon.setImageResource(bankImage.getResourceId(getBankImage(card.getBankName()), -1));
		holder.bankName.setText(card.getBankName());
		holder.cardNumber.setText(StringHelper.hintCardNum(card.getNumber()));
		if (card.getNumber().equals(selectedCard.getNumber()))
		{
			holder.selectedView.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.selectedView.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 获取银行卡icon
	 * @param bankName
	 * @return
	 */
	private int getBankImage(String bankName)
	{
		String[] mBankNameArray = mContext.getResources().getStringArray(R.array.bank_name);
		for (int i = 0; i < mBankNameArray.length; i++)
		{
			if (mBankNameArray[i].equals(bankName))
			{
				return i;
			}
		}
		return mBankNameArray.length;
	}

	private static class ViewHolder
	{
		private ImageView selectedView;
		private ImageView bankIcon;
		private TextView bankName;
		private TextView cardNumber;
	}
}
