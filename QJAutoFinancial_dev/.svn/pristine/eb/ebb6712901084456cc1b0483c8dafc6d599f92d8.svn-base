package com.qianjing.finance.ui.activity.history.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.util.QJColor;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.widget.ViewHolder;
import com.qjautofinancial.R;

public class SteadyHistoryAdapter extends BaseAdapter {

	
	private ArrayList<HashMap<String, Object>> item1List;
	private Context context;
	public static final int COMMON_ITEM = 0;
	public static final int RUNNING_TITLE = 1;
	public static final int COMPETED_TITLE = 2;
	
	
	
	public SteadyHistoryAdapter(Context context,
			ArrayList<HashMap<String, Object>> item1List) {
		super();
		this.context = context;
		this.item1List = item1List;
	}
	
	@Override
	public int getCount() {
		return item1List.size();
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	    
		if(convertView==null || !(convertView instanceof RelativeLayout)){
			convertView = View.inflate(context, R.layout.history_item, null);
		}
		
		TextView name = ViewHolder.get(convertView,R.id.tv_history_item_name);
		TextView time = ViewHolder.get(convertView, R.id.tv_time);
		TextView value = ViewHolder.get(convertView, R.id.tv_operation_value);
		TextView state = ViewHolder.get(convertView, R.id.tv_state);
		
		
		HashMap<String, Object> hashMap = item1List.get(position);
		    
		int status = Integer.parseInt((String)hashMap.get("status"));    
		
			//进行中的操作
			name.setText(getStateStr(false,Integer.parseInt((String)hashMap.get("status"))));
			time.setText(DateFormatHelper.formatDate((String)hashMap.get("createTime")+"000", DateFormatHelper.DateFormat.DATE_5));
			state.setText(getStateStr(true,status));
			float num = Float.parseFloat((String)hashMap.get("money"));
			if(status==2){
			    value.setTextColor(QJColor.PROFIT_RED.getColor());
			    value.setText("+"+formatString(num+""));
			}else{
			    value.setTextColor(QJColor.PROFIT_GREEN.getColor());
                value.setText("-"+formatString(num+""));
			}
        
		return convertView;
	}
	
	public String getStateStr(boolean type,int status){
	    if(type){
	        switch(status){
	            case 0:
	                return "处理中";
	            case 1:
	                return "取现成功";
	            case 2:
	                return "存入成功";
	        }
	    }else{
	        switch(status){
                case 0:
                case 1:
                    return "取现";
                case 2:
                    return "存入";
            }
	    }
	    
	    return "";
	}
	
	
	
	public static String formatString(String source) {
        DecimalFormat df = new DecimalFormat("##0.00");
        return df.format(new BigDecimal(source));
    }
}
