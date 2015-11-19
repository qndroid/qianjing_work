package com.qianjing.finance.ui.activity.card;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.widget.adapter.QuickCardListAdapter;
import com.qjautofinancial.R;


public class QuickBillBankActivity extends BaseActivity {
	
	private ListView lv_list;
	private QuickCardListAdapter adapter;
	private ArrayList<Card> mArrayCard = new ArrayList<Card>();
	private String strSelectedBankId;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}

	private void initView() {
		
		setContentView(R.layout.activity_quick_bill_bank);
		
		setTitleBack();
		setTitleWithId(R.string.title_card_select);
		setLoadingUncancelable();
		
		strSelectedBankId = getIntent().getStringExtra("bankId");
		
		lv_list = (ListView) findViewById(R.id.lv_list);
		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (mArrayCard!=null && mArrayCard.size()>0) {
					
					Card card = mArrayCard.get(position);
					Intent intent = new Intent();
					intent.putExtra(Flag.EXTRA_BEAN_CARD_CURRENT, card);
					setResult(112, intent);
					QuickBillBankActivity.this.finish();
				}
			}
		});
		
		requestSupportBankList();
	}
	
	/**
	 * <p>Title: requesQuickCardMap</p>
	 * <p>Description: 请求绑卡支持银行列表</p>
	 */
	private void requestSupportBankList() {
		showLoading();
		AnsynHttpRequest.requestByPost(this, UrlConst.CARD_LIST_SUPPORT, new HttpCallBack() {
			@Override
			public void back(String data, int status) {
				Message msg = Message.obtain();
				msg.obj = data;
				mhandler.sendMessage(msg);
			}
		}, null);
	}
	
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg) {
			handleSupportCardList((String)msg.obj);
		}
	};

	protected void handleSupportCardList(String strJson) {
		
		dismissLoading();
		
		if (strJson == null || "".equals(strJson)) {
			showHintDialog("网络不给力！");
			return;
		}
		
		try {
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			JSONObject data = object.optJSONObject("data");
			if (ecode == 0) {
				mArrayCard = CommonUtil.parseSupportCardList(data);
				
				// 判断哪些银行是银联绑卡方式
				for (Card card : mArrayCard) {
					if (TextUtils.equals("3", card.getCapitalMode())) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("name", card.getBankName());
						QuickBillActivity.mQuickActivity.listCpu.add(map);
					}
				}
				
				adapter = new QuickCardListAdapter(this, mArrayCard, strSelectedBankId);
				lv_list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			} 
			else {
				showHintDialog(emsg);
			}
		} catch (Exception e) {
			showHintDialog("网络不给力");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}
	
}
