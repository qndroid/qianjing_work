package com.qianjing.finance.ui.activity.account;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.GestureLockActivity;
import com.qianjing.finance.ui.activity.common.LoginActivity;
import com.qianjing.finance.util.PrefUtil;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.dialog.InputDialog;
import com.qjautofinancial.R;

public class PassWordManagerActivity extends BaseActivity implements OnClickListener {

	private Button rl_passwordset;
	
	private TextView title_middle_text;
	private Button butBack;
	private boolean GUESTURE_PASSWORD_MODIFY = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_manager);
		setTitleBack();
		setTitleWithId(R.string.title_password_setting);
		
		initView();
		setListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void setListener() {
		rl_passwordset.setOnClickListener(this);
		lockCheckBox.setOnClickListener(this);
		modifyTouchWord.setOnClickListener(this);
	}
	
	private void initView() {
		
		rl_passwordset = (Button) findViewById(R.id.rl_passwordset);
		lockCheckBox = (CheckBox) findViewById(R.id.ju_e_check_box);
		modifyTouchWord = (RelativeLayout) findViewById(R.id.rl_modify_pwd);
		
		updatePasswordUI();
	}
	
	@Override
	public void onStart() {
	    updatePasswordUI();
	    super.onStart();
	}
	
    private void updatePasswordUI() {
        if(!"".equals(PrefUtil.getKey(this))){
            lockCheckBox.setChecked(true);
            modifyTouchWord.setVisibility(View.VISIBLE);
        }else{
            lockCheckBox.setChecked(false);
            modifyTouchWord.setVisibility(View.GONE);
        }
    }
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_passwordset:// 登陆密码
			openActivity(PassActivity.class);
			break;
		case R.id.ju_e_check_box:// 手势密码
		    GUESTURE_PASSWORD_MODIFY = false;
		    lockCheckBox.setChecked(!lockCheckBox.isChecked());
		    
		    if("".equals(PrefUtil.getKey(this))){
		        guestureLock();
		    }else{
		        showPasswordDialog();
		    }
		    
			break;
		case R.id.rl_modify_pwd:
		    GUESTURE_PASSWORD_MODIFY = true;
		    showPasswordDialog();
		    
		    break;
		}
	}

    private void showPasswordDialog() {
        
        final InputDialog.Builder inputDialog = new InputDialog.Builder(this,null);
        inputDialog.setTitle("请输入登录密码");
        inputDialog.setNagetiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text=inputDialog.getEditText().getText().toString();
                if(StringHelper.isBlank(text)){
                    Toast.makeText(PassWordManagerActivity.this, "输入登录密码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    sendLoginRequest(text);
                };
                
                dialog.dismiss();
            }
        });
        inputDialog.create().show();
    }
    
	  private void sendLoginRequest(String psw) {
	    	 // 显示加载提示
	        showLoading();
	        
	        Hashtable<String, Object> upload = new Hashtable<String, Object>();
	        upload.put("mb", PrefUtil.getPhoneNumber(this));
	        upload.put("pwd", psw);
	        upload.put("tocken", "1234");
	        String url = "user/login_phone.php";
	        AnsynHttpRequest.requestByPost(this, url,
	                callbackData, upload);
	    }
	    
	    private HttpCallBack callbackData = new HttpCallBack() {
	        @Override
	        public void back(String data, int url) {
	            Message msg = new Message();
	            msg.what=1;
	            msg.obj = data;
	            handler.sendMessage(msg);
	        }
	    };
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				handleJsonData((String)msg.obj);
				break;

			}
		};
	};
    private CheckBox lockCheckBox;

    private RelativeLayout modifyTouchWord;
	    /**
	     * <p>Title: handleJsonData</p>
	     * <p>Description: 处理返回的JSON数据</p>
	     * @param strJson
	     */
	    public void handleJsonData(String strJson) {
	        dismissLoading();
	       // System.out.println("strJson"+strJson);
	        if (strJson==null || "".equals(strJson)) {
	            dismissLoading();
	            showHintDialog("提示", "网络不给力！");
	            return;
	        }

	        try {
		        JSONObject jsonObject = new JSONObject(strJson); // 返回的数据形式是一个Object类型，所以可以直接转换成一个Object
		        String Succ = jsonObject.getString("ecode");
		        String Errormsg = jsonObject.getString("emsg");
		        if ("132".equals(Succ)) {
		        	dismissLoading();
		        	showHintDialog("提示", Errormsg);
		            return;
		        }
		        if ("147".equals(Succ)) {
		        	dismissLoading();
		        	showHintDialog("提示", Errormsg);
		            return;
		        }

		        if ("0".equals(Succ)) {
		            guestureLock();
		        }
		        else {
		        	// 返回信息异常，停止加载提示，显示错误提示
		        	dismissLoading();
		        	showHintDialog("提示", Errormsg);
		            return;
		        }
		        
	        } catch (JSONException e) {
	        	dismissLoading();
	        	showHintDialog("提示", "网络不给力！");
//	        	WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
	        }
	    }
	    
        private void guestureLock() {
            
            if(GUESTURE_PASSWORD_MODIFY){
                PrefUtil.saveKey(this, "");
                Bundle bundle= new Bundle();
                bundle.putBoolean("isFromSet", true);
                openActivity(GestureLockActivity.class, bundle);
                return ;
            }
            if("".equals(PrefUtil.getKey(this))){
                PrefUtil.saveKey(this, "");
                Bundle bundle= new Bundle();
                bundle.putBoolean("isFromSet", true);
                openActivity(GestureLockActivity.class, bundle);
            }else{
                PrefUtil.saveKey(this, "");
                updatePasswordUI();
            }
        }
}