package com.qianjing.finance.ui.activity.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;


/**
 * @Description:绑定银行卡选择地区一级页面
 * @author majinxin
 * @date 2014-11-13 下午6:04:12
 */
public class CardBoundOneActivity extends BaseActivity
{

	private Button ButBack;
	private ListView lv_listview;
	private List<HashMap<String, Object>> listItem;
	String responseData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_bound_one);
		ButBack = (Button) findViewById(R.id.bt_back);
		ButBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CardBoundOneActivity.this.finish();
			}
		});
		String bankname = getIntent().getStringExtra("bankname");
		if (!StringHelper.isBlank(bankname))
		{
			requestBank(bankname);
			ContentValueBound.selectedBank = bankname;
		}

	}

	private void initView()
	{
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		lv_listview.setAdapter(new MyAdapter());
		lv_listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Iterator<String> keys = listItem.get(position).keySet().iterator();
				if (keys.hasNext())
				{
					String key = (String) listItem.get(position).get(keys.next().toString());
					ContentValueBound.levelOneKey = key;// 获取点击的条目内容
				}
				Intent intent = new Intent(CardBoundOneActivity.this, CardBoundTwoActivity.class);
				startActivityForResult(intent, 11);
			}
		});
	}

	private void requestBank(String bankTypeString)
	{
		showLoading();
		Hashtable<String, Object> getProvince = new Hashtable<String, Object>();
		getProvince.put("bank", bankTypeString);
		AnsynHttpRequest.requestByPost(CardBoundOneActivity.this, "/card/getprovince.php", new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				responseData = data;
				Message message = Message.obtain();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		}, getProvince);
	}
	
	Handler myHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			dismissLoading();
			switch (msg.what)
			{
			case 1:
				try
				{
					JSONObject object = new JSONObject(responseData);
					int ecode = object.optInt("ecode");
					if (ecode == 0)
					{
						JSONObject data = object.optJSONObject("data");
						JSONArray provinceArry = data.optJSONArray("provinces");
						if (provinceArry.length() < 1)
						{
							showToast("无支行信息");
							return;
						}
						ArrayList<HashMap<String, Object>> provinceList = new ArrayList<HashMap<String, Object>>();
						for (int i = 0; i < provinceArry.length(); i++)
						{
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("leveOne", provinceArry.getString(i));
							provinceList.add(map);
						}
						initData(provinceList);
						initView();
					}
					else
					{

					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				break;

			}
		}

	};

	private void initData(ArrayList<HashMap<String, Object>> provinceList)
	{
		listItem = provinceList;
	}

	private class MyAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;

		public MyAdapter()
		{
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			return listItem.size();
		}

		@Override
		public Object getItem(int position)
		{
			return listItem.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.area_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
				holder.line = convertView.findViewById(R.id.tv_name);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText((CharSequence) listItem.get(position).get("leveOne"));
			if (listItem.size() == position)
			{
				holder.line.setVisibility(View.INVISIBLE);
			}
			else
			{
				holder.line.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	public static class ViewHolder
	{
		public TextView textView;
		public View line;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 20)
		{
			setResult(20, data);
			finish();
		}
	}
}
