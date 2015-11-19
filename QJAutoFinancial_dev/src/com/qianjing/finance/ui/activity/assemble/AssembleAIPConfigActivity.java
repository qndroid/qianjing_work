
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleConfig;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.assemble.InvestPlan;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.quickbuy.FundItemDetails;
import com.qianjing.finance.model.quickbuy.QuickBuyDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.assemble.aip.AssembleAIPBuyActivity;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.fragment.details.AdviceFragment;
import com.qianjing.finance.ui.fragment.details.ConfigFragment;
import com.qianjing.finance.util.Common;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.AnimHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.FloatScrollView;
import com.qianjing.finance.view.dialog.ActionSheet;
import com.qianjing.finance.view.dialog.ActionSheet.OnSheetItemClickListener;
import com.qianjing.finance.view.slidingtabstrip.PagerSlidingTabStrip;
import com.qjautofinancial.R;

/**
 * @description 投资计划组合配置详情界面
 * @author liuchen
 * @date 2015年9月6日
 */

public class AssembleAIPConfigActivity extends BaseActivity implements OnClickListener, FloatScrollView.ScrollViewListener {

	/**
	 * UI
	 */
	private PagerSlidingTabStrip pstsTabs;
	private ImageView imageRight;
	private RelativeLayout btn_submit;
	private DisplayMetrics dm;
	private ViewPager vpPager;
	private FloatScrollView fsvScroll;
	private LinearLayout llRoot;
	private TextView tvMinPurchase;
	private TextView tvRiskTxt;
	private TextView tvProfit;
	private TextView tvProfitTxt;
	private ImageView riskExplain;
	private LinearLayout bottomItem;
	private LinearLayout llBottom;

	private CheckBox planOne;
	private CheckBox planTwo;
	private CheckBox planThree;
	private TextView planTitle;
	private TextView planContent;
	private TextView expectProfit;
	private LinearLayout llAdvice;
	private LinearLayout dreamBottom;
	private LinearLayout adviceCheck;
	private LinearLayout llFirst;
	private LinearLayout llValue;
	private LinearLayout llYear;
	private TextView tvFirst;
	private TextView tvValue;
	private TextView tvYear;

	/**
	 * data
	 */
	private boolean isFixedInvest = false;
	private boolean isScroll = false;
	private QuickBuyDetail quickDetails;
	private AssembleDetail detail;
	private InvestPlan investPlan;
	private AssembleDetail mAssembleDetail;
	private int llBottonY;
	private int llBottomH;
	private int dBottonY;
	private int dBottomH;
	private int assembleType;
	/** 有效银行卡容器 */
	private ArrayList<Card> listCard = new ArrayList<Card>();
	/** 已解绑银行卡容器 */
	private ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
	/** 全部银行卡容器 */
	private ArrayList<Card> listAllCard = new ArrayList<Card>();
	/**
	 * static
	 */
	private static final int TYPE_ASSEMBLE_CAL = 1;
	private static final int TYPE_ASSEMBLE_DELETE = 2;
	private static final int TYPE_CARD_LIST = 4;
	private static final int TYPE_CARD_HYCARD = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addEnterStatistics();
		initView();
		initDataView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (UserManager.getInstance().getUser() != null) {
			requestCardList();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		int pstsTabsHeight = pstsTabs.getHeight();
		int fsvScrollHeight = fsvScroll.getHeight();
		int bottomHeight = bottomItem.getHeight();
		LayoutParams layoutParams = vpPager.getLayoutParams();
		layoutParams.height = fsvScrollHeight - pstsTabsHeight - bottomHeight;
		vpPager.setLayoutParams(layoutParams);

		initBottomPosition();

		super.onWindowFocusChanged(hasFocus);
	}

