package com.qianjing.finance.ui.activity.virtual;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.model.redeem.RedeemModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.assemble.AssembleBuyActivity;
import com.qianjing.finance.ui.activity.assemble.redeem.AssembleRedeemDetailActivity;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.activity.purchase.PurchaseBuyActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.FormatNumberData;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.virtual.FundDetailsView;
import com.qianjing.finance.widget.adapter.base.BaseDetailsAdapter;
import com.qjautofinancial.R;

/**
 * @category 已购买组合详情页
 * @author liuchen
 * */

public class VirtualSchemaDetails extends VirtualBaseDetails implements OnClickListener
{

	private TextView totalMoney;
	private TextView unpaid;
	private TextView profit;
	private Button btnBuy;
	private Button btnRedeem;
	private Float useableAssets;
	private TextView tvHold;

	private ArrayList<Fund> infos = new ArrayList<Fund>();
	private String sid;
	private RelativeLayout copyInvest;

	/** 有效银行卡容器 */
	private ArrayList<Card> listCard = new ArrayList<Card>();
	/** 已解绑银行卡容器 */
	private ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
	/** 全部银行卡容器 */
	private ArrayList<Card> listAllCard = new ArrayList<Card>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// 验证银行卡列表
		if (UserManager.getInstance().getUser() != null)
		{
			requestCardList();
		}
	}

	private void initView()
	{

		setContentView(R.layout.activity_virtual_schema_details);
		setTitleBack();

		ImageView rightText = (ImageView) findViewById(R.id.iv_title_edit);
		rightText.setVisibility(View.VISIBLE);
		rightText.setOnClickListener(this);

		tvHold = (TextView) findViewById(R.id.tv_hold);
		totalMoney = (TextView) findViewById(R.id.tv_total_money);
		unpaid = (TextView) findViewById(R.id.tv_unpaid_value);
		profit = (TextView) findViewById(R.id.tv_total_profit);
		btnBuy = (Button) findViewById(R.id.btn_buy);
		btnRedeem = (Button) findViewById(R.id.btn_redeem);
		copyInvest = (RelativeLayout) findViewById(R.id.rl_assemble_copy);
		copyInvest.setOnClickListener(this);
		btnBuy.setOnClickListener(this);
		btnRedeem.setOnClickListener(this);
		tvHold.setOnClickListener(this);

		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		useableAssets = intent.getFloatExtra("useableAssets", 0);

		requestVirtualDetails(sid);
	}

	@Override
	public void initDataView()
	{

		FundDetailsView fdvDetails = (FundDetailsView) findViewById(R.id.fdv_details);
		fdvDetails.setDetailAdapter(new BaseDetailsAdapter()
		{

			@Override
			public int getRiskTypeValue()
			{
				return (int) detailsInfo.assembly.risk;
			}

			@Override
			public List<SparseArray<Object>> getDetailData()
			{

				List<SparseArray<Object>> dataList = new ArrayList<SparseArray<Object>>();

				ArrayList<String> nameList = detailsInfo.assembly.abbrevs;
				ArrayList<String> fundcode = detailsInfo.assembly.fdcodes;
				ArrayList<Float> ratioList = detailsInfo.assembly.ratios;

				for (int i = 0; i < nameList.size(); i++)
				{
					SparseArray<Object> sparseArray = new SparseArray<Object>();
					sparseArray.put(1, nameList.get(i));
					sparseArray.put(2, ratioList.get(i));
					sparseArray.put(3, fundcode.get(i));
					dataList.add(sparseArray);

					infos.add(new Fund(detailsInfo.assembly.fdcodes.get(i), detailsInfo.assembly.abbrevs.get(i),
							detailsInfo.assembly.sharetypes.get(i), detailsInfo.assembly.ratios.get(i), 0xff000000));
				}

				return dataList;
			}

			@Override
			public void setDetailsItemClick(int position)
			{
				Intent intent = new Intent(VirtualSchemaDetails.this, FundDetailsActivity.class);
				intent.putExtra("fdcode", detailsInfo.assembly.fdcodes.get(position));
				intent.putExtra("from_asemble", true);
				startActivity(intent);
			}

			@Override
			public void setRiskTypeClick()
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

			@Override
			public int getNostockRatio()
			{

				return detailsInfo.assembly.nostock_ratio;
			}

			@Override
			public int getStockRatio()
			{
				return detailsInfo.assembly.stock_ratio;
			}

			@Override
			public String getStock_text()
			{
				return detailsInfo.assembly.stock_text;
			}

			@Override
			public String getNostock_text()
			{

				return detailsInfo.assembly.nostock_text;
			}

		});

		float assetsValue = detailsInfo.assets.assets;
		float unpaidValue = detailsInfo.assets.unpaid;
		float profitValue = detailsInfo.assets.profit;

		FormatNumberData.simpleFormatNumber(assetsValue, totalMoney);
		FormatNumberData.simpleFormatNumber(unpaidValue, unpaid);
		FormatNumberData.simpleFormatNumberPM(profitValue, profit);

		setTitleWithString(detailsInfo.schema.name);

		// if(detailsInfo.assets.assets==0){
		// btnRedeem.setEnabled(false);
		// }
		if (useableAssets == 0)
		{
			btnBuy.setEnabled(false);
		}
	}

	/** 获取银行卡列表 */
	private void requestCardList()
	{

		showLoading();

		AnsynHttpRequest.requestByPost(this, UrlConst.WALLET_CARD_LIST, new HttpCallBack()
		{
			@Override
			public void back(String data, int type)
			{
				Message msg = new Message();
				msg.obj = data;
				msg.what = TYPE_CARD_LIST;
				mHandler.sendMessage(msg);
			}
		}, null);
	}

	private static final int TYPE_CARD_LIST = 4;
	private static final int TYPE_CARD_HYCARD = 5;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String strResponse = (String) msg.obj;
			switch (msg.what)
			{
			case TYPE_CARD_LIST:
				handCardListJson(strResponse);
				break;
			case TYPE_CARD_HYCARD:
				handHycardJson(strResponse);
				break;
			}
		}
	};

	@Override
	public void onClick(View v)
	{


		switch (v.getId())
		{

		case R.id.btn_buy:
			if (detailsInfo != null)
			{
				jump2PurchasePage();
			}
			break;
		case R.id.btn_redeem:
			if (detailsInfo != null)
			{
				if (detailsInfo.assets.assets == 0)
				{
					showHintDialog("您好，当前可赎回余额为0，无法赎回");
				}
				else
				{
					jump2RedeemPage();
				}
			}
			break;
		case R.id.tv_hold:
			jump2HoldingDetail();
			break;

		case R.id.iv_title_edit:
			showPopupWindow();
			break;
		case R.id.rl_assemble_copy:
			copy2RealInvest();
			break;
		}
	}

	/** 复制虚拟组合到真实组合 */
	private void copy2RealInvest()
	{

		if (listCard == null || listUnboundedCard == null)
		{
			showToast("处理银行卡数据发生错误.");
			return;
		}

		// 有银行卡则打开申购金额页
		if (!listCard.isEmpty())
		{
			Intent intent = new Intent(VirtualSchemaDetails.this, AssembleBuyActivity.class);
			intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_COPY_VIRTUAL_BUY);
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
							Hashtable<String, Object> map = new Hashtable<String, Object>();
							map.put("cd", info.getNumber());
							AnsynHttpRequest.requestByPost(VirtualSchemaDetails.this, UrlConst.CARD_HTYCARD,
									new HttpCallBack()
									{
										@Override
										public void back(String data, int type)
										{
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

	private void jump2HoldingDetail()
	{

		if (detailsInfo == null)
		{
			return;
		}

		Intent holdDetail = new Intent(this, HoldingDetails.class);
		PurchaseModel pModel = new PurchaseModel();
		/**
		 * 除了接口数据和sid，其他数据都是为了持仓明细中的投资按钮准备的
		 * */
		pModel.setSid(detailsInfo.schema.sid + "");
		pModel.setVirtual(true);
		pModel.setSchemaName(detailsInfo.schema.name);
		pModel.setUsableMonay(useableAssets);

		holdDetail.putExtra("purchaseInfo", pModel);
		startActivityForResult(holdDetail, CustomConstants.VIRTUAL_PUTCHASE);
	}

	private void jump2RedeemPage()
	{
		Intent redeem = new Intent(this, AssembleRedeemDetailActivity.class);
		RedeemModel rmodel = new RedeemModel();
		rmodel.setAssembleName(detailsInfo.schema.name);
		rmodel.setsId(String.valueOf(detailsInfo.schema.sid));
		rmodel.setUsableAsset(detailsInfo.assets.assets);
		rmodel.setVirtual(true);
		rmodel.setMinValue(1);
		rmodel.setMaxValue(100);
		rmodel.setFundAssetsList(detailsInfo.fundAssetList);
		redeem.putExtra("redeemInfo", rmodel);
		startActivity(redeem);
	}

	private void jump2PurchasePage()
	{

		Intent intent = new Intent(this, PurchaseBuyActivity.class);

		intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_VIRTUAL_ADD_BUY);

		PurchaseModel pModel = new PurchaseModel();
		pModel.setSid(detailsInfo.schema.sid + "");
		pModel.setVirtual(true);
		pModel.setSchemaName(detailsInfo.schema.name);
		pModel.setUsableMonay(useableAssets);
		intent.putExtra("purchaseInfo", pModel);
		intent.putParcelableArrayListExtra("fundList", infos);

		startActivityForResult(intent, CustomConstants.VIRTUAL_PUTCHASE);
	}

	@Override
	public View getPopParent()
	{
		return View.inflate(this, R.layout.activity_virtual_schema_details, null);
	}

	@Override
	public boolean getPageType()
	{
		return true;
	}

	// 处理银行卡列表数据
	private void handCardListJson(String jsonStr)
	{

		dismissLoading();

		if (StringHelper.isBlank(jsonStr))
		{
			showHintDialog("网络不给力");
			return;
		}

		try
		{
			JSONObject object = new JSONObject(jsonStr);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			String data = object.optString("data");
			if (ecode == 0)
			{
				JSONObject objData = new JSONObject(data);

				// 解析所有银行卡信息存入List
				// 临时措施，后续需要合并银行卡存放方式
				listAllCard = CommonUtil.parseCardList(objData);

				// 解析已绑定银行卡数据
				JSONArray arrayCard = objData.getJSONArray("cards");

				for (int i = 0; i < arrayCard.length(); i++)
				{
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
			}
			else
			{
				showHintDialog(emsg);
			}

		}
		catch (JSONException e)
		{
			showHintDialog("数据解析错误");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + jsonStr);
		}
	}

	// 处理银行卡还原数据
	private void handHycardJson(String strJson)
	{

		dismissLoading();

		if (StringHelper.isBlank(strJson))
		{
			showHintDialog("网络不给力");
			return;
		}

		try
		{
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == CustomConstants.MODIFY_SCHEMA && requestCode == CustomConstants.MODIFY_SCHEMA)
		{
			requestVirtualDetails(sid);
		}
		else if (resultCode == CustomConstants.SHUDDOWN_ACTIVITY && requestCode == CustomConstants.MODIFY_SCHEMA)
		{
			finish();
		}
		// 申购结果返回
		else if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE)
		{
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE);
			finish();
		}
	}


}
