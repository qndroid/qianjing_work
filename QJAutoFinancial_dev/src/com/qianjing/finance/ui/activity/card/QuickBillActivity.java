
package com.qianjing.finance.ui.activity.card;

import com.hxcr.chinapay.activity.Initialize;
import com.hxcr.chinapay.util.CPGlobaInfo;
import com.hxcr.chinapay.util.Utils;
import com.qianjing.finance.manager.UserManager;
import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.model.common.CardBound;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.util.UMengStatics;
import com.qianjing.finance.util.WriteLog;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.dialog.ShowDialog;
import com.qjautofinancial.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * <p>
 * Title: QuickBillActivity
 * </p>
 * <p>
 * Description: 快钱主界面
 * </p>
 * 
 * @author majinxing
 * @date 2015年3月25日
 */
public class QuickBillActivity extends BaseActivity implements OnClickListener {

    public static QuickBillActivity mQuickActivity;

    // 封装绑卡信息
    private CardBound cardBound;

    // 银联绑卡
    private static final int LOOP_TIME_LIMIT = 10 * 60 * 1000;
    private long currentTime;
    // handler flag
    private static final int FLAG_REQUEST_COMMIT_QUICKBILL = 1;
    private static final int FLAG_REQUEST_COMMIT_CPU = 2;
    private static final int FLAG_REQUEST_LOOP_CPU = 3;

    private String branchname;
    private String branchid;
    private String bankName;
    private String bankId;

    private ImageView iv_name;
    private ImageView iv_branch;
    private ImageView iv_phone;

    private Button bt_back;
    private LinearLayout ll_bank;
    private LinearLayout ll_branch;
    private LinearLayout ll_mobile;
    private EditText et_name;
    private EditText et_id;
    private TextView tv_bank;
    private TextView tv_branch;
    private EditText et_card;
    private EditText et_phone;
    private Button btn_confirm;
    private CheckBox check;
    private TextView tv_check_agreement;

    public ArrayList<HashMap<String, Object>> listCpu = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UMengStatics.onBindPage1View(this);

        mQuickActivity = this;

