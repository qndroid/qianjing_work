
package com.qianjing.finance.view.dialog;

import com.qianjing.finance.ui.activity.common.WebActivity;
import com.qjautofinancial.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @description 新手活动Dialog
 * @author majinxin
 * @date 2015年8月20日
 */
public class RedBagDialog extends Dialog
{
    /**
     * Data
     */
    private Context mContext;
    private String newUserURL;
    private String detailActivityURL;
    /**
     * UI
     */
    private ImageView mCloseView;
    private TextView mDetailView;
    private WebView mWebView;

    public RedBagDialog(Context context, String newUserURL, int theme)
    {
        super(context, theme);
        this.mContext = context;
        this.newUserURL = newUserURL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_activity_layout);
        initView();
    }

    private void initView()
    {
        mWebView = (WebView) findViewById(R.id.title_imag);
        mCloseView = (ImageView) findViewById(R.id.close_view);
        mCloseView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        mDetailView = (TextView) findViewById(R.id.look_detail_view);
        mDetailView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", detailActivityURL);
                intent.putExtra("webType", 8);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        mWebView.loadUrl(newUserURL);
    }

    public void setNewUserURL(String newUserURL)
    {
        this.newUserURL = newUserURL;
        mWebView.loadUrl(newUserURL);
    }

    public void setDetailURL(String detailURL)
    {
        this.detailActivityURL = detailURL;
    }
}
