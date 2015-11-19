package com.qianjing.finance.ui.activity.history;

import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.StringHelper;
import com.qjautofinancial.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Hashtable;

public class SteadyHistoryDetails extends BaseActivity{

	private RelativeLayout rlArriveItem;
	private TextView value;
    private TextView walletName;
    private TextView walletType;
    private TextView walletTime;
    private TextView walletBank;
    private TextView walletATime;
    public int PROFIT_RED  = 0xFFFF3B3B;
    public int PROFIT_GREEN  = 0xFF63CD99;
    private int status;
    private TextView walletATimeT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_wallet_details);
		setTitleBack();
		
		initView();
		requestP2pHistoryDetail();
	}
	
	private void requestP2pHistoryDetail() {
	    showLoading();
	    Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
	    hashTable.put("mobile", UserManager.getInstance().getUser().getMobile());
	    hashTable.put("order_sn", getIntent().getStringExtra("orderSn"));
	    hashTable.put("type", 1);
	    AnsynHttpRequest.requestByPost(this,
                UrlConst.P2P_HISTORY_DETAIL, new HttpCallBack() {
                    @Override
                    public void back(String data, int status) {
//                        LogUtils.syso("p2p历史记录交易详情："+data);
                        Message msg = Message.obtain();
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                }, hashTable);
    }
	
	Handler handler = new Handler(){
	  public void handleMessage(Message msg) {
	      String jsonStr = (String) msg.obj;
	      analysisDetailData(jsonStr);
	  };
	};
    
    
    private void initView() {
		
		setTitleWithString("交易详情");
		
		TextView valueTitle = (TextView) findViewById(R.id.tv_wallet_value_title);
		value = (TextView) findViewById(R.id.tv_wallet_value);
		walletName = (TextView) findViewById(R.id.tv_wallet_name);
		walletType = (TextView) findViewById(R.id.tv_wallet_type);
		walletTime = (TextView) findViewById(R.id.tv_wallet_time);
		walletBank = (TextView) findViewById(R.id.tv_wallet_bank);
		walletATime = (TextView) findViewById(R.id.tv_wallet_arrive_time);
		walletATimeT = (TextView) findViewById(R.id.tv_wallet_arrive_title);
		rlArriveItem = (RelativeLayout) findViewById(R.id.rl_arrive_item);
		ImageView stateIcon = (ImageView) findViewById(R.id.iv_state_icon);
		TextView stateText = (TextView) findViewById(R.id.tv_state_text);
		
		
		
		walletATimeT.setText("");
		valueTitle.setText("交易金额:");
		
		status = Integer.parseInt(getIntent().getStringExtra("status"));
		if(status==0){
		    stateText.setText("处理中");
		    stateIcon.setBackgroundResource(R.drawable.history_ing);
		    walletType.setText("取现");
		}else if(status==1){
		    stateText.setText("取现成功");
            stateIcon.setBackgroundResource(R.drawable.history_success);
            walletType.setText("取现");
		}else if(status==2){
		    stateText.setText("存入成功");
		    walletType.setText("存入");
            stateIcon.setBackgroundResource(R.drawable.history_success);
		}
	}
    
    protected void analysisDetailData(String jsonStr) {
        
//        LogUtils.syso("保本交易详情:"+jsonStr);
        if (jsonStr == null || "".equals(jsonStr)) {
            dismissLoading();
            showHintDialog(getString(R.string.net_prompt));
            return;
        }
        
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int ecode = jsonObj.optInt("ecode");
            String emsg = jsonObj.optString("emsg");
            if (ecode == 0) {
                
                JSONObject optJSONObject = jsonObj.optJSONObject("data");
                String detailValue = optJSONObject.optString("amount");
                String detailName = optJSONObject.optString("deal_name");
                String detailType = optJSONObject.optString("type_id");
                String detailProfitTime = optJSONObject.optString("profit_time");
                String detailBank = optJSONObject.optString("bank_name");
                String detailCard = optJSONObject.optString("card_no");
                String detailTradeTime = optJSONObject.optString("trade_time");
                
                if(!StringHelper.isBlank(detailValue)){
                    float valueNum = Float.parseFloat(detailValue);
                    if(status==2){
                        value.setTextColor(PROFIT_RED);
                    }else{
                        value.setTextColor(PROFIT_GREEN);
                    }
                    value.setText("¥"+StringHelper.formatDecimal(valueNum));
                }else{
                    value.setText("--");
                }
                walletName.setText(detailName);
                
                if(!StringHelper.isBlank(detailTradeTime)){
                    walletTime.setText(DateFormatHelper.formatDate(detailTradeTime+"000", DateFormatHelper.DateFormat.DATE_4));
                }
                if(!StringHelper.isBlank(detailProfitTime) && (status==0 || status==1)){
                    walletATimeT.setText("到账情况");
                    walletATime.setText("预计"+DateFormatHelper.formatDate(detailProfitTime+"000", DateFormatHelper.DateFormat.DATE_1)+"到账");
                }else if(!StringHelper.isBlank(detailProfitTime) && (status==2)){
                    walletATimeT.setText("收益情况");
                    walletATime.setText(DateFormatHelper.formatDate(detailProfitTime+"000", DateFormatHelper.DateFormat.DATE_1)+"当日起息");
                }
                
                walletBank.setText(detailBank+"(尾号"+StringHelper.showCardLast4(detailCard)+")");
                dismissLoading();
            } else {
                dismissLoading();
                showHintDialog(emsg);
            }
        } catch (JSONException e) {
            dismissLoading();
            e.printStackTrace();
        }
    }
}
