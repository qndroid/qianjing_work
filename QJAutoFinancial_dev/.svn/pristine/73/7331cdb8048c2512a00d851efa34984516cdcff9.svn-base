
package com.qianjing.finance.view.quickitem;

import com.qianjing.finance.util.helper.StringHelper;
import com.qianjing.finance.util.helper.StringHelper.StringFormat;
import com.qjautofinancial.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**********************************************************
 * @文件名称：QuickItemLayout.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年5月28日 下午2:05:28
 * @文件描述：快速购买首页列表项
 * @修改历史：2015年5月28日创建初始版本
 **********************************************************/
public class QuickItemLayout extends RelativeLayout
{
    private Context context;
    private Resources res;
    private RelativeLayout mContentView;
    private TextView leftTextView;
    private TextView halYearRateView;
    private TextView minScriptView;
    private TextView fenxianTypeView;
    private TextView halYearRateTitleView;

    public QuickItemLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        res = context.getResources();
        initView();
    }

    public QuickItemLayout(Context context)
    {
        this(context, null);
    }

    private void initView()
    {

        mContentView = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.quick_item_layout, this);
        leftTextView = (TextView) mContentView.findViewById(R.id.left_bg);
        halYearRateView = (TextView) mContentView.findViewById(R.id.half_year_rate_view);
        minScriptView = (TextView) mContentView.findViewById(R.id.min_script_view);
        fenxianTypeView = (TextView) mContentView.findViewById(R.id.fen_xian_view);
        halYearRateTitleView = (TextView) findViewById(R.id.half_year_title_view);
    }

    public void changeStyle(int type, String titleName, double halYearRate, String minScript,
            String fenXianString)
    {

        switch (type)
        {

            case 1:
                leftTextView.setBackgroundResource(R.drawable.icon_jinqu);
                fenxianTypeView.setTextColor(res.getColor(R.color.color_ff985a));
                break;
            case 2:
                leftTextView.setBackgroundResource(R.drawable.icon_wenjian);
                fenxianTypeView.setTextColor(res.getColor(R.color.color_63cd99));
                break;
            case 3:
                leftTextView.setBackgroundResource(R.drawable.icon_baoshou);
                fenxianTypeView.setTextColor(res.getColor(R.color.color_7ea1de));
                break;
            case 4:
                leftTextView.setBackgroundResource(R.drawable.icon_baoshou);
                fenxianTypeView.setTextColor(res.getColor(R.color.color_7ea1de));
                break;
        }

        leftTextView.setText(titleName);
        halYearRateView.setText(StringHelper.formatString(
                String.valueOf(halYearRate), StringFormat.FORMATE_2));
        minScriptView.setText(context.getString(R.string.ren_ming_bi) + minScript
                + context.getString(R.string.qi_gou));
        fenxianTypeView.setText(fenXianString);
        halYearRateTitleView.setText(context.getString(R.string.half_year_rate));
    }
}
