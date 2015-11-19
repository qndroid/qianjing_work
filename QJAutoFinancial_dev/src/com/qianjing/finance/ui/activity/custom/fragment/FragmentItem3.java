package com.qianjing.finance.ui.activity.custom.fragment;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.util.LogUtils;
import com.qjautofinancial.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentItem3 extends Fragment{

	private TextView tvWarn;
	private EditText letOne;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendConBtnEnable(false);
		LogUtils.syso("fragment3 onCreate");
	}
	
	public View onCreateView(LayoutInflater inflater
			, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.syso("fragment3 onCreateView");
		View view = View.inflate(getActivity(), R.layout.fragment_ask_enable_assets, null);
		letOne = (EditText) view.findViewById(R.id.et_vip_page_three);
		letOne.requestFocus();
		tvWarn = (TextView) view.findViewById(R.id.tv_warn_item3);
		tvWarn.setVisibility(View.GONE);
		letOne.addTextChangedListener(new VIPTextWatcher());
		if(UserManager.getInstance().getUser().getInit()!=null){
			String initStr = UserManager.getInstance().getUser().getInit();
			letOne.setText(initStr);
			letOne.setSelection(initStr.length());
		}
		return view;
	};
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	public EditText getEditText(){
		return letOne;
	}
	
	private class VIPTextWatcher implements TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			if("".equals(s+"")){
				return ;
			}
			
			if(".".equals(s+"")){
				return ;
			}
			
			float editNum = Float.parseFloat(s+"");
			
			LogUtils.i(getActivity(), "edit的当前值为:"+editNum);
			
			if(editNum>=100){
				tvWarn.setVisibility(View.GONE);
				UserManager.getInstance().getUser().setInit(s+"");
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
		intent.putExtra("pageNum", 3);
		intent.putExtra("isEnable", enable);
		getActivity().sendBroadcast(intent);
		
	}
	
	public void setContentView(String str){
		letOne.setText(str);
	}
}
