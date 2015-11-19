package com.qianjing.finance.ui.activity.rebalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.net.helper.RequestAssembleHelper;
import com.qianjing.finance.net.helper.RequestRebalanceHelper;
import com.qianjing.finance.net.ihandle.IHandleAssembleDetail;
import com.qianjing.finance.net.ihandle.IHandleAssembleList;
import com.qianjing.finance.net.response.model.ResponseAssembleDetail;
import com.qianjing.finance.net.response.model.ResponseAssembleList;
import com.qianjing.finance.ui.activity.assemble.AssembleAIPConfigActivity;
import com.qianjing.finance.ui.activity.assemble.AssembleDetailActivity;
import com.qianjing.finance.ui.activity.assemble.AssembleMineActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.history.AssembleHistoryActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.adapter.AssetsAdapter;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshListView;
import com.qjautofinancial.R;

public class RebalanceListActivity extends BaseActivity {

	private FrameLayout defPage;
	private ImageView defaultPic;
	private TextView defaultTxt;
	private PullToRefreshListView ptrlv;
	private ListView refreshableView;

	private List<Map<String, String>> mListData = null;// 展示信息
	private AssetsAdapter mAdapter;

	private boolean PULL_TO_REFRESH = false;
	private int startOf = 0;
	private int pageNum = CustomConstants.ASSETS_PAGE_NUMBER;
	private TextView btnReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_rebalance_list);
		setTitleWithId(R.string.balance_assemble_title);
		setTitleBack();
		btnReg = (TextView) findViewById(R.id.title_right_text);

		/**
		 * set title history
		 * */
//		btnReg.setVisibility(View.VISIBLE);
//		btnReg.setText(R.string.bill);
//		btnReg.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(RebalanceListActivity.this,
//						AssemableHistoryActivity.class);
//				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
//			}
//		});

		TextView rebalanceHeader = (TextView) findViewById(R.id.tv_rebalance_head);
		rebalanceHeader.setText(String.format(
				getString(R.string.balance_list_title), getIntent()
						.getStringExtra("count")));

		/**
		 * add the empty page
		 * */
		defPage = (FrameLayout) findViewById(R.id.in_default_page);
		defaultPic = (ImageView) findViewById(R.id.iv_deault_page_pic);
		defaultTxt = (TextView) findViewById(R.id.tv_deault_page_text);

		defaultPic.setBackgroundResource(R.drawable.img_assets);
		defaultTxt.setText(R.string.empty_group_assets);

		// 获取方案列表数据
		requestSchemaList(-1, -1);

		ptrlv = (PullToRefreshListView) findViewById(R.id.ptrflv_list_view);
		ptrlv.setMode(Mode.BOTH);
		ptrlv.setShowIndicator(false);
		ptrlv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				/**
				 * pull down to refresh
				 * */
				PULL_TO_REFRESH = true;
				requestSchemaList(0, startOf
						+ CustomConstants.ASSETS_PAGE_NUMBER);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {

				/**
				 * pull up to load
				 * */
				PULL_TO_REFRESH = false;
				startOf = startOf + CustomConstants.ASSETS_PAGE_NUMBER;
				requestSchemaList(-1, -1);
			}
		});
		refreshableView = ptrlv.getRefreshableView();

		mListData = new ArrayList<Map<String, String>>();

		refreshableView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String strSid = mListData.get(position).get("sid");
				RequestAssembleHelper.requestAssembleDetail(
						RebalanceListActivity.this, strSid,
						new IHandleAssembleDetail() {
							@Override
							public void handleResponse(
									ResponseAssembleDetail response) {

								AssembleDetail detail = response.detail;
								if (detail != null) {

									if (detail.getAssembleAssets() != null
											&& (!StringHelper.isBlankZero(detail
													.getAssembleAssets()
													.getRedemping()) || !StringHelper
													.isBlankZero(detail
															.getAssembleAssets()
															.getSubscripting()))) {
										showHintDialog(getString(R.string.rebalance_purchaseing_text));
									} else {
										Intent intent = new Intent();
										intent.setClass(
												RebalanceListActivity.this,
												RebalanceActivity.class);
										intent.putExtra("mSid", detail
												.getAssembleBase().getSid());
										intent.putExtra("mName", detail
												.getAssembleBase().getName());
										startActivity(intent);
									}
								}

								// ---------------------------
								// AssembleDetail detail = response.detail;
								// if (detail != null) {
								//
								// Intent intent = new Intent();
								// intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL,
								// detail);
								//
								// // 未申购组合
								// if
								// (StrUtil.isBlank(detail.getAssembleConfig().getBank()))
								// {
								// intent.putExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL,
								// ViewUtil.FROM_ASSETS);
								// intent.setClass(RebalanceListActivity.this,
								// AssembleConfigActivity.class);
								// startActivityForResult(intent,
								// ViewUtil.REQUEST_CODE);
								// }
								// else
								// {
								// // 已申购组合
								// intent.setClass(RebalanceListActivity.this,
								// AssembleDetailActivity.class);
								// startActivityForResult(intent,
								// ViewUtil.REQUEST_CODE);
								// }
								// }

								// ---------------------------

							}
						});
			}
		});

	}

	private void requestSchemaList(int of, int nm) {

		int startN;
		int pageN;

		if (of != -1 && nm != -1) {
			startN = of;
			pageN = nm;
		} else {
			startN = startOf;
			pageN = pageNum;
		}

		RequestRebalanceHelper.requestAssembleList(this, startN, pageN,
				new IHandleAssembleList() {
					@Override
					public void handleResponse(ResponseAssembleList response) {

						ptrlv.onRefreshComplete();

						ArrayList<AssembleBase> listAssemble = response.listAssemble;

						if (listAssemble != null && listAssemble.size() > 0) {

							if (PULL_TO_REFRESH) {
								mListData.clear();
							}

							if (listAssemble.size() < CustomConstants.ASSETS_PAGE_NUMBER) {
								ptrlv.setMode(Mode.PULL_FROM_START);
							}

							for (int i = 0; i < listAssemble.size(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("sid", listAssemble.get(i).getSid());
								map.put("name", listAssemble.get(i).getName());
								if (listAssemble.get(i).assets != null) {
									map.put("assets",
											listAssemble.get(i).assets
													.getAssets());
									map.put("profit",
											listAssemble.get(i).assets
													.getProfit());
									map.put("subscripting",
											listAssemble.get(i).assets
													.getSubscripting());
									map.put("isbalance", listAssemble.get(i)
											.getBalanceState() + "");
									map.put("balanceopt", listAssemble.get(i)
											.getBalanceOpState() + "");
								}
								mListData.add(map);
							}

							if (mListData.size() == 0) {
								// 如果为空,加载默认空布局
								ptrlv.setVisibility(View.GONE);
								defPage.setVisibility(View.VISIBLE);
							}
							ptrlv.onRefreshComplete();
							initAdapter();
						}
					}
				});
	}

	public void initAdapter() {
		if (mAdapter == null) {
			mAdapter = new AssetsAdapter(this, mListData);
			ptrlv.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_BUY_RESULT_CODE
				|| resultCode == ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE
				|| resultCode == ViewUtil.ASSEMBLE_DELETE_RESULT_CODE) {
			PULL_TO_REFRESH = true;
			requestSchemaList(0, startOf + CustomConstants.ASSETS_PAGE_NUMBER);
		}
	}
}
