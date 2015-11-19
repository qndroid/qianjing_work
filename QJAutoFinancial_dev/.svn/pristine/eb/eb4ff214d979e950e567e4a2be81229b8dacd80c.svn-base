package com.qianjing.finance.ui.activity.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.QApplication;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qjautofinancial.R;


/**
 * @Description:绑定银行卡选择地区二级页面
 * @author majinxin
 * @date 2014-11-13 下午6:04:30
 */
public class CardBoundTwoActivity extends BaseActivity {

	private Button ButBack;
	private ListView lv_listview;
	private List<HashMap<String, Object>> listItem;
	private String responseData;
	private QApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_bound_two);
		myApp = (QApplication) getApplication();
		myApp.addActivity(this);
		
		requestData();

	}
	private void requestData(){
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("bank", ContentValueBound.selectedBank);
		upload.put("province", ContentValueBound.levelOneKey);
		AnsynHttpRequest.requestByPost(CardBoundTwoActivity.this,
				"/card/getcity.php", callbackData, upload);
	}
	private void initView() {
		ButBack = (Button) findViewById(R.id.bt_back);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		lv_listview.setAdapter(new MyAdapter());
		lv_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Iterator<String> keys = listItem.get(position).keySet()
						.iterator();
				if (keys.hasNext()) {
					String key = (String) listItem.get(position).get(
							keys.next().toString());
					// System.out.println("点击的条目是" + position + key);
					ContentValueBound.levelTwoKey = key;// 获取点击的条目内容
				}
				Intent intent = new Intent(CardBoundTwoActivity.this,
						CardBoundThreeActivity.class);
				startActivityForResult(intent, 12);
			}
		});
		ButBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardBoundTwoActivity.this.finish();
			}
		});
	}

	private HttpCallBack callbackData = new HttpCallBack() {

		@Override
		public void back(String data, int url) {
		
				// System.out.println("二级市打印："+data);
				responseData = data;
				Message message = Message.obtain();
				message.what = 1;
				myHandler.sendMessage(message);
			
		}
	};
	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			dismissLoading();
			switch (msg.what) {
			case 1:
				if(responseData==null||"".equals(responseData)){
					showHintDialog("网络不给力！");
					return;
				}
				try {
					JSONObject object = new JSONObject(responseData);
					int ecode = object.optInt("ecode");
					String emsg = object.optString("emsg");
					if (ecode == 0) {
						JSONObject data = object.optJSONObject("data");
						JSONArray Arry = data.optJSONArray("citys");
						ArrayList<HashMap<String, Object>> arryList = new ArrayList<HashMap<String, Object>>();
						for (int i = 0; i < Arry.length(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("leveTwo", Arry.getString(i));
							arryList.add(map);
						}
						listItem = arryList;
						initView();
					} else {
						showHintDialog(emsg);
					}
				} catch (Exception e) {
					showHintDialog("网络不给力！");
				}
				break;

			}
		};
	};

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return listItem.size();
		}

		@Override
		public Object getItem(int position) {
			return listItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.area_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.line=convertView.findViewById(R.id.line);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText((CharSequence) listItem.get(position).get(
					"leveTwo"));
			if(listItem.size()==position+1){
				holder.line.setVisibility(View.INVISIBLE);
			}else{
				holder.line.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
		public View line;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 20) {
			setResult(20,data);
			finish();
		}
	}
}
