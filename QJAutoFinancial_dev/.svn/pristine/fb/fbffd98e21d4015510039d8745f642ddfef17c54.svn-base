
package com.qianjing.finance.ui.activity.common;

import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.handler.AliasHandler;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.activity.NewHandActivity;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.InformList;
import com.qianjing.finance.model.common.InformList.InformListKey.Information;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.helper.RequestActivityHelper;
import com.qianjing.finance.net.ihandle.IHandleActivityState;
import com.qianjing.finance.net.response.model.ResponseActivityState;
import com.qianjing.finance.service.update.UpdateService;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.fragment.AccountFragment;
import com.qianjing.finance.ui.fragment.InvestFragment;
import com.qianjing.finance.ui.fragment.RecommandFragment;
import com.qianjing.finance.ui.fragment.assets.AssembleFragment;
import com.qianjing.finance.ui.fragment.assets.VirtualAssetsFragment;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.BadgeView;
import com.qianjing.finance.view.dialog.CommonDialog;
import com.qianjing.finance.view.dialog.CommonDialog.DialogClickListener;
import com.qianjing.finance.view.dialog.RedBagDialog;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：MainActivity.java
 * @文件作者：fangyang
 * @创建时间：2015年6月23日 下午2:25:33
 * @文件描述：
 * @修改历史：2015年6月23日创建初始版本
 **********************************************************/
public class MainActivity extends BaseActivity implements OnClickListener {

	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private InvestFragment quickBuyFragment;
	private AccountFragment fragAccount;
	private RecommandFragment recommandFragment;

	private RadioGroup radioGroup;
	private RadioButton rbRecommand;
	private RadioButton rbQuickBuy;
	private RadioButton rbAssets;
	private RadioButton rbAccount;
	private TextView mTextTitle;
	public BadgeView badgeView;
	private static boolean isVirtual = false;
	private String strCurrentVersion;
	/**
	 * 当前选中 Fragment索引
	 */
	private int currentFragmentIndex;
	private String lastVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();

		checkVersion();

		checkActivity();// 是否有活动

		initAssetsView();

