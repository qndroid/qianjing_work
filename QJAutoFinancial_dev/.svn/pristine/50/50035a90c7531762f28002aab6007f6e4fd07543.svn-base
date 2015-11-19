package com.qianjing.finance.ui.activity.assemble.recommand;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.recommand.P2pCardModel;
import com.qianjing.finance.model.recommand.P2pRedeemResult;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.indictorview.CenterTextView;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * @description 推荐组合取现页面
 * @author majinxin
 * @date 2015年9月15日
 */
public class RecommandRedeemActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private ImageView bankIconImageView;
	private TextView bankNameTextView;
	private TextView cardNumberTextView;
	private TextView alterTextView;
	private EditText inputMoneyeditText;
	private CenterTextView mTipInfoView;
	private Button confirmBuybutton;
	private InputDialog.Builder inputDialog;
	/**
	 * data
	 */
	private TypedArray bankImage;
	private P2pCardModel mCardMdoel;
	private String mMaxRedeemMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommand_redeem);
		initData();
		initView();
		// 请求银行卡信息
		requestCardDetail();
		// 请求免费取出次数
		requestFeeCount();
	}

	private void initData()
	{
		Intent intent = getIntent();
		mMaxRedeemMoney = intent.getStringExtra("max_redeem");
	}

	private void initView()
	{
		setTitleWithId(R.string.recommand_redeem);
		setLoadingUncancelable();
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);

		bankIconImageView = (ImageView) findViewById(R.id.iv_image);
		bankNameTextView = (TextView) findViewById(R.id.tv_bankname);
		cardNumberTextView = (TextView) findViewById(R.id.tv_bankcard);
		inputMoneyeditText = (EditText) findViewById(R.id.edit_money);
		inputMoneyeditText.setHint(getString(R.string.recommand_redeem_max)
				+ StringHelper.formatString(mMaxRedeemMoney, StringFormat.FORMATE_1) + getString(R.string.YUAN));
		inputMoneyeditText.addTextChangedListener(watcher);
		alterTextView = (TextView) findViewById(R.id.tv_alerttext);
		mTipInfoView = (CenterTextView) findViewById(R.id.tip_info_view);
		confirmBuybutton = (Button) findViewById(R.id.btn_recharge);
		confirmBuybutton.setEnabled(false);
		confirmBuybutton.setOnClickListener(this);

		bankImage = getResources().obtainTypedArray(R.array.bank_image);
	}

	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			if (inputMoneyeditText.getText().toString() != null && !inputMoneyeditText.getText().toString().equals("")
					&& !inputMoneyeditText.getText().toString().equals("."))
			{
				String text = inputMoneyeditText.getText().toString();
				if (TextUtils.equals(".", text))
				{
					inputMoneyeditText.setText("");
					return;
				}

				if (Double.parseDouble(text) > Double.parseDouble(mMaxRedeemMoney))
				{
					alterTextView.setText(getString(R.string.recommand_redeem_max_info)
							+ StringHelper.formatString(mMaxRedeemMoney, StringFormat.FORMATE_1)
							+ getString(R.string.YUAN));
					confirmBuybutton.setEnabled(false);
				}
				else
				{
					alterTextView.setText("");
					confirmBuybutton.setEnabled(true);
				}
			}
			else
			{
				confirmBuybutton.setEnabled(false);
			}
		}
	};

	/**
	 * 发送快速购买请求
	 */
	private void requestCardDetail()
	{
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.P2P_RECOMMAND_CARD, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
				}
			}
		}, null);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0x01:
				handleDetail(msg.obj.toString());
				break;
			case 0x02:
				handleCount(msg.obj.toString());
				break;
			case 0x03:
				handleResult(msg.obj.toString());
				break;
			}
		};
	};

	protected void handleDetail(String strJson)
	{
		dismissLoading();
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject jsonObj = object.optJSONObject("data");
				mCardMdoel = new P2pCardModel();
				mCardMdoel.cardNo = jsonObj.optString("cardNo");
				mCardMdoel.bankName = jsonObj.optString("bankName");
				mCardMdoel.bankCode = jsonObj.optString("bankCode");
				mCardMdoel.acctName = jsonObj.optString("acctName");
				mCardMdoel.idNo = jsonObj.optString("idNo");
				/**
				 * 更新UI
				 */
				updateUI();
				break;
			default:
				showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{
		}
	}

	private void updateUI()
	{
		bankIconImageView.setImageResource(bankImage.getResourceId(getBankImage(mCardMdoel.bankName), -1));
		bankNameTextView.setText(mCardMdoel.bankName);
		cardNumberTextView.setText(StringHelper.hintCardNum(mCardMdoel.cardNo));
	}

	private void requestFeeCount()
	{
		AnsynHttpRequest.requestByPost(this, UrlConst.P2P_RECOMMAND_COUNT, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
				}
			}
		}, null);
	}

	private void handleCount(String data)
	{
		try
		{
			JSONObject object = new JSONObject(data);
			int ecode = object.optInt("ecode");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject jsonObj = object.optJSONObject("data");
				// String count = jsonObj.optString("count");
				// String fee = jsonObj.optString("fee");
				String info = jsonObj.optString("info");
				/**
				 * 更新UI
				 */
				mTipInfoView.setText(info);
				break;
			default:
				break;
			}
		}
		catch (Exception e)
		{
		}
	}

	private SpannableString getSpannableString(String source, int start, int end, int bigSize, int smallSize)
	{
		SpannableString result = new SpannableString(source);
		/**
		 * 前面的小字体
		 */
		if (start >= 0)
		{
			result.setSpan(new AbsoluteSizeSpan(smallSize, true), 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		// 中间大字体
		result.setSpan(new AbsoluteSizeSpan(bigSize, true), start, source.length() - end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		/**
		 * 结尾小字体
		 */
		result.setSpan(new AbsoluteSizeSpan(smallSize, true), source.length() - end, source.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return result;
	}

	/**
	 * 获取银行卡icon
	 * 
	 * @param bankName
	 * @return
	 */
	private int getBankImage(String bankName)
	{
		String[] mBankNameArray = getResources().getStringArray(R.array.bank_name);
		for (int i = 0; i < mBankNameArray.length; i++)
		{
			if (bankName.contains(mBankNameArray[i]))
			{
				return i;
			}
		}
		return mBankNameArray.length;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		case R.id.btn_recharge:
			showDialog();
			break;
		}
	}

	private void showDialog()
	{
		inputDialog = new InputDialog.Builder(this, null);
		inputDialog.setTitle("取现"
				+ StringHelper.formatString(inputMoneyeditText.getText().toString(), StringFormat.FORMATE_1)
				+ getString(R.string.YUAN));
		inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String pwd = inputDialog.getEditText().getText().toString();
				if (StringHelper.isBlank(pwd))
				{
					Toast.makeText(RecommandRedeemActivity.this, "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
				}
				else
				{
					requestRedeem(pwd);
				}

				dialog.dismiss();
			}
		});
		inputDialog.create().show();
	}

	private void requestRedeem(String passwd)
	{
		showLoading();
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("sum", inputMoneyeditText.getText());
		params.put("cd", mCardMdoel.cardNo);
		params.put("pwd", passwd);
		AnsynHttpRequest.requestByPost(this, UrlConst.P2P_RECOMMAND_REDEEM, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
				}
			}
		}, params);
	}

	private void handleResult(String data)
	{
		dismissLoading();
		try
		{
			JSONObject object = new JSONObject(data);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject jsonObj = object.optJSONObject("data");
				P2pRedeemResult result = new P2pRedeemResult();
				result.applyTime = jsonObj.optString("apply_time");
				result.bank = jsonObj.optString("bank");
				result.card = jsonObj.optString("card");
				result.sum = jsonObj.optString("sum");
				result.arriveTime = jsonObj.optString("arrive_time");
				result.weekDay = jsonObj.optString("weekday");

				Intent intent = new Intent(this, RecommandRedeemResultActivity.class);
				intent.putExtra("result", result);
				startActivityForResult(intent, Const.RECOMMAND_REDEEM_FLOW);
				break;
			default:
				showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case Const.RECOMMAND_REDEEM_FLOW:
			finish();
			break;
		}
	}
}
