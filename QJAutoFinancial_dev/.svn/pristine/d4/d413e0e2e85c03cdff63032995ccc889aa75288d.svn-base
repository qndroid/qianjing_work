package com.qianjing.finance.ui.activity.timedeposit;

import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.timedeposit.fragment.AllPaidFragment;
import com.qianjing.finance.ui.activity.timedeposit.fragment.DealingFragment;
import com.qianjing.finance.ui.fragment.BaseFragment;
import com.qianjing.finance.view.slidingtabstrip.UnderlinePageIndicator;
import com.qianjing.finance.widget.adapter.timedespority.CommonPagerAdapter;
import com.qjautofinancial.R;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/** 
* @description 定存资产详情页面
* @author majinxin
* @date 2015年9月7日
*/
public class TimeDepositDetailActivity extends BaseActivity implements OnClickListener
{
	/**
	 * UI
	 */
	private Button mBackButton;
	private UnderlinePageIndicator mTabs;
	private ViewPager mViewPager;
	private CommonPagerAdapter mAdapter;
	private TextView mDealView;
	private TextView mEndView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_desposit_detail);
		initView();
	}

	private void initView()
	{
		setTitleWithId(R.string.time_disport_asset_detail);
		mBackButton = (Button) findViewById(R.id.bt_back);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);

		mDealView = (TextView) findViewById(R.id.dealing_view);
		mEndView = (TextView) findViewById(R.id.end_view);
		mTabs = (UnderlinePageIndicator) findViewById(R.id.psts_tabs);
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		/**
		 * 初始化 viewpager 
		 */
		BaseFragment allPaidFragment = new AllPaidFragment();
		BaseFragment timingFragment = new DealingFragment();
		ArrayList<BaseFragment> mList = new ArrayList<BaseFragment>();
		mList.add(timingFragment);
		mList.add(allPaidFragment);
		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), this, mList);
		mViewPager.setAdapter(mAdapter);
		mTabs.setViewPager(mViewPager);
		mTabs.setOnPageChangeListener(pagerListener);
		setTabsValue();
	}

	private OnPageChangeListener pagerListener = new OnPageChangeListener()
	{
		@Override
		public void onPageSelected(int position)
		{
			switch (position)
			{
			case 0:
				mDealView.setTextColor(getResources().getColor(R.color.blue_deep));
				mEndView.setTextColor(getResources().getColor(R.color.color_a6b4d5));
				break;
			case 1:
				mDealView.setTextColor(getResources().getColor(R.color.color_a6b4d5));
				mEndView.setTextColor(getResources().getColor(R.color.blue_deep));
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}
	};

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bt_back:
			finish();
			break;
		}
	}

	private void setTabsValue()
	{
		mTabs.setFades(false);
		mTabs.setSelectedColor(getResources().getColor(R.color.blue_deep));
	}
}