		initPushAlias();
	}

	/**
	 * @description 注册推送别名
	 * @author fangyan
	 */
	private void initPushAlias() {
		User user = UserManager.getInstance().getUser();
		if (user != null && !StringHelper.isBlank(user.getMobile())) {
			// 注册推送别名
			new AliasHandler(this, user.getMobile()).sendEmptyMessage(0);
		}
	}

	private void initAssetsView() {
		realBtn = (TextView) findViewById(R.id.tv_real_trade);
		virtualBtn = (TextView) findViewById(R.id.tv_virtual_trade);
		realBtn.setSelected(true);
		virtualBtn.setSelected(false);
		realBtn.setTextColor(Color.WHITE);
		virtualBtn.setTextColor(0xff9098b4);
		realBtn.setOnClickListener(this);
		virtualBtn.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		boolean isSignout = intent.getBooleanExtra("isSignout", false);
		if (isSignout) {
			rbRecommand.setChecked(true);
			return;
		}
		/**
		 * 用户信息不为空,则刷新显示
		 */
		if (currentFragmentIndex == 2 && UserManager.getInstance().getUser() != null) {
			/**
			 * 事务必须有开始和结束
			 */
		    initAssetsView();
			transaction = fragmentManager.beginTransaction();
			mTextTitle.setVisibility(View.GONE);
			mTextTitle.setText("");

			if (realFragAssets == null) {
				realFragAssets = new AssembleFragment();
				transaction.add(R.id.content, realFragAssets);
			}

			if (virtualFragAssets == null) {
				virtualFragAssets = new VirtualAssetsFragment();
				transaction.add(R.id.content, virtualFragAssets);
			}

			if (isVirtual) {
				transaction.show(virtualFragAssets);
				transaction.hide(realFragAssets);
			} else {
				transaction.show(realFragAssets);
				transaction.hide(virtualFragAssets);
			}
			transaction.commit();
			return;
		}

		if (currentFragmentIndex == 3 && UserManager.getInstance().getUser() != null) {
			transaction = fragmentManager.beginTransaction();
			mTextTitle.setVisibility(View.VISIBLE);
			mTextTitle.setText(R.string.she_zhi);

			if (fragAccount == null) {
				fragAccount = new AccountFragment();
				transaction.add(R.id.content, fragAccount);
			} else {
				transaction.show(fragAccount);
			}
			transaction.commit();
			return;
		}

		
	}

	/**
	 * @description 请求消息通知列表
	 * @author fangyan
	 */
	private void checkInform() {
		// 没有登录则不验证通知状态
		if (UserManager.getInstance().getUser() == null)
			return;

		// 当用户从消息列表返回到帐户设置中，重置消息通知图标
		if (currentFragmentIndex == 3)
			setTitleImgRight(R.drawable.icon_inform, informListener);

		RequestManager.request(this, UrlConst.PUSH_LIST, null, InformList.class, new XCallBack() {
			@Override
			public void success(BaseModel model) {
				if (model != null) {
					InformList informList = (InformList) model;
					if (informList.keyList != null && informList.keyList.listInform != null) {

						List<Information> listInform = informList.keyList.listInform;
						PrefManager pref = PrefManager.getInstance();
						// 是否已初始化消息列表
						if (!pref.getBoolean(PrefManager.KEY_INIT_INFORM, false)) {
							// 遍历写入配置文件，初始化消息列表已读状态
							for (Information inform : listInform) {
								PrefManager.getInstance().putInt("inform_" + inform.id, PrefManager.STATE_READED);
							}
							pref.putBoolean(PrefManager.KEY_INIT_INFORM, true);
						} else {
							String keyPre = "inform_";
							// 非第一次登陆时，遍历写入配置文件，匹配读取状态
							for (Information inform : listInform) {
								if (pref.getInt(keyPre + inform.id, -1) != PrefManager.STATE_READED) {

									pref.putBoolean(PrefManager.KEY_UNREAD_INFORM, true);
									pref.putInt(keyPre + inform.id, PrefManager.STATE_UNREAD);

									// 当前已是设置页则不显示底部通知；但显示新消息提醒图标
									if (currentFragmentIndex == 3)
										setTitleImgRight(R.drawable.icon_inform_new, informListener);
									else
										badgeView.show();
								}
							}
						}
					}
				}
			}

			@Override
			public void fail() {
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkInform();

		/**
		 * 用户信息为空,则都跳转到首个fragment
		 */
		if (currentFragmentIndex == 2 && UserManager.getInstance().getUser() == null) {
			rbRecommand.setChecked(true);
		}
		if (currentFragmentIndex == 3 && UserManager.getInstance().getUser() == null) {
			rbRecommand.setChecked(true);
		}

		// 判断是否是由新建申购返回
		final String strLotteryUrl = PrefManager.getInstance().getString(PrefManager.KEY_LOTTERY_URL, "");
		if (!StringHelper.isBlank(strLotteryUrl)) {
			PrefManager.getInstance().putString(PrefManager.KEY_LOTTERY_URL, "");

			// 申购结果返回
			if (PrefManager.getInstance().getBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, true)) {
				PrefManager.getInstance().putBoolean(PrefManager.HAVE_MORE_THAN_ACTIVITY, false);

				String strMessage = PrefManager.getInstance().getString(PrefManager.ACTIVITY_LOTTERY_MESSAGE, "");
				showTwoButtonDialog(getString(R.string.more_than_activity_title), strMessage, getString(R.string.get_red_bag),
						getString(R.string.zhi_dao_l), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 去抽奖页面
								Bundle bundle = new Bundle();
								bundle.putInt("webType", 8);
								bundle.putString("url", strLotteryUrl);
								openActivity(WebActivity.class, bundle);
								dialog.dismiss();
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		
		PrefUtil.setMain(mApplication, true);
		if (UserManager.getInstance().getUser() != null) {
			PrefUtil.setIsFirstToMain(this, false);
		}

		Intent intent = getIntent();
		String strContentUrl = intent.getStringExtra(Const.KEY_NOTIFICATION_CONTENT_URL);
		if (!StringHelper.isBlank(strContentUrl)) {
			intent.setClass(this, AdsContentsActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		
		PrefUtil.setMain(mApplication, false);
	}
	
	

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 */
	public void setTabSelection(int index) {
		transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);

		switch (index) {
		case -1:
			currentFragmentIndex = -1;
			mTextTitle.setVisibility(View.VISIBLE);
			mTextTitle.setText(R.string.company_name);
			// 在线客服
			setTitleImgRight(R.drawable.icon_service, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Uri uriOnlineService = Uri.parse(UrlConst.ONLINE_SERVICE);
						startActivity(new Intent(Intent.ACTION_VIEW, uriOnlineService));
					} catch (Exception e) {
						showToast("请您安装QQ");
					}
				}
			});

			if (recommandFragment == null) {
				recommandFragment = new RecommandFragment();
				transaction.add(R.id.content, recommandFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(recommandFragment);
			}
			break;
		case ViewUtil.MAIN_TAB_QUICK:
			currentFragmentIndex = 0;
			mTextTitle.setVisibility(View.VISIBLE);
			mTextTitle.setText(R.string.all_invest);

			// 在线客服
			setTitleImgRight(R.drawable.icon_service, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Uri uriOnlineService = Uri.parse(UrlConst.ONLINE_SERVICE);
						startActivity(new Intent(Intent.ACTION_VIEW, uriOnlineService));
					} catch (Exception e) {
						showToast("请您安装QQ");
					}
				}
			});

			if (quickBuyFragment == null) {
				quickBuyFragment = new InvestFragment();
				transaction.add(R.id.content, quickBuyFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(quickBuyFragment);
			}
			break;
		case ViewUtil.MAIN_TAB_ASSETS:

			mTextTitle.setVisibility(View.GONE);
			hideTitleImgRight();

			currentFragmentIndex = 2;
			if (UserManager.getInstance().getUser() == null) {

				startActivity(new Intent(this, LoginActivity.class));
				/**
				 * 清除首页缓存
				 */
				if (fragAccount != null) {
					fragAccount = null;
				}
				if (realFragAssets != null) {
					realFragAssets = null;
				}
				if (virtualFragAssets != null) {
					virtualFragAssets = null;
				}
			} else {
				if (realFragAssets == null) {
					realFragAssets = new AssembleFragment();
					transaction.add(R.id.content, realFragAssets);
				}
				if (virtualFragAssets == null) {
					virtualFragAssets = new VirtualAssetsFragment();
					transaction.add(R.id.content, virtualFragAssets);
				}
				if (isVirtual) {
					transaction.show(virtualFragAssets);
					transaction.hide(realFragAssets);
				} else {
					transaction.show(realFragAssets);
					transaction.hide(virtualFragAssets);
				}
			}
			break;
		case ViewUtil.MAIN_TAB_SET:
			// 隐藏未读消息提示
			badgeView.hide();

			if (PrefManager.getInstance().getBoolean(PrefManager.KEY_UNREAD_INFORM, false))
				setTitleImgRight(R.drawable.icon_inform_new, informListener);
			else
				setTitleImgRight(R.drawable.icon_inform, informListener);

			currentFragmentIndex = 3;
			if (UserManager.getInstance().getUser() == null) {

				startActivity(new Intent(this, LoginActivity.class));
				if (fragAccount != null) {
					fragAccount = null;
				}
				if (realFragAssets != null) {
					realFragAssets = null;
				}
				if (virtualFragAssets != null) {
					virtualFragAssets = null;
				}
			} else {
				mTextTitle.setVisibility(View.VISIBLE);
				mTextTitle.setText(R.string.she_zhi);
				if (fragAccount == null) {
					fragAccount = new AccountFragment();
					transaction.add(R.id.content, fragAccount);
				} else {
					// 如果MessageFragment不为空，则直接将它显示出来
					transaction.show(fragAccount);
				}
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		setContentView(R.layout.activity_main);

		// 需要应用退出提示
		setNeedExitApp(true);

		flContent = (FrameLayout) findViewById(R.id.content);
		fragmentManager = getFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setChecked(checkedId);
			}
		});

		rbRecommand = (RadioButton) findViewById(R.id.rb_recommand);
		rbQuickBuy = (RadioButton) findViewById(R.id.rb_quick_buy);
		rbAssets = (RadioButton) findViewById(R.id.rb_assets);
		rbAccount = (RadioButton) findViewById(R.id.rb_account);
		mTextTitle = (TextView) findViewById(R.id.title_middle_text);
		llAssetsTitle = (LinearLayout) findViewById(R.id.ll_assets_title);
		titleBar = (RelativeLayout) findViewById(R.id.title_bar);

		badgeView = new BadgeView(MainActivity.this, radioGroup);
		badgeView.setSharpType(BadgeView.OVAL_SHAPE);
		badgeView.setBadgeBackgroundColor(Color.RED);
		badgeView.setHeight(Util.dip2px(MainActivity.this, 10f));
		badgeView.setWidth(Util.dip2px(MainActivity.this, 10f));
		badgeView.setPadding(0, 0, 0, 0);
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.setBadgeMargin1(Util.dip2px(MainActivity.this, 0));
		badgeView.setBadgeMargin2(Util.dip2px(MainActivity.this, 35));

		rbRecommand.setChecked(true);
	}

	private void setRadioButtonTextColor(RadioButton radioButton) {
		rbRecommand.setTextColor(getResources().getColor(R.drawable.gray_text));
		rbQuickBuy.setTextColor(getResources().getColor(R.drawable.gray_text));
		rbAssets.setTextColor(getResources().getColor(R.drawable.gray_text));
		rbAccount.setTextColor(getResources().getColor(R.drawable.gray_text));
		radioButton.setTextColor(getResources().getColor(R.color.red));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction 用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (quickBuyFragment != null) {
			transaction.hide(quickBuyFragment);
		}
		if (fragAccount != null) {
			transaction.hide(fragAccount);
		}

		if (realFragAssets != null) {
			transaction.hide(realFragAssets);
		}

		if (virtualFragAssets != null) {
			transaction.hide(virtualFragAssets);
		}
		if (recommandFragment != null) {
			transaction.hide(recommandFragment);
		}
	}

	private void setChecked(int checkedId) {
		switch (checkedId) {
		case R.id.rb_quick_buy:
			setRadioButtonTextColor(rbQuickBuy);
			llAssetsTitle.setVisibility(View.GONE);
			setTabSelection(0);
			titleBar.setBackgroundColor(CustomConstants.CURRENT_COLOR);
			flContent.setBackgroundColor(0xff2d3658);
			break;
		case R.id.rb_assets:
			setRadioButtonTextColor(rbAssets);
			llAssetsTitle.setVisibility(View.VISIBLE);
			setTabSelection(2);
			titleBar.setBackgroundColor(0x002d3658);
			flContent.setBackgroundColor(0xffffffff);
			break;
		case R.id.rb_account:
			setRadioButtonTextColor(rbAccount);
			llAssetsTitle.setVisibility(View.GONE);
			setTabSelection(3);
			titleBar.setBackgroundColor(0xffff3b3b);
			flContent.setBackgroundColor(0x002d3658);
			break;
		case R.id.rb_recommand:
			setRadioButtonTextColor(rbRecommand);
			llAssetsTitle.setVisibility(View.GONE);
			setTabSelection(-1);
			titleBar.setBackgroundColor(0x002d3658);
			flContent.setBackgroundColor(0xffffffff);
			break;
		}
	}

	private void checkVersion() {
		// 获取当前程序版本号。如果出现异常则终止版本号检测
		try {
			strCurrentVersion = getPackageManager().getPackageInfo(getPackageName(), 1).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		String url = "conf/autofinance.php";
		AnsynHttpRequest.requestByPost(this, url, callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack() {
		@Override
		public void back(String data, int url) {
			Message msg = new Message();
			msg.obj = data;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handleJsonData((String) msg.obj);
		};
	};

	private LinearLayout llAssetsTitle;

	private TextView realBtn;

	private TextView virtualBtn;

	private VirtualAssetsFragment virtualFragAssets;

	private AssembleFragment realFragAssets;

	private RelativeLayout titleBar;

	private FrameLayout flContent;

	private void handleJsonData(String strJson) {

		if (strJson == null || "".equals(strJson)) {
			dismissLoading();
			showHintDialog("网络不给力");
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(strJson);
			String Succ = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");

			if ("0".equals(Succ)) {
				String lines = jsonObject.getString("data");
				String Banks = jsonObject.getString("data");

				JSONObject bjsonObject2 = new JSONObject(Banks);
				String bline = bjsonObject2.getString("financial");
				JSONObject bjsonObject3 = new JSONObject(bline);
				Banks = bjsonObject3.getString("banks");

				JSONObject jsonObject2 = new JSONObject(lines);
				String line = jsonObject2.getString("software");
				JSONObject jsonObject3 = new JSONObject(line);
				lines = jsonObject3.toString();
				lines = jsonObject3.getString("android");
				JSONObject jsonObject1 = new JSONObject(lines);

				// 判断版本号
				lastVersion = jsonObject1.getString("curVersion");
				int nRet = Util.checkVersion(lastVersion, strCurrentVersion);
				if (nRet == 1) {
					CommonDialog dialog = new CommonDialog(this, getString(R.string.update_new_version), getString(R.string.update_title),
							getString(R.string.update_install), getString(R.string.cancel), new DialogClickListener() {
								@Override
								public void onDialogClick() {
									Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
									updateIntent.putExtra("lastVersion", lastVersion);
									startService(updateIntent);
								}
							});
					dialog.show();
				}
			} else {
				showHintDialog(Errormsg);
				dismissLoading();
				return;
			}

		} catch (JSONException e) {
			dismissLoading();
			showHintDialog("网络不给力");
			return;
		}
	}

	private void checkActivity() {
		RequestActivityHelper.requestState(this, new IHandleActivityState() {
			@Override
			public void handleResponse(ResponseActivityState response) {
				if (response.listActivity != null && response.listActivity.get(0) != null) {
					NewHandActivity newUserActivity = (NewHandActivity) response.listActivity.get(0);
					if (newUserActivity.isOpen && !PrefManager.getInstance().getBoolean(PrefManager.HAVE_SHOW_REWARD, false)) {
						PrefManager.getInstance().putBoolean(PrefManager.HAVE_SHOW_REWARD, true);
						/**
						 * 显示新手活动对话框，记录不再显示
						 */
						RedBagDialog dialog = new RedBagDialog(MainActivity.this, newUserActivity.strIntoduceUrl, R.style.Dialog);
						dialog.setDetailURL(newUserActivity.strThemeUrl);
						dialog.show();
					}
				}
			}
		});
	}

	/** 通知监听器 */
	private OnClickListener informListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MainActivity.this.openActivity(InformActivity.class);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_real_trade:
			realBtn.setSelected(true);
			virtualBtn.setSelected(false);
			realBtn.setTextColor(0xffffffff);
			virtualBtn.setTextColor(0xff9098b4);

			isVirtual = false;
			setTabSelection(2);
			break;
		case R.id.tv_virtual_trade:
			realBtn.setSelected(false);
			virtualBtn.setSelected(true);
			realBtn.setTextColor(0xff9098b4);
			virtualBtn.setTextColor(0xffffffff);

			isVirtual = true;
			setTabSelection(2);
			break;
		}
	}
}