        initView();
    }

    private void initView() {

        setContentView(R.layout.quick_bill_activity);

        setLoadingUncancelable();
        setHintLoadingUncancelable();

        iv_name = (ImageView) findViewById(R.id.iv_name);
        iv_branch = (ImageView) findViewById(R.id.iv_branch);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        iv_name.setOnClickListener(this);
        iv_branch.setOnClickListener(this);
        iv_phone.setOnClickListener(this);

        bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(this);
        ll_bank = (LinearLayout) findViewById(R.id.ll_bank);
        ll_branch = (LinearLayout) findViewById(R.id.ll_branch);
        ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);
        ll_bank.setOnClickListener(this);
        ll_branch.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_id = (EditText) findViewById(R.id.et_id);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_branch = (TextView) findViewById(R.id.tv_branch);
        et_card = (EditText) findViewById(R.id.et_card);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        check = (CheckBox) findViewById(R.id.check);
        check.setOnClickListener(this);
        tv_check_agreement = (TextView) findViewById(R.id.tv_check_agreement);
        tv_check_agreement.setOnClickListener(this);

        User user = UserManager.getInstance().getUser();
        String name = user.getName();
        String id = user.getIdentity();
        if (!StringHelper.isBlank(name) && !StringHelper.isBlank(id)) {
            et_name.setText(name);
            et_id.setText(id);
        }
    }

    /*
     * card=银行卡号 identityno=证件号码 realname=真实姓名 bankserial = 银行编号 mobile=手机号 bankname=银行名称 brach=支行号
     */
    private void confirmQuickRequest(CardBound bean) {

        showLoading();

        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", bean.card);
        upload.put("identityno", bean.identityno);
        upload.put("realname", bean.realname);
        upload.put("bankserial", bean.bankserial);
        upload.put("mobile", bean.mobile);
        upload.put("bankname", bean.bankname);
        upload.put("brach", bean.brach);

        // upload.put("card", "6225880155557177");
        // upload.put("identityno", "342422198409060196");
        // upload.put("realname", "方衍");
        // upload.put("bankserial","007" );
        // upload.put("mobile", "18614086960");
        // upload.put("bankname", "招商银行");
        // upload.put("brach", branchid);

        AnsynHttpRequest.requestByPost(this, UrlConst.QUICKBILL_CARD_VERIFY, new HttpCallBack() {
            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.what = FLAG_REQUEST_COMMIT_QUICKBILL;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        }, upload);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_REQUEST_COMMIT_QUICKBILL:
                    // 提交快钱
                    handleQuickRequest((String) msg.obj);
                    break;
                case FLAG_REQUEST_COMMIT_CPU:
                    // 提交银联
                    handleCpuRequest((String) msg.obj);
                    break;
                case FLAG_REQUEST_LOOP_CPU:
                    // 轮询银联绑卡Step2
                    requestBoundVerify();
                    break;
            }
        }
    };

    /*
     * 处理银联绑卡验证步骤的返回数据
     */
    protected void handleCpuRequest(String strJson) {

        dismissHintLoading();

        if (strJson == null || "".equals(strJson)) {
            showHintDialog("网络不给力！");
            return;
        }

        try {
            JSONObject str = new JSONObject(strJson);
            int mecode = str.optInt("ecode");
            String memsg = str.optString("emsg");
            if (mecode == 0) {
                JSONObject jsonObject = new JSONObject(str.optString("data"));
                cardBound.merorderid = jsonObject.optString("merorderid"); // 保存生成的商户订单号

                final String[][] properties = new String[6][2];
                properties[0][0] = "env";// 环境 测试：TEST 生产：PRODUCT
                properties[0][1] = "PRODUCT";
                properties[1][0] = "merchantId";// 商户号
                properties[1][1] = jsonObject.optString("merid");
                properties[2][0] = "merchantOrderId";// 商户订单号
                properties[2][1] = jsonObject.optString("merorderid");
                properties[3][0] = "merchantOrderTime";// 商户订单时间
                properties[3][1] = jsonObject.optString("merordertime");
                properties[4][0] = "orderKey";// cp特征码
                properties[4][1] = jsonObject.optString("charactercode");
                properties[5][0] = "sign";// 签名
                properties[5][1] = jsonObject.optString("sign");

                // System.out.println("调用认证插件前");
                openChinapayPlug(properties);
                // System.out.println("调用认证插件后");
            } else {
                showHintDialog(memsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if (ecode == 1001) {
        // // 绑卡成功
        // currentTime = System.currentTimeMillis();
        //
        // dismissHintLoading();
        //
        // requestBoundVerify();
        // }

        /**
         * 回调银联插件
         */
        if (Utils.getPayResult() != null && !"".equals(Utils.getPayResult())) {
            // 根据返回码做出相应处理
            if (Utils.getPayResult().indexOf("0000") > -1) {
                // 绑卡成功
                currentTime = System.currentTimeMillis();

                dismissHintLoading();

                requestBoundVerify();
            }
            else {
                showHintDialog("认证失败");
            }
            CPGlobaInfo.init(); // 清空返回结果
        }
    }

    // int ecode = 1;
    // boolean isOk = true;
    // private Handler testHandler = new Handler(){
    // public void handleMessage(Message msg) {
    // switch (msg.what) {
    // case 1:
    // ecode = 1001;
    // break;
    // case 2:
    // ecode = 0;
    // break;
    // }
    // }
    // };

    private void handleBoundVerify(String strJson) {

        if (StringHelper.isBlank(strJson)) {
            dismissHintLoading();
            showHintDialog("网络请求失败");
            return;
        }

        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            /*
             * 绑卡成功
             */
            if (ecode == 0) {

                UMengStatics.onBindPage1Submi(this);

                dismissHintLoading();

                // prepare looper
                Looper.prepare();
                ShowDialog.Builder builder = new ShowDialog.Builder(this);
                builder.setMessage("恭喜您绑卡成功");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContentValueBound.isFromCardManager) {
                            Intent intent = new Intent(QuickBillActivity.this,
                                    CardManagerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ContentValueBound.isFromCardManager = false;
                            startActivity(intent);
                        }
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();
                // start looper
                Looper.loop();
            }
            else {
                // 错误码1001：继续等待
                if (ecode == 1001) {
                    // 没有超时则继续轮询
                    if (System.currentTimeMillis() - currentTime < LOOP_TIME_LIMIT) {
                        Message message = Message.obtain();
                        message.what = FLAG_REQUEST_LOOP_CPU;
                        mHandler.sendMessageDelayed(message, 1500);

                        // if (isOk) {
                        // testHandler.sendEmptyMessageDelayed(2, 10*1000);
                        // isOk = false;
                        // }
                    }
                    // 超时则提示用户，停止轮询
                    else {
                        dismissHintLoading();
                        showHintDialog("网络连接超时");
                        return;
                    }
                } else {
                    dismissHintLoading();
                    showHintDialog("绑卡失败，错误原因：" + emsg);
                }
            }
        } catch (JSONException e) {
            dismissHintLoading();
            showHintDialog("数据解析失败");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }
    }

    /*
     * 调用银联插件后的提交请求
     */
    private void requestBoundVerify() {

        if (cardBound == null) {
            showHintLoading("认证失败");
            return;
        }

        if (StringHelper.isBlank(cardBound.bankserial) || StringHelper.isBlank(cardBound.merorderid)
                || StringHelper.isBlank(cardBound.brach) || StringHelper.isBlank(cardBound.card)) {
            showHintLoading("认证失败");
            return;
        }

        showHintLoading("等待银行结果...");

        Hashtable<String, Object> payPlugUpload = new Hashtable<String, Object>();
        // 银行编号
        payPlugUpload.put("bankserial", cardBound.bankserial);
        // 商户订单号
        payPlugUpload.put("merorderid", cardBound.merorderid);
        // 银行分行编号
        payPlugUpload.put("brachbank", cardBound.brach);
        // 银行卡号
        payPlugUpload.put("card", cardBound.card);

        AnsynHttpRequest.requestByPost(this, UrlConst.CHINAPAY_VERIFY, callbackVerify,
                payPlugUpload);
    }

    private HttpCallBack callbackVerify = new HttpCallBack() {

        @Override
        public void back(String data, int url) {

            handleBoundVerify(data);
        }
    };

    /**
     * 调用银联快捷认证插件
     */
    public void openChinapayPlug(String[][] properties) {
        // 初始化手机POS环境
        Utils.setPackageName(getPackageName());
        // 设置Intent指向Initialize.class
        Intent intent = new Intent(this, Initialize.class);// this为你当前的activity.this
        // 传入支付类型
        intent.putExtra(CPGlobaInfo.XML_TAG, createXML(properties));// xml
        // 使用intent跳转至移动认证插件
        startActivity(intent);
    }

    /*
     * 请求报文生成xml
     */
    public String createXML(String[][] properties) {
        String result = null;
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", null);
            xmlSerializer.startTag("", "CpPay");
            xmlSerializer.attribute("", "application", "LunchPay.Req");

            if (properties != null) {
                for (int i = 0; i < properties.length; i++) {
                    xmlSerializer.startTag("", properties[i][0]);
                    xmlSerializer.text(properties[i][1] == null ? ""
                            : properties[i][1]);
                    xmlSerializer.endTag("", properties[i][0]);
                }
            }
            xmlSerializer.endTag("", "CpPay");
            xmlSerializer.endDocument();
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 处理快钱绑卡验证步骤的返回数据
     */
    protected void handleQuickRequest(String strJson) {

        dismissLoading();

        if (strJson == null || "".equals(strJson)) {
            showHintDialog("网络不给力！");
            return;
        }
        try {
            JSONObject object = new JSONObject(strJson);
            int ecode = object.optInt("ecode");
            String emsg = object.optString("emsg");
            JSONObject data = object.optJSONObject("data");
            if (ecode == 0) {
                String accoreqserial = data.optString("accoreqserial");
                String otherserial = data.optString("otherserial");
                Bundle bundle = new Bundle();
                bundle.putString("accoreqserial", accoreqserial);
                bundle.putString("otherserial", otherserial);
                if (cardBound != null) {
                    bundle.putParcelable("bean", cardBound);
                }
                Intent intent = new Intent(this, QuickBillVerifyActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 122);

            } else {
                showHintDialog(emsg);
            }

        } catch (Exception e) {
            showHintDialog("数据解析失败");
            WriteLog.recordLog(e.toString() + "\r\n" + "strJson=" + strJson);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.ll_bank:
                clickBank();
                break;
            case R.id.ll_branch:
                clickBranch();
                break;
            case R.id.btn_confirm:
                confirmInfo();
                break;
            case R.id.check:

                break;
            case R.id.tv_check_agreement:
                Bundle bundle = new Bundle();
                bundle.putInt("webType", 0);
                openActivity(WebActivity.class, bundle);
                break;
            case R.id.iv_name:
                showHintDialog("持卡人说明", nameText);
                break;
            case R.id.iv_branch:
                showHintDialog("所属支行说明", branchText);
                break;
            case R.id.iv_phone:
                showHintDialog("手机号说明", phoneText);
                break;
        }
    }

    private void clickBranch() {
        String bankText = tv_bank.getText().toString();
        if (StringHelper.isBlank(bankText)) {
            showToast("请选择银行");
            return;
        }
        Intent intent = new Intent(QuickBillActivity.this, CardBoundOneActivity.class);
        intent.putExtra("bankname", tv_bank.getText());
        startActivityForResult(intent, 10);
    }

    private void clickBank() {

        tv_branch.setText("");
        Intent intentnew = new Intent(this, QuickBillBankActivity.class);
        if (StringHelper.isBlank(tv_bank.getText().toString())) {
            intentnew.putExtra("bankId", "");
        } else {
            intentnew.putExtra("bankId", bankId);
        }
        ;
        startActivityForResult(intentnew, 111);
    }

    private void confirmInfo() {

        UMengStatics.onBindPage1Click(this);

        String realname = et_name.getText().toString();
        String identityno = et_id.getText().toString();
        String bankname = tv_bank.getText().toString();
        String brach = tv_branch.getText().toString();
        String strCard = et_card.getText().toString();
        String mobile = et_phone.getText().toString();
        if (StringHelper.isBlank(realname) || StringHelper.isBlank(identityno) || StringHelper.isBlank(bankname)
                || StringHelper.isBlank(brach) || StringHelper.isBlank(strCard)) {
            showToast("请填写完全部信息");
            return;
        }
        if (!isMobileGone && StringHelper.isBlank(mobile)) {
            showToast("请填写完全部信息");
            return;
        }
        if (!check.isChecked()) {
            showHintDialog("请先阅读并同意用户协议");
            return;
        }
        // 判断身份证号位数
        if (!StringHelper.checkTheLength(identityno, 15, 18)) {
            showHintDialog("您填写身份证号码位数不正确，请检查后重新输入");
            return;
        }
        // 判断手机号位数
        if (!isMobileGone && !StringHelper.checkTheLength(mobile, 11)) {
            showHintDialog("预留手机号格式不正确，请检查后重新输入");
            return;
        }

        cardBound = new CardBound();
        cardBound.realname = realname;
        cardBound.identityno = identityno;
        cardBound.bankname = bankName;
        cardBound.bankserial = bankId;
        cardBound.brach = branchid;
        cardBound.card = strCard;
        cardBound.mobile = mobile;

        for (int index = 0; index < listCpu.size(); index++) {
            if (listCpu.get(index).get("name").equals(cardBound.bankname)) {
                // 跳转银联
                confirmCpu();
                return;
            }
        }

        // 跳转快钱
        confirmQuickRequest(cardBound);
    }

    // 提交银联
    private void confirmCpu() {

        // ecode = 1001;

        if (cardBound == null) {
            showToast("程序内部错误");
            return;
        }

        showHintLoading("正在跳转到中国银联认证");

        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("card", cardBound.card);
        upload.put("identityno", cardBound.identityno);
        upload.put("bankname", cardBound.bankname);
        upload.put("bankserial", cardBound.bankserial);
        upload.put("username", cardBound.realname);

        AnsynHttpRequest.requestByPost(this, UrlConst.CHINAPAY_COMMIT, new HttpCallBack() {
            @Override
            public void back(String data, int status) {
                Message msg = Message.obtain();
                msg.what = FLAG_REQUEST_COMMIT_CPU;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        }, upload);
    }

    private boolean isMobileGone;

    /*
     * 定义绑卡页面的请求码requestCode=10，一级页面11，二级12 三级页面finish后返回响应resultCode=20
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 20) {// 选择支行返回
            branchname = intent.getStringExtra("branchname");
            branchid = intent.getStringExtra("branchid");
            tv_branch.setText(branchname);
        }
        if (resultCode == 112) {// 选择银行返回
            Card card = intent.getParcelableExtra(Flag.EXTRA_BEAN_CARD_CURRENT);
            bankName = card.getBankName();
            bankId = card.getBankCode();
            tv_bank.setText(bankName);
            if (TextUtils.equals("l", card.getCapitalMode())) {
                ll_mobile.setVisibility(View.VISIBLE);
                isMobileGone = false;
            } else if (TextUtils.equals("3", card.getCapitalMode())) {
                ll_mobile.setVisibility(View.GONE);
                isMobileGone = true;
            }
        }
        if (resultCode == 222) {// 输入短信验证码验证成功
            finish();
        }
    };

    String nameText = "根据证监会的要求，用户申购公募基金必须实名验证，并保证申赎资金同卡进出。\n\n"
            + "请填写您的真实姓名并绑定用户本人持有的银行卡。";

    String branchText = "1.支行网点信息用于赎回时给用户划款。请尽量填写正确支行信息。\n\n"
            + "2.若在支行列表中找不到自己的银行卡开户网点，可以选择就近支行网点填写。\n\n"
            + "3.在选择支行时，可以在页面上部的标题栏搜索关键字。\n\n"
            + "4.获取更多帮助，请拨打客服电话400-893-6885。";

    String phoneText = "银行预留的手机号码是办理该银行卡时所填写的手机号码。\n\n"
            + "没有预留、手机号码忘记或者已停用，请联系银行客服更新处理。";

}
