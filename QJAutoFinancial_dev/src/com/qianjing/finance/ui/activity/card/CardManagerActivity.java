
package com.qianjing.finance.ui.activity.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.card.CardListResponse;
import com.qianjing.finance.model.card.CardListResponse.Card;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.model.p2p.P2PCardResponse;
import com.qianjing.finance.model.p2p.P2PCardResponse.P2PCard;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonCallBack;
import com.qianjing.finance.util.Network;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.adapter.CardListAdapter;
import com.qianjing.finance.widget.adapter.CardListAdapter.CardConst;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

/** 
* @description 新版银行卡管理界面
* @author fangyan
* @date 2015年9月28日
*/
public class CardManagerActivity extends BaseActivity {

	private ArrayList<HashMap<String, String>> mListData;
	private ArrayList<Card> mListCard = new ArrayList<Card>();
	private ArrayList<Card> mListUnboundedCard = new ArrayList<Card>();

	private ListView mLvCard;
	private CardListAdapter mCardAdapter;
	private Button mBtnNewbound;

	/** 待解绑银行卡卡号 */
	private String mUnboundCardNum;
	/** 用户登录密码 */
	private String mStrPass;
	/** P2P银行卡实例 */
	private P2PCardResponse mP2PCardResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	private void initView() {

		setContentView(R.layout.activity_card_manager);

		setTitleBack();

		setTitleWithId(R.string.title_card_mine);

		setLoadingUncancelable();

		mLvCard = (ListView) findViewById(R.id.lv_listview);
		mBtnNewbound = (Button) findViewById(R.id.btn_newbound);
	}

	@Override
	protected void onResume() {
		super.onResume();

		requestCardList();
	}

