package com.qianjing.finance.ui.activity.custom;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.model.common.VIPCustom;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.custom.fragment.FragmentItem1;
import com.qianjing.finance.ui.activity.custom.fragment.FragmentItem2;
import com.qianjing.finance.ui.activity.custom.fragment.FragmentItem3;
import com.qianjing.finance.ui.activity.custom.fragment.FragmentItem4;
import com.qianjing.finance.ui.activity.custom.fragment.FragmentItem5;
import com.qianjing.finance.util.LogUtils;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.custom.CtrlViewPager;
import com.qjautofinancial.R;

/**
 * @author liuchen
 * @date 2015/5/26
 * */
public class VIPCustomActivity extends BaseActivity implements OnClickListener {

	private CtrlViewPager cvp;
	private LinearLayout indicatorGroup;
	private int CURRENT_LOCK_ITEM = 0;
	private int startX;
	private int startY;
	private Button btnContinue;
	private boolean enableButton = true;
	private MyContinueJudgeReceiver judgeReceiver;

	private boolean frgBtn1IsEnable = false;
	private boolean frgBtn2IsEnable = false;
	private boolean frgBtn3IsEnable = false;
	private boolean frgBtn4IsEnable = false;
	private boolean frgBtn5IsEnable = false;

	private boolean isFirst2ThisPage = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_vip_custom);
		setTitleWithId(R.string.vip_change_preference);
		setTitleBackBtn();
		initView();

		judgeReceiver = new MyContinueJudgeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.qjautofinancial.vip.custom");
		registerReceiver(judgeReceiver, filter);

		vipCustom = new VIPCustom();
		requestGetUserHibit();

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(judgeReceiver);
		super.onDestroy();
	}

	private void initView() {

		ImageView titleOne = (ImageView) findViewById(R.id.iv_title_one);
		ImageView titleTwo = (ImageView) findViewById(R.id.iv_title_two);
		ImageView titleThree = (ImageView) findViewById(R.id.iv_title_three);
		ImageView titleFour = (ImageView) findViewById(R.id.iv_title_four);
		ImageView titleFive = (ImageView) findViewById(R.id.iv_title_five);
		titleOne.setOnClickListener(this);
		titleTwo.setOnClickListener(this);
		titleThree.setOnClickListener(this);
		titleFour.setOnClickListener(this);
		titleFive.setOnClickListener(this);

		TextView rightText = (TextView) findViewById(R.id.title_right_text);
		rightText.setText("保存");
		rightText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (frgBtn1IsEnable && frgBtn2IsEnable && frgBtn3IsEnable
						&& frgBtn4IsEnable && frgBtn5IsEnable) {
					saveCustomPreference();
				}
			}
		});

		btnContinue = (Button) findViewById(R.id.btn_continue);
		btnContinue.setOnClickListener(this);

		indicatorGroup = (LinearLayout) findViewById(R.id.indicator_item_title);
		indicatorGroup.getChildAt(0).setSelected(true);

		cvp = (CtrlViewPager) findViewById(R.id.cvp_view_pager);
		cvp.setAdapter(new VIPFragmentAdapter(getSupportFragmentManager()));
		cvp.setOnPageChangeListener(new VIPPagerChangeListener());
		// cvp.setOffscreenPageLimit(4);

		if (UserManager.getInstance().getUser().getAge() != null
				&& UserManager.getInstance().getUser().getMoney() != null
				&& UserManager.getInstance().getUser().getPreference() != null
				&& UserManager.getInstance().getUser().getInit() != null
				&& UserManager.getInstance().getUser().getRisk() != null) {

			CURRENT_LOCK_ITEM = 4;

			isFirst2ThisPage = false;

			frgBtn1IsEnable = true;
			frgBtn2IsEnable = true;
			frgBtn3IsEnable = true;
			frgBtn4IsEnable = true;
			frgBtn5IsEnable = true;

		}

		if (cvp.getCurrentItem() == CURRENT_LOCK_ITEM) {
			cvp.setStateCode(CtrlViewPager.HALF_ENABLE_SCROLL);
		}

		setImageTitleDisable(CURRENT_LOCK_ITEM);
		
		UMengStatics.onCustomizedPage1View(this);
	}

	private void saveCustomPreference() {

		if (UserManager.getInstance().getUser().getAge() != null
				&& UserManager.getInstance().getUser().getMoney() != null
				&& UserManager.getInstance().getUser().getPreference() != null
				&& UserManager.getInstance().getUser().getInit() != null
				&& UserManager.getInstance().getUser().getRisk() != null) {
			requestSaveUserHibit();

		} else {
			showHintDialog("信息不完整");
		}
	}

	private class VIPPagerChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (CURRENT_LOCK_ITEM == arg0) {
				cvp.setCurrentItem(CURRENT_LOCK_ITEM);
				cvp.setStateCode(CtrlViewPager.HALF_ENABLE_SCROLL);
			} else {
				cvp.setStateCode(CtrlViewPager.ALL_ENABLE_SCROLL);
			}
		}

		@Override
		public void onPageSelected(int index) {
		    
		    if (index == 0)
		        UMengStatics.onCustomizedPage1View(VIPCustomActivity.this);
		    else if (index == 1)
                UMengStatics.onCustomizedPage2View(VIPCustomActivity.this);
            else if (index == 2)
                UMengStatics.onCustomizedPage3View(VIPCustomActivity.this);
            else if (index == 3)
                UMengStatics.onCustomizedPage4View(VIPCustomActivity.this);
            else if (index == 4)
                UMengStatics.onCustomizedPage5View(VIPCustomActivity.this);
			
			if (index <= CURRENT_LOCK_ITEM) {
				setImageTitleByNum(index);
				
				if (CURRENT_LOCK_ITEM == index) {
					cvp.setStateCode(CtrlViewPager.HALF_ENABLE_SCROLL);
				} else {
					cvp.setStateCode(CtrlViewPager.ALL_ENABLE_SCROLL);
				}
			}
			
			if(index==3){
				Util.hideInputView(VIPCustomActivity.this);
			}
			
			if (index == 4) {
				btnContinue.setText("完成");
			} else {
				btnContinue.setText("继续");
			}
			setContinueBtnState(index);
		}
	}
	
	
	private void setContinueBtnState(int index) {

		boolean isEnable = false;
		switch (index) {
		case 0:
			isEnable = frgBtn1IsEnable;
			break;
		case 1:
			isEnable = frgBtn2IsEnable;
			break;
		case 2:
			isEnable = frgBtn3IsEnable;
			break;
		case 3:
			isEnable = frgBtn4IsEnable;
			break;
		case 4:
			isEnable = frgBtn5IsEnable;
			break;
		}
		btnContinue.setEnabled(isEnable);
	}

	private class VIPFragmentAdapter extends FragmentPagerAdapter {

		public VIPFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return getFragment(position);
		}

		@Override
		public int getCount() {
			return 5;
		}
	}

	public Fragment getFragment(int index) {

		Fragment fragment = null;

		switch (index) {
		case 0:
			fragment = new FragmentItem1();
			break;
		case 1:
			fragment = new FragmentItem2();
			break;
		case 2:
			fragment = new FragmentItem3();
			break;
		case 3:
			fragment = new FragmentItem4();
			break;
		case 4:
			fragment = new FragmentItem5();
			break;
		}

		return fragment;
	}

	@Override
	public void onClick(View v) {

		if (enableButton) {

			switch (v.getId()) {
			case R.id.iv_title_one:
				setImageTitleByNum(0);
				cvp.setCurrentItem(0);
				break;
			case R.id.iv_title_two:
				setImageTitleByNum(1);
				cvp.setCurrentItem(1);
				break;
			case R.id.iv_title_three:
				setImageTitleByNum(2);
				cvp.setCurrentItem(2);
				break;
			case R.id.iv_title_four:
				setImageTitleByNum(3);
				cvp.setCurrentItem(3);
				break;
			case R.id.iv_title_five:
				setImageTitleByNum(4);
				cvp.setCurrentItem(4);
				break;
			case R.id.btn_continue:
				nextPage();
				break;
			}
		}
	}

	private void nextPage() {
		int currentItem = cvp.getCurrentItem();
		if (currentItem < 4) {
			cvp.setCurrentItem(currentItem + 1);
			setImageTitleByNum(currentItem + 1);
			if (currentItem == CURRENT_LOCK_ITEM) {
				CURRENT_LOCK_ITEM++;
				setImageTitleDisable(CURRENT_LOCK_ITEM);
			}

		} else if (currentItem == 4) {
			saveCustomPreference();
		}
	}

	/**
	 * 数字按钮是否被选中
	 * */
	public void setImageTitleByNum(int index) {

		for (int i = 0; i < 5; i++) {
			indicatorGroup.getChildAt(i).setSelected(false);
		}

		indicatorGroup.getChildAt(index).setSelected(true);
	}

	/**
	 * 数字按钮是否可用
	 * */
	public void setImageTitleDisable(int index) {

		for (int i = 0; i < CURRENT_LOCK_ITEM + 1; i++) {
			indicatorGroup.getChildAt(i).setEnabled(true);
		}
		for (int i = CURRENT_LOCK_ITEM + 1; i < 5; i++) {
			indicatorGroup.getChildAt(i).setEnabled(false);
		}
	}

	/**
	 * 处理后退按钮逻辑
	 * */
	@Override
	public void onBackPressed() {
		int currentItem = cvp.getCurrentItem();
		if (currentItem == 0) {
			setResult(CustomConstants.CUSTOM_UNCHANGE);
			checkIsSavePreference();
		} else {
			cvp.setCurrentItem(currentItem - 1);
		}
	}

	private void setTitleBackBtn() {
		setTitleImgLeft(R.drawable.title_left, new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(CustomConstants.CUSTOM_UNCHANGE);
				checkIsSavePreference();
				Util.hideInputView(VIPCustomActivity.this);
			}
		});
	}

	private void checkIsSavePreference() {

		if(CURRENT_LOCK_ITEM==0 && StringHelper.isBlank(UserManager.getInstance().getUser().getAge())){
			VIPCustomActivity.this.finish();
			return ;
		}
		
		if (isFirst2ThisPage) {
			showTwoButtonDialog("返回后将不保存偏好信息，您确定要返回吗？",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							UserManager.getInstance().getUser().setAge(null);
							UserManager.getInstance().getUser()
									.setPreference(null);
							UserManager.getInstance().getUser().setMoney(null);
							UserManager.getInstance().getUser().setRisk(null);
							UserManager.getInstance().getUser().setInit(null);

							dialog.dismiss();
							VIPCustomActivity.this.finish();
						}
					});
		} else {
			if(UserManager.getInstance().getUser()!=null){
				User user = UserManager.getInstance().getUser();
				user.setAge(vipCustom.getAge());
				user.setInit(vipCustom.getInit());
				user.setMoney(vipCustom.getMoney());
				user.setPreference(vipCustom.getPreference());
				user.setRisk(vipCustom.getRisk());
			}
			VIPCustomActivity.this.finish();
		}
	}

	/**
	 * Judge continue button is enable
	 * */
	private class MyContinueJudgeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			int pageNum = intent.getIntExtra("pageNum", 0);
			boolean isEnable = intent.getBooleanExtra("isEnable", false);
			switch (pageNum) {

			case 1:
				frgBtn1IsEnable = isEnable;
				break;
			case 2:
				frgBtn2IsEnable = isEnable;
				break;
			case 3:
				frgBtn3IsEnable = isEnable;
				break;
			case 4:
				frgBtn4IsEnable = isEnable;
				break;
			case 5:
				frgBtn5IsEnable = isEnable;
				break;
			}

			if (isEnable) {
				enableScrollAndButton();
			} else {
				disableScrollAndButton();
			}
			btnContinue.setEnabled(isEnable);
		}
	}

	private void disableScrollAndButton() {
		cvp.setStateCode(CtrlViewPager.ALL_DISABLE_SCROLL);
		enableButton = false;
	}

	private void enableScrollAndButton() {
		cvp.setStateCode(CtrlViewPager.ALL_ENABLE_SCROLL);
		enableButton = true;
	}

	public void requestGetUserHibit(){
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("type", 7);
		AnsynHttpRequest.requestByPost(this, UrlConst.GET_USER_HIBIT, new HttpCallBack() {
			
			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}, hashTable);
	}
	
	public void requestSaveUserHibit() {
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("type", 7);
		hashTable.put("money", UserManager.getInstance().getUser().getMoney());
		hashTable.put("init", UserManager.getInstance().getUser().getInit());
		hashTable.put("age", UserManager.getInstance().getUser().getAge());
		hashTable.put("preference", UserManager.getInstance().getUser()
				.getPreference());
		hashTable.put("risk", UserManager.getInstance().getUser().getRisk());
		AnsynHttpRequest.requestByPost(this, UrlConst.SAVE_USER_HIBIT,
				new HttpCallBack() {

					@Override
					public void back(String data, int status) {
						Message msg = Message.obtain();
						msg.obj = data;
						msg.what = 2;
						handler.sendMessage(msg);
					}
				}, hashTable);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			String jsonStr = (String) msg.obj;

			switch (msg.what) {

			case 1:
				analysisGetUserDate(jsonStr);
				break;
			case 2:
				LogUtils.syso("保存定制数据" + jsonStr);
				analysisSaveUserDate(jsonStr);
				break;
			}
		}
	};
	private VIPCustom vipCustom;

	protected void analysisSaveUserDate(String jsonStr) {
		if (jsonStr == null || "".equals(jsonStr)) {
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
			return;
		}

		try {

			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");

			if (ecode == 0) {
				Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
				dismissLoading();
				setResult(CustomConstants.CUSTOM_CHANGE);
				VIPCustomActivity.this.finish();
			} else {
				showHintDialog(emsg);
				dismissLoading();
			}
		} catch (Exception e) {
			showHintDialog(getString(R.string.net_data_error));
			dismissLoading();
		}
	};
	
	protected void analysisGetUserDate(String jsonStr) {
		
		if(jsonStr==null || "".equals(jsonStr)){
			dismissLoading();
			showHintDialog(getString(R.string.net_prompt));
	        return;
		}
		try {
			
			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");
			JSONObject data = jobj.optJSONObject("data");
			
			if(ecode==0){
				
				String parasStr = data.optString("paras");
				if("".equals(parasStr)){
					dismissLoading();
				}else{
					
					JSONObject paras = new JSONObject(parasStr);
					
					int age = paras.optInt("age");
					
					String initStr = paras.optString("init");
					String moneyStr = paras.optString("money");
					
					
					String preference = paras.optString("preference");
					String risk = paras.optString("risk");
					
					
					if(UserManager.getInstance().getUser()!=null){
						vipCustom.setAge(age+"");
						vipCustom.setInit(initStr);
						vipCustom.setMoney(moneyStr);
						vipCustom.setPreference(preference);
						vipCustom.setRisk(risk);
					}
					
					dismissLoading();
				}
				
			}else{
				showHintDialog(emsg);
				dismissLoading();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showHintDialog(getString(R.string.net_data_error));
			dismissLoading();
		}
	}

}
