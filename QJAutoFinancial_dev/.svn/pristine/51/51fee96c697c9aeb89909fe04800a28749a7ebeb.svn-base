package com.qianjing.finance.ui.activity.custom.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

public class FragmentItem1 extends Fragment{

	private TextView tvWarn;
	private EditText letOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendConBtnEnable(false);
		LogUtils.syso("fragment1 onCreate");
	}
	
	
	public View onCreateView(LayoutInflater inflater
			, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.syso("fragment1 onCreateView");
		View view = View.inflate(getActivity(), R.layout.fragment_ask_age, null);
		
		letOne = (EditText) view.findViewById(R.id.et_vip_page_one);
		letOne.addTextChangedListener(new VIPTextWatcher());
		letOne.requestFocus();
		tvWarn = (TextView) view.findViewById(R.id.tv_warn_item1);
		tvWarn.setVisibility(View.GONE);
		if(UserManager.getInstance().getUser().getAge()!=null){
			String ageStr = UserManager.getInstance().getUser().getAge();
			letOne.setText(ageStr);
			letOne.setSelection(ageStr.length());
		}
		
		return view;
	};
	
	public EditText getEditText(){
		return letOne;
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	private class VIPTextWatcher implements TextWatcher{
		
		private String beforeStr;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			beforeStr = letOne.getText().toString().trim();
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			
			if("".equals(s+"")){
				return ;
			}
			
			String afterStr = letOne.getText().toString().trim();
			if(StringHelper.isSpecialMoney(afterStr)){
				letOne.setText(beforeStr);
			}
			
			
			int editNum = Integer.parseInt(s+"");
			
			LogUtils.i(getActivity(), "edit的当前值为:"+editNum);
			
			if(editNum>=18 && editNum<=100){
				tvWarn.setVisibility(View.GONE);
				UserManager.getInstance().getUser().setAge(editNum+"");
				sendConBtnEnable(true);
			}else{
				tvWarn.setVisibility(View.VISIBLE);
				sendConBtnEnable(false);
			}
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	}
	
	private void sendConBtnEnable(boolean enable){
		
		Intent intent = new Intent();
		intent.setAction("com.qjautofinancial.vip.custom");
		intent.putExtra("pageNum", 1);
		intent.putExtra("isEnable", enable);
		getActivity().sendBroadcast(intent);
	}
	
}
