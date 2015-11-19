package com.qianjing.finance.widget.adapter;

import java.util.List;
import java.util.Map;

import com.qjautofinancial.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class FinaListAdapter extends BaseAdapter {
	Context mConext;
	private LayoutInflater layoutFlater;
	private List<Map<String, String>> list;

	public FinaListAdapter(Context context, List<Map<String, String>> list2) {
		mConext = context;
		layoutFlater = (LayoutInflater) mConext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		list = list2;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutFlater.inflate( R.layout.fina_inform_item, null);
			holder = new ViewHolder();
			holder.tv_info_title=(TextView) convertView.findViewById(R.id.tv_info_title);
			holder.tv_info_time=(TextView) convertView.findViewById(R.id.tv_info_time);
			holder.tv_info_content=(TextView) convertView.findViewById(R.id.tv_info_content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		String info_title=list.get(position).get("info_title");
		String info_time=list.get(position).get("info_time");
		String info_content=list.get(position).get("info_content");
		holder.tv_info_title.setText(info_title);
		holder.tv_info_time.setText(info_time);
		holder.tv_info_content.setText(info_content);
		
		return convertView;
	}
	public class ViewHolder {
		TextView tv_info_title;
		TextView tv_info_time;
		TextView tv_info_content;
	}

}
