package com.qianjing.finance.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qjautofinancial.R;

public class DataItem extends LinearLayout {

	private TextView tv_handle;
	private TextView tv_abbrev;
	private TextView tv_shares;

	public DataItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public DataItem(Context context) {
		super(context);
		initView(context);
	}

	public DataItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.three_weight_item, this);
		tv_handle = (TextView) view.findViewById(R.id.tv_handle);
		tv_abbrev = (TextView) view.findViewById(R.id.tv_abbrev);
		tv_shares = (TextView) view.findViewById(R.id.tv_shares);
	}

	public void setHandle(String str) {
		tv_handle.setText(str);
	}

	public void setAbbrev(String str) {
		tv_abbrev.setText(str);
	}

	public void setShare(String str) {
		tv_shares.setText(str);
	}
}
