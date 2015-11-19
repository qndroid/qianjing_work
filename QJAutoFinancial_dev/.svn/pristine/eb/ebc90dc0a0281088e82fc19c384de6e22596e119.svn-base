
package com.qianjing.finance.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.account.AboutActivity;
import com.qianjing.finance.ui.activity.account.AccountActivity;
import com.qianjing.finance.ui.activity.account.PassWordManagerActivity;
import com.qianjing.finance.ui.activity.card.CardManagerActivity;
import com.qianjing.finance.ui.activity.common.MainActivity;
import com.qianjing.finance.ui.activity.common.SetActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

/**
 * Title: SetTabView Description: 账户管理
 */
@SuppressLint("NewApi")
public class AccountFragment extends Fragment implements OnClickListener {

	private MainActivity mCurrentActivity;
	private View view;
	private RelativeLayout rl_member;
	private RelativeLayout rl_account;
	private RelativeLayout rl_invite_code;
	private RelativeLayout rl_conpons;
	private RelativeLayout rl_card;
	private RelativeLayout rl_pass;
	private RelativeLayout rl_us;
	private RelativeLayout rl_set;
	private ImageView ivMember;
	private TextView tvMemberName;
	private TextView tvMemberLevel;

	private User mUser;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mCurrentActivity = (MainActivity) getActivity();
		view = inflater.inflate(R.layout.activity_account_2, null);

		rl_member = (RelativeLayout) view.findViewById(R.id.rl_member);
		rl_account = (RelativeLayout) view.findViewById(R.id.rl_account);
		rl_invite_code = (RelativeLayout) view.findViewById(R.id.rl_invite_code);
		rl_conpons = (RelativeLayout) view.findViewById(R.id.rl_conpons);
		rl_card = (RelativeLayout) view.findViewById(R.id.rl_card);
		rl_pass = (RelativeLayout) view.findViewById(R.id.rl_pass);
		rl_us = (RelativeLayout) view.findViewById(R.id.rl_us);
		rl_set = (RelativeLayout) view.findViewById(R.id.rl_set);
		ivMember = (ImageView) view.findViewById(R.id.iv_member);
		tvMemberName = (TextView) view.findViewById(R.id.tv_member_name);
		tvMemberLevel = (TextView) view.findViewById(R.id.tv_member_level);

		rl_member.setOnClickListener(this);
		rl_account.setOnClickListener(this);
		rl_invite_code.setOnClickListener(this);
		rl_conpons.setOnClickListener(this);
		rl_card.setOnClickListener(this);
		rl_pass.setOnClickListener(this);
		rl_us.setOnClickListener(this);
		rl_set.setOnClickListener(this);
		tvMemberLevel.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// 更新帐户信息
		mUser = UserManager.getInstance().getUser();
		if (mUser != null) {
			if (mUser.getIsMember() == 1) {
				ivMember.setImageResource(R.drawable.icon_member_common);
				tvMemberLevel.setText("普通会员");
			} else if (mUser.getIsMember() == 2) {
				ivMember.setImageResource(R.drawable.icon_member_vip);
				tvMemberLevel.setText("VIP会员");
			}

			// 没有身份证号则设置未绑卡图片
			if (StringHelper.isBlank(mUser.getIdentity()))
				tvMemberName.setText("未绑卡");
			else
				tvMemberName.setText(mUser.getName());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_member: // 会员介绍页
			Intent intent = new Intent(mCurrentActivity, WebActivity.class);
			intent.putExtra("url", UrlConst.MEMBER_SYSTEM_INTRODUCE);
			intent.putExtra("webType", 8);
			mCurrentActivity.startActivity(intent);
			break;
		case R.id.rl_account:// 我的账户
			mCurrentActivity.openActivity(AccountActivity.class);
			break;
		case R.id.rl_card:// 银行卡管理
			mCurrentActivity.openActivity(CardManagerActivity.class);
			break;
		case R.id.rl_pass:// 密码管理
			mCurrentActivity.openActivity(PassWordManagerActivity.class);
			break;
		case R.id.rl_us:// 关于我们
			mCurrentActivity.openActivity(AboutActivity.class);
			break;
		case R.id.rl_set:// 设置
			mCurrentActivity.openActivity(SetActivity.class);
			break;
		case R.id.rl_conpons:
			Intent intent0 = new Intent(getActivity(), WebActivity.class);
			intent0.putExtra("webType", 5);
			mCurrentActivity.startActivity(intent0);
			break;
		case R.id.rl_invite_code:
			Intent intent2 = new Intent(getActivity(), WebActivity.class);
			intent2.putExtra("webType", 6);
			mCurrentActivity.startActivity(intent2);
			break;
		}
	}

}
