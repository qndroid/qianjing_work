
package com.qianjing.finance.ui.activity.assemble.type;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.assemble.AssembleAIPConfigActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.AssembleTextWatcher;
import com.qjautofinancial.R;

/**
 * <p>Title: AssembleHouseActivity</p>
 * <p>Description: 存钱买房界面</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年4月23日
 */
public class AssembleHouseActivity extends BaseActivity {

	private EditText et_years;
	private EditText et_money;
	private EditText et_current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		setContentView(R.layout.activity_assemble_house);

		setTitleWithId(R.string.title_assemble_house);

		setTitleBack();

		et_years = (EditText) findViewById(R.id.et_years);
		et_money = (EditText) findViewById(R.id.et_money);
		et_current = (EditText) findViewById(R.id.et_current);
		et_years.addTextChangedListener(new AssembleTextWatcher(et_years));
		et_money.addTextChangedListener(new AssembleTextWatcher(et_money));
		et_current.addTextChangedListener(new AssembleTextWatcher(et_current));

		//如果从修改目标进入的话将值带过来
		final AssembleBase schemaInfo = getIntent().getParcelableExtra("schemaInfo");
		if (schemaInfo != null) {
			et_years.setText(schemaInfo.getHouseYears());
			et_money.setText(schemaInfo.getHouseGoalSum());
			et_current.setText(schemaInfo.getHouseInitSum());
		}

		Button btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String years = et_years.getText().toString();
				String input = et_money.getText().toString();
				String currentInput = et_current.getText().toString();

				if (StringHelper.isBlank(years) || StringHelper.isBlank(input) || StringHelper.isBlank(currentInput)) {
					showHintDialog(getString(R.string.ERR_MSG_INCOMPLETE));
					return;
				}

				// 修改目标
				if (getIntent().getIntExtra(ViewUtil.TO_SCHEMA_GOAL, 0) == ViewUtil.TO_SCHEMA_GOAL_MODIFY) {
					AssembleBase info = new AssembleBase();
					info.setSid(schemaInfo.getSid());
					info.setType(Const.ASSEMBLE_HOUSE);
					info.setName(schemaInfo.getName());
					info.setHouseInitSum(currentInput);
					info.setHouseGoalSum(input);
					info.setHouseYears(years);

					requestUpdateHouse(info);

				} else {

					requestAssembleLimit();
				}
			}
		});
	}

	/*
	 * 获取组合初始投资金额限制
	 */
	private void requestAssembleLimit() {

		showLoading();

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("type", "3");// 组合类型
		params.put("init", et_current.getText().toString());// 初始投资金额
		params.put("money", et_money.getText().toString());// 目标金额
		params.put("year", et_years.getText().toString());// 投资年数
		AnsynHttpRequest.requestByPost(this, UrlConst.CAL_ASSEMBLY, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = FLAG_ASSEMBLE_CAL;
				mHandler.sendMessage(msg);
			}
		}, params);
	}

	private void requestUpdateHouse(AssembleBase info) {

		showLoading();

		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", info.getSid()); // 组合ID
		upload.put("type", info.getType());// 方案类型
		upload.put("nm", info.getName());// 方案名
		upload.put("init", info.getHouseInitSum());// 初始投资金额
		upload.put("money", info.getHouseGoalSum());// 目标金额
		upload.put("nmonth", info.getHouseYears());// 投资月数
		AnsynHttpRequest.requestByPost(this, UrlConst.UPDATE_ASSEMBLE, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private static final int FLAG_ASSEMBLE_UPDATE = 1;
	private static final int FLAG_ASSEMBLE_CAL = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strResponse = (String) msg.obj;
			if (msg.what == FLAG_ASSEMBLE_UPDATE)
				handleAssembleUpdate(strResponse);
			else if (msg.what == FLAG_ASSEMBLE_CAL)
				handleAssembleCal(strResponse);
		}
	};

	public void handleAssembleCal(String strJson) {

		dismissLoading();

		if (StringHelper.isBlank(strJson)) {
			showHintDialog("网络不给力！");
			return;
		}

		try {
			JSONObject object = new JSONObject(strJson);
			int ecode = object.optInt("ecode");
			String emsg = object.optString("emsg");
			if (ecode == 0) {
				JSONObject data = object.optJSONObject("data");
				JSONObject schema = data.optJSONObject("schema");
				JSONObject limit = schema.optJSONObject("limit");
				//				double limitNum = limit.optDouble("init");
				double limitNum = 0;
				// 符合最低起购额
				if (/*!limit.has("init") || */Double.parseDouble(et_current.getText().toString().trim()) >= limitNum) {

					Intent intent = new Intent(AssembleHouseActivity.this, AssembleAIPConfigActivity.class);
					intent.putExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, ViewUtil.FROM_FIND);

					AssembleBase assemble = new AssembleBase();
					assemble.setHouseYears(et_years.getText().toString());
					assemble.setHouseGoalSum(et_money.getText().toString());
					assemble.setHouseInitSum(et_current.getText().toString());
					assemble.setType(Const.ASSEMBLE_HOUSE);
					intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, assemble);

					startActivityForResult(intent, ViewUtil.REQUEST_CODE);
				} else {
					showHintDialog(et_years.getText().toString() + "年目标金额不得小于" + limitNum + "元");
				}

			} else {
				showHintDialog(emsg);
			}
		} catch (Exception e) {
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}

	private void handleAssembleUpdate(String strResponse) {

		dismissLoading();

		if (StringHelper.isBlank(strResponse)) {
			showHintDialog("网络不给力");
			return;
		}

		try {
			JSONObject jsonObject = new JSONObject(strResponse);
			String Succ = jsonObject.getString("ecode");
			String Errormsg = jsonObject.getString("emsg");

			if (Succ.equals("0")) {

				String strData = jsonObject.getString("data");
				AssembleDetail detail = CommonUtil.parseAssembleDetail(strData);

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putParcelable("assembInfo", detail.getAssembleConfig());
				bundle.putParcelable("schemaInfo", detail.getAssembleBase());
				intent.putExtras(bundle);
				setResult(ViewUtil.RESULT_CODE, intent);
				finish();

			} else {
				showHintDialog(Errormsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("更新数据解析失败");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) {
			if (data.hasExtra("lottery")) {
				LotteryActivity lottery = (LotteryActivity) data.getSerializableExtra("lottery");
				if (lottery != null)
					PrefManager.getInstance().putString(PrefManager.KEY_LOTTERY_URL, lottery.strUrl);
			}
			finish();
		}
		if (resultCode == ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE) { // 定投申购结果返回
			setResult(ViewUtil.ASSEMBLE_AIP_BUY_RESULT_CODE, data);
			finish();
		}
	}

}
