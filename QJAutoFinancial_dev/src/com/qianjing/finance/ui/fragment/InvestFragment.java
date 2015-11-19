
package com.qianjing.finance.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.model.p2p.P2PSteadyStateResponse;
import com.qianjing.finance.model.p2p.TokenResponse;
import com.qianjing.finance.model.quickbuy.QuickBuyType;
import com.qianjing.finance.model.recommand.RecommendResponse;
import com.qianjing.finance.model.recommand.RecommendResponse.Recommend;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.CommonCallBack;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.StreamUtil;
import com.qianjing.finance.net.connection.ThreadPoolUtil;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.ui.activity.assemble.quickbuy.QuickFundDetailActivity;
import com.qianjing.finance.ui.activity.assemble.type.AssembleChildrenActivity;
import com.qianjing.finance.ui.activity.assemble.type.AssembleDreamActivity;
import com.qianjing.finance.ui.activity.assemble.type.AssembleHouseActivity;
import com.qianjing.finance.ui.activity.assemble.type.AssembleMarryActivity;
import com.qianjing.finance.ui.activity.assemble.type.AssemblePensionActivity;
import com.qianjing.finance.ui.activity.common.AdsContentsActivity;
import com.qianjing.finance.ui.activity.common.AssembleIntroduceActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity.LoginTarget;
import com.qianjing.finance.ui.activity.common.MainActivity;
import com.qianjing.finance.ui.activity.common.P2PWebActivity;
import com.qianjing.finance.ui.activity.custom.CustomDetailActivity;
import com.qianjing.finance.ui.activity.custom.VIPCustomActivity;
import com.qianjing.finance.ui.activity.fund.search.FundActivity;
import com.qianjing.finance.ui.activity.wallet.WalletActivity;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.Network;
import com.qianjing.finance.util.QJColor;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.RotateTextView;
import com.qianjing.finance.view.custom.listener.ScrollViewListener;
import com.qianjing.finance.view.scrollview.ListenerScrollView;
import com.qianjing.finance.widget.wheelview.ImageCycleView;
import com.qianjing.finance.widget.wheelview.ImageCycleView.ImageCycleViewListener;
import com.qjautofinancial.R;

/**
 * @description 投资主页Fragment
 * @date 2015年8月20日
 */
public class InvestFragment extends Fragment implements OnClickListener {
	/**
	 * UI
	 */
	private MainActivity mContext;
	private View mContentView;
	private ImageCycleView mAdView;
	private ListenerScrollView pullScrollView;
	private RelativeLayout customVipLayout;
	private RelativeLayout pensionLayout;
	private RelativeLayout educationLayout;
	private RelativeLayout houseLayout;
	private RelativeLayout marryLayout;
	private RelativeLayout dreamLayout;
	private RelativeLayout huoqiLayout;
	private RelativeLayout fundSelectedLayout;
	private LinearLayout deShenView;
	private TextView jingQuNameView;
	private TextView jingQuView;
	private RelativeLayout jingQuLayout;
	private TextView wenjianNameView;
	private TextView wenjianView;
	private RelativeLayout wenJianLayout;
	private TextView baoShouNameView;
	private TextView baoaShouView;
	private RelativeLayout baoShouLayout;
	private RelativeLayout rlSteady;
	private TextView tvSteadyProfitRatio;
	private TextView tvSteadyName;
	private TextView tvSteadyProfitName;
	private RelativeLayout rlTitle;
	private RelativeLayout fixInvestEnter;
	private RotateTextView rotateTv;
	private TextView fixValue;
	private SwipeRefreshLayout qjsrl;

	private int alpha = 0x00ff3b3b;
	private boolean isShowSteady;
	private Recommend recommend;
	/**
	 * data
	 */
	private ArrayList<String> imageUrlList = new ArrayList<String>();
	private HashMap<String, String> urlAndMsg = new HashMap<String, String>();
	private ArrayList<QuickBuyType> quickFundList = new ArrayList<QuickBuyType>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rlTitle = (RelativeLayout) getActivity().findViewById(R.id.title_bar);
		mContext = (MainActivity) getActivity();

		initImageLoader();
		requestADPicture();
		requestFixedInvestData();

