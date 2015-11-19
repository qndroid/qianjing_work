package com.qianjing.finance.ui.activity.assemble.recommand;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.quickbuy.QuickBuyDetail;
import com.qianjing.finance.model.quickbuy.QuickBuyListData;
import com.qianjing.finance.model.recommand.RecommandBuyDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.quickbuy.QuickAssembleBuyActivity;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qianjing.finance.view.indictorview.IndiactorView;
import com.qianjing.finance.view.indictorview.IndiactorView.IndictorClickListener;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**********************************************************
 * @文件名称：QuickFundDetailActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月29日 上午11:30:48
 * @文件描述：快速购买组合详情
 * @修改历史：2015年5月29日创建初始版本
 **********************************************************/
public class RecommandFundDetailActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 配置颜色等级
	 */
	private final int[] colors = new int[]
	{ R.color.color_5ba7e1, R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7, R.color.color_a6d0e6 };
	/**
	 * UI
	 */
	private LinearLayout contentView;
	private Button backButton;
	private TextView titleTextView;
	private TextView startBuyButton;
	private TextView xuNiButton;
	private IndiactorView indictorView;
	private PieGraph pieGraph;
	/**
	 * data
	 */
	private int riskType;
	private String mProductName;
	private RecommandBuyDetail quickBuyDetail;
	private ArrayList<QuickBuyListData> listData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_buy_detail_layout);
		initData();
		initView();
		requestFundDetail();
	}

	private void initData()
	{
		Intent intent = getIntent();
		riskType = intent.getIntExtra("type", -1);
		mProductName = intent.getStringExtra("product_name");

	}

	private void initView()
	{
		titleTextView = (TextView) findViewById(R.id.title_middle_text);
		titleTextView.setText(mProductName);
		backButton = (Button) findViewById(R.id.bt_back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);

		contentView = (LinearLayout) findViewById(R.id.content_view);
		startBuyButton = (TextView) findViewById(R.id.start_buy_btn);
		startBuyButton.setOnClickListener(this);
		xuNiButton = (TextView) findViewById(R.id.xu_ni_buy_btn);
		xuNiButton.setOnClickListener(this);
		indictorView = (IndiactorView) findViewById(R.id.indictor_view);
		pieGraph = (PieGraph) findViewById(R.id.pie_graph);
		quickBuyDetail = new RecommandBuyDetail();
		/**
		 * 只传给Adapter必要的数据
		 */
		listData = new ArrayList<QuickBuyListData>();
	}

	private void requestFundDetail()
	{
		showLoading();
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("risk", riskType);
		AnsynHttpRequest.requestByPost(this, UrlConst.RECOMMAND_FUND_DETAIL, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				dismissLoading();
				if (data == null || data.equals(""))
				{
					showHintDialog("网络不给力!");
				}
				else
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
				}

			}
		}, params);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{

			case 0x01:
				handleFundDetail(msg.obj.toString());
				break;
			case 0x02:
				handleCardList(msg.obj.toString());
				break;
			case 0x03:
				handleHYCard(msg.obj.toString());
				break;
			}
		};
	};

	protected void handleFundDetail(String strJson)
	{
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			switch (ecode)
			{

			case 0:
				JSONObject data = object.optJSONObject("data");
				JSONArray rations = data.optJSONObject("assembly").optJSONArray("ratios");
				JSONArray abbrevs = data.optJSONObject("assembly").optJSONArray("abbrevs");
				JSONArray fdcodes = data.optJSONObject("assembly").optJSONArray("fdcodes");
				double financialRisk = data.optJSONObject("assembly").optDouble("risk");
				double stockRadio = data.optJSONObject("assembly").optDouble("stock_ratio");
				double noStockRadio = data.optJSONObject("assembly").optDouble("nostock_ratio");
				String stockText = data.optJSONObject("assembly").optString("stock_text");
				String noStockText = data.optJSONObject("assembly").optString("nostock_text");
				String limitInit = data.optJSONObject("schema").optJSONObject("limit").optString("init");

				ArrayList<Double> tempRations = new ArrayList<Double>();
				ArrayList<String> tempAbbrevs = new ArrayList<String>();
				ArrayList<String> tempFdcodes = new ArrayList<String>();

				for (int i = 0; i < rations.length(); i++)
				{
					tempFdcodes.add(fdcodes.getString(i));
					tempRations.add(rations.getDouble(i));
					tempAbbrevs.add(abbrevs.getString(i));
					listData.add(new QuickBuyListData(rations.getDouble(i), abbrevs.getString(i), fdcodes.getString(i),
							colors[i]));
				}
				quickBuyDetail.setAbbrev(tempAbbrevs);
				quickBuyDetail.setRatios(tempRations);
				quickBuyDetail.setFdCodes(tempFdcodes);
				quickBuyDetail.setFinancialRisk(financialRisk);
				quickBuyDetail.setLimitInit(limitInit);
				quickBuyDetail.setNoStockRatio(noStockRadio);
				quickBuyDetail.setStockRatio(stockRadio);
				quickBuyDetail.setStockName(stockText);
				quickBuyDetail.setNoStockName(noStockText);
				/**
				 * updateUI
				 */
				updateUI();
				break;
			default:
				showHintDialog(emsg);
				break;
			}

		}
		catch (Exception e)
		{
			showHintDialog("数据解析异常!");
			finish();
		}
	}

	/**
	 * 根据请求返回结果，更新UI
	 */
	private void updateUI()
	{
		ArrayList<PieSlice> pieNodes = new ArrayList<PieSlice>();
		for (int i = 0; i < listData.size(); i++)
		{
			contentView.addView(createQucikItem(listData.get(i)), i);
			if (listData.get(i).getFundRate() > 0)
			{
				pieNodes.add(createPieSlice(listData.get(i)));
			}
		}
		/**
		 * 更新风险指示 View
		 */
		indictorView.setPosition((int) quickBuyDetail.getFinancialRisk());
		indictorView.setIndictorClickListener(new IndictorClickListener()
		{
			@Override
			public void onClick()
			{
				showHintDialog(getString(R.string.fen_xian_zhi_shu_shuo_ming),
						getString(R.string.fen_xian_zhi_shu_shuo_ming_nei_rong), new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						}, getString(R.string.zhi_dao_l));
			}
		});

		/**
		 * 更新饼状图
		 */
		pieGraph.setDrawText(quickBuyDetail.getNoStockName(), (int) quickBuyDetail.getNoStockRatio()
				+ getString(R.string.str_percent), quickBuyDetail.getStockName(), (int) quickBuyDetail.getStockRatio()
				+ getString(R.string.str_percent), pieNodes);
	}

	private AssembleNewItemLayout createQucikItem(final QuickBuyListData data)
	{
		AssembleNewItemLayout assembleItem = new AssembleNewItemLayout(this);
		assembleItem.initData(data.getIndictorColor(), data.getFundName(),
				getString(R.string.left_brackets) + data.getFundCode() + getString(R.string.right_brackets),
				StringHelper.formatString(String.valueOf(data.getFundRate() * 100), StringFormat.FORMATE_2));
		assembleItem.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(RecommandFundDetailActivity.this, FundDetailsActivity.class);
				intent.putExtra("fdcode", data.getFundCode());
				intent.putExtra("from_asemble", true);
				startActivity(intent);
			}
		});
		return assembleItem;
	}

	private PieSlice createPieSlice(QuickBuyListData data)
	{
		PieSlice slice = new PieSlice();
		slice.setColor(getResources().getColor(data.getIndictorColor()));
		slice.setValue((float) data.getFundRate() * 100);
		return slice;
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		case R.id.start_buy_btn:
			if (UserManager.getInstance().getUser() != null)
			{
				requestCardList();
			}
			else
			{
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("LoginTarget", LoginTarget.QUICK_FUND_DETAIL.getValue());
				startActivity(intent);
			}
			break;
		}
	}

	/**
	 * 获取银行卡列表
	 */
	private void requestCardList()
	{
		showLoading();
		// 请求银行卡信息
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_LIST, cardListHandle, null);
	}

	private HttpCallBack cardListHandle = new HttpCallBack()
	{

		@Override
		public void back(String data, int status)
		{

			dismissLoading();
			if (data != null && !data.equals(""))
			{

				mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
			}
			else
			{
				showHintDialog("网络不给力!");
			}
		}
	};

	/**
	 * 处理银行卡返回数据
	 * 
	 * @param obj
	 */
	private void handleCardList(String obj)
	{
		try
		{
			JSONObject object = new JSONObject(obj);
			JSONObject data = object.optJSONObject("data");
			int ecode = data.optInt("ecode");
			String reason = data.optString("emsg");
			switch (ecode)
			{

			case ErrorConst.EC_OK:
				JSONArray cards = data.optJSONArray("cards");
				/**
				 * 已经有银行卡信息，直接跳转
				 */
				if (cards != null && cards.length() > 0)
				{

					startAnotherActivity();
				}
				else
				{

					JSONArray arrayUnboundCard = data.getJSONArray("unbc");
					/**
					 * 有未解绑银行卡
					 */
					if (arrayUnboundCard != null && arrayUnboundCard.length() > 0)
					{

						recoverCard(arrayUnboundCard);
					}
					else
					{

						showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
						startActivity(new Intent(this, QuickBillActivity.class));
					}
				}
				break;
			default:
				dismissLoading();
				showHintDialog(reason);
				break;
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			showHintDialog("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + obj.toString());
		}
	}

	private void recoverCard(JSONArray arrayUnboundCard)
	{

		ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
		final Card maxOldCard = new Card();
		maxOldCard.setBoundTime(9223372036854775806L);

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

		/**
		 * 取出最先解绑的卡
		 */
		for (Card tempInfo : listUnboundedCard)
		{
			if (maxOldCard.getBoundTime() > tempInfo.getBoundTime())
			{
				maxOldCard.setNumber(tempInfo.getNumber());
				maxOldCard.setBankName(tempInfo.getBankName());
				maxOldCard.setBoundTime(tempInfo.getBoundTime());
			}
		}

		String strMsg = "您已经有解绑过的" + maxOldCard.getBankName() + "卡，卡号为"
				+ StringHelper.hintCardNum(maxOldCard.getNumber()) + "，是否直接还原该卡？";

		showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// 发送银行卡还原接口请求
				showLoading();
				requestHYCard(maxOldCard.getNumber());

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

	private void requestHYCard(String cardnum)
	{

		// 发送银行卡还原接口请求
		showLoading();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("cd", cardnum);
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_HTYCARD, hyCardCallback, map);
	}

	private HttpCallBack hyCardCallback = new HttpCallBack()
	{

		@Override
		public void back(String data, int status)
		{
			dismissLoading();
			if (data != null && !data.equals(""))
			{

				mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
			}
			else
			{
				showHintDialog("网络不给力");
			}
		}
	};

	private void handleHYCard(String json)
	{
		try
		{
			JSONObject data = new JSONObject(json);
			int ecode = data.optInt("ecode");
			String reason = data.optString("emsg");
			switch (ecode)
			{

			case ErrorConst.EC_OK:
				showToast("银行卡已成功还原");
				startAnotherActivity();
				break;
			default:
				showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{

			showHintDialog("数据解析异常!");
		}
	}

	/**
	 * 跳转到组合申购页面
	 */
	private void startAnotherActivity()
	{
		Intent intent = new Intent(this, QuickAssembleBuyActivity.class);
		intent.putExtra("type", riskType);
		intent.putExtra("isFromRecommend", true);
		intent.putExtra("product_name", mProductName);
		intent.putExtra("min_value", quickBuyDetail.getLimitInit());
		startActivityForResult(intent, CustomConstants.QUICK_PUECHASE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_BUY_RESULT_CODE)
		{
			if (data.hasExtra("lottery"))
			{
				LotteryActivity lottery = (LotteryActivity) data.getSerializableExtra("lottery");
				if (lottery != null)
					PrefManager.getInstance().putString(PrefManager.KEY_LOTTERY_URL, lottery.strUrl);
			}
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
