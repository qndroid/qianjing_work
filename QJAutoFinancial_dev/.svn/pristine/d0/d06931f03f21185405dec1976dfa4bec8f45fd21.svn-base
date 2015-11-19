package com.qianjing.finance.ui.activity.fund.search;

import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.fragment.fund.FundAllFragment;
import com.qianjing.finance.ui.fragment.fund.FundBondFragment;
import com.qianjing.finance.ui.fragment.fund.FundFinancialFragment;
import com.qianjing.finance.ui.fragment.fund.FundMixFragment;
import com.qianjing.finance.ui.fragment.fund.FundMoneyFragment;
import com.qianjing.finance.ui.fragment.fund.FundQDIIFragment;
import com.qianjing.finance.ui.fragment.fund.FundStockFragment;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.view.slidingtabstrip.PagerSlidingTabStrip;
import com.qjautofinancial.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * @description 基金列表页面
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class FundActivity extends BaseActivity implements OnClickListener {
	/**
	 * UI
	 */
	private ImageView rigthSearchView;
	private ViewPager pager;
	private PagerSlidingTabStrip tabs;
	private FundAllFragment fragmentAll;
	private FundMoneyFragment fragmentMoney;
	private FundStockFragment fragmentStock;
	private FundBondFragment fragmentBond;
	private FundMixFragment fragmentMix;
	private FundFinancialFragment fragmentFinancial;
	private FundQDIIFragment fragmentQDII;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	private String titles[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activity_fund);
		setTitleWithId(R.string.title_fund);
		setTitleBack();
		setOverflowShowingAlways();

		titles = getResources().getStringArray(R.array.fund_all_type);
		rigthSearchView = (ImageView) findViewById(R.id.title_right_image);
		rigthSearchView.setBackgroundResource(R.drawable.icon_title_fund);
		rigthSearchView.setVisibility(View.VISIBLE);
		rigthSearchView.setOnClickListener(this);

		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		pager.setOffscreenPageLimit(1);
		tabs.setViewPager(pager);
		setTabsValue();

	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		tabs.setShouldExpand(true);
		tabs.setDividerColor(getResources().getColor(R.color.color_00ffffff));
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, dm));
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabs.setIndicatorColor(getResources().getColor(R.color.color_e41e23));
		tabs.setSelectedTextColor(getResources().getColor(R.color.color_e41e23));
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0 :
					UMengStatics.onFundPage1View(FundActivity.this);
					if (fragmentAll == null) {
						fragmentAll = new FundAllFragment();
					}
					return fragmentAll;
				case 1 :
					UMengStatics.onFundPage2View(FundActivity.this);
					if (fragmentMoney == null) {
						fragmentMoney = new FundMoneyFragment();
					}
					return fragmentMoney;
				case 2 :
					UMengStatics.onFundPage3View(FundActivity.this);
					if (fragmentStock == null) {
						fragmentStock = new FundStockFragment();
					}
					return fragmentStock;
				case 3 :
					UMengStatics.onFundPage4View(FundActivity.this);
					if (fragmentBond == null) {
						fragmentBond = new FundBondFragment();
					}
					return fragmentBond;
				case 4 :
					UMengStatics.onFundPage5View(FundActivity.this);
					if (fragmentMix == null) {
						fragmentMix = new FundMixFragment();
					}
					return fragmentMix;
				case 5 :
					UMengStatics.onFundPage6View(FundActivity.this);
					if (fragmentFinancial == null) {
						fragmentFinancial = new FundFinancialFragment();
					}
					return fragmentFinancial;
				case 6 :
					UMengStatics.onFundPage7View(FundActivity.this);
					if (fragmentQDII == null) {
						fragmentQDII = new FundQDIIFragment();
					}
					return fragmentQDII;
				default :
					if (fragmentAll == null) {
						fragmentAll = new FundAllFragment();
					}
					return fragmentAll;
			}
		}

	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_right_image :
				Intent intent = new Intent(this, FundSearchActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fragmentAll = null;
		fragmentBond = null;
		fragmentFinancial = null;
		fragmentMix = null;
		fragmentMoney = null;
		fragmentQDII = null;
		fragmentStock = null;
	}
}
