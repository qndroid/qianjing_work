package com.qianjing.finance.view.dialog;

import com.qjautofinancial.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @description 暂停定投对话框
 * @author renzhiqiang
 * @date 2015年8月20日
 */
public class TimingPauseDialog extends Dialog implements View.OnClickListener
{
	/**
	 * Common
	 */
	private Context mContext;
	private TextView mTitleView;
	private TextView mContentView;
	private TextView mCancleView;
	private TextView mConfirmView;
	private EditText mInputPwdView;
	/**
	 * data
	 */
	private String title;
	private String content;

	public TimingPauseDialog(Context context, String title, String content)
	{
		super(context, R.style.Dialog);
		this.mContext = context;
		this.title = title;
		this.content = content;
		initView();
	}

	private void initView()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_timing_pause);
		setCanceledOnTouchOutside(true);

		mTitleView = (TextView) findViewById(R.id.title);
		mContentView = (TextView) findViewById(R.id.text_info);
		mCancleView = (TextView) findViewById(R.id.cancle_view);
		mConfirmView = (TextView) findViewById(R.id.confirm_view);
		mInputPwdView = (EditText) findViewById(R.id.input_pwd);
		mCancleView.setOnClickListener(this);

		mTitleView.setText(title);
		mContentView.setText(content);
	}

	public String getPassword()
	{
		return mInputPwdView.getText().toString();
	}

	public void setConfirmListener(View.OnClickListener clickListener)
	{
		mConfirmView.setOnClickListener(clickListener);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.cancle_view:
			dismiss();
			break;
		}
	}
}