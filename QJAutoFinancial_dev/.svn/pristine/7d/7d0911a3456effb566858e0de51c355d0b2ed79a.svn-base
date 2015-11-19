package com.qianjing.finance.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.qjautofinancial.R;


/**
 * <p>Title: LoadingDialog</p>
 * <p>Description: 含有MaterialDesign样式进度条的对话框</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年1月29日
 */
public class LoadingDialog extends Dialog {
	
	public LoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_loading);
		
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// 设置透明度
		lp.alpha = 1.0f;
		// 设置黑暗度
		lp.dimAmount = 0.0f;
		window.setAttributes(lp);
		
        setCanceledOnTouchOutside(false);
	}

}
