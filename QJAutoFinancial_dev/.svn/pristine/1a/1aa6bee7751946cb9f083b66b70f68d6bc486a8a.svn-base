package com.qianjing.finance.view.virtual;

import com.qianjing.finance.util.Util;
import com.qianjing.finance.widget.ViewHolder;
import com.qianjing.finance.widget.adapter.base.BaseCustomAdapter;
import com.qjautofinancial.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class CustomDetailsView extends RelativeLayout {

	private Context context;
	private ListView itemList;
	private TextView item1;
	private TextView item2;
	private TextView item3;
	private List<SparseArray<Serializable>> detailData;
	
	public CustomDetailsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView(context);
	}

	
	public CustomDetailsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
		
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CustomDetails);  
		String item1Str = ta.getString(R.styleable.CustomDetails_one_item);
		String item2Str = ta.getString(R.styleable.CustomDetails_two_item);
		String item3Str = ta.getString(R.styleable.CustomDetails_three_item);
		
		item1.setText(item1Str);
		item2.setText(item2Str);
		item3.setText(item3Str);
		
	}
	
	public void setOneItem(String str){
		item1.setText(str);
	}
	
	public void setTwoItem(String str){
		item2.setText(str);
	}

	public void setThreeItem(String str){
		item3.setText(str);
	}
	
	public CustomDetailsView(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}
	/**
	 * 设置适配器
	 * */
	public void setCustomAdapter(final BaseCustomAdapter baseCustomAdapter){
		
		detailData = baseCustomAdapter.getDetailData();
		itemList.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				
				if(convertView==null){
					convertView = View.inflate(context, R.layout.three_weight_item, null);
				}
				
				TextView oneItem1 = ViewHolder.get(convertView, R.id.tv_handle);
				TextView oneItem2 = ViewHolder.get(convertView, R.id.tv_abbrev);
				TextView oneItem3 = ViewHolder.get(convertView, R.id.tv_shares);
				
				oneItem1.setText(detailData.get(position).get(1)+"");
				oneItem2.setText(detailData.get(position).get(2)+"");
				oneItem3.setText(detailData.get(position).get(3)+"");
				
				
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						baseCustomAdapter.setCustomItemClick(v, position);
					}
				});
				
				
				
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return null;
			}
			
			@Override
			public int getCount() {
				return detailData.size();
			}
		});
		
		
		/**
		 * 对listView指定高,解决ScrollView里显示bug
		 * */
		Util.setListViewHeightBasedOnChildren(itemList);
	};
	
	
	private void initView(Context context) {
		
		View view = View.inflate(context, R.layout.custom_details_view, this);
		item1 = (TextView) view.findViewById(R.id.tv_item1);
		item2 = (TextView) view.findViewById(R.id.tv_item2);
		item3 = (TextView) view.findViewById(R.id.tv_item3);
		itemList = (ListView) view.findViewById(R.id.lv_item_list);
	}
	
}
