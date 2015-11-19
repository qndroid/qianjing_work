package com.qianjing.finance.ui.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.purchase.PurchaseModel;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qjautofinancial.R;


/**
 * @author liuchen
 * @date 2015年4月27日
 */
public class PurchaseBuyActivity extends BaseActivity implements OnClickListener {

	private float money = -1;
	private EditText moneyValue;
	private Button btnBuy;
	private TextView overMsg;
	private ImageView bankPic;
	private TextView bankname;
	private TextView bankCard;
	private ImageView rightArrow;
	private LinearLayout ll_bank_card;
	private TextView tvUplimite;

	private boolean isVirtual;
	private PurchaseModel pModel;
	// 最小申购金额的值
	private float minPurchaseValue;
	// 单笔最大限额
	private float maxPurchaseValue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}


	/**
	 * 初始化界面
	 */
	private void initView() {

		setContentView(R.layout.activity_assemble_buy);

		setTitleWithId(R.string.title_assemble_buy);
		
		setTitleBack();

		
		pModel = (PurchaseModel) getIntent().getSerializableExtra("purchaseInfo");
		
		btnBuy = (Button) findViewById(R.id.btn_buy_next);
		btnBuy.setEnabled(false);
		ll_bank_card = (LinearLayout) findViewById(R.id.ll_bank_card);
		ImageView ensure_bv = (ImageView) findViewById(R.id.iv_ensure_bv);

		bankPic = (ImageView) findViewById(R.id.iv_image);
		bankname = (TextView) findViewById(R.id.tv_bankname);
		bankCard = (TextView) findViewById(R.id.tv_bankcard);
		rightArrow = (ImageView) findViewById(R.id.iv_right_arrow);

		overMsg = (TextView) findViewById(R.id.tv_over_hint);
		tvUplimite = (TextView) findViewById(R.id.tv_uplimite);
		tvUplimite.setText(getString(R.string.useable_advice)+pModel.getUsableMonay()+"元");
		
		LinearLayout llBankItem = (LinearLayout) findViewById(R.id.ll_bank_useable_advice);
		llBankItem.setVisibility(View.INVISIBLE);
		
		minPurchaseValue = pModel.getMinPurchase();
		isVirtual = pModel.isVirtual();
		
		
		btnBuy.setOnClickListener(this);
		ll_bank_card.setOnClickListener(this);
		ensure_bv.setOnClickListener(this);

		moneyValue = (EditText) findViewById(R.id.et_downpayment);
		moneyValue.setHint(getString(R.string.setting_advice) + minPurchaseValue + "元");
		moneyValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String value = moneyValue.getText().toString().trim();

				if (".".equals(value)) {
					moneyValue.setText("");
					return;
				}
				
				if (!"".equals(value)) {

					money = Float.parseFloat(value);

					if (money >= minPurchaseValue && money <= maxPurchaseValue) {
						btnBuy.setEnabled(true);
					} else {
						btnBuy.setEnabled(false);
					}
					if (money<minPurchaseValue) {
						overMsg.setText(getString(R.string.below_able_buy_value) + minPurchaseValue + getString(R.string.money_unit));
						overMsg.setVisibility(View.VISIBLE);
					} else if (money>maxPurchaseValue) {
						overMsg.setText(getString(R.string.beyond_virtual_value));
						overMsg.setVisibility(View.VISIBLE);
					} else {
						overMsg.setVisibility(View.GONE);
					}
				} else {
					btnBuy.setEnabled(false);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		if (isVirtual) {
			
			bankPic.setImageResource(R.drawable.virtual_bank);
			bankname.setText(getString(R.string.virtual_assets));
			bankCard.setText(getString(R.string.virtual_bank_card));
			rightArrow.setVisibility(View.GONE);
			ll_bank_card.setClickable(false);
			maxPurchaseValue = pModel.getUsableMonay();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_buy_next:
			Intent intent = new Intent(PurchaseBuyActivity.this, PurchaseConfirmActivity.class);
			intent.putExtra(ViewUtil.ASSEMBLE_BUY_FLAG, 
					getIntent().getIntExtra(ViewUtil.ASSEMBLE_BUY_FLAG, ViewUtil.ASSEMBLE_VIRTUAL_BUY));
			intent.putExtra("purchaseInfo", pModel);
			intent.putExtra("sum", money);
			intent.putParcelableArrayListExtra("fundList", getIntent().getParcelableArrayListExtra("fundList"));
			intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE));
			startActivityForResult(intent, CustomConstants.VIRTUAL_PUTCHASE);
			setResult(CustomConstants.SHUDDOWN_ACTIVITY);
			break;
		case R.id.iv_ensure_bv:
			break;
		}
	}
}
