
package com.qianjing.finance.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.model.common.user.UserRequest;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.MainActivity;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

/**
 * @description 用户信息界面
 * @author fangyan
 * @date 2015年8月27日
 */
public class AccountActivity extends BaseActivity implements OnClickListener {
	private TextView account_name;
	private TextView account_Identity;
	private TextView account_mobile;
	private TextView tv_email;
	private TextView tv_address;
	private TextView tv_marital_status;
	private TextView tv_education;
	private TextView tv_job;
	private TextView tv_experience;
	private Button bt_exit;
	private RelativeLayout rl_email;
	private RelativeLayout rl_address;
	private RelativeLayout rl_marital_status;
	private RelativeLayout rl_education;
	private RelativeLayout rl_job;
	private RelativeLayout rl_experience;

	private String[] marText;
	private String[] eduText;
	private String[] investText;
	private String[] workText;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();

		intiView();

		requestUser();
	}

	private void initData() {
		marText = getResources().getStringArray(R.array.merried);
		eduText = getResources().getStringArray(R.array.edutcation);
		investText = getResources().getStringArray(R.array.invest);
		workText = getResources().getStringArray(R.array.work);
	}

	private void intiView() {

		setContentView(R.layout.activity_user_info);

		setTitleBack();

		setTitleWithId(R.string.title_user_info);

		account_name = (TextView) findViewById(R.id.Account_name);
		account_Identity = (TextView) findViewById(R.id.Account_Identity);
		account_mobile = (TextView) findViewById(R.id.Account_mobile);

		User user = UserManager.getInstance().getUser();
		account_name.setText(user.getName());
		account_Identity.setText(StringHelper.hideCertificate(user.getIdentity()));
		account_mobile.setText(PrefUtil.getPhoneNumber(this));

		rl_email = (RelativeLayout) findViewById(R.id.rl_email);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		rl_marital_status = (RelativeLayout) findViewById(R.id.rl_marital_status);
		rl_education = (RelativeLayout) findViewById(R.id.rl_education);
		rl_job = (RelativeLayout) findViewById(R.id.rl_job);
		rl_experience = (RelativeLayout) findViewById(R.id.rl_experience);

		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_marital_status = (TextView) findViewById(R.id.tv_marital_status);
		tv_education = (TextView) findViewById(R.id.tv_education);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_experience = (TextView) findViewById(R.id.tv_experience);

		bt_exit = (Button) findViewById(R.id.bt_exit);
		bt_exit.setOnClickListener(this);
		rl_email.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_marital_status.setOnClickListener(this);
		rl_education.setOnClickListener(this);
		rl_job.setOnClickListener(this);
		rl_experience.setOnClickListener(this);
	}

	private void requestLogout() {
		RequestManager.request(this, UrlConst.LOGOUT);
	}

	private void requestUser() {

		RequestManager.request(this, UrlConst.SELF, null, UserRequest.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				if (model.stateCode == ErrorConst.EC_OK) {
					user = ((UserRequest) model).user;
					setInfoToView();
				}
			}

			@Override
			public void fail() {
			}
		});
	}

	private void setInfoToView() {
		if (user != null) {
			tv_email.setText(user.getEmail());
			tv_address.setText(user.getAddr());
			tv_marital_status.setText(marText[Integer.parseInt(user.getHasMarried())]);
			tv_education.setText(eduText[Integer.parseInt(user.getEducate())]);
			tv_job.setText(workText[Integer.parseInt(user.getWork())]);
			tv_experience.setText(investText[Integer.parseInt(user.getHasInvest())]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_exit:// 退出登录
			userExit();
			break;
		case R.id.rl_email:// 电子邮箱
			openDetail(0);
			break;
		case R.id.rl_address:// 通讯地址
			openDetail(1);
			break;
		case R.id.rl_marital_status:// 婚姻状况
			openDetail(2);
			break;
		case R.id.rl_education:// 学历
			openDetail(3);
			break;
		case R.id.rl_job:// 职业
			openDetail(4);
			break;
		case R.id.rl_experience:// 证券投资经验
			openDetail(5);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ViewUtil.USER_INFO_MODIFY_CODE)
			requestUser();
	}

	private void openDetail(int detailflag) {
		// 对六项内容："电子邮箱","通讯地址","婚姻状况","学历","职业","证券投资" 设置标记分别对应为0,1,2,3,4,5
		if (user != null) {
			Intent intent = new Intent();
			intent.setClass(this, AccountDetailActivity.class);
			intent.putExtra("infoType", detailflag);
			intent.putExtra("user", user);
			startActivityForResult(intent, ViewUtil.REQUEST_CODE);
		}
	}

	private void userExit() {

		requestLogout();

		UserManager.getInstance().removeUser();

		PrefUtil.removeAllInformFlag(this);

		PrefUtil.saveKey(this, "");
		PrefUtil.setIsFirstToMain(this, true);

		Intent intent = new Intent(AccountActivity.this, MainActivity.class);
		intent.putExtra("isSignout", true);
		startActivity(intent);
	}

}
