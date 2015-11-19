
package com.qianjing.finance.ui.activity.common;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.util.PrefUtil;
import com.qjautofinancial.R;

/**
 * Title: GuideActivity Description: 引导页
 */
public class GuideActivity extends BaseActivity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup viewPics;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PrefUtil.setGuide(mApplication, true);

		initView();
	}

	private void initView() {
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.viewpager_page1, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_page2, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_page3, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_page4, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_page5, null));

		viewPics = (ViewGroup) inflater.inflate(R.layout.fina_new_guide_activity, null);
		viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);

		setContentView(viewPics);
		viewPager.setAdapter(new GuidePageAdapter());
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 调用应用激活通知接口
			sendActiveRequest();

			PrefUtil.setFirstEnter(GuideActivity.this);

			openActivity(MainActivity.class);
			finish();
		}
	};

	class GuidePageAdapter extends PagerAdapter {
		@Override
		public void destroyItem(View v, int position, Object arg2) {
			((ViewPager) v).removeView(pageViews.get(position));
		}

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(pageViews.get(position));

			// 测试页卡1内的按钮事件
			if (position == 4) {
				Button btn = (Button) v.findViewById(R.id.btn_close_guide);
				btn.setOnClickListener(onClickListener);
				Button btnOff = (Button) v.findViewById(R.id.guide_off_btn);
				btnOff.setOnClickListener(onClickListener);
			}

			return pageViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View v, Object arg1) {
			return v == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
	}

	private void sendActiveRequest() {

		AnsynHttpRequest.requestByPost(this, UrlConst.APP_ACTIVE, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
			}
		}, null);
	}
}
