package com.qianjing.finance.ui.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qjautofinancial.R;
public class CompanyActivity extends BaseActivity {

	private static QApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_company_activity);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(View.VISIBLE);
		TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("公司简介");
		myApp = (QApplication) getApplication();
		myApp.addActivity(CompanyActivity.this);
		
		butBack.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}
}