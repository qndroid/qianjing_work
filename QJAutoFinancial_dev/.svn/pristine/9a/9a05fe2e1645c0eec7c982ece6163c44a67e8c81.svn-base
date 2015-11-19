package com.qianjing.finance.ui.activity.assemble;

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

import com.qianjing.finance.model.assemble.AssembleBase;
import com.qianjing.finance.model.assemble.AssembleDetail;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;


/**
 * <p>Title: AssembleModifyNameActivity</p>
 * <p>Description: 保存组合名称界面</p>
 * @author fangyan
 * @date 2015年4月14日
 */
public class AssembleModifyNameActivity extends BaseActivity {

	/** 组合详情实例 */
	private AssembleDetail mAssembleDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	private void initView() {

		setContentView(R.layout.activity_assemble_modify_name);
		
		setTitleBack();
		
		setTitleWithId(R.string.TITLE_FIND_MODIFY_NAME);

		mAssembleDetail = getIntent().getParcelableExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL);
		final AssembleBase assemble = mAssembleDetail.getAssembleBase();

		final EditText et_name = (EditText)findViewById(R.id.et_name);
		et_name.setText(assemble.getName());

		Button btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String name = et_name.getText().toString();
				if(StringHelper.isBlank(name)){
					showHintDialog("请输入组合名称");
					return;
				}
				
				assemble.setName(name);
				updateAssemble(assemble);
			}
		});
	}
	
	private static final int FLAG_ASSEMBLE_UPDATE = 11;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strResponse = (String) msg.obj;
			switch (msg.what) { 
			case FLAG_ASSEMBLE_UPDATE:
				handleUpdateResponse(strResponse);
				break;
			}
		}
	};
	
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
				intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_DETAIL, detail);
				setResult(ViewUtil.ASSEMBLE_UPDATE_RESULT_CODE, intent);
				finish();
				
			} else {
				showHintDialog(Errormsg);
				return;
			}
		} catch (JSONException e) {
			showHintDialog("更新数据解析失败");
		}
	}

	/**
	 * <p>Title: updateAssemble</p>
	 * <p>Description: 更新组合</p>
	 * @param assemble
	 */
	private void updateAssemble(AssembleBase assemble) {
		showLoading();
		String type = String.valueOf(assemble.getType());
		String nm = assemble.getName();
		String sid = assemble.getSid();
		String init = "";
		String rate = "";
		String risk = "";
		String age = "";
		String retire = "";
		String month = "";
		String money = "";
		String year = "";
		String nmonth = "";
		String preference = "";
		switch (Integer.parseInt(type)) {
		case Const.ASSEMBLE_INVEST:
			init = assemble.getInvestInitSum();
			rate = assemble.getInvestRate();
			risk = assemble.getInvestRisk();
			break;
		case Const.ASSEMBLE_PENSION:
			init = assemble.getPensionInitSum();
			month = assemble.getPensionMonthAmount();
			age = assemble.getPensionCurrentAge();
			retire = assemble.getPensionRetireAge();
			break;
		case Const.ASSEMBLE_HOUSE:
			init = assemble.getHouseInitSum();
			money = assemble.getHouseGoalSum();
			year = assemble.getHouseYears();
			break;
		case Const.ASSEMBLE_CHILDREN:
			year = assemble.getChildYears();
			money = assemble.getChildGoalSum();
			break;
		case Const.ASSEMBLE_MARRY:
			init = assemble.getMarryInitSum();
			money = assemble.getMarryGoalSum();
			year = assemble.getMarryYears();
			break;
		case Const.ASSEMBLE_DREAM:
			init = assemble.getDreamInitSum();
			nmonth = assemble.getDreamMonths();
			break;
		case Const.ASSEMBLE_SPESIAL:
			init = String.valueOf(assemble.getSpecialInitSum());
			age = String.valueOf(assemble.getSpecialAge());
			risk = assemble.getSpecialRisk();
			preference = assemble.getSpecialPref();
			break;
		}
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", sid); // 组合ID
		upload.put("type", type); // 组合类型
		upload.put("nm", nm);// 组合名
		upload.put("init", init);// 初始投资金额
		upload.put("rate", rate);// 期望收益率
		upload.put("risk", risk);// 风险偏好
		upload.put("age", age);// 年龄
		upload.put("retire", retire);// 退休年龄
		upload.put("month", month);// 月定投金额
		upload.put("money", money);// 目标金额
		upload.put("year", year);// 投资年数
		upload.put("nmonth", nmonth); // 预期月份
		upload.put("preference", preference); // 风险偏好
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ViewUtil.SPECIAL_RESULT_CODE) {
			setResult(ViewUtil.SPECIAL_RESULT_CODE);
			finish();
		}
	}
	
}
