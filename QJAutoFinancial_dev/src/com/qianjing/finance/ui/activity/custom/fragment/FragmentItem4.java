package com.qianjing.finance.ui.activity.custom.fragment;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.util.LogUtils;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class FragmentItem4 extends Fragment implements OnClickListener{

	private CheckBox item1;
	private CheckBox item2;
	private CheckBox item3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendConBtnEnable(false);
		LogUtils.syso("fragment4 onCreate");
	}
	
	public View onCreateView(LayoutInflater inflater
			, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.syso("fragment4 onCreateView");
		View view = View.inflate(getActivity(), R.layout.fragment_ask_invest_type, null);
		
		item1 = (CheckBox) view.findViewById(R.id.cb_page_four_item1);
		item2 = (CheckBox) view.findViewById(R.id.cb_page_four_item2);
		item3 = (CheckBox) view.findViewById(R.id.cb_page_four_item3);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		item3.setOnClickListener(this);
		
		if(UserManager.getInstance().getUser().getPreference()!=null){
			String str = UserManager.getInstance().getUser().getPreference();
			setContentView(str);
		}
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onClick(View v) {
		String preference = "";
		switch(v.getId()){
		case R.id.cb_page_four_item1:
			item1.setChecked(true);
			item2.setChecked(false);
			item3.setChecked(false);
			preference = "A";
			break;
		case R.id.cb_page_four_item2:
			item1.setChecked(false);
			item2.setChecked(true);
			item3.setChecked(false);
			preference = "B";
			break;
		case R.id.cb_page_four_item3:
			item3.setChecked(true);
			item2.setChecked(false);
			item1.setChecked(false);
			preference = "C";
			break;
		}
		sendConBtnEnable(true);
		UserManager.getInstance().getUser().setPreference(preference);
	};
	
	private void sendConBtnEnable(boolean enable){
		
		Intent intent = new Intent();
		intent.setAction("com.qjautofinancial.vip.custom");
		intent.putExtra("pageNum", 4);
		intent.putExtra("isEnable", enable);
		getActivity().sendBroadcast(intent);
		
	}
	
	public void setContentView(String str){
		if("A".equals(str)){
			item1.setChecked(true);
		}else if("B".equals(str)){
			item2.setChecked(true);
		}else if("C".equals(str)){
			item3.setChecked(true);
		}
		sendConBtnEnable(true);
	}
}
