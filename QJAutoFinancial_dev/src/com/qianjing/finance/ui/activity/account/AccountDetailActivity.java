
package com.qianjing.finance.ui.activity.account;

import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.common.BaseModel;
import com.qianjing.finance.model.common.User;
import com.qianjing.finance.net.RequestManager;
import com.qianjing.finance.net.connection.XCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.ViewUtil;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.widget.adapter.AccountDetailAdapter;
import com.qjautofinancial.R;

public class AccountDetailActivity extends BaseActivity implements
        OnClickListener {
    private AccountDetailAdapter adapter;
    private String[] titleStrs = new String[] {
            "电子邮箱", "通讯地址", "婚姻状况",
            "学历", "职业", "证券投资"
    };
    private int itemposition = -1;// listview 条目的一个标记位

    private User user;
    private int infoType;

    private LinearLayout ll_email;
    private EditText et_emailaddress;
    private Button bt_emailsave;

    private LinearLayout ll_address;
    private EditText et_address;
    private Button bt_addresssave;

    private LinearLayout ll_marriage;
    private RelativeLayout rl_marriageno;
    private RelativeLayout rl_marriageyes;
    private ImageView im_marriageno;
    private ImageView im_marriageyes;

    private LinearLayout ll_education;
    private RelativeLayout rl_education01;
    private RelativeLayout rl_education02;
    private RelativeLayout rl_education03;
    private RelativeLayout rl_education04;
    private ImageView im_education01;
    private ImageView im_education02;
    private ImageView im_education03;
    private ImageView im_education04;

    private LinearLayout ll_invest;
    private RelativeLayout rl_investyes;
    private RelativeLayout rl_investno;
    private ImageView im_investyes;
    private ImageView im_investno;

    private ListView lv_job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        initView();

        setListener();

        showDetailView();
    }

    private void initData() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        infoType = intent.getIntExtra("infoType", 0);
    }

    private void showDetailView() {
        switch (infoType) {
            case 0:// 显示电子邮箱布局
                ll_email.setVisibility(View.VISIBLE);
                break;
            case 1:// 显示通讯地址布局
                ll_address.setVisibility(View.VISIBLE);
                et_address.setText(user.getAddr());
                break;
            case 2:// 显示婚姻状况布局
                ll_marriage.setVisibility(View.VISIBLE);
                setMarrView(Integer.parseInt(user.getHasMarried()));
                break;
            case 3:// 显示学历布局
                ll_education.setVisibility(View.VISIBLE);
                setEduView(Integer.parseInt(user.getEducate()));
                // if(eduText[Integer.parseInt(intent.getStringExtra("educate"))])
                break;
            case 4:// 显示职业布局
                lv_job.setVisibility(View.VISIBLE);
                adapter = new AccountDetailAdapter(this, itemposition);
                lv_job.setAdapter(adapter);
                break;
            case 5:// 显示证券投资经验布局
                ll_invest.setVisibility(View.VISIBLE);
                setInvestView(Integer.parseInt(user.getHasInvest()));
                break;
        }
    }

    void saveInfo(String key, String value) {
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put(key, value);
        RequestManager.request(this, UrlConst.SAVE_INFO, upload, BaseModel.class,
                new XCallBack() {
                    @Override
                    public void success(BaseModel model) {
                        if (model.stateCode == ErrorConst.EC_OK) {
                            setResult(ViewUtil.USER_INFO_MODIFY_CODE);
                            finish();
                        } else {
                            showHintDialog(model.strErrorMessage);
                        }
                    }

                    @Override
                    public void fail() {
                    }
                });
    }

    private void setEduView(int parseInt) {
        im_education01.setVisibility(View.INVISIBLE);
        im_education02.setVisibility(View.INVISIBLE);
        im_education03.setVisibility(View.INVISIBLE);
        im_education04.setVisibility(View.INVISIBLE);
        switch (parseInt) {
            case 1:
                im_education01.setVisibility(View.VISIBLE);
                break;
            case 2:
                im_education02.setVisibility(View.VISIBLE);
                break;
            case 3:
                im_education03.setVisibility(View.VISIBLE);
                break;
            case 4:
                im_education04.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setMarrView(int parseInt) {
        im_marriageno.setVisibility(View.INVISIBLE);
        im_marriageyes.setVisibility(View.INVISIBLE);
        switch (parseInt) {
            case 0:
                im_marriageno.setVisibility(View.VISIBLE);
                break;
            case 1:
                im_marriageyes.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setInvestView(int parseInt) {
        im_investyes.setVisibility(View.INVISIBLE);
        im_investno.setVisibility(View.INVISIBLE);
        switch (parseInt) {
            case 0:
                im_investyes.setVisibility(View.VISIBLE);
                break;
            case 1:
                im_investno.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setListener() {
        bt_emailsave.setOnClickListener(this);
        bt_addresssave.setOnClickListener(this);
        rl_marriageno.setOnClickListener(this);
        rl_marriageyes.setOnClickListener(this);
        rl_education01.setOnClickListener(this);
        rl_education02.setOnClickListener(this);
        rl_education03.setOnClickListener(this);
        rl_education04.setOnClickListener(this);
        rl_investyes.setOnClickListener(this);
        rl_investno.setOnClickListener(this);
        lv_job.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (itemposition != -1) {
                    lv_job.getChildAt(itemposition).findViewById(R.id.im_ico)
                            .setVisibility(View.INVISIBLE);
                }
                itemposition = position;
                lv_job.getChildAt(itemposition).findViewById(R.id.im_ico)
                        .setVisibility(View.VISIBLE);
                int temp = position + 1;
                saveInfo("work", temp + "");
            }
        });
    }

    private void initView() {

        setContentView(R.layout.activity_user_info_detail);
        setTitleBack();

        if (infoType >= 0 && infoType <= 5) {
            setTitleWithString(titleStrs[infoType]);
        }

        /*
         * 电子邮箱部分的id初始化
         */
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        et_emailaddress = (EditText) findViewById(R.id.et_emailaddress);
        bt_emailsave = (Button) findViewById(R.id.bt_emailsave);
        /*
         * 通讯地址部分的id初始化
         */
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        et_address = (EditText) findViewById(R.id.et_address);
        bt_addresssave = (Button) findViewById(R.id.bt_addresssave);
        /*
         * 婚姻状况部分的id初始化
         */
        ll_marriage = (LinearLayout) findViewById(R.id.ll_marriage);
        rl_marriageno = (RelativeLayout) findViewById(R.id.rl_marriageno);
        rl_marriageyes = (RelativeLayout) findViewById(R.id.rl_marriageyes);
        im_marriageno = (ImageView) findViewById(R.id.im_marriageno);
        im_marriageyes = (ImageView) findViewById(R.id.im_marriageyes);
        /*
         * 学历部分的id初始化
         */
        ll_education = (LinearLayout) findViewById(R.id.ll_education);
        rl_education01 = (RelativeLayout) findViewById(R.id.rl_education01);
        rl_education02 = (RelativeLayout) findViewById(R.id.rl_education02);
        rl_education03 = (RelativeLayout) findViewById(R.id.rl_education03);
        rl_education04 = (RelativeLayout) findViewById(R.id.rl_education04);
        im_education01 = (ImageView) findViewById(R.id.im_education01);
        im_education02 = (ImageView) findViewById(R.id.im_education02);
        im_education03 = (ImageView) findViewById(R.id.im_education03);
        im_education04 = (ImageView) findViewById(R.id.im_education04);
        /*
         * 职业部分的id初始化
         */
        lv_job = (ListView) findViewById(R.id.lv_job);
        itemposition = Integer.parseInt(user.getWork()) - 1;
        /*
         * 证券投资经验部分的id初始化
         */
        ll_invest = (LinearLayout) findViewById(R.id.ll_invest);
        rl_investyes = (RelativeLayout) findViewById(R.id.rl_investyes);
        rl_investno = (RelativeLayout) findViewById(R.id.rl_investno);
        im_investyes = (ImageView) findViewById(R.id.im_investyes);
        im_investno = (ImageView) findViewById(R.id.im_investno);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_emailsave:// 保存邮箱
                if (!StringHelper.isBlank(et_emailaddress.getText().toString().trim()))
                    saveInfo("email", et_emailaddress.getText().toString().trim());
                break;
            case R.id.bt_addresssave:// 保存通讯地址
                if (!StringHelper.isBlank(et_address.getText().toString().trim()))
                    saveInfo("addr", et_address.getText().toString().trim());
                break;
            case R.id.rl_marriageno:// 选择未婚
                setMarrView(0);
                saveInfo("married", "0");
                break;
            case R.id.rl_marriageyes:// 选择已婚
                setMarrView(1);
                saveInfo("married", "1");
                break;
            case R.id.rl_education01://
                setEduView(1);
                saveInfo("education", "1");
                break;
            case R.id.rl_education02://
                setEduView(2);
                saveInfo("education", "2");
                break;
            case R.id.rl_education03://
                setEduView(3);
                saveInfo("education", "3");
                break;
            case R.id.rl_education04://
                setEduView(4);
                saveInfo("education", "4");
                break;
            case R.id.rl_investyes://
                setInvestView(0);
                saveInfo("invest", "0");
                break;
            case R.id.rl_investno://
                setInvestView(1);
                saveInfo("invest", "1");
                break;
        }
    }

}
