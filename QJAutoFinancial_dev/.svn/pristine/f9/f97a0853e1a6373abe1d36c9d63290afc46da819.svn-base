package com.qianjing.finance.ui.activity.custom;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.common.CustomDetails;
import com.qianjing.finance.model.common.VIPCustom;
import com.qianjing.finance.model.fund.Fund;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.model.quickbuy.FundItemDetails;
import com.qianjing.finance.model.quickbuy.QuickBuyDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.response.ParseUtil;
import com.qianjing.finance.net.response.model.ResponseBase;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.assemble.AssembleBuyActivity;
import com.qianjing.finance.ui.activity.assemble.quickbuy.QuickFundDetailActivity;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.fund.buy.FundDetailsActivity;
import com.qianjing.finance.ui.activity.purchase.PurchaseBuyActivity;
import com.qianjing.finance.ui.fragment.details.AdviceFragment;
import com.qianjing.finance.ui.fragment.details.ConfigFragment;
import com.qianjing.finance.util.Common;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.FloatScrollView;
import com.qianjing.finance.view.indictorview.IndiactorView;
import com.qianjing.finance.view.slidingtabstrip.PagerSlidingTabStrip;
import com.qianjing.finance.view.virtual.FundDetailsView;
import com.qianjing.finance.widget.adapter.base.BaseDetailsAdapter;
import com.qjautofinancial.R;

public class CustomDetailActivity extends BaseActivity implements OnClickListener{

	protected static final int TYPE_CARD_LIST = 0;
	protected static final int TYPE_CUSTOM_DETAILS = 1;
	protected static final int TYPE_CARD_HYCARD = 2;
	protected static final int TYPE_VIRTUAL_ASSETS = 3;
	
	private FundDetailsView fdvVeiw;
	private VIPCustom customInfo;
	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;
	/** 有效银行卡容器 */
	private ArrayList<Card> listCard = new ArrayList<Card>();
	/** 已解绑银行卡容器 */
	private ArrayList<Card> listUnboundedCard = new ArrayList<Card>();
	/** 全部银行卡容器 */
	ArrayList<Card> listAllCard = new ArrayList<Card>();
	
	private AssembleBase assembleBase;

	
	
    private DisplayMetrics dm;
    private FloatScrollView fsvScroll;
    private PagerSlidingTabStrip pstsTabs;
    private ViewPager vpPager;
    private ViewPagerAdapter adapter;
    private TextView tvMinPurchase;
    private TextView tvRiskTxt;
    private TextView tvProfit;
    private TextView tvProfitTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UMengStatics.onCustomizedPage6View(this);
        