	/** 
	* @description 银行卡列表请求
	* @author fangyan 
	*/
	private void requestCardList() {

		mListCard.clear();

		showLoading();

		RequestManager.request(this, UrlConst.CARD_LIST, null, CardListResponse.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				dismissLoading();

				if (model.stateCode == ErrorConst.EC_OK) {
					CardListResponse response = (CardListResponse) model;
					mListCard = response.cards.listBoundCard;
					mListUnboundedCard = response.cards.listUnboundCard;

					if (!mListCard.isEmpty()) {
						mBtnNewbound.setVisibility(View.INVISIBLE);
					} else {
						mBtnNewbound.setVisibility(View.VISIBLE);
						mBtnNewbound.setOnClickListener(boundListener);
					}

					// --------------------------------------------------
					// For test：添加增卡入口
					mBtnNewbound.setVisibility(View.VISIBLE);
					mBtnNewbound.setOnClickListener(boundListener);
					// --------------------------------------------------

					showLoading();

					User user = UserManager.getInstance().getUser();
					Hashtable<String, Object> upload = new Hashtable<String, Object>();
					upload.put("mobile", user == null ? "" : user.getMobile());
					RequestManager.request(CardManagerActivity.this, UrlConst.P2P_CARD_DETAIL, upload, P2PCardResponse.class, new XCallBack() {
						@Override
						public void success(BaseModel model) {

							dismissLoading();

							if (model.stateCode == ErrorConst.EC_OK) {
								mP2PCardResponse = (P2PCardResponse) model;
								P2PCard p2pCard = mP2PCardResponse.card;
								if (StringHelper.isNotBlank(p2pCard.cardNum)) {
									Card card = new Card();
									card.setNumber(p2pCard.cardNum);
									card.setBankName(p2pCard.bankName);
									card.setBankCode(p2pCard.bankCode);
									card.setMasterName(p2pCard.masterName);
									card.setIsP2P(1);
									mListCard.add(card);
								}
							}
							setView(mListCard);
						}

						@Override
						public void fail() {
							dismissLoading();
							setView(mListCard);
						}
					});

				} else
					showHintDialog(model.strErrorMessage);
			}

			@Override
			public void fail() {
				dismissLoading();
			}
		});
	}

	private OnClickListener boundListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			// 判断网络
			if (!Network.checkNetWork(CardManagerActivity.this)) {
				showToast(getString(R.string.net_prompt));
				return;
			}

			if (mListUnboundedCard != null && mListUnboundedCard.isEmpty()) {
				Intent intent = new Intent();
				intent.setClass(CardManagerActivity.this, QuickBillActivity.class);
				intent.putExtra("EXTRA_BEAN_P2P_CARD", mP2PCardResponse);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
				ContentValueBound.isFromCardManager = true;
				return;
			}

			// 判断是否需要还原卡
			if (mListUnboundedCard != null && !mListUnboundedCard.isEmpty()) {
				final Card card = new Card();
				// 设置最大值
				card.setBoundTime(9223372036854775806L);
				if (mListUnboundedCard.size() > 0) {
					for (Card tempInfo : mListUnboundedCard) {
						if (card.getBoundTime() > tempInfo.getBoundTime()) {
							card.setNumber(tempInfo.getNumber());
							card.setBankName(tempInfo.getBankName());
							card.setBoundTime(tempInfo.getBoundTime());
						}
					}

					String strMsg = "您已经有解绑过的" + card.getBankName() + "卡，卡号为" + StringHelper.hintCardNum(card.getNumber()) + "，是否直接还原该卡？";

					showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 发送银行卡还原接口请求
							if (StringHelper.isNotBlank(card.getNumber())) {

								Hashtable<String, Object> map = new Hashtable<String, Object>();
								map.put("cd", card.getNumber());
								RequestManager.request(CardManagerActivity.this, UrlConst.CARD_HTYCARD, map, BaseModel.class, new XCallBack() {
									@Override
									public void success(BaseModel model) {
										dismissLoading();
										if (model.stateCode == ErrorConst.EC_OK) {
											showToast("绑卡成功");

											requestCardList();
										} else
											showHintDialog(model.strErrorMessage);
									}

									@Override
									public void fail() {
										dismissLoading();
									}
								});
							}
							dialog.dismiss();
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				}
			}

		}
	};

	/*
	 * 设置银行卡列表数据源
	 */
	private void setView(ArrayList<Card> listCard) {

		mListData = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		User user = UserManager.getInstance().getUser();
		for (int i = 0; i < listCard.size(); i++) {
			Card card = listCard.get(i);
			map = new HashMap<String, String>();
			map.put(CardConst.KEY_BANK_NAME, card.getBankName());
			map.put(CardConst.KEY_BANK_CODE, card.getBankCode());
			map.put(CardConst.KEY_CARD_TYPE, card.getIsP2P() == 1 ? CardConst.TYPE_STEADY : CardConst.TYPE_FUND);
			map.put(CardConst.KEY_CARD_NUM, StringHelper.showCardLast4(card.getNumber()));
			map.put(CardConst.KEY_MASTER_NAME, StringHelper.isBlank(card.getMasterName()) ? user.getName() : card.getMasterName());
			mListData.add(map);
		}

		mCardAdapter = new CardListAdapter(this, mListData, new CommonCallBack() {
			@Override
			public void back(Object obj) {
				requestCardUnboundState(obj);
			}
		});
		mLvCard.setAdapter(mCardAdapter);
	}

	/**
	 * @description 检查银行卡可解绑状态请求
	 * @author fangyan
	 * @param objPosition
	 */
	private void requestCardUnboundState(Object objPosition) {
		if (objPosition instanceof Integer) {
			int position = (Integer) objPosition;
			if (mListCard != null && position <= mListCard.size()) {
				Card bankInfo = mListCard.get(position);
				mUnboundCardNum = bankInfo.getNumber();

				showLoading();
				Hashtable<String, Object> map = new Hashtable<String, Object>();
				map.put("cd", mUnboundCardNum);
				RequestManager.request(this, UrlConst.CARD_UNBOUND_STATE, map, BaseModel.class, new XCallBack() {
					@Override
					public void success(BaseModel model) {
						dismissLoading();
						if (model.stateCode == ErrorConst.EC_OK) {
							InputDialog.Builder inputDialog = new InputDialog.Builder(CardManagerActivity.this, new CommonCallBack() {
								@Override
								public void back(Object obj) {
									mStrPass = (String) obj;
									if (StringHelper.isNotBlank(mStrPass))
										requestCardUnbound();
								}
							});
							inputDialog.setTitle("请输入登录密码");
							inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							inputDialog.create().show();
						} else
							showHintDialog(model.strErrorMessage);
					}

					@Override
					public void fail() {
						dismissLoading();
					}
				});

			} else
				showToast("程序内部错误");
		} else
			showToast("程序内部错误");
	}

	/** 
	* @description 银行卡解绑请求
	* @author fangyan 
	*/
	private void requestCardUnbound() {
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("cd", mUnboundCardNum);
		upload.put("passwd", mStrPass);
		RequestManager.request(CardManagerActivity.this, UrlConst.CARD_UNBOUND, upload, BaseModel.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				dismissLoading();
				if (model.stateCode == ErrorConst.EC_OK) {
					showToast("解绑成功");
					// 刷新银行卡列表
					requestCardList();
				} else
					showHintDialog(model.strErrorMessage);
			}

			@Override
			public void fail() {
				dismissLoading();
			}
		});
	}

}
