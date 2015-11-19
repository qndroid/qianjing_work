package com.qianjing.finance.ui.activity.assemble;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.model.virtual.AssetsBean;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.purchase.PurchaseBuyActivity;
import com.qianjing.finance.util.Common;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qianjing.finance.view.indictorview.IndiactorView;
import com.qianjing.finance.view.indictorview.IndiactorView.IndictorClickListener;
import com.qjautofinancial.R;


/**
 * <p>Title: AssembleConfigrationActivity</p>
 * <p>Description: 组合配置详情界面（新版）</p>
 * @author fangyan
 * @date 2015年5月29日
 */
public class AssembleConfigActivity extends BaseActivity {
	
	/** 组合编辑按钮 */
	private ImageView imageRight;
	/** 风险指示 */
	private IndiactorView indictorView;
	/** 基金配比环形图 */
	private PieGraph pieGraph;
	/** 基金列表 */
	private LinearLayout contentView;
	/** 投资建议 */
	private TextView tv_init;
	/** 投资收益 */
	private TextView tv_profit;
	/** 开始投资 */
	private Button btn_submit;
	/** 开始虚拟投资 */
	private Button btn_buy_virtual;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;
	/** 有效银行卡容器 */
	private ArrayList<Card> listCard = new ArrayList<Card>();
	/** 已解绑银行卡容器 */
	private ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
	/** 全部银行卡容器 */
	private ArrayList<Card> listAllCard = new ArrayList<Card>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addEnterStatistics();
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// 验证银行卡列表
		if (UserManager.getInstance().getUser() != null) {
			requestCardList();
		}
	}


	private void initView() {
		
		setContentView(R.layout.activity_assemble_config);

		setTitleBack();
		setLoadingUncancelable();

		indictorView = (IndiactorView) findViewById(R.id.indictor_view);
		indictorView.setIndictorClickListener(new IndictorClickListener() {
			@Override
			public void onClick() {
				showHintDialog(getString(R.string.fen_xian_zhi_shu_shuo_ming),
					getString(R.string.fen_xian_zhi_shu_shuo_ming_nei_rong), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}, getString(R.string.zhi_dao_l));
			}
		});

		contentView = (LinearLayout) findViewById(R.id.content_view);
		pieGraph = (PieGraph) findViewById(R.id.pie_graph);

		tv_init = (TextView) findViewById(R.id.tv_init);
		tv_profit = (TextView) findViewById(R.id.tv_profit);

		imageRight = (ImageView) findViewById(R.id.title_right_image);
		imageRight.setImageResource(R.drawable.title_edit);

		/** 编辑按钮 */
		final RelativeLayout re_assemble_edit = (RelativeLayout) findViewById(R.id.re_assemble_edit);
		Button btn_edit_name = (Button) findViewById(R.id.btn_edit_name);
		Button btn_edit_goal = (Button) findViewById(R.id.btn_edit_goal);
		Button btn_delete = (Button) findViewById(R.id.btn_delete);
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_edit_goal.setVisibility(View.GONE);

		btn_edit_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				re_assemble_edit.setVisibility(View.GONE);

				Intent intent = new Intent(AssembleConfigActivity.this, AssembleModifyNameActivity.class);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			}
		});
		btn_edit_goal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				re_assemble_edit.setVisibility(View.GONE);
