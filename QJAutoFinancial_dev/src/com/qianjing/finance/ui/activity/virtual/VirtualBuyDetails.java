package com.qianjing.finance.ui.activity.virtual;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.activity.purchase.PurchaseBuyActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.view.virtual.FundDetailsView;
import com.qianjing.finance.widget.adapter.base.BaseDetailsAdapter;
import com.qjautofinancial.R;

/**
 * @category:未买组合详情页
 * @author:liuchen
 * */

public class VirtualBuyDetails extends VirtualBaseDetails implements OnClickListener{

	private TextView tvAdviceTitle;
	private TextView tvAdviceContent;
	private TextView tvAdviceLast;
	private Float useableAssets;
	private Button btnInvest;
	private String sid;
	
	ArrayList<Fund> infos = new ArrayList<Fund>();
	private AssembleBase assemble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_virtual_buy_detail);
		setTitleBack();
		
		ImageView rightText = (ImageView) findViewById(R.id.iv_title_edit);
		rightText.setVisibility(View.VISIBLE);
		rightText.setOnClickListener(this);
		
		
		tvAdviceTitle = (TextView) findViewById(R.id.tv_invest_title);
		tvAdviceContent = (TextView) findViewById(R.id.tv_invest_advice);
		tvAdviceLast = (TextView) findViewById(R.id.tv_invest_last);
		btnInvest = (Button) findViewById(R.id.btn_virtual_invest);
		btnInvest.setOnClickListener(this);
		
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		useableAssets = intent.getFloatExtra("useableAssets", 0);
		requestVirtualDetails(sid);
		
	}

	private void initAssembleData() {
		assemble = new AssembleBase();
		
		assemble.setUid(detailsInfo.schema.uid);
		assemble.setSid(detailsInfo.schema.sid+"");
		assemble.setName(detailsInfo.schema.name);
		assemble.setType(Integer.parseInt(detailsInfo.schema.type));
		
		assemble.setChildGoalSum(detailsInfo.schema.education_money+"");
		assemble.setChildYears(detailsInfo.schema.education_year+"");
		assemble.setDreamInitSum(detailsInfo.schema.small_init+"");
		assemble.setDreamMonths(detailsInfo.schema.small_nmonth+"");
		assemble.setHouseGoalSum(detailsInfo.schema.house_money+"");
		assemble.setHouseInitSum(detailsInfo.schema.house_init+"");
		assemble.setHouseYears(detailsInfo.schema.house_year+"");
		assemble.setInvestInitSum(detailsInfo.schema.financial_init+"");
		assemble.setInvestRate(detailsInfo.schema.financial_rate+"");
		assemble.setInvestRisk(detailsInfo.schema.financial_risk+"");
		assemble.setMarryGoalSum(detailsInfo.schema.married_money+"");
		assemble.setMarryInitSum(detailsInfo.schema.married_init+"");
		assemble.setMarryYears(detailsInfo.schema.married_year+"");
		assemble.setPensionCurrentAge(detailsInfo.schema.pension_age+"");
		assemble.setPensionInitSum(detailsInfo.schema.pension_init+"");
		assemble.setPensionMonthAmount(detailsInfo.schema.pension_month+"");
		assemble.setPensionRetireAge(detailsInfo.schema.pension_retire+"");
		
	}

	@Override
	public void initDataView() {
		
		FundDetailsView fdvDetails = (FundDetailsView) findViewById(R.id.fdv_details);
		fdvDetails.setDetailAdapter(new BaseDetailsAdapter() {
			
			@Override
			public int getRiskTypeValue() {
				return (int)detailsInfo.assembly.risk;
			}
			
			@Override
			public List<SparseArray<Object>> getDetailData() {
				
				List<SparseArray<Object>> dataList = new ArrayList<SparseArray<Object>>();
				
				ArrayList<String> nameList = detailsInfo.assembly.abbrevs;
				ArrayList<Float> ratioList = detailsInfo.assembly.ratios;
				ArrayList<String> fundcode = detailsInfo.assembly.fdcodes;
				
				for(int i=0;i<nameList.size();i++){
					SparseArray<Object> sparseArray = new SparseArray<Object>();
					sparseArray.put(1, nameList.get(i));
					sparseArray.put(2, ratioList.get(i));
					sparseArray.put(3, fundcode.get(i));
					dataList.add(sparseArray);

					infos.add(new Fund(detailsInfo.assembly.fdcodes.get(i), detailsInfo.assembly.abbrevs.get(i),
							detailsInfo.assembly.sharetypes.get(i), detailsInfo.assembly.ratios.get(i),0xff000000));
				}
				
				return dataList;
			}

			@Override
			public void setDetailsItemClick(int position) {
				Intent intent = new Intent(VirtualBuyDetails.this,FundDetailsActivity.class);
				intent.putExtra("fdcode", detailsInfo.assembly.fdcodes.get(position));
				intent.putExtra("from_asemble", true);
				startActivity(intent);
			}

			@Override
			public void setRiskTypeClick() {
				showHintDialog(getString(R.string.fen_xian_zhi_shu_shuo_ming),
						getString(R.string.fen_xian_zhi_shu_shuo_ming_nei_rong), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, getString(R.string.zhi_dao_l));
			}
			
			@Override
			public int getNostockRatio() {
				return detailsInfo.assembly.nostock_ratio;
			}

			@Override
			public int getStockRatio() {
				return detailsInfo.assembly.stock_ratio;
			}

			@Override
			public String getStock_text() {
				return detailsInfo.assembly.stock_text;
			}

			@Override
			public String getNostock_text() {
				return detailsInfo.assembly.nostock_text;
			}

			
		});
		setTitleWithString(detailsInfo.schema.name);
		if(useableAssets==0){
			btnInvest.setEnabled(false);
		}
		initAdviceData();
		initAssembleData();
	}

	/**
	 * init invest advice 
	 * 
	 * */
	private void initAdviceData() {
		String type = detailsInfo.schema.type;
		
		if("3".equals(type)){
			if(detailsInfo.assembly.init>detailsInfo.schema.house_init){
				tvAdviceTitle.setText(getString(R.string.invest_plan_advice));
				tvAdviceContent.setText(getString(R.string.invest_advice_item1)
						+detailsInfo.assembly.init+"元");
			}else{
				tvAdviceTitle.setText(getString(R.string.invest_plan));
				tvAdviceContent.setText(getString(R.string.invest_advice_item2)
						+detailsInfo.schema.house_init+"元");
			}
			
		}else if("5".equals(type)){
			
			if(detailsInfo.assembly.init>detailsInfo.schema.married_init){
				tvAdviceTitle.setText(getString(R.string.invest_plan_advice));
				tvAdviceContent.setText(getString(R.string.invest_advice_item1)
						+detailsInfo.assembly.init+"元");
			}else{
				tvAdviceTitle.setText(getString(R.string.invest_plan));
				tvAdviceContent.setText(getString(R.string.invest_advice_item2)
						+detailsInfo.schema.married_init+"元");
			}
			
			
		}else if("1".equals(type)){
			tvAdviceTitle.setText(getString(R.string.invest_plan));
			tvAdviceContent.setText(getString(R.string.invest_advice_item2)
					+detailsInfo.schema.financial_init+"元");
		}else if("2".equals(type)){
			tvAdviceTitle.setText(getString(R.string.invest_plan));
			tvAdviceContent.setText(getString(R.string.invest_advice_item2)
					+detailsInfo.schema.pension_init+"元,每月定投"+detailsInfo.schema.pension_month+"元");
		}else if("4".equals(type)){
			tvAdviceTitle.setText(getString(R.string.invest_plan_advice));
			tvAdviceContent.setText("建议您每月定投"+detailsInfo.assembly.month+"元");
		}else if("6".equals(type)){
			tvAdviceTitle.setText(getString(R.string.invest_plan));
			tvAdviceContent.setText(getString(R.string.invest_advice_item2)+detailsInfo.schema.small_init+"元"
					+detailsInfo.schema.small_nmonth+"个月后预计收益"+detailsInfo.assembly.earnings);
		}
		
		tvAdviceLast.setText(getString(R.string.invest_advice_last));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_virtual_invest:
			jump2PurchasePage();
			break;
			
		case R.id.iv_title_edit:
			showPopupWindow();
			break;
		}
		
	}
	
	
	private void jump2PurchasePage() {

		if(detailsInfo==null){
			return;
		}
		Intent intent = new Intent(this, PurchaseBuyActivity.class);
		PurchaseModel pModel = new PurchaseModel();
		pModel.setSid(detailsInfo.schema.sid + "");
		pModel.setVirtual(true);
		pModel.setSchemaName(detailsInfo.schema.name);
		pModel.setUsableMonay(useableAssets);
		intent.putExtra("purchaseInfo", pModel);
		intent.putParcelableArrayListExtra("fundList", infos);
		intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, assemble);
		startActivityForResult(intent, CustomConstants.VIRTUAL_PUTCHASE);
	}

	@Override
	public View getPopParent() {
		return View.inflate(this, R.layout.activity_virtual_buy_detail, null);
	}

	@Override
	public boolean getPageType() {
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==CustomConstants.MODIFY_SCHEMA){
			requestVirtualDetails(sid);
		}else if(resultCode==CustomConstants.SHUDDOWN_ACTIVITY
				&& requestCode==CustomConstants.MODIFY_SCHEMA){
			finish();
		}
		
	}
	
}
