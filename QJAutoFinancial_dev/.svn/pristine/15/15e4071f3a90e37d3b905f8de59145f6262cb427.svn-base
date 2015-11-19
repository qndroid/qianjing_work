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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.qianjing.finance.database.PrefManager;
import com.qianjing.finance.model.activity.LotteryActivity;
import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
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
 * <p>Title: AssembleInvestActivity</p>
 * <p>Description: 理财增值组合首页面</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年4月6日
 */
public class AssembleInvestActivity extends BaseActivity {

	/** 四个选项对应的风险值 */
	private int[] risks = {28, 64, 86, 100};
	/** 被选中的单选按钮 */
	private int checked = risks[0];
	private RadioButton[] rbs;
	private EditText et_money;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	/**
	 * 初始化界面
	 */
	private void initView(){

		setContentView(R.layout.activity_assemble_invest);

		setTitleWithId(R.string.title_assemble_invest);
		
		setTitleBack();

		et_money = (EditText)findViewById(R.id.et_money);
		et_money.addTextChangedListener(new AssembleTextWatcher(et_money));
		
		final RadioGroup radio_group = (RadioGroup)findViewById(R.id.rg);
		rbs = new RadioButton[4];
		rbs[0] = (RadioButton)findViewById(R.id.rb_1);
		rbs[1] = (RadioButton)findViewById(R.id.rb_2);
		rbs[2] = (RadioButton)findViewById(R.id.rb_3);
		rbs[3] = (RadioButton)findViewById(R.id.rb_4);
		
		radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				switch (checkedId) {
				case R.id.rb_1:
					checked = risks[0];
					break;
				case R.id.rb_2:
					checked = risks[1];
					break;
				case R.id.rb_3:
					checked = risks[2];
					break;
				case R.id.rb_4:
					checked = risks[3];
					break;
				default:
					checked = risks[0];
					break;
				}
			}
		});

		
		//如果从修改目标进入的话将初始投资和风险值带过来
		final AssembleBase schemaInfo = getIntent().getParcelableExtra("schemaInfo");
		if(schemaInfo != null){
			et_money.setText(schemaInfo.getInvestInitSum());
			for (int i = 0; i < risks.length; i++) {
				double risk = StringHelper.isBlank(schemaInfo.getInvestRisk()) ? risks[0] : Double.parseDouble(schemaInfo.getInvestRisk());
				if(risk == risks[i]){
					radio_group.check(i);
					rbs[i].setChecked(true);
					break;
				}
			}
		}
		
		
		Button btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final String money = et_money.getText().toString();
				if(StringHelper.isBlank(money)){
					showHintDialog("请填写完全部信息");
					return;
				}

				// 修改组合目标
				if(getIntent().getIntExtra(ViewUtil.TO_SCHEMA_GOAL, 0) == ViewUtil.TO_SCHEMA_GOAL_MODIFY){
					AssembleBase scheme = new AssembleBase();
					scheme.setSid(schemaInfo.getSid());
					scheme.setType(1);
					scheme.setName(schemaInfo.getName());
					scheme.setInvestInitSum(money);
					scheme.setInvestRate("0");
					scheme.setInvestRisk("" + checked);
					
					updateAssemble(scheme);
				}
				// 新建组合
				else {
				
					// 获取方案初始投资金额限制
					requestSchemeLimit();
				}
			}
		});
		
	}
	
	/*
	 * 获取方案初始投资金额限制
	 */
	private void requestSchemeLimit() {
		
		showLoading();
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("type", "1");// 方案类型
		params.put("nm", "");// 方案名
		params.put("init", et_money.getText().toString().trim());// 初始投资金额
		params.put("rate", 0);// 期望收益率
		params.put("risk", checked);// 风险偏好
		params.put("age", 0);// 年龄
		params.put("retire", 0);// 退休年龄
		params.put("month", "0");// 月定投金额
		params.put("money", "0");// 目标金额
		params.put("year", 0);// 投资年数
		AnsynHttpRequest.requestByPost(this, UrlConst.CAL_ASSEMBLY, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.obj = data;
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}, params);
	}

	/**
	 * 修改组合名称
	 * @param name
	 */
	private void updateAssemble(AssembleBase schemaInfo) {
		
		showLoading();
		
		String type = String.valueOf(schemaInfo.getType());
		String nm = schemaInfo.getName();
		String sid = schemaInfo.getSid();
		String init = "";
		String rate = "";
		String risk = "";
		String age = "";
		String retire = "";
		String month = "";
		String money = "";
		String year = "";
		String nmonth = "";
		switch (Integer.parseInt(type)) {
		case 1:
			init = schemaInfo.getInvestInitSum();
			rate = schemaInfo.getInvestRate();
			risk = schemaInfo.getInvestRisk();
			break;
		case 2:
			init = schemaInfo.getPensionInitSum();
			month = schemaInfo.getPensionMonthAmount();
			age = schemaInfo.getPensionCurrentAge();
			retire = schemaInfo.getPensionRetireAge();
			break;
		case 3:
			init = schemaInfo.getHouseInitSum();
			money = schemaInfo.getHouseGoalSum();
			year = schemaInfo.getHouseYears();
			break;
		case 4:
			year = schemaInfo.getChildYears();
			money = schemaInfo.getChildGoalSum();
			break;
		case 5:
			init = schemaInfo.getMarryInitSum();
			money = schemaInfo.getMarryGoalSum();
			year = schemaInfo.getMarryYears();
			break;
		case 6:
			init = schemaInfo.getDreamInitSum();
			nmonth = schemaInfo.getDreamMonths();
			break;
		default:
			init = schemaInfo.getInvestInitSum();
			rate = schemaInfo.getInvestRate();
			risk = schemaInfo.getInvestRisk();
			break;
		}
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", sid); // 组合ID
		upload.put("type", type); // 方案类型
		upload.put("nm", nm);// 方案名
		upload.put("init", init);// 初始投资金额
		upload.put("rate", rate);// 期望收益率
		upload.put("risk", risk);// 风险偏好
		upload.put("age", age);// 年龄
		upload.put("retire", retire);// 退休年龄
		upload.put("month", month);// 月定投金额
		upload.put("money", money);// 目标金额
		upload.put("year", year);// 投资年数
		upload.put("nmonth", nmonth);// 投资年数
		AnsynHttpRequest.requestByPost(this, UrlConst.UPDATE_ASSEMBLE, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.what = FLAG_ASSEMBLE_UPDATE;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private static final int FLAG_ASSEMBLE_UPDATE = 3;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				handleJsonData((String) msg.obj);
				break;
			case 2:
				dismissLoading();
				finish();
				break;
			case FLAG_ASSEMBLE_UPDATE:
				handleUpdateResponse((String) msg.obj);
				break;
			}
		}
	};

	public void handleJsonData(String strJson) {
		
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
				JSONObject data1 = object.optJSONObject("data");
				JSONObject schema = data1.optJSONObject("schema");
				JSONObject limit = schema.optJSONObject("limit");
				double limitNum = limit.optDouble("init");
				
				// 符合最低起购额
				if (Double.parseDouble(et_money.getText().toString().trim()) >= limitNum) {
					
					// 修改方案
					if (getIntent().getIntExtra(ViewUtil.TO_SCHEMA_GOAL, 0) == ViewUtil.TO_SCHEMA_GOAL_MODIFY) {
						
					} 
					// 新建方案
					else {
						Intent intent = new Intent(this, AssembleAIPConfigActivity.class);
						intent.putExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, ViewUtil.FROM_FIND);
						AssembleBase assemble = new AssembleBase();
						assemble.setInvestInitSum(et_money.getText().toString().trim());
						assemble.setInvestRate("0");
						assemble.setInvestRisk("" + checked);
						assemble.setType(Const.ASSEMBLE_INVEST);
						intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, assemble);
						startActivityForResult(intent, ViewUtil.REQUEST_CODE);
					}
				} else {
					showHintDialog("当前投资额不得小于" + limitNum + "元");
				}
			} else {
				showHintDialog(emsg);
			}
		} catch (Exception e) {
			showHintDialog("网络不给力！");
			WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
		}
	}

	private void handleUpdateResponse(String strResponse) {

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
		if (resultCode == ViewUtil.SPECIAL_RESULT_CODE) {
			setResult(ViewUtil.SPECIAL_RESULT_CODE);
			finish();
		}
		// 申购结果返回
		if (resultCode == ViewUtil.ASSEMBLE_ADD_BUY_RESULT_CODE) {
			if (data.hasExtra("lottery")) {
				LotteryActivity lottery = (LotteryActivity) data.getSerializableExtra("lottery");
				if (lottery != null)
					PrefManager.getInstance()
							.putString(PrefManager.KEY_LOTTERY_URL, lottery.strUrl);
			}
			finish();
		}
	}

}