	private void initView() {
		setContentView(R.layout.activity_assemble_config_2_1);
		setTitleBack();
		// setLoadingUncancelable();

		dm = getResources().getDisplayMetrics();

		fsvScroll = (FloatScrollView) findViewById(R.id.fsv_scroll);
		llRoot = (LinearLayout) findViewById(R.id.ll_fsv_root);
		pstsTabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
		vpPager = (ViewPager) findViewById(R.id.vp_pager);
		fsvScroll.setFloatView(pstsTabs, vpPager);
		bottomItem = (LinearLayout) findViewById(R.id.ll_bottom_item);
		llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
		llAdvice = (LinearLayout) findViewById(R.id.ll_advice);

		adviceCheck = (LinearLayout) findViewById(R.id.ll_advice_check);
		planOne = (CheckBox) findViewById(R.id.cb_plan_one);
		planTwo = (CheckBox) findViewById(R.id.cb_plan_two);
		planThree = (CheckBox) findViewById(R.id.cb_plan_three);
		planTitle = (TextView) findViewById(R.id.tv_invest_plan_title);
		planContent = (TextView) findViewById(R.id.tv_invest_plan_content);
		expectProfit = (TextView) findViewById(R.id.tv_expect_profit);

		planOne.setOnClickListener(this);
		planTwo.setOnClickListener(this);
		planThree.setOnClickListener(this);

		tvMinPurchase = (TextView) findViewById(R.id.tv_min_purchase);
		tvRiskTxt = (TextView) findViewById(R.id.tv_risk_txt);
		tvProfit = (TextView) findViewById(R.id.tv_profit);
		tvProfitTxt = (TextView) findViewById(R.id.tv_profit_txt);
		riskExplain = (ImageView) findViewById(R.id.iv_risk_explain);

		ImageView ivExplain = (ImageView) findViewById(R.id.iv_explain);
		// invest_plan_expect_explain

		imageRight = (ImageView) findViewById(R.id.title_right_image);
		imageRight.setImageResource(R.drawable.title_edit);
		imageRight.setOnClickListener(this);

		llFirst = (LinearLayout) findViewById(R.id.ll_first);
		llValue = (LinearLayout) findViewById(R.id.ll_value);
		llYear = (LinearLayout) findViewById(R.id.ll_year);

		tvFirst = (TextView) findViewById(R.id.tv_first);
		tvValue = (TextView) findViewById(R.id.tv_value);
		tvYear = (TextView) findViewById(R.id.tv_year);

		btn_submit = (RelativeLayout) findViewById(R.id.btn_submit);
		dreamBottom = (LinearLayout) findViewById(R.id.ll_bottom_button);
		TextView buyButton = (TextView) findViewById(R.id.start_buy_btn);

		ivExplain.setOnClickListener(this);
		riskExplain.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		buyButton.setOnClickListener(this);
		fsvScroll.setOnScrollViewListener(this);
	}

	private void initDataView() {
		int from = getIntent().getIntExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, ViewUtil.FROM_FIND);
		if (from == ViewUtil.FROM_FIND)
			initFindView();
		else if (from == ViewUtil.FROM_ASSETS)
			initMyAssets();

		if (assembleType == Const.ASSEMBLE_DREAM) {
			llAdvice.setVisibility(View.GONE);
			dreamBottom.setVisibility(View.VISIBLE);
			llBottom.setVisibility(View.GONE);
		} else {
			llBottom.setVisibility(View.VISIBLE);
			dreamBottom.setVisibility(View.GONE);
		}

