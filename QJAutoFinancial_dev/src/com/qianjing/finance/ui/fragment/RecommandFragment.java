package com.qianjing.finance.ui.fragment;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.assemble.quickbuy.QuickAssembleBuyActivity;
import com.qianjing.finance.ui.activity.assemble.recommand.RecommandDetailActivity;
import com.qianjing.finance.ui.activity.assemble.recommand.RecommandFundDetailActivity;
import com.qianjing.finance.ui.activity.card.QuickBillActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 推荐产品Fragment页面
 * @author majinxin
 * @date 2015年9月16日
 */
public class RecommandFragment extends Fragment implements OnClickListener
{
	/**
	 * UI
	 */
	private BaseActivity mContext;
	private View mContentView;
	private SwipeRefreshLayout mRefreshView;
	private TextView mProductNameView;
	private TextView mProductProfitView;
	private LinearLayout mProductLayout;
	private TextView mProductMsgTitleView;
	private TextView mProductMsgEndView;
	private TextView mPutInView;
	private TextView mProductInfoView;

	/**
	 * data
	 */
	private String mBid;
	private String mProductName;
	private String mProductProfit;
	private String mProductTitle;
	private String mProductEnd;
	private int mType;
	private String mProdutcInfo;
	private String mToken;
	private int mRisk;
	private String minValue;
	private String mActive;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = (BaseActivity) getActivity();
		requestRecommand(true);

	}

	/** 为Fragment加载布局时调用 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mContentView = inflater.inflate(R.layout.fragment_recommand_ayout, null);
		mRefreshView = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
		mRefreshView.setColorSchemeResources(R.color.red_VI, R.color.red_VI, R.color.red_VI, R.color.red_VI);
		mRefreshView.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				requestRecommand(false);
			}
		});
		mProductNameView = (TextView) mContentView.findViewById(R.id.name_view);
		mProductProfitView = (TextView) mContentView.findViewById(R.id.profit_view);
		mProductLayout = (LinearLayout) mContentView.findViewById(R.id.title_layout);
		mProductLayout.setOnClickListener(this);
		mProductMsgTitleView = (TextView) mContentView.findViewById(R.id.product_msg_title_view);
		mProductMsgEndView = (TextView) mContentView.findViewById(R.id.product_msg_end_view);
		mPutInView = (TextView) mContentView.findViewById(R.id.put_money_view);
		mPutInView.setOnClickListener(this);
		mProductInfoView = (TextView) mContentView.findViewById(R.id.product_info);

		return mContentView;
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
				handleCardList(msg.obj.toString());
				break;
			case 0x03:
				handleToken(msg.obj.toString());
				break;
			case 0x04:
				handleHYCard(msg.obj.toString());
				break;
			default:
				handleError();
				break;
			}
		};
	};

	/**
	 * 发送快速购买请求
	 */
	private void requestRecommand(boolean isShow)
	{
		if (isShow)
		{
			mContext.showLoading();
		}
		AnsynHttpRequest.requestByPost(mContext, UrlConst.P2P_RECOMMAND, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
				}
				else
				{
					mHandler.sendEmptyMessage(0x09);
				}
			}
		}, null);
	}

	private void handleError()
	{
		mContext.dismissLoading();
		mRefreshView.setRefreshing(false);
	}

	protected void handleDetail(String strJson)
	{
		mContext.dismissLoading();
		mRefreshView.setRefreshing(false);
		try
		{
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode)
			{
			case ErrorConst.EC_OK:
				JSONObject jsonObj = object.optJSONObject("data");
				mProductName = jsonObj.optString("title");
				mProductProfit = jsonObj.optString("deal_rate");
				mProductTitle = jsonObj.optString("attr_first");
				mProductEnd = jsonObj.optString("attr_second");
				mProdutcInfo = jsonObj.optString("rate_name");
				mBid = jsonObj.optString("bid");
				mType = jsonObj.optInt("type");
				mRisk = jsonObj.optInt("risk");
				minValue = jsonObj.optString("min_subscript");
				mActive = jsonObj.optString("active");
				updateUI();
				break;
			default:
				mContext.showHintDialog(reason);
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
		mProductNameView.setText(mProductName);
		mProductProfitView.setText(getSpannableString(StringHelper.formatString(mProductProfit, StringFormat.FORMATE_2)
				+ getString(R.string.str_percent), 0, 1, 72, 22));
		mProductMsgTitleView.setText(mProductTitle);
		mProductMsgEndView.setText(mProductEnd);
		mProductInfoView.setText(mActive);
		mPutInView.setText(mType == 1 ? getString(R.string.start_buying) : getString(R.string.kai_shi_tou_zi));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.put_money_view:
			if (UserManager.getInstance().getUser() != null)
			{
				if (mType == 1)
				{
					requestToken();
				}
				else
				{
					// 判断是否绑卡
					requestCardList();
				}
			}
			else
			{
				Intent loginIntent = new Intent(mContext, LoginActivity.class);
				startActivity(loginIntent);
			}
			break;
		case R.id.title_layout:
			if (mType == 1)
			{
				Intent intent = new Intent(mContext, RecommandDetailActivity.class);
				intent.putExtra("product_name", mProductName);
				// intent.putExtra("type", mType);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(mContext, RecommandFundDetailActivity.class);
				intent.putExtra("type", mRisk);
				intent.putExtra("product_name", mProductName);
				startActivity(intent);
			}
			break;
		}
	}

	private void requestToken()
	{
		mContext.showLoading();
		AnsynHttpRequest.requestByPost(mContext, UrlConst.P2P_TOKEN, new HttpCallBack()
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
					mHandler.sendEmptyMessage(0x09);
				}
			}
		}, null);
	}

	protected void handleToken(String string)
	{
		mContext.dismissLoading();
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
				url.append(mBid).append("&tk=").append(mToken);
				Intent intent = new Intent(mContext, WebActivity.class);
				intent.putExtra("webType", 8);
				intent.putExtra("url", url.toString());
				startActivity(intent);

				break;
			default:
				mContext.showHintDialog(reason);
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
	 * 获取银行卡列表
	 */
	private void requestCardList()
	{
		mContext.showLoading();
		// 请求银行卡信息
		AnsynHttpRequest.requestByPost(mContext, UrlConst.CARD_LIST, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				mContext.dismissLoading();
				if (data != null && !data.equals(""))
				{
					mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
				}
				else
				{
					mContext.showHintDialog("网络不给力!");
				}
			}
		}, null);
	}

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
						mContext.showToast("根据证监会要求，您需要绑定银行卡才能申购基金.");
						startActivity(new Intent(mContext, QuickBillActivity.class));
					}
				}
				break;
			default:
				mContext.showHintDialog(reason);
				break;
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
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
		mContext.showTwoButtonDialog(strMsg, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// 发送银行卡还原接口请求
				mContext.showLoading();
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
		mContext.showLoading();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("cd", cardnum);
		AnsynHttpRequest.requestByPost(mContext, UrlConst.CARD_HTYCARD, new HttpCallBack()
		{
			@Override
			public void back(String data, int status)
			{
				mContext.dismissLoading();
				if (data != null && !data.equals(""))
				{

					mHandler.sendMessage(mHandler.obtainMessage(0x04, data));
				}
				else
				{
					mContext.showHintDialog("网络不给力");
				}
			}
		}, map);
	}

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
				mContext.showToast("银行卡已成功还原");
				startAnotherActivity();
				break;
			default:
				mContext.showHintDialog(reason);
				break;
			}
		}
		catch (Exception e)
		{
			mContext.showHintDialog("数据解析异常!");
		}
	}

	/**
	 * 跳转到组合申购页面
	 */
	private void startAnotherActivity()
	{
		Intent intent = new Intent(mContext, QuickAssembleBuyActivity.class);
		intent.putExtra("isFromRecommend", true);
		intent.putExtra("type", mRisk);
		intent.putExtra("min_value", minValue);
		intent.putExtra("product_name", mProductName);
		startActivity(intent);
	}

}
