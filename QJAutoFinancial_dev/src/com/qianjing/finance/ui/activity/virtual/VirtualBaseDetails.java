package com.qianjing.finance.ui.activity.virtual;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.DealWithSchemaInter;
import com.qianjing.finance.model.fund.MyFundAssets;
import com.qianjing.finance.model.virtual.AssetsDetailBean;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.assemble.AssembleDetailActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.DealWithSchema;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.view.dialog.ActionSheet;
import com.qianjing.finance.view.dialog.ActionSheet.OnSheetItemClickListener;
import com.qjautofinancial.R;

public abstract class VirtualBaseDetails extends BaseActivity
{

	protected AssetsDetailBean detailsInfo;

	/** 组合详情实例 */
	protected AssembleDetail mAssembleDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	/**
	 * request virtual assets details data
	 * */
	public void requestVirtualDetails(String sid)
	{
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("sid", sid);
		AnsynHttpRequest.requestByPost(this, UrlConst.VIRTUAL_COMBINATION, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}, hashTable);
	}

	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			String jsonStr = (String) msg.obj;
			switch (msg.what)
			{
			case 1:
				//LogUtils.syso("虚拟资产详情:" + jsonStr);
				analysisDetailsData(jsonStr);
				break;
			}
		};
	};

	protected void analysisDetailsData(String jsonStr)
	{

		if (jsonStr == null || "".equals(jsonStr))
		{
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try
		{
			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			if (ecode == 0)
			{
				String data = jobj.optString("data");
				mAssembleDetail = CommonUtil.parseAssembleDetail(data);
				mAssembleDetail.getAssembleAssets().setSid("");
				mAssembleDetail.getAssembleBase().setSid("");
				mAssembleDetail.getAssembleConfig().setSid("");

				JSONObject dataObj = new JSONObject(data);
				detailsInfo = new AssetsDetailBean();

				JSONObject assembly = dataObj.optJSONObject("assembly");
				JSONObject assets = dataObj.optJSONObject("assets");
				JSONObject investReminder = dataObj.optJSONObject("investReminder");
				JSONObject schema = dataObj.optJSONObject("schema");

				detailsInfo.assembly.DeltaR = (float) assembly.optDouble("DeltaR");
				detailsInfo.assembly.Rn = (float) assembly.optDouble("Rn");
				detailsInfo.assembly.Vn = (float) assembly.optDouble("Vn");
				detailsInfo.assembly.assembT = (float) assembly.optDouble("assembT");
				detailsInfo.assembly.hgRatio = (float) assembly.optDouble("hgRatio");
				detailsInfo.assembly.init = (float) assembly.optDouble("init");
				detailsInfo.assembly.month = (float) assembly.optDouble("month");
				detailsInfo.assembly.risk = (float) assembly.optDouble("risk");
				detailsInfo.assembly.sid = (float) assembly.optDouble("sid");

				detailsInfo.assembly.bank = assembly.optString("bank");
				detailsInfo.assembly.card = assembly.optString("card");
				detailsInfo.assembly.earnings = assembly.optString("earnings");
				detailsInfo.assembly.sname = assembly.optString("sname");
				detailsInfo.assembly.tradeacco = assembly.optString("tradeacco");
				detailsInfo.assembly.uid = assembly.optString("uid");
				detailsInfo.assembly.stock_text = assembly.optString("stock_text");
				detailsInfo.assembly.nostock_text = assembly.optString("nostock_text");

				detailsInfo.assembly.nostock_ratio = assembly.optInt("nostock_ratio");
				detailsInfo.assembly.stock_ratio = assembly.optInt("stock_ratio");

				JSONArray abbrevs = assembly.optJSONArray("abbrevs");
				JSONArray fdcodes = assembly.optJSONArray("fdcodes");
				JSONArray ratios = assembly.optJSONArray("ratios");
				JSONArray sharetypes = assembly.optJSONArray("sharetypes");
				for (int i = 0; i < abbrevs.length(); i++)
				{
					detailsInfo.assembly.abbrevs.add((String) abbrevs.get(i));
				}

				for (int i = 0; i < fdcodes.length(); i++)
				{
					detailsInfo.assembly.fdcodes.add((String) fdcodes.get(i));
				}

				for (int i = 0; i < ratios.length(); i++)
				{
					/**
					 * 
					 * */
					detailsInfo.assembly.ratios.add((float) ratios.getDouble(i));
				}

				for (int i = 0; i < sharetypes.length(); i++)
				{
					detailsInfo.assembly.sharetypes.add((String) sharetypes.get(i));
				}

				detailsInfo.assets.assets = (float) assets.optDouble("assets");
				detailsInfo.assets.income = (float) assets.optDouble("income");
				detailsInfo.assets.invest = (float) assets.optDouble("invest");
				detailsInfo.assets.moditm = (float) assets.optDouble("moditm");
				detailsInfo.assets.profit = (float) assets.optDouble("profit");
				detailsInfo.assets.profitYesday = (float) assets.optDouble("profitYesday");
				detailsInfo.assets.redemping = (float) assets.optDouble("redemping");
				detailsInfo.assets.sid = (float) assets.optDouble("sid");
				detailsInfo.assets.subscripting = (float) assets.optDouble("subscripting");
				detailsInfo.assets.unpaid = (float) assets.optDouble("unpaid");

				detailsInfo.assets.uid = assets.optString("");

				detailsInfo.investReminder.message = investReminder.optString("message");

				/**
				 * 
				 * schema data
				 * */
				detailsInfo.schema.createT = (float) schema.optDouble("createT");

				detailsInfo.schema.pension_init = (float) schema.optDouble("pension_init");
				detailsInfo.schema.pension_month = (float) schema.optDouble("pension_month");
				detailsInfo.schema.pension_retire = (float) schema.optDouble("pension_retire");
				detailsInfo.schema.pension_age = (float) schema.optDouble("pension_age");

				detailsInfo.schema.small_init = (float) schema.optDouble("small_init");
				detailsInfo.schema.small_month = (float) schema.optDouble("small_month");
				detailsInfo.schema.small_nmonth = (float) schema.optDouble("small_nmonth");

				detailsInfo.schema.house_init = (float) schema.optDouble("house_init");
				detailsInfo.schema.house_money = (float) schema.optDouble("house_money");
				detailsInfo.schema.house_year = (float) schema.optDouble("house_year");

				detailsInfo.schema.financial_init = (float) schema.optDouble("financial_init");
				detailsInfo.schema.financial_rate = (float) schema.optDouble("financial_rate");
				detailsInfo.schema.financial_risk = (float) schema.optDouble("financial_risk");

				detailsInfo.schema.married_init = (float) schema.optDouble("married_init");
				detailsInfo.schema.married_money = (float) schema.optDouble("married_money");
				detailsInfo.schema.married_year = (float) schema.optDouble("married_year");

				detailsInfo.schema.education_money = (float) schema.optDouble("education_money");
				detailsInfo.schema.education_year = (float) schema.optDouble("education_year");

				detailsInfo.schema.financial2_age = schema.optInt("financial2_age");
				detailsInfo.schema.financial2_init = (float) schema.optDouble("financial2_init");
				detailsInfo.schema.financial2_preference = (float) schema.optDouble("financial2_preference");
				detailsInfo.schema.financial2_risk = (float) schema.optDouble("financial2_risk");
				detailsInfo.schema.financial2_risklevel = (float) schema.optDouble("financial2_risklevel");

				detailsInfo.schema.state = (float) schema.optDouble("state");
				detailsInfo.schema.updateT = (float) schema.optDouble("updateT");

				detailsInfo.schema.name = schema.optString("name");
				detailsInfo.schema.type = schema.optString("type");
				detailsInfo.schema.uid = schema.optString("uid");

				detailsInfo.schema.sid = schema.optInt("sid");
				detailsInfo.schema.real_sid = schema.optInt("real_sid");

				JSONArray fundAssets = dataObj.optJSONArray("share_details");
				ArrayList<MyFundAssets> fundAssetList = new ArrayList<MyFundAssets>();
				for (int i = 0; i < fundAssets.length(); i++)
				{
					MyFundAssets asset = new MyFundAssets();
					asset.setShares(fundAssets.getJSONObject(i).optString("shares"));
					asset.setUsableShares(fundAssets.getJSONObject(i).optString("usable_shares"));
					asset.setAbbrev(fundAssets.getJSONObject(i).optString("abbrev"));
					asset.setNav(fundAssets.getJSONObject(i).optString("nav"));
					fundAssetList.add(asset);
				}
				detailsInfo.fundAssetList = fundAssetList;
				if (detailsInfo != null)
				{
					initDataView();
					dismissLoading();
				}
			}
			else
			{
				showHintDialog(emsg);
				dismissLoading();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			WriteLog.recordLog(e.toString());
			showHintDialog(getString(R.string.net_data_error));
			dismissLoading();
		}
	}

	public void showPopupWindow()
	{
//		View popupView = View.inflate(this, R.layout.popupow_layout, null);
//
//		Button motifyName = (Button) popupView.findViewById(R.id.btn_motify_name);
//		Button motifyTarget = (Button) popupView.findViewById(R.id.btn_motify_target);
//		Button deleteSchema = (Button) popupView.findViewById(R.id.btn_delete_schema);
//		Button cancel = (Button) popupView.findViewById(R.id.btn_cancel);
//
//		if (getPageType())
//		{
//			deleteSchema.setVisibility(View.GONE);
//			motifyTarget.setVisibility(View.GONE);
//		}
//
//		motifyName.setOnClickListener(this);
//		motifyTarget.setOnClickListener(this);
//		deleteSchema.setOnClickListener(this);
//		cancel.setOnClickListener(this);
//
//		popupWindow = new PopupWindow(popupView, -2, -2, true);
//		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//		popupWindow.showAtLocation(getPopParent(), Gravity.BOTTOM, 0, 0);
//
//		showAnimation(popupView);
		ActionSheet actionSheet =new ActionSheet(VirtualBaseDetails.this)
		.build()
		.setCancleable(false)
		.setCancleOnTouchOutside(true);
		actionSheet.addSheetItem("再平衡", getResources().getColor(R.color.actionsheet_blue),
				new OnSheetItemClickListener() {
					
					@Override
					public void onClick(int which) {
						Toast.makeText(VirtualBaseDetails.this, "再平衡", 1).show();
					}
				});
		actionSheet.addSheetItem("修改名称", getResources().getColor(R.color.actionsheet_blue),
				new OnSheetItemClickListener() {
					
					@Override
					public void onClick(int which) {
						motifyName(1);
					}
				});
		
		if (!getPageType()){
			actionSheet.addSheetItem("修改目标", getResources().getColor(R.color.actionsheet_blue),
					new OnSheetItemClickListener() {
						
						@Override
						public void onClick(int which) {
							motifyName(3);
						}
					});
			actionSheet.addSheetItem("删除组合", getResources().getColor(R.color.actionsheet_blue),
					new OnSheetItemClickListener() {
						
						@Override
						public void onClick(int which) {
							deleteSchema();
						}
					});
		}
		
		actionSheet.show();
		
	}

	/**
	 * jump to delete schema page
	 * 
	 * */
	private void deleteSchema()
	{

		if (detailsInfo == null)
		{
			return;
		}

		Intent dsIntent = new Intent(this, DealWithSchema.class);
		DealWithSchemaInter dealWithSchemaInter2 = new DealWithSchemaInter();
		dealWithSchemaInter2.setInterType(2);
		dealWithSchemaInter2.setSid(detailsInfo.schema.sid + "");
		dsIntent.putExtra("dealWithSchemaInter", dealWithSchemaInter2);
		startActivityForResult(dsIntent, CustomConstants.MODIFY_SCHEMA);
	}

	/**
	 * jump to modify name page
	 * */
	private void motifyName(int code)
	{

		if (detailsInfo == null)
		{
			return;
		}

		Intent mnIntent = new Intent(this, DealWithSchema.class);
		DealWithSchemaInter dealWithSchemaInter = new DealWithSchemaInter();
		dealWithSchemaInter.setInterType(code);
		dealWithSchemaInter.setSid(detailsInfo.schema.sid + "");
		dealWithSchemaInter.setType(detailsInfo.schema.type);
		dealWithSchemaInter.setNm(detailsInfo.schema.name);

		dealWithSchemaInter.setSchema2(detailsInfo.schema);

		mnIntent.putExtra("dealWithSchemaInter", dealWithSchemaInter);
		startActivityForResult(mnIntent, CustomConstants.MODIFY_SCHEMA);
	}

	public abstract boolean getPageType();

	public abstract void initDataView();

	public abstract View getPopParent();

}
