package com.qianjing.finance.view.runnabletextview;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**********************************************************
 * @文件名称：RunningTextView.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月27日 下午6:02:52
 * @文件描述：自定义活期宝动画效果 View
 * @修改历史：2015年5月27日创建初始版本
 **********************************************************/
public class RunningTextView extends TextView
{

	private int frames = 25;// 总共跳跃的帧数,默认25跳
	private double content;// 最后显示的数字
	private double nowNumber = 0.00;
	private ExecutorService thread_pool;
	private DecimalFormat formater;

	public RunningTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public RunningTextView(Context context, AttributeSet attrs)
	{
		this(context, attrs, -1);
	}

	public RunningTextView(Context context)
	{
		this(context, null, -1);
	}

	public int getFrames()
	{
		return frames;
	}

	public void setFrames(int frames)
	{
		this.frames = frames;
	}

	/**
	 * 设置数字格式，具体查DecimalFormat类的api
	 * @param pattern
	 */
	public void setFormat(String pattern)
	{
		formater = new DecimalFormat(pattern);
	}

	private void init()
	{
		thread_pool = Executors.newCachedThreadPool();// 2个线程的线程池
		formater = new DecimalFormat("0.00");// 最多两位小数，而且不够两位整数用0占位。可以通过setFormat再次设置
	}

	/**
	 * 播放数字动画的方法
	 *
	 * @param moneyNumber
	 */
	public void playNumber(double moneyNumber)
	{
		if (moneyNumber == 0)
		{
			this.setText("0.00");
			return;
		}
		content = moneyNumber;
		nowNumber = 0.00;
		thread_pool.execute(new Runnable()
		{
			@Override
			public void run()
			{
				Message msg = handler.obtainMessage();
				double temp = content / frames;
				msg.obj = temp < 0.01 ? 0.01 : temp;// 如果每帧的间隔比1小，就设置为1
				handler.sendMessage(msg);
			}
		});
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			setText(formater.format(nowNumber).toString());// 更新显示的数字
			
			nowNumber += Double.parseDouble(msg.obj.toString());// 跳跃arg1那么多的数字间隔
			if (nowNumber < content)
			{
				Message msg2 = handler.obtainMessage();
				msg2.obj = msg.obj;
				handler.sendMessage(msg2);// 继续发送通知改变UI
			}
			else
			{
				setText(formater.format(content).toString());
			}
		}
	};
}
