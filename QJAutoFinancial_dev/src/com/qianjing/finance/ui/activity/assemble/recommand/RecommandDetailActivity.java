package com.qianjing.finance.ui.activity.assemble.recommand;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.recommand.RecommandConfig;
import com.qianjing.finance.model.recommand.RecommandDetailModel;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleNewItemLayout;
import com.qianjing.finance.view.chartview.PieGraph;
import com.qianjing.finance.view.chartview.PieSlice;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @description 推荐产品详情页
 * @author majinxin
 * @date 2015年9月11日
 */
public class RecommandDetailActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 配置颜色等级
	 */
	private final int[] colors = new int[]
	{ R.color.color_5ba7e1, R.color.color_b19ee0, R.color.color_e7a3e5, R.color.color_5a79b7, R.color.color_a6d0e6 };
	/**
	 * UI
	 */
	private Button mBackButton;
	private ImageView mHelpView;
	private TextView mProfitView;
	private TextView mProfitInfoView;
	private TextView mUsableView;
	private TextView mLimitView;
	private TextView mMinValue;
	private PieGraph mPieGraph;
	private LinearLayout mContentLayout;
	private TextView mBuyView;

	/**
	 * data
	 */
	private String mProductName;
	private RecommandDetailModel mDetailModel;
	private String mToken;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommand_detail);
		initData();
		initView();
		requestRecommandDetail(true);
	}

	private void initData()
	{
		Intent intent = getIntent();
		mProductName = intent.getStringExtra("product_name");
	}

	private void initView()
	{
		setTitleWithString(mProductName);
		setLoadingUncancelable();
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);
		mHelpView = (ImageView) findViewById(R.id.im_help);
		mHelpView.setVisibility(View.VISIBLE);
		mHelpView.setOnClickListener(this);

		mProfitView = (TextView) findViewById(R.id.profit_value_view);
		mProfitInfoView = (TextView) findViewById(R.id.product_info_view);
		mUsableView = (TextView) findViewById(R.id.usable_money);
		mLimitView = (TextView) findViewById(R.id.one_day_limit);
		mMinValue = (TextView) findViewById(R.id.qi_gou_money);
		mPieGraph = (PieGraph) findViewById(R.id.pie_graph);
		mContentLayout = (LinearLayout) findViewById(R.id.content_view);
		mBuyView = (TextView) findViewById(R.id.start_buy_btn);
		mBuyView.setOnClickListener(this);

		mDetailModel = new RecommandDetailModel();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		case R.id.im_help:
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("webType", 8);
			intent.putExtra("url", UrlConst.P2P_RECOMMAND_DETAIL_HELP);
			startActivity(intent);
			break;
		case R.id.start_buy_btn:
			// 跳转到wap申购页
			if (UserManager.getInstance().getUser() != null)
			{
				requestToken();
			}
			else
			{
				Intent loginIntent = new Intent(this, LoginActivity.class);
				loginIntent.putExtra("LoginTarget", LoginTarget.RECOMMAND_DETAIL.getValue());
				startActivity(loginIntent);
			}
			break;
		}
	}

	/**
	 * 发送快速购买请求
	 */
	private void requestRecommandDetail(boolean isShow)
	{
		if (isShow)
		{
			showLoading();
		}
		AnsynHttpRequest.requestByPost(this, UrlConst.P2P_RECOMMAND_DETAIL, new HttpCallBack()
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
			case 0x03:
				handleToken(msg.obj.toString());
				break;
			}
		};
	};

	protected void handleDetail(String strJson)
	{
		// Log.e("---->", strJson);
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

				mDetailModel.mProductName = jsonObj.optString("dealName");
				mDetailModel.mProductInfo = jsonObj.optString("rate_name");
				mDetailModel.mRate = jsonObj.optString("dealRate");
				mDetailModel.mUsable = jsonObj.optString("loan");
				mDetailModel.mBuyLimit = jsonObj.optString("buyLimit");
				mDetailModel.mMinBuy = jsonObj.optString("minLoanMoney");
				mDetailModel.mBoundName = jsonObj.optString("bond_name");
				mDetailModel.mBoundRate = jsonObj.optString("bond_ratio");
				mDetailModel.mNoBoundName = jsonObj.optString("other_name");
				mDetailModel.mNoBoundRate = jsonObj.optString("other_ratio");
				mDetailModel.mBid = jsonObj.optString("bid");
				JSONArray array = jsonObj.optJSONArray("conf");
				if (array != null && array.length() > 0)
				{
					ArrayList<RecommandConfig> configList = new ArrayList<RecommandConfig>();
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject data = (JSONObject) array.get(i);
						RecommandConfig config = new RecommandConfig();
						config.name = data.optString("name");
						config.type = data.optString("type");
						config.precent = data.optString("percent");

						configList.add(config);
					}
					mDetailModel.mList = configList;

					/**
					 * 更新UI
					 */
					updateUI();
				}
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

	private void requestToken()
	{
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.P2P_TOKEN, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x03, data));
				}
				else
				{
					mHandler.sendEmptyMessage(0x02);
				}
			}
		}, null);
	}

	protected void handleToken(String string)
	{
		dismissLoading();
		try
		{
			JSONObject object = new JSONObject(string);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject jsonObj = object.optJSONObject("data");
				mToken = jsonObj.optString("tk");
				/**
				 * 拿到token,拼接到地址中，跳转到wap页面
				 */
				StringBuilder url = new StringBuilder(UrlConst.P2P_RECOMMAND_BUY);
				url.append(mDetailModel.mBid).append("&tk=").append(mToken);
				Intent intent = new Intent(this, WebActivity.class);
				intent.putExtra("webType", 8);
				intent.putExtra("url", url.toString());
				startActivity(intent);
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

	/**
	 * 根据服务器返回的数据更新UI
	 */
	private void updateUI()
	{
		mProfitView.setText(getSpannableString(StringHelper.formatString(mDetailModel.mRate, StringFormat.FORMATE_1)
				+ getString(R.string.str_percent), 0, 1, 60, 30));
		mProfitInfoView.setText(mDetailModel.mProductInfo);
		mUsableView.setText(getString(R.string.left_money)
				+ StringHelper.formatString(mDetailModel.mUsable, StringFormat.FORMATE_1) + getString(R.string.YUAN));
		mLimitView.setText(getString(R.string.once_limit)
				+ StringHelper.formatString(mDetailModel.mBuyLimit, StringFormat.FORMATE_1) + getString(R.string.YUAN));
		mMinValue.setText(getSpannableString(mDetailModel.mMinBuy + getString(R.string.YUAN), 0, 1, 17, 13));

		ArrayList<PieSlice> pieNodes = new ArrayList<PieSlice>();
		for (int i = 0; i < mDetailModel.mList.size(); i++)
		{
			mContentLayout.addView(createItem(i, mDetailModel.mList.get(i)));
			pieNodes.add(createPieSlice(i, mDetailModel.mList.get(i)));
		}

		mPieGraph.setDrawText(mDetailModel.mBoundName, mDetailModel.mBoundRate + getString(R.string.str_percent),
				mDetailModel.mNoBoundName, mDetailModel.mNoBoundRate + getString(R.string.str_percent), pieNodes);
	}

	private AssembleNewItemLayout createItem(int index, RecommandConfig config)
	{
		AssembleNewItemLayout assembleItem = new AssembleNewItemLayout(this);
		assembleItem.initData(colors[index], config.name, "",
				StringHelper.formatString(config.precent, StringFormat.FORMATE_1));

		return assembleItem;
	}

	private PieSlice createPieSlice(int index, RecommandConfig config)
	{
		PieSlice slice = new PieSlice();
		slice.setColor(getResources().getColor(colors[index]));
		slice.setValue(Float.parseFloat(config.precent));
		return slice;
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
}
