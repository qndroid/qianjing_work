package com.qianjing.finance.widget.adapter.timedespority;

import com.qianjing.finance.ui.fragment.BaseFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/** 
* @description ViewPager通用Adapter
* @author majinxin
* @date 2015年9月7日
*/
public class CommonPagerAdapter extends FragmentPagerAdapter
{
	private ArrayList<BaseFragment> mList;

	public CommonPagerAdapter(FragmentManager fm, Context context, ArrayList<BaseFragment> mList)
	{
		super(fm);
		this.mList = mList;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		return mList.get(arg0);
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}
}