package com.qianjing.finance.ui.activity.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qjautofinancial.R;

public class AboutActivity extends BaseActivity {

	private static QApplication myApp;
	private String CallTelphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fina_us);
		Button butBack = (Button) findViewById(R.id.bt_back);
		butBack.setVisibility(View.VISIBLE);
		TextView title_middle_text = (TextView) findViewById(R.id.title_middle_text);
		title_middle_text.setText("关于我们");
		TextView SmallNote = (TextView) findViewById(R.id.SmallNote);
		SmallNote.setText("@钱景私人理财");
		myApp = (QApplication) getApplication();
		myApp.addActivity(AboutActivity.this);
		RelativeLayout rl_telophone = (RelativeLayout) findViewById(R.id.rl_telophone);
		final TextView Telophone = (TextView) findViewById(R.id.Telophone);
		RelativeLayout rl_company = (RelativeLayout) findViewById(R.id.rl_company);
		final TextView VerNub = (TextView) findViewById(R.id.VerNub);
		try{
		VerNub.setText(getPackageManager().getPackageInfo(
				getPackageName(), 1).versionName);
		}
		 catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		butBack.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				
				finish();

			}
		});

		rl_company.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/*Intent intent = new Intent(Us.this,
						CompanyActivity.class);
				startActivity(intent);*/
				Bundle bundle3=new Bundle();
				bundle3.putInt("webType", 1);
				openActivity(WebActivity.class,bundle3);
			}
		});

		rl_telophone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CallTelphone = Telophone.getText().toString();
				UserCallDialog();

			}
		});
	}

	
	private void UserCallDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		Window window = dlg.getWindow();

		window.setContentView(R.layout.fina_sure_cancel_dialog);

		// 为确认按钮添加事执行退出应用操
		Button ok = (Button) window.findViewById(R.id.ButSure);
		Button ButCancel = (Button) window.findViewById(R.id.ButCancel);
		TextView title = (TextView) window.findViewById(R.id.Title);
		TextView Msg = (TextView) window.findViewById(R.id.Msg);
		ok.setText("确定");
		ButCancel.setText("取消");
		// title.setText(title_middle_text.getText().toString());
		Msg.setText("您确认要呼叫客服吗");

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				int nRet = myApp.activity_list.size();
				for (int i = 0; i < nRet; i++) {
					myApp.activity_list.get(i).finish();

				}
				Intent intent = new Intent();
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:" + CallTelphone));
				startActivity(intent);

				dlg.cancel();

			}
		});
		ButCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.cancel();

			}
		});

	}

}