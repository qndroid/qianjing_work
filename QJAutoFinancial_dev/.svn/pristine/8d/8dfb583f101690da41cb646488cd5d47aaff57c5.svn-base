package com.qianjing.finance.ui.activity.account;

import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Util;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VersionDescriptionActivity extends BaseActivity implements OnClickListener{
	
	private TextView tv_version;
	private TextView title_middle_text;
	private Button ButBack;
	private TextView tv_vcontent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fina_version_description);
		
		initView();
		
		setListener();
	}

	private void setListener() {
		ButBack.setOnClickListener(this);
	}

	private void initView() {
		tv_version=(TextView) findViewById(R.id.tv_version);
		tv_version.setText("当前版本："+Util.getVersionName(this));
		title_middle_text=(TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("版本说明");
		ButBack=(Button) findViewById(R.id.bt_back);
		ButBack.setVisibility(View.VISIBLE);
		tv_vcontent=(TextView) findViewById(R.id.tv_vcontent);
		tv_vcontent.setText(R.string.Update_description);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back://返回
			finish();
			break;
		}
	}
}
