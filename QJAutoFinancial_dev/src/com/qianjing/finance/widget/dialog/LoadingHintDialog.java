package com.qianjing.finance.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qjautofinancial.R;

/**
 * <p>Title: LoadingDialog</p>
 * <p>Description: 加载对话框</p>
 * <p>Company: www.qianjing.com</p> 
 * @author	fangyan
 * @date	2015年1月29日
 */
public class LoadingHintDialog extends Dialog {
	
	private TextView tv;
	
	public LoadingHintDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private LoadingHintDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public void setHint(String strHint) {
		if (strHint != null) {
			tv.setText(strHint);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_hint_loading);
		
		tv = (TextView) this.findViewById(R.id.tv);
		tv.setText("处理中,请稍候...");

		LinearLayout linearLayout = (LinearLayout) this
				.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(210);

        setCanceledOnTouchOutside(false);
	}

}