//				toGoalPage();
			}
		});
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				re_assemble_edit.setVisibility(View.GONE);

				requestDeleteAssemble();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				re_assemble_edit.setVisibility(View.GONE);
			}
		});

		imageRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (re_assemble_edit.getVisibility() == View.GONE) {
					re_assemble_edit.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_buy_virtual = (Button) findViewById(R.id.btn_buy_virtual);


		int from = getIntent().getIntExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, ViewUtil.FROM_FIND);
		if (from == ViewUtil.FROM_FIND)
			initFindView();
		else if (from == ViewUtil.FROM_ASSETS)
			initMyAssets();
	}

	/**
	 * 从发现页进入的页面初始化
	 */
	private void initFindView() {
		
		imageRight.setVisibility(View.GONE);
		btn_buy_virtual.setVisibility(View.VISIBLE);

		AssembleBase assemble = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE);
		requestCalculate(assemble);
	}

	/**
	 * 从我的资产进入的页面初始化
	 */
	private void initMyAssets() {

		imageRight.setVisibility(View.VISIBLE);
		// 隐藏虚拟投资按钮
		btn_buy_virtual.setVisibility(View.GONE);
		
		AssembleDetail detail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		initAssembleDetail(detail);
	}


	/**
	 * <p>Title: initAssembleDetail</p>
	 * <p>Description: 初始化组合详情数据</p>
	 * @param detail
	 */
	private void initAssembleDetail(AssembleDetail detail) {
		
		mAssembleDetail = detail;

		final AssembleBase assemble = detail.getAssembleBase();
		AssembleConfig config = detail.getAssembleConfig();

		// title
		int type = assemble.getType();
		if (getIntent().getIntExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, -1) == ViewUtil.FROM_FIND) {
			if (type == Const.ASSEMBLE_INVEST)
				setTitleWithId(R.string.title_assemble_invest);
			else if (type == Const.ASSEMBLE_PENSION)
				setTitleWithId(R.string.title_assemble_pension);
			else if (type == Const.ASSEMBLE_HOUSE)
				setTitleWithId(R.string.title_assemble_house);
			else if (type == Const.ASSEMBLE_CHILDREN)
				setTitleWithId(R.string.title_assemble_children);
			else if (type == Const.ASSEMBLE_MARRY)
				setTitleWithId(R.string.title_assemble_marry);
			else if (type == Const.ASSEMBLE_DREAM)
				setTitleWithId(R.string.title_assemble_dream);
		}
		else setTitleWithString(assemble.getName());

		// 风险指数
		indictorView.setPosition(Integer.valueOf(config.getRisk()));

		// 组合配比的环形图和列表
		ArrayList<Fund> listFund = config.getFundList();
		ArrayList<PieSlice> listSlice = new ArrayList<PieSlice>();
		for (int i = 0; i < listFund.size(); i++) {
			Fund fund = listFund.get(i);
			contentView.addView(Common.createFundItem(this, fund), i);
			listSlice.add(new PieSlice((float)fund.rate*100, getResources().getColor(fund.color)));
		}
		pieGraph.setDrawText(config.getStrNonStock(), (int)config.getRatioNonStock()+"%", 
				config.getStrStock(), (int)config.getRatioStock()+"%", listSlice);
		

		String init = config.getInit();
		// 投资建议和预期收益
		tv_profit.setText(R.string.assemble_config_rebalance_hint);
		switch (type) {
		case Const.ASSEMBLE_INVEST:

			tv_init.setText("一次性投入" + StringHelper.formatDecimal(config.getInit()) + "元。");
			break;
		case Const.ASSEMBLE_PENSION:

			if (StringHelper.isNotBlank(config.getInit()) && Double.parseDouble(config.getInit())==0) {
				tv_init.setText("每个月定投" + StringHelper.formatDecimal(config.getMonth()) + "元。");
			} else {
				tv_init.setText("一次性投入" + StringHelper.formatDecimal(config.getInit())
						+ "元，每个月定投" + StringHelper.formatDecimal(config.getMonth()) + "元。");
			}
			break;
		case Const.ASSEMBLE_HOUSE:

			if (Double.parseDouble(init) > Double.parseDouble(assemble.getHouseInitSum())) {
				tv_init.setText("建议您一次性投入金额增加到" + StringHelper.formatDecimal(init) + "元。");
			} else {
				tv_init.setText("一次性投入" + StringHelper.formatDecimal(assemble.getHouseInitSum()) + "元。");
			}
			break;
		case Const.ASSEMBLE_CHILDREN:

			tv_init.setText("建议您每月定投" + StringHelper.formatDecimal(config.getMonth()) + "元。");
			break;
		case Const.ASSEMBLE_MARRY:

			String married_init = assemble.getMarryInitSum();
			if (Double.parseDouble(init) > Double.parseDouble(married_init)) {
				tv_init.setText("建议您一次性投入金额增加到" + StringHelper.formatDecimal(init) + "元。");
			} else {
				tv_init.setText("一次性投入" + StringHelper.formatDecimal(married_init) + "元。");
			}
			break;
		case Const.ASSEMBLE_DREAM:

			String small_init = assemble.getDreamInitSum();
			String small_nmonth = assemble.getDreamMonths();
			tv_init.setText("一次性投入" + StringHelper.formatDecimal(init) + "元.");
			tv_profit.setText(small_nmonth + "个月后预计收益" + StringHelper.formatDecimal(config.getEarnings()) + "元.");
			break;
		}


		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				addInvestStatistics();

				// 判断登录状态
				if (UserManager.getInstance().getUser() == null) {
					Bundle bundle = new Bundle();
					bundle.putInt("LoginTarget", LoginTarget.ASSEMBLE.getValue());
					openActivity(LoginActivity.class, bundle);
					return;
				}
				
				if (listCard==null || listUnboundedCard==null) {
					showToast("处理银行卡数据发生错误.");
					return;
				}
				
				// 有银行卡则打开申购金额页
				if (!listCard.isEmpty()) {
					Intent intent = new Intent(AssembleConfigActivity.this, AssembleBuyActivity.class);
					intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_BUY);
					intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
					intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listAllCard);
					startActivityForResult(intent, ViewUtil.REQUEST_CODE);
					return;
				}

				// 新用户则提示绑卡
				if (listCard.isEmpty() && listUnboundedCard.isEmpty())
				{
					showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
					openActivity(QuickBillActivity.class);
					return;
				}
				
				// 判断是否需要还原卡
				if (listCard.isEmpty() && !listUnboundedCard.isEmpty())
				{
					final Card info = new Card();
					// 设置最大值
					info.setBoundTime(9223372036854775806L);
					if (listUnboundedCard.size() > 0) 
					{
						for(Card tempInfo : listUnboundedCard)
						{
							if (info.getBoundTime() > tempInfo.getBoundTime())
							{
								info.setNumber(tempInfo.getNumber());
								info.setBankName(tempInfo.getBankName());
								info.setBoundTime(tempInfo.getBoundTime());
							}
						}
						
						String strMsg = "您已经有解绑过的" + info.getBankName() + "卡，卡号为" + StringHelper.hintCardNum(info.getNumber()) + "，是否直接还原该卡？";
						
						showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 发送银行卡还原接口请求
								if (info.getNumber()!=null && !TextUtils.equals("", info.getNumber()))
								{
									Hashtable<String, Object> map = new Hashtable<String,Object>();
									map.put("cd", info.getNumber());
									AnsynHttpRequest.requestByPost(AssembleConfigActivity.this, UrlConst.CARD_HTYCARD, new HttpCallBack() {
										@Override
										public void back(String data, int type) {
											Message msg = new Message();
											msg.obj = data;
											msg.what = TYPE_CARD_HYCARD;
											mHandler.sendMessage(msg);
										}
									}, map);
									
									showLoading();
								}

								dialog.dismiss();
							}
						}, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
					}
				}
			}
		});

		btn_buy_virtual.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				addInvestStatistics();

				// 保存方案时如未登录先登录
				if (UserManager.getInstance().getUser() == null) {
					Bundle bundle = new Bundle();
					bundle.putInt("LoginTarget", LoginTarget.ASSEMBLE.getValue());
					openActivity(LoginActivity.class, bundle);
					return;
				}

				requsetVirtualTotalAssets();
			}
		});
		
	}


	/** 获取银行卡列表 */
	private void requestCardList() {

		showLoading();
		
		AnsynHttpRequest.requestByPost(AssembleConfigActivity.this, UrlConst.WALLET_CARD_LIST, new HttpCallBack() {
			@Override
			public void back(String data, int type) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = TYPE_CARD_LIST;
				mHandler.sendMessage(msg);
			}
		}, null);
	}
	
	/** 计算组合 */
	private void requestCalculate(final AssembleBase assemble) {

		showLoading();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		switch (assemble.getType()) {
		case Const.ASSEMBLE_INVEST:
			upload.put("type", "1"); // 组合类型
			upload.put("nm", ""); // 组合名
			upload.put("init", assemble.getInvestInitSum()); // 初始投资金额
			upload.put("rate", "0"); // 期望收益率
			upload.put("risk", assemble.getInvestRisk()); // 风险偏好
			upload.put("age", 0); // 年龄
			upload.put("retire", 0); // 退休年龄
			upload.put("month", "0"); // 月定投金额
			upload.put("money", "0"); // 目标金额
			upload.put("year", 0); // 投资年数
			upload.put("nmonth", 0); // 投资月数
			break;
		case Const.ASSEMBLE_PENSION:
			upload.put("type", "2");// 方案类型
			upload.put("nm", "");// 方案名
			upload.put("init", assemble.getPensionInitSum());// 初始投资金额
			upload.put("rate", "0");// 期望收益率
			upload.put("risk", "0");// 风险偏好
			upload.put("age", assemble.getPensionCurrentAge());// 年龄
			upload.put("retire", assemble.getPensionRetireAge());// 退休年龄
			upload.put("month", assemble.getPensionMonthAmount());// 月定投金额
			upload.put("money", "0");// 目标金额
			upload.put("year", 0);// 投资年数
			upload.put("nmonth", 0);// 投资月数
			break;
		case Const.ASSEMBLE_HOUSE:
			upload.put("type", "3");// 方案类型
			upload.put("nm", "");// 方案名
			upload.put("init", assemble.getHouseInitSum());// 初始投资金额
			upload.put("rate", "0");// 期望收益率
			upload.put("risk", "0");// 风险偏好
			upload.put("age", 0);// 年龄
			upload.put("retire", 0);// 退休年龄
			upload.put("month", "0");// 月定投金额
			upload.put("money", assemble.getHouseGoalSum());// 目标金额
			upload.put("year", assemble.getHouseYears());// 投资年数
			upload.put("nmonth", 0);// 投资月数
			break;
		case Const.ASSEMBLE_CHILDREN:
			upload.put("type", "4");// 方案类型
			upload.put("nm", "");// 方案名
			upload.put("init", "0");// 初始投资金额
			upload.put("rate", "0");// 期望收益率
			upload.put("risk", "0");// 风险偏好
			upload.put("age", 0);// 年龄
			upload.put("retire", 0);// 退休年龄
			upload.put("month", "0");// 月定投金额
			upload.put("money", assemble.getChildGoalSum());// 目标金额
			upload.put("year", assemble.getChildYears());// 投资年数
			upload.put("nmonth", 0);// 投资月数
			break;
		case Const.ASSEMBLE_MARRY:
			upload.put("type", "5");// 方案类型
			upload.put("nm", "");// 方案名
			upload.put("init", assemble.getMarryInitSum());// 初始投资金额
			upload.put("rate", "0");// 期望收益率
			upload.put("risk", "0");// 风险偏好
			upload.put("age", 0);// 年龄
			upload.put("retire", 0);// 退休年龄
			upload.put("month", "0");// 月定投金额
			upload.put("money", assemble.getMarryGoalSum());// 目标金额
			upload.put("year", assemble.getMarryYears());// 投资年数
			upload.put("nmonth", 0);// 投资月数
			break;
		case Const.ASSEMBLE_DREAM:
			upload.put("type", "6");// 方案类型
			upload.put("nm", "");// 方案名
			upload.put("init", assemble.getDreamInitSum());// 初始投资金额
			upload.put("rate", "0");// 期望收益率
			upload.put("risk", "0");// 风险偏好
			upload.put("age", 0);// 年龄
			upload.put("retire", 0);// 退休年龄
			upload.put("month", "0");// 月定投金额
			upload.put("money", "0");// 目标金额
			upload.put("year", "0");// 投资年数
			upload.put("nmonth", assemble.getDreamMonths());// 投资月数
			break;
		}
		AnsynHttpRequest.requestByPost(this, UrlConst.CAL_ASSEMBLY,
				new HttpCallBack() {
					@Override
					public void back(String data, int url) {
						Message msg = new Message();
						msg.what = TYPE_ASSEMBLE_CAL;
						msg.obj = data;
						mHandler.sendMessage(msg);
					}
				}, upload);
	}


	private void requestDeleteAssemble() {

		showLoading();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", mAssembleDetail.getAssembleBase().getSid());
		AnsynHttpRequest.requestByPost(this, UrlConst.REMOVE_ASSEMBLE,
				new HttpCallBack() {
					@Override
					public void back(String data, int url) {
						Message msg = new Message();
						msg.what = TYPE_ASSEMBLE_DELETE;
						msg.obj = data;
						mHandler.sendMessage(msg);
					}
				}, upload);
	}

	/**
	 * 虚拟总资产接口：VIRTUAL_TOTAL_ASSETS
	 * 虚拟列表接口：VIRTUAL_ASSETS
	 * */
	private void requsetVirtualTotalAssets(){
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_TOTAL_ASSETS, 
				new HttpCallBack() {
					@Override
					public void back(String data, int status) {
						Message msg = Message.obtain();
						msg.obj = data;
						msg.what = TYPE_ASSEMBLE_VIRTUAL_ASSETS;
						mHandler.sendMessage(msg);
					}
				}, null);
	}

	private static final int TYPE_ASSEMBLE_CAL = 1;
	private static final int TYPE_ASSEMBLE_DELETE = 2;
	private static final int TYPE_ASSEMBLE_VIRTUAL_ASSETS = 3;
	private static final int TYPE_CARD_LIST = 4;
	private static final int TYPE_CARD_HYCARD = 5;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strResponse = (String) msg.obj;
			switch (msg.what) {
			case TYPE_ASSEMBLE_CAL:
				handleAssembleCal(strResponse);
				break;
			case TYPE_ASSEMBLE_DELETE:
				handleDeleteResponse(strResponse);
				break;
			case TYPE_ASSEMBLE_VIRTUAL_ASSETS:
				handleVirtualAssets(strResponse);
				break;
			case TYPE_CARD_LIST:
				handCardListJson(strResponse);
				break;
			case TYPE_CARD_HYCARD:
				handHycardJson(strResponse);
				break;
			}
		}
	};

	protected void handleAssembleCal(String result) {

		dismissLoading();

		if (StringHelper.isBlank(result)) {
			showHintDialog("网络不给力！");
			return;
		}

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			String Succ = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");

			if (Succ.equals("0")) {
				String data = jsonObject.getString("data");
				AssembleDetail detail = Common.parseAssembleDetail(data);
				
				initAssembleDetail(detail);

			} else {
				showHintDialog(Errormsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("网络不给力！");
		}
	}


	private void handleDeleteResponse(String result) {

		dismissLoading();

		if (StringHelper.isBlank(result)) {
			showHintDialog("网络不给力");
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(result);
			String Succ = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");

			if (Succ.equals("0")) {

				setResult(ViewUtil.ASSEMBLE_DELETE_RESULT_CODE);
				finish();

			} else {
				showHintDialog(Errormsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("网络不给力");
		}
	}

	private void handleVirtualAssets(String jsonObj) {

		dismissLoading();

		if (StringHelper.isBlank(jsonObj)) {
			 showHintDialog(getString(R.string.net_prompt));
	         return;
		}
		 
		try {
			
			JSONObject jobj = new JSONObject(jsonObj);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			String data = jobj.optString("data");
			
			if (ecode == 0) {
				
				AssetsBean assets = new AssetsBean();
				JSONObject dataObj = new JSONObject(data);
				JSONObject assetsData = dataObj.optJSONObject("assets");
				
				com.qianjing.finance.model.virtual.Assets assetsInfo 
							= new com.qianjing.finance.model.virtual.Assets();
				
				assetsInfo.uid = assetsData.optString("uid");
				assetsInfo.sid = assetsData.optString("sid");
				assetsInfo.assets = (float) assetsData.optDouble("assets");
				assetsInfo.profitYestday = (float) assetsData.optDouble("profitYestday");
				assetsInfo.moditm = (float) assetsData.optDouble("moditm");
				assetsInfo.unpaid = (float) assetsData.optDouble("unpaid");
				assetsInfo.invest = (float) assetsData.optDouble("invest");
				assetsInfo.income = (float) assetsData.optDouble("income");
				assetsInfo.subscripting = (float) assetsData.optDouble("subscripting");
				assetsInfo.redemping = (float) assetsData.optDouble("redemping");
				assetsInfo.profit = (float) assetsData.optDouble("profit");
				assetsInfo.usable_assets = (float) assetsData.optDouble("usable_assets");
				assetsInfo.total_assets = assetsData.optString("total_assets");
				assets.setAssets(assetsInfo);
				
				Intent intent = new Intent();
				PurchaseModel pModel = new PurchaseModel();
				pModel.setVirtual(true);
				pModel.setUsableMonay(assets.assets.usable_assets);
				intent.putExtra("purchaseInfo", pModel);
				intent.putExtra(ViewUtil.ASSEMBLE_NAME_FLAG, ViewUtil.ASSEMBLE_VIRTUAL_NAME_SAVE);
				intent.putParcelableArrayListExtra("fundList", mAssembleDetail.getAssembleConfig().getFundList());
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE));
				
				intent.setClass(this, PurchaseBuyActivity.class);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
				
			} else {
				showHintDialog(emsg);
			}
			
		} catch (JSONException e) {
			showHintDialog(getString(R.string.net_data_error));
			WriteLog.recordLog(e.toString());
		}
	}


	// 处理银行卡列表数据
	private void handCardListJson(String jsonStr) {
		
        dismissLoading();
		
		if (StringHelper.isBlank(jsonStr)) {
            showHintDialog("网络不给力");
            return;
        }
		
		try {
			JSONObject object = new JSONObject(jsonStr);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String data = object.optString("data");
			if (ecode == 0) {
				JSONObject objData = new JSONObject(data);
				
				// 解析所有银行卡信息存入List
				// 临时措施，后续需要合并银行卡存放方式
				listAllCard = Common.parseCardList(objData);
				
				// 解析已绑定银行卡数据
				JSONArray arrayCard = objData.getJSONArray("cards");
				
				for (int i = 0; i < arrayCard.length(); i++) {
					JSONObject jsonOb = (JSONObject) arrayCard.opt(i);
					String bank = jsonOb.optString("bank");
					String card = jsonOb.optString("card");
					Card info = new Card();
					info.setNumber(card);
					info.setBankName(bank);
					listCard.add(info);
				}
				
				// 解析已解绑银行卡数据
				JSONArray arrayUnboundCard = objData.getJSONArray("unbc");
				for (int i = 0; i < arrayUnboundCard.length(); i++) {
					JSONObject jsonOb = (JSONObject) arrayUnboundCard.opt(i);
					String bank = jsonOb.optString("bank");
					String card = jsonOb.optString("card");
					long boundTime = Long.valueOf(jsonOb.optString("boundT"));
					Card info = new Card();
					info.setNumber(card);
					info.setBankName(bank);
					info.setBoundTime(boundTime);
					listUnboundedCard.add(info);
				}
			} else {
				showHintDialog(emsg);
			}

		} catch (JSONException e) {
			showHintDialog("数据解析错误");
        	WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
		}
	}

	// 处理银行卡还原数据
	private void handHycardJson(String strJson) {

        dismissLoading();

		if (StringHelper.isBlank(strJson)) {
            showHintDialog("网络不给力");
            return;
        }
		
		try {
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			if (ecode == 0) 
			{
				showToast("绑卡成功");
				
				// 重新刷新银行卡列表
				requestCardList();
				
				listCard.clear();
			} 
			else {
				showHintDialog(emsg);
			}
		} 
		catch (JSONException e) {
			showHintDialog("数据解析错误");
        	WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 修改名称
		if (resultCode == ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE) {
			if (data!=null && data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL)!=null) {
				mAssembleDetail = data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
				setTitleWithString(mAssembleDetail.getAssembleBase().getName());
				setResult(ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE);
			}
		} 
		else if (resultCode == ViewUtil.RESULT_CODE) { // 修改目标
//			setHasSave();
//			AssembleBase schemaInfo = data.getParcelableExtra("schemaInfo");
//			setPs(schemaInfo.getType());
		}
		else if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) { // 申购结果返回
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, data);
			finish();
		}
	}

	private void addEnterStatistics() {
		if (mAssembleDetail!=null && mAssembleDetail.getAssembleBase()!=null) {
			int type = mAssembleDetail.getAssembleBase().getType();
			if (type == Const.ASSEMBLE_HOUSE)
				UMengStatics.onGoufangPage3View(this);
			else if (type == Const.ASSEMBLE_MARRY)
				UMengStatics.onJiehunPage3View(this);
			else if (type == Const.ASSEMBLE_INVEST)
				UMengStatics.onLicaiPage3View(this);
			else if (type == Const.ASSEMBLE_DREAM)
				UMengStatics.onDreamPage3View(this);
			else if (type == Const.ASSEMBLE_PENSION)
				UMengStatics.onPensionPage3View(this);
			else if (type == Const.ASSEMBLE_CHILDREN)
				UMengStatics.onChildrenPage3View(this);
		}
	}
	
	private void addInvestStatistics() {
		if (mAssembleDetail!=null && mAssembleDetail.getAssembleBase()!=null) {
			int type = mAssembleDetail.getAssembleBase().getType();
			if (type == Const.ASSEMBLE_HOUSE)
				UMengStatics.onGoufangPage3Click(this);
			else if (type == Const.ASSEMBLE_MARRY)
				UMengStatics.onJiehunPage3Click(this);
			else if (type == Const.ASSEMBLE_INVEST)
				UMengStatics.onLicaiPage3Click(this);
			else if (type == Const.ASSEMBLE_DREAM)
				UMengStatics.onDreamPage3Click(this);
			else if (type == Const.ASSEMBLE_PENSION)
				UMengStatics.onPensionPage3Click(this);
			else if (type == Const.ASSEMBLE_CHILDREN)
				UMengStatics.onChildrenPage3Click(this);
		}
	}
	
}
