package com.qianjing.finance.ui.activity.card;

import com.qianjing.finance.model.common.BrachBank;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Util;
import com.qianjing.finance.util.WriteLog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @Description: 绑定银行卡选择地区三级页面
 * @author majinxin
 * @date 2014-11-13 下午6:04:46
 */
public class CardBoundThreeActivity extends BaseActivity
{

	public static final String TAG = "CardBound";

	private Button ButBack;
	private ListView lv_listview;
	private ArrayList<BrachBank> list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_bound_three);

		mApplication.addActivity(this);

		initView();
		sendRequest();

	}

	private void initView()
	{
		ButBack = (Button) findViewById(R.id.bt_back);
		lv_listview = (ListView) findViewById(R.id.lv_listview);

		ButBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				CardBoundThreeActivity.this.finish();
			}
		});

		EditText edit_search = (EditText) findViewById(R.id.edittext_search);
		edit_search.addTextChangedListener(watcher);
	}

	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			if (list != null && s.length() > 0)
			{
				ArrayList<BrachBank> resultList = new ArrayList<BrachBank>();
				for (int i = 0; i < list.size(); i++)
				{
					resultList.add(list.get(i));
				}
				// 对分行数据进行查找测试
				resultList = Util.searchBank(list, s.toString());
				// 更新列表数据
				setView(resultList);
			}
			else
			{
				// 如果没有搜索数据，恢复全部分行数据
				setView(list);
			}
		}
	};

	private void sendRequest()
	{
		showLoading();
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("province", ContentValueBound.levelOneKey);
		upload.put("city", ContentValueBound.levelTwoKey);
		upload.put("bank", ContentValueBound.selectedBank);
		AnsynHttpRequest.requestByPost(CardBoundThreeActivity.this, "/card/getbrach.php", callbackData, upload);
	}

	private HttpCallBack callbackData = new HttpCallBack()
	{

		@Override
		public void back(String data, int url)
		{
			Message msg = new Message();
			msg.obj = data;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			String strJson = (String) msg.obj;
			if (strJson == null || "".equals(strJson))
			{
				dismissLoading();
				showHintDialog("网络不给力！");
				return;
			}

			dismissLoading();

			try
			{
				JSONObject jsonObject = new JSONObject(strJson);
				if (jsonObject.optInt("ecode") == 0)
				{
					String data = jsonObject.optString("data");
					JSONObject object = new JSONObject(data);
					JSONArray array = object.getJSONArray("banks");
					list = new ArrayList<BrachBank>();

					for (int i = 0; i < array.length(); i++)
					{
						JSONObject jsonOb = (JSONObject) array.opt(i);
						String name = jsonOb.optString("name");
						String brach = jsonOb.optString("brach");
						BrachBank info = new BrachBank(name, brach);
						list.add(info);
					}

					setView(list);
				}
				else
				{
					throw new IllegalStateException("ecode!=0" + "\r\n" + "illegal state json(" + strJson + ")");
				}
			}
			catch (JSONException e)
			{
				showHintDialog("数据解析异常");
				WriteLog.recordLog(e.toString() + "\r\n" + "error json(" + strJson + ")");
			}
			catch (IllegalStateException e)
			{
				showHintDialog("数据解析异常");
				WriteLog.recordLog(e.toString());
			}
		}
	};

	public void setView(final ArrayList<BrachBank> bankList)
	{

		if (bankList == null)
		{
			// 数据异常判断。如果意外没有数据，重新请求数据
			showLoading();
			sendRequest();
		}

		lv_listview.setAdapter(new MyAdapter(bankList));
		lv_listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{

				ContentValueBound.selBrachBank = bankList.get(position);
				ContentValueBound.levelThreeKey = bankList.get(position).getName();
				Intent intent = new Intent();
				intent.putExtra("branchname", bankList.get(position).getName());
				intent.putExtra("branchid", bankList.get(position).getCode());
				setResult(20, intent);
				CardBoundThreeActivity.this.finish();
			}
		});
	}

	private class MyAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		private ArrayList<BrachBank> bankList;

		public MyAdapter(ArrayList<BrachBank> bankList)
		{
			this.bankList = bankList;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			return bankList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return bankList.get(position);
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
				holder.line = convertView.findViewById(R.id.line);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(bankList.get(position).getName());
			if (bankList.size() == position + 1)
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

}