		RequestManager.request(mContext, UrlConst.P2P_STEADY_STATE, null, P2PSteadyStateResponse.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				if (model.stateCode == ErrorConst.EC_OK) {
					P2PSteadyStateResponse steadyState = (P2PSteadyStateResponse) model;
					isShowSteady = steadyState.state.state == 1 ? true : false;
				}
				requestQuickFundList(true);
			}

			@Override
			public void fail() {
				requestQuickFundList(true);
			}
		});
	}

	private void requestFixedInvestData() {
		AnsynHttpRequest.requestByPost(mContext, UrlConst.FIXED_INVEST_ENTER_DATA, new HttpCallBack() {
			@Override
			public void back(final String data, int status) {

			    mContext.runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        if (StringHelper.isBlank(data)) {
                            mContext.showHintDialog(getString(R.string.net_prompt));
                            return;
                        }
                        try {
                            JSONObject jsondata = new JSONObject(data);
                            if (jsondata.optInt("ecode") == 0) {
                                JSONObject data0 = jsondata.optJSONObject("data");
                                rotateTv.setText(data0.optString("limitTime") + "天");
                                fixValue.setText(data0.optString("rate"));
                            } else {
                                mContext.showHintDialog(jsondata.optString("emsg"));
                            }
                        } catch (Exception e) {
                             mContext.showHintDialog(getString(R.string.net_data_error));
                        }
                    }
                });
			}
		}, null);
	}

	/** 为Fragment加载布局时调用 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.activity_quick_new_layout, null);
		mAdView = (ImageCycleView) mContentView.findViewById(R.id.ad_view);
		customVipLayout = (RelativeLayout) mContentView.findViewById(R.id.custom_vip_layout);
		customVipLayout.setOnClickListener(this);
		pensionLayout = (RelativeLayout) mContentView.findViewById(R.id.pension_layout);
		pensionLayout.setOnClickListener(this);
		educationLayout = (RelativeLayout) mContentView.findViewById(R.id.education_layout);
		educationLayout.setOnClickListener(this);
		houseLayout = (RelativeLayout) mContentView.findViewById(R.id.buy_house_layout);
		houseLayout.setOnClickListener(this);
		marryLayout = (RelativeLayout) mContentView.findViewById(R.id.marry_layout);
		marryLayout.setOnClickListener(this);
		dreamLayout = (RelativeLayout) mContentView.findViewById(R.id.dream_layout);
		dreamLayout.setOnClickListener(this);
		fundSelectedLayout = (RelativeLayout) mContentView.findViewById(R.id.fund_layout);
		fundSelectedLayout.setOnClickListener(this);
		huoqiLayout = (RelativeLayout) mContentView.findViewById(R.id.huoqi_layout);
		huoqiLayout.setOnClickListener(this);
		deShenView = (LinearLayout) mContentView.findViewById(R.id.de_shen_layout);
		deShenView.setOnClickListener(this);
		jingQuNameView = (TextView) mContentView.findViewById(R.id.jingqu_view);
		jingQuView = (TextView) mContentView.findViewById(R.id.jingqu_value_view);
		jingQuLayout = (RelativeLayout) mContentView.findViewById(R.id.jingqu_layout);
		jingQuLayout.setOnClickListener(this);
		wenjianNameView = (TextView) mContentView.findViewById(R.id.wenjian_view);
		wenjianView = (TextView) mContentView.findViewById(R.id.wenjian_value_view);
		wenJianLayout = (RelativeLayout) mContentView.findViewById(R.id.wenjian_layout);
		wenJianLayout.setOnClickListener(this);
		baoShouNameView = (TextView) mContentView.findViewById(R.id.baoshou_view);
		baoaShouView = (TextView) mContentView.findViewById(R.id.baoshou_value_view);
		baoShouLayout = (RelativeLayout) mContentView.findViewById(R.id.baoshou_layout);
		baoShouLayout.setOnClickListener(this);
		fixInvestEnter = (RelativeLayout) mContentView.findViewById(R.id.rl_fix_invest);
		fixInvestEnter.setOnClickListener(this);
		rotateTv = (RotateTextView) mContentView.findViewById(R.id.rtv_fix_day);
		fixValue = (TextView) mContentView.findViewById(R.id.tv_fix_value);

		qjsrl = (SwipeRefreshLayout) mContentView.findViewById(R.id.srl_view);
		qjsrl.setColorSchemeColors(QJColor.PROFIT_RED.getColor());
		qjsrl.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				initImageLoader();
				requestFixedInvestData();
				requestQuickFundList(false);
				requestADPicture();
			}
		});

		rlSteady = (RelativeLayout) mContentView.findViewById(R.id.rl_steady);
		rlSteady.setOnClickListener(this);
		tvSteadyProfitRatio = (TextView) mContentView.findViewById(R.id.tv_steady_profit_ratio);
		tvSteadyName = (TextView) mContentView.findViewById(R.id.tv_steady_name);
		tvSteadyProfitName = (TextView) mContentView.findViewById(R.id.tv_steady_profit_ratio_name);

		pullScrollView = (ListenerScrollView) mContentView.findViewById(R.id.pull_to_refresh_scrollview);
		pullScrollView.setScrollViewListener(new ScrollViewListener() {

			private int unit = 0;
			private double max;
			private double yHex;
			private int maxScroll = 270;
			private int range = 50;

			@Override
			public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {

				if (y < 100) {
					pullScrollView.setBackgroundColor(0XFF192847);
				} else {
					pullScrollView.setBackgroundColor(0XFFFFFFFF);
				}
				if (y > range && y <= maxScroll) {
					max = maxScroll - range;
					yHex = ((double) (y - range)) / max;
					unit = (int) (yHex * 0xff);
					rlTitle.setBackgroundColor(alpha + unit * 0X1000000);
					CustomConstants.CURRENT_COLOR = alpha + unit * 0X1000000;
				}

				if (y <= range) {
					rlTitle.setBackgroundColor(0X00FF3B3B);
					CustomConstants.CURRENT_COLOR = 0X00FF3B3B;
				}
			}

		});
		return mContentView;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				handleADData(msg.obj.toString());
				break;
			case 0x02:
				handleFundList(msg.obj.toString());
				break;
			case 0x03:
				handleError();
				break;
			case 0x04:
				analysisGetUserDate(msg.obj.toString());
				break;
			}
		};
	};

	/**
	 * 发送快速购买请求
	 */
	private void requestQuickFundList(boolean isShow) {
		if (isShow) {
			mContext.showLoading();
		}
		AnsynHttpRequest.requestByPost(mContext, UrlConst.QUICK_BUY_LIST, fundListCallback, null);
	}

	private void handleError() {
		// tvTitle.setVisibility(View.VISIBLE);
		// pullScrollView.onRefreshComplete();
	}

	protected void handleFundList(String strJson) {
		dismissLoadView();

		try {
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String reason = object.optString("emsg");
			switch (ecode) {
			case ErrorConst.EC_OK:
				JSONArray fundArray = object.optJSONArray("data");
				quickFundList.clear();
				for (int i = 0; i < fundArray.length(); i++) {
					JSONObject tempObject = fundArray.optJSONObject(i);
					QuickBuyType quickType = new QuickBuyType();
					quickType.setName(tempObject.optString("name"));
					quickType.setHalfYearTitle(tempObject.optString("yield_text"));
					quickType.setHalfYearRate(tempObject.optDouble("yield"));
					quickType.setMinScript(tempObject.optString("min_subscript"));
					quickType.setRiskText(tempObject.optString("risk_text"));
					quickType.setRiskType(tempObject.optInt("risk_type"));
					quickFundList.add(quickType);
				}

				mContext.showLoading();
				RequestManager.request(mContext, UrlConst.P2P_RECOMMAND, null, RecommendResponse.class, new XCallBack() {
					@Override
					public void success(BaseModel model) {
						mContext.dismissLoading();

						if (model.stateCode == ErrorConst.EC_OK)
							recommend = ((RecommendResponse) model).recommend;
						updateUI();
					}

					@Override
					public void fail() {
						mContext.dismissLoading();
						updateUI();
					}
				});
				break;
			default:
				mContext.showHintDialog(reason);
				break;
			}
		} catch (Exception e) {
		}

	}

	private void dismissLoadView() {
		mContext.dismissLoading();
		qjsrl.setRefreshing(false);
	}

	private HttpCallBack fundListCallback = new HttpCallBack() {
		@Override
		public void back(String data, int status) {
			if (data != null && !data.equals("")) {
				mHandler.sendMessage(mHandler.obtainMessage(0x02, data));
			} else {
				mHandler.sendEmptyMessage(0x03);
			}
		}
	};

	/**
	 * 根据服务器返回的数据更新UI
	 */
	private void updateUI() {
		jingQuNameView.setText(quickFundList.get(0).getName());
		wenjianNameView.setText(quickFundList.get(1).getName());
		baoShouNameView.setText(quickFundList.get(2).getName());
		jingQuView.setText(StringHelper.formatString(String.valueOf(quickFundList.get(0).getHalfYearRate()), StringFormat.FORMATE_2));
		wenjianView.setText(StringHelper.formatString(String.valueOf(quickFundList.get(1).getHalfYearRate()), StringFormat.FORMATE_2));
		baoaShouView.setText(StringHelper.formatString(String.valueOf(quickFundList.get(2).getHalfYearRate()), StringFormat.FORMATE_2));
		if (isShowSteady && recommend != null) {
			baoShouLayout.setVisibility(View.GONE);
			rlSteady.setVisibility(View.VISIBLE);
			tvSteadyName.setText(recommend.name);
			tvSteadyProfitRatio.setText(recommend.ratio);
			tvSteadyProfitName.setText(recommend.ratioName);
		}
	}

	private void requestADPicture() {
	    ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (StreamUtil.getData(UrlConst.URL_ADS) != null) {
                    String datas = new String(StreamUtil.getData(UrlConst.URL_ADS));
                    mHandler.sendMessage(mHandler.obtainMessage(0x01, datas));
                }
            }
        });
	}

	public void handleADData(String result) {
		if (null == result || "".equals(result)) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			String ecode = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");
			if ("0".equals(ecode)) {
				JSONObject JsArray = new JSONObject(result);
				JSONArray nameList = JsArray.getJSONArray("data");
				String[] arr = new String[nameList.length()];
				imageUrlList.clear();
				urlAndMsg.clear();
				for (int i = 0; i < nameList.length(); i++) {
					arr[i] = nameList.getString(i);
					JSONObject jsonObject2 = new JSONObject(arr[i]);
					String AddPicture = jsonObject2.getString("size_720_226");
					imageUrlList.add(AddPicture);
					urlAndMsg.put(AddPicture, arr[i]);
					mAdView.setImageResources(imageUrlList, new ImageCycleViewListener() {
						@Override
						public void onImageClick(int position, View imageView) {
							Intent intent = new Intent(mContext, AdsContentsActivity.class);
							intent.putExtra("url", urlAndMsg.get(imageView.getTag().toString().trim()));
							startActivity(intent);
						}

						@Override
						public void displayImage(String imageURL, ImageView imageView) {
							ImageLoader.getInstance().displayImage(imageURL, imageView);
						}
					});
				}
			} else {
				mContext.showHintDialog(Errormsg);
			}
		} catch (Exception e) {
			mContext.showHintDialog("数据解析异常!");
		}
	}

	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_waiting)
				.showImageForEmptyUri(R.drawable.bg_waiting).showImageOnFail(R.drawable.bg_waiting).cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext.getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.fund_layout:
			if (Network.checkNetWork(mContext)) {
				intent = new Intent(mContext, FundActivity.class);
				startActivity(intent);
			} else {
				mContext.showHintDialog(getString(R.string.net_prompt));
			}
			break;
		case R.id.pension_layout:
			mContext.openActivity(AssemblePensionActivity.class);
			break;
		case R.id.education_layout:
			mContext.openActivity(AssembleChildrenActivity.class);
			break;
		case R.id.buy_house_layout:
			mContext.openActivity(AssembleHouseActivity.class);
			break;
		case R.id.marry_layout:
			mContext.openActivity(AssembleMarryActivity.class);
			break;
		case R.id.dream_layout:
			mContext.openActivity(AssembleDreamActivity.class);
			break;
		case R.id.huoqi_layout:
			// 无用户信息则跳转到登录页
			if (UserManager.getInstance().getUser() == null) {
				Bundle bundle = new Bundle();
				bundle.putInt("LoginTarget", LoginTarget.WALLET.getValue());
				mContext.openActivity(LoginActivity.class, bundle);
			} else {
				mContext.openActivity(WalletActivity.class);
			}
			break;
		case R.id.de_shen_layout:
			mContext.openActivity(AssembleIntroduceActivity.class);
			break;
		case R.id.custom_vip_layout:
			if (UserManager.getInstance().getUser() == null) {
				mContext.openActivity(LoginActivity.class);
			} else {
				if (Network.checkNetWork(mContext)) {
					requestGetUserHibit();
				} else {
					mContext.showHintDialog(getString(R.string.net_prompt));
				}
			}
			break;
		case R.id.jingqu_layout:
			if (Network.checkNetWork(mContext)) {
				if (quickFundList.size() != 0 && quickFundList.get(0) != null) {
					intent = new Intent(mContext, QuickFundDetailActivity.class);
					intent.putExtra("type", quickFundList.get(0).getRiskType());
					startActivity(intent);
				} else {
					mContext.showHintDialog(getString(R.string.refresh_continue));
				}
			} else {
				mContext.showHintDialog(getString(R.string.net_prompt));
			}
			break;
		case R.id.wenjian_layout:
			if (Network.checkNetWork(mContext)) {
				if (quickFundList.size() != 0 && quickFundList.get(1) != null) {
					intent = new Intent(mContext, QuickFundDetailActivity.class);
					intent.putExtra("type", quickFundList.get(1).getRiskType());
					startActivity(intent);
				} else {
					mContext.showHintDialog(getString(R.string.refresh_continue));
				}
			} else {
				mContext.showHintDialog(getString(R.string.net_prompt));
			}
			break;
		case R.id.baoshou_layout:
			if (Network.checkNetWork(mContext)) {
				if (quickFundList.size() != 0 && quickFundList.get(2) != null) {
					intent = new Intent(mContext, QuickFundDetailActivity.class);
					intent.putExtra("type", quickFundList.get(2).getRiskType());
					startActivity(intent);
				} else {
					mContext.showHintDialog(getString(R.string.refresh_continue));
				}
			} else {
				mContext.showHintDialog(getString(R.string.net_prompt));
			}
			break;
		case R.id.rl_fix_invest:
		    if(UserManager.getInstance().getUser()!=null){
		        jumpByToken();
		    }else{
		        mContext.openActivity(LoginActivity.class);
		    }
			break;
		}
	}

	private void jumpByToken() {
		com.qianjing.finance.net.request.RequestManager.requestCommon(mContext, UrlConst.P2P_CREATE_TOKEN, null, TokenResponse.class,
                new XCallBack() {
            @Override
            public void success(BaseModel model) {
//                LogUtils.syso("success");
                if (Integer.parseInt(model.strStateCode) == ErrorConst.EC_OK) {
                    TokenResponse getToken = (TokenResponse) model;
                    Intent p2pIntent = new Intent(getActivity(), P2PWebActivity.class);
                    p2pIntent.putExtra("url", UrlConst.P2P_INVEST + getToken.token.token);
                    startActivity(p2pIntent);
                }
                else
                    mContext.showToast(model.strErrorMessage);
            }

            @Override
            public void fail() {
//                LogUtils.syso("fail");
            }
        });
	
	}

	/**
	 * 发送尊享定制请求
	 */
	public void requestGetUserHibit() {
		mContext.showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("type", 7);
		AnsynHttpRequest.requestByPost(getActivity(), UrlConst.GET_USER_HIBIT, new HttpCallBack() {
			@Override
			public void back(String data, int status) {
				mHandler.sendMessage(mHandler.obtainMessage(0x04, data));
			}
		}, hashTable);
	}

	protected void analysisGetUserDate(String jsonStr) {
		dismissLoadView();
		if (jsonStr == null || "".equals(jsonStr)) {
			mContext.showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try {
			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			JSONObject data = jobj.optJSONObject("data");

			if (ecode == 0) {
				String parasStr = data.optString("paras");
				if ("".equals(parasStr)) {
					Intent customIntent = new Intent(getActivity(), VIPCustomActivity.class);
					startActivity(customIntent);
				} else {
					JSONObject paras = new JSONObject(parasStr);
					int age = paras.optInt("age");
					String initStr = paras.optString("init");
					String moneyStr = paras.optString("money");
					String preference = paras.optString("preference");
					String risk = paras.optString("risk");
					if (UserManager.getInstance().getUser() != null) {
						User user = UserManager.getInstance().getUser();
						user.setAge(String.valueOf(age));
						user.setInit(initStr);
						user.setMoney(moneyStr);
						user.setPreference(preference);
						user.setRisk(risk);
					}
					Intent customIntent = new Intent(getActivity(), CustomDetailActivity.class);
					startActivity(customIntent);
				}
			} else {
				mContext.showHintDialog(emsg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mContext.showHintDialog(getString(R.string.net_data_error));
		}
	}

}