		initPlanView();
	}

	/**
	 * 从发现页进入的页面初始化
	 */
	private void initFindView() {

		imageRight.setVisibility(View.GONE);
		// btn_buy_virtual.setVisibility(View.VISIBLE);
		// imageRight.setVisibility(View.VISIBLE);
		AssembleBase assemble = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE);
		assembleType = assemble.getType();
		requestCalculate(assemble);
	}

	/**
	 * 从我的资产进入的页面初始化
	 */
	private void initMyAssets() {

		imageRight.setVisibility(View.VISIBLE);
		// 隐藏虚拟投资按钮
		// btn_buy_virtual.setVisibility(View.GONE);

		AssembleDetail detail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		assembleType = detail.getAssembleBase().getType();
		initAssembleDetail(detail);
	}

	/** 
	* @description 初始化投资计划view,设置投资计划显隐类型
	* @author liuchen
	*/
	private void initPlanView() {
		View adviceTop = findViewById(R.id.view_advice_top);
		switch (assembleType) {

		case Const.ASSEMBLE_INVEST:

			break;
		case Const.ASSEMBLE_PENSION:
			if (getIntent().getBooleanExtra("hasInit", true)) {
				planTitle.setText(getString(R.string.invest_plan_two_title));
			} else {
				planTitle.setText(getString(R.string.invest_plan_three_title));
			}
			adviceTop.setVisibility(View.VISIBLE);
			adviceCheck.setVisibility(View.GONE);
			break;
		case Const.ASSEMBLE_HOUSE:
			planTitle.setText(getString(R.string.invest_plan_one_title));
			planOne.setChecked(true);
			break;
		case Const.ASSEMBLE_CHILDREN:
			planTitle.setText(getString(R.string.invest_plan_three_title));
			adviceTop.setVisibility(View.VISIBLE);
			adviceCheck.setVisibility(View.GONE);
			break;
		case Const.ASSEMBLE_MARRY:
			planTitle.setText(getString(R.string.invest_plan_one_title));
			planOne.setChecked(true);
			break;
		case Const.ASSEMBLE_DREAM:
			break;
		}
	}

	/** 
	* @description 初始化投资计划数据
	* @author liuchen
	*/
	private void initPlan() {

		if (detail.getAssembleBase().getPlan1() == null) {
			planOne.setVisibility(View.GONE);
		}
		if (detail.getAssembleBase().getPlan2() == null) {
			planTwo.setVisibility(View.GONE);
			planThree.setText("计划二");
		}
		if (detail.getAssembleBase().getPlan3() == null) {
			planThree.setVisibility(View.GONE);
		}

		switch (assembleType) {

		case Const.ASSEMBLE_INVEST:

			break;
		case Const.ASSEMBLE_PENSION:

			if (getIntent().getBooleanExtra("hasInit", true)) {
				initPlanData(2);
			} else {
				initPlanData(3);
			}

			break;
		case Const.ASSEMBLE_HOUSE:
			planOne.setChecked(true);
			initPlanData(1);
			break;
		case Const.ASSEMBLE_CHILDREN:
			planOne.setVisibility(View.GONE);
			planTwo.setVisibility(View.GONE);
			planThree.setChecked(true);
			initPlanData(3);
			break;
		case Const.ASSEMBLE_MARRY:
			planOne.setChecked(true);
			initPlanData(1);
			break;
		case Const.ASSEMBLE_DREAM:
			break;
		}
	}

	/**
	 * @param planType
	 */
	private void initPlanData(int planType) {

		if (detail == null) {
			return;
		}

		try {
			switch (planType) {
			case 1:
				isFixedInvest = false;
				investPlan = detail.getAssembleBase().getPlan1();
				if (detail.getAssembleBase().getPlan1() == null) {
					return;
				}

				planTitle.setText(getString(R.string.invest_plan_one_title));
				planContent.setText(String.format(getString(R.string.invest_plan_one), StringHelper.formatDecimal(investPlan.getOne_invest())));
				expectProfit.setText(investPlan.getExcept_min() + "-" + StringHelper.formatDecimal(investPlan.getExcept_max()));

				llFirst.setVisibility(View.VISIBLE);
				llValue.setVisibility(View.GONE);
				llYear.setVisibility(View.GONE);
				tvFirst.setText(StringHelper.formatDecimal(investPlan.getOne_invest()) + "元");
				break;
			case 2:
				isFixedInvest = true;
				investPlan = detail.getAssembleBase().getPlan2();
				if (detail.getAssembleBase().getPlan2() == null) {
					return;
				}

				planTitle.setText(getString(R.string.invest_plan_two_title));
				planContent.setText(String.format(getString(R.string.invest_plan_two), StringHelper.formatDecimal(investPlan.getOne_invest()),
						StringHelper.formatDecimal(investPlan.getMonth_fixed()), investPlan.getInvest_year()));
				expectProfit.setText(StringHelper.formatDecimal(investPlan.getExcept_min()) + "-"
						+ StringHelper.formatDecimal(investPlan.getExcept_max()));

				llFirst.setVisibility(View.VISIBLE);
				llValue.setVisibility(View.VISIBLE);
				llYear.setVisibility(View.VISIBLE);
				tvFirst.setText(StringHelper.formatDecimal(investPlan.getOne_invest()) + "元");
				tvValue.setText(StringHelper.formatDecimal(investPlan.getMonth_fixed()) + "元");
				tvYear.setText(investPlan.getInvest_year() + "年");
				break;
			case 3:
				isFixedInvest = true;
				investPlan = detail.getAssembleBase().getPlan3();
				if (detail.getAssembleBase().getPlan3() == null) {
					return;
				}
				planTitle.setText(getString(R.string.invest_plan_three_title));
				planContent.setText(String.format(getString(R.string.invest_plan_three), StringHelper.formatDecimal(investPlan.getMonth_fixed()),
						investPlan.getInvest_year()));
				expectProfit.setText(StringHelper.formatDecimal(investPlan.getExcept_min()) + "-"
						+ StringHelper.formatDecimal(investPlan.getExcept_max()));

				llFirst.setVisibility(View.GONE);
				llValue.setVisibility(View.VISIBLE);
				llYear.setVisibility(View.VISIBLE);
				tvValue.setText(StringHelper.formatDecimal(investPlan.getMonth_fixed()) + "元");
				tvYear.setText(investPlan.getInvest_year() + "年");

				break;
			}
		} catch (NumberFormatException e) {
			showToast("数据解析异常");
		}

	}

	/** 
	* @description 获取对应的Fragment
	* @author liuchen
	* @param index
	* @return
	*/
	private Fragment getFragment(int index) {
		Fragment fragment = null;
		switch (index) {
		case 0:
			ConfigFragment configFragment = new ConfigFragment();
			if (quickDetails != null) {
				configFragment.setFundDetails(quickDetails);
			}
			fragment = configFragment;
			break;
		case 1:
			AdviceFragment adviceFragment = new AdviceFragment();
			if (quickDetails != null) {
				adviceFragment.setAdviceText(quickDetails.common);
			}
			fragment = adviceFragment;
			break;
		}
		return fragment;
	}

	private void setTabsValue() {
		pstsTabs.setShouldExpand(true);
		pstsTabs.setDividerColor(getResources().getColor(R.color.color_00ffffff));
		pstsTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		pstsTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm));
		pstsTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		pstsTabs.setIndicatorColor(getResources().getColor(R.color.low_stroke));
		pstsTabs.setTabBackground(0);
	}

	/** 
	* @description 波动指数说明
	* @author liuchen
	*/
	private void showRiskExplain() {
		showHintDialog(getString(R.string.fen_xian_zhi_shu_shuo_ming), getString(R.string.fen_xian_zhi_shu_shuo_ming_nei_rong),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}, getString(R.string.zhi_dao_l));
	}

	/**
	 * @description 右上角组合操作按钮
	 * @author liuchen
	 */
	private void showAssembleMenu() {
		ActionSheet actionSheet = new ActionSheet(AssembleAIPConfigActivity.this).build().setCancleable(false).setCancleOnTouchOutside(true);

		actionSheet.addSheetItem("修改名称", getResources().getColor(R.color.actionsheet_blue), new OnSheetItemClickListener() {

			@Override
			public void onClick(int which) {
				Intent intent = new Intent(AssembleAIPConfigActivity.this, AssembleModifyNameActivity.class);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			}
		});
		actionSheet.addSheetItem("删除组合", getResources().getColor(R.color.actionsheet_blue), new OnSheetItemClickListener() {

			@Override
			public void onClick(int which) {
				requestDeleteAssemble();
			}
		});
		actionSheet.show();
	}

	/** 
	* @description 预计到期本息说明
	* @author liuchen
	*/
	private void showInvestPlanExplain() {
		showHintDialog(getString(R.string.invest_plan_expect), getString(R.string.invest_plan_expect_explain), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}, getString(R.string.zhi_dao_l));
	}

	/**
	 * @description 初始化组合详情数据
	 * @author fangyan
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
		} else
			setTitleWithString(assemble.getName());

	}

	private void jump2BuyFilter() {
		addInvestStatistics();

		// 判断登录状态
		if (UserManager.getInstance().getUser() == null) {
			Bundle bundle = new Bundle();
			bundle.putInt("LoginTarget", LoginTarget.ASSEMBLE.getValue());
			openActivity(LoginActivity.class, bundle);
			return;
		}

		if (listCard == null || listUnboundedCard == null) {
			showToast("处理银行卡数据发生错误.");
			return;
		}

		// 有银行卡则打开申购金额页
		if (!listCard.isEmpty()) {

			if (isFixedInvest) {
				Intent intent = new Intent(AssembleAIPConfigActivity.this, AssembleAIPBuyActivity.class);
				intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_BUY);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
				intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listAllCard);
				intent.putExtra(Flag.EXTRA_BEAN_INVESTPLAN, investPlan);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			} else {
				Intent intent = new Intent(AssembleAIPConfigActivity.this, AssembleBuyActivity.class);
				intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_BUY);
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, mAssembleDetail);
				intent.putExtra(Flag.EXTRA_BEAN_CARD_LIST, listAllCard);
				startActivityForResult(intent, ViewUtil.REQUEST_CODE);
			}
			return;
		}

		// 新用户则提示绑卡
		if (listCard.isEmpty() && listUnboundedCard.isEmpty()) {
			showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
			openActivity(QuickBillActivity.class);
			return;
		}

		// 判断是否需要还原卡
		if (listCard.isEmpty() && !listUnboundedCard.isEmpty()) {
			final Card info = new Card();
			// 设置最大值
			info.setBoundTime(9223372036854775806L);
			if (listUnboundedCard.size() > 0) {
				for (Card tempInfo : listUnboundedCard) {
					if (info.getBoundTime() > tempInfo.getBoundTime()) {
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
						if (info.getNumber() != null && !TextUtils.equals("", info.getNumber())) {
							Hashtable<String, Object> map = new Hashtable<String, Object>();
							map.put("cd", info.getNumber());
							AnsynHttpRequest.requestByPost(AssembleAIPConfigActivity.this, UrlConst.CARD_HTYCARD, new HttpCallBack() {
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
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}
		}
	}

	/** 
	* @description 获取银行卡列表请求
	*/
	private void requestCardList() {

		showLoading();

		AnsynHttpRequest.requestByPost(AssembleAIPConfigActivity.this, UrlConst.WALLET_CARD_LIST, new HttpCallBack() {
			@Override
			public void back(String data, int type) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = TYPE_CARD_LIST;
				mHandler.sendMessage(msg);
			}
		}, null);
	}

	/** 
	* @description 计算组合请求
	* @param assemble
	*/
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
		AnsynHttpRequest.requestByPost(this, UrlConst.CAL_ASSEMBLY, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = TYPE_ASSEMBLE_CAL;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	/** 
	* @description 删除组合请求
	* 
	*/
	private void requestDeleteAssemble() {

		showLoading();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", mAssembleDetail.getAssembleBase().getSid());
		AnsynHttpRequest.requestByPost(this, UrlConst.REMOVE_ASSEMBLE, new HttpCallBack() {
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
	* @description 解析组合详情数据
	* @param result
	*/
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
				JSONObject data = jsonObject.getJSONObject("data");
				ResponseBase responseBase = new ResponseBase();
				responseBase.ecode = 0;
				responseBase.jsonObject = data;

				detail = ParseUtil.parseAssembleDetail(responseBase);
				initAssembleViewData(detail);
				initAssembleDetail(detail);

				tvProfit.setText(StringHelper.formatDecimal(data.optJSONObject("assembly").optDouble("yield")) + "%");
				tvProfitTxt.setText(data.optJSONObject("assembly").optString("yield_text"));
				tvRiskTxt.setText(data.optJSONObject("assembly").optString("risk_text"));
				// tvMinPurchase.setText("¥"
				// + data.optJSONObject("schema").optJSONObject("limit").optString("init")
				// + "起购");

				initPlan();

			} else {
				showHintDialog(Errormsg);
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showHintDialog("网络不给力！");
		}
	}

	/** 
	* @description 将组合数据转为快速组合详情数据
	* @param detail
	* @return
	*/
	public QuickBuyDetail convertAssembleDetailToQuick(AssembleDetail detail) {
		QuickBuyDetail quickDetails = new QuickBuyDetail();

		quickDetails.common = detail.getAssembleConfig().getComment();
		quickDetails.noStockName = detail.getAssembleConfig().getStrNonStock();
		quickDetails.stockName = detail.getAssembleConfig().getStrStock();
		quickDetails.noStockRatio = detail.getAssembleConfig().getRatioNonStock();
		quickDetails.stockRatio = detail.getAssembleConfig().getRatioStock();

		ArrayList<Fund> fundList = detail.getAssembleConfig().getFundList();
		for (int i = 0; i < fundList.size(); i++) {
			Fund fund = fundList.get(i);
			FundItemDetails funeItemDetail = new FundItemDetails();
			funeItemDetail.abbrev = fund.name;
			funeItemDetail.fdcode = fund.code;
			funeItemDetail.rank = fund.rank;
			funeItemDetail.ratio = fund.ratio;
			funeItemDetail.total_rank = fund.total_rank;
			funeItemDetail.recomm_reason = fund.recomm_reason;
			funeItemDetail.star = fund.star;
			quickDetails.list.add(funeItemDetail);
		}

		return quickDetails;
	}

	private void initAssembleViewData(AssembleDetail detail) {
		quickDetails = convertAssembleDetailToQuick(detail);
		if (quickDetails != null) {
			vpPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
			pstsTabs.setViewPager(vpPager);
			setTabsValue();
		}
	}

	/** 
	* @description 解析删除组合信息
	* @param result
	*/
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

	/** 
	* @description  处理银行卡列表数据
	* @param jsonStr
	*/
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

				listAllCard = Common.parseCardList(objData);

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

	/** 
	* @description 解析银行卡还原信息
	* @param strJson
	*/
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
			if (ecode == 0) {
				showToast("绑卡成功");

				// 重新刷新银行卡列表
				requestCardList();

				listCard.clear();
			} else {
				showHintDialog(emsg);
			}
		} catch (JSONException e) {
			showHintDialog("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}

	/**
	 * 处理上一个页面返回信息
	 * @see com.qianjing.finance.ui.activity.common.BaseActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 修改名称
		if (resultCode == ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE) {
			if (data != null && data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL) != null) {
				mAssembleDetail = data.getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
				setTitleWithString(mAssembleDetail.getAssembleBase().getName());
				setResult(ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE);
			}
		} else if (resultCode == ViewUtil.RESULT_CODE) { // 修改目标
			// setHasSave();
			// AssembleBase schemaInfo = data.getParcelableExtra("schemaInfo");
			// setPs(schemaInfo.getType());
		} else if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) { // 普通申购结果返回
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE, data);
			finish();
		} else if (resultCode == ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE) { // 定投申购结果返回
			setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE, data);
			finish();
		}
	}

	/** 
	* @description 友盟统计
	* @author fangyan
	*/
	private void addEnterStatistics() {
		if (mAssembleDetail != null && mAssembleDetail.getAssembleBase() != null) {
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

	/** 
	 * @description 友盟统计
	 * @author fangyan
	 */
	private void addInvestStatistics() {
		if (mAssembleDetail != null && mAssembleDetail.getAssembleBase() != null) {
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

	/** 
	* @fields 该页面的Handler实例
	*/
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strResponse = (String) msg.obj;
			switch (msg.what) {
			case TYPE_ASSEMBLE_CAL:
				//                    LogUtils.syso("未投资详情：" + strResponse);
				handleAssembleCal(strResponse);
				break;
			case TYPE_ASSEMBLE_DELETE:
				handleDeleteResponse(strResponse);
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

	/**
	 * 处理页面点击事件
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.cb_plan_one:
			planOne.setChecked(true);
			planTwo.setChecked(false);
			planThree.setChecked(false);
			initPlanData(1);
			break;
		case R.id.cb_plan_two:
			planOne.setChecked(false);
			planTwo.setChecked(true);
			planThree.setChecked(false);
			initPlanData(2);
			break;
		case R.id.cb_plan_three:
			planOne.setChecked(false);
			planTwo.setChecked(false);
			planThree.setChecked(true);
			initPlanData(3);
			break;
		case R.id.iv_risk_explain:
			showRiskExplain();
			break;
		case R.id.iv_explain:
			showInvestPlanExplain();
			break;
		case R.id.title_right_image:
			showAssembleMenu();
			break;
		case R.id.btn_submit:
		case R.id.start_buy_btn:
			jump2BuyFilter();
			break;
		}
	}

	private void initBottomPosition() {

		llBottonY = (int) llBottom.getY();
		llBottomH = llBottom.getHeight();

		dBottonY = (int) dreamBottom.getY();
		dBottomH = dreamBottom.getHeight();

	}

	/**
	 * 监听页面滑动事件，从而改变动画状态
	 * @see com.qianjing.finance.view.custom.AnimLScrollView.ScrollViewListener#onScrollChanged(android.widget.ScrollView, int, int, int, int)
	 */
	@Override
	public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
		if (!isScroll) {
			isScroll = true;
			//            Animation loadAnimation = AnimationUtils
			//                    .loadAnimation(AssembleAIPConfigActivity.this, R.anim.actionsheet_out);

			if (assembleType == Const.ASSEMBLE_DREAM) {
				//                dreamBottom.startAnimation(loadAnimation);
				AnimHelper.loadPropertyTransAnim(dreamBottom, AnimHelper.AnimType.TRANS_Y, dBottonY + dBottomH);
			} else {
				//                llBottom.startAnimation(loadAnimation);
				AnimHelper.loadPropertyTransAnim(llBottom, AnimHelper.AnimType.TRANS_Y, llBottonY + llBottomH);
			}
		}
	}

	/**
	 * 监听页面停止事件，从而改变动画状态
	 * @see com.qianjing.finance.view.custom.AnimLScrollView.ScrollViewListener#onScrollChanged(android.widget.ScrollView, int, int, int, int)
	 */
	@Override
	public void onScrollViewStop() {

		if (isScroll) {
			//            Animation loadAnimation = AnimationUtils
			//                    .loadAnimation(AssembleAIPConfigActivity.this, R.anim.actionsheet_in);
			isScroll = false;
			if (assembleType == Const.ASSEMBLE_DREAM) {
				AnimHelper.loadPropertyTransAnim(dreamBottom, AnimHelper.AnimType.TRANS_Y, dBottonY);
				//                dreamBottom.startAnimation(loadAnimation);
			} else {
				AnimHelper.loadPropertyTransAnim(llBottom, AnimHelper.AnimType.TRANS_Y, llBottonY);
				//                llBottom.startAnimation(loadAnimation);
			}
		}
	}

	/** 
	* @description 内部类
	* @author liuchen
	* @date 2015年9月24日
	*/

	private class ViewPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int arg0) {
			return getFragment(arg0);
		}

		@Override
		public View getPageView(int position) {

			View view = View.inflate(AssembleAIPConfigActivity.this, R.layout.item_assemble_tab, null);
			ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);

			if (position == 0) {
				ivIcon.setBackgroundResource(R.drawable.selector_icon_details);
				tvTitle.setText(getString(R.string.configure_detail));
			} else {
				ivIcon.setBackgroundResource(R.drawable.selector_icon_advice);
				tvTitle.setText(getString(R.string.expert_advice));
			}
			return view;
		}
	}

}
