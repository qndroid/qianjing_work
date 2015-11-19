package com.qianjing.finance.widget.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.qjautofinancial.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Title: TradeListAdapter
 * Description: 交易记录列表适配
 * @author zhangqi
 * @date 2014-7-10
 */
public class TradeListAdapter extends BaseAdapter{
	
	Context mConext;
	private LayoutInflater layoutFlater;
	private List<HashMap<String, String>> list;
	
	public TradeListAdapter(Context context,List<HashMap<String, String>> mList){
		mConext = context;
		layoutFlater = (LayoutInflater) mConext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		list = mList;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = layoutFlater.inflate(R.layout.myasset_trade_list_item, null);
			holder = new ViewHolder();
			holder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);
			holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
			holder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
			holder.tv_value = (TextView)convertView.findViewById(R.id.tv_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_type.setText(list.get(position).get("operate"));
		holder.tv_date.setText(list.get(position).get("date"));
		holder.tv_status.setText(list.get(position).get("state"));
		holder.tv_value.setText(list.get(position).get("value")+"元");
		
		return convertView;
	}
	
	public class ViewHolder{
		TextView tv_type;
		TextView tv_date;
		TextView tv_status;
		TextView tv_value;
	}
	
	public void addListData(List<HashMap<String, String>> lists){
		list.addAll(lists);
		notifyDataSetChanged();
	}
	
	public void replaceAllData(List<HashMap<String, String>> lists){
		this.list = lists;
		notifyDataSetChanged();
	}
	
}
