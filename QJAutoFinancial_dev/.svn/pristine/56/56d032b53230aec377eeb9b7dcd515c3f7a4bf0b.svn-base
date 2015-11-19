
package com.qianjing.finance.ui.activity.assemble.aip.modify;

import com.qianjing.finance.constant.ErrorConst;
import com.qianjing.finance.model.aip.AIPDetail;
import com.qianjing.finance.model.aip.AIPFundItem;
import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.net.connection.UrlConst;
import com.qianjing.finance.ui.Const;
import com.qianjing.finance.ui.activity.assemble.aip.chargedetail.AIPChargeDetailActivity;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.helper.DateFormatHelper;
import com.qianjing.finance.util.helper.DateFormatHelper.DateFormat;
import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qianjing.finance.view.assembledetailview.AssembleReasonItemLayout;
import com.qianjing.finance.view.dialog.TimingPauseDialog;
import com.qjautofinancial.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @description 组合定投详情页面
 * @author renzhiqiang
 * @date 2015年8月6日
 */
public class AssembleAIPDetailActivity extends BaseActivity implements OnClickListener
{
    /**
     * common
     */
    private static final String AIP_STATE_NORMAL = "1";
    private static final String AIP_STATE_PAUSE = "2";
    private static final String AIP_STATE_STOP = "3";
    private static final String AIP_STATE_FAILED = "4";
    /**
     * UI
     */
    private Button mBackButton;
    private TextView mKouKuanDetailView;
    private TextView mFeeChargeView;
    private TextView mPaidWayView;
    private TextView mCardView;
    private TextView mOnceInvestmentView;
    private TextView mTimingRepeatView;
    private TextView mTimingDateView;
    private TextView mStopDateView;
    private ImageView mStatusView;
    private TextView mModifyView;
    private TextView mPauseView;
    private TextView mAbortView;
    private LinearLayout mOperationLayout;
    private LinearLayout mContentLayout;
    private TimingPauseDialog pauseDialog;
    /**
     * data
     */
    private AIPDetail mAipDetail;
    private String sid;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_aip_detail);
        initData();
        initView();

        requestAipDetail();
    }

    private void initData()
    {
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
    }

    private void initView()
    {
        setTitleWithId(R.string.title_aip_detail);
        setLoadingUncancelable();
        mBackButton = (Button) findViewById(R.id.bt_back);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(this);

        mKouKuanDetailView = (TextView) findViewById(R.id.right_title_view);
        mKouKuanDetailView.setVisibility(View.VISIBLE);
        mKouKuanDetailView.setText(getString(R.string.cut_money_detail));
        mKouKuanDetailView.setOnClickListener(this);

        mFeeChargeView = (TextView) findViewById(R.id.timing_charge_way_value);
        mPaidWayView = (TextView) findViewById(R.id.timing_paid_way_value);
        mCardView = (TextView) findViewById(R.id.timing_card_value);
        mOnceInvestmentView = (TextView) findViewById(R.id.timing_once_investment_value);
        mTimingRepeatView = (TextView) findViewById(R.id.timing_modify_repeat_value);
        mTimingDateView = (TextView) findViewById(R.id.timing_modify_date_value);
        mStopDateView = (TextView) findViewById(R.id.timing_stop_date_value);
        mStatusView = (ImageView) findViewById(R.id.status_view);
        mModifyView = (TextView) findViewById(R.id.modify_view);
        mPauseView = (TextView) findViewById(R.id.modify_pause);
        mAbortView = (TextView) findViewById(R.id.modify_abort);
        mOperationLayout = (LinearLayout) findViewById(R.id.operation_layout);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout); // 使用sebmleReasonItemLayout
        mModifyView.setOnClickListener(this);
        mPauseView.setOnClickListener(this);
        mAbortView.setOnClickListener(this);
    }

    private void requestAipDetail()
    {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sid", sid);

        AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_AIP_DETAIL, new HttpCallBack()
        {
            @Override
            public void back(String data, int status)
            {
                if (data != null && !data.equals(""))
                {
                    mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
                }
                else
                {
                    dismissLoading();
                    showHintDialog("网络不给力，请重试!");
                }
            }
        }, upload);
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    handleAipDetail(msg.obj.toString());
                    break;
            }
        };
    };

    private void handleAipDetail(String data)
    {
        dismissLoading();
        try
        {
            JSONObject object = new JSONObject(data);
            int ecode = object.optInt("ecode");
            String reason = object.optString("emsg");
            switch (ecode)
            {
                case ErrorConst.EC_OK:
                    JSONObject result = object.optJSONObject("data");
                    JSONArray array = result.getJSONArray("list");
                    JSONObject detail = result.optJSONObject("detail");
                    if (array != null && array.length() > 0)
                    {
                        mAipDetail = new AIPDetail();
                        mAipDetail.sdid = detail.optString("sdid");
                        mAipDetail.state = detail.optString("state");
                        mAipDetail.sid = detail.optString("sid");
                        mAipDetail.ctime = detail.optString("ctime");
                        mAipDetail.stop_date = detail.optString("stop_date");
                        mAipDetail.month_sum = detail.optString("month_sum");
                        mAipDetail.day = detail.optString("day");
                        mAipDetail.bank = detail.optString("bank");
                        mAipDetail.card = detail.optString("card");
                        mAipDetail.first_date = detail.optString("first_date");
                        mAipDetail.sname = detail.optString("sname");
                        mAipDetail.next_day = detail.optString("next_day");
                        mAipDetail.min_sum = detail.optString("min_sum");

                        ArrayList<AIPFundItem> mListData = new ArrayList<AIPFundItem>();
                        for (int i = 0; i < array.length(); i++)
                        {
                            JSONObject jobj = array.getJSONObject(i);
                            AIPFundItem item = new AIPFundItem();
                            item.abbrev = jobj.optString("abbrev");
                            item.sid = jobj.optString("sid");
                            item.state = jobj.optString("state");
                            item.month_sum = jobj.optString("month_sum");
                            item.reason = jobj.optString("reason");
                            item.id = jobj.optString("id");
                            mListData.add(item);
                        }
                        mAipDetail.aipItem = mListData;
                        updateUI();
                    }
                    break;
                case ErrorConst.EC_PASSWD_DISMATCH:
                    showHintDialog(reason);
                    break;
                default:
                    break;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            showHintDialog("数据解析异常!");
        }
    }

    private void updateUI()
    {
        mCardView.setText(mAipDetail.bank + getString(R.string.left_brackets) + getString(R.string.wei_hao)
                + StringHelper.showCardLast4(mAipDetail.card) + getString(R.string.right_brackets));
        mOnceInvestmentView.setText(getString(R.string.ren_ming_bi) + mAipDetail.month_sum);
        mTimingDateView.setText(mAipDetail.day + getString(R.string.timing_modify_day));
        mStopDateView.setText(DateFormatHelper.formatDate(mAipDetail.stop_date.concat("000"), DateFormat.DATE_12));

        // switch (mAipDetail.state)
        // {
        // case AIP_STATE_FAILED:
        // mStatusView.setBackgroundResource(R.drawable.icon_timing_failed_statue);
        // mContentLayout.removeAllViews();
        // for (int i = 0; i < mAipDetail.aipItem.size(); i++)
        // {
        // mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
        // StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
        // getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
        // }
        // mOperationLayout.setVisibility(View.GONE);
        // break;
        // case AIP_STATE_STOP:
        // mStatusView.setBackgroundResource(R.drawable.icon_timing_pause_end);
        // mContentLayout.removeAllViews();
        // for (int i = 0; i < mAipDetail.aipItem.size(); i++)
        // {
        // mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
        // StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
        // getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
        // }
        //
        // mOperationLayout.setVisibility(View.GONE);
        // break;
        // case AIP_STATE_PAUSE:
        // mStatusView.setBackgroundResource(R.drawable.icon_timing_pause_status);
        // mContentLayout.removeAllViews();
        // for (int i = 0; i < mAipDetail.aipItem.size(); i++)
        // {
        // mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
        // StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
        // getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
        // }
        // isPause = true;
        // /**
        // * isPuase = true,按钮为恢复功能
        // */
        // mPauseView.setText(getString(R.string.aip_resume_state));
        // break;
        // case AIP_STATE_NORMAL:
        // mStatusView.setBackgroundResource(R.drawable.icon_timing_normal_statue);
        // mContentLayout.removeAllViews();
        // for (int i = 0; i < mAipDetail.aipItem.size(); i++)
        // {
        // mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
        // StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
        // DateFormatHelper.formatDate(mAipDetail.first_date.concat("000"), DateFormat.DATE_7),
        // mAipDetail.aipItem.get(i).reason));
        // }
        //
        // isPause = false;
        // /**
        // * isPause = false,按钮为暂停功能
        // */
        // mPauseView.setText(getString(R.string.timing_pause));
        // break;
        // }
        /**
         * 失败状态，重新发起定投
         */
        if (mAipDetail.state.equals(AIP_STATE_FAILED))
        {
            mStatusView.setBackgroundResource(R.drawable.icon_timing_pause_end);
            mContentLayout.removeAllViews();
            for (int i = 0; i < mAipDetail.aipItem.size(); i++)
            {
                mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
                        StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
                        getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
            }
            mOperationLayout.setVisibility(View.GONE);
        }

        /**
         * 为停止转态 ，不显示操作按钮
         */
        if (mAipDetail.state.equals(AIP_STATE_STOP))
        {
            mStatusView.setBackgroundResource(R.drawable.icon_timing_pause_end);
            mContentLayout.removeAllViews();
            for (int i = 0; i < mAipDetail.aipItem.size(); i++)
            {
                mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
                        StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
                        getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
            }

            mOperationLayout.setVisibility(View.GONE);
        }
       
        /**
         * 当前组合为暂停态
         */
        if (mAipDetail.state.equals(AIP_STATE_PAUSE))
        {
            mStatusView.setBackgroundResource(R.drawable.icon_timing_pause_status);
            mContentLayout.removeAllViews();
            for (int i = 0; i < mAipDetail.aipItem.size(); i++)
            {
                mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
                        StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
                        getString(R.string.default_number), mAipDetail.aipItem.get(i).reason));
            }
            isPause = true;
            /**
             * isPuase = true,按钮为恢复功能
             */
            mPauseView.setText(getString(R.string.aip_resume_state));
        }
        /**
         * 当前组合为成功状态
         */
        if (mAipDetail.state.equals(AIP_STATE_NORMAL))
        {
            mStatusView.setBackgroundResource(R.drawable.icon_timing_normal_statue);
            mContentLayout.removeAllViews();
            for (int i = 0; i < mAipDetail.aipItem.size(); i++)
            {
                mContentLayout.addView(addFundItem(mAipDetail.aipItem.get(i).abbrev,
                        StringHelper.formatString(mAipDetail.aipItem.get(i).month_sum, StringFormat.FORMATE_2),
                        DateFormatHelper.formatDate(mAipDetail.first_date.concat("000"), DateFormat.DATE_7),
                        mAipDetail.aipItem.get(i).reason));
            }

            isPause = false;
            /**
             * isPause = false,按钮为暂停功能
             */
            mPauseView.setText(getString(R.string.timing_pause));
        }
    }

    /**
     * 转化为想转化的组合状态
     */
    private void requestChageState(String state, String password)
    {
        showLoading();
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("sdid", mAipDetail.sdid);
        upload.put("state", state);
        upload.put("passwd", password);
        AnsynHttpRequest.requestByPost(this, UrlConst.ASSEMBLE_AIP_CHANGE_STATE, new HttpCallBack()
        {
            @Override
            public void back(String data, int status)
            {
                if (data != null && !data.equals(""))
                {
                    dismissLoading();
                    mHandler.sendMessage(mHandler.obtainMessage(0x01, data));
                }
                else
                {
                    dismissLoading();
                    showHintDialog("网络不给力，请重试!");
                }
            }
        }, upload);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.modify_view:
                Intent intent = new Intent(this, AssembleAIPModifyActivity.class);
                intent.putExtra("AIPDetail", mAipDetail);
                startActivityForResult(intent, Const.ASSEMBLE_TIMING_FLOW);
                break;
            case R.id.modify_pause:
                if (isPause)
                {
                    showPwdDialog(getString(R.string.aip_resume_title), getString(R.string.aip_resume_title_info),
                            AIP_STATE_NORMAL);
                }
                else
                {
                    showPwdDialog(getString(R.string.timing_pause_statue), getString(R.string.timing_pause_statue_info),
                            AIP_STATE_PAUSE);
                }
                pauseDialog.show();
                break;
            case R.id.modify_abort:
                showPwdDialog(getString(R.string.aip_abort_title), getString(R.string.aip_abort_title_info), AIP_STATE_STOP);
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.right_title_view:
                Intent chargeIntent = new Intent(this, AIPChargeDetailActivity.class);
                chargeIntent.putExtra("sid", mAipDetail.sid);
                startActivity(chargeIntent);
                break;
        }
    }

    /**
     * 修改定投转态是密码提示框
     */
    private void showPwdDialog(final String title, final String content, final String state)
    {
        pauseDialog = new TimingPauseDialog(this, title, content);
        pauseDialog.setConfirmListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pauseDialog.dismiss();
                requestChageState(state, pauseDialog.getPassword());
            }
        });
        pauseDialog.show();
    }

    private AssembleReasonItemLayout addFundItem(String fundName, String money, String date, String reason)
    {
        AssembleReasonItemLayout layout = new AssembleReasonItemLayout(this);
        layout.initData(fundName, money, date, reason);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        return layout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case Const.ASSEMBLE_TIMING_FLOW:
                if (resultCode == RESULT_OK)
                {
                    finish();
                }
                break;
        }
    }
}
