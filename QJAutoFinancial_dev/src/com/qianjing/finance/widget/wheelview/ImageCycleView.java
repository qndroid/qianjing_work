package com.qianjing.finance.widget.wheelview;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qianjing.finance.util.Util;
import com.qjautofinancial.R;

/**
 * custom control for displaying image from the first to the end automatically and recursively
 * @author keen
 */
public class ImageCycleView extends LinearLayout
{
	private Context mContext;

	/**
	 * mAdvPager for show the img from first to the end recursively
	 */
	private ViewPager mAdvPager = null;

	/**
	 * adapter for @see mAdvPager
	 */
	private ImageCycleAdapter mAdvAdapter;

	/**
	 * the container for indicator
	 */
	private ViewGroup mGroup;

	/**
	 * the view nested within  @see mGroup
	 */
	private ImageView mImageView = null;

	/**
	 * contains all @see mImageView
	 */
	private ImageView[] mImageViews = null;

	/**
	 * the current index for the selected item of @see mAdvPager
	 */
	private int mImageIndex = 0;

	/**
	 * the density of the device
	 */
	private float mScale;

	/**
	 * @param context
	 */
	private ArrayList<ImageView> mImageViewCacheList = new ArrayList<ImageView>();
	
	
	public ImageCycleView(Context context)
	{
		super(context);
	}

	/**
	 * Inflates ImageCycleView from the specified layout xml file
	 * @param context
	 * @param attrs
	 */
	public ImageCycleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		// inflate the layout file into this ( ImageCycleView )
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);

		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// stop the task for recursively and automatically showing the image while at least one finger touched on the
		// screen,
		// restart the task once the this listener receives the ACTION_UP event.
		mAdvPager.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_UP:
					// start sliding the image from first to end automatically and recursively
					startImageTimerTask();
					break;
				default:
					// stop sliding the image automatically and recursively
					stopImageTimerTask();
					break;
				}
				return false;
			}
		});
		// indicator container
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	/**
	 * 优化触控的拦截体验
	 * 
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{

		switch (ev.getAction())
		{

		case MotionEvent.ACTION_DOWN:

			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();

			if (Math.abs(moveX - startX) > Math.abs(moveY - startY))
			{
				getParent().requestDisallowInterceptTouchEvent(true);
			}

			if (Math.abs(moveX - startX) < Math.abs(moveY - startY))
			{
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 
	 * @param imageUrlList list of url which hosted the image
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageUrlList, final ImageCycleViewListener imageCycleViewListener)
	{
		mGroup.removeAllViews();
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		// add indicator ImageViews into the mGroup
		for (int i = 0; i < imageCount; i++)
		{
			mImageView = new ImageView(mContext);
			// int imageParams = (int) (mScale * 20 + 0.5f);
			// int imagePadding = (int) (mScale * 5 + 0.5f);

			LayoutParams params = new LayoutParams(Util.dip2px(mContext, 10), Util.dip2px(mContext, 10));
			params.setMargins(10, 10, 10, 10);
			mImageView.setLayoutParams(params);
			// mImageView.setPadding(imagePadding, imagePadding, imagePadding,
			// imagePadding);

			mImageViews[i] = mImageView;
			// page_indicator_focused banner_dian_focus
			if (i == 0)
			{
				mImageViews[i].setBackgroundResource(R.drawable.sharp_circle_select);
			}
			else
			{// page_indicator banner_dian_blur
				mImageViews[i].setBackgroundResource(R.drawable.sharp_circle_normal);
			}
			mGroup.addView(mImageViews[i]);
		}
		// create the adapter for the ViewPager
//        Toast.makeText(mContext, "size:"+imageUrlList.size(), 1).show();
        mImageViewCacheList.removeAll(mImageViewCacheList);
		for (int i = 0; i < imageUrlList.size(); i++) {
		    final ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final int position = i;
            imageView.setTag(imageUrlList.get(i));
            imageView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    imageCycleViewListener.onImageClick(position, imageView);
                }
            });
            imageCycleViewListener.displayImage(imageUrlList.get(i), imageView);
            mImageViewCacheList.add(imageView);
        }
		mAdvAdapter = new ImageCycleAdapter(mContext, mImageViewCacheList);
		mAdvPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}

	public void startImageCycle()
	{
		startImageTimerTask();
	}

	public void pushImageCycle()
	{
		stopImageTimerTask();
	}

	private void startImageTimerTask()
	{
		stopImageTimerTask();
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	private void stopImageTimerTask()
	{
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * the task for showing the image from the first to the end recursively 
	 */
	private Runnable mImageTimerTask = new Runnable()
	{

		@Override
		public void run()
		{
			if (mImageViews != null)
			{
				// reset the indicator index if the mImageIndex's value is equal to the amount of mImageViews
				if ((++mImageIndex) == mImageViews.length)
				{
					mImageIndex = 0;
				}
				mAdvPager.setCurrentItem(mImageIndex);
			}
		}
	};

	private int startX;

	private int startY;

	/**
	 * listener for monitoring the state of ViewPager's item
	 * @author keen
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (state == ViewPager.SCROLL_STATE_IDLE)
			{
				startImageTimerTask(); // restart showing the image recursively when the ViewPager's state is
										// SCROLL_STATE_IDLE
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int index)
		{
			// set the current index for indicator
			mImageIndex = index;
			// update the background of ImageView for the selected item page_indicator_focused banner_dian_focus
			
			
			
			for (int i = 0; i < mImageViews.length; i++)
			{
				if (index != i)
				{
					mImageViews[i]// page_indicator banner_dian_blur
							.setBackgroundResource(R.drawable.sharp_circle_normal);
				}
				if(index == i){
				    mImageViews[index].setBackgroundResource(R.drawable.sharp_circle_select);
				}
			}

		}
	}

	private class ImageCycleAdapter extends PagerAdapter
	{
		private ArrayList<ImageView> mAdList;
		
		private Context mContext;
		
		public ImageCycleAdapter(Context context, ArrayList<ImageView> adList)
		{
			mContext = context;
			mAdList = adList;
		}

		@Override
		public int getCount()
		{
			return mAdList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj)
		{
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			container.addView(mAdList.get(position));
			return mAdList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			ImageView view = (ImageView) object;
			container.removeView(view);
		}
	}

	/**
	 * listener for monitoring the ImageCycleView component
	 * @author keen
	 */
	public static interface ImageCycleViewListener
	{

		/**
		 * display image
		 * @param imageURL
		 * @param imageView
		 */
		public void displayImage(String imageURL, ImageView imageView);

		/**
		 * single click event for ImageView
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);
	}

}
