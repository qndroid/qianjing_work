package com.qianjing.finance.ui.activity.common;

import java.util.Hashtable;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.constant.CustomConstants;
import com.qianjing.finance.model.common.DealWithSchemaInter;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qjautofinancial.R;

/**
 * @category 修改方案以及删除方案
 * @author liuchen
 * 
 * */

public class DealWithSchema extends BaseActivity implements OnClickListener {

	private static final int MOTIFY_SCHEMA = 0x01;
	private static final int DELETE_SCHEMA = 0x02;

	private DealWithSchemaInter dealWithSchemaInter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	public void showAnimation(View view) {

		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(500);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
		alphaAnimation.setDuration(500);
		AnimationSet animationSet = new AnimationSet(false);

		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);

		view.startAnimation(animationSet);
	}

	private void initView() {

		View view = View
				.inflate(this, R.layout.activity_deal_with_schema, null);
		showAnimation(view);
		setContentView(view);

		dealWithSchemaInter = (DealWithSchemaInter) getIntent()
				.getSerializableExtra("dealWithSchemaInter");

		/**
		 * find view by id
		 * */

		etItem1 = (EditText) findViewById(R.id.et_item_1);
		etItem2 = (EditText) findViewById(R.id.et_item_2);
		etItem3 = (EditText) findViewById(R.id.et_item_3);
		etItem4 = (EditText) findViewById(R.id.et_item_4);

		tvItem1 = (TextView) findViewById(R.id.tv_item_1);
		tvItem2 = (TextView) findViewById(R.id.tv_item_2);
		tvItem3 = (TextView) findViewById(R.id.tv_item_3);
		tvItem4 = (TextView) findViewById(R.id.tv_item_4);

		Button sure = (Button) findViewById(R.id.btn_sure);
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);

		switch (dealWithSchemaInter.getInterType()) {

		case 1:
			modifySchemaName();
			break;
		case 2:
			requestDeleteSchema();
			break;
		case 3:
			modifySchema();
			break;
		}

	}

	private void modifySchema() {
		dealTypeWithFill(dealWithSchemaInter.getType());
	}

	private void modifySchemaName() {

		etItem2.setVisibility(View.GONE);
		etItem3.setVisibility(View.GONE);
		etItem4.setVisibility(View.GONE);

		tvItem2.setVisibility(View.GONE);
		tvItem3.setVisibility(View.GONE);
		tvItem4.setVisibility(View.GONE);

		etItem1.setText(dealWithSchemaInter.getNm());

		tvItem1.setText("修改组合名称");

	}

	public void requestModifySchema() {
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("sid", dealWithSchemaInter.getSid());
		hashTable.put("nm", dealWithSchemaInter.getNm());
		hashTable.put("type", dealWithSchemaInter.getType());

		if ("1".equals(dealWithSchemaInter.getType())) {
			hashTable.put("init",
					dealWithSchemaInter.getSchema().financial_init);
			hashTable.put("rate",
					dealWithSchemaInter.getSchema().financial_rate);
			hashTable.put("risk",
					dealWithSchemaInter.getSchema().financial_risk);
		} else if ("2".equals(dealWithSchemaInter.getType())) {
			hashTable.put("init", dealWithSchemaInter.getSchema().pension_init);
			hashTable.put("month",
					dealWithSchemaInter.getSchema().pension_month);
			hashTable.put("retire",
					dealWithSchemaInter.getSchema().pension_retire);
			hashTable.put("age", dealWithSchemaInter.getSchema().pension_age);
		} else if ("3".equals(dealWithSchemaInter.getType())) {
			hashTable.put("init", dealWithSchemaInter.getSchema().house_init);
			hashTable.put("money", dealWithSchemaInter.getSchema().house_money);
			hashTable.put("year", dealWithSchemaInter.getSchema().house_year);
		} else if ("4".equals(dealWithSchemaInter.getType())) {
			hashTable.put("money",
					dealWithSchemaInter.getSchema().education_money);
			hashTable.put("year",
					dealWithSchemaInter.getSchema().education_year);
		} else if ("5".equals(dealWithSchemaInter.getType())) {
			hashTable.put("init", dealWithSchemaInter.getSchema().married_init);
			hashTable.put("money",
					dealWithSchemaInter.getSchema().married_money);
			hashTable.put("year", dealWithSchemaInter.getSchema().married_year);
		} else if ("6".equals(dealWithSchemaInter.getType())) {
			hashTable.put("init", dealWithSchemaInter.getSchema().small_init);
			hashTable.put("month", dealWithSchemaInter.getSchema().small_month);
			hashTable.put("nmonth",
					dealWithSchemaInter.getSchema().small_nmonth);
		} else if("7".equals(dealWithSchemaInter.getType())){
			hashTable.put("init", dealWithSchemaInter.getSchema().financial2_init);
			hashTable.put("age", dealWithSchemaInter.getSchema().financial2_age);
			hashTable.put("risk", dealWithSchemaInter.getSchema().financial2_risk);
			hashTable.put("preference", dealWithSchemaInter.getSchema().financial2_preference);
		}

		AnsynHttpRequest.requestByPost(this,
				UrlConst.VIRTUAL_MODIFY_SCHEME, new HttpCallBack() {

					@Override
					public void back(String data, int status) {
						Message msg = Message.obtain();
						msg.obj = data;
						msg.what = MOTIFY_SCHEMA;
						handler.sendMessage(msg);
					}
				}, hashTable);
	}

	public void requestDeleteSchema() {
		showLoading();
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
		hashTable.put("sid", dealWithSchemaInter.getSid());

		AnsynHttpRequest.requestByPost(this,
				UrlConst.VIRTUAL_DELETE_SCHEME, new HttpCallBack() {

					@Override
					public void back(String data, int status) {
						Message msg = Message.obtain();
						msg.obj = data;
						msg.what = DELETE_SCHEMA;
						handler.sendMessage(msg);
					}
				}, hashTable);
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			String jsonStr = (String) msg.obj;
			switch (msg.what) {
			case MOTIFY_SCHEMA:
				analysisModifySchema(jsonStr);
				break;
			case DELETE_SCHEMA:
				analysisDeleteSchema(jsonStr);
				break;
			}
		};
	};
	private EditText etItem1;
	private EditText etItem2;
	private EditText etItem3;
	private EditText etItem4;
	private TextView tvItem1;
	private TextView tvItem2;
	private TextView tvItem3;
	private TextView tvItem4;

	protected void analysisModifySchema(String jsonStr) {

		if (jsonStr == null || "".equals(jsonStr)) {
			dismissLoading();
			Toast.makeText(this, getString(R.string.net_prompt),
					Toast.LENGTH_LONG).show();
			endActivity();
			return;
		}

		try {

			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");

			if (ecode == 0) {
				Toast.makeText(this, "方案修改成功", Toast.LENGTH_LONG).show();
				CustomConstants.REFESH_VIRTUAL_LIST = true;
				dismissLoading();
				endActivity();
			} else {
				Toast.makeText(this, emsg, Toast.LENGTH_LONG).show();
				dismissLoading();
				endActivity();
			}
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.net_data_error),
					Toast.LENGTH_LONG).show();
			dismissLoading();
			endActivity();
		}

	}

	protected void analysisDeleteSchema(String jsonStr) {
		if (jsonStr == null || "".equals(jsonStr)) {
			dismissLoading();
			Toast.makeText(this, getString(R.string.net_prompt),
					Toast.LENGTH_LONG).show();
			endActivity();
			return;
		}

		try {

			JSONObject jobj = new JSONObject(jsonStr);
			int ecode = jobj.optInt("ecode");
			String emsg = jobj.optString("emsg");

			if (ecode == 0) {
				Toast.makeText(this, "方案删除成功", Toast.LENGTH_LONG).show();
				CustomConstants.REFESH_VIRTUAL_LIST = true;
				dismissLoading();
				setResult(CustomConstants.SHUDDOWN_ACTIVITY);
				finish();

			} else {
				Toast.makeText(this, emsg, Toast.LENGTH_LONG).show();
				dismissLoading();
				endActivity();
			}
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.net_data_error),
					Toast.LENGTH_LONG).show();
			dismissLoading();
			endActivity();
		}
	}

	public void endActivity() {
		setResult(CustomConstants.MODIFY_SCHEMA);
		finish();
	}

	public void dealTypeWithFill(String type) {

		if ("1".equals(type)) {
			// 2-----显示条目数
			etItem2.setVisibility(View.GONE);
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			tvItem2.setVisibility(View.GONE);
			tvItem3.setVisibility(View.GONE);
			tvItem4.setVisibility(View.GONE);

			tvItem1.setText("初始投入资金");
			etItem1.setText(dealWithSchemaInter.getSchema().financial_init + "");

		} else if ("2".equals(type)) {
			// 4
			etItem1.setText(dealWithSchemaInter.getSchema().pension_age + "");
			etItem2.setText(dealWithSchemaInter.getSchema().pension_init + "");
			etItem3.setText(dealWithSchemaInter.getSchema().pension_month + "");
			etItem4.setText(dealWithSchemaInter.getSchema().pension_retire + "");

			tvItem1.setText("我的年龄");
			tvItem2.setText("我当前可以投入多少钱");
			tvItem3.setText("每月准备拿出多少钱");
			tvItem4.setText("我的退休年龄");

		} else if ("3".equals(type)) {
			// 3
			etItem4.setVisibility(View.GONE);
			tvItem4.setVisibility(View.GONE);

			etItem1.setText(dealWithSchemaInter.getSchema().house_year + "");
			etItem2.setText(dealWithSchemaInter.getSchema().house_money + "");
			etItem3.setText(dealWithSchemaInter.getSchema().house_init + "");

			tvItem1.setText("打算几年后购房");
			tvItem2.setText("预计需要多少钱");
			tvItem3.setText("当前可以拿出多少钱");

		} else if ("4".equals(type)) {
			// 2
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			tvItem3.setVisibility(View.GONE);
			tvItem4.setVisibility(View.GONE);

			etItem1.setText(dealWithSchemaInter.getSchema().education_year + "");
			etItem2.setText(dealWithSchemaInter.getSchema().education_money
					+ "");

			tvItem1.setText("多久后会用到这笔钱");
			tvItem2.setText("打算每月投入多少钱");

		} else if ("5".equals(type)) {
			// 3
			etItem4.setVisibility(View.GONE);

			tvItem4.setVisibility(View.GONE);

			etItem1.setText(dealWithSchemaInter.getSchema().married_year + "");
			etItem2.setText(dealWithSchemaInter.getSchema().married_money + "");
			etItem3.setText(dealWithSchemaInter.getSchema().married_init + "");

			tvItem1.setText("多久后会用到这笔钱");
			tvItem2.setText("预计需要多少钱");
			tvItem3.setText("当前可以拿出多少钱");

		} else if ("6".equals(type)) {
			// 2
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			tvItem3.setVisibility(View.GONE);
			tvItem4.setVisibility(View.GONE);

			etItem1.setText(dealWithSchemaInter.getSchema().small_init + "");
			etItem2.setText(dealWithSchemaInter.getSchema().small_month + "");

			tvItem1.setText("投资金额");
			tvItem2.setText("预计期限(月)");
		}
	}

	public void dealTypeWithGet(String type) {

		if ("1".equals(type)) {
			// 2-----显示条目数
			etItem2.setVisibility(View.GONE);
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			dealWithSchemaInter.getSchema().financial_init = Float
					.parseFloat(etItem1.getText().toString().trim());

		} else if ("2".equals(type)) {
			// 4
			dealWithSchemaInter.getSchema().pension_age = Float
					.parseFloat(etItem1.getText().toString().trim());

			dealWithSchemaInter.getSchema().pension_init = Float
					.parseFloat(etItem2.getText().toString().trim());

			dealWithSchemaInter.getSchema().pension_month = Float
					.parseFloat(etItem3.getText().toString().trim());

			dealWithSchemaInter.getSchema().pension_retire = Float
					.parseFloat(etItem4.getText().toString().trim());

		} else if ("3".equals(type)) {
			// 3
			etItem4.setVisibility(View.GONE);

			dealWithSchemaInter.getSchema().house_year = Float
					.parseFloat(etItem1.getText().toString().trim());
			dealWithSchemaInter.getSchema().house_money = Float
					.parseFloat(etItem2.getText().toString().trim());
			dealWithSchemaInter.getSchema().house_init = Float
					.parseFloat(etItem3.getText().toString().trim());

		} else if ("4".equals(type)) {
			// 2
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			dealWithSchemaInter.getSchema().education_year = Float
					.parseFloat(etItem1.getText().toString().trim());
			dealWithSchemaInter.getSchema().education_money = Float
					.parseFloat(etItem2.getText().toString().trim());

		} else if ("5".equals(type)) {
			// 3
			etItem4.setVisibility(View.GONE);

			dealWithSchemaInter.getSchema().married_year = Float
					.parseFloat(etItem1.getText().toString().trim());
			dealWithSchemaInter.getSchema().married_money = Float
					.parseFloat(etItem2.getText().toString().trim());
			dealWithSchemaInter.getSchema().married_init = Float
					.parseFloat(etItem3.getText().toString().trim());

		} else if ("6".equals(type)) {
			// 2
			etItem3.setVisibility(View.GONE);
			etItem4.setVisibility(View.GONE);

			dealWithSchemaInter.getSchema().small_init = Float
					.parseFloat(etItem1.getText().toString().trim());
			dealWithSchemaInter.getSchema().small_month = Float
					.parseFloat(etItem2.getText().toString().trim());

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_sure:
			sumbit();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		}
	}

	public void sumbit() {

		switch (dealWithSchemaInter.getInterType()) {

		case 3:
			dealTypeWithGet(dealWithSchemaInter.getType());
			requestModifySchema();
			break;
		case 1:
			dealWithSchemaInter.setNm(etItem1.getText().toString().trim());
			requestModifySchema();
			break;
		}

	}

}
