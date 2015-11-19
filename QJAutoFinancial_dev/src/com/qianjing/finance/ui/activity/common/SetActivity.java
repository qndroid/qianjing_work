
package com.qianjing.finance.ui.activity.common;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.service.update.UpdateService;
import com.qianjing.finance.ui.activity.account.UserfeedbackActivity;
import com.qianjing.finance.ui.activity.account.VersionDescriptionActivity;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.view.dialog.CommonDialog;
import com.qianjing.finance.view.dialog.CommonDialog.DialogClickListener;
import com.qjautofinancial.R;

/**
 * @description 用户设置页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class SetActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_update;
	private TextView tv_updatetext;
	private RelativeLayout rl_version;
	private RelativeLayout rl_feedback;
	private String versionStr;
	private String lastVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_other);
		setTitleWithId(R.string.title_other);
		setTitleBack();

		tv_updatetext = (TextView) findViewById(R.id.tv_updatetext);
		versionStr = Util.getVersionName(this);
		tv_updatetext.setText("当前版本" + versionStr);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_version = (RelativeLayout) findViewById(R.id.rl_version);
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);

		rl_update.setOnClickListener(this);
		rl_version.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
	}

	private void checkVersion() {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		String url = "conf/autofinance.php";
		AnsynHttpRequest.requestByPost(SetActivity.this, url, callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack() {
		@Override
		public void back(String data, int url) {
			Message msg = new Message();
			msg.obj = data;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handleJsonData((String) msg.obj);
		};
	};

	private void handleJsonData(String strJson) {
		dismissLoading();
		if (strJson == null || "".equals(strJson) || "1001".equals(strJson)) {
			dismissLoading();
			showHintDialog("网络不给力！");
			return;
		}
		try {
			JSONObject object = new JSONObject(strJson);
			String emsg = object.optString("emsg");
			int ecode = object.optInt("ecode");
			if (ecode == 0) {
				JSONObject data = object.optJSONObject("data");
				JSONObject software = data.optJSONObject("software");
				JSONObject android = software.optJSONObject("android");
				lastVersion = android.optString("curVersion");
				if (Util.checkVersion(lastVersion, Util.getVersionName(this)) == 1) {
					CommonDialog dialog = new CommonDialog(this, getString(R.string.update_new_version), getString(R.string.update_title),
							getString(R.string.update_install), getString(R.string.cancel), new DialogClickListener() {
								@Override
								public void onDialogClick() {
									Intent updateIntent = new Intent(SetActivity.this, UpdateService.class);
									updateIntent.putExtra("lastVersion", lastVersion);
									startService(updateIntent);
								}
							});
					dialog.show();
				} else {
					showHintDialog("您已是最新版本");
				}
			} else {
				showHintDialog(emsg);
			}

		} catch (JSONException e) {
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_update:// 检查更新
			checkVersion();
			break;
		case R.id.rl_version:// 版本说明
			openActivity(VersionDescriptionActivity.class);
			break;
		case R.id.rl_feedback:// 用户反馈
			openActivity(UserfeedbackActivity.class);
			break;
		}
	}
}
