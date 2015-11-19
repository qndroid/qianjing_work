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
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.assemble.AssembleAIPConfigActivity;
import com.qianjing.finance.ui.activity.assemble.AssembleConfigActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.CommonUtil;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.view.AssembleTextWatcher;
import com.qjautofinancial.R;


/**
 * <p>Title: AssembleDreamActivity</p>
 * <p>Description: 梦想基金界面</p>
 * <p>Company: www.qianjing.com</p> 
 * @author fangyan
 * @date 2015年4月23日
 */
public class AssembleDreamActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	/**
	 * 初始化界面
	 */
	private void initView(){
		
		setContentView(R.layout.activity_assemble_dream);
		
		setTitleWithId(R.string.title_assemble_dream);
		
		setTitleBack();

		final EditText et_small_init = (EditText)findViewById(R.id.et_small_init);
		final EditText et_small_nmonth = (EditText)findViewById(R.id.et_small_nmonth);
		et_small_init.addTextChangedListener(new AssembleTextWatcher(et_small_init));
		et_small_nmonth.addTextChangedListener(new AssembleTextWatcher(et_small_nmonth));
		
		//如果从修改目标进入的话将值带过来
		final AssembleBase schemaInfo = getIntent().getParcelableExtra("schemaInfo");
		if(schemaInfo != null){
			et_small_nmonth.setText(schemaInfo.getDreamMonths());
			et_small_init.setText(schemaInfo.getDreamInitSum());
		}
		
		Button btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String small_nmonth = et_small_nmonth.getText().toString();
				String small_init = et_small_init.getText().toString();
				
				if(StringHelper.isBlank(small_nmonth) || StringHelper.isBlank(small_init)){
					showHintDialog(getString(R.string.ERR_MSG_INCOMPLETE));
					return;
				}
				
				if(getIntent().getIntExtra(ViewUtil.TO_SCHEMA_GOAL, 0)==ViewUtil.TO_SCHEMA_GOAL_MODIFY){
					AssembleBase scheme = new AssembleBase();
					scheme.setSid(schemaInfo.getSid());
					scheme.setType(6);
					scheme.setName(schemaInfo.getName());
					scheme.setDreamInitSum(small_init);
					scheme.setDreamMonths(small_nmonth);

					AssembleBase info = new AssembleBase();
					info.setSid(schemaInfo.getSid());
					info.setType(Const.ASSEMBLE_DREAM);
					info.setName(schemaInfo.getName());
					info.setDreamInitSum(small_init);
					info.setDreamMonths(small_nmonth);
					
					requestUpdateDream(info);
					
				} else {
					Intent intent = new Intent(AssembleDreamActivity.this, AssembleAIPConfigActivity.class);
					intent.putExtra(ViewUtil.FLAG_ASSEMBLE_DETAIL, ViewUtil.FROM_FIND);
					AssembleBase assemble = new AssembleBase();
					assemble.setType(Const.ASSEMBLE_DREAM);
					assemble.setDreamMonths(small_nmonth);
					assemble.setDreamInitSum(small_init);
					intent.putExtra(Flag.EXTRA_BEAN_ASSEMBLE_BASE, assemble);
					startActivityForResult(intent, ViewUtil.REQUEST_CODE);
				}
			}
		});
	}

	private void requestUpdateDream(AssembleBase info) {
	    
		showLoading();
		
		Hashtable<String, Object> upload = new Hashtable<String, Object>();
		upload.put("sid", info.getSid()); // 组合ID
		upload.put("type", info.getType());// 方案类型
		upload.put("nm", info.getName());// 方案名
		upload.put("init", info.getDreamInitSum());// 初始投资金额
		upload.put("rate", "0");// 期望收益率
		upload.put("risk", 0);// 风险偏好
		upload.put("age", 0);// 年龄
		upload.put("retire", 0);// 退休年龄
		upload.put("month", "0");// 月定投金额
		upload.put("money", "0");// 目标金额
		upload.put("year", 0);// 投资年数
		upload.put("nmonth", info.getDreamMonths());// 投资月数
		AnsynHttpRequest.requestByPost(this, UrlConst.UPDATE_ASSEMBLE, new HttpCallBack() {
			@Override
			public void back(String data, int url) {
				Message msg = new Message();
				msg.obj=data;
				mHandler.sendMessage(msg);
			}
		}, upload);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strResponse = (String) msg.obj;
			handleResponse(strResponse);
		}
	};
	
	private void handleResponse(String strResponse) {
	    
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
					PrefManager.getInstance()
							.putString(PrefManager.KEY_LOTTERY_URL, lottery.strUrl);
			}
			finish();
		}
	}

}
