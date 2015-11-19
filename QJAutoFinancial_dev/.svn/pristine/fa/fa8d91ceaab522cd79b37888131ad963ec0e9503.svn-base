package com.qianjing.finance.ui.activity.custom.fragment;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.util.LogUtils;
import com.qjautofinancial.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;

public class FragmentItem5 extends Fragment implements OnClickListener{

	
	private CheckBox item1;
	private CheckBox item2;
	private CheckBox item3;
	private CheckBox item4;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendConBtnEnable(false);
		LogUtils.syso("fragment5 onCreate");
	}
	
	
	@SuppressLint("ResourceAsColor")
	public View onCreateView(LayoutInflater inflater
			, ViewGroup container, Bundle savedInstanceState) {

		LogUtils.syso("fragment5 onCreateView");
		View view = View.inflate(getActivity(), R.layout.fragment_ask_lose_action, null);
		
		
		item1 = (CheckBox) view.findViewById(R.id.cb_page_five_item1);
		item2 = (CheckBox) view.findViewById(R.id.cb_page_five_item2);
		item3 = (CheckBox) view.findViewById(R.id.cb_page_five_item3);
		item4 = (CheckBox) view.findViewById(R.id.cb_page_five_item4);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		item3.setOnClickListener(this);
		item4.setOnClickListener(this);
		
		
		if(UserManager.getInstance().getUser().getRisk()!=null){
			String str = UserManager.getInstance().getUser().getRisk();
			setContentView(str);
		}
		
//		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
//		boolean isOpen = imm.isAcceptingText();
//		if(isOpen){
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		}
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	

	@Override
	public void onClick(View v) {
		String risk = "";
		switch(v.getId()){
		case R.id.cb_page_five_item1:
			item1.setChecked(true);
			item2.setChecked(false);
			item3.setChecked(false);
			item4.setChecked(false);
			risk = "A";
			break;
		case R.id.cb_page_five_item2:
			item1.setChecked(false);
			item2.setChecked(true);
			item3.setChecked(false);
			item4.setChecked(false);
			risk = "B";
			break;
		case R.id.cb_page_five_item3:
			item1.setChecked(false);
			item2.setChecked(false);
			item3.setChecked(true);
			item4.setChecked(false);
			risk = "C";
			break;
		case R.id.cb_page_five_item4:
			item1.setChecked(false);
			item2.setChecked(false);
			item3.setChecked(false);
			item4.setChecked(true);
			risk = "D";
			break;
		}
		sendConBtnEnable(true);
		UserManager.getInstance().getUser().setRisk(risk);
	};
	
	private void sendConBtnEnable(boolean enable){
		
		Intent intent = new Intent();
		intent.setAction("com.qjautofinancial.vip.custom");
		intent.putExtra("pageNum", 5);
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
		}else if("D".equals(str)){
			item4.setChecked(true);
		}
		sendConBtnEnable(true);
	}
	
}
