package com.qianjing.finance.ui.activity.fund;

import com.qianjing.finance.model.activity.RedBag;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleRedBagList;
import com.qianjing.finance.net.response.model.ResponseRedBagList;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.widget.adapter.redbag.RewardAdapter;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @description 基金组合申购时，红包选择页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class RewardListActivity extends BaseActivity implements OnItemClickListener {
	/**
	 * UI
	 */
	private LinearLayout contentLayout;
	private LinearLayout emptyLayout;
	private ListView rewardListView;
	private RewardAdapter rewardAdapter;
	private View footerView;
	/**
	 * data
	 */
	private Intent mIntent;
	private ArrayList<RedBag> listData;
	private RedBag currentBag;
	private String buyAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mIntent = getIntent();
		currentBag = (RedBag) mIntent.getSerializableExtra("selected_redbag");
		buyAccount = mIntent.getStringExtra("buy_account");

		setContentView(R.layout.activity_reward_listview_layout);
		setTitleImgLeft(R.drawable.title_left, new OnClickListener() {
			@Override
			public void onClick(View v) {
				backData();
			}
		});
		setTitleWithId(R.string.xuan_ze_hong_bao);

		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
		rewardListView = (ListView) findViewById(R.id.reward_list_view);
		footerView = LayoutInflater.from(this).inflate(
				R.layout.activity_reward_listview_footer, null);
		rewardListView.addFooterView(footerView, null, false);
		rewardListView.setOnItemClickListener(this);
	}

	private void initData() {
		RequestActivityHelper.requestRedBagList(this, new IHandleRedBagList() {
			@Override
			public void handleResponse(ResponseRedBagList response) {
				if (response != null && response.listRedBag != null) {
					listData = response.listRedBag;
					updateUI();
				}
			}
		});
	}

	/**
	 * 更新UI
	 */
	private void updateUI() {
		if (listData == null || listData.size() == 0) {
			contentLayout.setVisibility(View.GONE);
			// 显示空间面
			emptyLayout.setVisibility(View.VISIBLE);
		} else {
			contentLayout.setVisibility(View.VISIBLE);
			rewardAdapter = new RewardAdapter(this, listData, currentBag,
					buyAccount);
			rewardListView.setAdapter(rewardAdapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RedBag redbag = (RedBag) rewardAdapter.getItem(position);
		// 可用
		if (Float.valueOf(buyAccount) >= redbag.limitSum) {
			if (currentBag == null) {
				currentBag = redbag;
				rewardAdapter.updateRedBag(currentBag);
			} else {
				if (currentBag.id == redbag.id) {
					currentBag = null;
					rewardAdapter.updateRedBag(currentBag);
				} else {
					currentBag = redbag;
					rewardAdapter.updateRedBag(redbag);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backData();
		}
		return true;
	}

	/**
	 * 向前一页返回数据
	 */
	private void backData() {
		if (currentBag != null) {
			mIntent.putExtra("redbag", currentBag);
			setResult(RESULT_OK, mIntent);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
	}
}
