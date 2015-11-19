package com.qianjing.finance.ui.activity.wallet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.response.ResponseManager;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.activity.history.WalletHistoryActivity;
import com.qianjing.finance.util.Network;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.dialog.CommonDialog;
import com.qianjing.finance.view.dialog.CommonDialog.DialogClickListener;
import com.qianjing.finance.view.runnabletextview.RunningTextView;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.qianjing.finance.widget.pulltorefresh.PullToRefreshScrollView;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 活期宝主页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class WalletActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private CommonDialog riskDialog;
	private Button backBtn;
	private TextView fromWalletView;
	private TextView totalWalletView;
	private TextView titleView;
	private TextView walletBalanceView;
	private TextView walletProfitView;
	private TextView abbrevView;
	private TextView unpaidView;
	private TextView dateBeforeView;
	private TextView profitMillionView;
	private RunningTextView yesterdayProfitView;
	private ImageView helpView;
	private TextView qiriRateView;
	private RelativeLayout relativeLayout;
	private RelativeLayout yesterdayLayout;
	private RelativeLayout profitLayout;
	private RelativeLayout totalMoneyLayout;
	private RelativeLayout unPaidLayout;
	private TextView canGetMoneyView;
	private RelativeLayout canGetMoneyLayout;
	private PullToRefreshScrollView pullScrollView;
	/**
	 * 图表相关UI
	 */
	private LinearLayout contentLayout;
	private LineChart lineView;
	private Resources resource;
	private float yAxisMax = -1.0f;
	private float yAxisMin = 100.0f;
	private float yAxisGap = 0.1f;
	private int yAxislabelNum = 5;
	/**
	 * data
	 */
	private int isWorkday; // 是否工作日
	private String strTimeProfitBefore;
	private String strProfitTemp;
	private String fdCode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		UMengStatics.onCurrentPage0Click(this);

		setContentView(R.layout.activity_wallet_new);
		initView();
		requestWallet(true);
		requestYeild();
	}

	private void initView()
	{
		resource = getResources();
		// 设置默认加载框不可取消
		setLoadingUncancelable();

		titleView = (TextView) findViewById(R.id.title_middle_text);
		titleView.setText(R.string.str_wallettitle);
		yesterdayProfitView = (RunningTextView) findViewById(R.id.tv_profit_date_before);
		qiriRateView = (TextView) findViewById(R.id.text_qiri_rate);
		backBtn = (Button) findViewById(R.id.bt_back);
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(this);
		helpView = (ImageView) findViewById(R.id.im_help);
		helpView.setVisibility(View.VISIBLE);
		helpView.setOnClickListener(this);
		fromWalletView = (TextView) findViewById(R.id.bt_fromwallet);
		totalWalletView = (TextView) findViewById(R.id.bt_towallet);
		walletBalanceView = (TextView) findViewById(R.id.tv_assets);
		abbrevView = (TextView) findViewById(R.id.tv_abbrev);
		walletProfitView = (TextView) findViewById(R.id.tv_profit);
		unpaidView = (TextView) findViewById(R.id.tv_profit_unpaid);
		dateBeforeView = (TextView) findViewById(R.id.tv_date_before);
		profitMillionView = (TextView) findViewById(R.id.tv_profit_million);
		yesterdayLayout = (RelativeLayout) findViewById(R.id.total_layout);
		profitLayout = (RelativeLayout) findViewById(R.id.profit_layout);
		totalMoneyLayout = (RelativeLayout) findViewById(R.id.total_money_layout);
		unPaidLayout = (RelativeLayout) findViewById(R.id.unpaid_layout);
		canGetMoneyView = (TextView) findViewById(R.id.can_get_money_value_view);
		canGetMoneyLayout = (RelativeLayout) findViewById(R.id.can_get_money_layout);
		pullScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh_scrollview);
		pullScrollView.setOnRefreshListener(refreshListener);
		fromWalletView.setOnClickListener(this);
		totalWalletView.setOnClickListener(this);
		abbrevView.setOnClickListener(this);
		yesterdayLayout.setOnClickListener(this);
		profitLayout.setOnClickListener(this);
		totalMoneyLayout.setOnClickListener(this); // 跳转到交易记录列表
		unPaidLayout.setOnClickListener(this);
		canGetMoneyLayout.setOnClickListener(this);
		riskDialog = new CommonDialog(this, getString(R.string.unpaid_income),
				getString(R.string.unpaid_income_content), "确定", new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
					}
				});
		riskDialog.setPositiveButtonColor(getResources().getColor(R.color.red_VI));
		relativeLayout = (RelativeLayout) findViewById(R.id.rate_layout);
		contentLayout = (LinearLayout) findViewById(R.id.line_Layout);
	}

	private Handler myHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			dismissLoading();
			pullScrollView.onRefreshComplete();
			String strJson = (String) msg.obj;
			if (strJson == null || "".equals(strJson))
			{
				showHintDialog("网络不给力！");
				return;
			}
			switch (msg.what)
			{
			case 1:// 获取总资产详情接口的数据处理
				handleWalletAssets(strJson);
				break;
			case 2:// 获取银行卡列表的处理
				handCardListJson((String) msg.obj);
				break;
			case 3:// 处理取现点击后是否有可取现银行卡
				handCashCardJson((String) msg.obj);
				break;
			case 6:
				// 获取是否解绑成功回复
				handHycardJson((String) msg.obj);
				break;
			case 4:
				handYelid((String) msg.obj);
				break;
			default:
				break;
			}
		}

	};

	private void requestYeild()
	{
		AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_YIELD_YEAR, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				myHandler.sendMessage(myHandler.obtainMessage(4, data));
			}
		}, null);
	}

	private void handYelid(String obj)
	{
		try
		{
			JSONObject object = new JSONObject(obj);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			if (ecode == 0)
			{
				JSONObject data = object.optJSONObject("data");
				JSONArray array = data.optJSONArray("profit_list");
				if (array != null && array.length() != 0)
				{
					if (array.get(0) != null)
					{
						qiriRateView.setText(StringHelper.formatString(
								((JSONObject) array.get(0)).optString("profitRate"), StringFormat.FORMATE_3)
								+ getString(R.string.str_percent));
						/**
						 * 开始绘制表格
						 */
						addLineChartView(array);
					}
				}
				else
				{
					relativeLayout.setVisibility(View.GONE);
				}

			}
			else
			{
				showHintDialog(emsg);
				return;
			}
		}
		catch (JSONException e)
		{
			showHintDialog("数据解析异常");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + obj);
			return;
		}
		catch (NumberFormatException e)
		{
			showHintDialog("数据解析异常");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + obj);
			return;
		}
	}

	/**
	 * 获取最大最小年化率
	 * 
	 * @param currentNum
	 */
	private void initMaxMin(float currentNum)
	{
		if (currentNum >= yAxisMax)
		{
			yAxisMax = currentNum;
		}

		if (currentNum < yAxisMin)
		{
			yAxisMin = currentNum;
		}
	}

	/**
	 * 绘制图表
	 * 
	 * @param array
	 * @throws JSONException
	 */
	private void addLineChartView(JSONArray array) throws JSONException
	{
		lineView = new LineChart(this);
		lineView.setDescription("");
		lineView.setScaleEnabled(false);
		lineView.getAxisRight().setEnabled(true);
		lineView.setDrawGridBackground(false);
		lineView.setTouchEnabled(false);
		lineView.getLegend().setEnabled(false);
		lineView.setHardwareAccelerationEnabled(true);

		ArrayList<Entry> yRawData = new ArrayList<Entry>();
		ArrayList<String> xRawDatas = new ArrayList<String>();
		int index = 0;
		for (int i = array.length() - 1; i >= 0; i--)
		{
			if (array.get(i) != null)
			{
				JSONObject jsonObj = (JSONObject) array.get(i);
				yRawData.add(new Entry(Float.parseFloat(jsonObj.optString("profitRate")), index));
				xRawDatas.add(DateFormatHelper.formatDate(jsonObj.optString("dt").concat("000"), DateFormat.DATE_1));
				index++;
				initMaxMin(Float.parseFloat(jsonObj.optString("profitRate")));
			}
		}
		/**
		 * x轴样式设置
		 */
		XAxis xAxis = lineView.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);// 设置x轴在底部显示
		xAxis.setAvoidFirstLastClipping(true);
		xAxis.setSpaceBetweenLabels(0); // x轴间距
		xAxis.setTextColor(resource.getColor(R.color.grey_low_txt));
		xAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
		xAxis.setDrawGridLines(true);
		xAxis.setGridColor(resource.getColor(R.color.color_dddddd));
		/**
		 * y轴样式设置
		 */
		YAxis leftAxis = lineView.getAxisLeft();
		leftAxis.setStartAtZero(false);
		leftAxis.setLabelCount(yAxislabelNum, true);
		leftAxis.setDrawLimitLinesBehindData(true);
		leftAxis.setTextColor(resource.getColor(R.color.grey_low_txt));
		leftAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
		leftAxis.setDrawGridLines(true);
		leftAxis.setGridColor(resource.getColor(R.color.color_dddddd));
		leftAxis.setAxisMaxValue(yAxisMax + yAxisGap); // 设置Y轴最大值
		leftAxis.setAxisMinValue(yAxisMin - yAxisGap);// 设置Y轴最小值

		YAxis rightAxis = lineView.getAxisRight();
		rightAxis.setDrawLabels(false);
		rightAxis.setDrawGridLines(false);
		rightAxis.setAxisLineColor(resource.getColor(R.color.color_dddddd));
		/**
		 * 曲线样式设置
		 */
		LineDataSet set = new LineDataSet(yRawData, "");
		set.setDrawCubic(true);
		set.setCubicIntensity(0.2f);
		set.setLineWidth(2.0f);
		set.setColor(resource.getColor(R.color.color_fd4634));
		set.setCircleSize(3.0f);
		set.setCircleColor(resource.getColor(R.color.color_fd4634));
		set.setFillColor(resource.getColor(R.color.color_fd4634));
		set.setFillAlpha(128);
		set.setDrawFilled(true);
		set.setDrawValues(false);

		LineData data = new LineData(xRawDatas, set);
		lineView.setData(data);
		lineView.invalidate();

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(this, 150));
		contentLayout.addView(lineView, params);
	}

	/**
	 * 发送异步请求获取活期宝资产数据
	 */
	private void requestWallet(boolean isShowLoading)
	{
		if (isShowLoading)
		{
			showLoading();
		}
		AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_ASSETS, new HttpCallBack()
		{

			@Override
			public void back(String data, int url)
			{
				if (data != null && !data.equals(""))
				{
					myHandler.sendMessage(myHandler.obtainMessage(1, data));
				}
				else
				{
					myHandler.sendEmptyMessage(5);
				}
			}
		}, null);
	}

	private void handleWalletAssets(String strJson)
	{
		// 活期宝总资产数据
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			if (ecode == 0)
			{
				JSONObject data = object.optJSONObject("data");
				JSONObject assetsObject = data.optJSONObject("assets");

				/**
				 * updateUI
				 */
				fdCode = assetsObject.optString("fdcode");
				isWorkday = data.optInt("is_worker");
				if (isWorkday == 1)
				{
					yesterdayProfitView.playNumber(Double.parseDouble(StringHelper.formatString(
							assetsObject.optString("profitYesday"), StringFormat.FORMATE_2)));
				}
				else
				{
					if (StringHelper.isNumberString(assetsObject.optString("yesday_text")))
					{
						yesterdayProfitView.playNumber(Double.parseDouble(assetsObject.optString("yesday_text")));
					}
					else
					{
						yesterdayProfitView.setText(assetsObject.optString("yesday_text"));
					}
				}

				walletBalanceView.setText(StringHelper.formatString(assetsObject.optString("assets"),
						StringFormat.FORMATE_2));
				// 昨日收益日期(时间戳)
				strTimeProfitBefore = assetsObject.optString("profitT");

				if (strTimeProfitBefore == null || strTimeProfitBefore.equals("") || strTimeProfitBefore.equals("null"))
				{
					dateBeforeView.setText(DateFormatHelper.getYesterdayTime(DateFormat.DATE_7));
				}
				else
				{
					dateBeforeView.setText(DateFormatHelper.formatDate(strTimeProfitBefore.concat("000"),
							DateFormat.DATE_5));
				}

				strProfitTemp = StringHelper.formatString(assetsObject.optString("profit"), StringFormat.FORMATE_2);
				if (strProfitTemp.equals("") || TextUtils.equals(strProfitTemp, "null") || strProfitTemp.isEmpty())
				{
					strProfitTemp = "0.00";
					walletProfitView.setText(strProfitTemp);
				}
				else
				{
					walletProfitView.setText("+" + strProfitTemp);
				}
				unpaidView.setText(StringHelper.formatString(assetsObject.optString("unpaid"), StringFormat.FORMATE_2));
				profitMillionView.setText(StringHelper.formatString(assetsObject.optString("profit10K"),
						StringFormat.FORMATE_3));
				abbrevView.setText(assetsObject.optString("abbrev"));
				canGetMoneyView.setText(String.valueOf(assetsObject.optDouble("usable_assets")));
			}
			else
			{
				showHintDialog(emsg);
			}
		}
		catch (JSONException e)
		{
			showHintDialog("数据解析异常");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
			return;
		}
		catch (NumberFormatException e)
		{
			showHintDialog("数据解析异常");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
			return;
		}
	}

	private void handCashCardJson(String jsonStr)
	{
		try
		{
			JSONObject object = new JSONObject(jsonStr);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String data = object.optString("data");
			if (ecode == 0)
			{
				JSONObject objData = new JSONObject(data);
				// 解析已绑定银行卡数据
				JSONArray arrayCard = objData.optJSONArray("assets_list");
				if (arrayCard.length() > 0)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("is_worker", isWorkday);
					bundle.putString("wallet_asset", walletBalanceView.getText().toString());
					openActivity(WalletCashActivity.class, bundle);
				}
				else
				{
					showHintDialog(getString(R.string.no_usable_asset));
				}
			}
			else
			{
				showHintDialog(emsg);
			}

		}
		catch (JSONException e)
		{
			showToast("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
		}

	}

	// 处理银行卡还原数据
	private void handHycardJson(String strJson)
	{
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			if (ecode == 0)
			{
				showToast("银行卡已成功还原");
				// 跳转到活期宝充值界面
				openActivity(WalletRechargeActivity.class);
			}
			else
			{
				showHintDialog(emsg);
			}
		}
		catch (JSONException e)
		{
			showHintDialog("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}

	private void handleClickRecharge()
	{
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_LIST, callback, null);
	}

	private HttpCallBack callback = new HttpCallBack()
	{
		@Override
		public void back(String data, int type)
		{
			myHandler.sendMessage(myHandler.obtainMessage(2, data));
		}
	};

	private ArrayList<Card> listUnboundedCard = new ArrayList<Card>();

	// 处理银行卡列表数据
	private void handCardListJson(String strJson)
	{
		ResponseBase responseBase = ResponseManager.getInstance().parse(strJson);
		if (responseBase != null)
		{
			if (responseBase.ecode == ErrorConst.EC_OK || StringHelper.isNotBlank(responseBase.strError))
			{
				JSONObject objData = responseBase.jsonObject;
				try
				{
					JSONArray cards = objData.getJSONArray("cards");
					JSONArray arrayUnboundCard = objData.getJSONArray("unbc");
					for (int i = 0; i < arrayUnboundCard.length(); i++)
					{
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
					// 有已绑定银行卡，直接跳转到充值
					if (cards != null && cards.length() > 0)
					{
						startActivityForResult(new Intent(WalletActivity.this, WalletRechargeActivity.class),
								Const.WALLET_BUY_FLOW);
						return;
					}
					else
					{
						// 没有已绑定银行卡，有已解绑银行卡，提示还原，还原后跳转到充值
						if (listUnboundedCard != null && listUnboundedCard.size() > 0)
						{
							recoverCard();
						}
						else
						{
							// 没有已绑定银行卡，也没有已解绑银行卡，则跳转到绑卡界面
							showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
							openActivity(QuickBillActivity.class);
						}
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					showHintDialog("数据解析错误");
					WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
				}
			}
			else
			{
				showHintDialog("网络不给力");
				return;
			}
		}
		else
		{
			showHintDialog("网络不给力");
			return;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case Const.WALLET_BUY_FLOW:
			if (PrefManager.getInstance().getBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, false))
			{
				PrefManager.getInstance().putBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, false);
				// 显示对话框
				final LotteryActivity mActivity = (LotteryActivity) data.getSerializableExtra("activity");
				if (mActivity != null)
				{
					CommonDialog dialog = new CommonDialog(this, getString(R.string.more_than_activity_title),
							mActivity.strMessage, getString(R.string.get_red_bag), getString(R.string.zhi_dao_l),
							new DialogClickListener()
							{
								@Override
								public void onDialogClick()
								{
									// 去抽奖页面
									Intent intent = new Intent(WalletActivity.this, WebActivity.class);
									intent.putExtra("url", mActivity.strUrl);
									intent.putExtra("webType", 8);
									startActivity(intent);
								}
							});
					dialog.show();
				}
			}
			break;
		}
	}

	private void recoverCard()
	{
		// 执行还原银行卡操作
		if (listUnboundedCard != null && !listUnboundedCard.isEmpty())
		{
			final Card info = new Card();
			info.setBoundTime(9223372036854775806L);
			if (listUnboundedCard.size() > 0)
			{
				for (Card tempInfo : listUnboundedCard)
				{
					if (info.getBoundTime() > tempInfo.getBoundTime())
					{
						info.setNumber(tempInfo.getNumber());
						info.setBankName(tempInfo.getBankName());
						info.setBoundTime(tempInfo.getBoundTime());
					}
				}

				String strMsg = "您已经有解绑过的" + info.getBankName() + "卡，卡号为" + StringHelper.hintCardNum(info.getNumber())
						+ "，是否直接还原该卡？";

				showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// 发送银行卡还原接口请求
						if (info.getNumber() != null && !TextUtils.equals("", info.getNumber()))
						{
							String strUrl = UrlConst.CARD_HTYCARD;
							Hashtable<String, Object> map = new Hashtable<String, Object>();
							map.put("cd", info.getNumber());
							AnsynHttpRequest.requestByPost(WalletActivity.this, strUrl, callback5, map);

							showLoading();
						}

						dialog.dismiss();
					}
				}, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
			}
		}
	}

	private HttpCallBack callback5 = new HttpCallBack()
	{
		@Override
		public void back(String data, int type)
		{
			myHandler.sendMessage(myHandler.obtainMessage(6, data));
		}
	};

	// 加载绑卡数据
	private void requestUnbound()
	{
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD, callback2, null);
	}

	private HttpCallBack callback2 = new HttpCallBack()
	{
		@Override
		public void back(String data, int type)
		{
			myHandler.sendMessage(myHandler.obtainMessage(3, data));
		}
	};

	private OnRefreshListener<ScrollView> refreshListener = new OnRefreshListener<ScrollView>()
	{
		@Override
		public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
		{
			requestWallet(false);
		}
	};

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:// 返回
			finish();
			break;
		case R.id.bt_towallet:
			handleClickRecharge();
			break;
		case R.id.bt_fromwallet:// 取现
			requestUnbound();
			break;
		case R.id.can_get_money_layout:
		case R.id.total_money_layout:// 钱包余额显示 点击后 跳转到 全部（交易记录）
			openActivity(WalletHistoryActivity.class);
			break;
		case R.id.profit_layout:
		case R.id.total_layout:
		case R.id.tv_profit:// 累计收益显示 点击后调到 每日收益
			if (Network.checkNetWork(this))
			{
				Bundle bundle2 = new Bundle();
				bundle2.putString("profit", strProfitTemp);
				openActivity(WalletProfitActivity.class, bundle2);
			}
			else
			{
				showHintDialog(getString(R.string.net_prompt));
			}
			break;
		case R.id.unpaid_layout:// 未付收益
			riskDialog.show();
			break;
		case R.id.im_help:// 帮助
			Bundle bundle3 = new Bundle();
			bundle3.putInt("webType", 2);
			openActivity(WebActivity.class, bundle3);
			break;
		case R.id.tv_abbrev:
			if (Network.checkNetWork(this))
			{
				Intent intent = new Intent(this, FundDetailsActivity.class);
				intent.putExtra("fdcode", fdCode);
				intent.putExtra("from_asemble", true);
				startActivity(intent);
			}
			else
			{
				showHintDialog(getString(R.string.net_prompt));
			}
			break;
		}
	}
}
