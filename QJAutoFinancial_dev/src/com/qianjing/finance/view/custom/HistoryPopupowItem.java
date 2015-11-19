package com.qianjing.finance.view.custom;

import com.qjautofinancial.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryPopupowItem extends RelativeLayout {

	private ImageView ivIcon;
	private TextView itemName;
	private ImageView ivSelected;
	private boolean checked;

	public HistoryPopupowItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	@SuppressLint("NewApi")
	public HistoryPopupowItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.HistoryCheckBox);
		
		Drawable drawable = ta.getDrawable(R.styleable.HistoryCheckBox_src);
		String str = ta.getString(R.styleable.HistoryCheckBox_text);
		checked = ta.getBoolean(R.styleable.HistoryCheckBox_checked, false);
		
		ivIcon.setBackground(drawable);
		itemName.setText(str);
		if(checked){
			ivSelected.setVisibility(View.VISIBLE);
		}else{
			ivSelected.setVisibility(View.INVISIBLE);
		}
	}

	public HistoryPopupowItem(Context context) {
		super(context);
		initView(context);
	}
	
	public void setIcon(int id){
		ivIcon.setBackgroundResource(id);
	}
	
	public void setText(String str){
		itemName.setText(str);
	}
	
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		if(checked){
			ivSelected.setVisibility(View.VISIBLE);
		}else{
			ivSelected.setVisibility(View.INVISIBLE);
		}
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.popupow_history_item, this);
		ivIcon = (ImageView) view.findViewById(R.id.iv_assemble);
		itemName = (TextView) view.findViewById(R.id.tv_item_name);
		ivSelected = (ImageView) view.findViewById(R.id.iv_item_selected);
	}
}