        initView();
    }
    
    private void initView() {
        setContentView(R.layout.activity_custom_details_2_1);
        dm = getResources().getDisplayMetrics();

        fsvScroll = (FloatScrollView) findViewById(R.id.fsv_scroll);
        pstsTabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vpPager = (ViewPager) findViewById(R.id.vp_pager);
        // updateUI();
        fsvScroll.setFloatView(pstsTabs, vpPager);
        
        tvMinPurchase = (TextView) findViewById(R.id.tv_min_purchase);
        tvRiskTxt = (TextView) findViewById(R.id.tv_risk_txt);
        tvProfit = (TextView) findViewById(R.id.tv_profit);
        tvProfitTxt = (TextView) findViewById(R.id.tv_profit_txt);
        ImageView riskExplain = (ImageView) findViewById(R.id.iv_risk_explain);
        riskExplain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

        startBuyButton = (TextView) findViewById(R.id.start_buy_btn);
        startBuyButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                try {
                    jump2RealPurchase();
                } catch (Exception e) {
                }
            }
        });
        initCustomView();

    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            vpPager.setAdapter(adapter);
            pstsTabs.setViewPager(vpPager);
            setTabsValue();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int pstsTabsHeight = pstsTabs.getHeight();
        int fsvScrollHeight = fsvScroll.getHeight();
        LayoutParams layoutParams = vpPager.getLayoutParams();
        layoutParams.height = fsvScrollHeight - pstsTabsHeight;
        vpPager.setLayoutParams(layoutParams);

        super.onWindowFocusChanged(hasFocus);
    }
    
    
    public QuickBuyDetail convertAssembleDetailToQuick(AssembleDetail detail){
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

    public Fragment getFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                ConfigFragment configFragment = new ConfigFragment();
                if (quickBuyDetail != null) {
                    configFragment.setFundDetails(quickBuyDetail);
                }
                fragment = configFragment;
                break;
            case 1:
                AdviceFragment adviceFragment = new AdviceFragment();
                if (quickBuyDetail != null) {
                    adviceFragment.setAdviceText(quickBuyDetail.common);
                }
                fragment = adviceFragment;
                break;
        }
        return fragment;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter implements
            PagerSlidingTabStrip.ViewTabProvider {

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

            View view = View
                    .inflate(CustomDetailActivity.this, R.layout.item_assemble_tab, null);
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
    
    private void setTabsValue() {
        pstsTabs.setShouldExpand(true);
        pstsTabs.setDividerColor(getResources().getColor(R.color.color_00ffffff));
        pstsTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                dm));
        pstsTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                dm));
        pstsTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        pstsTabs.setIndicatorColor(getResources().getColor(R.color.low_stroke));
        pstsTabs.setTabBackground(0);
    }
	
	@Override
	protected void onResume() {
		requestCardList();
		super.onResume();
	}

	private void initCustomView() {
		
		setTitleWithId(R.string.assemble_details);
		setTitleBack();
		fdvVeiw = (FundDetailsView) findViewById(R.id.fdv_vip_view);
		customInfo = (VIPCustom) getIntent().getSerializableExtra("customInfo");
//		btnVirtual = (Button) findViewById(R.id.btn_invest_virtual);
//		btnInvest = (Button) findViewById(R.id.btn_invest);
		TextView rightText = (TextView) findViewById(R.id.title_right_text);
		rightText.setText("更改偏好");
		
		indictorView = (IndiactorView) findViewById(R.id.iv_indictor_view);
		
//		btnVirtual.setOnClickListener(this);
//		btnInvest.setOnClickListener(this);
		rightText.setOnClickListener(this);
		
		checkData();
	}
	
	private void checkData() {
		if(UserManager.getInstance().getUser().getAge()!=null && 
				UserManager.getInstance().getUser().getMoney()!=null && 
				UserManager.getInstance().getUser().getPreference()!=null &&
				UserManager.getInstance().getUser().getInit()!=null &&
				UserManager.getInstance().getUser().getRisk()!=null){
			
			if(assembleBase==null){
				assembleBase = new AssembleBase();
				assembleBase.setSpecialAge(UserManager.getInstance().getUser().getAge());
				assembleBase.setSpecialInitSum(UserManager.getInstance().getUser().getInit());
				assembleBase.setSpecialMoney(UserManager.getInstance().getUser().getMoney());
				assembleBase.setSpecialPref(UserManager.getInstance().getUser().getPreference());
				assembleBase.setSpecialRisk(UserManager.getInstance().getUser().getRisk());
				assembleBase.setType(Const.ASSEMBLE_SPESIAL);
			}
			requestCustomDetails();
		}
	}
	
	
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	private void requestCustomDetails(){
		
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("type", 7);
		hashTable.put("init", UserManager.getInstance().getUser().getInit());
		hashTable.put("age", UserManager.getInstance().getUser().getAge());
		hashTable.put("preference", UserManager.getInstance().getUser().getPreference());
		hashTable.put("risk", UserManager.getInstance().getUser().getRisk());
		hashTable.put("money", UserManager.getInstance().getUser().getMoney());
		
		LogUtils.syso("init"+UserManager.getInstance().getUser().getInit()
		        +"age"+UserManager.getInstance().getUser().getAge()
		        +"preference"+UserManager.getInstance().getUser().getPreference()
		        +"risk"+UserManager.getInstance().getUser().getRisk()
		        +"money"+UserManager.getInstance().getUser().getMoney());
		
		AnsynHttpRequest.requestByPost(this, UrlConst.CAL_ASSEMBLY, new HttpCallBack()
		{

			@Override
			public void back(String data, int status)
			{
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = TYPE_CUSTOM_DETAILS;
				mHandler.sendMessage(msg);
			}
		}, hashTable);
	}
	
	/** 获取银行卡列表 */
	private void requestCardList() {

		showLoading();
		
		AnsynHttpRequest.requestByPost(CustomDetailActivity.this, UrlConst.WALLET_CARD_LIST, new HttpCallBack() {
			@Override
			public void back(String data, int type) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = TYPE_CARD_LIST;
				mHandler.sendMessage(msg);
			}
		}, null);
	}
	
	Handler mHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			String jsonStr = (String) msg.obj;
			switch(msg.what){
			case TYPE_CUSTOM_DETAILS:
//				WriteLog.recordLog("定制详情："+jsonStr);
				LogUtils.syso("定制详情："+jsonStr);
				analysisCustomDetails(jsonStr);
				break;
			case TYPE_CARD_LIST:
				handCardListJson(jsonStr);
				break;
			case TYPE_CARD_HYCARD:
				handHycardJson(jsonStr);
				break;
			case TYPE_VIRTUAL_ASSETS:
				handleVirtualAssets(jsonStr);
				break;
			}
		};
	};
	
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
						msg.what = TYPE_VIRTUAL_ASSETS;
						mHandler.sendMessage(msg);
					}
				}, null);
	}

