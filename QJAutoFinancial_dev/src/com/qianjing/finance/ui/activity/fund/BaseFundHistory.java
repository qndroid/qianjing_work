package com.qianjing.finance.ui.activity.fund;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import com.qianjing.finance.model.fund.MyFundHistory;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CheckTools;

public abstract class BaseFundHistory extends BaseActivity {

	protected List<MyFundHistory> historyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		historyList = new ArrayList<MyFundHistory>();
	}
	
	
	protected void requestHistory(int offset,int pageNum,String fdcode){
		
		Hashtable< String, Object> hashTable = new Hashtable<String, Object>();
		
		hashTable.put("page_number", pageNum);
		hashTable.put("offset",offset);
		if(fdcode!=null){
			hashTable.put("fund_code", fdcode);
		}
		
		
		AnsynHttpRequest.requestByPost(this, UrlConst.URL_FUND_MINE_HISTORY, historyCallBack, hashTable);
		
		
	}
	
	HttpCallBack historyCallBack = new HttpCallBack(){

		@Override
		public void back(String data, int status) {
			
			Message msg = Message.obtain();
			msg.obj = data;
			handler.sendMessage(msg);
			
		}
		
	};
	
	
	Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			String data = (String) msg.obj;
			
			System.out.println("基金历史记录数据:"+data);
			analysisHistoryData(data);
			dismissLoading();
		};
		
	};
	
	protected void analysisHistoryData(String jStr) {
		
		if(jStr==null || "".equals(jStr)){
			 dismissLoading();
	         showHintDialog("网络不给力！");
	         return;
		}
		
		
		
		try {
			
			JSONObject jObj = new JSONObject(jStr);
			int ecode = jObj.optInt("ecode");
			String emsg = jObj.optString("emsg");
			
			if(ecode==0){
				
				JSONObject data = jObj.optJSONObject("data");
				JSONArray tradeLog = data.optJSONArray("trade_logs");
				
				if(tradeLog.length()==0){
					stopPullLoad();
				}
				
				
				for (int i = 0; i < tradeLog.length(); i++) {
					
					MyFundHistory mfh = new MyFundHistory();
					
					JSONObject historyItem = tradeLog.getJSONObject(i);
					
					mfh.setAbbrev(CheckTools.checkJson(historyItem.optString("abbrev")));
					mfh.setApplyserial(CheckTools.checkJson(historyItem.optString("applyserial")));
					mfh.setBank(CheckTools.checkJson(historyItem.optString("bank")));
					mfh.setCard(CheckTools.checkJson(historyItem.optString("card")));
					mfh.setFdcode(CheckTools.checkJson(historyItem.optString("fdcode")));
					mfh.setNav(CheckTools.checkJson(historyItem.optString("nav")));
					mfh.setOpDate(CheckTools.checkJson(historyItem.optString("opDate")));
					mfh.setOperate(CheckTools.checkJson(historyItem.optString("operate")));
					mfh.setOpid(CheckTools.checkJson(historyItem.optString("opid")));
					mfh.setReason(CheckTools.checkJson(historyItem.optString("reason")));
					mfh.setShares(CheckTools.checkJson(historyItem.optString("shares")));
					mfh.setSid(CheckTools.checkJson(historyItem.optString("sid")));
					mfh.setSopid(CheckTools.checkJson(historyItem.optString("sopid")));
					mfh.setState(CheckTools.checkJson(historyItem.optString("state")));
					mfh.setSum(CheckTools.checkJson(historyItem.optString("sum")));
					mfh.setUid(CheckTools.checkJson(historyItem.optString("uid")));
					
					historyList.add(mfh);
				}
				
				initHistoryAdapter();
				
			}else{
				dismissLoading();
				showHintDialog(emsg);
				
			}
			
		} catch (JSONException e) {
			dismissLoading();
			showHintDialog("数据解析异常");
		}
		
	}
	
	protected abstract void initHistoryAdapter();
	
	protected abstract void stopPullLoad();
	
	
	protected abstract class BaseHistoryAdapter extends BaseAdapter{

		public static final int CUSTOM_ITEM = 0;
		public static final int LOAD_MORE = 1;
		
		@Override
		public int getCount() {
			return historyList.size();
		}
		
		public int getItemType(int position){
			
			if(getItemId(position)==historyList.size()){
				return LOAD_MORE;
			}
			return CUSTOM_ITEM;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}
	
	
	
	protected String getOptType(int index){
		
		if(index==1){
			return "申购";
		}else if(index==2){
			return "赎回";
		}
		
		return "--";
		
	}
	
	protected String getOptState(int index){
		
		switch(index){
		
			case 0:
				return "受理中";
			case 1:
				return "申购中";
			case 2:
				return "赎回中";
			case 3:
				return "成功";
			case 4:
				return "失败";
			case 5:
				return "撤单";
			default: return "--";
		}
		
	}
	
}
