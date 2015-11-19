package com.qianjing.finance.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


/**
 * <p>Title: AssembleTextWatcher</p>
 * <p>Description: 设置输入框不可输入0</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年5月18日
 */
public class AssembleTextWatcher implements TextWatcher {
	
	private EditText mEditText;
	
	public AssembleTextWatcher (EditText mEditText) {
		this.mEditText = mEditText;
	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			if (mEditText!=null && Integer.valueOf(mEditText.getText().toString())==0)
				mEditText.setText("");
		} catch(Exception e){}
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	
}
