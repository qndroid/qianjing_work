package com.qianjing.finance.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjautofinancial.R;

public class AccountDetailAdapter extends BaseAdapter {
	Context mConext;
	private LayoutInflater layoutFlater;
	String[] strText=new String[]{"政府部门","教科文","金融","商贸","房地产","制造业","自由职业","其他","事业单位","国有企业"};
	int mflag=-1;
	public AccountDetailAdapter(Context context, int flag){
		this.mConext=context;
		this.mflag=flag;
		layoutFlater = (LayoutInflater) mConext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strText.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutFlater.inflate( R.layout.fina_myaccount_detail_item, null);
			holder = new ViewHolder();
			holder.tv_mesg=(TextView) convertView.findViewById(R.id.tv_mesg);
			holder.im_ico= (ImageView) convertView.findViewById(R.id.im_ico);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(mflag==position){
			holder.im_ico.setVisibility(View.VISIBLE);
		}else{
			holder.im_ico.setVisibility(View.INVISIBLE);
		}
		
		holder.tv_mesg.setText(strText[position]);
		
		return convertView;
	}
	public class ViewHolder {
		ImageView im_ico;
		TextView tv_mesg;
	}
}
