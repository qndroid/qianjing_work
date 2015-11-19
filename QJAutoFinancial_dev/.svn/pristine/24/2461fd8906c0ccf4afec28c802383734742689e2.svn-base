package com.qianjing.finance.view.assembleredeem;

import com.qjautofinancial.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

/**********************************************************
 * @文件名称：RedeemPickerateDialog.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月17日 上午11:57:51
 * @文件描述：
 * @修改历史：2015年6月17日创建初始版本
 **********************************************************/
public class RedeemPickerateDialog extends Dialog implements OnClickListener
{
	private NumberPicker numberPickerView;
	private TextView mTitleView;
	private TextView cancleView;
	private TextView confirmView;
	private android.view.View.OnClickListener confirmListener;
	private int mMin;
	private int mMax = 100;
	private int currentValue;

	public RedeemPickerateDialog(Context context, int min, int currentvalue)
	{
		this(context, min, 100, currentvalue);
	}

	public RedeemPickerateDialog(Context context, int min, int max, int currentvalue)
	{
		super(context);
		this.mMin = min;
		this.mMax = max;
		this.currentValue = currentvalue;
		initView();
	}

	private void initView()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_redeem_picker);

		numberPickerView = (NumberPicker) findViewById(R.id.picker);
		numberPickerView.setMaxValue(mMax);
		numberPickerView.setMinValue(mMin);
		numberPickerView.setValue(currentValue);
		numberPickerView.setFocusable(true);
		numberPickerView.setFocusableInTouchMode(true);
		numberPickerView.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		cancleView = (TextView) findViewById(R.id.btn_cancel);
		cancleView.setOnClickListener(this);
		confirmView = (TextView) findViewById(R.id.btn_confirm);
		confirmView.setOnClickListener(confirmListener);
		mTitleView = (TextView) findViewById(R.id.title);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_cancel:
			numberPickerView.setValue(currentValue);
			this.dismiss();
			break;
		}

	}

	public void setConfirmListener(android.view.View.OnClickListener confirmListener)
	{
		confirmView.setOnClickListener(confirmListener);
	}

	public int getSelectedNumber()
	{
		return numberPickerView.getValue();
	}

	public void setSelectedNumber(int number)
	{
		numberPickerView.setValue(number);
	}

	public void setMaxValue(final int maxValue)
	{
		numberPickerView.setMaxValue(maxValue);
	}

	public void setTitleText(String titleMessage)
	{
		mTitleView.setText(titleMessage);
	}
}