//	private Button btnVirtual;

//	private Button btnInvest;
    private TextView startBuyButton;
    private QuickBuyDetail quickBuyDetail;
    private IndiactorView indictorView;

	
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
				
				JSONObject dataObj = new JSONObject(data);
				JSONObject assetsData = dataObj.optJSONObject("assets");
				
				
				float usableAssets = (float) assetsData.optDouble("usable_assets");
				
				Intent intent = new Intent();
				PurchaseModel pModel = new PurchaseModel();
				pModel.setVirtual(true);
				pModel.setUsableMonay(usableAssets);
				pModel.setSchemaName("深度定制组合");
				intent.putExtra("purchaseInfo", pModel);
				intent.putExtra(ViewUtil.ASSEMBLE_NAME_FLAG, ViewUtil.ASSEMBLE_VIRTUAL_NAME_SAVE);
				intent.putParcelableArrayListExtra("fundList", mAssembleDetail.getAssembleConfig().getFundList());
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, assembleBase);
				
				intent.setClass(this, PurchaseBuyActivity.class);
				startActivityForResult(intent, CustomConstants.VIRTUAL_PUTCHASE);
				
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
	/**
	 * 请求到定制详情的数据
	 * */
	protected void analysisCustomDetails(String jsonStr) {
		if(jsonStr==null || "".equals(jsonStr)){
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
	        return;
		}
		
		try {
			
			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			
			if(ecode==0){
				
				/**
				 * 暂时兼容性处理
				 * */
				JSONObject data = jobj.optJSONObject("data");
				ResponseBase responseBase = new ResponseBase();
                responseBase.ecode = 0;
                responseBase.jsonObject = data;
				mAssembleDetail = ParseUtil.parseAssembleDetail(responseBase);
				
				quickBuyDetail = convertAssembleDetailToQuick(mAssembleDetail);
				
				JSONObject assembly = data.optJSONObject("assembly");
				
				int risk = assembly.optInt("risk");
				indictorView.setPosition(risk);
				
				tvProfit.setText(assembly.optString("yield") + "%");
                tvProfitTxt.setText(assembly.optString("yield_text"));
                tvRiskTxt.setText(assembly.optString("risk_text"));
                JSONArray optJSONArray = data.optJSONObject("schema").optJSONArray("limit");
                if(!"[]".equals(optJSONArray.toString())){
                    tvMinPurchase.setText("¥" + optJSONArray.getString(0) + "起购");
                }else{
                    tvMinPurchase.setText("--");
                }
				
				updateUI();
				
				dismissLoading();
				
			}else{
				showHintDialog(emsg);
				dismissLoading();
			}
		} catch (Exception e) {
			showHintDialog(getString(R.string.net_data_error));
			dismissLoading();
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
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.title_right_text:
			Intent customIntent = new Intent(CustomDetailActivity.this, VIPCustomActivity.class);
			startActivityForResult(customIntent,0);
			break;
		case R.id.btn_invest_virtual:
			jump2VirtualPurchase();
			break;
		case R.id.btn_invest:
			jump2RealPurchase();
			break;
		}
	}

	private void jump2VirtualPurchase() {
		requsetVirtualTotalAssets();
	}

	private void jump2RealPurchase() {
		
		if (listCard==null || listUnboundedCard==null) {
			showToast("处理银行卡数据发生错误.");
			return;
		}
		
		// 有银行卡则打开申购金额页
		if (!listCard.isEmpty()) {
			mAssembleDetail.setAssembleBase(assembleBase);
			Intent intent = new Intent(CustomDetailActivity.this, AssembleBuyActivity.class);
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
			if (listUnboundedCard.size() > 0){
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
							AnsynHttpRequest.requestByPost(CustomDetailActivity.this, UrlConst.CARD_HTYCARD, new HttpCallBack() {
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) { // 申购结果返回
			setResult(ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE);
			finish();
		}
		
		if(resultCode == CustomConstants.CUSTOM_CHANGE){
			checkData();
		}else if(resultCode == CustomConstants.CUSTOM_UNCHANGE){
			
		}
		
	}
}
	

