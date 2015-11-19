
package com.qianjing.finance.widget.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjing.finance.util.CommonCallBack;
import com.qianjing.finance.util.CommonUtil;
import com.qjautofinancial.R;

/**
 * @description 银行卡列表适配器
 * @author fangyan
 * @date 2015年9月11日
 */
public class CardListAdapter extends BaseAdapter {

	private final ArrayList<HashMap<String, Object>> LIST_BANK;
	private final ArrayList<HashMap<String, Object>> LIST_P2P_BANK;
	private CommonCallBack callback;
	private ArrayList<HashMap<String, String>> mListData;
	private LayoutInflater mInflater;

	public CardListAdapter(Context context, ArrayList<HashMap<String, String>> listData, CommonCallBack callback) {
		LIST_BANK = CommonUtil.getListBank();
		LIST_P2P_BANK = CommonUtil.getListP2PBank();
		this.callback = callback;
		this.mListData = listData;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ItemViewHolder holder;
		if (convertView != null) {
			holder = (ItemViewHolder) convertView.getTag();
		} else {
			holder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.item_card, null);
			holder.ivBankIcon = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.tvUnbound = (TextView) convertView.findViewById(R.id.tv_unbound);
			holder.tvBankName = (TextView) convertView.findViewById(R.id.tv_bank_name2);
			holder.tvCardType = (TextView) convertView.findViewById(R.id.tv_card_type);
			holder.tvCardNum = (TextView) convertView.findViewById(R.id.tv_card_num);
			holder.tvMasterName = (TextView) convertView.findViewById(R.id.tv_master_name);

			HashMap<String, String> map = mListData.get(position);
			holder.tvUnbound.setOnClickListener(new UnboundListener(position));
			if (TextUtils.equals(map.get(CardConst.KEY_CARD_TYPE), CardConst.TYPE_STEADY)) {
				holder.tvUnbound.setVisibility(View.INVISIBLE);
				// P2P银行卡匹配设置银行图标
				for (HashMap<String, Object> mapBank : LIST_P2P_BANK) {
					String bankCode = (String) mapBank.get(CommonUtil.KEY_BANK_CODE);
					if (TextUtils.equals(map.get(CardConst.KEY_BANK_CODE), bankCode))
						holder.ivBankIcon.setImageResource((Integer) mapBank.get(CommonUtil.KEY_BANK_ICON));
				}
			} else {
				// 基金银行卡匹配设置银行图标
				for (HashMap<String, Object> mapBank : LIST_BANK) {
					String bankName = (String) mapBank.get(CommonUtil.KEY_BANK_NAME);
					if (TextUtils.equals(map.get(CardConst.KEY_BANK_NAME), bankName))
						holder.ivBankIcon.setImageResource((Integer) mapBank.get(CommonUtil.KEY_BANK_ICON));
				}
			}
			holder.tvBankName.setText(map.get(CardConst.KEY_BANK_NAME));
			holder.tvCardType.setText(map.get(CardConst.KEY_CARD_TYPE));
			holder.tvCardNum.setText(map.get(CardConst.KEY_CARD_NUM));
			holder.tvMasterName.setText(map.get(CardConst.KEY_MASTER_NAME));
			convertView.setTag(holder);
		}
		return convertView;
	}

	class UnboundListener implements OnClickListener {
		private int position;

		UnboundListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (callback != null)
				callback.back(position);
		}
	}

	private class ItemViewHolder {
		ImageView ivBankIcon;
		TextView tvUnbound;
		TextView tvBankName;
		TextView tvCardType;
		TextView tvCardNum;
		TextView tvMasterName;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	public static class CardConst {
		public static final String KEY_BANK_NAME = "bankName";
		public static final String KEY_BANK_CODE = "bankCode";
		public static final String KEY_CARD_TYPE = "cardType";
		public static final String KEY_CARD_NUM = "cardNum";
		public static final String KEY_MASTER_NAME = "masterName";

		public static final String TYPE_STEADY = "保本保息组合专用";
		public static final String TYPE_FUND = "基金专用";
	}

};
