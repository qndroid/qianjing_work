package com.qianjing.finance.service;

import com.qianjing.finance.database.DBDataHelper;
import com.qianjing.finance.database.DBHelper;
import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.net.helper.RequestFundHelper;
import com.qianjing.finance.net.ihandle.IHandleError;
import com.qianjing.finance.net.ihandle.IHandleFundSearch;
import com.qianjing.finance.net.ihandle.IHandleFundSearchLatestUpdate;
import com.qianjing.finance.net.response.model.ResponseFundSearch;
import com.qianjing.finance.net.response.model.ResponseFundSearchLatestUpdate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * @description 基金更新后台服务
 * @author renzhiqiang
 * @date 2015年8月19日
 */
public class UpdateFundService extends Service {
	/**
	 * 常量
	 */
	private static final int UPDATE_FLAG = 0x01;
	private static final int FUND_FLAG = 0x02;
	private static final int STOP_SELF = 0x03;
	private static final String STOP_ACTION = "com.qijinag.finance.stop_fund_service";

	private PrefManager spManager;
	private long updateTime;
	private StopServiceReceiver stopReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		spManager = PrefManager.getInstance();
		registerReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mHandler.sendEmptyMessage(UPDATE_FLAG);
		// 被回收，不重启
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	/**
	 * 请求更新时间，看是否要更新
	 */
	private void requestUpdateTime() {
		RequestFundHelper.reqeustFundSearchLatestUpdate(this,
				new IHandleFundSearchLatestUpdate() {
					@Override
					public void handleResponse(
							ResponseFundSearchLatestUpdate response) {
						if (response.latestTime != -1) {
							if (response.latestTime != spManager.getLong(
									PrefManager.LAST_UPDATE_FUND, -1)) {
								updateTime = response.latestTime;
								mHandler.sendEmptyMessage(FUND_FLAG);
							} else {
								stopSelf();
							}
						}
					}
				}, new IHandleError() {
					@Override
					public void handleError(int state) {
						if (DBDataHelper
								.getInstance()
								.select(DBHelper.FUND_LIST_TABLE, null, null,
										null, null).size() > 0) {
							stopSelf();
						} else {
							mHandler.sendEmptyMessageDelayed(UPDATE_FLAG, 1000);
						}
					}
				});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_FLAG :
					requestUpdateTime();
					break;
				case FUND_FLAG :
					requestFundList();
					break;
				case STOP_SELF :
					stopSelf();
					break;
			}
		};
	};

	private void requestFundList() {
		RequestFundHelper.requestFundSearch(this, new IHandleFundSearch() {
			@Override
			public void handleResponse(ResponseFundSearch response) {
				if (response.listFund != null) {
					DBDataHelper.getInstance().delete(DBHelper.FUND_LIST_TABLE,
							null, null);
					if (DBDataHelper.getInstance().insert(
							DBHelper.FUND_LIST_TABLE, response.listFund)) {
						/**
						 * 更新成功,此时可以发送一个notification通知用户
						 */
						spManager.putLong(PrefManager.LAST_UPDATE_FUND,
								updateTime);
						stopSelf();
					} else {
						mHandler.sendEmptyMessageDelayed(FUND_FLAG, 5000);
					}
				}
			}
		}, new IHandleError() {
			@Override
			public void handleError(int state) {
				if (DBDataHelper
						.getInstance()
						.select(DBHelper.FUND_LIST_TABLE, null, null, null,
								null).size() > 0) {
					// 有数据但没网，这次不更细了，提高效率
					stopSelf();
				} else {
					mHandler.sendEmptyMessageDelayed(FUND_FLAG, 5000);
				}
			}
		});
	}

	private void registerReceiver() {
		stopReceiver = new StopServiceReceiver();
		IntentFilter filter = new IntentFilter(STOP_ACTION);
		this.registerReceiver(stopReceiver, filter);
	}

	private void unregisterReceiver() {
		this.unregisterReceiver(stopReceiver);
	}

	private class StopServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mHandler.sendEmptyMessage(STOP_SELF);
		}
	}
}
